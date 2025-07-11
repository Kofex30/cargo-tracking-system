package com.example.cargo_app_3.ui.theme.ercancam.cargotracking.shipments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.cargo_app_3.R;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.ApiService;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.RetrofitClient;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.adapter.ShipmentListAdapter;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.model.Shipment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllShipmentsFragment extends Fragment {

    private ListView listView;
    private List<Shipment> shipmentList = new ArrayList<>();
    private ShipmentListAdapter adapter;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_shipments, container, false);

        listView = view.findViewById(R.id.lvShipments);
        adapter = new ShipmentListAdapter(getContext(), shipmentList);
        listView.setAdapter(adapter);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        loadShipments();

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Shipment shipment = shipmentList.get(position);
            if ("CREATED".equalsIgnoreCase(shipment.getStatus()) && shipment.getId() != null) {
                Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                intent.putExtra("shipmentId", shipment.getId().longValue());
                startActivity(intent);
            }
        });


        return view;
    }

    private void loadShipments() {
        apiService.getAllShipments().enqueue(new Callback<List<Shipment>>() {
            @Override
            public void onResponse(Call<List<Shipment>> call, Response<List<Shipment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    shipmentList.clear();
                    for (Shipment s : response.body()) {
                        if ("CREATED".equalsIgnoreCase(s.getStatus()))
                            shipmentList.add(s);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Shipment>> call, Throwable t) {
                Toast.makeText(getContext(), "Siparişler yüklenemedi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

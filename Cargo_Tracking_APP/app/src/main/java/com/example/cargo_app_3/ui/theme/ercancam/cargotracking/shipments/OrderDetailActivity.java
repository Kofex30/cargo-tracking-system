package com.example.cargo_app_3.ui.theme.ercancam.cargotracking.shipments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cargo_app_3.R;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.ApiService;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.RetrofitClient;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.model.Shipment;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView tvSender, tvReceiver, tvOrigin, tvDestination, tvStatus;
    private Button btnAccept;
    private ApiService apiService;
    private Long shipmentId;
    private Shipment currentShipment;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // ActionBar’da geri butonunu aktif et
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvSender = findViewById(R.id.tvSender);
        tvReceiver = findViewById(R.id.tvReceiver);
        tvOrigin = findViewById(R.id.tvOrigin);
        tvDestination = findViewById(R.id.tvDestination);
        tvStatus = findViewById(R.id.tvStatus);
        btnAccept = findViewById(R.id.btnAccept);

        shipmentId = getIntent().getLongExtra("shipmentId", -1L);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        loadShipmentDetail();

        btnAccept.setOnClickListener(v -> {
            if (currentShipment != null && "CREATED".equalsIgnoreCase(currentShipment.getStatus())) {
                Map<String, String> statusMap = new HashMap<>();
                statusMap.put("status", "IN_TRANSIT");

                apiService.updateShipmentStatus(shipmentId, statusMap).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(OrderDetailActivity.this, "Sipariş kabul edildi", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(OrderDetailActivity.this, QrCodeActivity.class);
                            intent.putExtra("qrCode", currentShipment.getQrCode());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(OrderDetailActivity.this, "Güncelleme başarısız", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(OrderDetailActivity.this, "Sunucu hatası: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // ActionBar’daki geri butonuna tıklanmasını yakala
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadShipmentDetail() {
        apiService.getShipmentById(shipmentId).enqueue(new Callback<Shipment>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Shipment> call, Response<Shipment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentShipment = response.body();
                    tvSender.setText("Gönderen: " + currentShipment.getSenderName());
                    tvReceiver.setText("Alıcı: " + currentShipment.getReceiverName());
                    tvOrigin.setText("Başlangıç: " + currentShipment.getOriginAddress());
                    tvDestination.setText("Varış: " + currentShipment.getDestinationAddress());
                    tvStatus.setText("Durum: " + currentShipment.getStatus());
                }
            }

            @Override
            public void onFailure(Call<Shipment> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Detaylar yüklenemedi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.cargo_app_3.ui.theme.ercancam.cargotracking.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.example.cargo_app_3.R;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.model.Shipment;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.shipments.OrderDetailActivity;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.shipments.QrCodeActivity;

import java.util.List;

public class ShipmentListAdapter extends ArrayAdapter<Shipment> {
    private Context context;
    private List<Shipment> shipments;

    public ShipmentListAdapter(Context context, List<Shipment> shipments) {
        super(context, 0, shipments);
        this.context = context;
        this.shipments = shipments;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Shipment shipment = shipments.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_shipment, parent, false);
        }

        TextView tvSender = convertView.findViewById(R.id.tvSender);
        TextView tvReceiver = convertView.findViewById(R.id.tvReceiver);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);

        tvSender.setText("Gönderen: " + shipment.getSenderName());
        tvReceiver.setText("Alıcı: " + shipment.getReceiverName());
        tvStatus.setText("Durum: " + shipment.getStatus());

        convertView.setOnClickListener(v -> {
            if ("IN_TRANSIT".equalsIgnoreCase(shipment.getStatus()) && shipment.getQrCode() != null) {
                Intent intent = new Intent(context, QrCodeActivity.class);
                intent.putExtra("qrCode", shipment.getQrCode());
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("shipmentId", shipment.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}

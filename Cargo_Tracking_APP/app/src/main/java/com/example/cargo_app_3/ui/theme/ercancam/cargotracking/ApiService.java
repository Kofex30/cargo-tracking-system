package com.example.cargo_app_3.ui.theme.ercancam.cargotracking;

import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.model.CourierDto;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.model.Shipment;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @GET("couriers/{id}")
    Call<CourierDto> getCourierById(@Path("id") Long courierId);

    @GET("shipments")
    Call<List<Shipment>> getAllShipments();

    @PUT("shipments/deliver/{qrCode}")
    Call<String> markAsDelivered(@Path("qrCode") String qrCode);


    @GET("shipments/{id}")
    Call<Shipment> getShipmentById(@Path("id") Long shipmentId);

    @PUT("shipments/{id}/status")
    Call<Void> updateShipmentStatus(@Path("id") Long shipmentId, @Body Map<String, String> statusBody);

}


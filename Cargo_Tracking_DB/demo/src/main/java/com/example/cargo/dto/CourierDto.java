package com.example.cargo.dto;

import com.example.cargo.model.Shipment;

import java.util.List;

public class CourierDto {

    private Long id;
    private String name;
    private List<Shipment> shipments;

    public CourierDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }
}

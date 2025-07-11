package com.example.cargo.service;

import com.example.cargo.dto.CourierDto;
import com.example.cargo.model.Courier;
import com.example.cargo.model.Shipment;
import com.example.cargo.model.ShipmentStatus;
import com.example.cargo.repository.CourierRepository;
import com.example.cargo.repository.ShipmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourierService {

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;
    @Autowired
    private ObjectMapper objectMapper;

    // 📌 Gönderiye kurye atama
    public void assignCourierToShipment(Long shipmentId, Long courierId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found"));

        shipment.setCourier(courier);
        shipment.setStatus(ShipmentStatus.OUT_FOR_DELIVERY);
        shipmentRepository.save(shipment);
    }

    // ➕ Yeni kurye ekle
    public Long createOrUpdateCourier(CourierDto courierDto) {
        Courier newCourier = new Courier();
        if(courierDto.getId() != null) {
            newCourier = courierRepository.findById(courierDto.getId()).orElseThrow(
                    () -> new RuntimeException("Courier not found")
            );
        }
        newCourier.setName(courierDto.getName());
        newCourier.setShipments(courierDto.getShipments());

        courierRepository.save(newCourier);

        return newCourier.getId();
    }

    // 📋 Tüm kuryeleri getir
    public List<CourierDto> getAllCouriers() {
        return courierRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    // 📦 Kurye üzerindeki tüm gönderileri getir
    public List<Shipment> getCourierShipments(Long courierId) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found"));

        return courier.getShipments();
    }
    public CourierDto getCourierById(Long id) {
        Courier courier = courierRepository.findById(id).orElseThrow(() -> new RuntimeException("Courıer not found"));
        return toDto(courier);
    }


    public CourierDto toDto(Courier courier) {
        return objectMapper.convertValue(courier, CourierDto.class);
    }

}

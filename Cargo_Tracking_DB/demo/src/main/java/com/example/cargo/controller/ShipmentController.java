package com.example.cargo.controller;

import com.example.cargo.dto.ShipmentDto;
import com.example.cargo.model.Courier;
import com.example.cargo.model.Shipment;
import com.example.cargo.model.ShipmentStatus;
import com.example.cargo.service.CourierService;
import com.example.cargo.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private CourierService courierService;

    @PostMapping
    public ResponseEntity<ShipmentDto> createShipment(@RequestBody ShipmentDto shipmentDto) {
        Shipment shipment = toEntity(shipmentDto);
        Shipment created = shipmentService.createShipment(shipment);
        ShipmentDto responseDto = shipmentService.toDto(created);
        return ResponseEntity.ok(responseDto);
    }
    private Shipment toEntity(ShipmentDto dto) {
        Shipment shipment = new Shipment();
        shipment.setOriginAddress(dto.getOriginAddress());
        shipment.setDestinationAddress(dto.getDestinationAddress());
        shipment.setSenderName(dto.getSenderName());
        shipment.setReceiverName(dto.getReceiverName());

        if (dto.getCourierId() != null) {
            Courier courier = new Courier();
            courier.setId(dto.getCourierId());
            shipment.setCourier(courier);
        }
        return shipment;
    }
    @GetMapping
    public ResponseEntity<List<ShipmentDto>> getAllShipments() {
        List<ShipmentDto> shipments = shipmentService.getAllShipments();
        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDto> getShipmentById(@PathVariable Long id) {
        Optional<ShipmentDto> shipmentDto = shipmentService.getShipmentById(id);
        return shipmentDto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/courier/{courierId}")
    public ResponseEntity<List<ShipmentDto>> getShipmentsByCourier(@PathVariable Long courierId) {
        List<ShipmentDto> shipments = shipmentService.getShipmentsByCourier(courierId);
        return ResponseEntity.ok(shipments);
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        String statusString = statusMap.get("status");
        try {
            ShipmentStatus status = ShipmentStatus.valueOf(statusString.toUpperCase());
            shipmentService.updateShipmentStatus(id, status);
            return ResponseEntity.ok("Gönderi durumu güncellendi.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Geçersiz durum: " + statusString);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{shipmentId}/assign-courier/{courierId}")
    public ResponseEntity<String> assignCourierToShipment(
            @PathVariable Long shipmentId,
            @PathVariable Long courierId) {
        try {
            courierService.assignCourierToShipment(shipmentId, courierId);
            return ResponseEntity.ok("Kurye başarıyla atandı!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/qr/{qrCode}")
    public ResponseEntity<ShipmentDto> getShipmentByQrCode(@PathVariable String qrCode) {
        Optional<ShipmentDto> shipmentDto = shipmentService.getShipmentByQrCode(qrCode).map(shipmentService::toDto);
        return shipmentDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/deliver/confirm/{qrCode}")
    public ResponseEntity<String> confirmDeliveryGet(@PathVariable String qrCode) {
        boolean confirmed = shipmentService.confirmDelivery(qrCode);
        if (confirmed) {
            return ResponseEntity.ok("Teslimat başarıyla onaylandı.");
        } else {
            return ResponseEntity.badRequest().body("Teslimat onaylanamadı veya zaten teslim edilmiş.");
        }
    }
    @PutMapping("/deliver/{qrCode}")
    public ResponseEntity<String> markAsDelivered(@PathVariable String qrCode) {
        boolean confirmed = shipmentService.confirmDelivery(qrCode);
        if (confirmed) {
            return ResponseEntity.ok("Teslimat başarıyla onaylandı.");
        } else {
            return ResponseEntity.badRequest().body("Teslimat onaylanamadı veya zaten teslim edilmiş.");
        }
    }
    @PutMapping("/{shipmentId}/accept/{courierId}")
    public ResponseEntity<String> acceptShipment(
            @PathVariable Long shipmentId,
            @PathVariable Long courierId) {
        boolean accepted = shipmentService.acceptShipment(shipmentId, courierId);
        if (accepted) {
            return ResponseEntity.ok("Gönderi başarıyla kabul edildi.");
        } else {
            return ResponseEntity.badRequest().body("Gönderi kabul edilemedi.");
        }
    }
}

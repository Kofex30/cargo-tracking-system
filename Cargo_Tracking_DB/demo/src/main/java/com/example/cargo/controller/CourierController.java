package com.example.cargo.controller;

import com.example.cargo.dto.CourierDto;
import com.example.cargo.model.Courier;
import com.example.cargo.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/couriers")
public class CourierController {
    @Autowired
    private CourierService courierService;
    // Yeni kurye ekle
    @PostMapping
    public ResponseEntity<Long> createCourier(@RequestBody CourierDto courierDto) {
        return ResponseEntity.ok(courierService.createOrUpdateCourier(courierDto));
    }
    // Tüm kuryeleri getir
    @GetMapping
    public ResponseEntity<List<CourierDto>> getAllCouriers() {
        return ResponseEntity.ok(courierService.getAllCouriers());
    }
    // Belirli kuryenin görevdeki gönderileri
    @GetMapping("/{id}/shipments")
    public ResponseEntity<List<?>> getCourierShipments(@PathVariable Long id) {
        return ResponseEntity.ok(courierService.getCourierShipments(id));
    }
    // Belirli kurye bilgisi
    @GetMapping("/{id}")
    public ResponseEntity<CourierDto> getCourierById(@PathVariable Long id) {
        var response = courierService.getCourierById(id);
        return ResponseEntity.ok(response);
    }
    // Kurye atama endpoint'i
    @PutMapping("/assign")
    public ResponseEntity<String> assignCourierToShipment(@RequestParam Long shipmentId, @RequestParam Long courierId) {
        try {
            courierService.assignCourierToShipment(shipmentId, courierId);
            return ResponseEntity.ok("Kurye başarıyla gönderiye atandı.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

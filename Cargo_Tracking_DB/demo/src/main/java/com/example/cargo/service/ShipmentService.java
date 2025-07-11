package com.example.cargo.service;

import com.example.cargo.dto.ShipmentDto;
import com.example.cargo.model.Courier;
import com.example.cargo.model.Shipment;
import com.example.cargo.model.ShipmentStatus;
import com.example.cargo.repository.CourierRepository;
import com.example.cargo.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private NotificationService notificationService;  // Bildirim servisi

    @Autowired
    private QRCodeService qrCodeService;  // QR kod üreteci

    // Yeni gönderi oluşturulurken QR kod üretip, durum atıyoruz
    public Shipment createShipment(Shipment shipment) {
        shipment.setStatus(ShipmentStatus.CREATED);
        shipment.setCreatedAt(LocalDateTime.now());
        // UUID ile benzersiz QR kod üret
        shipment.setQrCode(UUID.randomUUID().toString());
        return shipmentRepository.save(shipment);
    }

    // Tüm gönderileri DTO listesi olarak al
    public List<ShipmentDto> getAllShipments() {
        List<Shipment> shipments = shipmentRepository.findAll();
        return shipments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ID'ye göre gönderi bul ve DTO dön
    public Optional<ShipmentDto> getShipmentById(Long id) {
        return shipmentRepository.findById(id)
                .map(this::toDto);
    }

    public Optional<Shipment> getShipmentByQrCode(String qrCode) {
        return shipmentRepository.findByQrCode(qrCode);
    }

    // Kuryeye ait ve durumları CREATED veya IN_TRANSIT olan gönderileri DTO olarak getir
    public List<ShipmentDto> getShipmentsByCourier(Long courierId) {
        List<Shipment> shipments = shipmentRepository.findByCourier_IdAndStatusIn(
                courierId,
                List.of(ShipmentStatus.CREATED, ShipmentStatus.IN_TRANSIT)
        );
        return shipments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Shipment> getShipmentsByStatus(ShipmentStatus status) {
        return shipmentRepository.findByStatus(status);
    }

    public boolean acceptShipment(Long shipmentId, Long courierId) {
        Optional<Shipment> optShipment = shipmentRepository.findById(shipmentId);
        if (optShipment.isEmpty()) return false;

        Shipment shipment = optShipment.get();

        if (shipment.getStatus() != ShipmentStatus.CREATED) {
            return false; // zaten kabul edilmiş veya başka durumda
        }

        // Kurye atanıyor
        Courier courier = courierRepository.findById(courierId).orElse(null);
        if (courier == null) return false;

        shipment.setCourier(courier);
        shipment.setStatus(ShipmentStatus.IN_TRANSIT);
        shipmentRepository.save(shipment);

        String message = "Gönderi #" + shipmentId + " kuryeniz tarafından kabul edildi.";
        notificationService.sendNotification(message);

        return true;
    }

    // Gönderi durumunu güncelleme (eski fonksiyonun korunması)
    public Shipment updateShipmentStatus(Long id, ShipmentStatus newStatus) {
        Optional<Shipment> optionalShipment = shipmentRepository.findById(id);
        if (optionalShipment.isPresent()) {
            Shipment shipment = optionalShipment.get();
            shipment.setStatus(newStatus);
            Shipment updatedShipment = shipmentRepository.save(shipment);

            // Durum güncellendiğinde bildirim gönder
            String notificationMessage = "Gönderi #" + id + " durumu güncellendi: " + newStatus.name();
            notificationService.sendNotification(notificationMessage);

            return updatedShipment;
        } else {
            throw new RuntimeException("Shipment not found with id: " + id);
        }
    }

    // QR kod ile teslimat onayı (müşteri telefondan okuttuğunda)
    public boolean confirmDelivery(String qrCode) {
        Optional<Shipment> optShipment = shipmentRepository.findByQrCode(qrCode);

        if (optShipment.isEmpty()) {
            // QR kod ile kargo bulunamadı
            return false;
        }

        Shipment shipment = optShipment.get();

        if (shipment.getStatus() == ShipmentStatus.DELIVERED) {
            // Zaten teslim edilmiş
            return false;
        }

        shipment.setStatus(ShipmentStatus.DELIVERED);
        shipment.setDeliveredAt(LocalDateTime.now()); // teslim zamanı kaydı (modelde deliveredAt alanı olmalı)

        shipmentRepository.save(shipment);

        // Bildirim gönder (kurye ve sistem için)
        String message = "Gönderi #" + shipment.getId() + " başarıyla teslim edildi.";
        notificationService.sendNotification(message);

        return true;
    }

    // Shipment -> ShipmentDto dönüşümü
    public ShipmentDto toDto(Shipment shipment) {
        ShipmentDto dto = new ShipmentDto();
        dto.setId(shipment.getId());
        dto.setOriginAddress(shipment.getOriginAddress());
        dto.setDestinationAddress(shipment.getDestinationAddress());
        dto.setStatus(shipment.getStatus());
        dto.setCreatedAt(shipment.getCreatedAt());
        dto.setQrCode(shipment.getQrCode());
        dto.setCourierId(shipment.getCourier() != null ? shipment.getCourier().getId() : null);
        dto.setSenderName(shipment.getSenderName());
        dto.setReceiverName(shipment.getReceiverName());

        return dto;
    }

}

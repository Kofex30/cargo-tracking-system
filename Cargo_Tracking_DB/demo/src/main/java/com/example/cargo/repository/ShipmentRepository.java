package com.example.cargo.repository;

import com.example.cargo.model.Shipment;
import com.example.cargo.model.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findByCourier_IdAndStatusIn(Long courierId, List<ShipmentStatus> statuses);
    Optional<Shipment> findByQrCode(String qrCode);
    List<Shipment> findByStatus(ShipmentStatus status);





}

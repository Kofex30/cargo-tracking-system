package com.example.cargo.dto;

import com.example.cargo.model.ShipmentStatus;

import java.time.LocalDateTime;

public class ShipmentDto {
    private Long id;
    private String originAddress;
    private String destinationAddress;
    private ShipmentStatus status;
    private LocalDateTime createdAt;
    private Long courierId;
    private String qrCode;

    private String senderName;      // <-- eklendi
    private String receiverName;    // <-- eklendi

    // Getter ve Setter metodları

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOriginAddress() { return originAddress; }
    public void setOriginAddress(String originAddress) { this.originAddress = originAddress; }

    public String getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }

    public ShipmentStatus getStatus() { return status; }
    public void setStatus(ShipmentStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getCourierId() { return courierId; }
    public void setCourierId(Long courierId) { this.courierId = courierId; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
}

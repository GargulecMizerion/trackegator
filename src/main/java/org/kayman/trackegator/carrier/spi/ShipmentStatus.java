package org.kayman.trackegator.carrier.spi;

public enum ShipmentStatus {
    CREATED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    READY_FOR_PICKUP,
    DELIVERED,
    RETURNED,
    CANCELED,
    EXCEPTION,
    UNKNOWN
}

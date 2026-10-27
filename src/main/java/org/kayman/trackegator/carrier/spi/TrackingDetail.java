package org.kayman.trackegator.carrier.spi;

import java.time.OffsetDateTime;

public record TrackingDetail(ShipmentStatus status, String rawStatus, OffsetDateTime occurredAt) {}

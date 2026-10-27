package org.kayman.trackegator.carrier.spi;

import java.util.List;

public interface ParcelTracking {

    List<TrackingDetail> track(String number);
}

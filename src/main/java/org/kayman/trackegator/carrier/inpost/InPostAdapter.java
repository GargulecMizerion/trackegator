package org.kayman.trackegator.carrier.inpost;

import org.kayman.trackegator.carrier.inpost.networking.InPostApi;
import org.kayman.trackegator.carrier.inpost.networking.InPostTrackingResponse;
import org.kayman.trackegator.carrier.spi.ParcelTracking;
import org.kayman.trackegator.carrier.spi.TrackingDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class InPostAdapter implements ParcelTracking {

    private final InPostApi api;

    public InPostAdapter(InPostApi api) {
        this.api = api;
    }

    @Override
    public List<TrackingDetail> track(String number) {
        InPostTrackingResponse response;
        try {
            response = api.track(number);
        } catch (HttpClientErrorException.NotFound e) {
            return List.of();
        }

        if (response == null || response.details() == null) {
            return List.of();
        }
        return response.details().stream()
                .map(InPostAdapter::toTrackingDetail)
                .toList();
    }

    private static TrackingDetail toTrackingDetail(InPostTrackingResponse.Detail detail) {
        return new TrackingDetail(
                InPostStatusMapper.map(detail.status()),
                detail.status(),
                detail.datetime()
        );
    }
}

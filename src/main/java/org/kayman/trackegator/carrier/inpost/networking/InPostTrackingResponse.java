package org.kayman.trackegator.carrier.inpost.networking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record InPostTrackingResponse(
        @JsonProperty("tracking_number") String trackingNumber,
        String status,
        @JsonProperty("tracking_details") List<Detail> details) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Detail(
            String status,
            @JsonProperty("origin_status") String originStatus,
            // keep InPost's original offset instead of normalizing to UTC
            @JsonFormat(without = JsonFormat.Feature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            OffsetDateTime datetime) {}
}

package org.kayman.trackegator.carrier.inpost;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kayman.trackegator.carrier.spi.ShipmentStatus;
import org.kayman.trackegator.carrier.spi.TrackingDetail;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class InPostAdapterTest {

    private static final String BASE_URL = "https://inpost.test";
    private static final String NUMBER = "620000000000000000000001";

    private MockRestServiceServer server;
    private InPostAdapter adapter;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        adapter = new InPostAdapter(InPostConfig.createApi(builder, BASE_URL));
    }

    @Test
    void mapsTrackingHistory() {
        server.expect(requestTo(BASE_URL + "/v1/tracking/" + NUMBER))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "tracking_number": "620000000000000000000001",
                          "service": "inpost_locker_standard",
                          "status": "delivered",
                          "tracking_details": [
                            {"status": "delivered", "origin_status": "DOR", "agency": null,
                             "datetime": "2026-10-20T14:03:11.000+02:00"},
                            {"status": "ready_to_pickup", "origin_status": "WPA", "agency": null,
                             "datetime": "2026-10-20T09:12:45.000+02:00"},
                            {"status": "something_new", "origin_status": "XXX", "agency": null,
                             "datetime": "2026-10-19T18:00:00.000+02:00"}
                          ],
                          "created_at": "2026-10-18T10:00:00.000+02:00"
                        }
                        """, MediaType.APPLICATION_JSON));

        List<TrackingDetail> details = adapter.track(NUMBER);

        assertThat(details).containsExactly(
                new TrackingDetail(ShipmentStatus.DELIVERED, "delivered",
                        OffsetDateTime.parse("2026-10-20T14:03:11+02:00")),
                new TrackingDetail(ShipmentStatus.READY_FOR_PICKUP, "ready_to_pickup",
                        OffsetDateTime.parse("2026-10-20T09:12:45+02:00")),
                new TrackingDetail(ShipmentStatus.UNKNOWN, "something_new",
                        OffsetDateTime.parse("2026-10-19T18:00:00+02:00")));
        server.verify();
    }

    @Test
    void returnsEmptyListWhenParcelNotFound() {
        server.expect(requestTo(BASE_URL + "/v1/tracking/" + NUMBER))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {"trackingNumber":"620000000000000000000001","message":"Tracking information about 620000000000000000000001 shipment has not been found."}
                                """));

        assertThat(adapter.track(NUMBER)).isEmpty();
        server.verify();
    }
}

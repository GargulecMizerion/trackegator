package org.kayman.trackegator.carrier.inpost.networking;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/v1/tracking")
public interface InPostApi {

    @GetExchange("/{nr}")
    InPostTrackingResponse track(@PathVariable String nr);
}

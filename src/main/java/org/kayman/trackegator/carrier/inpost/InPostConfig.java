package org.kayman.trackegator.carrier.inpost;

import org.kayman.trackegator.carrier.inpost.networking.InPostApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class InPostConfig {

    @Bean
    InPostApi inPostApi(@Value("${trackegator.carriers.inpost.base-url}") String baseUrl) {
        return createApi(RestClient.builder(), baseUrl);
    }

    static InPostApi createApi(RestClient.Builder builder, String baseUrl) {
        RestClient restClient = builder.baseUrl(baseUrl).build();
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(InPostApi.class);
    }
}

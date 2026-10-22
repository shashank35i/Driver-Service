package com.goon.driverservice.config;

import com.goon.driverservice.security.InternalServiceTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient rideRestClient(
            RestClient.Builder builder,
            InternalServiceTokenProvider tokenProvider,
            @Value("${app.ride-service.url}") String url)
    {
        return builder
                .baseUrl(url)
                .requestInterceptor((request, body, execution) -> {
                    request.getHeaders().setBearerAuth(tokenProvider.getAccessToken());
                    return execution.execute(request, body);
                })
                .build();
    }

}

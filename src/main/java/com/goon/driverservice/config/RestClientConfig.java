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
                    String path = request.getURI().getPath();
                    String registrationId;
                    if (path.endsWith("/accept")) registrationId = "goon-offers-accept";
                    else if (path.endsWith("/decline")) registrationId = "goon-offers-decline";
                    else if (path.endsWith("/arrived")) registrationId = "goon-rides-arrive";
                    else if (path.endsWith("/start")) registrationId = "goon-rides-start";
                    else if (path.endsWith("/end")) registrationId = "goon-rides-end";
                    else throw new IllegalStateException("No service-token registration for " + path);
                    request.getHeaders().setBearerAuth(tokenProvider.getAccessToken(registrationId));
                    return execution.execute(request, body);
                })
                .build();
    }

}

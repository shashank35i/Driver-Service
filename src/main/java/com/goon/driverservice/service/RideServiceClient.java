package com.goon.driverservice.service;

import com.goon.driverservice.config.RestClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RideServiceClient {

    private final RestClient rideRestClient;


    public void declineOffer(UUID offerId, UUID driverId){

        rideRestClient.patch()
                .uri("/internal/offers/{offerId}/decline",offerId)
                .body(driverId)
                .retrieve()
                .toBodilessEntity();
    }
    public AcceptOfferResponse acceptOffer(
            UUID offerId,
            AcceptOfferRequest request) {

        return rideRestClient.post()
                .uri("/internal/offers/{offerId}/accept", offerId)
                .body(request)
                .retrieve()
                .body(AcceptOfferResponse.class);
    }

}

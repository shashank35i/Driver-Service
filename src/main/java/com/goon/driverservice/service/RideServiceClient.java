package com.goon.driverservice.service;

import com.goon.driverservice.dto.response.StartResult;
import com.goon.driverservice.dto.request.AcceptOfferRequest;
import com.goon.driverservice.dto.response.AcceptOfferResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
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

    public Instant markArrived(
            UUID rideId,
            UUID driverId
    ){
       return rideRestClient.post()
                .uri("/internal/{rideId}/arrived",rideId)
                .body(driverId)
                .retrieve()
                .body(Instant.class);
    }

    public StartResult start(
            UUID rideId,
            UUID driverId,
            String otp
    ){
        return rideRestClient.post()
                .uri("/{rideId}/start",rideId)
                .body(new StartRequest(driverId,otp))
                .retrieve()
                .body(StartResult.class);

    }
}

package com.chrosciu.rxbooking;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RxBookingService {
    enum TripType {
        THERE,
        BACK
    }

    public Mono<String> book(String destination) {
        return Mono.zip(
            book(destination, TripType.THERE),
            book(destination, TripType.BACK),
            (there, back) -> String.join("\n", there, back)
        );
    }

    private Mono<String> book(String destination, TripType tripType) {
        return Mono.fromCallable(() -> destination)
            .doOnNext(s -> log.info("[{} {}] Booking start", destination, tripType))
            .map(d -> bookWithDelay(d, tripType))
            .doOnNext(s -> log.info("[{} {}] Booking end", destination, tripType));
    }

    @SneakyThrows
    private String bookWithDelay(String destination, TripType tripType) {
        Thread.sleep(3000);
        return String.format("Booked %s travel to: %s", tripType, destination);
    }
}

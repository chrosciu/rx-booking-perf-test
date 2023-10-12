package com.chrosciu.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class BookingService {
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
                .delayElement(Duration.ofSeconds(6))
                .map(d -> String.format("Booked %s travel to: %s", tripType, d))
                .doOnNext(s -> log.info("[{} {}] Booking end", destination, tripType));
    }

}

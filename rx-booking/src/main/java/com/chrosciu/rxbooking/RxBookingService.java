package com.chrosciu.rxbooking;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RxBookingService {
    public Mono<String> book(String destination) {
        return Mono.fromCallable(() -> destination)
            .doOnNext(s -> log.info("[{}] Booking start", destination))
            .delayElement(Duration.ofSeconds(3))
            .map(d -> "Booked travel to: " + d)
            .doOnNext(s -> log.info("[{}] Booking end", destination));
    }
}

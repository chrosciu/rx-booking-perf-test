package com.chrosciu.booking;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ThreadFactory;

@Service
@Slf4j
public class BookingService {
    enum TripType {
        THERE,
        BACK
    }

    /*
        Default boundedElastic() scheduler behaves like a hybrid
        - it creates virtual threads but is capped like they would have been platform ones
        (10 * number of cores)
        It is possible to change these settings via system properties,
        however I decided just to create my two schedulers and use them alternatively`
    */

    private final ThreadFactory virtualThreadFactory = Thread.ofVirtual()
            .name("prefix", 0)
            .factory();

    private final ThreadFactory platformThreadFactory = Thread.ofPlatform()
            .name("prefix", 0)
            .factory();


    private final Scheduler virtualScheduler = Schedulers
            .newBoundedElastic(1_000_000, 1_000_000, virtualThreadFactory, 60);

    private final Scheduler platformScheduler = Schedulers
            .newBoundedElastic(160, 1_000_000, platformThreadFactory, 60);

    private final Scheduler scheduler = virtualScheduler;
    //private final Scheduler scheduler = platformScheduler;

    public Mono<String> book(String destination) {
        return Mono.zip(
                book(destination, TripType.THERE).subscribeOn(scheduler),
                book(destination, TripType.BACK).subscribeOn(scheduler),
                (there, back) -> String.join("\n", there, back)
        );
    }

    private Mono<String> book(String destination, TripType tripType) {
        return Mono.fromCallable(() -> bookInternal(destination, tripType))
                .doOnSubscribe(s -> log.info("[{} {}] Booking start", destination, tripType))
                .doOnNext(s -> log.info("[{} {}] Booking end", destination, tripType));
    }

    @SneakyThrows
    private String bookInternal(String destination, TripType tripType) {
        Thread.sleep(6000);
        log.info("{}", Thread.currentThread().isVirtual());
        return String.format("Booked %s travel to: %s", tripType, destination);
    }

}

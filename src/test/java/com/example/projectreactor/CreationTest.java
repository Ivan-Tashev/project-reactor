package com.example.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

public class CreationTest {

    @Test
    void create(){
        Flux<String> flux = Flux.just("A", "B", "C");

        StepVerifier.create(flux) // subscribe to Reactive type
                .expectNext("A") // apply assertion to each data, as it flows
                .expectNext("B", "C")
                .verifyComplete(); // verify the stream completed
    }

    @Test
    void createFromArray(){
        Flux<String> flux = Flux.fromArray(new String[]{"A", "B", "C"});

        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .verifyComplete();
    }

    @Test
    void createFromIterable(){
        Flux<String> flux = Flux.fromIterable(List.of("A", "B", "C"));

        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .verifyComplete();
    }

    @Test
    void createFromStream(){
        Flux<String> flux = Flux.fromStream(Stream.of("A", "B", "C"));

        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .verifyComplete();
    }

    @Test
    void createRange(){
        Flux<Integer> flux = Flux.range(1, 5);

        StepVerifier.create(flux)
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }

    @Test
    void createInterval(){
        Flux<Long> flux = Flux.interval(Duration.ofMillis(100)).take(3);

        StepVerifier.create(flux)
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }


}

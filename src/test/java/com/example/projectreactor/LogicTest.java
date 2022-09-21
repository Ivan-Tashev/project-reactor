package com.example.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class LogicTest {

    @Test
    void all() {
        Mono<Boolean> mono = Flux.just("A", "B", "C")
                .all(el -> el.length() == 1);

        StepVerifier.create(mono)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void any() {
        Mono<Boolean> mono = Flux.just("A", "B", "C")
                .any(el -> el.equalsIgnoreCase("c"));

        StepVerifier.create(mono)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }
}

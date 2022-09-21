package com.example.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class CombineSplitTest {

    @Test
    void mergeTwo() {
        Flux<String> first = Flux.just("A", "C");
        Flux<String> second = Flux.just("B", "D");

        Flux<String> merged = first.mergeWith(second);

        StepVerifier.create(merged)
                .expectNext("A", "C", "B", "D")
                .verifyComplete();
    }

    @Test
    void mergeTwoDelayed() {
        Flux<String> first = Flux.just("A", "C")
                .delayElements(Duration.ofMillis(100));
        Flux<String> second = Flux.just("B", "D")
                .delaySubscription(Duration.ofMillis(50))
                .delayElements(Duration.ofMillis(100));

        Flux<String> merged = first.mergeWith(second);

        StepVerifier.create(merged)
                .expectNext("A", "B", "C", "D")
                .verifyComplete();
    }

    @Test
    void zip() {
        Flux<String> names = Flux.just("A", "B");
        Flux<Integer> ages = Flux.just(20, 30);

        Flux<Tuple2<String, Integer>> zip = Flux.zip(names, ages);

        StepVerifier.create(zip)
                .expectNextMatches(z -> z.getT1().equals("A") && z.getT2() == 20)
                .expectNextMatches(z -> z.getT1().equals("B") && z.getT2() == 30)
                .verifyComplete();
    }

    @Test
    void zipToObject() {
        Flux<String> names = Flux.just("A", "B");
        Flux<Integer> ages = Flux.just(20, 30);

        Flux<String> zip = Flux.zip(names, ages, (a, b) -> a + b);

        StepVerifier.create(zip)
                .expectNext("A20", "B30")
                .verifyComplete();
    }

}

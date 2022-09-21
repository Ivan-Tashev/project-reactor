package com.example.projectreactor;

import lombok.Data;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

public class TransformFilterTest {

    @Test
    void skip() {
        Flux<String> flux = Flux.just("A", "B", "C")
                .skip(2);

        StepVerifier.create(flux)
                .expectNext("C")
                .verifyComplete();
    }

    @Test
    void take() {
        Flux<String> flux = Flux.just("A", "B", "C")
                .take(2);

        StepVerifier.create(flux)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void filter() {
        Flux<String> flux = Flux.just("A", "B", "C")
                .filter(el -> el.equalsIgnoreCase("b"));

        StepVerifier.create(flux)
                .expectNext("B")
                .verifyComplete();
    }

    @Test
    void distinct() {
        Flux<String> flux = Flux.just("A", "B", "B", "C", "A")
                .distinct();

        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .verifyComplete();
    }

    @Test
    void map() {
        Flux<User> flux = Flux.just("A", "B")
                .map(el -> new User(el));

        StepVerifier.create(flux)
                .expectNext(new User("A"), new User("B"))
                .verifyComplete();
    }

    @Data
    private static class User {
        private final String name;
    }

    @Test
    void flatMap() {
        Flux<User> flux = (Flux<User>) Flux.just("A", "B", "C")
                .flatMap(el -> {
                    Mono<String> monoString = Mono.just(el);
                    Mono<User> userMono = monoString
                            .map(User::new)
                            .subscribeOn(Schedulers.parallel()); // each subscription take place in parallel thread
                    return userMono;
                });

        List<User> users = List.of(new User("A"), new User("B"), new User("C"));

        StepVerifier.create(flux)
//                .expectNext(new User("A"), new User("B"), new User("C"))
                .expectNextMatches(el -> users.contains(el))
                .expectNextMatches(users::contains)
                .expectNextMatches(users::contains)
                .verifyComplete();
    }

    @Test
    void buffer() {
        Flux<List<String>> flux = Flux.just("A", "B", "C", "D", "E")
                .buffer(3);

        StepVerifier.create(flux)
                .expectNext(List.of("A", "B", "C"))
                .expectNext(List.of("D", "E"))
                .verifyComplete();
    }

    @Test
    void bufferFlatMap() {
        Flux<String> flux = Flux.just("A", "B", "C", "D", "E")
                .buffer(3)
                .flatMap(collection ->
                        Flux.fromIterable(collection)
                                .map(el -> el.toLowerCase())
                                .subscribeOn(Schedulers.parallel())
                );

        List<String> list = List.of("a", "b", "c", "d", "e");

        StepVerifier.create(flux)
                .expectNextMatches(el -> list.contains(el))
                .expectNextMatches(list::contains)
                .expectNextMatches(list::contains)
                .expectNextMatches(list::contains)
                .expectNextMatches(list::contains)
                .verifyComplete();
    }

    @Test
    void collectList() {
        Mono<List<String>> mono = Flux.just("A", "B", "C")
                .collectList();

        StepVerifier.create(mono)
                .expectNext(List.of("A", "B", "C"))
                .verifyComplete();
    }

    @Test
    void collectMap() {
        Mono<Map<Integer, String>> mono = Flux.just("A", "B", "C")
                .collectMap(el -> el.length());

        StepVerifier.create(mono)
                .expectNext(Map.of(1, "C"))
                .verifyComplete();
    }
}

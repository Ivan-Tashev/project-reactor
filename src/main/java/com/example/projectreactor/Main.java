package com.example.projectreactor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class Main implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String name = "Imperative"; // perform steps, one after another
        String capitalName = name.toUpperCase();
        String text = "Hello " + capitalName;
        System.out.println(text); // 100% all on a single thread

        Mono.just("Reactive") // publisher (Mono impl) that emits value
                .map(n -> n.toUpperCase()) // get value and publish it, another Mono
                .map(cn -> "Hello " + cn) // get value and publish it, another Mono
                .subscribe( // finally subscribe to Mono (publisher) and receive the data
                        System.out::println);// maybe on single thread..., or maybe not.

        Flux<String> flux = Flux.just("A", "B", "C");
        flux.subscribe(l -> System.out.print(l + " "));
    }
}


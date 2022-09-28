package com.example.projectreactor.web;

import com.example.projectreactor.model.Player;
import com.example.projectreactor.model.Team;
import com.example.projectreactor.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

@Slf4j
@Controller
public class RsocketServerController {

    @MessageMapping("user/{id}") // handle incoming msg on this route
    public Mono<User> getUserMsg( // payload is received as Mono
                                  @DestinationVariable("id") Long id, Mono<String> name) {
        log.info("User {} with id {} received.", name, id);
        return Mono.just(new User(id, name.toString()));
    }

    @MessageMapping("allByName/{name}") // handle incoming msg on this route
    public Flux<User> getAllUsersByName(@DestinationVariable("name") String name) {
        return Flux.interval(Duration.ofSeconds(1)) // no msg payload, but response with Stream
                .map(el -> new User(new Random().nextLong(), name));
    }

    @MessageMapping("display") // handle incoming msg on this route
    public Mono<Void> displayUser(Mono<User> userMono) { // msg with payload
        return userMono.doOnNext(user -> log.info(user.getName()))
                .thenEmpty(Mono.empty()); // simply NO response
    }

    @MessageMapping("team") // handle incoming msg on this route
    public Flux<Team> exchangeData(Flux<Player> playerFlux) { // msg with Flux payload
        return playerFlux.doOnNext(el -> log.info("Player {}", el.getName()))
                .map(el -> new Team()); // get the corresponding Team from repo
    }   // multiple responses as Flux
}

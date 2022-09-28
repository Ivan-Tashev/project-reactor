package com.example.projectreactor.config;

import com.example.projectreactor.model.Player;
import com.example.projectreactor.model.Team;
import com.example.projectreactor.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Configuration
public class RSocketClientConfig {

    @Bean
    public ApplicationRunner senderReqRes(RSocketRequester.Builder requestBuilder) {
        return args -> {
            RSocketRequester tcp = requestBuilder.tcp("localhost", 3000);

            tcp.route("user/{id}", 5) // route to be sent to
                    .data("John") // msg payload, in example String
                    .retrieveMono(User.class) // subscribe to received Mono<User> and handle payload
                    .subscribe(response -> log.info("Response: {}", response));
        };
    }

    @Bean
    public ApplicationRunner senderReqStream(RSocketRequester.Builder requestBuilder) {
        return args -> {
            RSocketRequester tcp = requestBuilder.tcp("localhost", 3000);

            tcp.route("allByName/{name}", "John") // route to be sent to
                    .retrieveFlux(User.class) // subscribe to received Flux<User> and handle payload
                    .doOnNext(el -> log.info("User id {} with name {} ", el.getId(), el.getName()))
                    .subscribe();
        };
    }

    @Bean
    public ApplicationRunner senderFireAndForget(RSocketRequester.Builder requestBuilder) {
        return args -> {
            RSocketRequester tcp = requestBuilder.tcp("localhost", 3000);

            tcp.route("display") // route to be sent to
                    .data(new User()) // some data payload
                    .send() // instead of retrieveMono/Flux, simply send
                    .subscribe();
        };
    }

    @Bean
    public ApplicationRunner senderChannel(RSocketRequester.Builder requestBuilder) {
        return args -> {
            RSocketRequester tcp = requestBuilder.tcp("localhost", 3000);

            tcp.route("team") // route to be sent to
                    .data(Flux.fromIterable(List.of(new Player(), new Player())))
                    .retrieveFlux(Team.class) // opens bidirectional channel
                    .subscribe(team -> log.info("Team {}", team.getName()));
        };
    }
}

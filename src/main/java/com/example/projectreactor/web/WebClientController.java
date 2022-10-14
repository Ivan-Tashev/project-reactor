package com.example.projectreactor.web;

import com.example.projectreactor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/webclient", produces = "application/json")
public class WebClientController {

    private static final String SERVER_ROUTE = "https://jsonplaceholder.typicode.com";

    @Autowired
    private WebClient webClient;

    @GetMapping("/all")
    public Flux<User> getAll() {
        return WebClient.create()// create basic WebClient here
                .get() // GET http method
                .uri(SERVER_ROUTE + "/users") // server uri, concatenated
                .retrieve() // return simple obj ResponseSpec
                .bodyToFlux(User.class) // subscribe to body, as Flux<User>
                .timeout(Duration.ofMillis(2000));
    } // to apply additional operations to Mono subscribe to it
    // mono.subscribe(element -> {..});

    @GetMapping("/{id}")
    public Mono<String> getById(@PathVariable("id") String id) {
        return webClient // WebClient from Bean in @Configuration
                .method(HttpMethod.GET)
                .uri("/users/{id}", id) // server uri, with argument
                .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND,
                        response -> Mono.just(new NoSuchElementException("No such user")))
                .bodyToMono(String.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@RequestBody User user) {
        return webClient
                .post()
                .uri("/users")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(user) // or .body(Mono.just(user), User.class)
                .retrieve() // return simple ResponseSpec obj
                .bodyToMono(User.class);
    }

    @PutMapping
    public Mono<User> updateUser(@RequestParam("id") String id, @RequestBody Mono<User> user) {
        return webClient
                .put()
                .uri("/users/{id}", id)
                .bodyValue(user)
                .exchangeToMono(response ->  // return full ClientResponse obj
                        response.headers().header("X_Auth").isEmpty() // response.cookies().containsKey("A")
                                ? Mono.empty() : Mono.just(response)) // get Mono<ClientResponse>
                .flatMap(clientResponse -> clientResponse.bodyToMono(User.class));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return webClient
                .delete()
                .uri("/users/{id}", id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.just(new NoSuchElementException("Bad client request")))
                .bodyToMono(Void.class); // response body is empty
    }
}

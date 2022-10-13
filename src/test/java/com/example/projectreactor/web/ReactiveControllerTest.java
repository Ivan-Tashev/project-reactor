package com.example.projectreactor.web;

import com.example.projectreactor.model.User;
import com.example.projectreactor.repo.ReactiveUserRepo;
import com.example.projectreactor.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReactiveControllerTest {

    @Mock
    private UserRepo userRepo;

    private final ReactiveUserRepo reactiveUserRepo = Mockito.mock(ReactiveUserRepo.class);

    private final WebTestClient webTestClient = WebTestClient.bindToController(
            new ReactiveController(userRepo, reactiveUserRepo)).build();

    @Test
    void getAllUsers() {
        when(reactiveUserRepo.findAll()).thenReturn(
                Flux.just(new User(1L, "Ivan")));

        webTestClient.get().uri("/reactive/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$[0].id").isEqualTo(1L)
                .jsonPath("$[0].name").isEqualTo("Ivan")
                .jsonPath("$[1]").doesNotExist();
    }

    @Test
    void getById() {
        when(reactiveUserRepo.findById(anyLong())).thenReturn(
                Mono.just(new User(1L, "Ivan")));

        webTestClient.get().uri("/reactive/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$.id").isEqualTo(1L)
                .jsonPath("$.name").isEqualTo("Ivan");
    }

    @Test
    void saveUser() {
        User user = new User(1L, "Ivan");

        when(reactiveUserRepo.save(any())).thenReturn(Mono.just(user));

        webTestClient.post().uri("/reactive", 1)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class)
                .isEqualTo(user);
    }

    @Test
    void deleteUser() {
        when(reactiveUserRepo.deleteById(anyLong())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/reactive/{id}", 1)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .isEmpty();
    }
}
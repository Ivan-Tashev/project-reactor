package com.example.projectreactor.repo;

import com.example.projectreactor.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveUserRepo extends ReactiveCrudRepository<User, Long> {

    @Override
    Flux<User> findAll();

    @Override
    Mono<User> findById(Long id);
}

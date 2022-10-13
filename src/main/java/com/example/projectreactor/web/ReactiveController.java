package com.example.projectreactor.web;


import com.example.projectreactor.repo.ReactiveUserRepo;
import com.example.projectreactor.repo.UserRepo;
import com.example.projectreactor.model.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@AllArgsConstructor
@org.springframework.web.bind.annotation.RestController
@RequestMapping(path = "/reactive", produces = "application/json")
public class ReactiveController {

    private UserRepo userRepo;
    private ReactiveUserRepo reactiveUserRepo;

    // --------- Poor practice ---------
    @GetMapping("/bad")
    public Flux<User> getAllUsersFromIterable() {
        return Flux.fromIterable(userRepo.findAll());
    }

    // --------- Good practice ---------


    @GetMapping("/all")
    public Flux<User> getAllUsers() { // if RxJava can return Observable or Flowable
        return reactiveUserRepo.findAll(); // .take(5);
    }

    @GetMapping("/{id}")
    public Mono<User> getById(@PathVariable("id") Long id) { // if RxJava can return Single
        return reactiveUserRepo.findById(id);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> saveUser(@RequestBody Mono<User> mono) {
        return mono.flatMap(user -> reactiveUserRepo.save(user));
//        return reactiveUserRepo.saveAll(mono).next();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable("id") Long id){ // if RxJava can return Completable
        return reactiveUserRepo.deleteById(id);
    }
}

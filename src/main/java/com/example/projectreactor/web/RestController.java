package com.example.projectreactor.web;


import com.example.projectreactor.repo.UserRepo;
import com.example.projectreactor.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestController
@RequestMapping(path = "/rest", produces = "application/json")
public class RestController {

    private UserRepo userRepo;

    @GetMapping("/all")
    public Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepo.save(user);
    }

    @PutMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User update(@RequestParam("id") Long id, @RequestBody User user) {
        userRepo.findById(id);
        // map the updates
        return userRepo.save(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id) {
        userRepo.deleteById(id);
    }
}

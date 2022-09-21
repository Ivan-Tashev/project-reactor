package com.example.projectreactor.Repo;

import com.example.projectreactor.model.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepo extends ReactiveCrudRepository<Player, Long> {
}

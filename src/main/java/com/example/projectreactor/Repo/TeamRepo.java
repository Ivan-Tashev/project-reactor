package com.example.projectreactor.Repo;

import com.example.projectreactor.model.Team;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepo extends ReactiveCrudRepository<Team, Long> {
}

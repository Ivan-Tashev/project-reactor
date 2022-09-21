package com.example.projectreactor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Team {

    @Id
    private Long id;

    private @NonNull String name;

    private Set<Long> playerIds = new HashSet<>();
}

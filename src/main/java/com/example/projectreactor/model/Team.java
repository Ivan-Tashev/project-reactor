package com.example.projectreactor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Id;

import java.util.HashSet;
import java.util.Set;

@Table
@Data
@NoArgsConstructor
public class Team {

    @Id
    private Long id;

    private @NonNull String name;

    private Set<Long> playerIds = new HashSet<>();
}

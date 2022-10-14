package com.example.projectreactor.model;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Id;

@Table
@Data // incl. @ReqArgsCtor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Player {

    @Id
    private Long id; // when saving, if id is non-null will be treated as update!

    @NonNull // from lombok, to enforce it in ReqArgsCtor
    private String name; // Spring R2DBC require setters on properties!
}

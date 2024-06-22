package org.ironhack.lab408.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // cascade = CascadeType.ALL ensures that all operations (persist, merge, remove, refresh, detach) are cascaded from Author to BlogPost.
    // orphanRemoval = true ensures that BlogPost entities that are no longer referenced by an Author are automatically removed.
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BlogPost> blogPosts;

    public Author(String name) {
        this.name = name;
    }
}

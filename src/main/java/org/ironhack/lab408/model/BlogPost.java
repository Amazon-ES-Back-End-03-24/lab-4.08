package org.ironhack.lab408.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String post;

    @ManyToOne
    private Author author;

    @ManyToOne
    @JsonIgnore
    private User user;

    public BlogPost(String title, String post) {
        this.title = title;
        this.post = post;
    }

    public BlogPost(Author author, String title, String post) {
        this.author = author;
        this.title = title;
        this.post = post;
    }
}

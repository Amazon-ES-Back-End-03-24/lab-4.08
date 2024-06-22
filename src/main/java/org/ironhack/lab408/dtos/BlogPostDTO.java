package org.ironhack.lab408.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlogPostDTO {

    @NotEmpty(message = "Title can't be empty or null.")
    private String title;
    @NotEmpty(message = "Post can't be empty or null.")
    private String post;
    @Digits(integer = 5, fraction = 0, message = "The id has a maximum of 5 digits.")
    private Long authorId;
}

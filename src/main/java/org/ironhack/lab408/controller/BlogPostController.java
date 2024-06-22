package org.ironhack.lab408.controller;

import jakarta.validation.Valid;
import org.ironhack.lab408.dtos.BlogPostDTO;
import org.ironhack.lab408.model.BlogPost;
import org.ironhack.lab408.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @GetMapping("/blogposts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BlogPost findById(@PathVariable long id) {
        return blogPostService.findById(id);
    }


    @PostMapping("/blogposts")
    @ResponseStatus(HttpStatus.CREATED)
    public BlogPost store(@RequestBody @Valid BlogPostDTO blogPostDTO) {
        return blogPostService.store(blogPostDTO);
    }

    @PutMapping("/blogposts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long id, @RequestBody @Valid BlogPostDTO blogPostDTO) {
        blogPostService.update(id, blogPostDTO);
    }

    @DeleteMapping("/blogposts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        blogPostService.delete(id);
    }

}

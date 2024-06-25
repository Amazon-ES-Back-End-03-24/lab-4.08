package org.ironhack.lab408.controller;

import jakarta.validation.Valid;
import org.ironhack.lab408.dtos.BlogPostDTO;
import org.ironhack.lab408.model.BlogPost;
import org.ironhack.lab408.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping("/blogposts/{id}/favourite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void favouritePost(@PathVariable long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        System.out.println("LOGGED USER  " + username);
        blogPostService.favouritePost(id, username);
    }

    @GetMapping("/blogposts/favourite")
    @ResponseStatus(HttpStatus.OK)
    public List<BlogPost> findByUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        System.out.println("LOGGED USER  " + username);
        return blogPostService.findByUsername(username);
    }
}

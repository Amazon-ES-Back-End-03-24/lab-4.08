package org.ironhack.lab408.controller;

import jakarta.validation.Valid;
import org.ironhack.lab408.dtos.AuthorDTO;
import org.ironhack.lab408.model.Author;
import org.ironhack.lab408.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Author findById(@PathVariable long id) {
        return authorService.findbyId(id);
    }

    @PostMapping("/authors")
    @ResponseStatus(HttpStatus.CREATED)
    public Author store(@RequestBody @Valid AuthorDTO authorDTO) {
        return authorService.store(authorDTO);
    }

    @PutMapping("/authors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long id, @RequestBody @Valid AuthorDTO authorDTO) {
        authorService.update(id, authorDTO);
    }

    @DeleteMapping("/authors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        authorService.delete(id);
    }


}

package org.ironhack.lab408.service;

import org.ironhack.lab408.dtos.AuthorDTO;
import org.ironhack.lab408.model.Author;
import org.ironhack.lab408.repository.AuthorRepository;
import org.ironhack.lab408.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    public Author findbyId(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            return author.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author id not found.");
        }
    }

    public Author store(AuthorDTO authorDTO) {
        return authorRepository.save(new Author(authorDTO.getName()));
    }

    public void update(long id, AuthorDTO authorDTO) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            author.setName(authorDTO.getName());
            authorRepository.save(author);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author id not found.");
        }
    }

    public void delete(long id) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            // Not needed because we have orphan removal in the relationship:
//            for (BlogPost blogPost : blogPostRepository.findByAuthorId(id)) {
//                blogPostRepository.delete(blogPost);
//            }
            authorRepository.delete(author.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author id not found.");
        }
    }
}

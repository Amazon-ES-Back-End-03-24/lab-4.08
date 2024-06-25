package org.ironhack.lab408.service;

import org.ironhack.lab408.dtos.BlogPostDTO;
import org.ironhack.lab408.model.Author;
import org.ironhack.lab408.model.BlogPost;
import org.ironhack.lab408.model.User;
import org.ironhack.lab408.repository.AuthorRepository;
import org.ironhack.lab408.repository.BlogPostRepository;
import org.ironhack.lab408.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    public BlogPost findById(Long id) {
        Optional<BlogPost> blogPost = blogPostRepository.findById(id);
        if (blogPost.isPresent()) {
            return blogPost.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog Post id not found.");
        }
    }

    public BlogPost store(BlogPostDTO blogPostDTO) {
        Optional<Author> author = authorRepository.findById(blogPostDTO.getAuthorId());
        if (author.isPresent()) {
            BlogPost blogPost = new BlogPost(blogPostDTO.getTitle(), blogPostDTO.getPost());
            blogPost.setAuthor(author.get());
            return blogPostRepository.save(blogPost);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author id not found.");
        }
    }

    public void update(long id, BlogPostDTO blogPostDTO) {
        Optional<BlogPost> optionalBlogPost = blogPostRepository.findById(id);
        if (optionalBlogPost.isPresent()) {
            BlogPost blogPost = optionalBlogPost.get();

            if (blogPostDTO.getAuthorId() != null) {
                Optional<Author> author = authorRepository.findById(blogPostDTO.getAuthorId());
                if (author.isPresent()) {
                    blogPost.setAuthor(author.get());
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author id not found.");
                }
            }

            blogPost.setTitle(blogPostDTO.getTitle());
            blogPost.setPost(blogPostDTO.getPost());
            blogPostRepository.save(blogPost);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BlogPost id not found.");
        }
    }

    public void delete(long id) {
        Optional<BlogPost> blogPost = blogPostRepository.findById(id);
        if (blogPost.isPresent()) {
            blogPostRepository.delete(blogPost.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BlogPost id not found");
        }
    }

    public void favouritePost(long id, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username doesn't exist in DB");
        }
        Optional<BlogPost> blogPost = blogPostRepository.findById(id);

        if (blogPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BlogPost id not found");
        }

        blogPost.get().setUser(user);
        blogPostRepository.save(blogPost.get());
    }

    public List<BlogPost> findByUsername(String username) {
        return blogPostRepository.findByUserUsername(username);
    }
}

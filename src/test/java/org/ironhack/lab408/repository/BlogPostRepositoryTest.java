package org.ironhack.lab408.repository;

import org.ironhack.lab408.model.Author;
import org.ironhack.lab408.model.BlogPost;
import org.ironhack.lab408.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BlogPostRepositoryTest {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);

        Author author = new Author();
        authorRepository.save(author);

        BlogPost post1 = new BlogPost();
        post1.setUser(user);
        post1.setAuthor(author);
        BlogPost post2 = new BlogPost();
        post2.setUser(user);
        post2.setAuthor(author);

        blogPostRepository.save(post1);
        blogPostRepository.save(post2);
    }

    @AfterEach
    void tearDown() {
        blogPostRepository.deleteAll();
        userRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void findByAuthorId_existingAuthorId_blogPostsReturned() {
        Author author = authorRepository.findAll().get(0);
        List<BlogPost> found = blogPostRepository.findByAuthorId(author.getId());
        assertEquals(2, found.size());
    }

    @Test
    void findByUserUsername_existingUsername_blogPostsReturned() {
        List<BlogPost> found = blogPostRepository.findByUserUsername("testUser");
        assertEquals(2, found.size());
    }
}
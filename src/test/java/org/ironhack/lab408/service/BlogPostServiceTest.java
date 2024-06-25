package org.ironhack.lab408.service;

import org.ironhack.lab408.dtos.BlogPostDTO;
import org.ironhack.lab408.model.Author;
import org.ironhack.lab408.model.BlogPost;
import org.ironhack.lab408.model.User;
import org.ironhack.lab408.repository.AuthorRepository;
import org.ironhack.lab408.repository.BlogPostRepository;
import org.ironhack.lab408.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogPostServiceTest {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Author author = new Author();
        author.setName("John Doe");
        authorRepository.save(author);

        BlogPost post = new BlogPost();
        post.setTitle("Test Title");
        post.setPost("Test Content");
        post.setAuthor(author);
        blogPostRepository.save(post);

        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        blogPostRepository.deleteAll();
        authorRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findById_existingId_blogPostReturned() {
        BlogPost post = blogPostRepository.findAll().get(0);
        BlogPost found = blogPostService.findById(post.getId());
        assertNotNull(found);
        assertEquals("Test Title", found.getTitle());
    }

    @Test
    void findById_nonExistingId_throwsNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            blogPostService.findById(0L);
        });
    }

    @Test
    void store_validBlogPost_blogPostSaved() {
        Author author = authorRepository.findAll().get(0);
        BlogPostDTO blogPostDTO = new BlogPostDTO("New Title", "New Content", author.getId());
        BlogPost saved = blogPostService.store(blogPostDTO);
        assertNotNull(saved);
        assertEquals("New Title", saved.getTitle());
    }

    @Test
    void store_invalidAuthorId_throwsNotFound() {
        BlogPostDTO blogPostDTO = new BlogPostDTO("New Title", "New Content", 0L);
        assertThrows(ResponseStatusException.class, () -> {
            blogPostService.store(blogPostDTO);
        });
    }

    @Test
    void update_existingId_blogPostUpdated() {
        BlogPost post = blogPostRepository.findAll().get(0);
        BlogPostDTO blogPostDTO = new BlogPostDTO("Updated Title", "Updated Content", post.getAuthor().getId());
        blogPostService.update(post.getId(), blogPostDTO);
        BlogPost updated = blogPostRepository.findById(post.getId()).get();
        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    void update_nonExistingId_throwsNotFound() {
        BlogPostDTO blogPostDTO = new BlogPostDTO("Updated Title", "Updated Content", 0L);
        assertThrows(ResponseStatusException.class, () -> {
            blogPostService.update(0L, blogPostDTO);
        });
    }

    @Test
    void update_invalidAuthorId_throwsNotFound() {
        BlogPost post = blogPostRepository.findAll().get(0);
        BlogPostDTO blogPostDTO = new BlogPostDTO("Updated Title", "Updated Content", 999L);
        assertThrows(ResponseStatusException.class, () -> {
            blogPostService.update(post.getId(), blogPostDTO);
        });
    }

    @Test
    void delete_existingId_blogPostDeleted() {
        BlogPost post = blogPostRepository.findAll().get(0);
        blogPostService.delete(post.getId());
        assertFalse(blogPostRepository.findById(post.getId()).isPresent());
    }

    @Test
    void delete_nonExistingId_throwsNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            blogPostService.delete(0L);
        });
    }

    @Test
    void favouritePost_existingIdAndUsername_blogPostUpdated() {
        BlogPost post = blogPostRepository.findAll().get(0);
        blogPostService.favouritePost(post.getId(), "testUser");
        BlogPost updatedPost = blogPostRepository.findById(post.getId()).get();
        assertEquals("testUser", updatedPost.getUser().getUsername());
    }

    @Test
    void favouritePost_nonExistingUsername_throwsNotFound() {
        BlogPost post = blogPostRepository.findAll().get(0);
        assertThrows(ResponseStatusException.class, () -> {
            blogPostService.favouritePost(post.getId(), "nonExistingUser");
        });
    }

    @Test
    void favouritePost_nonExistingPostId_throwsNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            blogPostService.favouritePost(999L, "testUser");
        });
    }

    @Test
    void findByUsername_existingUsername_blogPostsReturned() {
        BlogPost post = blogPostRepository.findAll().get(0);
        blogPostService.favouritePost(post.getId(), "testUser");
        List<BlogPost> posts = blogPostService.findByUsername("testUser");
        assertFalse(posts.isEmpty());
        assertEquals("testUser", posts.get(0).getUser().getUsername());
    }

    @Test
    void findByUsername_nonExistingUsername_emptyListReturned() {
        List<BlogPost> posts = blogPostService.findByUsername("nonExistingUser");
        assertTrue(posts.isEmpty());
    }
}
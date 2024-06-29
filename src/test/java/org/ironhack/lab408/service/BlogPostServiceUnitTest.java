package org.ironhack.lab408.service;


import org.ironhack.lab408.dtos.BlogPostDTO;
import org.ironhack.lab408.model.Author;
import org.ironhack.lab408.model.BlogPost;
import org.ironhack.lab408.model.User;
import org.ironhack.lab408.repository.AuthorRepository;
import org.ironhack.lab408.repository.BlogPostRepository;
import org.ironhack.lab408.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
class BlogPostServiceUnitTest {

    // Creates an instance of the class and injects the mocks into it.
    @InjectMocks
    private BlogPostService blogPostService;

    // Creates an instance of the mock
    @Mock
    private BlogPostRepository blogPostRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Initializes mocks annotated with @Mock and injects these mocks into the fields annotated with @InjectMocks.
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_existingId_blogPostReturned() {
        BlogPost post = new BlogPost();
        post.setId(1L);
        post.setTitle("Test Title");
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(post));

        BlogPost found = blogPostService.findById(1L);
        assertNotNull(found);
        assertEquals("Test Title", found.getTitle());
    }

    @Test
    void findById_nonExistingId_throwsNotFound() {
        when(blogPostRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> blogPostService.findById(1L));
    }

    @Test
    void store_validBlogPost_blogPostSaved() {
        Author author = new Author();
        author.setId(1L);
        BlogPostDTO blogPostDTO = new BlogPostDTO("New Title", "New Content", 1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(blogPostRepository.save(any(BlogPost.class))).thenReturn(new BlogPost(author, "New Title", "New Content"));

        BlogPost saved = blogPostService.store(blogPostDTO);
        assertNotNull(saved);
        assertEquals("New Title", saved.getTitle());
    }

    @Test
    void store_invalidAuthorId_throwsNotFound() {
        BlogPostDTO blogPostDTO = new BlogPostDTO("New Title", "New Content", 0L);
        when(authorRepository.findById(0L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> blogPostService.store(blogPostDTO));
    }

    @Test
    void update_existingId_blogPostUpdated() {
        BlogPost post = new BlogPost();
        post.setId(1L);
        post.setTitle("Test Title");
        Author author = new Author();
        author.setId(1L);
        BlogPostDTO blogPostDTO = new BlogPostDTO("Updated Title", "Updated Content", 1L);

        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(post));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(blogPostRepository.save(any(BlogPost.class))).thenReturn(post);

        blogPostService.update(1L, blogPostDTO);
        assertEquals("Updated Title", post.getTitle());
    }

    @Test
    void update_nonExistingId_throwsNotFound() {
        BlogPostDTO blogPostDTO = new BlogPostDTO("Updated Title", "Updated Content", 0L);
        when(blogPostRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> blogPostService.update(1L, blogPostDTO));
    }

    @Test
    void delete_existingId_blogPostDeleted() {
        BlogPost post = new BlogPost();
        post.setId(1L);
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(post));
        doNothing().when(blogPostRepository).delete(any(BlogPost.class));

        blogPostService.delete(1L);
        verify(blogPostRepository, times(1)).delete(post);
    }

    @Test
    void delete_nonExistingId_throwsNotFound() {
        when(blogPostRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> blogPostService.delete(1L));
    }

    @Test
    void favouritePost_existingId_blogPostFavourited() {
        BlogPost post = new BlogPost();
        post.setId(1L);
        post.setTitle("Test Title");
        User user = new User();
        user.setUsername("testUser");

        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(blogPostRepository.save(any(BlogPost.class))).thenReturn(post);

        blogPostService.favouritePost(1L, "testUser");
        assertEquals("testUser", post.getUser().getUsername());
    }

    @Test
    void favouritePost_nonExistingId_throwsNotFound() {
        when(blogPostRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> blogPostService.favouritePost(1L, "testUser"));
    }

    @Test
    void findByUsername_existingUsername_blogPostsReturned() {
        User user = new User();
        user.setUsername("testUser");
        BlogPost post = new BlogPost();
        post.setTitle("Test Title");
        post.setUser(user);

        when(blogPostRepository.findByUserUsername("testUser")).thenReturn(List.of(post));

        List<BlogPost> posts = blogPostService.findByUsername("testUser");
        assertFalse(posts.isEmpty());
        assertEquals("Test Title", posts.get(0).getTitle());
    }

    @Test
    void findByUsername_nonExistingUsername_emptyListReturned() {
        when(blogPostRepository.findByUserUsername(anyString())).thenReturn(List.of());

        List<BlogPost> posts = blogPostService.findByUsername("nonExistingUser");
        assertTrue(posts.isEmpty());
    }
}
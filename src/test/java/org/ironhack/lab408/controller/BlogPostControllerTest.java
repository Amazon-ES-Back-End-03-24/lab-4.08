package org.ironhack.lab408.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class BlogPostControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Author author;
    private BlogPost post;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        author = authorRepository.save(new Author("John Doe"));

        post = blogPostRepository.save(new BlogPost(author, "Test Title", "Test Content"));

        user = new User();
        user.setUsername("testUser");
        userRepository.save(user);    }

    @AfterEach
    void tearDown() {
        blogPostRepository.deleteAll();
        authorRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findById_existingId_blogPostReturned() throws Exception {
        BlogPost post = blogPostRepository.findAll().get(0);
        MvcResult result = mockMvc.perform(get("/blogposts/{id}", post.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Test Title"));
    }

    @Test
    void findById_nonExistingId_throwsNotFound() throws Exception {
        mockMvc.perform(get("/blogposts/{id}", 0L))
                .andExpect(status().isNotFound());
    }

    @Test
    void store_validBlogPost_blogPostCreated() throws Exception {
        BlogPostDTO blogPostDTO = new BlogPostDTO("New Title", "New Content", author.getId());
        String body = objectMapper.writeValueAsString(blogPostDTO);

        MvcResult result = mockMvc.perform(post("/blogposts")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("New Title"));
    }

    @Test
    void store_invalidAuthorId_throwsNotFound() throws Exception {
        BlogPostDTO blogPostDTO = new BlogPostDTO("New Title", "New Content", 0L);
        String body = objectMapper.writeValueAsString(blogPostDTO);

        mockMvc.perform(post("/blogposts")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_existingId_blogPostUpdated() throws Exception {
        BlogPostDTO blogPostDTO = new BlogPostDTO("Updated Title", "Updated Content", post.getAuthor().getId());
        String body = objectMapper.writeValueAsString(blogPostDTO);

        mockMvc.perform(put("/blogposts/{id}", post.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        BlogPost updated = blogPostRepository.findById(post.getId()).get();
        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    void update_nonExistingId_throwsNotFound() throws Exception {
        BlogPostDTO blogPostDTO = new BlogPostDTO("Updated Title", "Updated Content", 0L);
        String body = objectMapper.writeValueAsString(blogPostDTO);

        mockMvc.perform(put("/blogposts/{id}", 0L)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_existingId_blogPostDeleted() throws Exception {
        mockMvc.perform(delete("/blogposts/{id}", post.getId()))
                .andExpect(status().isNoContent());

        assertFalse(blogPostRepository.findById(post.getId()).isPresent());
    }

    @Test
    void delete_nonExistingId_throwsNotFound() throws Exception {
        mockMvc.perform(delete("/blogposts/{id}", 0L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "ADMIN")
    void favouritePost_existingId_blogPostFavourited() throws Exception {
        mockMvc.perform(patch("/blogposts/{id}/favourite", post.getId()))
                .andExpect(status().isNoContent());

        BlogPost updated = blogPostRepository.findById(post.getId()).get();
        assertEquals("testUser", updated.getUser().getUsername());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void favouritePost_nonExistingId_throwsNotFound() throws Exception {
        mockMvc.perform(patch("/blogposts/{id}/favourite", 0L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "ADMIN")
    void findByUsername_existingUsername_blogPostsReturned() throws Exception {
        mockMvc.perform(patch("/blogposts/{id}/favourite", post.getId()));

        MvcResult result = mockMvc.perform(get("/blogposts/favourite"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Test Title"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findByUsername_nonExistingUsername_emptyListReturned() throws Exception {
        MvcResult result = mockMvc.perform(get("/blogposts/favourite"))
                .andExpect(status().isOk())
                .andReturn();

        assertFalse(result.getResponse().getContentAsString().contains("Test Title"));
    }
}
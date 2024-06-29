package org.ironhack.lab408.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.lab408.dtos.BlogPostDTO;
import org.ironhack.lab408.model.BlogPost;
import org.ironhack.lab408.model.User;
import org.ironhack.lab408.repository.UserRepository;
import org.ironhack.lab408.service.BlogPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BlogPostControllerUnitTest {

    // Creates an instance of the class and injects the mocks into it.
    @InjectMocks
    private BlogPostController blogPostController;

    // Creates an instance of the mock
    @Mock
    private BlogPostService blogPostService;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Initializes mocks annotated with @Mock and injects these mocks into the fields annotated with @InjectMocks.
        MockitoAnnotations.openMocks(this);

        // Sets up the MockMvc object to simulate HTTP requests and responses for the specified controller in isolation
        mockMvc = MockMvcBuilders.standaloneSetup(blogPostController).build();
    }

    @Test
    void findById_existingId_blogPostReturned() throws Exception {
        BlogPost post = new BlogPost();
        post.setId(1L);
        post.setTitle("Test Title");

        when(blogPostService.findById(1L)).thenReturn(post);

        mockMvc.perform(get("/blogposts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void findById_nonExistingId_throwsNotFound() throws Exception {
        when(blogPostService.findById(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/blogposts/{id}", 0L))
                .andExpect(status().isNotFound());
    }

    @Test
    void store_validBlogPost_blogPostCreated() throws Exception {
        BlogPostDTO blogPostDTO = new BlogPostDTO("New Title", "New Content", 1L);
        BlogPost post = new BlogPost("New Title", "New Content");

        when(blogPostService.store(any(BlogPostDTO.class))).thenReturn(post);

        mockMvc.perform(post("/blogposts")
                        .content(objectMapper.writeValueAsString(blogPostDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    void update_existingId_blogPostUpdated() throws Exception {
        BlogPostDTO blogPostDTO = new BlogPostDTO("Updated Title", "Updated Content", 1L);
        doNothing().when(blogPostService).update(anyLong(), any(BlogPostDTO.class));

        mockMvc.perform(put("/blogposts/{id}", 1L)
                        .content(objectMapper.writeValueAsString(blogPostDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_existingId_blogPostDeleted() throws Exception {
        doNothing().when(blogPostService).delete(anyLong());

        mockMvc.perform(delete("/blogposts/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "ADMIN")
    void favouritePost_existingId_blogPostFavourited() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);
        doNothing().when(blogPostService).favouritePost(anyLong(), anyString());

        mockMvc.perform(patch("/blogposts/{id}/favourite", 1L))
                .andExpect(status().isNoContent());
    }
}

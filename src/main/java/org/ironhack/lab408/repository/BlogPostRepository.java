package org.ironhack.lab408.repository;

import org.ironhack.lab408.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findByAuthorId(Long id);

    List<BlogPost> findByUserUsername(String username);
}

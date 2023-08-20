package com.example.reddit.repository;

import com.example.reddit.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post,Long> {

    List<Post> findByUserId(Long userId);
    Post findPostByid(Long id);
}

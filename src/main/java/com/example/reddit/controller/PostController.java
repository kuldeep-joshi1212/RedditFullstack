package com.example.reddit.controller;

import com.example.reddit.Exception.PostException;
import com.example.reddit.Exception.UserException;
import com.example.reddit.model.Post;
import com.example.reddit.service.PostsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@Log4j2
public class PostController {

    private final PostsService postsService;

    @Autowired
    public PostController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping(value = "/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Post>> getAllUserPosts(@RequestParam("username") String username) {
        try {
            return new ResponseEntity<>(postsService.getAllPostForUser(username), HttpStatus.OK);
        } catch (UserException e) {
            log.error("user not found for finding out posts with username {}", username);
            return new ResponseEntity<>(List.of(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Post> savePost(@RequestBody Post post,
                                  @RequestParam("username") String username) {
        try {
            return new ResponseEntity<>(postsService.savePost(post, username), HttpStatus.OK);
        } catch (PostException | UserException e) {
            return new ResponseEntity<>(post, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/vote")
    ResponseEntity voteOnPost(@RequestParam("postId") Long postId,
                              @RequestParam("username") String username,
                              @RequestParam("vote") String vote) {
        try {
            postsService.voteOnPost(postId, username, vote);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserException | PostException e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Post> getPostByid(@RequestParam("id") Long id) {
        try {
            return new ResponseEntity<>(postsService.getPostByid(id), HttpStatus.OK);
        } catch (PostException e) {
            log.error("invalid post id");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


}

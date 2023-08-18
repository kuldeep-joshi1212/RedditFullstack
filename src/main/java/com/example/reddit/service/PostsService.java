package com.example.reddit.service;

import com.example.reddit.Exception.PostException;
import com.example.reddit.Exception.UserException;
import com.example.reddit.model.Post;
import com.example.reddit.model.User;
import com.example.reddit.repository.PostRepository;
import com.example.reddit.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
public class PostsService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    @Autowired
    public PostsService(UserRepository userRepository,
                        PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<Post> getAllPostForUser(String username) throws UserException {
        if (Boolean.FALSE.equals(StringUtils.hasText(username))) {
            return List.of();
        }

        User user = getUser(username);

        return postRepository.findByUserId(user.getId());
    }

    private User getUser(String username) throws UserException {
        User user = userRepository.findByUsername(username);

        if (Objects.isNull(user)) {
            throw new UserException("user not found while fetching posts");
        }
        return user;
    }

    public Post savePost(Post post, String username) throws PostException, UserException {
        if (Boolean.FALSE.equals(StringUtils.hasText(post.getTitle()))
                || Boolean.FALSE.equals(StringUtils.hasText(post.getBody()))) {
            throw new PostException("Post object incorrect");
        }

        User user = getUser(username);
        post.setUser(user);
        return postRepository.save(post);
    }
    public void upvotePost(Long postId,String username) throws UserException,PostException{
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UserException("user not found while upvote");
        }
        Post post = postRepository.findPostByid(postId);
        if(Objects.isNull(post)){
            throw new PostException("Post with id: " + postId + " not found.");
        }
        long upvotes = 0;
        long downvotes = 0;
        if(post.getUpvotes()!=null  ){upvotes = post.getUpvotes();}
        if(post.getDownvotes()!=null){downvotes = post.getDownvotes();}
        if(!user.getUpvotes().contains(postId)){
            post.setUpvotes(upvotes+1);
            user.getUpvotes().add(postId);
        }
        if(user.getDownvotes().contains(postId)) {
            post.setDownvotes(downvotes-1);
            user.getDownvotes().remove(postId);
        }
        postRepository.save(post);
        userRepository.save(user);
    }
    public void downvotePost(Long postId,String username) throws UserException,PostException{
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UserException("user not found while upvote");
        }
        Post post = postRepository.findPostByid(postId);
        if(Objects.isNull(post)){
            throw new PostException("Post with id: " + postId + " not found.");
        }
        long upvotes = 0;
        long downvotes = 0;
        if(post.getUpvotes()!=null  ){upvotes = post.getUpvotes();}
        if(post.getDownvotes()!=null){downvotes = post.getDownvotes();}
        if(user.getUpvotes().contains(postId)){
            post.setUpvotes(upvotes-1);
            user.getUpvotes().remove(postId);
        }
        if(!user.getDownvotes().contains(postId)) {
            post.setDownvotes(downvotes+1);
            user.getDownvotes().add(postId);
        }
        postRepository.save(post);
        userRepository.save(user);
    }
}

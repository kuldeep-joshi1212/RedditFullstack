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
    public void upvotePost(Long postId,String username) throws UserException{
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UserException("user not found while upvote");
        }
        user.getDownvotes().remove(postId);
        user.getUpvotes().add(postId);
        userRepository.save(user);
    }
    public void downvotePost(Long postId,String username) throws UserException{
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UserException("user not found while upvote");
        }
        user.getUpvotes().remove(postId);
        user.getDownvotes().add(postId);
        userRepository.save(user);
    }
}

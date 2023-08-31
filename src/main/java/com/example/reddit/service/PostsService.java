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
        List<Post> posts = postRepository.findByUserId(user.getId());
        return updateIsLikedforPosts(posts, user); // Temporary return value to test isupvote/isdownvote functionality
    }

    List<Post> updateIsLikedforPosts(List<Post> posts, User user) {
        for (Post post : posts) {
            setVotes(post, user);
        }
        return posts;
    }

    Post setVotes(Post post, User user) {
        if (user.getUpvotes().contains(post.getId())) {
            post.setUpvote(Boolean.TRUE);
        }
        if (user.getDownvotes().contains(post.getId())) {
            post.setDownvote(Boolean.TRUE);
        }
        return post;
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

    public Post voteOnPost(Long postId, String username, String vote) throws UserException, PostException {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UserException("user not found while upvote");
        }
        Post post = postRepository.findPostByid(postId);
        if (Objects.isNull(post)) {
            throw new PostException("Post with id: " + postId + " not found.");
        }
        if (vote.equals("upvote")) {
            upvotePost(user, post, 0L, 0L);
        }
        if (vote.equals("downvote")) {
            downvotePost(user, post, 0L, 0L);
        }
        setVotes(post, user);
        return post;
    }

    public void upvotePost(User user, Post post, Long upvotes, Long downvotes) {
        if (Objects.nonNull(post.getUpvotes())) {
            upvotes = post.getUpvotes();
        }
        if (Objects.nonNull(post.getDownvotes())) {
            downvotes = post.getDownvotes();
        }
        if (!user.getUpvotes().contains(post.getId())) {
            post.setUpvotes(upvotes + 1);
            user.getUpvotes().add(post.getId());
        } else {
            post.setUpvotes(upvotes - 1);
            user.getUpvotes().remove(post.getId());
        }
        if (user.getDownvotes().contains(post.getId())) {
            post.setDownvotes(downvotes - 1);
            user.getDownvotes().remove(post.getId());
        }
        postRepository.save(post);
        userRepository.save(user);
    }

    public void downvotePost(User user, Post post, Long upvotes, Long downvotes) {
        if (Objects.nonNull(post.getUpvotes())) {
            upvotes = post.getUpvotes();
        }
        if (Objects.nonNull(post.getDownvotes())) {
            downvotes = post.getDownvotes();
        }
        if (user.getUpvotes().contains(post.getId())) {
            post.setUpvotes(upvotes - 1);
            user.getUpvotes().remove(post.getId());
        }
        if (!user.getDownvotes().contains(post.getId())) {
            post.setDownvotes(downvotes + 1);
            user.getDownvotes().add(post.getId());
        } else {
            post.setDownvotes(downvotes - 1);
            user.getDownvotes().remove(post.getId());
        }
        postRepository.save(post);
        userRepository.save(user);
    }

    public Post getPostByid(Long id, String username) throws PostException {
        if (id == null
                || Boolean.FALSE.equals(postRepository.existsById(id))) {
            throw new PostException("invalid post id");
        }
        User user = userRepository.findByUsername(username);
        Post post = postRepository.findPostByid(id);
        setVotes(post, user);
        return post;

    }
}

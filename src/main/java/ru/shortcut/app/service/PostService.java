package ru.shortcut.app.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shortcut.app.model.Post;
import ru.shortcut.app.repository.PostRepository;

import java.util.List;

@Service
@AllArgsConstructor

public class PostService {
    private PostRepository postRepository;

    public void save(Post post) {
        postRepository.save(post);
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public List<Post> getByUser(String username) {
        return postRepository.findByUser(username);
    }
}

package ru.shortcut.app.repository;

import org.springframework.stereotype.Repository;
import ru.shortcut.app.model.Post;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class PostRepository {

    private final ConcurrentHashMap<String, List<Post>> storage = new ConcurrentHashMap<>();

    public void save(Post post) {
        storage.computeIfAbsent(
                post.getUsername(), u ->new CopyOnWriteArrayList<>())
                .add(post);
    }

    public List<Post> findAll() {
        return storage.values().stream()
                .flatMap(List::stream).toList();
    }

    public List<Post> findByUser(String username) {
        return storage.getOrDefault(username, List.of());
    }
}

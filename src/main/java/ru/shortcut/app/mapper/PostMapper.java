package ru.shortcut.app.mapper;

import org.springframework.stereotype.Component;
import ru.shortcut.app.dto.PostRequest;
import ru.shortcut.app.dto.PostResponse;
import ru.shortcut.app.model.Post;

import java.util.List;

@Component
public class PostMapper {

    public Post toEntity(PostRequest request) {
        return new Post(request.getUsername(), request.getPost());
    }

    public PostResponse toDto(Post post) {
        return new PostResponse(post.getUsername(), post.getText());
    }

    public List<PostResponse> toDtoList(List<Post> posts) {
        return posts.stream()
                .map(this::toDto)
                .toList();
    }
}

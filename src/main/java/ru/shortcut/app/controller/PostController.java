package ru.shortcut.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shortcut.app.dto.MessageStatsResponse;
import ru.shortcut.app.dto.PostRequest;
import ru.shortcut.app.dto.PostResponse;
import ru.shortcut.app.mapper.MessageStatsMapper;
import ru.shortcut.app.mapper.PostMapper;
import ru.shortcut.app.model.Message;
import ru.shortcut.app.model.MessageStats;
import ru.shortcut.app.model.Post;
import ru.shortcut.app.service.MessageStatsService;
import ru.shortcut.app.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final MessageStatsService messageStatsService;
    private final MessageStatsMapper messageStatsMapper;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest request) {
        Post post = postMapper.toEntity(request);
        postService.save(post);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<PostResponse> getPosts(@RequestParam(required = false) String username) {
        if (username != null) {
            return postMapper.toDtoList(postService.getByUser(username));
        }
        return postMapper.toDtoList(postService.getAll());
    }

    @GetMapping("/stats")
    public MessageStatsResponse getStats() {
        List<Post> posts = postService.getAll();
        List<Message> messages = posts.stream()
                .map(p -> new Message(p.getUsername(),p.getText()))
                .toList();
        MessageStats stats = messageStatsService.calculate(messages);
        return messageStatsMapper.toDto(stats);
    }

}

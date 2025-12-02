package ru.shortcut.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@AllArgsConstructor
@Getter
public class MessageStats {
    private final String mostActiveUser;
    private final String longestMessage;
    private final List<String> top3Users;
}

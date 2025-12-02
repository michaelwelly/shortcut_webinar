package ru.shortcut.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MessageStatsResponse {
    private String mostActiveUser;
    private String longestMessage;
    private List<String> top3Users;
}

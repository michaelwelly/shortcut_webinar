package ru.shortcut.app.service;

import org.springframework.stereotype.Service;
import ru.shortcut.app.model.Message;
import ru.shortcut.app.model.MessageStats;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageStatsService {

    public MessageStats calculate(List<Message> messages) {

        Map<String,Long> count = messages.stream()
                .collect(Collectors.groupingBy(Message::getUser,
                        Collectors.counting()));

    String mostActive = count.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

    String longest = messages.stream()
            .max(Comparator.comparingInt(
                    m -> m.getText().length()))
            .map(Message::getText)
            .orElse(null);

    List<String> top3 = count.entrySet().stream()
            .sorted((a,b) -> Long.compare(b.getValue(),a.getValue()))
            .limit(3)
            .map(Map.Entry::getKey)
            .toList();

    return new MessageStats(mostActive, longest, top3);
    }
}

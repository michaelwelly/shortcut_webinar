package ru.shortcut.app.mapper;

import org.springframework.stereotype.Component;
import ru.shortcut.app.dto.MessageStatsResponse;
import ru.shortcut.app.model.MessageStats;

@Component
public class MessageStatsMapper {
    public MessageStatsResponse toDto(MessageStats stats) {
        return new MessageStatsResponse(
                stats.getMostActiveUser(),
                stats.getLongestMessage(),
                stats.getTop3Users()
        );
    }
}

import org.junit.jupiter.api.Test;
import ru.shortcut.app.model.Message;
import ru.shortcut.app.model.MessageStats;
import ru.shortcut.app.service.MessageStatsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageStatsServiceTest {

    private final MessageStatsService messageStatsService = new MessageStatsService();

    @Test
    void shouldFindMostActiveUser() {
        List<Message> messages = List.of(
                new Message("User1", "Hello"),
                new Message("User2", "Hi"),
                new Message("User1", "How are you?")
        );
        MessageStats stats = messageStatsService.calculate(messages);

        assertEquals("User1", stats.getMostActiveUser());
    }

    @Test
    void shouldNotFindLongestMessage() {
        List<Message> messages = List.of(
                new Message("User1", "Hi"),
                new Message("User2", "Hello world")
        );

        MessageStats stats = messageStatsService.calculate(messages);

        assertEquals("Hello world", stats.getLongestMessage());
    }

    @Test
    void shouldReturnTop3Users() {
        List<Message> messages = List.of(
                new Message("U1", "A"),
                new Message("U2", "B"),
                new Message("U1", "C"),
                new Message("U3", "D"),
                new Message("U3", "E"),
                new Message("U4", "F")
        );

        MessageStats stats = messageStatsService.calculate(messages);

        assertEquals(List.of("U1", "U3", "U2"), stats.getTop3Users());

    }

    @Test
    void shouldHandleEmptyMessages() {
        List<Message> messages = List.of();

        MessageStats stats = messageStatsService.calculate(messages);

        assertNull(stats.getMostActiveUser());
        assertNull(stats.getLongestMessage());
        assertTrue(stats.getTop3Users().isEmpty());
    }

    @Test
    void shouldWorkWithSingleUser() {
        List<Message> messages2 = List.of(
                new Message("Solo", "Test")
        );

        MessageStats stats = messageStatsService.calculate(messages2);

        assertEquals("Solo", stats.getMostActiveUser());
        assertEquals("Test", stats.getLongestMessage());
        assertEquals(List.of("Solo"), stats.getTop3Users());
    }
}


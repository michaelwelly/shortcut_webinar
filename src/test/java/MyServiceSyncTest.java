import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.shortcut.app.MyService;
import ru.shortcut.client.ExternalApiClient;
import ru.shortcut.properties.ServiceProperties;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;


/**
 * Этот тест проверяет, что при отключённом асинхронном режиме
 * (service.async.enabled = false) метод execute() выполняется синхронно:
 * 1) getData вызывается немедленно и только один раз,
 * 2) результат возвращается как уже завершённый CompletableFuture,
 * 3) строка результата имеет вид "Sync Processed: <DATA>".
 */

@ExtendWith(MockitoExtension.class)
public class MyServiceSyncTest {

    @Mock
    ExternalApiClient externalApiClient;

    @InjectMocks
    MyService myService;

    @BeforeEach
    void setup() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setAsyncEnabled(false);
        ReflectionTestUtils.setField(myService, "serviceProperties", serviceProperties);

    }

    @Test
    void excecute_shouldProcessSynchronously_whenAsyncDisabled() {
        String input  = "abc";
        when(externalApiClient.getData(input)).thenReturn("DATA");

        CompletableFuture<String>result = myService.execute(input);

        assertTrue(result.isDone());
        assertEquals("Sync Processed: DATA", result.join());
        verify(externalApiClient,times(1)).getData(input);
    }


}

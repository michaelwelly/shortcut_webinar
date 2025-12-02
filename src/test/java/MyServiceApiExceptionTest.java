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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Этот тест проверяет, что MyService корректно обрабатывает ошибку внешнего API.
 * Логика:
 * 1) externalApiClient.getData(input) бросает RuntimeException("API FAILED"),
 *    что эмулируется через Mockito.
 * 2) Метод execute() возвращает CompletableFuture, и попытка вызвать future.join()
 *    должна привести к выбросу исключения (RuntimeException или CompletionException),
 *    которое мы перехватываем через assertThrows().
 * 3) Мы проверяем, что корневое исключение — RuntimeException с сообщением "API FAILED".
 * 4) Также проверяем, что внешний API вызывается ровно один раз.
 * Тест гарантирует, что при ошибке в ExternalApiClient сервис не скрывает исключения
 * и корректно пробрасывает их наружу.
 */


@ExtendWith(MockitoExtension.class)
public class MyServiceApiExceptionTest {

    @Mock
    ExternalApiClient externalApiClient;

    @InjectMocks
    MyService myService;

    @BeforeEach
    void setup() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setAsyncEnabled(true);
        ReflectionTestUtils.setField(myService, "serviceProperties", serviceProperties);
    }

    @Test
    void execute_shouldCompleteExceptionally_whenExternalApiThrows() {
        String input = "boom";

        when(externalApiClient.getData(input)).thenThrow(new RuntimeException("API FAILED"));

        CompletableFuture<String> future = myService.execute(input);

        Throwable t = assertThrows(Throwable.class, future::join);
        assertEquals("API FAILED", t.getCause().getMessage());

        verify(externalApiClient, times(1)).getData(input);

    }
}

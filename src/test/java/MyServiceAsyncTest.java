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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Этот тест проверяет, что при включённом асинхронном режиме
 * (service.async.enabled = true) метод execute() работает асинхронно:
 * 1) Логика запускается через CompletableFuture.supplyAsync(),
 *    поэтому future не должен быть завершён сразу (isDone() == false).
 * 2) Возвращаемое значение формируется только после выполнения лямбды
 *    в отдельном потоке, поэтому мы используем future.get(...) с таймаутом.
 * 3) Строка результата должна иметь вид "Async Processed: <DATA>".
 * 4) Метод externalApiClient.getData(input) вызывается ровно один раз,
 *    даже при асинхронной обработке.
 * Таким образом тест гарантирует корректное переключение MyService
 * в асинхронный режим и проверяет отложенное выполнение кода.
 */

@ExtendWith(MockitoExtension.class)
public class MyServiceAsyncTest {

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
    void excecute_shouldProcessAsynchronously_whenAsyncEnabled() throws ExecutionException,
            InterruptedException,
            TimeoutException {
        String input = "xyz";
        when(externalApiClient.getData(input)).thenAnswer(invocation -> {
            Thread.sleep(50);
            return "DATA";
        });

        CompletableFuture<String> future = myService.execute(input);

        assertFalse(future.isDone(), "Future should not be done");

        String result = future.get(300, TimeUnit.MILLISECONDS);
        assertEquals("Async Processed: DATA", result);
        verify(externalApiClient, times(1)).getData(input);
    }
}

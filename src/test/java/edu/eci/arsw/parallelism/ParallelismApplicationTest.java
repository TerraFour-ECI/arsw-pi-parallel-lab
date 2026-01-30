package edu.eci.arsw.parallelism;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParallelismApplication Unit Tests")
class ParallelismApplicationTest {

    @Test
    @DisplayName("Should create application instance")
    void shouldCreateApplicationInstance() {
        ParallelismApplication application = new ParallelismApplication();
        assertNotNull(application);
    }

    @Test
    @DisplayName("Main method should not throw exception")
    void mainMethodShouldNotThrowException() {
        assertDoesNotThrow(() -> ParallelismApplication.main(new String[]{}));
    }
}

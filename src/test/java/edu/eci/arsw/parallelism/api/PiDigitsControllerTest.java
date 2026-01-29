package edu.eci.arsw.parallelism.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("PiDigitsController Integration Tests")
class PiDigitsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ========== Happy Path Tests ==========

    @Test
    @DisplayName("Should return Pi digits successfully with valid parameters")
    void shouldReturnDigits() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.start").value(0))
                .andExpect(jsonPath("$.count").value(5))
                .andExpect(jsonPath("$.digits").value("243F6"));
    }

    @Test
    @DisplayName("Should return single digit")
    void shouldReturnSingleDigit() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").value(0))
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.digits").value("2"));
    }

    @ParameterizedTest
    @DisplayName("Should return correct digits for various start positions")
    @CsvSource({
        "0, 5, 243F6",
        "5, 5, A8885",
        "10, 3, A30",
        "100, 4, 29B7"
    })
    void shouldReturnDigitsFromDifferentPositions(int start, int count, String expected) 
            throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", String.valueOf(start))
                .param("count", String.valueOf(count)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digits").value(expected));
    }

    @Test
    @DisplayName("Should handle large count values")
    void shouldHandleLargeCount() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(100))
                .andExpect(jsonPath("$.digits").value(hasLength(100)));
    }

    // ========== Validation Tests ==========

    @Test
    @DisplayName("Should return 400 for negative start position")
    void shouldReturnBadRequestForNegativeStart() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "-1")
                .param("count", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for negative count")
    void shouldReturnBadRequestForNegativeCount() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for zero count")
    void shouldReturnBadRequestForZeroCount() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when start parameter is missing")
    void shouldReturnBadRequestWhenStartMissing() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("count", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when count parameter is missing")
    void shouldReturnBadRequestWhenCountMissing() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for invalid parameter types")
    void shouldReturnBadRequestForInvalidParameterTypes() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "abc")
                .param("count", "xyz"))
                .andExpect(status().isBadRequest());
    }

    // ========== Response Format Tests ==========

    @Test
    @DisplayName("Should return response with correct JSON structure")
    void shouldReturnCorrectJsonStructure() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.count").exists())
                .andExpect(jsonPath("$.digits").exists())
                .andExpect(jsonPath("$.start").isNumber())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.digits").isString());
    }

    @Test
    @DisplayName("Should return only hexadecimal characters in digits field")
    void shouldReturnOnlyHexCharacters() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digits").value(matchesPattern("[0-9A-F]+")));
    }

    // ========== Consistency Tests ==========

    @Test
    @DisplayName("Should return consistent results for repeated requests")
    void shouldReturnConsistentResults() throws Exception {
        String result1 = mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "10")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String result2 = mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "10")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result1, result2, "Results should be consistent");
    }

    // ========== Content Type Tests ==========

    @Test
    @DisplayName("Should accept requests with various acceptable headers")
    void shouldAcceptRequestWithAcceptHeader() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

// ========== Phase 1 - New Parameters Tests ==========


    @Test
    @DisplayName("Should work without threads and strategy parameters (backward compatibility)")
    void shouldWorkWithoutOptionalParameters() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digits").value("243F6"));
    }


    @Test
    @DisplayName("Should work with strategy=sequential")
    void shouldWorkWithSequentialStrategy() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "5")
                .param("strategy", "sequential"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digits").value("243F6"));
    }


    @Test
    @DisplayName("Should ignore threads parameter when strategy is sequential")
    void shouldIgnoreThreadsWithSequentialStrategy() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "5")
                .param("strategy", "sequential")
                .param("threads", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digits").value("243F6"));
    }


    @Test
    @DisplayName("Should accept threads strategy with valid threads parameter")
    void shouldAcceptThreadsStrategyWithValidThreads() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "5")
                .param("strategy", "threads")
                .param("threads", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digits").value("243F6")); 
    }


    @Test
    @DisplayName("Should return same digits with and without strategy parameter")
    void shouldReturnSameDigitsWithAndWithoutStrategy() throws Exception {
        String result1 = mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String result2 = mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "10")
                .param("strategy", "sequential"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result1, result2);
    }

    @Test
    @DisplayName("Should accept threads=1 (minimum valid)")
    void shouldAcceptThreadsOne() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "5")
                .param("strategy", "threads")
                .param("threads", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digits").value("243F6"));
    }

    @Test
    @DisplayName("Should accept threads=200 (maximum valid)")
    void shouldAcceptThreadsMax() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                .param("start", "0")
                .param("count", "5")
                .param("strategy", "threads")
                .param("threads", "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digits").value("243F6"));
    }
}
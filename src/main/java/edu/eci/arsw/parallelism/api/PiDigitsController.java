
package edu.eci.arsw.parallelism.api;

import edu.eci.arsw.parallelism.core.PiDigitsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pi")
@Tag(name = "Pi Digits", description = "API for calculating digits of Pi")
public class PiDigitsController {

    private final PiDigitsService service;

    public PiDigitsController(PiDigitsService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get digits of Pi",
            description = "Calculates and returns a sequence of digits of Pi starting from a given position"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully calculated Pi digits",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PiResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters (start must be >= 0, count must be >= 1)",
                    content = @Content
            )
    })
    @GetMapping("/digits")
    public PiResponse digits(
            @Parameter(description = "Starting position for Pi digits (0-indexed)", example = "0")
            @RequestParam @Min(0) int start,
            @Parameter(description = "Number of digits to calculate", example = "10")
            @RequestParam @Min(1) int count,
            @Parameter(description = "Number of threads to use (optional, must be > 0)", example = "4")
            @RequestParam(required = false) @Min(1) Integer threads,
            @Parameter(description = "Calculation strategy: 'sequential' or 'threads' (optional)", example = "threads")
            @RequestParam(required = false) String strategy
    ) {
        String digits = service.calculateSequential(start, count);
        return new PiResponse(start, count, digits);
    }
}

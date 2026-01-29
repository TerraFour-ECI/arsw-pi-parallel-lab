
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
            description = """
                Calculates and returns a sequence of hexadecimal digits of Pi starting from a given position.
                
                Supports both sequential and parallel calculation strategies:
                - Without 'strategy' parameter: Uses sequential calculation (default)
                - With 'strategy=sequential': Explicitly uses sequential calculation
                - With 'strategy=threads': Uses parallel calculation with specified number of threads
                
                For parallel calculation, the 'threads' parameter is required and must be between 1 and 200.
                """
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
                    description = """
                        Invalid parameters:
                        - start must be >= 0
                        - count must be >= 1
                        - threads must be > 0 (if provided)
                        - strategy must be 'sequential' or 'threads'
                        - threads is required when strategy='threads'
                        """,
                    content = @Content
            )
    })
    @GetMapping("/digits")
    public PiResponse digits(
            @Parameter(description = "Starting position for Pi digits (0-indexed)", example = "0", required = true)
            @RequestParam @Min(0) int start,
            @Parameter(description = "Number of digits to calculate", example = "10", required = true)
            @RequestParam @Min(1) int count,
            @Parameter(description = "Number of threads to use (optional, must be > 0)", example = "4", required = false)
            @RequestParam(required = false) @Min(1) Integer threads,
            @Parameter(description = "Calculation strategy: 'sequential' or 'threads' (optional)", example = "threads", schema = @Schema(allowableValues = {"sequential", "threads"}), required = false)
            @RequestParam(required = false) String strategy
    ) {
        String digits = service.calculateWithStrategy(start, count, threads, strategy);
        return new PiResponse(start, count, digits);
    }
}

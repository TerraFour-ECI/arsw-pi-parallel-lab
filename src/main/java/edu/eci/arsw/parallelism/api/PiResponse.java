
package edu.eci.arsw.parallelism.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing calculated digits of Pi")
public record PiResponse(
        @Schema(description = "Starting position of the calculated digits", example = "0")
        int start,
        @Schema(description = "Number of digits calculated", example = "10")
        int count,
        @Schema(description = "The calculated digits of Pi", example = "3141592653")
        String digits
) {
}

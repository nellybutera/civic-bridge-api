package africa.civicbridge.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record QuizResultRequest(
        @NotNull(message = "userId is required")
        @Schema(example = "3", description = "The id from your /api/auth/login or /api/auth/signup response.")
        Long userId,

        @NotNull(message = "scorePercent is required")
        @Min(value = 0, message = "scorePercent must be between 0 and 100")
        @Max(value = 100, message = "scorePercent must be between 0 and 100")
        @Schema(example = "67")
        Integer scorePercent
) {}

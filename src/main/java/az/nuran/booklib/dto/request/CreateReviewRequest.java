package az.nuran.booklib.dto.request;

import az.nuran.booklib.validation.ValidationConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReviewRequest(
        @Min(ValidationConstants.REVIEW_RATING_MIN_VALUE)
        @Max(ValidationConstants.REVIEW_RATING_MAX_VALUE)
        @NotNull
        Integer rating,

        @Size(max = ValidationConstants.REVIEW_COMMENT_MAX_LENGTH)
        @NotBlank
        String comment
) {}

package az.nuran.booklib.dto.request;

import az.nuran.booklib.validation.ValidationConstants;
import az.nuran.booklib.validation.NotFutureYear;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBookRequest(
        @Size(max = ValidationConstants.BOOK_TITLE_MAX_LENGTH)
        @NotBlank
        String title,

        @Size(max = ValidationConstants.BOOK_AUTHOR_MAX_LENGTH)
        @NotBlank
        String author,

        @Min(ValidationConstants.BOOK_PUBLICATION_YEAR_MIN_VALUE)
        @NotFutureYear
        @NotNull
        Integer publicationYear,

        @Min(ValidationConstants.BOOK_AVAILABLE_COPIES_MIN_VALUE)
        @NotNull
        Integer availableCopies
) {
}

package az.nuran.booklib.dto.request;

import az.nuran.booklib.validation.ValidationConstants;
import az.nuran.booklib.validation.NotFutureYear;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateBookRequest(
        @Size(max = ValidationConstants.BOOK_TITLE_MAX_LENGTH)
        String title,

        @Size(max = ValidationConstants.BOOK_AUTHOR_MAX_LENGTH)
        String author,

        @Min(ValidationConstants.BOOK_PUBLICATION_YEAR_MIN_VALUE)
        @NotFutureYear
        Integer publicationYear,

        @Min(ValidationConstants.BOOK_AVAILABLE_COPIES_MIN_VALUE)
        Integer availableCopies
) {
}

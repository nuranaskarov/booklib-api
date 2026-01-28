package az.nuran.booklib.dto.response;

import java.util.UUID;

public record BookResponse(
        UUID id,
        String title,
        String author,
        Integer publicationYear,
        Integer availableCopies
) {}

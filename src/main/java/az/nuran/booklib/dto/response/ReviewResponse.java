package az.nuran.booklib.dto.response;

import java.util.UUID;

public record ReviewResponse(
        UUID id,
        Integer rating,
        String comment
) {}

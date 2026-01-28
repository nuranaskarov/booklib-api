package az.nuran.booklib.dto.response;

import java.util.Map;

public record ErrorsResponse(
        Map<String, String> errors
) {}

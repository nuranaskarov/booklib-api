package az.nuran.booklib.exception;

import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

public class ReviewNotFoundException extends EntityNotFoundException {
    public ReviewNotFoundException(UUID reviewId) {
        super("Review with ID '%s' not found".formatted(reviewId));
    }
}

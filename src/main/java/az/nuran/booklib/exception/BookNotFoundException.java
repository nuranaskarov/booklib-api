package az.nuran.booklib.exception;

import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

public class BookNotFoundException extends EntityNotFoundException {
    public BookNotFoundException(UUID bookId) {
        super("Book with ID '%s' not found".formatted(bookId));
    }
}

package az.nuran.booklib.controller;

import az.nuran.booklib.api.BookApi;
import az.nuran.booklib.dto.request.CreateBookRequest;
import az.nuran.booklib.dto.request.UpdateBookRequest;
import az.nuran.booklib.dto.response.BookAverageRatingResponse;
import az.nuran.booklib.dto.response.BookResponse;
import az.nuran.booklib.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController implements BookApi {
    private final BookService bookService;

    @Override
    public ResponseEntity<Void> createBook(CreateBookRequest request) {
        UUID bookId = bookService.createBook(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/books/{bookId}")
                .buildAndExpand(bookId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public List<BookResponse> listBooks() {
        return bookService.listBooks();
    }

    @Override
    public List<BookResponse> listBooksPublishedAfter(Integer year) {
        return bookService.listBooksAfterYear(year);
    }

    @Override
    public List<BookAverageRatingResponse> listAverageRatingsPerBook() {
        return bookService.listAverageRatingsPerBook();
    }

    @Override
    public List<BookResponse> listBooksWithHighestRatingSQL() {
        return bookService.listBooksWithHighestRatingSQL();
    }

    @Override
    public List<BookResponse> listBooksWithHighestRatingJPQL() {
        return bookService.listBooksWithHighestRatingJPQL();
    }

    @Override
    public List<BookResponse> searchBooks(String query) {
        return bookService.searchBooks(query);
    }

    @Override
    public BookResponse getBook(UUID bookId) {
        return bookService.getBook(bookId);
    }

    @Override
    public void updateBook(UUID bookId, UpdateBookRequest request) {
        bookService.updateBook(bookId, request);
    }

    @Override
    public void deleteBook(UUID bookId) {
        bookService.deleteBook(bookId);
    }
}

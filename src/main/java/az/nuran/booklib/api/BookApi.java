package az.nuran.booklib.api;

import az.nuran.booklib.dto.request.CreateBookRequest;
import az.nuran.booklib.dto.request.UpdateBookRequest;
import az.nuran.booklib.dto.response.BookAverageRatingResponse;
import az.nuran.booklib.dto.response.BookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;

@Tag(name = "Book", description = "Operations related to book management")
@RequestMapping("/books")
public interface BookApi {

    @Operation(summary = "Creates a new book")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> createBook(@Valid @RequestBody CreateBookRequest request);

    @Operation(summary = "Returns a list of all books")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<BookResponse> listBooks();

    @Operation(summary = "Returns a list of all books published after a specified year")
    @GetMapping("/published-after/{year}")
    @ResponseStatus(HttpStatus.OK)
    List<BookResponse> listBooksPublishedAfter(@PathVariable("year") Integer year);

    @Operation(summary = "Returns a list of all books and their average rating")
    @GetMapping("/average-rating")
    @ResponseStatus(HttpStatus.OK)
    List<BookAverageRatingResponse> listAverageRatingsPerBook();

    @Operation(summary = "Returns books with rating higher than 4 using SQL query")
    @GetMapping("/highest-rating/sql")
    @ResponseStatus(HttpStatus.OK)
    List<BookResponse> listBooksWithHighestRatingSQL();

    @Operation(summary = "Returns books with rating higher than 4 using JGPL query")
    @GetMapping("/highest-rating/jgpl")
    @ResponseStatus(HttpStatus.OK)
    List<BookResponse> listBooksWithHighestRatingJPQL();

    @Operation(summary = "Searches books by title or author")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    List<BookResponse> searchBooks(@RequestParam String query);

    @Operation(summary = "Returns a book by its ID")
    @GetMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    BookResponse getBook(@PathVariable UUID bookId);

    @Operation(summary = "Updates a book by its ID")
    @PatchMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateBook(@PathVariable UUID bookId, @Valid @RequestBody UpdateBookRequest request);

    @Operation(summary = "Deletes a book by its ID")
    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBook(@PathVariable UUID bookId);
}

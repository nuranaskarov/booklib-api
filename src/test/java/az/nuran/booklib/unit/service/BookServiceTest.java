package az.nuran.booklib.unit.service;

import az.nuran.booklib.dto.request.CreateBookRequest;
import az.nuran.booklib.dto.request.UpdateBookRequest;
import az.nuran.booklib.dto.response.BookAverageRatingResponse;
import az.nuran.booklib.dto.response.BookResponse;
import az.nuran.booklib.entity.Book;
import az.nuran.booklib.exception.BookNotFoundException;
import az.nuran.booklib.mapper.BookMapper;
import az.nuran.booklib.repository.BookRepository;
import az.nuran.booklib.service.BookService;
import az.nuran.booklib.unit.BaseUT;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookServiceTest extends BaseUT {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void createBook_shouldCreateBookSuccessfully() {
        // Given

        CreateBookRequest request = new CreateBookRequest("", "", 1, 1);
        Book book = Book.builder().id(UUID.randomUUID()).build();

        when(bookMapper.toEntity(request)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        // When

        UUID result = bookService.createBook(request);

        // Then

        assertThat(result).isEqualTo(book.getId());
        verify(bookRepository).save(book);
    }

    @Test
    void getBook_returnsBookWhenExists() {
        // Given

        Book book = Book.builder().id(UUID.randomUUID()).build();
        BookResponse response = new BookResponse(book.getId(), "", "", 1, 1);

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toResponse(book)).thenReturn(response);

        // When

        BookResponse result = bookService.getBook(book.getId());

        // Then

        assertThat(result).isEqualTo(response);
    }

    @Test
    void getBook_throwsExceptionWhenNotFound() {
        // Given

        UUID bookId = UUID.randomUUID();
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When / Then

        assertThatThrownBy(() -> bookService.getBook(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining(bookId.toString());
    }

    @Test
    void updateBook_updatesBookSuccessfully() {
        // Given

        Book book = Book.builder().id(UUID.randomUUID()).build();
        UpdateBookRequest request = new UpdateBookRequest("", "", 1, 1);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        // When

        bookService.updateBook(book.getId(), request);

        // Then

        verify(bookMapper).patch(request, book);
        verify(bookRepository).save(book);
    }

    @Test
    void listBooksAfterYear_returnsFilteredBooks() {
        // Given

        Integer year = 1950;
        Book book = Book.builder().title("Tutunamayanlar").author("Oguz Altay").publicationYear(1971).availableCopies(3).build();
        BookResponse response = new BookResponse(null, "Tutunamayanlar", "Oguz Altay", 1971, 3);

        when(bookRepository.findAllByPublicationYearAfter(year)).thenReturn(List.of(book));
        when(bookMapper.toResponse(book)).thenReturn(response);

        // When

        List<BookResponse> result = bookService.listBooksAfterYear(year);

        // Then

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(response);
    }

    @Test
    void listAverageRatingsPerBook_returnsRatings() {
        // Given

        BookAverageRatingResponse rating1 = new BookAverageRatingResponse("Layla and Majnun", 4.5);
        BookAverageRatingResponse rating2 = new BookAverageRatingResponse("Ali and Nino", 4.8);

        when(bookRepository.findAverageRatingPerBook()).thenReturn(List.of(rating1, rating2));

        // When

        List<BookAverageRatingResponse> result = bookService.listAverageRatingsPerBook();

        // Then

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(rating1, rating2);
    }

    @Test
    void listBooksWithHighestRatingSQL_returnsHighlyRatedBooks() {
        // Given

        Book book1 = Book.builder().title("Ali and Nino").author("Kurban Said").publicationYear(1937).availableCopies(7).build();
        Book book2 = Book.builder().title("Layla and Majnun").author("Nizami Ganjavi").publicationYear(1188).availableCopies(5).build();

        BookResponse response1 = new BookResponse(null, "Ali and Nino", "Kurban Said", 1937, 7);
        BookResponse response2 = new BookResponse(null, "Layla and Majnun", "Nizami Ganjavi", 1188, 5);

        when(bookRepository.findBooksWithHighRatingsSQL()).thenReturn(List.of(book1, book2));
        when(bookMapper.toResponse(book1)).thenReturn(response1);
        when(bookMapper.toResponse(book2)).thenReturn(response2);

        // When

        List<BookResponse> result = bookService.listBooksWithHighestRatingSQL();

        // Then

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(response1, response2);
    }

    @Test
    void listBooksWithHighestRatingJPQL_returnsHighlyRatedBooks() {
        // Given

        Book book1 = Book.builder().title("Ali and Nino").author("Kurban Said").publicationYear(1937).availableCopies(7).build();
        Book book2 = Book.builder().title("Layla and Majnun").author("Nizami Ganjavi").publicationYear(1188).availableCopies(5).build();

        BookResponse response1 = new BookResponse(null, "Ali and Nino", "Kurban Said", 1937, 7);
        BookResponse response2 = new BookResponse(null, "Layla and Majnun", "Nizami Ganjavi", 1188, 5);

        when(bookRepository.findBooksWithHighRatingsJPQL()).thenReturn(List.of(book1, book2));
        when(bookMapper.toResponse(book1)).thenReturn(response1);
        when(bookMapper.toResponse(book2)).thenReturn(response2);

        // When

        List<BookResponse> result = bookService.listBooksWithHighestRatingJPQL();

        // Then

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(response1, response2);
    }

    @Test
    void searchBooks_returnsBooksMatchingQuery() {
        // Given

        String query = "  Ali  ";
        Book book = Book.builder().title("Ali and Nino").author("Kurban Said").publicationYear(1937).availableCopies(7).build();
        BookResponse response = new BookResponse(null, "Ali and Nino", "Kurban Said", 1937, 7);

        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase("Ali", "Ali"))
                .thenReturn(List.of(book));
        when(bookMapper.toResponse(book)).thenReturn(response);

        // When

        List<BookResponse> result = bookService.searchBooks(query);

        // Then

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(response);
    }

    @Test
    void deleteBook_deletesBookWhenExists() {
        // Given

        Book book = Book.builder().id(UUID.randomUUID()).build();
        when(bookRepository.existsById(book.getId())).thenReturn(true);

        // When

        bookService.deleteBook(book.getId());

        // Then

        verify(bookRepository).deleteById(book.getId());
    }

    @Test
    void deleteBook_throwsExceptionWhenNotFound() {
        // Given

        UUID bookId = UUID.randomUUID();

        when(bookRepository.existsById(bookId)).thenReturn(false);

        // When / Then

        assertThatThrownBy(() -> bookService.deleteBook(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining(bookId.toString());
    }

    @Test
    void listBooks_returnsAllBooks() {
        // Given

        Book book1 = Book.builder().title("Layla and Majnun").author("Nizami Ganjavi").publicationYear(1188).availableCopies(5).build();
        Book book2 = Book.builder().title("Ali and Nino").author("Kurban Said").publicationYear(1937).availableCopies(7).build();
        Book book3 = Book.builder().title("Tutunamayanlar").author("Oguz Altay").publicationYear(1971).availableCopies(3).build();

        BookResponse response1 = new BookResponse(null, "Layla and Majnun", "Nizami Ganjavi", 1188, 5);
        BookResponse response2 = new BookResponse(null, "Ali and Nino", "Kurban Said", 1937, 7);
        BookResponse response3 = new BookResponse(null, "Tutunamayanlar", "Oguz Altay", 1971, 3);

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2, book3));
        when(bookMapper.toResponse(book1)).thenReturn(response1);
        when(bookMapper.toResponse(book2)).thenReturn(response2);
        when(bookMapper.toResponse(book3)).thenReturn(response3);

        // When

        List<BookResponse> result = bookService.listBooks();

        // Then

        assertThat(result)
                .extracting(
                        BookResponse::title,
                        BookResponse::author,
                        BookResponse::publicationYear,
                        BookResponse::availableCopies
                )
                .containsExactlyInAnyOrder(
                        tuple("Layla and Majnun", "Nizami Ganjavi", 1188, 5),
                        tuple("Ali and Nino", "Kurban Said", 1937, 7),
                        tuple("Tutunamayanlar", "Oguz Altay", 1971, 3)
                );

    }
}

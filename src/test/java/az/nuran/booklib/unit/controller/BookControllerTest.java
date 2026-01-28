package az.nuran.booklib.unit.controller;

import az.nuran.booklib.controller.BookController;
import az.nuran.booklib.dto.request.CreateBookRequest;
import az.nuran.booklib.dto.request.UpdateBookRequest;
import az.nuran.booklib.dto.response.BookAverageRatingResponse;
import az.nuran.booklib.dto.response.BookResponse;
import az.nuran.booklib.exception.BookNotFoundException;
import az.nuran.booklib.service.BookService;
import az.nuran.booklib.unit.BaseUT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest extends BaseUT {

    @MockitoBean
    private BookService bookService;

    @Test
    void createBook_shouldReturnCreatedWithLocation() throws Exception {
        // Given

        UUID bookId = UUID.randomUUID();
        CreateBookRequest request = new CreateBookRequest("1984", "George Orwell", 1949, 5);

        when(bookService.createBook(any(CreateBookRequest.class))).thenReturn(bookId);

        // When / Then

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/books/" + bookId));

        verify(bookService).createBook(any(CreateBookRequest.class));
    }

    @Test
    void listBooks_shouldReturnAllBooks() throws Exception {
        // Given

        BookResponse book1 = new BookResponse(UUID.randomUUID(), "1984", "George Orwell", 1949, 5);
        BookResponse book2 = new BookResponse(UUID.randomUUID(), "Animal Farm", "George Orwell", 1945, 3);

        when(bookService.listBooks()).thenReturn(List.of(book1, book2));

        // When / Then

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("1984"))
                .andExpect(jsonPath("$[0].author").value("George Orwell"))
                .andExpect(jsonPath("$[1].title").value("Animal Farm"));

        verify(bookService).listBooks();
    }

    @Test
    void listBooksPublishedAfter_shouldReturnFilteredBooks() throws Exception {
        // Given

        Integer year = 1940;
        BookResponse book = new BookResponse(UUID.randomUUID(), "1984", "George Orwell", 1949, 5);

        when(bookService.listBooksAfterYear(year)).thenReturn(List.of(book));

        // When / Then

        mockMvc.perform(get("/books/published-after/{year}", year))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("1984"))
                .andExpect(jsonPath("$[0].publicationYear").value(1949));

        verify(bookService).listBooksAfterYear(year);
    }

    @Test
    void listAverageRatingsPerBook_shouldReturnRatings() throws Exception {
        // Given

        BookAverageRatingResponse rating1 = new BookAverageRatingResponse("1984", 4.5);
        BookAverageRatingResponse rating2 = new BookAverageRatingResponse("Animal Farm", 4.8);

        when(bookService.listAverageRatingsPerBook()).thenReturn(List.of(rating1, rating2));

        // When / Then

        mockMvc.perform(get("/books/average-rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("1984"))
                .andExpect(jsonPath("$[0].averageRating").value(4.5))
                .andExpect(jsonPath("$[1].title").value("Animal Farm"))
                .andExpect(jsonPath("$[1].averageRating").value(4.8));

        verify(bookService).listAverageRatingsPerBook();
    }

    @Test
    void listBooksWithHighestRatingSQL_shouldReturnHighlyRatedBooks() throws Exception {
        // Given

        BookResponse book = new BookResponse(UUID.randomUUID(), "1984", "George Orwell", 1949, 5);

        when(bookService.listBooksWithHighestRatingSQL()).thenReturn(List.of(book));

        // When / Then

        mockMvc.perform(get("/books/highest-rating/sql"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("1984"));

        verify(bookService).listBooksWithHighestRatingSQL();
    }

    @Test
    void listBooksWithHighestRatingJPQL_shouldReturnHighlyRatedBooks() throws Exception {
        // Given

        BookResponse book = new BookResponse(UUID.randomUUID(), "1984", "George Orwell", 1949, 5);

        when(bookService.listBooksWithHighestRatingJPQL()).thenReturn(List.of(book));

        // When / Then

        mockMvc.perform(get("/books/highest-rating/jgpl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("1984"));

        verify(bookService).listBooksWithHighestRatingJPQL();
    }

    @Test
    void searchBooks_shouldReturnMatchingBooks() throws Exception {
        // Given

        String query = "Orwell";
        BookResponse book = new BookResponse(UUID.randomUUID(), "1984", "George Orwell", 1949, 5);

        when(bookService.searchBooks(query)).thenReturn(List.of(book));

        // When / Then

        mockMvc.perform(get("/books/search")
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author").value("George Orwell"));

        verify(bookService).searchBooks(query);
    }

    @Test
    void getBook_shouldReturnBook() throws Exception {
        // Given

        UUID bookId = UUID.randomUUID();
        BookResponse book = new BookResponse(bookId, "1984", "George Orwell", 1949, 5);

        when(bookService.getBook(bookId)).thenReturn(book);

        // When / Then

        mockMvc.perform(get("/books/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId.toString()))
                .andExpect(jsonPath("$.title").value("1984"))
                .andExpect(jsonPath("$.author").value("George Orwell"));

        verify(bookService).getBook(bookId);
    }

    @Test
    void getBook_shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        // Given

        UUID bookId = UUID.randomUUID();

        when(bookService.getBook(bookId)).thenThrow(new BookNotFoundException(bookId));

        // When / Then

        mockMvc.perform(get("/books/{bookId}", bookId))
                .andExpect(status().isNotFound());

        verify(bookService).getBook(bookId);
    }

    @Test
    void updateBook_shouldReturnNoContent() throws Exception {
        // Given

        UUID bookId = UUID.randomUUID();
        UpdateBookRequest request = new UpdateBookRequest("1984 Updated", "George Orwell", 1949, 10);

        // When / Then

        mockMvc.perform(patch("/books/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(bookService).updateBook(eq(bookId), any(UpdateBookRequest.class));
    }

    @Test
    void updateBook_shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        // Given

        UUID bookId = UUID.randomUUID();
        UpdateBookRequest request = new UpdateBookRequest("1984 Updated", "George Orwell", 1949, 10);

        doThrow(new BookNotFoundException(bookId)).when(bookService).updateBook(eq(bookId), any(UpdateBookRequest.class));

        // When / Then

        mockMvc.perform(patch("/books/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(bookService).updateBook(eq(bookId), any(UpdateBookRequest.class));
    }

    @Test
    void deleteBook_shouldReturnNoContent() throws Exception {
        // Given

        UUID bookId = UUID.randomUUID();

        // When / Then

        mockMvc.perform(delete("/books/{bookId}", bookId))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(bookId);
    }

    @Test
    void deleteBook_shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        // Given

        UUID bookId = UUID.randomUUID();

        doThrow(new BookNotFoundException(bookId)).when(bookService).deleteBook(bookId);

        // When / Then

        mockMvc.perform(delete("/books/{bookId}", bookId))
                .andExpect(status().isNotFound());

        verify(bookService).deleteBook(bookId);
    }
}

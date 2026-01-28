package az.nuran.booklib.integration.book;

import az.nuran.booklib.dto.request.UpdateBookRequest;
import az.nuran.booklib.entity.Book;
import az.nuran.booklib.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateBookIT extends BaseIT {

    @Test
    void shouldUpdateBookSuccessfully() throws Exception {
        // Given

        Book book = Book.builder()
                .title("Brave New World")
                .author("Aldous Huxley")
                .publicationYear(1932)
                .availableCopies(8)
                .build();

        bookRepository.save(book);

        // When

        mockMvc.perform(patch("/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UpdateBookRequest(
                                        "Amazing New World",
                                        "Mr. Good",
                                        2026,
                                        1000
                                )
                        )))
                .andExpect(status().isNoContent());

        // Then

        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();

        assertThat(updatedBook)
                .extracting(
                        Book::getTitle,
                        Book::getAuthor,
                        Book::getPublicationYear,
                        Book::getAvailableCopies
                )
                .containsExactly(
                        "Amazing New World",
                        "Mr. Good",
                        2026,
                        1000
                );
    }
}

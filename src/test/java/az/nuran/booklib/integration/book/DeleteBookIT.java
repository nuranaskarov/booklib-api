package az.nuran.booklib.integration.book;

import az.nuran.booklib.entity.Book;
import az.nuran.booklib.integration.BaseIT;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteBookIT extends BaseIT {

    @Test
    void shouldDeleteBookSuccessfully() throws Exception {
        // Given

        Book book = Book.builder()
                .title("Animal Farm")
                .author("George Orwell")
                .publicationYear(1945)
                .availableCopies(1)
                .build();

        bookRepository.save(book);

        // When

        mockMvc.perform(delete("/books/{id}", book.getId()))
                .andExpect(status().isNoContent());

        // Then

        assertThat(bookRepository.findById(book.getId())).isNotPresent();
    }
}

package az.nuran.booklib.integration.book;

import az.nuran.booklib.dto.request.CreateBookRequest;
import az.nuran.booklib.entity.Book;
import az.nuran.booklib.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateBookIT extends BaseIT {

    @Test
    void shouldCreateBooksSuccessfully() throws Exception {
        // Given

        List<CreateBookRequest> payloads = List.of(
                new CreateBookRequest("Layla and Majnun", "Nizami Ganjavi", 1188, 5),
                new CreateBookRequest("Ali and Nino", "Kurban Said", 1937, 7),
                new CreateBookRequest("Tutunamayanlar", "Oguz Altay", 1971, 3)
        );

        // When

        for (CreateBookRequest payload : payloads) {
            mockMvc.perform(post("/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().isCreated());
        }

        // Then

        List<Book> books = bookRepository.findAll();

        assertThat(books)
                .extracting(
                        Book::getTitle,
                        Book::getAuthor,
                        Book::getPublicationYear,
                        Book::getAvailableCopies
                )
                .containsExactlyInAnyOrder(
                        tuple("Layla and Majnun", "Nizami Ganjavi", 1188, 5),
                        tuple("Ali and Nino", "Kurban Said", 1937, 7),
                        tuple("Tutunamayanlar", "Oguz Altay", 1971, 3)
                );
    }
}

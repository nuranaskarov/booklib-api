package az.nuran.booklib.integration.book;

import az.nuran.booklib.entity.Book;
import az.nuran.booklib.integration.BaseIT;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SearchBookIT extends BaseIT {

    @Test
    void shouldSearchBooksSuccessfully() throws Exception {
        // Given

        List<Book> books = List.of(
                Book.builder()
                        .title("An Old Silent Pond")
                        .author("Matsuo Bashō")
                        .publicationYear(1686)
                        .availableCopies(5)
                        .build(),
                Book.builder()
                        .title("Spring Night")
                        .author("Yosa Buson")
                        .publicationYear(1768)
                        .availableCopies(3)
                        .build(),
                Book.builder()
                        .title("This World of Dew")
                        .author("Kobayashi Issa")
                        .publicationYear(1819)
                        .availableCopies(4)
                        .build()
        );

        bookRepository.saveAll(books);

        // When

        Book[] fetchedBooks = objectMapper.readValue(
                mockMvc.perform(get("/books/search")

                                .param("query", "Bashō"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                Book[].class
        );

        // Then

        assertThat(fetchedBooks)
                .hasSize(1)
                .extracting(
                        Book::getTitle,
                        Book::getAuthor,
                        Book::getPublicationYear,
                        Book::getAvailableCopies
                )
                .containsExactly(
                        tuple("An Old Silent Pond", "Matsuo Bashō", 1686, 5)
                );
    }
}

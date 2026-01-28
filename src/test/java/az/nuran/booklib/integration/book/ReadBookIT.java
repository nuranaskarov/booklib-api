package az.nuran.booklib.integration.book;

import az.nuran.booklib.dto.response.BookResponse;
import az.nuran.booklib.entity.Book;
import az.nuran.booklib.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReadBookIT extends BaseIT {
    
    @Test
    void shouldListBooksSuccessfully() throws Exception {
        // Given

        List<Book> books = List.of(
                Book.builder()
                        .title("Quiet Night Thought")
                        .author("Li Bai")
                        .publicationYear(701)
                        .availableCopies(5)
                        .build(),
                Book.builder()
                        .title("Spring View")
                        .author("Du Fu")
                        .publicationYear(757)
                        .availableCopies(3)
                        .build(),
                Book.builder()
                        .title("Deer Enclosure")
                        .author("Wang Wei")
                        .publicationYear(750)
                        .availableCopies(4)
                        .build()
        );

        bookRepository.saveAll(books);

        // When

        Book[] fetchedBooks = objectMapper.readValue(
                mockMvc.perform(get("/books"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                Book[].class
        );

        // Then

        assertThat(fetchedBooks)
                .extracting(
                        Book::getTitle,
                        Book::getAuthor,
                        Book::getPublicationYear,
                        Book::getAvailableCopies
                )
                .containsExactlyInAnyOrder(
                        tuple("Quiet Night Thought", "Li Bai", 701, 5),
                        tuple("Spring View", "Du Fu", 757, 3),
                        tuple("Deer Enclosure", "Wang Wei", 750, 4)
                );
        ;
    }

    @Test
    void shouldFindBookSuccessfully() throws Exception {
        // Given

        Book book = Book.builder()
                .title("Drinking Alone by Moonlight")
                .author("Li Bai")
                .publicationYear(701)
                .availableCopies(7)
                .build();


        bookRepository.save(book);

        // When

        Book fetchedBook = objectMapper.readValue(
                mockMvc.perform(get("/books/{id}", book.getId()))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                Book.class
        );

        // Then

        assertThat(fetchedBook)
                .extracting(
                        Book::getTitle,
                        Book::getAuthor,
                        Book::getPublicationYear,
                        Book::getAvailableCopies
                )
                .containsExactly(
                        "Drinking Alone by Moonlight",
                        "Li Bai",
                        701,
                        7
                );
    }

    @Test
    void shouldListBooksPublishedAfterYearSuccessfully() throws Exception {
        // Given

        List<Book> books = List.of(
                Book.builder()
                        .title("Quiet Night Thought")
                        .author("Li Bai")
                        .publicationYear(701)
                        .availableCopies(5)
                        .build(),
                Book.builder()
                        .title("Spring View")
                        .author("Du Fu")
                        .publicationYear(757)
                        .availableCopies(3)
                        .build(),
                Book.builder()
                        .title("Deer Enclosure")
                        .author("Wang Wei")
                        .publicationYear(750)
                        .availableCopies(4)
                        .build()
        );
        bookRepository.saveAll(books);

        // When

        BookResponse[] fetchedBooks = objectMapper.readValue(
                mockMvc.perform(get("/books/published-after/{year}", 750)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                BookResponse[].class
        );

        // Then

        assertThat(fetchedBooks)
                .extracting(
                        BookResponse::title,
                        BookResponse::author,
                        BookResponse::publicationYear,
                        BookResponse::availableCopies
                )
                .containsExactlyInAnyOrder(
                        tuple("Spring View", "Du Fu", 757, 3)
                );
    }
}

package az.nuran.booklib.integration.book;

import az.nuran.booklib.dto.response.BookAverageRatingResponse;
import az.nuran.booklib.dto.response.BookResponse;
import az.nuran.booklib.entity.Book;
import az.nuran.booklib.entity.Review;
import az.nuran.booklib.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class BookRatingIT extends BaseIT {

    @Test
    void shouldListAverageRatingsPerBookSuccessfully() throws Exception {
        // Given

        Book book1 = Book.builder().title("Top Book").author("Author X").publicationYear(2020).availableCopies(5).build();
        Book book2 = Book.builder().title("Average Book").author("Author Y").publicationYear(2019).availableCopies(2).build();

        List<Review> reviews = List.of(
                Review.builder().book(book1).rating(5).comment("Great!").build(),
                Review.builder().book(book1).rating(5).comment("Excellent!").build(),
                Review.builder().book(book2).rating(3).comment("Okay").build(),
                Review.builder().book(book2).rating(4).comment("Good").build()
        );

        bookRepository.saveAll(List.of(book1, book2));
        reviewRepository.saveAll(reviews);

        Map<String, Double> expectedAverages = reviews.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getBook().getTitle(),
                        Collectors.averagingDouble(Review::getRating)
                ));

        // When / Then

        BookAverageRatingResponse[] response = objectMapper.readValue(
                mockMvc.perform(get("/books/average-rating")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                BookAverageRatingResponse[].class
        );

        assertThat(response).hasSize(2);

        for (BookAverageRatingResponse r : response) {
            assertThat(r.averageRating())
                    .isEqualTo(expectedAverages.get(r.title()));
        }
    }

    @Test
    void shouldListBooksWithHighestRatingSuccessfully() throws Exception {
        // Given

        Book book1 = Book.builder().title("Top Book").author("Author X").publicationYear(2020).availableCopies(5).build();
        Book book2 = Book.builder().title("Average Book").author("Author Y").publicationYear(2019).availableCopies(2).build();

        List<Review> reviews = List.of(
                Review.builder().book(book1).rating(5).comment("Great!").build(),
                Review.builder().book(book1).rating(5).comment("Excellent!").build(),
                Review.builder().book(book2).rating(3).comment("Okay").build(),
                Review.builder().book(book2).rating(4).comment("Good").build()
        );

        bookRepository.saveAll(List.of(book1, book2));
        reviewRepository.saveAll(reviews);

        // When / Then

        BookResponse[] sqlResponse = objectMapper.readValue(
                mockMvc.perform(get("/books/highest-rating/sql")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                BookResponse[].class
        );

        BookResponse[] jpqlResponse = objectMapper.readValue(
                mockMvc.perform(get("/books/highest-rating/jgpl")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                BookResponse[].class
        );

        Map<UUID, Double> bookAverageRatings = reviewRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        r -> r.getBook().getId(),
                        Collectors.averagingDouble(Review::getRating)
                ));

        assertThat(sqlResponse)
                .allSatisfy(b -> assertThat(bookAverageRatings.get(b.id())).isGreaterThan(4));

        assertThat(jpqlResponse)
                .allSatisfy(b -> assertThat(bookAverageRatings.get(b.id())).isGreaterThan(4));

        assertThat(sqlResponse)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(jpqlResponse);
    }
}

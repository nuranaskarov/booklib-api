package az.nuran.booklib.integration.review;

import az.nuran.booklib.dto.response.ReviewResponse;
import az.nuran.booklib.entity.Book;
import az.nuran.booklib.entity.Review;
import az.nuran.booklib.integration.BaseIT;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class ReadReviewIT extends BaseIT {

    @Test
    void shouldListReviewsSuccessfully() throws Exception {
        // Given

        Book book = bookRepository.save(
                Book.builder()
                        .title("Brave New World")
                        .author("Aldous Huxley")
                        .publicationYear(1932)
                        .availableCopies(6)
                        .build()
        );

        List<Review> reviews = List.of(
                Review.builder()
                        .comment("Thought-provoking")
                        .rating(5)
                        .book(book)
                        .build(),
                Review.builder()
                        .comment("A bit disturbing")
                        .rating(4)
                        .book(book)
                        .build()
        );

        reviewRepository.saveAll(reviews);

        // When

        ReviewResponse[] fetchedReviews = objectMapper.readValue(
                mockMvc.perform(get("/books/{bookId}/reviews", book.getId()))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ReviewResponse[].class
        );

        // Then

        assertThat(fetchedReviews)
                .extracting(
                        ReviewResponse::comment,
                        ReviewResponse::rating
                )
                .containsExactlyInAnyOrder(
                        tuple("Thought-provoking", 5),
                        tuple("A bit disturbing", 4)
                );

    }
}

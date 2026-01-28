package az.nuran.booklib.integration.review;

import az.nuran.booklib.entity.Book;
import az.nuran.booklib.entity.Review;
import az.nuran.booklib.integration.BaseIT;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteReviewIT extends BaseIT {

    @Test
    void shouldDeleteReviewSuccessfully() throws Exception {
        // Given

        Book book = bookRepository.save(
                Book.builder()
                        .title("The Trial")
                        .author("Franz Kafka")
                        .publicationYear(1925)
                        .availableCopies(2)
                        .build()
        );

        Review review = reviewRepository.save(
                Review.builder()
                        .comment("Nice")
                        .rating(5)
                        .book(book)
                        .build()
        );

        // When

        mockMvc.perform(delete("/reviews/{reviewId}", review.getId()))
                .andExpect(status().isNoContent());

        // Then

        assertThat(reviewRepository.findById(review.getId())).isNotPresent();
    }
}

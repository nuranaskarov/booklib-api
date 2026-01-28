package az.nuran.booklib.integration.review;

import az.nuran.booklib.dto.request.UpdateReviewRequest;
import az.nuran.booklib.entity.Book;
import az.nuran.booklib.entity.Review;
import az.nuran.booklib.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateReviewIT extends BaseIT {

    @Test
    void shouldUpdateReviewSuccessfully() throws Exception {
        // Given

        Book book = bookRepository.save(
                Book.builder()
                        .title("Fahrenheit 451")
                        .author("Ray Bradbury")
                        .publicationYear(1953)
                        .availableCopies(3)
                        .build()
        );

        Review review = reviewRepository.save(
                Review.builder()
                        .comment("Not bad")
                        .rating(3)
                        .book(book)
                        .build()
        );

        UpdateReviewRequest request = new UpdateReviewRequest(
                5,
                "Amazing and timeless"
        );

        // When

        mockMvc.perform(patch("/reviews/{reviewId}", review.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        // Then

        Review updatedReview = reviewRepository.findById(review.getId()).orElseThrow();

        assertThat(updatedReview)
                .extracting(
                        Review::getRating,
                        Review::getComment
                )
                .containsExactly(
                        5,
                        "Amazing and timeless"
                );
    }
}

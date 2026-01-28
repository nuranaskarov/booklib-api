package az.nuran.booklib.integration.review;

import az.nuran.booklib.dto.request.CreateReviewRequest;
import az.nuran.booklib.dto.response.ReviewResponse;
import az.nuran.booklib.entity.Book;
import az.nuran.booklib.entity.Review;
import az.nuran.booklib.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CreateReviewIT extends BaseIT {

    @Test
    void shouldCreateReviewsSuccessfully() throws Exception {
        // Given

        Book book = bookRepository.save(
                Book.builder()
                        .title("1984")
                        .author("George Orwell")
                        .publicationYear(1949)
                        .availableCopies(4)
                        .build()
        );

        CreateReviewRequest payload = new CreateReviewRequest(
                5,
                "Excellent book"
        );

        // When

        mockMvc.perform(post("/books/{bookId}/reviews", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());

        // Then

        Review createdReview = reviewRepository.findAll().getFirst();

        assertThat(createdReview)
                .extracting(
                        Review::getComment,
                        Review::getRating
                )
                .containsExactly(
                        "Excellent book",
                        5
                );

        assertThat(createdReview.getBook().getId())
                .isEqualTo(book.getId());
    }
}

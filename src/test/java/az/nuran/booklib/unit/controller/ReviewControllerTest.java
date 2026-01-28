package az.nuran.booklib.unit.controller;

import az.nuran.booklib.controller.ReviewController;
import az.nuran.booklib.dto.request.CreateReviewRequest;
import az.nuran.booklib.dto.request.UpdateReviewRequest;
import az.nuran.booklib.dto.response.ReviewResponse;
import az.nuran.booklib.exception.BookNotFoundException;
import az.nuran.booklib.exception.ReviewNotFoundException;
import az.nuran.booklib.service.ReviewService;
import az.nuran.booklib.unit.BaseUT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ReviewController.class)
public class ReviewControllerTest extends BaseUT {

    @MockitoBean
    private ReviewService reviewService;

    @Test
    void createReview_shouldReturnCreatedWithLocation() throws Exception {
        // Given

        UUID bookId = UUID.randomUUID();
        CreateReviewRequest request = new CreateReviewRequest(5, "Excellent book!");

        // When / Then

        mockMvc.perform(post("/books/{bookId}/reviews", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/books/" + bookId + "/reviews"));

        verify(reviewService).createReview(eq(bookId), any(CreateReviewRequest.class));
    }

    @Test
    void createReview_shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        // Given

        UUID bookId = UUID.randomUUID();
        CreateReviewRequest request = new CreateReviewRequest(5, "Excellent book!");

        doThrow(new BookNotFoundException(bookId))
                .when(reviewService)
                .createReview(eq(bookId), any(CreateReviewRequest.class));

        // When / Then

        mockMvc.perform(post("/books/{bookId}/reviews", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(reviewService).createReview(eq(bookId), any(CreateReviewRequest.class));
    }

    @Test
    void getReviews_shouldReturnAllReviewsForBook() throws Exception {
        // Given

        UUID bookId = UUID.randomUUID();
        ReviewResponse review1 = new ReviewResponse(UUID.randomUUID(), 5, "Great book!");
        ReviewResponse review2 = new ReviewResponse(UUID.randomUUID(), 4, "Good read");
        ReviewResponse review3 = new ReviewResponse(UUID.randomUUID(), 5, "Amazing");

        when(reviewService.listReviews(bookId)).thenReturn(List.of(review1, review2, review3));

        // When / Then

        mockMvc.perform(get("/books/{bookId}/reviews", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].comment").value("Great book!"))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[1].comment").value("Good read"))
                .andExpect(jsonPath("$[1].rating").value(4))
                .andExpect(jsonPath("$[2].comment").value("Amazing"))
                .andExpect(jsonPath("$[2].rating").value(5));

        verify(reviewService).listReviews(bookId);
    }

    @Test
    void getReviews_shouldReturnEmptyListWhenNoReviews() throws Exception {
        // Given

        UUID bookId = UUID.randomUUID();

        when(reviewService.listReviews(bookId)).thenReturn(List.of());

        // When / Then

        mockMvc.perform(get("/books/{bookId}/reviews", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(reviewService).listReviews(bookId);
    }

    @Test
    void updateReview_shouldReturnNoContent() throws Exception {
        // Given

        UUID reviewId = UUID.randomUUID();
        UpdateReviewRequest request = new UpdateReviewRequest(4, "Updated review");

        // When / Then

        mockMvc.perform(patch("/reviews/{reviewId}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(reviewService).updateReview(eq(reviewId), any(UpdateReviewRequest.class));
    }

    @Test
    void updateReview_shouldReturnNotFoundWhenReviewDoesNotExist() throws Exception {
        // Given

        UUID reviewId = UUID.randomUUID();
        UpdateReviewRequest request = new UpdateReviewRequest(4, "Updated review");

        doThrow(new ReviewNotFoundException(reviewId))
                .when(reviewService)
                .updateReview(eq(reviewId), any(UpdateReviewRequest.class));

        // When / Then

        mockMvc.perform(patch("/reviews/{reviewId}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(reviewService).updateReview(eq(reviewId), any(UpdateReviewRequest.class));
    }

    @Test
    void deleteReview_shouldReturnNoContent() throws Exception {
        // Given

        UUID reviewId = UUID.randomUUID();

        // When / Then

        mockMvc.perform(delete("/reviews/{reviewId}", reviewId))
                .andExpect(status().isNoContent());

        verify(reviewService).deleteReview(reviewId);
    }

    @Test
    void deleteReview_shouldReturnNotFoundWhenReviewDoesNotExist() throws Exception {
        // Given

        UUID reviewId = UUID.randomUUID();

        doThrow(new ReviewNotFoundException(reviewId))
                .when(reviewService)
                .deleteReview(reviewId);

        // When / Then

        mockMvc.perform(delete("/reviews/{reviewId}", reviewId))
                .andExpect(status().isNotFound());

        verify(reviewService).deleteReview(reviewId);
    }
}

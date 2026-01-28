package az.nuran.booklib.controller;

import az.nuran.booklib.api.ReviewApi;
import az.nuran.booklib.dto.request.CreateReviewRequest;
import az.nuran.booklib.dto.request.UpdateReviewRequest;
import az.nuran.booklib.dto.response.ReviewResponse;
import az.nuran.booklib.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReviewController implements ReviewApi {
    private final ReviewService reviewService;

    @Override
    public ResponseEntity<Void> createReview(UUID bookId, CreateReviewRequest request) {
        reviewService.createReview(bookId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/books/{bookId}/reviews")
                .buildAndExpand(bookId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public List<ReviewResponse> getReviews(UUID bookId) {
        return reviewService.listReviews(bookId);
    }

    @Override
    public void updateReview(UUID reviewId, UpdateReviewRequest request) {
        reviewService.updateReview(reviewId, request);
    }

    @Override
    public void deleteReview(UUID reviewId) {
        reviewService.deleteReview(reviewId);
    }
}

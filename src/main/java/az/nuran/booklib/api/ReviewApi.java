package az.nuran.booklib.api;

import az.nuran.booklib.dto.request.CreateReviewRequest;
import az.nuran.booklib.dto.request.UpdateReviewRequest;
import az.nuran.booklib.dto.response.ReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;

@Tag(name = "Review", description = "Operations related to review management")
public interface ReviewApi {

    @Operation(summary = "Creates a review for a book")
    @PostMapping("/books/{bookId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> createReview(@PathVariable UUID bookId, @Valid @RequestBody CreateReviewRequest request);

    @Operation(summary = "Lists reviews of a book")
    @GetMapping("/books/{bookId}/reviews")
    @ResponseStatus(HttpStatus.OK)
    List<ReviewResponse> getReviews(@PathVariable UUID bookId);

    @Operation(summary = "Updates a review")
    @PatchMapping("/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateReview(@PathVariable UUID reviewId, @Valid @RequestBody UpdateReviewRequest request);

    @Operation(summary = "Delete a review")
    @DeleteMapping("/reviews/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteReview(@PathVariable UUID reviewId);
}

package az.nuran.booklib.unit.service;

import az.nuran.booklib.dto.request.CreateReviewRequest;
import az.nuran.booklib.dto.request.UpdateReviewRequest;
import az.nuran.booklib.dto.response.ReviewResponse;
import az.nuran.booklib.entity.Book;
import az.nuran.booklib.entity.Review;
import az.nuran.booklib.exception.BookNotFoundException;
import az.nuran.booklib.exception.ReviewNotFoundException;
import az.nuran.booklib.mapper.ReviewMapper;
import az.nuran.booklib.repository.BookRepository;
import az.nuran.booklib.repository.ReviewRepository;
import az.nuran.booklib.service.ReviewService;
import az.nuran.booklib.unit.BaseUT;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReviewServiceTest extends BaseUT {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void createReview_shouldCreateReviewSuccessfully() {
        // Given

        UUID bookId = UUID.randomUUID();
        CreateReviewRequest request = new CreateReviewRequest(5, "");
        Review review = Review.builder().id(UUID.randomUUID()).build();
        Book book = Book.builder().id(bookId).build();

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(reviewMapper.toEntity(request)).thenReturn(review);
        when(entityManager.getReference(Book.class, bookId)).thenReturn(book);

        // When

        reviewService.createReview(bookId, request);

        // Then

        verify(reviewRepository).save(review);
        assertThat(review.getBook()).isEqualTo(book);
    }

    @Test
    void createReview_throwsExceptionWhenBookNotFound() {
        // Given

        UUID bookId = UUID.randomUUID();
        CreateReviewRequest request = new CreateReviewRequest(5, "");

        when(bookRepository.existsById(bookId)).thenReturn(false);

        // When / Then

        assertThatThrownBy(() -> reviewService.createReview(bookId, request))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining(bookId.toString());
    }

    @Test
    void updateReview_updatesReviewSuccessfully() {
        // Given

        UUID reviewId = UUID.randomUUID();
        Review review = Review.builder().id(reviewId).build();
        UpdateReviewRequest request = new UpdateReviewRequest(4, "");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // When

        reviewService.updateReview(reviewId, request);

        // Then

        verify(reviewMapper).patch(request, review);
        verify(reviewRepository).save(review);
    }

    @Test
    void updateReview_throwsExceptionWhenNotFound() {
        // Given

        UUID reviewId = UUID.randomUUID();
        UpdateReviewRequest request = new UpdateReviewRequest(4, "");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // When / Then

        assertThatThrownBy(() -> reviewService.updateReview(reviewId, request))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessageContaining(reviewId.toString());
    }

    @Test
    void deleteReview_deletesReviewSuccessfully() {
        // Given

        UUID reviewId = UUID.randomUUID();
        Review review = Review.builder().id(reviewId).build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // When

        reviewService.deleteReview(reviewId);

        // Then

        verify(reviewRepository).delete(review);
    }

    @Test
    void deleteReview_throwsExceptionWhenNotFound() {
        // Given

        UUID reviewId = UUID.randomUUID();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // When / Then

        assertThatThrownBy(() -> reviewService.deleteReview(reviewId))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessageContaining(reviewId.toString());
    }

    @Test
    void listReviews_returnsAllReviewsForBook() {
        // Given

        UUID bookId = UUID.randomUUID();
        Review review1 = Review.builder().id(UUID.randomUUID()).comment("Great book!").rating(5).build();
        Review review2 = Review.builder().id(UUID.randomUUID()).comment("Good read").rating(4).build();
        Review review3 = Review.builder().id(UUID.randomUUID()).comment("Amazing").rating(5).build();

        ReviewResponse response1 = new ReviewResponse(review1.getId(), 5, "Great book!");
        ReviewResponse response2 = new ReviewResponse(review2.getId(), 4, "Good read");
        ReviewResponse response3 = new ReviewResponse(review3.getId(), 5, "Amazing");

        when(reviewRepository.findAllByBookId(bookId)).thenReturn(List.of(review1, review2, review3));
        when(reviewMapper.toResponse(review1)).thenReturn(response1);
        when(reviewMapper.toResponse(review2)).thenReturn(response2);
        when(reviewMapper.toResponse(review3)).thenReturn(response3);

        // When

        List<ReviewResponse> result = reviewService.listReviews(bookId);

        // Then

        assertThat(result).hasSize(3);
        assertThat(result).containsExactly(response1, response2, response3);
    }

    @Test
    void listReviews_returnsEmptyListWhenNoReviews() {
        // Given

        UUID bookId = UUID.randomUUID();

        when(reviewRepository.findAllByBookId(bookId)).thenReturn(List.of());

        // When

        List<ReviewResponse> result = reviewService.listReviews(bookId);

        // Then

        assertThat(result).isEmpty();
    }
}

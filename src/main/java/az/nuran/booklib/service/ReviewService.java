package az.nuran.booklib.service;

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
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final EntityManager entityManager;

    public void createReview(UUID bookId, CreateReviewRequest request) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        Review review = reviewMapper.toEntity(request);

        Book book = entityManager.getReference(Book.class, bookId);
        review.setBook(book);

        reviewRepository.save(review);
    }

    public void updateReview(UUID reviewId, UpdateReviewRequest request) {
        Review review = getReview(reviewId);

        reviewMapper.patch(request, review);

        reviewRepository.save(review);
    }

    public void deleteReview(UUID reviewId) {
        Review review = getReview(reviewId);

        reviewRepository.delete(review);
    }

    public List<ReviewResponse> listReviews(UUID bookId) {
        return reviewRepository
                .findAllByBookId(bookId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    private Review getReview(UUID reviewId) {
        return reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
    }
}

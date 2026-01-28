package az.nuran.booklib.repository;

import az.nuran.booklib.dto.response.BookAverageRatingResponse;
import az.nuran.booklib.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findAllByBookId(UUID bookId);
}

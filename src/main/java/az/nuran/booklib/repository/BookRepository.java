package az.nuran.booklib.repository;

import az.nuran.booklib.dto.response.BookAverageRatingResponse;
import az.nuran.booklib.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    @Query(
            value = "SELECT * FROM book WHERE publication_year > :year",
            nativeQuery = true
    )
    List<Book> findAllByPublicationYearAfter(Integer year);

    @Query("""
        SELECT new az.nuran.booklib.dto.response.BookAverageRatingResponse(b.title, AVG(r.rating))
        FROM Book b
        JOIN b.reviews r
        GROUP BY b.title
    """)
    List<BookAverageRatingResponse> findAverageRatingPerBook();

    @Query(
            value = """
                SELECT b.* FROM book b
                JOIN review r ON b.id = r.book_id
                GROUP BY b.id
                HAVING AVG(r.rating) >= 4
            """,
            nativeQuery = true
    )
    List<Book> findBooksWithHighRatingsSQL();

    @Query("""
        SELECT b FROM Book b
        JOIN b.reviews r
        GROUP BY b
        HAVING AVG(r.rating) >= 4
    """)
    List<Book> findBooksWithHighRatingsJPQL();
}

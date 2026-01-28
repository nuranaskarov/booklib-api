package az.nuran.booklib.service;

import az.nuran.booklib.dto.request.CreateBookRequest;
import az.nuran.booklib.dto.request.UpdateBookRequest;
import az.nuran.booklib.dto.response.BookAverageRatingResponse;
import az.nuran.booklib.dto.response.BookResponse;
import az.nuran.booklib.entity.Book;
import az.nuran.booklib.exception.BookNotFoundException;
import az.nuran.booklib.mapper.BookMapper;
import az.nuran.booklib.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public UUID createBook(CreateBookRequest request) {
        Book book = bookMapper.toEntity(request);

        Book createdBook = bookRepository.save(book);

        return createdBook.getId();
    }

    public List<BookResponse> listBooks() {
        return bookRepository
                .findAll()
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    public List<BookResponse> listBooksAfterYear(Integer year) {
        return bookRepository
                .findAllByPublicationYearAfter(year)
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    public List<BookAverageRatingResponse> listAverageRatingsPerBook() {
        return bookRepository.findAverageRatingPerBook();
    }

    public List<BookResponse> listBooksWithHighestRatingSQL() {
        return bookRepository
                .findBooksWithHighRatingsSQL()
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    public List<BookResponse> listBooksWithHighestRatingJPQL() {
        return bookRepository
                .findBooksWithHighRatingsJPQL()
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    public List<BookResponse> searchBooks(String query) {
        String term = query.trim();
        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(term, term)
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    public BookResponse getBook(UUID bookId) {
        return bookRepository
                .findById(bookId)
                .map(bookMapper::toResponse)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    public void updateBook(UUID bookId, UpdateBookRequest request) {
        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        bookMapper.patch(request, book);

        bookRepository.save(book);
    }

    public void deleteBook(UUID bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        bookRepository.deleteById(bookId);
    }
}

package az.nuran.booklib.integration;

import az.nuran.booklib.TestConfig;
import az.nuran.booklib.repository.BookRepository;
import az.nuran.booklib.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class BaseIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected BookRepository bookRepository;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void cleanup() {
        bookRepository.deleteAll();
        reviewRepository.deleteAll();
    }
}

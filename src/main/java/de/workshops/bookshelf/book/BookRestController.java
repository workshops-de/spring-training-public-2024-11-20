package de.workshops.bookshelf.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Use Spring annotations to make this class a RestController mapped to an HTTP URL.
@RestController
@RequestMapping("/book")
public class BookRestController {

    private final ObjectMapper mapper;

    private final ResourceLoader resourceLoader;

    private List<Book> books;

    public BookRestController(ObjectMapper mapper, ResourceLoader resourceLoader) {
        this.mapper = mapper;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return books;
    }
    @GetMapping("{isbn}")
    public Book getBookByIsdn(@PathVariable String isbn) {
        return this.books.stream().filter(book -> hasIsbn(book, isbn)).findFirst().orElseThrow();
    }

    private boolean hasIsbn(Book book, String isbn) {
        return book.getIsbn().equals(isbn);
    }

    @GetMapping(params="author")
    public Book getBookByAuthor(@RequestParam("author") String author) {
        return this.books.stream().filter(book -> hasAuthor(book, author)).findFirst().orElseThrow();
    }

    private boolean hasAuthor(Book book, String author) {
        return book.getAuthor().contains(author);
    }

    @PostConstruct
    public void init() throws Exception {
        final var resource = resourceLoader.getResource("classpath:books.json");
        this.books = mapper.readValue(resource.getInputStream(), new TypeReference<>() {});
    }

    // Map a method returning books to HTTP GET requests for this controller's URL.
    // ...
}
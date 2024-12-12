package com.nesreading.service;

import com.nesreading.model.Book;
import com.nesreading.repository.BookRepository;

import com.nesreading.specification.BookSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private final BookRepository bookRepository;
    private final UploadService uploadService;
    private final BookSpecification bookSpecification;

    public BookService(BookRepository bookRepository, UploadService uploadService, BookSpecification bookSpecification) {
        this.bookRepository = bookRepository;
        this.uploadService = uploadService;
        this.bookSpecification = bookSpecification;
    }

    public boolean handleCheckBookExist(int id) {
        return !bookRepository.existsById(id);
    }

    public List<Book> handleFetchAllBooks() {
        return bookRepository.findAll();
    }

    public Page<Book> handleFetchAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Page<Book> handleFetchFilteredBook(Pageable pageable, String title, String author, Double minPrice, Double maxPrice) {
        Specification<Book> specification = Specification.where(bookSpecification.hasTitle(title))
                .and(bookSpecification.hasAuthor(author))
                .and(bookSpecification.hasPriceBetween(minPrice, maxPrice));

        return bookRepository.findAll(specification, pageable);
    }

    public List<Book> handleFetchBestSellerBooks() {
        return bookRepository.findTop8ByOrderBySoldDesc();
    }

    public List<Book> handleFetchRandomBooks() {
        return bookRepository.find8RandomBooks();
    }

    public Book handleFetchBookById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void handleCreateBook(Book newBook) {
        newBook.setStatus(0);
        bookRepository.save(newBook);
    }

    public void handleCreateBook(Book newBook, MultipartFile file) {
        String bookImage = uploadService.handleSaveUploadFile(file, "book");

        newBook.setImageUrl(bookImage);
        newBook.setSold(0);
        newBook.setStatus(0);

        bookRepository.save(newBook);
    }

    public void handleDeleteBookById(int id) {
        Book tempBook = bookRepository.getReferenceById(id);

        if (tempBook.getImageUrl() != null && !tempBook.getImageUrl().isEmpty()) {
            uploadService.handleDeleteFile(tempBook.getImageUrl(), "book");
        }

        bookRepository.deleteById(id);
    }

    public void handleUpdateBook(Book tempBook, MultipartFile file) {
        Book dbBook = handleFetchBookById(tempBook.getId());

        dbBook.setTitle(tempBook.getTitle());
        dbBook.setAuthor(tempBook.getAuthor());
        dbBook.setPublisher(tempBook.getPublisher());
        dbBook.setPublicationYear(tempBook.getPublicationYear());
        dbBook.setShortDescription(tempBook.getShortDescription());
        dbBook.setDetailDescription(tempBook.getDetailDescription());
        dbBook.setPrice(tempBook.getPrice());
        dbBook.setStock(tempBook.getStock());
        dbBook.setCategory(tempBook.getCategory());

        if (!file.isEmpty()) {
            String bookImage = uploadService.handleSaveUploadFile(file, "book");
            dbBook.setImageUrl(bookImage);
        }

        bookRepository.save(dbBook);
    }
}

package com.nesreading.service;

import com.nesreading.domain.Book;
import com.nesreading.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public boolean handleCheckBookExist(int id) {
        return !bookRepository.existsById(id);
    }

    public List<Book> handleFetchAllBooks() {
        return bookRepository.findAll();
    }

    public Book handleFetchBookById(int id) {
        return bookRepository.getReferenceById(id);
    }

    public void handleCreateBook(Book newBook) {
        bookRepository.save(newBook);
    }

    public void handleDeleteBookById(int id) {
        bookRepository.deleteById(id);
    }

    public void handleUpdateBook(Book tempBook) {
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

        bookRepository.save(dbBook);
    }
}

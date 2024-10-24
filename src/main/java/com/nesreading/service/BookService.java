package com.nesreading.service;

import com.nesreading.domain.Author;
import com.nesreading.domain.Book;
import com.nesreading.domain.BookCategory;
import com.nesreading.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final BookCategoryService bookCategoryService;

    public BookService(BookRepository bookRepository, AuthorService authorService, BookCategoryService bookCategoryService) {
        this.bookRepository = bookRepository;
		this.authorService = authorService;
		this.bookCategoryService = bookCategoryService;
    }

    public Book handleCreateBook(Book book, List<Long> authorIds, long bookCategoryId) {
    	List<Author> authors = new ArrayList<>(authorService.handleFetchAllAuthorsById(authorIds));
    	BookCategory bookCategory = bookCategoryService.handleFetchBookCategoryById(bookCategoryId).orElseThrow(() -> new IllegalArgumentException("Book category didn't exist"));;
    	
//        Book tempBook = new Book();
//        tempBook.setTitle(book.getTitle());
//        tempBook.setPublisher(book.getPublisher());
//        tempBook.setPublicationYear(book.getPublicationYear());
//        tempBook.setShortDescription(book.getShortDescription());
//        tempBook.setDetailDescription(book.getDetailDescription());
//        tempBook.setPrice(book.getPrice());
//        tempBook.setStock(book.getStock());
//        tempBook.setAuthors(authors);
//        tempBook.setBookCategory(bookCategory);
//    	  System.out.println(tempBook.toString());
    	
    	 // Liên kết thông tin cho sách
        book.setAuthors(authors);
        book.setBookCategory(bookCategory);

        return bookRepository.save(book);
    }

    public List<Book> handleFetchAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> handleFetchBookById(long id) {
        return bookRepository.findById(id);
    }

    public Book handleUpdateBook(Book book, List<Long> authorIds, long bookCategoryId) {
        Book dbBook = bookRepository.findById(book.getId()).get();
        
        dbBook.setTitle(book.getTitle());
        dbBook.setPublisher(book.getPublisher());
        dbBook.setPublicationYear(book.getPublicationYear());
        dbBook.setShortDescription(book.getShortDescription());
        dbBook.setDetailDescription(book.getDetailDescription());
        dbBook.setPrice(book.getPrice());
        dbBook.setStock(book.getStock());
        
     // Cập nhật tác giả và thể loại
        List<Author> authors = authorService.handleFetchAllAuthorsById(authorIds);
        BookCategory bookCategory = bookCategoryService.handleFetchBookCategoryById(bookCategoryId)
                                                        .orElseThrow(() -> new IllegalArgumentException("Book category didn't exist"));
        
        dbBook.setAuthors(authors);
        dbBook.setBookCategory(bookCategory);

        return bookRepository.save(dbBook);
    }
    
    public void handleDeleteBook(long id) {
    	bookRepository.deleteById(id);
    }
}

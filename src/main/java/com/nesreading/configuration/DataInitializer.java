package com.nesreading.configuration;

import com.nesreading.model.Book;
import com.nesreading.model.User;
import com.nesreading.repository.BookRepository;
import com.nesreading.repository.UserRepository;
import com.nesreading.service.BookService;
import com.nesreading.service.CsvHelper;
import com.nesreading.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

@Configuration
public class DataInitializer {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    private final UserService userService;
    private final BookService bookService;

    public DataInitializer(UserService userService, UserRepository userRepository, BookRepository bookRepository, BookService bookService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            // Đoạn code sẽ được chạy khi ứng dụng khởi động
            System.out.println("Application has started!");

            // Ví dụ: Thực hiện khởi tạo dữ liệu
            initialDefaultUsers();
            initialDefaultBooks();

            System.out.println("Application Still running at: ");
            System.out.println("localhost:8080/admin");
            System.out.println("localhost:8080");
        };
    }

    private void initialDefaultUsers() {
        if(userRepository.count() == 0) {
            try {
                InputStream is = new ClassPathResource("static/csv/users.csv").getInputStream();
                List<User> userList = CsvHelper.csvToUser(is);

                for (User user : userList) {
                    userService.handleCreateUser(user);
                }

                System.out.println("Imported user data from CSV file successfully.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("User data already exists in the database.");
        }
    }

    private void initialDefaultBooks() {
        if (bookRepository.count() == 0) {
            try {
                InputStream is = new ClassPathResource("static/csv/books.csv").getInputStream();
                List<Book> bookList = CsvHelper.csvToBook(is);

                for (Book book : bookList) {
                    bookService.handleCreateBook(book);
                }

                System.out.println("Imported book data from CSV file successfully.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Book data already exists in the database.");
        }
    }
}

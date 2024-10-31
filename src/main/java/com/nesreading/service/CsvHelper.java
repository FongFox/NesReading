package com.nesreading.service;

import com.nesreading.domain.Book;
import com.nesreading.domain.User;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvHelper {
    public static List<User> csvToUser(InputStream is) {
        List<User> userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))){
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 5) {
                    User user = getUser(fields);

                    userList.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userList;
    }

    private static User getUser(String[] fields) {
        User user = new User();

        user.setRole(fields[0]);
        user.setFirstName(fields[1]);
        user.setLastName(fields[2]);
        user.setEmail(fields[3]);
        user.setPassword(fields[4]);
        user.setPhoneNumber(fields[5]);

        return user;
    }

    public static List<Book> csvToBook(InputStream is) {
        List<Book> bookList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(is))){
            String[] fields;
            reader.readNext();

            while ((fields = reader.readNext()) != null) {
                if (fields.length >= 11) {
                    Book book = getBook(fields);
                    bookList.add(book);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return bookList;
    }

    private static Book getBook(String[] fields) {
        Book book = new Book();

        book.setCategory(fields[0]);
        book.setTitle(fields[1]);
        book.setAuthor(fields[2]);
        book.setShortDescription(fields[3]);
        book.setDetailDescription(fields[4]);
        book.setPublisher(fields[5]);

        try {
            book.setPublicationYear(Integer.parseInt(fields[6]));
        } catch (NumberFormatException e) {
            System.err.println("Invalid publication year: " + fields[6]);
            book.setPublicationYear(0); // hoặc giá trị mặc định khác
        }

        try {
            book.setPrice(Double.parseDouble(fields[7]));
        } catch (NumberFormatException e) {
            System.err.println("Invalid price: " + fields[7]);
            book.setPrice(0.0); // hoặc giá trị mặc định khác
        }

        try {
            book.setStock(Integer.parseInt(fields[8]));
        } catch (NumberFormatException e) {
            System.err.println("Invalid stock: " + fields[8]);
            book.setStock(0); // hoặc giá trị mặc định khác
        }

        try {
            book.setStatus(Integer.parseInt(fields[9]));
        } catch (NumberFormatException e) {
            System.err.println("Invalid status: " + fields[9]);
            book.setStatus(0); // hoặc giá trị mặc định khác
        }

        try {
            book.setSold(Integer.parseInt(fields[10]));
        } catch (NumberFormatException e) {
            System.err.println("Invalid sold: " + fields[10]);
            book.setSold(0); // hoặc giá trị mặc định khác
        }

        return book;
    }
}

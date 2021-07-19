package com.example.springrestvalidations.resources;

import java.util.List;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import com.example.springrestvalidations.errors.BookNotFoundException;
import com.example.springrestvalidations.errors.BookUnSupportedFieldPatchException;
import com.example.springrestvalidations.model.Book;
import com.example.springrestvalidations.repo.BookRepository;

@Validated
@RestController
public class BookController {

    @Autowired
    private BookRepository repository;

    // Find
    @GetMapping("/books")
    List<Book> findAll() {
        return repository.findAll();
    }

    // Save
    //return 201 instead of 200(OK)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/books")
    Book newBook(@Valid @RequestBody Book newBook) {
        return repository.save(newBook);
    }

    // Find
    // if no id found it throws BookNotFoundException and it shows 500 Internal Server error in response on Postman
    // As customer exception handler is written so its displaying 404 Not Found error
    @GetMapping("/books/{id}")
    Book findOne(@Min(1) @PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    // Save or update
    @PutMapping("/books/{id}")
    Book saveOrUpdate(@RequestBody Book newBook, @PathVariable Long id) {

    	System.out.println("save or update @@@");
        return repository.findById(id)
                .map(x -> {
                    if(newBook.getName() != null) x.setName(newBook.getName());
                    if(newBook.getAuthor() != null) x.setAuthor(newBook.getAuthor());
                    if(newBook.getPrice() != null) x.setPrice(newBook.getPrice());
                    return repository.save(x);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return repository.save(newBook);
                });
    }

    // update author only
    @PatchMapping("/books/{id}")
    Book patch(@RequestBody Map<String, String> update, @PathVariable Long id) {

        return repository.findById(id)
                .map(x -> {

                    String author = update.get("author");
                    if (author !=null && !author.isEmpty()) {
                        x.setAuthor(author);

                        // better create a custom method to update a value = :newValue where id = :id
                        return repository.save(x);
                    } else {
                        throw new BookUnSupportedFieldPatchException(update.keySet());
                    }

                })
                .orElseGet(() -> {
                    throw new BookNotFoundException(id);
                });

    }

    @DeleteMapping("/books/{id}")
    void deleteBook(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
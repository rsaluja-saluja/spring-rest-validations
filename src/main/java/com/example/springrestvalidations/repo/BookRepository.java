package com.example.springrestvalidations.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springrestvalidations.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	
}

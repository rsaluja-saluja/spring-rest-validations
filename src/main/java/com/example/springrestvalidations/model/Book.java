package com.example.springrestvalidations.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.example.springrestvalidations.validator.Author;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;
    
    @NotEmpty(message = "Please provide name.")
    private String name;
    
    @Author //custom validator
 //   @Author(message = "Given author is not allowed")
    @NotEmpty(message = "Please provide author")
    private String author;
    
    @NotNull(message = "Please provide price")
    @DecimalMin(value = "1.0", message = "Price must be >= 1.0")
    private BigDecimal price;
    
	public Book(String name, String author, BigDecimal price) {
		super();
		
		this.name = name;
		this.author = author;
		this.price = price;
	}
	
	public Book(Long id, String name, String author, BigDecimal price) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.price = price;
	}
	public Book() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

   
}

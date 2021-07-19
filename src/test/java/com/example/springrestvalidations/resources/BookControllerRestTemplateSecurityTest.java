package com.example.springrestvalidations.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import org.json.JSONException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.springrestvalidations.model.Book;
import com.example.springrestvalidations.repo.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/* To run these test cases, need to uncomment the below:
 * SpringSecurityConfig file content
 * pom.xml - Spring security and spring security test dependencies
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
public class BookControllerRestTemplateSecurityTest {

	

	    private static final ObjectMapper om = new ObjectMapper();

	    @Autowired
	    private TestRestTemplate restTemplate;

	    @MockBean
	    private BookRepository mockRepository;

	    @BeforeEach
	    public void init() {
	    	System.out.println("*** Before");
	        Book book = new Book(1L, "Book Name", "Mkyong", new BigDecimal("9.99"));
	        when(mockRepository.findById(1L)).thenReturn(Optional.of(book));
	    }

	    @Test
	    public void find_login_ok() throws Exception {
	    	System.out.println("*** Login OK");

	        String expected = "{id:1,name:\"Book Name\",author:\"Mkyong\",price:9.99}";

	        ResponseEntity<String> response = restTemplate
	                .withBasicAuth("user", "password")
	                .getForEntity("/books/1", String.class);

	        printJSON(response);

	        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
	        assertEquals(HttpStatus.OK, response.getStatusCode());

	        JSONAssert.assertEquals(expected, response.getBody(), false);

	    }

	    @Test
	    public void find_nologin_401() throws Exception {
	    	System.out.println("*** Login Not OK");

	        String expected = "{\"status\":401,\"error\":\"Unauthorized\",\"path\":\"/books/1\"}";

	        ResponseEntity<String> response = restTemplate
	                .getForEntity("/books/1", String.class);

	        printJSON(response);

	        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
	        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

	        JSONAssert.assertEquals(expected, response.getBody(), false);

	    }

	    @Test
	    public void save_book_OK() throws Exception {

	        Book newBook = new Book(2L, "Spring Boot Guide", "mkyong", new BigDecimal("2.99"));
	        when(mockRepository.save(any(Book.class))).thenReturn(newBook);

	        String expected = om.writeValueAsString(newBook);

	        ResponseEntity<String> response = restTemplate
	        		.withBasicAuth("admin", "password")
	        		.postForEntity("/books", newBook, String.class);

	        printJSON(response);

	        assertEquals(HttpStatus.CREATED, response.getStatusCode());
	        JSONAssert.assertEquals(expected, response.getBody(), false);

	    //    verify(mockRepository, times(1)).save(any(Book.class));

	    }
	    @Test
	    public void save_book_403() throws Exception {

	        Book newBook = new Book(1L, "Spring Boot Guide", "mkyong", new BigDecimal("2.99"));
	        when(mockRepository.save(any(Book.class))).thenReturn(newBook);

	        String expected = "{\"status\":403,\"error\":\"Forbidden\",\"path\":\"/books\"}";


	        ResponseEntity<String> response = restTemplate
	        		.withBasicAuth("user", "password")
	        		.postForEntity("/books", newBook, String.class);

	        printJSON(response);

	        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	        JSONAssert.assertEquals(expected, response.getBody(), false);

	       // verify(mockRepository, times(1)).save(any(Book.class));

	    }
	    
//	   
	    
	    private static void printJSON(Object object) {
	        String result;
	        try {
	            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
	            System.out.println(result);
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
	    }

}

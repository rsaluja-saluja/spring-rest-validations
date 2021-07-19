package com.example.springrestvalidations.errors;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.springrestvalidations.error.CustomErrorResponse;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Let Spring handle the exception, we just override the status code
	// in this case CustomErrorAttribute is getting called so response format is getting changed according to that

    @ExceptionHandler(BookNotFoundException.class)
    public void springHandleNotFound(HttpServletResponse response) throws IOException {
    	System.out.println("CustomGlobalExceptionHandler@@@@@@@@@@@@@@@@@@@@@@@@@@");
    	response.sendError(HttpStatus.NOT_FOUND.value());
    	
    	
    }

  //customized error response for BookNotFoundException
// in this case CustomErrorAttribute is not getting called
//    @ExceptionHandler(BookNotFoundException.class)
//    public ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex, WebRequest request) {
//    	
//    	System.out.println("CustomGlobalExceptionHandler#######################");
//
//    	CustomErrorResponse error = new CustomErrorResponse();
//    	error.setStatus(HttpStatus.NOT_FOUND.value());
//    	error.setTimestamp(LocalDateTime.now());
//    	error.setError(ex.getMessage());
//
//    	return new ResponseEntity<CustomErrorResponse>(error, HttpStatus.NOT_FOUND);
//    }
    
		
	
    @ExceptionHandler(BookUnSupportedFieldPatchException.class)
    public void springUnSupportedFieldPatch(HttpServletResponse response) throws IOException {
    	System.out.println("Unsupported Field Patch excpetion ^^^^^^^");

    	response.sendError(HttpStatus.METHOD_NOT_ALLOWED.value());
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(HttpServletResponse response) throws IOException {
    	System.out.println("Constraint Violation excpetion ^^^^^^^");
    	response.sendError(HttpStatus.BAD_REQUEST.value());
    }

 // in this case CustomErrorAttribute is not getting called
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
    		HttpHeaders headers, HttpStatus status, WebRequest request) {
    	// TODO Auto-generated method stub
    	Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("status message", status.toString());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
        
    	//return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }
}

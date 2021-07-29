Spring Rest Validation
	https://mkyong.com/spring-boot/spring-rest-validation-example/

	Required spring-boot-starter-validation dependency
	
	@ControllerAdvice is an annotation, to handle the exceptions globally
	@ExceptionHandler is an annotation used to handle the specific exceptions and sending the custom responses to the client.
	
	Bean Validation:
		@NotEmpty, @NotNull, @DecimalMin etc constraints added in Book Entity
		Add @Valid to @RequestBody
		POST request will trigger a MethodArgumentNotValidException. Spring will send HTTP status 400 Bad Request. without any message.
		MethodArgumentNotValidException can be catched and response can be overridden.
	
	Path Variables Validations:
		Use @Validated on Controller class level
		Will give default error message with 500 Internal server error code
		If the @Validated is failed, it will trigger a ConstraintViolationException, that can be catched in CustomGlobalExceptionHandler
		and override the error code.
		
	Custom Validator:
		custom validator can be created for the author field, only allowing 4 authors to save into the database.
		Author.java and AuthorValidator.java
		If customValidator would fail, it will throw MethodArgumentNotValidException exception

	
Spring REST Error Handling:
	https://mkyong.com/spring-boot/spring-rest-error-handling-example/
	
	By default, Spring Boot provides a BasicErrorController(package org.springframework.boot.autoconfigure.web.servlet.error) controller 
	for /error mapping that handles all errors, and getErrorAttributes to produce a JSON response with details of the error, 
	the HTTP status, and the exception message.
		{	
		    "timestamp":"2019-02-27T04:03:52.398+0000",
		    "status":500,
		    "error":"Internal Server Error",
		    "message":"...",
		    "path":"/path"
		}
	Custom Exception Handler:
		@ControllerAdvice to handle custom exception.
		Eg - CustomGlobalExceptionHandler
		Status code and JSON error response can be customized
	
	JSR 303 Validation error
		For Spring @valid validation errors, it will throw handleMethodArgumentNotValid
		Eg - CustomGlobalExceptionHandler
	
	DefaultErrorAttributes
		To override the default JSON error response for all exceptions, create a bean and extends DefaultErrorAttributes.
	

Spring REST Security:
	https://mkyong.com/spring-boot/spring-rest-spring-security-example/
	
		
		
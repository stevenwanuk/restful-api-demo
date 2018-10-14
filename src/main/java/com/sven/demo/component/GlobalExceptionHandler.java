package com.sven.demo.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.sven.demo.exception.NotFoundException;
import com.sven.demo.exception.UnauthorizedException;

@ControllerAdvice
public class GlobalExceptionHandler
{

	private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);

	@ExceptionHandler(UnauthorizedException.class)
	public final ResponseEntity<String> handleUnauthorizedException(Exception ex, WebRequest request)
	{
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<String> handleNotFoundException(Exception ex, WebRequest request)
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<String> handleException(Exception ex, WebRequest request)
	{
		LOG.error("Exception is caught", ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}

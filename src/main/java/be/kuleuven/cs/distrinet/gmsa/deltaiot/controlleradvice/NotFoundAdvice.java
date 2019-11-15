package be.kuleuven.cs.distrinet.gmsa.deltaiot.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Advice to translate NotFoundException thrown by a controller into a HTTP 404 Not found response.
 * 
 * @author koeny
 *
 */
@ControllerAdvice
class NotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String unauthorizedHandler(NotFoundException ex) {
		return "The requested resource cannot be found.";
	}
}
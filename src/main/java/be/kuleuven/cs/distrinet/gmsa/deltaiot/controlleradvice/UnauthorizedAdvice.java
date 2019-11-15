package be.kuleuven.cs.distrinet.gmsa.deltaiot.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Advice to translate UnauthorizedExceptions thrown by a controller into a HTTP 401 Unauthorized response.
 * 
 * @author koeny
 *
 */
@ControllerAdvice
class UnauthorizedAdvice {

	@ResponseBody
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	String unauthorizedHandler(UnauthorizedException ex) {
		return "The provided token is invalid or does not allow the attempted action.";
	}
}
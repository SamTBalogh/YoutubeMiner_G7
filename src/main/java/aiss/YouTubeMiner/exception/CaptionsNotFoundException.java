package aiss.YouTubeMiner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "Captions not found with that Video Id")
public class CaptionsNotFoundException extends Exception{
}

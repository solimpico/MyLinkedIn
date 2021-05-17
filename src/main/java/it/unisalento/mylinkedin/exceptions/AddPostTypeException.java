package it.unisalento.mylinkedin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_MODIFIED, reason = "ERRORE NELL'AGGIUNTA DELLA TIPOLOGIA")
public class AddPostTypeException extends Exception {
}

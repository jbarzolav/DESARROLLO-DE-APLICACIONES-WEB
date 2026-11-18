package com.tecsup.exception;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.*;
@RestControllerAdvice public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class) public ResponseEntity<?> missing(ResourceNotFoundException e){
        return ResponseEntity.status(404).body(Map.of("mensaje",e.getMessage(),"codigo","404"));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<?> invalid(MethodArgumentNotValidException e){
        Map<String,String> m=new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x->m.put(x.getField(),x.getDefaultMessage()));
        return ResponseEntity.badRequest().body(m);
    }
    @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<?> invalid(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(Map.of("mensaje",e.getMessage()));
    }
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class) public ResponseEntity<?> conflict(Exception e){
        return ResponseEntity.status(409).body(Map.of("mensaje","Registro duplicado o relacionado con otros registros"));
    }

    @ExceptionHandler(feign.FeignException.class)
    public ResponseEntity<?> unavailable(feign.FeignException error) {
        return ResponseEntity.status(503).body(Map.of("mensaje", "El servicio consultado no respondió correctamente"));
    }
}

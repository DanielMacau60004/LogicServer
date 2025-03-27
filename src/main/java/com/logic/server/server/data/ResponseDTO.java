package com.logic.server.server.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@JsonPropertyOrder({"status", "code", "result"})
public class ResponseDTO<E> {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final HttpStatus status;
    private final E result;

    ResponseDTO(HttpStatus status, E result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status.name();
    }

    public int getCode() {
        return status.value();
    }

    public E getResult() {
        return result;
    }

    public static <T> ResponseEntity<String> entity(T obj) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseDTO<T> response = new ResponseDTO<>(HttpStatus.OK, obj);
            return new ResponseEntity<>(mapper.writeValueAsString(response), headers, HttpStatus.OK);
        } catch (Exception e) {
            return error(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<String> error(Exception exception, HttpStatus status) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseDTO<String> response = new ResponseDTO<>(status, exception.getMessage());
            return new ResponseEntity<>(mapper.writeValueAsString(response), headers, status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.inn.alstom.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AlstomUtils {
    private AlstomUtils(){

    }
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
}

package com.hospital.exception;



import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final CustomMsg customMsg;

    public GlobalExceptionHandler(CustomMsg customMsg){
        this.customMsg = customMsg;
    }


    private static String getFieldName(JsonMappingException jme){
        if(jme.getPath() == null || jme.getPath().isEmpty())
            return null;
        return jme.getPath().get(jme.getPath().size()-1).getFieldName();
    }

    //REQUEST BODY EXCEPTIONS

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException
            (HttpMessageNotReadableException ex, HttpServletRequest request){

        ExceptionResponse exceptionResponse = null;

        Throwable cause = ex.getCause();

        if(cause instanceof InvalidFormatException ife &&
                ife.getTargetType().isEnum()){

            exceptionResponse = new ExceptionResponse(LocalDateTime.now(),
                    400,
                    "Invalid value for enum field",
                    request.getRequestURI(),
                    Collections.singletonMap("error",
                            "Invalid "+getFieldName(ife)+" : '"+ife.getValue()+"'. Allowed Only: "+
                                    Arrays.toString(ife.getTargetType().getEnumConstants())));
        }

        else if (cause instanceof InvalidFormatException ife){
            exceptionResponse = new ExceptionResponse(
                    LocalDateTime.now(),
                    400,
                    "Invalid Format Exception",
                    request.getRequestURI(),
                    Collections.singletonMap("error","Invalid "+getFieldName(ife)
                            +" : '"+ife.getValue()+"'.Enter Valid "+getFieldName(ife)+".")
            );
        }

        else if(cause instanceof MismatchedInputException mismatchEx) {
//            map.put(getFieldName(mismatchEx), "Invalid and mismatched data sent.Only"
//                    + mismatchEx.getTargetType() + " is Accepted");
            exceptionResponse = new ExceptionResponse
                    (LocalDateTime.now(),
                            400,
                            "Mismatched input data"+getFieldName(mismatchEx),
                            request.getRequestURI(),
                            Collections.singletonMap("error","Invalid "+getFieldName(mismatchEx)
                                    +".Enter Valid "+getFieldName(mismatchEx)+"."));
        }

        else if(cause instanceof IgnoredPropertyException ipe){

            exceptionResponse = new ExceptionResponse(
                    LocalDateTime.now(),
                    400,
                    "Ignored Property Exception",
                    request.getRequestURI(),
                    Collections.singletonMap("error",
                            "Invalid property : '"+ipe.getPropertyName()+"' found in request.Please remove the property.")
            );
        }

        else if(cause instanceof UnrecognizedPropertyException urpe){

            exceptionResponse = new ExceptionResponse(
                    LocalDateTime.now(),
                    400,
                    "Unrecognized Property Exception",
                    request.getRequestURI(),
                    Collections.singletonMap("error",
                            "Unrecognized property : '"+urpe.getPropertyName()+"' found in request.Please remove the property.")
            );
        }

        else if(cause instanceof JsonMappingException jme) {

            exceptionResponse = new ExceptionResponse
                    (LocalDateTime.now(),
                            400,
                            "Json Mapping Failed",
                            request.getRequestURI(),
                            Collections.singletonMap("error","Invalid "+getFieldName(jme)
                                    +".Enter Valid "+getFieldName(jme)+"."));
        }

        else if(cause instanceof JsonParseException jpe) {

            exceptionResponse = new ExceptionResponse
                    (LocalDateTime.now(),
                            400,
                            "Json Parsing Failed at Line : "+jpe.getLocation().getLineNr(),
                            request.getRequestURI(),
                            Collections.singletonMap("error", "Line number : "+jpe.getLocation().getLineNr()
                                    +", Column : "+jpe.getLocation().getColumnNr()
                                    +".")
                    );
        }

        else if(cause instanceof JsonProcessingException jpe) {
            exceptionResponse = new ExceptionResponse
                    (LocalDateTime.now(),
                            400,
                            "Json Mapping Failed",
                            request.getRequestURI(),
                            Collections.singletonMap("error", "Json Processing Failed"));
        }

        else {
            exceptionResponse = new ExceptionResponse
                    (LocalDateTime.now(),
                            400,
                            "Json Request Failed",
                            request.getRequestURI(),
                            Collections.singletonMap("error", "Json deserialization Failed"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException
            (MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error->{
                    String finalMsg = customMsg.getMessage(error.getField(), error.getDefaultMessage());
                    errors.put(error.getField(), finalMsg);
                });

        if(errors.size()!=0 && !errors.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ExceptionResponse(
                            LocalDateTime.now(),
                            400,
                            "Method Argument Not Valid Exception",
                            request.getRequestURI(),
                            errors)
            );
        }

        return ResponseEntity.badRequest().body(
                new ExceptionResponse(LocalDateTime.now(),400,
                        "Method Argument Not Valid Exception",
                        request.getRequestURI(),
                        Collections.singletonMap("error","Invalid Data Sent")));
    }

    // PATH OR QUERY PARAMETER

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolationException
            (ConstraintViolationException ex,HttpServletRequest req){

        ExceptionResponse exception = null;
        String causeEx = ex.getMessage();
        String msg = "";

        msg = ex.getConstraintViolations().stream().findFirst().map(
                violation -> {
                    String invalidValue = violation.getInvalidValue() == null ? "" :
                            violation.getInvalidValue().toString();
                    String path = violation.getPropertyPath().toString();
                    if (path.contains("getUserById") || path.contains("updateUserById") ||
                            path.contains("toUpdatePassword") || path.contains("deleteUserById") ) {
                        return "Invalid User Id : '" + invalidValue + "'.Enter a valid User ID";}
                    else if (path.contains("getUserByEmail")) {
                        return "Invalid email : '" + invalidValue + "'.Enter a valid email addess";}
                    else
                        return "Invalid Value : '"+ invalidValue + "'.Enter valid Details";
                }
        ).orElse("Please enter valid details");


        exception = new ExceptionResponse(
                LocalDateTime.now(),
                400,
                "Constraint Violation Exception",
                req.getRequestURI(),
                Collections.singletonMap("error", msg)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException
            (MethodArgumentTypeMismatchException ex,HttpServletRequest request){
        String msg = "Invalid value : '"+ex.getValue()+"' provided for "+ ex.getName() + ".Please check your "+ex.getName()+".";
        ExceptionResponse exception = new ExceptionResponse(
                LocalDateTime.now(),
                400,
                "Method Argument Type Mismatch Exception",
                request.getRequestURI(),
                Collections.singletonMap("error",msg));
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolationException
            (MissingPathVariableException ex,HttpServletRequest request){
        ExceptionResponse exception = new ExceptionResponse(
                LocalDateTime.now(),
                400,
                "MissingPathVariableException",
                request.getRequestURI(),
                Collections.singletonMap("error",ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse>handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest req){
        ExceptionResponse exception = new ExceptionResponse(
                LocalDateTime.now(),
                400,
                "Illegal Argument Exception",
                req.getRequestURI(),
                Collections.singletonMap("error",ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(PatientAlreadyExistsByEmail.class)
    public ResponseEntity<ExceptionResponse>handlePatientAlreadyExistsByEmail
            (PatientAlreadyExistsByEmail ex,HttpServletRequest request) {

        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        LocalDateTime.now(),
                        400,
                        "Patient Already Exists",
                        request.getRequestURI(),
                        Collections.singletonMap("error", ex.getMessage())
                )
        );
    }

    @ExceptionHandler(DoctorAlreadyExists.class)
    public ResponseEntity<ExceptionResponse>handleDoctorAlreadyExists
            (DoctorAlreadyExists ex,HttpServletRequest request) {

        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        LocalDateTime.now(),
                        400,
                        "Doctor Already Exists",
                        request.getRequestURI(),
                        Collections.singletonMap("error", ex.getMessage())
                )
        );
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ExceptionResponse>handleDoctorNotFoundException
            (DoctorNotFoundException ex,HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse(
                        LocalDateTime.now(),
                        404,
                        "Doctor Not Found",
                        request.getRequestURI(),
                        Collections.singletonMap("error", ex.getMessage())
                )
        );
    }

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ExceptionResponse>handleLoginFailedException
            (LoginFailedException ex,HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ExceptionResponse(
                        LocalDateTime.now(),
                        401,
                        "Login Failed",
                        request.getRequestURI(),
                        Collections.singletonMap("error", ex.getMessage())
                )
        );
    }



}









































//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.exc.*;
//import jakarta.servlet.http.HttpServletRequest;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//
//    private static String getFieldName(JsonMappingException jme) {
//        if (jme.getPath() == null || jme.getPath().isEmpty())
//            return null;
//        return jme.getPath()
//                .get(jme.getPath().size() - 1)
//                .getFieldName();
//    }
//
//    private Map<String, List<String>> buildError(String key, String message) {
//        return Map.of(key, List.of(message));
//    }
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException
//            (HttpMessageNotReadableException ex,HttpServletRequest request) {
//
//        ExceptionResponse response;
//        Throwable cause = ex.getCause();
//
//        if (cause instanceof InvalidFormatException ife &&
//                ife.getTargetType().isEnum()) {
//
//            response = new ExceptionResponse(
//                    LocalDateTime.now(),
//                    400,
//                    "Invalid value for enum field",
//                    request.getRequestURI(),
//                    buildError(
//                            "error",
//                            "Invalid " + getFieldName(ife) +
//                                    " : '" + ife.getValue() +
//                                    "'. Allowed Only: " +
//                                    Arrays.toString(ife.getTargetType().getEnumConstants())
//                    )
//            );
//        }
//
//        else if (cause instanceof InvalidFormatException ife) {
//
//            response = new ExceptionResponse(
//                    LocalDateTime.now(),
//                    400,
//                    "Invalid Format Exception",
//                    request.getRequestURI(),
//                    buildError(
//                            "error",
//                            "Invalid " + getFieldName(ife) +
//                                    " : '" + ife.getValue() +
//                                    "'. Enter Valid " +
//                                    getFieldName(ife) + "."
//                    )
//            );
//        }
//
//        else if (cause instanceof MismatchedInputException mismatchEx) {
//
//            response = new ExceptionResponse(
//                    LocalDateTime.now(),
//                    400,
//                    "Mismatched input data",
//                    request.getRequestURI(),
//                    buildError(
//                            "error",
//                            "Invalid " + getFieldName(mismatchEx) +
//                                    ". Enter Valid " +
//                                    getFieldName(mismatchEx) + "."
//                    )
//            );
//        }
//
//        else if (cause instanceof IgnoredPropertyException ipe) {
//
//            response = new ExceptionResponse(
//                    LocalDateTime.now(),
//                    400,
//                    "Ignored Property Exception",
//                    request.getRequestURI(),
//                    buildError(
//                            "error",
//                            "Invalid property : '" +
//                                    ipe.getPropertyName() +
//                                    "' found in request. Please remove it."
//                    )
//            );
//        }
//
//        else if (cause instanceof UnrecognizedPropertyException urpe) {
//
//            response = new ExceptionResponse(
//                    LocalDateTime.now(),
//                    400,
//                    "Unrecognized Property Exception",
//                    request.getRequestURI(),
//                    buildError(
//                            "error",
//                            "Unrecognized property : '" +
//                                    urpe.getPropertyName() +
//                                    "' found in request. Please remove it."
//                    )
//            );
//        }
//
//        else if (cause instanceof JsonMappingException jme) {
//
//            response = new ExceptionResponse(
//                    LocalDateTime.now(),
//                    400,
//                    "Json Mapping Failed",
//                    request.getRequestURI(),
//                    buildError(
//                            "error",
//                            "Invalid " + getFieldName(jme) +
//                                    ". Enter Valid " +
//                                    getFieldName(jme) + "."
//                    )
//            );
//        }
//
//        else if (cause instanceof JsonParseException jpe) {
//
//            response = new ExceptionResponse(
//                    LocalDateTime.now(),
//                    400,
//                    "Json Parsing Failed",
//                    request.getRequestURI(),
//                    buildError(
//                            "error",
//                            "Line : " +
//                                    jpe.getLocation().getLineNr() +
//                                    ", Column : " +
//                                    jpe.getLocation().getColumnNr()
//                    )
//            );
//        }
//
//        else if (cause instanceof JsonProcessingException) {
//
//            response = new ExceptionResponse(
//                    LocalDateTime.now(),
//                    400,
//                    "Json Processing Failed",
//                    request.getRequestURI(),
//                    buildError(
//                            "error",
//                            "Json Processing Failed"
//                    )
//            );
//        }
//
//        else {
//
//            response = new ExceptionResponse(
//                    LocalDateTime.now(),
//                    400,
//                    "Json Request Failed",
//                    request.getRequestURI(),
//                    buildError(
//                            "error",
//                            "Json deserialization Failed"
//                    )
//            );
//        }
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(response);
//    }
//
//
//    @ExceptionHandler(PatientAlreadyExistsByEmail.class)
//    public ResponseEntity<ExceptionResponse>handlePatientAlreadyExistsByEmail
//            (PatientAlreadyExistsByEmail ex,HttpServletRequest request) {
//
//        return ResponseEntity.badRequest().body(
//                new ExceptionResponse(
//                        LocalDateTime.now(),
//                        400,
//                        "Patient Already Exists",
//                        request.getRequestURI(),
//                        buildError("error", ex.getMessage())
//                )
//        );
//    }
//
//    @ExceptionHandler(DoctorAlreadyExists.class)
//    public ResponseEntity<ExceptionResponse>handleDoctorAlreadyExists
//            (DoctorAlreadyExists ex,HttpServletRequest request) {
//
//        return ResponseEntity.badRequest().body(
//                new ExceptionResponse(
//                        LocalDateTime.now(),
//                        400,
//                        "Doctor Already Exists",
//                        request.getRequestURI(),
//                        buildError("error", ex.getMessage())
//                )
//        );
//    }
//
//    @ExceptionHandler(DoctorNotFoundException.class)
//    public ResponseEntity<ExceptionResponse>handleDoctorNotFoundException
//            (DoctorNotFoundException ex,HttpServletRequest request) {
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                new ExceptionResponse(
//                        LocalDateTime.now(),
//                        404,
//                        "Doctor Not Found",
//                        request.getRequestURI(),
//                        buildError("error", ex.getMessage())
//                )
//        );
//    }
//
//    @ExceptionHandler(LoginFailedException.class)
//    public ResponseEntity<ExceptionResponse>handleLoginFailedException
//            (LoginFailedException ex,HttpServletRequest request) {
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
//                new ExceptionResponse(
//                        LocalDateTime.now(),
//                        401,
//                        "Login Failed",
//                        request.getRequestURI(),
//                        buildError("error", ex.getMessage())
//                )
//        );
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ExceptionResponse>handleMethodArgumentNotValidException
//            (MethodArgumentNotValidException ex,HttpServletRequest request) {
//
//        Map<String, List<String>> errors = new HashMap<>();
//
//        ex.getBindingResult()
//                .getFieldErrors()
//                .forEach(error -> {
//                    errors.computeIfAbsent(error.getField(), key -> new ArrayList<>())
//                            .add(error.getDefaultMessage());
//                });
//
//        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(),
//                        400, "Validation Failed",
//                        request.getRequestURI(), errors);
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//    }
//
//
//
//}

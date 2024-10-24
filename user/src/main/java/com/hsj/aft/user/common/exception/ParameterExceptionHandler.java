package com.hsj.aft.user.common.exception;//package com.hsj.aft.api.exception;
//
//import com.hsj.aft.api.dto.ResponseDTO;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class ParameterExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ResponseDTO> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//
//        String errorMessage = ex.getBindingResult()
//                .getFieldErrors()
//                .get(0)
//                .getDefaultMessage();  // 첫 번째 에러 메시지만 가져오기
//
//        return ResponseEntity
//                .badRequest()
//                .body(new ResponseDTO("400", errorMessage, null));
//    }
//}

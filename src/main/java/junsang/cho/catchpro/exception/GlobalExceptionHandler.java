package junsang.cho.catchpro.exception;

import jakarta.validation.UnexpectedTypeException;
import junsang.cho.catchpro.common.dto.response.CommonResponse;
import junsang.cho.catchpro.common.dto.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jdbc.BatchFailedException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static junsang.cho.catchpro.common.dto.response.ErrorCode.*;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 빈 문자열을 null로 변환
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResponse<?> Exception(Exception e) {
//        printError(e);
        e.printStackTrace();
        return CommonResponse.fail(ErrorCode.COMMON_SYSTEM_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public CommonResponse<?> NoSuchElementException(NoSuchElementException e) {
        log.warn(e.getMessage());
        return CommonResponse.fail(COMMON_NO_SUCH_ELEMENT);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, UnexpectedTypeException.class, BindException.class})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public CommonResponse<?> DataIntegrityViolationException(Exception e) {
        printError(e);
        return CommonResponse.fail(COMMON_INVALID_PARAMETER);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public CommonResponse<?> DuplicateKeyException(DuplicateKeyException e) {
        log.warn(e.getMessage());
        return CommonResponse.fail(COMMON_DUPLICATE_ENTRY);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public CommonResponse<?> AccessDeniedException(AccessDeniedException e) {
        log.warn(e.getMessage());
        return CommonResponse.fail(COMMON_ACCESS_DENIED);
    }

//    @ExceptionHandler(FeignException.class)
//    @ResponseStatus(value = HttpStatus.OK)
//    @ResponseBody
//    public CommonResponse<?> FeignException(FeignException e) {
//        log.warn(e.getMessage());
//        return CommonResponse.fail(COMMON_SYSTEM_ERROR);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public CommonResponse<?> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return CommonResponse.fail(COMMON_INVALID_PARAMETER, errorMessage);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public CommonResponse<?> BadCredentialsException(BadCredentialsException e) {
        log.warn(e.getMessage());
        return CommonResponse.fail(COMMON_BAD_CREDENTIAL);
    }


    @ExceptionHandler(BatchFailedException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public CommonResponse<?> BatchFailedException(BatchFailedException e) {
        log.warn(e.getMessage());
        return CommonResponse.fail(BATCH_SCHEDULER_FAILED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public CommonResponse<?> IllegalArgumentException(IllegalArgumentException e) {
        log.warn(e.getMessage());
        return CommonResponse.fail(COMMON_DUPLICATE_ENTRY);
    }


    private void printError(Exception e){
        log.error("Exception [Exception Name]: {}", e.getClass().getName());
        log.error("Exception [Err_Msg]: {}", e.getMessage());
        log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
    }
}

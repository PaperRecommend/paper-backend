package com.paper.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.ObjectError;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO<T> {
    private boolean success;

    private String message;

    public ResponseVO(Boolean success,String message){
        this.success=success;
        this.message=message;
    }

    public static ResponseVO buildSuccess(String msg){
        return new ResponseVO(true,msg);
    }

    public static ResponseVO buildSuccess(){
        return new ResponseVO(true,"");
    }

    public static ResponseVO buildFailure(String msg){
        return new ResponseVO(false,msg);
    }

    public static ResponseVO buildError(List<ObjectError> constraintViolations){
        Set<String> setMessage=new HashSet<>();
        for(ObjectError message:constraintViolations){
            setMessage.add(message.getDefaultMessage());
        }
        return buildFailure(setMessage.toString());
    }

}

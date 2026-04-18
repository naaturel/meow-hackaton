package com.hackaton.meow.domain.exceptions;

public class ServiceException extends Exception{
    public ServiceException(String message, Exception innerException){
        super(message, innerException);
    }

    public ServiceException(String message){
        super(message);
    }
}

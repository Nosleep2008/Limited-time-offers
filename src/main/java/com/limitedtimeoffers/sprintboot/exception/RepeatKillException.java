package com.limitedtimeoffers.sprintboot.exception;

public class RepeatKillException extends SeckillException{
    public RepeatKillException(String message){
        super(message);
    }
    public RepeatKillException(String message, Throwable cause){
        super(message,cause);
    }

}

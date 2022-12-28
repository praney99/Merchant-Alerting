package com.homedepot.mm.pc.merchantalerting.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(){
        super();
    }

    public NotFoundException(String message){
        super(message);
    }

}

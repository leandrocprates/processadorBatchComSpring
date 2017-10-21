/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fnt.esb.webchat.enumm;

/**
 *
 * @author leandro.prates
 */
public enum HttpEnum {
    
    HTTP_OK (200) , 
    HTTP_INVALIDO(400),
    HTTP_ERRO_INTERNO(500);
    
    public int code ; 
    
    HttpEnum(int code){
        this.code = code;
    }
    
    
}

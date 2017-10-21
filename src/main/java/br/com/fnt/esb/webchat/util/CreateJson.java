/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fnt.esb.webchat.util;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author leandro.prates
 */
public class CreateJson {
    
    
    static final Logger logger = LogManager.getLogger(CreateJson.class.getName());
    
    
    public static String createGson(Object objeto){
        Gson gson = new Gson();
        String  stringJson =  gson.toJson(objeto);
        logger.debug("Json Gerado : {}" , stringJson );
        return stringJson;
    }
    
    
}

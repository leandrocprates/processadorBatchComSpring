/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fnt.esb.webchat.batch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * executar como batch :   java -cp .;batch-1.0.jar  br.com.fnt.esb.webchat.batch.Main 
 * @author leandro.prates
 */
public class Main {
    
    static final Logger logger = LogManager.getLogger(Main.class.getName());
    
    public static void main (String args[]){
        
        logger.debug("Subindo processador...... ");
        
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);   
        
        Config config = (Config) context.getBean(Config.class);
        
        
        
        //new Thread(new ThreadMessageEntrada(config)).start();
        new Thread(new ThreadMessageSaida(config)).start();
        
        logger.trace("Fim aplicacao");
        
    }
    
    
}

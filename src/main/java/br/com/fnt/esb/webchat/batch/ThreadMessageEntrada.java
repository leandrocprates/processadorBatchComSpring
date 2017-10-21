/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fnt.esb.webchat.batch;

import br.com.fnt.esb.webchat.http.HttpClient;
import br.com.fnt.esb.webchat.dao.ChatEntradaSaidaDAO;
import br.com.fnt.esb.webchat.model.ChatEntradaSaida;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Processa mensagens que vem do Mobile para a Interface WEB 
 * @author leandro.prates
 */
public class ThreadMessageEntrada implements Runnable{

    
    static final Logger logger = LogManager.getLogger(ThreadMessageEntrada.class.getName());
    Config config = null ; 
    ChatEntradaSaidaDAO chatEntradaSaidaDAO ; 
    HttpClient httpClient ; 
    
    
    public ThreadMessageEntrada(Config config){
        this.config = config ; 
        this.chatEntradaSaidaDAO = new ChatEntradaSaidaDAO(config) ; 
        this.httpClient = new HttpClient(config.getUrlDistribuidor(), false , null);
    }
    
    public void run() {
        logger.debug("ThreadMessageEntrada -- Distribuidor ");
        logger.debug("Config url Distribuidor : [{}] "  , config.getUrlDistribuidor() );
        
        while ( true ){
            try{
                List<ChatEntradaSaida> listaChatEntradaSaida = chatEntradaSaidaDAO.selectMessage("ENTRADA", "NAO_PROCESSADO");
                
                for ( ChatEntradaSaida chatEntradaSaida :  listaChatEntradaSaida ){
                    httpClient.criarRequisicao(chatEntradaSaida);
                }
                
                Thread.sleep(config.getTimeoutMessageEntrada());
            }catch(Exception ex ){
                logger.error("Exception: {} " ,  ex);
            }
        }
    }
    
}

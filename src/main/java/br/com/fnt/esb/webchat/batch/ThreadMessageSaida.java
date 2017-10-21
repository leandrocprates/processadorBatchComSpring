/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fnt.esb.webchat.batch;

import br.com.fnt.esb.webchat.http.HttpClient;
import br.com.fnt.esb.webchat.dao.ChatEntradaSaidaDAO;
import br.com.fnt.esb.webchat.enumm.HttpEnum;
import br.com.fnt.esb.webchat.model.ChatEntradaSaida;
import br.com.fnt.esb.webchat.fcm.FcmData;
import br.com.fnt.esb.webchat.fcm.FcmNotificacao;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Processa mensagens que vem da Interface WEB  para o Mobile
 * @author leandro.prates
 */
public class ThreadMessageSaida implements Runnable{

    
    static final Logger logger = LogManager.getLogger(ThreadMessageSaida.class.getName());
    Config config = null ; 
    ChatEntradaSaidaDAO chatEntradaSaidaDAO ; 
    
    HttpClient httpClient ; 
    
    public ThreadMessageSaida(Config config){
        this.config = config ; 
        this.chatEntradaSaidaDAO = new ChatEntradaSaidaDAO(config) ; 
        this.httpClient = new HttpClient(config.getUrlFcm() , true , config.getKeyFcm() );
    }
    
    public void run() {
        
        logger.debug("ThreadMessageSaida -- FCM ");
        logger.debug("URL FCM [{}]" , this.config.getUrlFcm() );
        logger.debug("KEY FCM [{}]" , this.config.getKeyFcm() );

        
        
        while ( true ){
            try{
                List<ChatEntradaSaida> listaChatEntradaSaida = chatEntradaSaidaDAO.selectMessage("SAIDA", "NAO_PROCESSADO");
                
                for ( ChatEntradaSaida chatEntradaSaida :  listaChatEntradaSaida ){
                    
                    FcmData fcmData = criarMessageData(chatEntradaSaida); 
                    
                    int codigoResposta = httpClient.criarRequisicao(fcmData);
                    
                    if ( codigoResposta == HttpEnum.HTTP_OK.code ){
                        logger.debug("Resposta OK");
                    }
                }
                
                Thread.sleep(config.getTimeoutMessageSaida());
            }catch(Exception ex ){
                logger.error("Exception: {} " ,  ex);
            }
        }
        
    }
    
    
   
    
    
    private FcmData criarMessageData(ChatEntradaSaida chatEntradaSaida){
        FcmData fcmData =new FcmData();
        fcmData.getData().setMessage(chatEntradaSaida.getMensagem());
        fcmData.getData().setWho("contact");
        fcmData.getData().setTime(chatEntradaSaida.getData());
        fcmData.setTo(chatEntradaSaida.getToken());
        fcmData.setPriority("high");
        fcmData.setRestricted_package_name("");
        return fcmData;
    }
    
    
    private FcmNotificacao criarNotificacao(ChatEntradaSaida chatEntradaSaida){
        FcmNotificacao fcmNotificacao = new FcmNotificacao();
        fcmNotificacao.getNotification().setBody("Corpo Mensagem");
        fcmNotificacao.getNotification().setTitle("Titulo");
        fcmNotificacao.setTo(chatEntradaSaida.getToken());
        fcmNotificacao.setPriority("high");
        fcmNotificacao.setRestricted_package_name("");
        return fcmNotificacao;
    }
    
    
}

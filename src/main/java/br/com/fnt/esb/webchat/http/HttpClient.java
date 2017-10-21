/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fnt.esb.webchat.http;

/**
 *
 * @author leandro.prates
 */


import br.com.fnt.esb.webchat.util.CreateJson;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public class HttpClient {
    
    
    static final Logger logger = LogManager.getLogger(HttpClient.class.getName());
    
    String url ; 
    boolean sendToFcm ;
    String keyFcm ;

    public HttpClient(String url, boolean sendToFcm, String keyFcm) {
        this.url = url;
        this.sendToFcm = sendToFcm;
        this.keyFcm = keyFcm ; 
    }
    
    public int criarRequisicao(Object objeto){
        
        int codigoResposta = 0  ; 
        
        try{
            OkHttpClient client = new OkHttpClient();

            Request.Builder  requestBuilder = new Request.Builder();
            requestBuilder.url(this.url); 
            
            if ( this.sendToFcm ) {
                requestBuilder.addHeader("Authorization", "key=" + this.keyFcm );
            }
            
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            
            String stringJson = CreateJson.createGson(objeto);
            
            
            RequestBody requestBody = RequestBody.create(mediaType, stringJson) ; 
            requestBuilder.post(requestBody) ; 
            
            Request request  = requestBuilder.build();
            
            Response response = client.newCall(request).execute() ; 
            
            logger.debug("STATUS RESPOSTA: [{}]" , response.code());
            
            codigoResposta = response.code();
            
            response.close();
            
            
        }catch(Exception ex){
            logger.error("Erro ao efetuar requisicao HTTP: {}" , ex );
        }
        
        return codigoResposta ; 
        
    }
    
    
    
    
}
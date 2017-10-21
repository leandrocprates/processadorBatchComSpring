/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fnt.esb.webchat.model;

import lombok.Data;

/**
 *
 * @author leandro.prates
 */
@Data
public class ChatEntradaSaida {
        private Long id;
	private String mensagem;
	private String tipo ;
	private String status ;
        private String token ;
        private String data ;
        private long fkChatFilaAten ; 
        
        
}

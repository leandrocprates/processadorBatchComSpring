/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fnt.esb.webchat.dao;

//import br.com.fnt.esb.webchat.model.enumm.StatusChatEnum;
//import br.com.fnt.esb.webchat.model.enumm.TipoEnum;
import br.com.fnt.esb.webchat.model.ChatEntradaSaida;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.springframework.jdbc.core.RowMapper;

//import br.com.fnt.esb.webchat.model.mensagem.ChatEntradaSaida;

public class ChatEntradaSaidaRowMapper implements RowMapper {  

    
    
    SimpleDateFormat formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    
    
    
    @Override
    public Object mapRow(ResultSet rs, int i) throws SQLException {
        ChatEntradaSaida chatEntradaSaida = new ChatEntradaSaida();
        chatEntradaSaida.setId(rs.getLong("ID"));
        chatEntradaSaida.setMensagem(rs.getString("MENSAGEM"));
        chatEntradaSaida.setTipo(rs.getString("TIPO"));
        chatEntradaSaida.setStatus(rs.getString("STATUS"));
        
        return chatEntradaSaida ; 
    }
    
}

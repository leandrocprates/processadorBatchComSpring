/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fnt.esb.webchat.dao;

//import br.com.fnt.esb.webchat.model.mensagem.ChatEntradaSaida;
import br.com.fnt.esb.webchat.batch.Config;
import br.com.fnt.esb.webchat.model.ChatEntradaSaida;
import java.util.List;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 * @author leandro.prates
 */
public class ChatEntradaSaidaDAO {
    
    
    static final Logger logger = LogManager.getLogger(ChatEntradaSaidaDAO.class.getName());
    
    private DataSource dataSource;
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    
    public ChatEntradaSaidaDAO(Config config ){
        this.dataSource = config.getDataSource();
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
    }
    
    public List<ChatEntradaSaida> selectMessage(String tipo, String status ){
        
        String SQL = "SELECT ID,MENSAGEM, TIPO,STATUS FROM PS_CHAT_ENTRADA_SAIDA " + 
                    " WHERE TIPO = :tipo AND STATUS = :status ORDER BY ID  "; 
        
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("tipo", tipo); 
        namedParameters.addValue("status", status);
        
        List<ChatEntradaSaida> listaChatEntradaSaida = namedParameterJdbcTemplate.query(SQL, namedParameters, new ChatEntradaSaidaRowMapper());
        
        return listaChatEntradaSaida;
        
    }
    
    public void deleteById(long id ){
        String SQL = " DELETE FROM PS_CHAT_ENTRADA_SAIDA WHERE ID = :id" ; 
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id); 
        namedParameterJdbcTemplate.update(SQL, namedParameters) ; 
    }
    
}

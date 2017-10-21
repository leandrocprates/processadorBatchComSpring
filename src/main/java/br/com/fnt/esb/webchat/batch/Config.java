/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fnt.esb.webchat.batch;

import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author leandro.prates
 */
@Configuration
@ComponentScan("br.com.fnt.esb")
@PropertySource("classpath:config.properties")
@ImportResource({"classpath:Spring-Datasource.xml"})
public class Config {

        static final Logger logger = LogManager.getLogger(Config.class.getName());

        @Autowired
        private DataSource dataSource;
        
        @Autowired
	Environment env;    
        
        @Value("${urlDistribuidor}")
        String urlDistribuidor ; 
        
        @Value("${urlFcm}")
        String urlFcm ; 
    
        @Value("${keyFcm}")
        String keyFcm ; 
        
        @Value("${timeoutMessageEntrada}")
        int timeoutMessageEntrada;
        
        @Value("${timeoutMessageSaida}")
        int timeoutMessageSaida;
        
        public String getUrlDistribuidor(){
            //urlDistribuidor = env.getProperty("urlDistribuidor"); 
            return urlDistribuidor; 
        }

        public String getUrlFcm() {
            return urlFcm;
        }

        public String getKeyFcm() {
            return keyFcm;
        }

        public int getTimeoutMessageEntrada() {
            return timeoutMessageEntrada;
        }

        public int getTimeoutMessageSaida() {
            return timeoutMessageSaida;
        }
        
        public DataSource getDataSource() {
            return dataSource;
        }

        public void setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
        }
        
        
}

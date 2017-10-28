# processadorBatchComSpring


Executar processo batch :  **java -cp .;batch-1.0.jar  br.com.fnt.esb.webchat.batch.Main**


Este projeto tem por objectivo  a criação de um processo batch utilizando spring para carregar arquivos de configuracao e acesso ao banco de dados e enviar mensagem para o FCM (Firebase Cloud Message).   

- Mysql - Banco de Dados 
- Lombok - Para geração de Getter e Setter automaticos 
- spring JDBC (NamedParameter)
- FCM 

- Banco de dados do projeto encontrado na pasta **src/main/resources/springdb.sql**.




1 - Arquivo de configuração do banco de dados **Spring-Datasource.xml** 

```xml 
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

	<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
            <property name="driverClassName" value="com.mysql.jdbc.Driver" />
            <property name="url" value="jdbc:mysql://localhost:3306/springdb" />
            <property name="username" value="root" />
            <property name="password" value="root" />
	</bean>

</beans>
```


2 - Arquivo de properties **config.properties**

```

urlDistribuidor=http://localhost:8080/fntesb-ws/ws/chat/mobile/distribuidor

urlFcm=https://fcm.googleapis.com/fcm/send

#chave fcm
keyFcm=AAAAGYw4hd8:APA91bG_vtOqwjhgXCbtwLltqUJuWSIF_H4ScHybfNhWoJ5JNxEnLKogPwyzfpHZidfn4H95jO88ViSaI7mgJLnm7K9fqrch8Ma-R-pbRaS1slspAum7UmBsa5ljbxJc1EUtDNWRt0CN


timeoutMessageEntrada=10000
timeoutMessageSaida=10000


```


3 - Configuração de logs apache **log4j2.xml** 


```xml 
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
  <Appenders>
  
    <Console name="Console" target="SYSTEM_OUT">
      	<PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
    </Console>
    
    <RollingFile name="RollingFile" fileName="C:/Users/lprates/Desktop/springBatch/logBatch.log"
                     filePattern="C:/Users/lprates/Desktop/springBatch/logBatch.log-%d{MM-dd-yyyy}-%i.log.gz">
          <PatternLayout>
            <Pattern>%d %p [%t] %-5level %logger{36} - %m%n</Pattern>
          </PatternLayout>
          <Policies>
            <TimeBasedTriggeringPolicy />
            <SizeBasedTriggeringPolicy size="250 MB"/>
          </Policies>
          <DefaultRolloverStrategy max="20"/>
    </RollingFile>    
    
    <Async name="Async">
      	<AppenderRef ref="RollingFile"/>
    </Async>    
    
  </Appenders>
  
  <Loggers>
    <Root level="trace">
      <AppenderRef ref="Console"/>
      <AppenderRef ref="Async"/>
    </Root>
  </Loggers>
  
</Configuration>



```


4 - Classe principal **Main.java** , responsavel pelo start da aplicação . 


``` 
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
 *
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


```

5 - A classe **Config.java** carrega os arquivos de configuração **config.properties** , **Spring-Datasource.xml** , injetando os valores das propriedades e o datasource. 


```

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

```


6 - A classe **ThreadMessageSaida.java** é a responsavel por criar as notificacoes que serão geradas para o FCM para o token do celular indicado . 


```

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
                    
                    
                    chatEntradaSaida.setToken("dvveubohvVU:APA91bGaAGUmEYHurCHXeiivbO4m2dVg64TY3yj4b7ZFPDLLORdYocDi4Ta2VX_CQuAA3tJhW6_9pK021ypmqr4g8LWDCNqdZSDb5Ul264ROUx5ZTnDhNi7j6vycI03EWYlofdRUpNGq");
                    
                    //FcmData fcmData = criarMessageData(chatEntradaSaida); 
                    
                    FcmNotificacao fcmNotificacao = criarNotificacao(chatEntradaSaida); 
                    
                    int codigoResposta = httpClient.criarRequisicao(fcmNotificacao);
                    
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


```

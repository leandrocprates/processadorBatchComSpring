# processadorBatchComSpring



Este projeto tem por objectivo  a criaçaõ de um processo batch utilizando spring para carregar arquivos de configuracao e acesso ao banco de dados e enviar mensagem para o FCM (Firebase Cloud Message).   

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

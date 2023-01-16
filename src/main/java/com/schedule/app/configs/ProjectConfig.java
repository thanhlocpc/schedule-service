package com.schedule.app.configs;

import com.schedule.app.handler.RestTemplateHandler;
import com.schedule.app.utils.ScheduleServiceException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties
public class ProjectConfig {

    private final ScheduleServiceException scheduleServiceException;

    @Autowired
    public ProjectConfig(ScheduleServiceException scheduleServiceException) {
        this.scheduleServiceException = scheduleServiceException;
    }

    @Bean
    public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RestTemplateHandler());

        return restTemplate;

    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        List<Server> servers = new ArrayList<>();
        System.out.println(scheduleServiceException.getSwaggerUrl());

        Server server = new Server();
        server.setUrl(scheduleServiceException.getSwaggerUrl());
        server.setDescription("Swagger");

        servers.add(server);

        return new OpenAPI()
                .info(new Info().title("Tutor Service Swagger")
                        .description("Tutor Service Swagger UI")
                        .version("1.0"))
                .servers(servers);
    }

}

package com.sparta.product.infrastructure.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchClientConfig {

  @Value("${spring.elasticsearch.rest.host}")
  String host;

  @Value("${spring.elasticsearch.rest.port}")
  int port;

  @Value("${spring.elasticsearch.fingerprint}")
  String fingerprint;

  @Value("${spring.elasticsearch.account}")
  String account;

  @Value("${spring.elasticsearch.password}")
  String password;

  @Bean
  RestClientTransport restClientTransport(
      RestClient restClient, ObjectProvider<RestClientOptions> restClientOptions) {
    return new RestClientTransport(
        restClient, new JacksonJsonpMapper(), restClientOptions.getIfAvailable());
  }

  @Bean
  public ElasticsearchClient elasticsearchClientWithSSL() {
    SSLContext sslContext = TransportUtils.sslContextFromCaFingerprint(fingerprint);

    BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
    credsProv.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(account, password));

    RestClient restClient =
        RestClient.builder(new HttpHost(host, port, "https"))
            .setHttpClientConfigCallback(
                hc -> hc.setSSLContext(sslContext).setDefaultCredentialsProvider(credsProv))
            .build();

    ElasticsearchTransport transport =
        new RestClientTransport(restClient, new JacksonJsonpMapper());
    return new ElasticsearchClient(transport);
  }
}

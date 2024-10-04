package com.sparta.product.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.EnableCassandraAuditing;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.sparta.product.domain.repository.jpa")
@EnableCassandraRepositories(basePackages = "com.sparta.product.domain.repository.cassandra")
@EnableCassandraAuditing
public class RepositoryConfig {}

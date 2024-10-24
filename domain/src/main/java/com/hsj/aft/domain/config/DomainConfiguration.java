package com.hsj.aft.domain.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "com.hsj.aft.domain.entity")
public class DomainConfiguration {
}

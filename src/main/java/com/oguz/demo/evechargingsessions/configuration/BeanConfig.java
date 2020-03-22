package com.oguz.demo.evechargingsessions.configuration;

import com.oguz.demo.evechargingsessions.service.ringbuffer.RingBufferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public RingBufferService ringBufferService() {
        return RingBufferService.instance();
    }
}

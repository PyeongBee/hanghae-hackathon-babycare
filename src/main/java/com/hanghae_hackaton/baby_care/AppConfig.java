package com.hanghae_hackaton.baby_care;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ComponentScan
public class AppConfig {
    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    public OpenAiService openAiService () {
        return new OpenAiService(apiKey, Duration.ofSeconds(60));
    }
}

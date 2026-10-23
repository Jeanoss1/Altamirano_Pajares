package com.altamirano_pajares.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    // Entidad <-> DTO. Los nombres de campo coinciden, asi que basta con el mapeo por convencion.
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // evita cruces startDate/endDate
        return mapper;
    }
}

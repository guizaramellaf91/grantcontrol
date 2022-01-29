package br.com.zaratech.bean;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DtoMaps {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
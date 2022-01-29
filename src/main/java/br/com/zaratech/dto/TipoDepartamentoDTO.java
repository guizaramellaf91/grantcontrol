package br.com.zaratech.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoDepartamentoDTO implements Serializable {

    private long departamentoId;
    private String nomeDepartamento;
}

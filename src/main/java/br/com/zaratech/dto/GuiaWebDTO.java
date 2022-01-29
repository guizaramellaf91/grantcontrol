package br.com.zaratech.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuiaWebDTO implements Serializable {

    private Long guiaWebId;
    private String titulo;
    private String descricao;
    private Date dataCadastro;
}
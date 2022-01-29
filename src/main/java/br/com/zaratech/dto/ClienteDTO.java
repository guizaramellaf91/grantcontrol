package br.com.zaratech.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.zaratech.model.ClienteProduto;
import br.com.zaratech.model.ControleVersaoCliente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTO implements Serializable {

    private Long clienteId;
    private String nomeCliente;
    private boolean ativo;
    private Date dataCadastro;
    private Date dataAlteracao;
    private List<ClienteProduto> clienteProduto;
    private List<ControleVersaoCliente> controleVersaoCliente;
}
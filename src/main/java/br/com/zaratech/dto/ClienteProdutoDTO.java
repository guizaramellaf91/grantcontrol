package br.com.zaratech.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.zaratech.model.Cliente;
import br.com.zaratech.model.TipoAmbiente;
import br.com.zaratech.model.TipoProduto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClienteProdutoDTO implements Serializable {

    private Long clienteProdutoId;
    private String descricao;
    private String ip;
    private String usuario;
    private String senha;
    private String usuarioAux;
    private String senhaAux;
    private Integer porta;
    private Date dataCadastro;
    private Date dataAlteracao;
    private String usuarioAlteracao;
    private String urlAplicacao;
    private String usuarioAplicacao;
    private String senhaAplicacao;
    private boolean possuiChave;
    private String nomeChave;
    private TipoProduto tipoProduto;
    private TipoAmbiente tipoAmbiente;
    private Cliente cliente;
}
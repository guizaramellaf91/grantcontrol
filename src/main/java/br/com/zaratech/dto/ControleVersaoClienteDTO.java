package br.com.zaratech.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.zaratech.model.Cliente;
import br.com.zaratech.model.TipoProduto;
import br.com.zaratech.model.TipoSubProduto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControleVersaoClienteDTO implements Serializable {

    private Long controleVersaoClienteId;
    private Date dataAlteracaoVersaoHomologacao;
    private String versaoHomologacao;
    private Date dataAlteracaoVersaoPreProducao;
    private String versaoPreProducao;
    private Date dataAlteracaoVersaoProducao;
    private String versaoProducao;
    private Cliente cliente;
    private TipoSubProduto tipoSubProduto;
    private TipoProduto tipoProduto;
    private Date dataCadastro;
    private Date dataAlteracao;
}
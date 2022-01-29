package br.com.zaratech.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("SpellCheckingInspection")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "cliente_produto", columnNames = "tipo_sub_produto_id")})
public class ControleVersaoCliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long controleVersaoClienteId;
    @Column(nullable = true)
    private Date dataAlteracaoVersaoHomologacao;
    @Column(nullable = true)
    private String versaoHomologacao;
    @Column(nullable = true)
    private Date dataAlteracaoVersaoPreProducao;
    @Column(nullable = true)
    private String versaoPreProducao;
    @Column(nullable = true)
    private Date dataAlteracaoVersaoProducao;
    @Column(nullable = true)
    private String versaoProducao;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "tipo_sub_produto_id")
    private TipoSubProduto tipoSubProduto;
    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "tipo_produto_id")
    private TipoProduto tipoProduto;
    @Column(nullable = false)
    private Date dataCadastro;
    @Column(nullable = true)
    private Date dataAlteracao;
}
package br.com.zaratech.model;

import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("SpellCheckingInspection")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cliente_produto")
public class ClienteProduto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long clienteProdutoId;
    @Column(nullable = false)
    private String descricao;
    @Column(nullable = false)
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
    @OneToOne
    @JoinColumn(name = "tipo_produto_id")
    private TipoProduto tipoProduto;
    @OneToOne
    @JoinColumn(name = "tipo_ambiente_id")
    private TipoAmbiente tipoAmbiente;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
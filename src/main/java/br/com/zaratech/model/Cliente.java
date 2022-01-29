package br.com.zaratech.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clienteId;
    @Column(nullable = false)
    private String nomeCliente;
    @Column(nullable = false)
    private boolean ativo;
    @Column(nullable = false)
    private Date dataCadastro;
    private Date dataAlteracao;
    @JsonBackReference
    @OneToMany(mappedBy = "cliente", targetEntity = ClienteProduto.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ClienteProduto> clienteProduto;
    @JsonBackReference
    @OneToMany(mappedBy = "cliente", targetEntity = ControleVersaoCliente.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ControleVersaoCliente> controleVersaoCliente;
}
package br.com.zaratech.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("SpellCheckingInspection")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "parametros_sistema", uniqueConstraints = { @UniqueConstraint(columnNames = "chave") })
public class ParametrosSistema implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long parametroId;
    @Column(nullable = false)
    private String chave;
    @Column(nullable = false)
    private String valor;
    @Column(nullable = false)
    private String descricao;
}
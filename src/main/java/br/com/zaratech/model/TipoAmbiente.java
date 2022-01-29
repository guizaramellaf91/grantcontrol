package br.com.zaratech.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.*;

@SuppressWarnings("SpellCheckingInspection")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "tipo_ambiente", columnNames = "nomeAmbiente")})
public class TipoAmbiente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tipoAmbienteId;
    @Column(nullable = false)
    private String nomeAmbiente;
}
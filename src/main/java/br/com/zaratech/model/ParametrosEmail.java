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

/*SpellCheckingInspection*/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "parametros_email", uniqueConstraints = { @UniqueConstraint(columnNames = "emailFrom") })
public class ParametrosEmail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parametroEmailId;
    @Column(nullable = false)
    private String emailFrom;
    @Column(nullable = false)
    private String emailFromName;
    @Column(nullable = false)
    private String emailSmtpUsername;
    @Column(nullable = false)
    private String emailSmtpPassword;
    @Column(nullable = false)
    private String emailSmtpHost;
    @Column(nullable = false)
    private int emailPort;
    @Column(nullable = false)
    private String emailSubject;
    @Column(nullable = false)
    private String configSet;
    @Column(nullable = false)
    private String urlEnvio;
}
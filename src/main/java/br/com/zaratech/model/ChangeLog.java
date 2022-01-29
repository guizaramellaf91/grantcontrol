package br.com.zaratech.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "changelog")
public class ChangeLog implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long changeLogId;
	@Column(nullable = false)
	private Date dataAlteracao;
	@Column(nullable = false)
	private String descricao;
}
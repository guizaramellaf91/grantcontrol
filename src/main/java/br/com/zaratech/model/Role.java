package br.com.zaratech.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("SpellCheckingInspection")
@Entity
public class Role implements GrantedAuthority{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Size(max = 30)
	private String nomeRole;
			
	public Role() {
	}
	
	public Role(String nome) {
		this.nomeRole = nome;
	}

	@Override
	public String getAuthority() {
		return this.nomeRole;
	}

	public String getNomeRole() {
		return nomeRole;
	}

	public void setNomeRole(String nomeRole) {
		this.nomeRole = nomeRole;
	}
}
package br.com.zaratech.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.zaratech.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO implements Serializable {

    private long usuarioId;
    private String nome;
    private String login;
    private String senha;
    private String email;
    private boolean ativo;
    private int nivel;
    private long acessos;
    private Date ultimoAcesso;
    private String departamento;
    private Date dataAlteracao;
    private List<Role> roles;
}
package br.com.zaratech.util;

import br.com.zaratech.model.Usuario;

@SuppressWarnings("SpellCheckingInspection")
public class ValidaPermissoesUsuario {

    private ValidaPermissoesUsuario() {

    }

    public static boolean usuarioAdmin(Usuario usuario) {
        return usuario.getNivel() == 1;
    }
}
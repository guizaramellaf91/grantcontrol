package br.com.zaratech.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.dto.TipoDepartamentoDTO;
import br.com.zaratech.dto.UsuarioDTO;
import br.com.zaratech.service.UsuarioService;

@Controller
public class UsuarioController {

    UsuarioService usuarioService;

    @Autowired
    void handle(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping(value = "/usuarios")
    public ModelAndView usuarios(@RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("page") Optional<Integer> page) {

        return usuarioService.usuarios(pageSize, page);
    }

    @PostMapping(value = "/buscarUsuario")
    public ModelAndView buscarUsuario(String nomeUsuario, @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("page") Optional<Integer> page) {

        return usuarioService.buscaUsuario(nomeUsuario, pageSize, page);
    }

    @GetMapping(value = "/cadastrarUsuario")
    public ModelAndView cadastrar() {

        return usuarioService.cadastrar();
    }

    @PostMapping(value = "cadastrarUsuario")
    public ModelAndView cadastrar(UsuarioDTO usuarioDTO, TipoDepartamentoDTO tipoDepartamentoDTO) {

        return usuarioService.cadastrar(usuarioDTO, tipoDepartamentoDTO);
    }

    @GetMapping(value = "detalhesUsuario")
    public ModelAndView detalhesUsuario(long usuarioId) {

        return usuarioService.detalhesUsuario(usuarioId);
    }

    @PostMapping(value = "alterarUsuario")
    public ModelAndView alterarUsuario(Long usuarioId, UsuarioDTO usuarioDTO, TipoDepartamentoDTO tipoDepartamentoDTO) {

        return usuarioService.alterarUsuario(usuarioId, usuarioDTO, tipoDepartamentoDTO);
    }

    @GetMapping(value = "/recuperarAcesso")
    public ModelAndView recuperarAcesso(String id) {

        return usuarioService.recuperarAcesso(id);
    }

    @PostMapping(value = "/recuperarAcesso")
    public ModelAndView recuperarAcessoAgora(String id, String senha) {

        return usuarioService.recuperarAcesso(id, senha);
    }

    @GetMapping(value = "/esqueciMinhaSenha")
    public ModelAndView esqueciMinhaSenha() {

        return usuarioService.esqueciMinhaSenha();
    }

    @PostMapping(value = "/esqueciMinhaSenha")
    public ModelAndView esqueciMinhaSenhaEnviar(String email) {

        return usuarioService.esqueciMinhaSenha(email);
    }
}
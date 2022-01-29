package br.com.zaratech.service;

import java.util.Optional;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.dto.TipoDepartamentoDTO;
import br.com.zaratech.dto.UsuarioDTO;

@SuppressWarnings("SpellCheckingInspection")
/**
 * Impl UsuarioServiceImpl
 */
public interface UsuarioService {

        ModelAndView usuarios(@RequestParam("pageSize") Optional<Integer> pageSize,
                        @RequestParam("page") Optional<Integer> page);

        ModelAndView buscaUsuario(String nomeUsuario, @RequestParam("pageSize") Optional<Integer> pageSize,
                        @RequestParam("page") Optional<Integer> page);

        ModelAndView cadastrar();

        ModelAndView cadastrar(UsuarioDTO usuarioDTO, TipoDepartamentoDTO tipoDepartamentoDTO);

        ModelAndView detalhesUsuario(long usuarioId);

        ModelAndView alterarUsuario(Long usuarioId, UsuarioDTO usuarioDTO, TipoDepartamentoDTO tipoDepartamentoDTO);

        ModelAndView recuperarAcesso(String id);

        ModelAndView recuperarAcesso(String id, String senha);

        ModelAndView esqueciMinhaSenha();

        ModelAndView esqueciMinhaSenha(String email);
}

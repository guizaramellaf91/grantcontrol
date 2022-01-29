package br.com.zaratech.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.dto.ClienteProdutoDTO;

@SuppressWarnings("SpellCheckingInspection")
public interface ClienteProdutoService {

    void downloadPrivateKey(HttpServletRequest req, HttpServletResponse resp, Long clienteProdutoId) throws IOException;

    void exportarDadosProduto(HttpServletRequest req, HttpServletResponse resp, Long clienteProdutoId) throws IOException;

    ModelAndView detalhesClienteProduto(long clienteProdutoId);

    ModelAndView cadastrar(long clienteId);

    ModelAndView cadastrarClienteProduto(ClienteProdutoDTO clienteProdutoDTO,
                                         long clienteId, long tipoAmbienteId, long tipoProdutoId, boolean possuiChave, String nomeChave);

    ModelAndView cadastrarDetalhesClienteProduto(long clienteProdutoId);

    ModelAndView alterarClienteProduto(long clienteProdutoId, ClienteProdutoDTO clienteProdutoDTO,
                                       long tipoAmbienteId, long tipoProdutoId, boolean possuiChave, String nomeChave);

    ModelAndView deletarClienteProduto(long clienteProdutoId);
}
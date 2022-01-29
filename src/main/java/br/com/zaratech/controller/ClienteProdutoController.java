package br.com.zaratech.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.dto.ClienteProdutoDTO;
import br.com.zaratech.service.ClienteProdutoService;

@Controller
public class ClienteProdutoController {

    ClienteProdutoService clienteProdutoService;

    @Autowired
    void handle(ClienteProdutoService clienteProdutoService) {
        this.clienteProdutoService = clienteProdutoService;
    }

    @GetMapping(value = "/downloadPrivateKey/{clienteProdutoId}")
    public void getFile(HttpServletRequest req, HttpServletResponse res, @PathVariable(value = "clienteProdutoId", required = false) Long clienteProdutoId) throws IOException {

        clienteProdutoService.downloadPrivateKey(req, res, clienteProdutoId);
    }

    @GetMapping(value = "/exportarDadosProduto/{clienteProdutoId}")
    public void getExportFile(HttpServletRequest req, HttpServletResponse res, @PathVariable(value = "clienteProdutoId", required = false) Long clienteProdutoId) throws IOException {

        clienteProdutoService.exportarDadosProduto(req, res, clienteProdutoId);
    }

    @GetMapping(value = "detalhesClienteProduto/{clienteProdutoId}")
    public ModelAndView detalhesClienteProduto(@PathVariable(value = "clienteProdutoId", required = false) long clienteProdutoId) {

        return clienteProdutoService.detalhesClienteProduto(clienteProdutoId);
    }

    @GetMapping(value = "cadastrarDetalhesClienteProduto/{clienteProdutoId}")
    public ModelAndView cadastrarDetalhesClienteProduto(@PathVariable(value = "clienteProdutoId", required = false) long clienteProdutoId) {

        return clienteProdutoService.cadastrarDetalhesClienteProduto(clienteProdutoId);
    }

    @PostMapping(value = "detalhesClienteProduto/{clienteProdutoId}")
    public ModelAndView alterarClienteProduto(@PathVariable(value = "clienteProdutoId", required = false) long clienteProdutoId, ClienteProdutoDTO clienteProdutoDTO,
                                              long tipoAmbienteId, long tipoProdutoId, boolean possuiChave, String nomeChave) {

        return clienteProdutoService.alterarClienteProduto(clienteProdutoId, clienteProdutoDTO, tipoAmbienteId, tipoProdutoId, possuiChave, nomeChave);
    }

    @GetMapping(value = "cadastrarClienteProduto/{clienteId}")
    public ModelAndView cadastrar(@PathVariable(value = "clienteId", required = false) long clienteId) {

        return clienteProdutoService.cadastrar(clienteId);
    }

    @PostMapping(value = "cadastrarClienteProduto/{clienteId}")
    public ModelAndView cadastrarClienteProduto(ClienteProdutoDTO clienteProdutoDTO,
                                                @PathVariable(value = "clienteId", required = false) long clienteId, long tipoAmbienteId, long tipoProdutoId, boolean possuiChave, String nomeChave) {

        return clienteProdutoService.cadastrarClienteProduto(clienteProdutoDTO, clienteId, tipoAmbienteId, tipoProdutoId, possuiChave, nomeChave);
    }

    @GetMapping(value = "deletarClienteProduto/{clienteProdutoId}")
    public ModelAndView deletarClienteProduto(@PathVariable(value = "clienteProdutoId", required = false) long clienteProdutoId) {

        return clienteProdutoService.deletarClienteProduto(clienteProdutoId);
    }
}
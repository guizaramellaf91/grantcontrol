package br.com.zaratech.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.dto.ClienteDTO;
import br.com.zaratech.service.ClienteService;

@Controller
public class ClienteController {

    private ClienteService clienteService;

    @Autowired
    void handle(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping(value = "/clientes")
    public ModelAndView clientes(@RequestParam("pageSize") Optional<Integer> pageSize,
                                 @RequestParam("page") Optional<Integer> page) {

        return clienteService.clientes(pageSize, page);
    }

    @PostMapping(value = "/buscarCliente")
    public ModelAndView buscarClientes(String nomeCliente, @RequestParam("pageSize") Optional<Integer> pageSize,
                                       @RequestParam("page") Optional<Integer> page) {

        return clienteService.buscarClientes(nomeCliente, pageSize, page);
    }

    @GetMapping(value = "/cadastrarCliente")
    public ModelAndView cadastrar() {

        return clienteService.cadastrarCliente();
    }

    @PostMapping(value = "/cadastrarCliente")
    public String cadastrar(Model model, ClienteDTO cliente) {

        return clienteService.cadastrarCliente(model, cliente);
    }

    @GetMapping(value = "detalhesCliente/{clienteId}")
    public ModelAndView detalhesCliente(@PathVariable(value = "clienteId", required = false) long clienteId) {

        return clienteService.detalhesCliente(clienteId);
    }

    @PostMapping(value = "detalhesCliente/{clienteId}")
    public String alterar(@PathVariable(value = "clienteId", required = false) long clienteId, ClienteDTO cliente) {

        return clienteService.detalhesCliente(clienteId, cliente);
    }

    @GetMapping(value = "deletarCliente/{clienteId}")
    public String deletarCliente(@PathVariable(value = "clienteId", required = false) long clienteId) {

        return clienteService.deletarCliente(clienteId);
    }
}
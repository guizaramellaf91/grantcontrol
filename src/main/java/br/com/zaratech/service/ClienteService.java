package br.com.zaratech.service;

import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.dto.ClienteDTO;

@SuppressWarnings("SpellCheckingInspection")
public interface ClienteService {

    ModelAndView clientes(@RequestParam("pageSize") Optional<Integer> pageSize,
                          @RequestParam("page") Optional<Integer> page);
    ModelAndView buscarClientes(@RequestParam("buscaCliente") String buscaCliente,
                                @RequestParam("pageSize") Optional<Integer> pageSize,
                                @RequestParam("page") Optional<Integer> page);
    ModelAndView cadastrarCliente();
    String cadastrarCliente(Model model, ClienteDTO cliente);
    ModelAndView detalhesCliente(long clienteId);
    String detalhesCliente(long clienteId, ClienteDTO cliente);
    String deletarCliente(long clienteId);
}
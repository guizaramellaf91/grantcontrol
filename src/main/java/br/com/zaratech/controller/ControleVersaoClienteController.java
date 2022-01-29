package br.com.zaratech.controller;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.dto.ControleVersaoClienteDTO;
import br.com.zaratech.model.Cliente;
import br.com.zaratech.model.TipoAmbiente;
import br.com.zaratech.model.TipoSubProduto;
import br.com.zaratech.service.ControleVersaoClienteService;

@Controller
public class ControleVersaoClienteController {

    private ControleVersaoClienteService controleVersaoClienteService;

    @Autowired
    private void handle(ControleVersaoClienteService controleVersaoClienteService) {
        this.controleVersaoClienteService = controleVersaoClienteService;
    }

    @GetMapping(value = "/controleVersao")
    public ModelAndView controleVersao() {

        return controleVersaoClienteService.controleVersao();
    }

    @GetMapping(value = "/controleVersaoPacote")
    public ModelAndView controleVersaoPacote() {

        return controleVersaoClienteService.controleVersaoPacote();
    }

    @PostMapping(value = "/controleVersaoPacote")
    public ModelAndView controleVersaoPacote(String clienteChip, String ambienteChip, Long tipoProdutoId, String subProdutoChip, String versaoHomologacao, String versaoPreProducao, String versaoProducao) {

        return controleVersaoClienteService.controleVersaoPacote(clienteChip, ambienteChip, tipoProdutoId, subProdutoChip, versaoHomologacao, versaoPreProducao, versaoProducao);
    }

    @JsonIgnore
    @GetMapping(value = "/controleVersaoCliente")
    public ModelAndView controleVersaoCliente(long clienteId, @RequestParam("pageSize") Optional<Integer> pageSize,
                                              @RequestParam("page") Optional<Integer> page) {

        return controleVersaoClienteService.controleVersaoCliente(clienteId, pageSize, page);
    }

    @ResponseBody
    @GetMapping(value = "buscarClientesControleVersao")
    public List<Cliente> buscarClientesControleVersao() {

        return controleVersaoClienteService.buscarClientesControleVersao();
    }

    @ResponseBody
    @GetMapping(value = "buscarTipoAmbiente")
    public List<TipoAmbiente> buscarTipoAmbiente() {

        return controleVersaoClienteService.buscarTipoAmbiente();
    }

    @ResponseBody
    @GetMapping(value = "buscarSubProduto")
    public List<TipoSubProduto> buscarSubProduto(Long tipoProdutoId) {

        return controleVersaoClienteService.buscarSubProduto(tipoProdutoId);
    }

    @GetMapping(value = "/cadastrarControleVersaoCliente")
    public ModelAndView cadastrarControleVersaoCliente() {

        return controleVersaoClienteService.cadastrarControleVersaoCliente();
    }

    @PostMapping(value = "/cadastrarControleVersaoCliente")
    public ModelAndView cadastrarControleVersaoCliente(long clienteId, ControleVersaoClienteDTO controleVersaoClienteDTO, long tipoProdutoId, long tipoSubProdutoId) {

        return controleVersaoClienteService.cadastrarControleVersaoCliente(clienteId, controleVersaoClienteDTO, tipoProdutoId, tipoSubProdutoId);
    }

    @PostMapping(value = "/alterarControleVersaoCliente")
    public ModelAndView alterarControleVersaoCliente(long clienteId, ControleVersaoClienteDTO controleVersaoClienteDTO) {

        return controleVersaoClienteService.alterarControleVersaoCliente(clienteId, controleVersaoClienteDTO);
    }

    @PostMapping(value = "/deletarControleVersaoCliente")
    public ModelAndView deletarControleVersaoCliente(long clienteId, long controleVersaoClienteId) {

        return controleVersaoClienteService.deletarControleVersaoCliente(clienteId, controleVersaoClienteId);
    }
}

package br.com.zaratech.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.dto.ControleVersaoClienteDTO;
import br.com.zaratech.model.Cliente;
import br.com.zaratech.model.TipoAmbiente;
import br.com.zaratech.model.TipoSubProduto;

@SuppressWarnings("SpellCheckingInspection")
public interface ControleVersaoClienteService {

    ModelAndView controleVersao();

    ModelAndView controleVersaoPacote();

    ModelAndView controleVersaoPacote(String clienteChip, String ambienteChip, Long tipoProdutoId, String subProdutoChip, String versaoHomologacao, String versaoPreProducao, String versaoProducao);

    ModelAndView controleVersaoCliente(long clienteId, @RequestParam("pageSize") Optional<Integer> pageSize,
                                       @RequestParam("page") Optional<Integer> page);

    List<Cliente> buscarClientesControleVersao();

    List<TipoAmbiente> buscarTipoAmbiente();

    List<TipoSubProduto> buscarSubProduto(Long tipoProdutoId);

    ModelAndView cadastrarControleVersaoCliente();

    ModelAndView cadastrarControleVersaoCliente(long clienteId, ControleVersaoClienteDTO controleVersaoClienteDTO, long tipoProdutoId, long tipoSubProdutoId);

    ModelAndView alterarControleVersaoCliente(long clienteId, ControleVersaoClienteDTO controleVersaoClienteDTO);

    ModelAndView deletarControleVersaoCliente(long clienteId, long controleVersaoClienteId);

}
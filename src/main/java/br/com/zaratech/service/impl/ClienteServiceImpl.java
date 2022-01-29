package br.com.zaratech.service.impl;

import static br.com.zaratech.util.GrantControlConstants.ATIVO_STRING_UM;
import static br.com.zaratech.util.GrantControlConstants.GET_LISTAR_CLIENTES;
import static br.com.zaratech.util.GrantControlConstants.GOOGLE_CHARTS_CLIENTS;
import static br.com.zaratech.util.GrantControlConstants.MODEL_VIEW_CADASTRAR_CLIENTE;
import static br.com.zaratech.util.GrantControlConstants.MODEL_VIEW_REDIRECT_CLIENTES;
import static br.com.zaratech.util.GrantControlConstants.PAGE_SIZE_LIST;
import static br.com.zaratech.util.GrantControlConstants.USUARIO_LOGADO;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.bean.DtoMaps;
import br.com.zaratech.bean.UsuarioBean;
import br.com.zaratech.dto.ClienteDTO;
import br.com.zaratech.model.Cliente;
import br.com.zaratech.model.ClienteProduto;
import br.com.zaratech.model.ControleVersaoCliente;
import br.com.zaratech.model.PagerModel;
import br.com.zaratech.model.ParametrosSistema;
import br.com.zaratech.repository.ClienteProdutoRepository;
import br.com.zaratech.repository.ClienteRepository;
import br.com.zaratech.repository.ControleVersaoClienteRepository;
import br.com.zaratech.repository.ParametrosRepository;
import br.com.zaratech.service.ClienteService;
import lombok.extern.slf4j.Slf4j;

/*SpellCheckingInspection*/
@Slf4j
@Service
public class ClienteServiceImpl implements ClienteService {

    private UsuarioBean usuarioBean;
    private ClienteRepository clienteRepository;
    private ControleVersaoClienteRepository controleVersaoClienteRepository;
    private ClienteProdutoRepository clienteProdutoRepository;
    private ParametrosRepository parametrosRepository;

    @Autowired
    private DtoMaps dtoMaps;

    @Autowired
    private void handleImpl(UsuarioBean usuarioBean, ClienteRepository clienteRepository,
            ControleVersaoClienteRepository controleVersaoClienteRepository,
            ClienteProdutoRepository clienteProdutoRepository,
            ParametrosRepository parametrosRepository) {
        this.usuarioBean = usuarioBean;
        this.clienteRepository = clienteRepository;
        this.controleVersaoClienteRepository = controleVersaoClienteRepository;
        this.clienteProdutoRepository = clienteProdutoRepository;
        this.parametrosRepository = parametrosRepository;

    }

    private ModelAndView mv = new ModelAndView("cliente/clientes");

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int[] PAGE_SIZES = { 5, 10 };

    public ModelAndView clientes(@RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("page") Optional<Integer> page) {

        try {

            int evalPageSize;
            int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

            final ParametrosSistema pageSizeList = parametrosRepository.findByChave(PAGE_SIZE_LIST);
            if (pageSizeList != null && pageSizeList.getChave() != null && pageSizeList.getValor() != null) {
                evalPageSize = pageSize.orElse(Integer.valueOf(pageSizeList.getValor()));
            } else {
                evalPageSize = 5;
            }

            final ParametrosSistema googleChartsClients = parametrosRepository.findByChave(GOOGLE_CHARTS_CLIENTS);
            mv.addObject("googleChartsClients",
                    !Objects.isNull(googleChartsClients) && googleChartsClients.getValor().equals(ATIVO_STRING_UM));

            usuarioBean.setPageSize(evalPageSize);
            usuarioBean.setPage((page.orElse(0) < 1) ? INITIAL_PAGE : page.get());

            Page<Cliente> clientes = clienteRepository
                    .findAll(PageRequest.of(evalPage, evalPageSize, Sort.by("nomeCliente").ascending()));
            PagerModel pager = new PagerModel(clientes.getTotalPages(), clientes.getNumber(), BUTTONS_TO_SHOW);
            mv.addObject("selectedPageSize", evalPageSize);
            mv.addObject("pageSizes", PAGE_SIZES);
            mv.addObject("pager", pager);
            mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
            mv.addObject(GET_LISTAR_CLIENTES, clientes);
            mv.addObject("quantidadeClientes", clienteRepository.totalClientes());
            mv.addObject("quantidadeProdutos", clienteProdutoRepository.totalClienteProdutoTodos());
            mv.addObject("hrefCliente", "/grantcontrol/detalhesCliente/");
            return mv;
        } catch (Exception e) {
            log.error(e.getMessage());
            mv = new ModelAndView("index");
            return mv;
        }
    }

    public ModelAndView buscarClientes(@RequestParam("buscaCliente") String buscaCliente,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("page") Optional<Integer> page) {

        mv = new ModelAndView("cliente/clientes");

        try {

            int evalPageSize;
            int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

            final ParametrosSistema pageSizeList = parametrosRepository.findByChave(PAGE_SIZE_LIST);
            if (pageSizeList != null && pageSizeList.getChave() != null && pageSizeList.getValor() != null) {
                evalPageSize = pageSize.orElse(Integer.valueOf(pageSizeList.getValor()));
            } else {
                evalPageSize = 5;
            }

            final ParametrosSistema googleChartsClients = parametrosRepository.findByChave(GOOGLE_CHARTS_CLIENTS);
            mv.addObject("dashboardTela",
                    !Objects.isNull(googleChartsClients) && googleChartsClients.getValor().equals(ATIVO_STRING_UM));

            usuarioBean.setPageSize(evalPageSize);
            usuarioBean.setPage((page.orElse(0) < 1) ? INITIAL_PAGE : page.get());

            if (!buscaCliente.isEmpty()) {

                List<Cliente> clientesNomeCliente = clienteRepository.findByNomeClienteContaining(buscaCliente);
                List<Cliente> clientesIPServidor = clienteRepository.buscarPorIPServidor(buscaCliente);

                if (clientesNomeCliente.isEmpty() && clientesIPServidor.isEmpty()) {
                    Page<Cliente> cliente = clienteRepository
                            .findAll(PageRequest.of(evalPage, evalPageSize, Sort.by("nomeCliente").ascending()));
                    PagerModel pager = new PagerModel(cliente.getTotalPages(), cliente.getNumber(), BUTTONS_TO_SHOW);
                    mv.addObject("selectedPageSize", evalPageSize);
                    mv.addObject("pageSizes", PAGE_SIZES);
                    mv.addObject("pager", pager);
                    mv.addObject(GET_LISTAR_CLIENTES, cliente);
                } else if (!clientesNomeCliente.isEmpty()) {
                    mv.addObject("searchCliente", true);
                    mv.addObject(GET_LISTAR_CLIENTES, clientesNomeCliente);
                } else if (!clientesIPServidor.isEmpty()) {
                    mv.addObject("searchCliente", true);
                    mv.addObject(GET_LISTAR_CLIENTES, clientesIPServidor);
                }
                mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
                mv.addObject("quantidadeClientes", clienteRepository.totalClientes());
                mv.addObject("quantidadeProdutos", clienteProdutoRepository.totalClienteProdutoTodos());
            } else {
                mv = new ModelAndView(MODEL_VIEW_REDIRECT_CLIENTES);
            }
            return mv;
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(e.getMessage());
            mv = new ModelAndView(MODEL_VIEW_REDIRECT_CLIENTES);
            return mv;
        }
    }

    public ModelAndView cadastrarCliente() {
        if (usuarioBean.getUsuario().getNivel() == 1) {
            mv = new ModelAndView("cliente/cadastrarCliente");
            mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
        } else {
            mv = new ModelAndView("redirect:/index");
        }
        return mv;
    }

    public String cadastrarCliente(Model model, ClienteDTO clienteDTO) {

        try {
            if (clienteRepository.findByNomeCliente(clienteDTO.getNomeCliente()) != null) {
                log.warn("O cliente informado já existe!");
                return MODEL_VIEW_CADASTRAR_CLIENTE;
            }
            model.addAttribute("ativo", clienteDTO.isAtivo());
            clienteDTO.setDataCadastro(new Date());
            clienteDTO.setDataAlteracao(new Date());
            Cliente cliente = dtoMaps.modelMapper().map(clienteDTO, Cliente.class);
            clienteRepository.save(cliente);
            log.info("\nUsuario " + cliente.getClienteId() + " - " + cliente.getNomeCliente() + " cadastrado!");
            return MODEL_VIEW_REDIRECT_CLIENTES;
        } catch (Exception e) {
            log.error(e.getMessage());
            return MODEL_VIEW_CADASTRAR_CLIENTE;
        }
    }

    public ModelAndView detalhesCliente(long clienteId) {

        mv = new ModelAndView("cliente/detalhesCliente");

        try {

            final ParametrosSistema pageSizeList = parametrosRepository.findByChave(PAGE_SIZE_LIST);
            Cliente cliente = clienteRepository.findByClienteId(clienteId);

            List<ClienteProduto> clienteProdutoLinuxServer = clienteProdutoRepository
                    .buscarClienteProdutoTipoProdutoIdclienteId(1, clienteId);
            List<ClienteProduto> clienteProdutoWindowsServer = clienteProdutoRepository
                    .buscarClienteProdutoTipoProdutoIdclienteId(2, clienteId);
            List<ClienteProduto> clienteProdutoDatabaseOracle = clienteProdutoRepository
                    .buscarClienteProdutoTipoProdutoIdclienteId(3, clienteId);
            List<ClienteProduto> clienteProdutoDatabaseMysql = clienteProdutoRepository
                    .buscarClienteProdutoTipoProdutoIdclienteId(4, clienteId);
            List<ClienteProduto> clienteProdutoOutros = clienteProdutoRepository
                    .buscarClienteProdutoTipoProdutoIdclienteIdWithout(new long[] { 1, 2, 15, 8, 11 }, clienteId);

            List<ControleVersaoCliente> lsControleVersaoCliente = controleVersaoClienteRepository
                    .findByClienteId(cliente.getClienteId());

            if (usuarioBean.getPageSize() == 0 && usuarioBean.getPage() == 0) {

                if (pageSizeList != null && pageSizeList.getChave() != null && pageSizeList.getValor() != null) {
                    usuarioBean.setPageSize(Integer.valueOf(pageSizeList.getValor()));
                } else {
                    usuarioBean.setPageSize(5);
                }
                usuarioBean.setPage(1);
            }

            mv.addObject("isControleVersaoCliente", !lsControleVersaoCliente.isEmpty());
            mv.addObject("pageSizeNavi", usuarioBean.getPageSize());
            mv.addObject("pageNavi", usuarioBean.getPage());
            mv.addObject("quantidadeProduto", clienteProdutoRepository.totalClienteProduto(clienteId));
            mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
            mv.addObject("cliente", cliente);
            mv.addObject("clienteProduto", cliente.getClienteProduto());
            mv.addObject("clienteProdutoLinuxServer", clienteProdutoLinuxServer);
            mv.addObject("clienteProdutoWindowsServer", clienteProdutoWindowsServer);
            mv.addObject("clienteProdutoDatabaseOracle", clienteProdutoDatabaseOracle);
            mv.addObject("clienteProdutoDatabaseMysql", clienteProdutoDatabaseMysql);
            mv.addObject("clienteProdutoOutros", clienteProdutoOutros);
            return mv;
        } catch (Exception e) {
            return new ModelAndView("clientes");
        }
    }

    public String detalhesCliente(long clienteId, ClienteDTO cliente) {
        try {
            Cliente c = clienteRepository.findByClienteId(clienteId);
            Cliente cexists = clienteRepository.findByNomeCliente(cliente.getNomeCliente());
            if (cexists != null && !c.equals(cexists)) {
                log.warn("O cliente " + cliente.getNomeCliente() + " informado já existe!");
                return "redirect:/detalhesCliente/" + clienteId;
            }
            c.setNomeCliente(cliente.getNomeCliente());
            c.setAtivo(cliente.isAtivo());
            c.setDataAlteracao(new Date());
            clienteRepository.save(c);
            log.info("Cliente " + cliente.getClienteId() + " - " + cliente.getNomeCliente() + " alterado!");
            return "redirect:/detalhesCliente/" + clienteId;
        } catch (Exception e) {
            log.info("alterar: " + e.getMessage());
            return MODEL_VIEW_CADASTRAR_CLIENTE;
        }
    }

    public String deletarCliente(long clienteId) {
        Cliente cliente = clienteRepository.findByClienteId(clienteId);
        clienteRepository.delete(cliente);
        log.info("Cliente " + cliente.getNomeCliente() + " excluido com sucesso!");
        return MODEL_VIEW_REDIRECT_CLIENTES;
    }
}
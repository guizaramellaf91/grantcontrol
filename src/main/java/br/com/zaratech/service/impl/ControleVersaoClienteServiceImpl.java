package br.com.zaratech.service.impl;

import static br.com.zaratech.util.GrantControlConstants.GET_CLIENTE_ID;
import static br.com.zaratech.util.GrantControlConstants.MODEL_VIEW_REDIRECT_CONTROLE_VERSAO_CLIENTE;
import static br.com.zaratech.util.GrantControlConstants.PAGE_SIZE_LIST;
import static br.com.zaratech.util.GrantControlConstants.USUARIO_LOGADO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.bean.DtoMaps;
import br.com.zaratech.bean.UsuarioBean;
import br.com.zaratech.dto.ControleVersaoClienteDTO;
import br.com.zaratech.model.Cliente;
import br.com.zaratech.model.ControleVersaoCliente;
import br.com.zaratech.model.PagerModel;
import br.com.zaratech.model.ParametrosSistema;
import br.com.zaratech.model.TipoAmbiente;
import br.com.zaratech.model.TipoProduto;
import br.com.zaratech.model.TipoSubProduto;
import br.com.zaratech.repository.ClienteRepository;
import br.com.zaratech.repository.ControleVersaoClienteRepository;
import br.com.zaratech.repository.ParametrosRepository;
import br.com.zaratech.repository.TipoAmbienteRepository;
import br.com.zaratech.repository.TipoProdutoRepository;
import br.com.zaratech.repository.TipoSubProdutoRepository;
import br.com.zaratech.service.ControleVersaoClienteService;
import br.com.zaratech.util.SendMail;
import br.com.zaratech.util.ValidaPermissoesUsuario;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@Service
public class ControleVersaoClienteServiceImpl implements ControleVersaoClienteService {

    private UsuarioBean usuarioBean;
    private SendMail sendMail;
    private ClienteRepository clienteRepository;
    private ControleVersaoClienteRepository controleVersaoClienteRepository;
    private TipoProdutoRepository tipoProdutoRepository;
    private TipoAmbienteRepository tipoAmbienteRepository;
    private TipoSubProdutoRepository tipoSubProdutoRepository;
    private ParametrosRepository parametrosRepository;

    @Autowired
    private DtoMaps dtoMaps;

    @Autowired
    private void handleImpl(UsuarioBean usuarioBean, SendMail sendMail, ClienteRepository clienteRepository,
            ControleVersaoClienteRepository controleVersaoClienteRepository,
            TipoProdutoRepository tipoProdutoRepository, TipoAmbienteRepository tipoAmbienteRepository,
            TipoSubProdutoRepository tipoSubProdutoRepository, ParametrosRepository parametrosRepository) {

        this.usuarioBean = usuarioBean;
        this.sendMail = sendMail;
        this.clienteRepository = clienteRepository;
        this.controleVersaoClienteRepository = controleVersaoClienteRepository;
        this.tipoProdutoRepository = tipoProdutoRepository;
        this.tipoAmbienteRepository = tipoAmbienteRepository;
        this.tipoSubProdutoRepository = tipoSubProdutoRepository;
        this.parametrosRepository = parametrosRepository;
    }

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int[] PAGE_SIZES = { 5, 10 };

    ModelAndView mv = new ModelAndView(MODEL_VIEW_REDIRECT_CONTROLE_VERSAO_CLIENTE);

    public ModelAndView controleVersao() {

        mv = new ModelAndView("controleVersaoCliente/controleVersao");
        mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
        return mv;
    }

    public ModelAndView controleVersaoPacote() {
        mv = new ModelAndView("controleVersaoCliente/controleVersaoPacote");
        mv.addObject("lsTipoProduto", tipoProdutoRepository.findAll());
        mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
        return mv;
    }

    public ModelAndView controleVersaoPacote(String clienteChip, String ambienteChip, Long tipoProdutoId,
            String subProdutoChip,
            String versaoHomologacao, String versaoPreProducao, String versaoProducao) {

        mv = new ModelAndView("redirect:/controleVersaoPacote");

        List<Cliente> lsCliente = new ArrayList<>();
        List<TipoAmbiente> lsTipoAmbiente = new ArrayList<>();
        List<TipoSubProduto> lsTipoSubProduto = new ArrayList<>();

        try {
            jsonArrays(clienteChip, ambienteChip, subProdutoChip, lsCliente, lsTipoAmbiente, lsTipoSubProduto);

            cadastroControleV(tipoProdutoId, versaoHomologacao, versaoPreProducao, versaoProducao, lsCliente, lsTipoSubProduto);

            sendMail.enviarEmailPacoteAtualizacao(usuarioBean.getUsuario().getEmail(), lsCliente, lsTipoAmbiente,
                    lsTipoSubProduto, versaoHomologacao, versaoPreProducao, versaoProducao);
            log.info("\nPacote de versionamento executado!" +
                    "\nCLIENTE ATUALIZADOS: " + lsCliente.size() +
                    "\nTIPOS DE AMBIENTE: " + lsTipoAmbiente.size() +
                    "\nSUB-PRODUTOS: " + lsTipoSubProduto.size() +
                    "\nVERSAO HOMOLOGACAO: " + versaoHomologacao +
                    "\nVERSAO PRE-PRODUCAO: " + versaoPreProducao +
                    "\nVERSAO PRODUCAO: " + versaoProducao +
                    "\n-------------------------------------------------------------------------------[ Controle de Versao Pacote ]");

            log.info("Controle de Versao Pacote concluido com sucesso!");
            return mv;

        } catch (Exception e) {
            log.error("Ops! Ocorreu um erro ao atualizar versao dos clientes. Motivo: " + e.getMessage());
            log.error(e.getMessage());
            return mv;
        }
    }

    private void cadastroControleV(Long tipoProdutoId, String versaoHomologacao, String versaoPreProducao,
            String versaoProducao, List<Cliente> lsCliente, List<TipoSubProduto> lsTipoSubProduto) {

        TipoProduto tipoProduto = tipoProdutoRepository.findByTipoProdutoId(tipoProdutoId);
        ControleVersaoCliente controleVersaoCliente;

        ControleVersaoCliente controleVersaoClienteExiste;
        for (int i = 0; i < lsCliente.size(); i++) {

            for (int a = 0; a < lsTipoSubProduto.size(); a++) {

                controleVersaoClienteExiste = controleVersaoClienteRepository.buscarPorTipoSubProdutoIdEclienteId(
                        lsTipoSubProduto.get(a).getTipoSubProdutoId(), lsCliente.get(i).getClienteId());

                if (controleVersaoClienteExiste != null) {
                    atualizarControleV(versaoHomologacao, versaoPreProducao, versaoProducao, controleVersaoClienteExiste);
                } else {
                    controleVersaoCliente = new ControleVersaoCliente();
                    controleVersaoCliente.setCliente(lsCliente.get(i));
                    controleVersaoCliente.setTipoProduto(tipoProduto);
                    controleVersaoCliente.setTipoSubProduto(lsTipoSubProduto.get(a));
                    controleVersaoCliente.setDataCadastro(new Date());
                    controleVersaoCliente.setDataAlteracao(controleVersaoCliente.getDataCadastro());
                    controleVersaoCliente.setDataAlteracaoVersaoHomologacao(new Date());
                    controleVersaoCliente.setDataAlteracaoVersaoPreProducao(new Date());
                    controleVersaoCliente.setDataAlteracaoVersaoProducao(new Date());
                    controleVersaoCliente.setVersaoHomologacao(versaoHomologacao);
                    controleVersaoCliente.setVersaoPreProducao(versaoPreProducao);
                    controleVersaoCliente.setVersaoProducao(versaoProducao);
                    controleVersaoClienteRepository.save(controleVersaoCliente);
                    log.info("CONTROLE DE VERSAO: " + controleVersaoCliente.getCliente().getNomeCliente() + " - [" +
                            controleVersaoCliente.getTipoSubProduto().getNomeSubProduto()
                            + "] CADASTRADO COM SUCESSO!");
                }
            }
        }
    }


    private void atualizarControleV(String versaoHomologacao, String versaoPreProducao, String versaoProducao,
            ControleVersaoCliente controleVersaoClienteExiste) {
        if (versaoHomologacao != null && !versaoHomologacao.isEmpty()) {
            controleVersaoClienteExiste.setDataAlteracaoVersaoHomologacao(new Date());
            controleVersaoClienteExiste.setVersaoHomologacao(versaoHomologacao);
        }
        if (versaoPreProducao != null && !versaoPreProducao.isEmpty()) {
            controleVersaoClienteExiste.setDataAlteracaoVersaoPreProducao(new Date());
            controleVersaoClienteExiste.setVersaoPreProducao(versaoPreProducao);
        }
        if (versaoProducao != null && !versaoProducao.isEmpty()) {
            controleVersaoClienteExiste.setDataAlteracaoVersaoProducao(new Date());
            controleVersaoClienteExiste.setVersaoProducao(versaoProducao);
        }
        controleVersaoClienteRepository.save(controleVersaoClienteExiste);
        log.info("CONTROLE DE VERSAO: " + controleVersaoClienteExiste.getCliente().getNomeCliente()
                + " - [" +
                controleVersaoClienteExiste.getTipoSubProduto().getNomeSubProduto()
                + "] ATUALIZADA COM SUCESSO!");
    }

    private void jsonArrays(String clienteChip, String ambienteChip, String subProdutoChip, List<Cliente> lsCliente,
            List<TipoAmbiente> lsTipoAmbiente, List<TipoSubProduto> lsTipoSubProduto) throws JSONException {
        JSONArray jsonArrayCliente = new JSONArray(clienteChip);
        for (int i = 0; i < jsonArrayCliente.length(); i++) {

            JSONObject jsonObject = jsonArrayCliente.getJSONObject(i);
            Cliente cliente = clienteRepository.buscarPorNome(jsonObject.getString("tag"));
            if (cliente != null) {
                log.info("CLIENTE: " + cliente.getClienteId() + " - " + cliente.getNomeCliente());
                lsCliente.add(cliente);
            }
        }

        JSONArray jsonArrayAmbientes = new JSONArray(ambienteChip);
        for (int i = 0; i < jsonArrayAmbientes.length(); i++) {

            JSONObject jsonObject = jsonArrayAmbientes.getJSONObject(i);
            TipoAmbiente tipoAmbiente = tipoAmbienteRepository
                    .buscarTipoAmbientePorNome(jsonObject.getString("tag"));
            if (tipoAmbiente != null) {
                log.info("TIPO DE AMBIENTE: " + tipoAmbiente.getTipoAmbienteId() + " - "
                        + tipoAmbiente.getNomeAmbiente());
                lsTipoAmbiente.add(tipoAmbiente);
            }
        }

        JSONArray jsonArraySubProdutos = new JSONArray(subProdutoChip);
        for (int i = 0; i < jsonArraySubProdutos.length(); i++) {

            JSONObject jsonObject = jsonArraySubProdutos.getJSONObject(i);
            TipoSubProduto tipoSubProduto = tipoSubProdutoRepository
                    .buscarTipoSubProdutoPorNome(jsonObject.getString("tag"));
            if (tipoSubProduto != null) {
                log.info("TIPO DE SUB-PRODUTO: " + tipoSubProduto.getTipoSubProdutoId() + " - "
                        + tipoSubProduto.getNomeSubProduto());
                lsTipoSubProduto.add(tipoSubProduto);
            }
        }
    }

    public ModelAndView controleVersaoCliente(long clienteId, @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("page") Optional<Integer> page) {

        mv = new ModelAndView("controleVersaoCliente/controleVersaoCliente");
        try {
            int evalPageSize;
            int evalPag = page.isPresent() ? page.get() - 1 : 0;
            int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : evalPag;

            final ParametrosSistema pageSizeList = parametrosRepository.findByChave(PAGE_SIZE_LIST);
            if (pageSizeList != null && pageSizeList.getChave() != null && pageSizeList.getValor() != null) {
                evalPageSize = pageSize.orElse(Integer.valueOf(pageSizeList.getValor()));
            } else {
                evalPageSize = 5;
            }

            usuarioBean.setPageSize(evalPageSize);
            int pag = page.isPresent() ? page.get() : 0;
            usuarioBean.setPage((page.orElse(0) < 1) ? INITIAL_PAGE : pag);

            Cliente cliente = clienteRepository.findByClienteId(clienteId);
            Page<ControleVersaoCliente> lsControleVersaoCliente = controleVersaoClienteRepository
                    .buscarControleVersaoPorCliente(clienteId, PageRequest.of(evalPage, evalPageSize,
                            Sort.by("controle_versao_cliente_id").descending()));
            PagerModel pager = new PagerModel(lsControleVersaoCliente.getTotalPages(),
                    lsControleVersaoCliente.getNumber(), BUTTONS_TO_SHOW);
            mv.addObject("searchVersao", false);
            mv.addObject(GET_CLIENTE_ID, cliente.getClienteId());
            mv.addObject("clienteNome", cliente.getNomeCliente());
            mv.addObject("selectedPageSize", evalPageSize);
            mv.addObject("pageSizes", PAGE_SIZES);
            mv.addObject("pager", pager);
            mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
            mv.addObject("lsControleVersaoCliente", lsControleVersaoCliente);
            mv.addObject("lsTipoProduto", tipoProdutoRepository.findAll());
            mv.addObject("quantidadeVersionamento",
                    controleVersaoClienteRepository.totalControleVersaoCliente(clienteId));
            return mv;
        } catch (Exception e) {
            log.error("Ocorreu um erro! Motivo: " + e.getMessage());
            return new ModelAndView("redirect:/clientes");
        }
    }

    public List<Cliente> buscarClientesControleVersao() {

        return clienteRepository.buscarTodosClientes();
    }

    public List<TipoAmbiente> buscarTipoAmbiente() {
        return tipoAmbienteRepository.findAll();
    }

    public List<TipoSubProduto> buscarSubProduto(Long tipoProdutoId) {

        List<TipoSubProduto> lsTipoSubProduto;
        if (tipoProdutoId != null) {
            lsTipoSubProduto = tipoSubProdutoRepository.buscarPorTipoProdutoId(tipoProdutoId);
        } else {
            lsTipoSubProduto = tipoSubProdutoRepository.findAll();
        }
        return lsTipoSubProduto;
    }

    public ModelAndView cadastrarControleVersaoCliente() {

        mv = null;
        if (ValidaPermissoesUsuario.usuarioAdmin(usuarioBean.getUsuario())) {
            mv = new ModelAndView("controleVersaoCliente/cadastrarControleVersaoCliente");
            mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
        } else {
            mv = new ModelAndView("redirect:/index");
        }
        return mv;
    }

    public ModelAndView cadastrarControleVersaoCliente(long clienteId,
            ControleVersaoClienteDTO controleVersaoClienteDTO, long tipoProdutoId, long tipoSubProdutoId) {

        mv.addObject(GET_CLIENTE_ID, clienteId);
        try {
            Cliente c = clienteRepository.findByClienteId(clienteId);
            TipoProduto tp = tipoProdutoRepository.findByTipoProdutoId(tipoProdutoId);
            TipoSubProduto tsp = tipoSubProdutoRepository.findByTipoSubProdutoId(tipoSubProdutoId);
            controleVersaoClienteDTO.setCliente(c);
            controleVersaoClienteDTO.setTipoProduto(tp);
            controleVersaoClienteDTO.setTipoSubProduto(tsp);
            controleVersaoClienteDTO.setDataCadastro(new Date());
            controleVersaoClienteDTO.setDataAlteracao(controleVersaoClienteDTO.getDataCadastro());
            controleVersaoClienteDTO.setDataAlteracaoVersaoHomologacao(new Date());
            controleVersaoClienteDTO
                    .setDataAlteracaoVersaoPreProducao(controleVersaoClienteDTO.getDataAlteracaoVersaoHomologacao());
            controleVersaoClienteDTO
                    .setDataAlteracaoVersaoProducao(controleVersaoClienteDTO.getDataAlteracaoVersaoPreProducao());
            ControleVersaoCliente controleVersaoCliente = dtoMaps.modelMapper().map(controleVersaoClienteDTO,
                    ControleVersaoCliente.class);
            controleVersaoClienteRepository.save(controleVersaoCliente);
            log.info(controleVersaoCliente.getControleVersaoClienteId() + " cadastrado com sucesso!");
            return mv;

        } catch (Exception e) {
            log.error(e.getMessage());
            return mv;
        }
    }

    public ModelAndView alterarControleVersaoCliente(long clienteId,
            ControleVersaoClienteDTO controleVersaoClienteDTO) {

        mv.addObject(GET_CLIENTE_ID, clienteId);
        try {
            ControleVersaoCliente cvc = controleVersaoClienteRepository
                    .findByControleVersaoClienteId(controleVersaoClienteDTO.getControleVersaoClienteId());
            if (!cvc.getVersaoHomologacao().equals(controleVersaoClienteDTO.getVersaoHomologacao())) {
                cvc.setVersaoHomologacao(controleVersaoClienteDTO.getVersaoHomologacao());
                cvc.setDataAlteracaoVersaoHomologacao(new Date());
            }
            if (!cvc.getVersaoPreProducao().equals(controleVersaoClienteDTO.getVersaoPreProducao())) {
                cvc.setVersaoPreProducao(controleVersaoClienteDTO.getVersaoPreProducao());
                cvc.setDataAlteracaoVersaoPreProducao(new Date());
            }
            if (!cvc.getVersaoProducao().equals(controleVersaoClienteDTO.getVersaoProducao())) {
                cvc.setVersaoProducao(controleVersaoClienteDTO.getVersaoProducao());
                cvc.setDataAlteracaoVersaoProducao(new Date());
            }
            cvc.setDataAlteracao(new Date());
            controleVersaoClienteRepository.save(cvc);
            log.info(controleVersaoClienteDTO.getControleVersaoClienteId() + " alterado com sucesso!");
            return mv;

        } catch (Exception e) {
            log.error(e.getMessage());
            return mv;
        }
    }

    public ModelAndView deletarControleVersaoCliente(long clienteId, long controleVersaoClienteId) {

        mv.addObject(GET_CLIENTE_ID, clienteId);
        try {
            ControleVersaoCliente cvc = controleVersaoClienteRepository
                    .findByControleVersaoClienteId(controleVersaoClienteId);
            controleVersaoClienteRepository.delete(cvc);
            log.info(cvc.getControleVersaoClienteId() + " deletado com sucesso!");
            return mv;
        } catch (Exception e) {
            log.error(e.getMessage());
            return mv;
        }
    }
}
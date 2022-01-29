package br.com.zaratech.service.impl;

import static br.com.zaratech.util.GrantControlConstants.CHAVE_AUTENTICACAO_TEXT;
import static br.com.zaratech.util.GrantControlConstants.CHECK_CODE_URL;
import static br.com.zaratech.util.GrantControlConstants.GET_CLIENTE;
import static br.com.zaratech.util.GrantControlConstants.GET_CLIENTE_PRODUTO;
import static br.com.zaratech.util.GrantControlConstants.GET_LS_TIPO_AMBIENTE;
import static br.com.zaratech.util.GrantControlConstants.GET_LS_TIPO_PRODUTO;
import static br.com.zaratech.util.GrantControlConstants.USUARIO_LOGADO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.bean.DtoMaps;
import br.com.zaratech.bean.UsuarioBean;
import br.com.zaratech.dto.ClienteProdutoDTO;
import br.com.zaratech.model.Cliente;
import br.com.zaratech.model.ClienteProduto;
import br.com.zaratech.model.ParametrosSistema;
import br.com.zaratech.model.TipoAmbiente;
import br.com.zaratech.model.TipoProduto;
import br.com.zaratech.repository.ClienteProdutoRepository;
import br.com.zaratech.repository.ClienteRepository;
import br.com.zaratech.repository.ParametrosRepository;
import br.com.zaratech.repository.TipoAmbienteRepository;
import br.com.zaratech.repository.TipoProdutoRepository;
import br.com.zaratech.service.ClienteProdutoService;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@Service
public class ClienteProdutoServiceImpl implements ClienteProdutoService {

    private UsuarioBean usuarioBean;
    private ClienteRepository clienteRepository;
    private ClienteProdutoRepository clienteProdutoRepository;
    private TipoProdutoRepository tipoProdutoRepository;
    private TipoAmbienteRepository tipoAmbienteRepository;
    private ParametrosRepository parametrosRepository;

    @Autowired
    private DtoMaps dtoMaps;

    @Autowired
    private void handleImpl(UsuarioBean usuarioBean, ClienteRepository clienteRepository,
            ClienteProdutoRepository clienteProdutoRepository, TipoProdutoRepository tipoProdutoRepository,
            TipoAmbienteRepository tipoAmbienteRepository, ParametrosRepository parametrosRepository) {

        this.usuarioBean = usuarioBean;
        this.clienteRepository = clienteRepository;
        this.clienteProdutoRepository = clienteProdutoRepository;
        this.tipoProdutoRepository = tipoProdutoRepository;
        this.tipoAmbienteRepository = tipoAmbienteRepository;
        this.parametrosRepository = parametrosRepository;
    }

    public int codigoUrl(String urlAplicacao) {
        int codeUrl = 0;
        try {
            URL url = new URL(urlAplicacao);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            codeUrl = connection.getResponseCode();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return codeUrl;
    }

    public void downloadPrivateKey(HttpServletRequest req, HttpServletResponse resp, Long clienteProdutoId) {

        try {
            ClienteProduto clienteProduto = clienteProdutoRepository.findByClienteProdutoId(clienteProdutoId);
            DefaultResourceLoader loader = new DefaultResourceLoader();
            InputStream is = loader.getResource("downloads/" + clienteProduto.getNomeChave().concat(".ppk"))
                    .getInputStream();
            IOUtils.copy(is, resp.getOutputStream());
            resp.setHeader("Content-Disposition",
                    "attachment; filename=" + clienteProduto.getNomeChave().concat(".ppk") + "");
            resp.flushBuffer();
        } catch (IOException ex) {
            log.error("Erro ao ler o arquivo! Motivo: " + ex.getMessage());
            try {
                resp.sendRedirect(req.getContextPath().concat("/detalhesClienteProduto/" + clienteProdutoId));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    public void exportarDadosProduto(HttpServletRequest req, HttpServletResponse resp, Long clienteProdutoId)
            throws IOException {

        try {

            ClienteProduto clienteProduto = clienteProdutoRepository.findByClienteProdutoId(clienteProdutoId);

            PrintWriter pw = new PrintWriter(new FileOutputStream("PATH.txt"));
            pw.write("dsadsadsa");
            pw.flush();
            pw.close();

            resp.setHeader("Content-Disposition",
                    "attachment; filename=" + clienteProduto.getDescricao().concat(".txt") + "");
            resp.flushBuffer();

        } catch (IOException ex) {
            log.error("Erro ao gerar o arquivo! Motivo: " + ex.getMessage());
            resp.sendRedirect(req.getContextPath().concat("/detalhesClienteProduto/" + clienteProdutoId));
        }
    }

    public ModelAndView detalhesClienteProduto(long clienteProdutoId) {

        ModelAndView mv = new ModelAndView("clienteProduto/detalhesClienteProduto");
        ClienteProduto clienteProduto = clienteProdutoRepository.findByClienteProdutoId(clienteProdutoId);

        List<TipoProduto> lsTipoProduto = tipoProdutoRepository.findAllByOrderByNomeProdutoAsc();
        List<TipoAmbiente> lsTipoAmbiente = tipoAmbienteRepository.findAllByOrderByNomeAmbienteAsc();

        if (clienteProduto.isPossuiChave() && !Objects.isNull(clienteProduto.getNomeChave())) {
            mv.addObject("possuiKey", clienteProduto.isPossuiChave());
            mv.addObject("key", clienteProduto.getNomeChave() + ".ppk");
            mv.addObject("keyName", clienteProduto.getNomeChave() + ".ppk");
        }

        final ParametrosSistema checkCodeUrl = parametrosRepository.findByChave(CHECK_CODE_URL);
        if (!Objects.isNull(checkCodeUrl) && !Objects.isNull(checkCodeUrl.getChave())
                && !Objects.isNull(checkCodeUrl.getValor()) && checkCodeUrl.getValor().equals("1")) {
            mv.addObject("statusURL", codigoUrl(clienteProduto.getUrlAplicacao()));
        }
        mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
        mv.addObject(GET_CLIENTE, clienteProduto.getCliente());
        mv.addObject(GET_CLIENTE_PRODUTO, clienteProduto);
        mv.addObject(GET_LS_TIPO_AMBIENTE, lsTipoAmbiente);
        mv.addObject(GET_LS_TIPO_PRODUTO, lsTipoProduto);
        mv.addObject("tipoAmbienteId", clienteProduto.getTipoAmbiente().getTipoAmbienteId());
        mv.addObject("tipoProdutoId", clienteProduto.getTipoProduto().getTipoProdutoId());
        return mv;
    }

    public ModelAndView cadastrar(long clienteId) {

        try {
            ModelAndView mv = new ModelAndView("clienteProduto/cadastrarClienteProduto");
            List<TipoAmbiente> lsTipoAmbiente = tipoAmbienteRepository.findAllByOrderByNomeAmbienteAsc();
            List<TipoProduto> lsTipoProduto = tipoProdutoRepository.findAllByOrderByNomeProdutoAsc();
            Cliente cliente = clienteRepository.findByClienteId(clienteId);
            ClienteProduto cp = new ClienteProduto();
            if (usuarioBean.getUsuario().getNivel() == 1) {
                mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
                mv.addObject(GET_CLIENTE, cliente);
                mv.addObject(GET_CLIENTE_PRODUTO, cp);
                mv.addObject(GET_LS_TIPO_PRODUTO, lsTipoProduto);
                mv.addObject(GET_LS_TIPO_AMBIENTE, lsTipoAmbiente);
            } else {
                mv = new ModelAndView("redirect:/index");
            }
            return mv;
        } catch (Exception e) {
            return new ModelAndView("/login");
        }
    }

    public ModelAndView cadastrarClienteProduto(ClienteProdutoDTO clienteProdutoDTO,
            long clienteId, long tipoAmbienteId, long tipoProdutoId, boolean possuiChave, String nomeChave) {

        try {
            TipoAmbiente ta = tipoAmbienteRepository.findByTipoAmbienteId(tipoAmbienteId);
            TipoProduto tp = tipoProdutoRepository.findByTipoProdutoId(tipoProdutoId);
            Cliente cliente = clienteRepository.findByClienteId(clienteId);
            clienteProdutoDTO.setCliente(cliente);
            clienteProdutoDTO.setTipoAmbiente(ta);
            clienteProdutoDTO.setTipoProduto(tp);
            clienteProdutoDTO.setDataCadastro(new Date());

            if (possuiChave && nomeChave != null) {
                clienteProdutoDTO.setPossuiChave(possuiChave);
                clienteProdutoDTO.setNomeChave(nomeChave);
                clienteProdutoDTO.setUsuario("-");
                clienteProdutoDTO.setSenha("-");
                log.info(CHAVE_AUTENTICACAO_TEXT + nomeChave + ".ppk");
            }
            ClienteProduto clienteProduto = dtoMaps.modelMapper().map(clienteProdutoDTO, ClienteProduto.class);
            clienteProdutoRepository.save(clienteProduto);
            log.info("Produto Cadastrado para o cliente " + cliente.getNomeCliente());
            return new ModelAndView("redirect:/detalhesCliente/{clienteId}");

        } catch (Exception e) {

            log.error(e.getMessage());
            return new ModelAndView("redirect:/index");
        }
    }

    public ModelAndView cadastrarDetalhesClienteProduto(long clienteProdutoId) {

        ClienteProduto clienteProduto = clienteProdutoRepository.findByClienteProdutoId(clienteProdutoId);
        List<TipoProduto> lsTipoProduto = tipoProdutoRepository.findAllByOrderByNomeProdutoAsc();
        List<TipoAmbiente> lsTipoAmbiente = tipoAmbienteRepository.findAllByOrderByNomeAmbienteAsc();
        ModelAndView mv = new ModelAndView("clienteProduto/cadastrarDetalhesClienteProduto");
        mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
        mv.addObject(GET_CLIENTE, clienteProduto.getCliente());
        mv.addObject(GET_CLIENTE_PRODUTO, clienteProduto);
        mv.addObject(GET_LS_TIPO_AMBIENTE, lsTipoAmbiente);
        mv.addObject(GET_LS_TIPO_PRODUTO, lsTipoProduto);
        mv.addObject("tipoAmbienteId", clienteProduto.getTipoAmbiente().getTipoAmbienteId());
        mv.addObject("tipoProdutoId", clienteProduto.getTipoProduto().getTipoProdutoId());
        return mv;
    }

    public ModelAndView alterarClienteProduto(long clienteProdutoId, ClienteProdutoDTO clienteProdutoDTO,
            long tipoAmbienteId, long tipoProdutoId, boolean possuiChave, String nomeChave) {

        try {
            ClienteProduto cp = clienteProdutoRepository.findByClienteProdutoId(clienteProdutoId);
            ClienteProduto clienteProduto = cp;
            TipoAmbiente ta = tipoAmbienteRepository.findByTipoAmbienteId(tipoAmbienteId);
            TipoProduto tp = tipoProdutoRepository.findByTipoProdutoId(tipoProdutoId);
            clienteProduto.setClienteProdutoId(clienteProdutoId);
            clienteProduto.setCliente(cp.getCliente());
            clienteProduto.setDescricao(clienteProdutoDTO.getDescricao());
            clienteProduto.setIp(clienteProdutoDTO.getIp());
            clienteProduto.setUsuario(clienteProdutoDTO.getUsuario());
            clienteProduto.setSenha(clienteProdutoDTO.getSenha());
            clienteProduto.setUsuarioAux(clienteProdutoDTO.getUsuarioAux());
            clienteProduto.setSenhaAux(clienteProdutoDTO.getSenhaAux());
            clienteProduto.setPorta(clienteProdutoDTO.getPorta());
            clienteProduto.setUrlAplicacao(clienteProdutoDTO.getUrlAplicacao());
            clienteProduto.setUsuarioAplicacao(clienteProdutoDTO.getUsuarioAplicacao());
            clienteProduto.setSenhaAplicacao(clienteProdutoDTO.getSenhaAplicacao());
            clienteProduto.setTipoAmbiente(ta);
            clienteProduto.setTipoProduto(tp);
            clienteProduto.setDataCadastro(clienteProdutoDTO.getDataCadastro());
            clienteProduto.setDataAlteracao(new Date());
            clienteProduto.setUsuarioAlteracao(usuarioBean.getUsuario().getLogin());

            if (nomeChave.isEmpty()) {

                clienteProduto.setPossuiChave(false);
                clienteProduto.setNomeChave("");

            } else if (possuiChave && !nomeChave.isEmpty()) {

                clienteProduto.setPossuiChave(true);
                clienteProduto.setNomeChave(nomeChave);
                clienteProduto.setUsuario("-");
                clienteProduto.setSenha("-");
                log.info(CHAVE_AUTENTICACAO_TEXT + nomeChave + ".ppk");

            } else {

                if (cp.isPossuiChave() && cp.getNomeChave() != null) {
                    clienteProduto.setPossuiChave(cp.isPossuiChave());
                    clienteProduto.setNomeChave(cp.getNomeChave());
                    clienteProduto.setUsuario("-");
                    clienteProduto.setSenha("-");
                    log.info(CHAVE_AUTENTICACAO_TEXT + cp.getNomeChave() + ".ppk");
                }
            }

            Cliente cliente = clienteRepository.findByClienteId(clienteProduto.getCliente().getClienteId());
            ModelAndView mv = new ModelAndView("redirect:/detalhesClienteProduto/" + clienteProdutoId);
            mv.addObject(GET_CLIENTE, cliente);
            clienteProdutoRepository.save(clienteProduto);
            log.info("Alteracao de produto:\nCliente: " + cliente.getNomeCliente() +
                    "\nProduto: " + clienteProduto.getDescricao() +
                    "\nAlterado por: " + usuarioBean.getUsuario().getNome() +
                    "\nData/Hora: " + clienteProduto.getDataAlteracao());
            return mv;
        } catch (Exception e) {
            log.error("Erro ao realizar alteracao do clienteProduto: " + e.getMessage());
            return null;
        }
    }

    public ModelAndView deletarClienteProduto(long clienteProdutoId) {

        ClienteProduto clienteProduto = clienteProdutoRepository.findByClienteProdutoId(clienteProdutoId);
        clienteProdutoRepository.delete(clienteProduto);
        log.info("Produto excluido!");
        return new ModelAndView("redirect:/detalhesCliente/" + clienteProduto.getCliente().getClienteId());
    }
}
package br.com.zaratech.controller;

import br.com.zaratech.bean.UsuarioBean;
import br.com.zaratech.model.*;
import br.com.zaratech.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import static br.com.zaratech.util.GrantControlConstants.*;

import java.util.List;

@Slf4j
@Controller
public class ConfiguracaoController {

    private UsuarioBean usuarioBean;
    private ParametrosRepository parametrosRepository;
    private ParametrosEmailRepository parametrosEmailRepository;
    private TipoAmbienteRepository tipoAmbienteRepository;
    private TipoProdutoRepository tipoProdutoRepository;
    private TipoDepartamentoRepository tipoDepartamentoRepository;

    private ModelAndView mv = new ModelAndView(MODEL_VIEW_CONFIGURACAO);

    @Autowired
    void handle(UsuarioBean usuarioBean,
                ParametrosRepository parametrosRepository,
                ParametrosEmailRepository parametrosEmailRepository,
                TipoAmbienteRepository tipoAmbienteRepository,
                TipoProdutoRepository tipoProdutoRepository,
                TipoDepartamentoRepository tipoDepartamentoRepository) {

        this.usuarioBean = usuarioBean;
        this.parametrosRepository = parametrosRepository;
        this.parametrosEmailRepository = parametrosEmailRepository;
        this.tipoAmbienteRepository = tipoAmbienteRepository;
        this.tipoProdutoRepository = tipoProdutoRepository;
        this.tipoDepartamentoRepository = tipoDepartamentoRepository;
    }

    private boolean adminAutorizado() {
        return usuarioBean.getUsuario().getLogin().equals(USER_ADMIN) &&
                usuarioBean.getUsuario().getNivel() == 1;
    }

    private Object modelDefault() {
        mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
        return mv;
    }

    @GetMapping(value = "configuracao")
    public ModelAndView configuracoes() {
        mv.addObject(modelDefault());
        if (adminAutorizado()) {
            List<ParametrosSistema> lsParametrosSistema = parametrosRepository.findAll();
            List<ParametrosEmail> lsParametrosEmail = parametrosEmailRepository.findAll();
            List<TipoAmbiente> lsTipoAmbiente = tipoAmbienteRepository.findAllByOrderByNomeAmbienteAsc();
            Iterable<TipoProduto> lsTipoProduto = tipoProdutoRepository.findAllByOrderByNomeProdutoAsc();
            List<TipoDepartamento> lsTipoDepartamento = tipoDepartamentoRepository.findAll();
            mv.addObject("lsParametrosSistema", lsParametrosSistema);
            mv.addObject("lsParametrosEmail", lsParametrosEmail);
            mv.addObject("lsTipoAmbiente", lsTipoAmbiente);
            mv.addObject("lsTipoProduto", lsTipoProduto);
            mv.addObject("lsTipoDepartamento", lsTipoDepartamento);
        } else {
            mv = new ModelAndView(MODEL_VIEW_REDIRECT_INDEX);
        }
        return mv;
    }

    @PostMapping(value = "alterarConfiguracao")
    public ModelAndView alterarConfiguracao(Long parametroId, String chave, String valor, String descricao) {
        mv.addObject(modelDefault());
        try {
            if (adminAutorizado()) {
                ParametrosSistema parametrosSistema = parametrosRepository.findByParametroId(parametroId);
                parametrosSistema.setChave(chave);
                parametrosSistema.setValor(valor);
                parametrosSistema.setDescricao(descricao);
                parametrosRepository.save(parametrosSistema);
                log.info("Dados alterados no banco: " + parametroId + ";" + chave + ";" + valor + ";" + descricao);
            } else {
                mv = new ModelAndView(MODEL_VIEW_REDIRECT_INDEX);
            }
            return mv;
        } catch (Exception e) {
            log.info(e.getMessage());
            new ModelAndView("redirect:/configuracao");
        }
        return mv;
    }

    @PostMapping(value = "inserirParametro")
    public ModelAndView inserirParametro(String chave, String valor, String descricao) {
        mv.addObject(modelDefault());
        if (adminAutorizado()) {
            ParametrosSistema parametroSistema = parametrosRepository.findByChave(chave);
            if (parametroSistema == null) {
                parametroSistema = new ParametrosSistema();
                parametroSistema.setChave(chave);
                parametroSistema.setValor(valor);
                parametroSistema.setDescricao(descricao);
                parametrosRepository.save(parametroSistema);
                log.info("Dados inseridos no banco: " + parametroSistema.getParametroId() + ";" +
                        parametroSistema.getChave() + ";" + parametroSistema.getValor() + ";" + parametroSistema.getDescricao());
            }
        } else {
            mv = new ModelAndView(MODEL_VIEW_REDIRECT_INDEX);
        }
        return mv;
    }

    @PostMapping(value = "alterarParametrosEmail")
    public ModelAndView alterarParametrosEmail(Long parametroEmailId, String emailFrom, String emailFromName,
                                               String emailSmtpUsername, String emailSmtpPassword, String emailSmtpHost, int emailPort,
                                               String emailSubject, String configSet, String urlEnvio) {
        mv.addObject(modelDefault());
        if (adminAutorizado()) {
            ParametrosEmail parametroEmail = parametrosEmailRepository.findByParametroEmailId(parametroEmailId);
            parametroEmail.setEmailFrom(emailFrom);
            parametroEmail.setEmailFromName(emailFromName);
            parametroEmail.setEmailSmtpUsername(emailSmtpUsername);
            parametroEmail.setEmailSmtpPassword(emailSmtpPassword);
            parametroEmail.setEmailSmtpHost(emailSmtpHost);
            parametroEmail.setEmailPort(emailPort);
            parametroEmail.setEmailSubject(emailSubject);
            parametroEmail.setConfigSet(configSet);
            parametroEmail.setUrlEnvio(urlEnvio);
            parametrosEmailRepository.save(parametroEmail);
            log.info("Dados de e-mail alterados no banco: " + parametroEmailId + ";" + emailFrom + ";" + emailFromName +
                    ";" + emailSmtpUsername + ";" + emailSmtpPassword + emailSmtpHost + ";" + emailPort + ";" + emailSubject +
                    ";" + configSet + ";" + urlEnvio);
        } else {
            mv = new ModelAndView(MODEL_VIEW_REDIRECT_INDEX);
        }
        return mv;
    }

    @PostMapping(value = "alterarAmbiente")
    public ModelAndView alterarAmbiente(Long tipoAmbienteId, String nomeAmbiente) {
        mv.addObject(modelDefault());
        if (adminAutorizado()) {
            TipoAmbiente tipoAmbiente = tipoAmbienteRepository.findByTipoAmbienteId(tipoAmbienteId);
            tipoAmbiente.setNomeAmbiente(nomeAmbiente);
            tipoAmbienteRepository.save(tipoAmbiente);
            log.info("Ambiente alterados no banco: " + tipoAmbienteId + ";" + nomeAmbiente);
            return mv;
        } else {
            return new ModelAndView(MODEL_VIEW_REDIRECT_INDEX);
        }
    }

    @PostMapping(value = "alterarProduto")
    public ModelAndView alterarProduto(Long tipoProdutoId, String nomeProduto) {
        mv.addObject(modelDefault());
        if (adminAutorizado()) {
            TipoProduto tipoProduto = tipoProdutoRepository.findByTipoProdutoId(tipoProdutoId);
            tipoProduto.setNomeProduto(nomeProduto);
            tipoProdutoRepository.save(tipoProduto);
            log.info("Produto alterados no banco: " + tipoProdutoId + ";" + nomeProduto);
            return mv;
        } else {
            return new ModelAndView(MODEL_VIEW_REDIRECT_INDEX);
        }
    }

    @PostMapping(value = "inserirProduto")
    public ModelAndView inserirProduto(String nomeProduto) {
        mv.addObject(modelDefault());
        if (adminAutorizado()) {
            TipoProduto tipoProduto = tipoProdutoRepository.findByNomeProduto(nomeProduto);
            if (tipoProduto == null) {
                tipoProduto = new TipoProduto();
                tipoProduto.setNomeProduto(nomeProduto);
                tipoProdutoRepository.save(tipoProduto);
                log.info("Produto inserido no banco: " + tipoProduto.getTipoProdutoId() + ";" + tipoProduto.getNomeProduto());
            }
            return mv;
        } else {
            return new ModelAndView(MODEL_VIEW_REDIRECT_INDEX);
        }
    }

    @PostMapping(value = "alterarDepartamento")
    public ModelAndView alterarDepartamento(Long parametroId, String chave, String valor, String descricao) {
        mv.addObject(modelDefault());
        if (adminAutorizado()) {
            ParametrosSistema parametrosSistema = parametrosRepository.findByParametroId(parametroId);
            parametrosSistema.setChave(chave);
            parametrosSistema.setValor(valor);
            parametrosSistema.setDescricao(descricao);
            parametrosRepository.save(parametrosSistema);
            log.info("Dados alterados no banco: " + parametroId + ";" + chave + ";" + valor + ";" + descricao);
        } else {
            mv = new ModelAndView(MODEL_VIEW_REDIRECT_INDEX);
        }
        return mv;
    }
}
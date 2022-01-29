package br.com.zaratech.controller;

import static br.com.zaratech.util.GrantControlConstants.LISTAR_COMPARTILHAMENTOS;
import static br.com.zaratech.util.GrantControlConstants.USUARIO_LOGADO;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.bean.UsuarioBean;
import br.com.zaratech.dto.GuiaWebDTO;
import br.com.zaratech.model.GuiaWeb;
import br.com.zaratech.repository.GuiaWebRepository;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@Controller
public class GuiaWebController {

    private GuiaWebRepository guiaWebRepository;
    private UsuarioBean usuarioBean;

    @Autowired
    void handle(GuiaWebRepository guiaWebRepository, UsuarioBean usuarioBean) {
        this.guiaWebRepository = guiaWebRepository;
        this.usuarioBean = usuarioBean;
    }

    @GetMapping(value = "/guiaweb")
    public ModelAndView guiaweb() {

        ModelAndView mv = new ModelAndView("ferramentas/guiaweb");
        Iterable<GuiaWeb> compartilhamentos = guiaWebRepository.findAllCompartilhamentos();
        mv.addObject(LISTAR_COMPARTILHAMENTOS, compartilhamentos);
        mv.addObject("quantidadeCompartilhamentos", guiaWebRepository.totalGuiaWeb());
        mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
        return mv;
    }

    @GetMapping(value = "/guiawebBusca")
    public ModelAndView guiawebBusca(String titulo) {
        ModelAndView mv = new ModelAndView("ferramentas/guiaweb");
        if (!titulo.isEmpty()) {
            List<GuiaWeb> compartilhamentos = guiaWebRepository.findByTituloContaining(titulo);
            if (compartilhamentos.isEmpty()) {
                compartilhamentos = guiaWebRepository.findByDescricaoContaining(titulo);
                if (compartilhamentos.isEmpty()) {
                    compartilhamentos = guiaWebRepository.findAllCompartilhamentos();
                }
            }
            mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
            mv.addObject(LISTAR_COMPARTILHAMENTOS, compartilhamentos);
            mv.addObject("quantidadeCompartilhamentos", guiaWebRepository.totalGuiaWeb());
        } else {
            new ModelAndView("redirect:/guiaweb");
        }
        return mv;
    }

    @GetMapping(value = "detalhesCompartilhamento/{guiaWebId}")
    public ModelAndView detalhesCompartilhamento(@PathVariable(value = "guiaWebId", required = false) long guiaWebId) {

        GuiaWeb guiaWeb = guiaWebRepository.findByGuiaWebId(guiaWebId);
        ModelAndView mv = new ModelAndView("ferramentas/detalhesCompartilhamento");
        mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
        mv.addObject("guiaWeb", guiaWeb);
        return mv;
    }

    @GetMapping(value = "/cadastrarCompartilhamento")
    public ModelAndView cadastroCompartilhamento() {

        ModelAndView mv = null;
        mv = new ModelAndView("ferramentas/cadastrarCompartilhamento");
        mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
        return mv;
    }

    @PostMapping(value = "/cadastrarCompartilhamento")
    public ModelAndView cadastrarCompartilhamento(GuiaWebDTO guiaWeb) {

        ModelAndView mv = new ModelAndView("ferramentas/cadastrarCompartilhamento");
        try {
            guiaWeb.setDataCadastro(new Date());
            guiaWebRepository.save(guiaWeb);
            List<GuiaWeb> compartilhamentos = guiaWebRepository.findAllCompartilhamentos();
            mv.addObject(LISTAR_COMPARTILHAMENTOS, compartilhamentos);
            mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
            return mv;
        } catch (Exception e) {
            log.error(e.getMessage());
            mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
            return mv;
        }
    }
}
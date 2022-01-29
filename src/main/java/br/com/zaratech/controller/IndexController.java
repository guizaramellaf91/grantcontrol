package br.com.zaratech.controller;

import static br.com.zaratech.util.GrantControlConstants.MODAL_INFORMATIVO;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.bean.UsuarioBean;
import br.com.zaratech.model.ParametrosSistema;
import br.com.zaratech.repository.ParametrosRepository;

@Controller
public class IndexController {

    private UsuarioBean usuarioBean;
    private ParametrosRepository parametrosRepository;

    @Autowired
    void handle(UsuarioBean usuarioBean,
            ParametrosRepository parametrosRepository) {
        this.usuarioBean = usuarioBean;
        this.parametrosRepository = parametrosRepository;
    }

    @GetMapping("/")
    public String home() {

        if (!Objects.isNull(usuarioBean)) {
            return "redirect:/index";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping(value = "/login")
    public ModelAndView login() {

        return new ModelAndView("login");
    }

    @GetMapping(value = "/index")
    public ModelAndView index() {

        ParametrosSistema modalInformativo = parametrosRepository.findByChave(MODAL_INFORMATIVO);
        ModelAndView mv = new ModelAndView("index");

        if (!Objects.isNull(usuarioBean.getUsuario())) {

            mv.addObject("modalInformativo", !Objects.isNull(modalInformativo) &&
                    !Objects.isNull(modalInformativo.getChave()) &&
                    modalInformativo.getValor().equals("1"));
            mv.addObject("usuarioLogado", usuarioBean.getUsuario());
            return mv;
        } else {
            return new ModelAndView("login");
        }
    }

    @GetMapping(value = "/logout")
    public String isLogout() {

        return "logout";
    }

    @PostMapping(value = "/logout")
    public String logout() {

        return "logout";
    }
}
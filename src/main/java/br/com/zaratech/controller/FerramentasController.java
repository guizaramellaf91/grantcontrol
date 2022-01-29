package br.com.zaratech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.bean.UsuarioBean;

@Controller
public class FerramentasController {

    private UsuarioBean usuarioBean;

    @Autowired
    void handle(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    @GetMapping("/ferramentas")
    public ModelAndView ferramentas() {

        ModelAndView mv = new ModelAndView("ferramentas/ferramentas");
        if (usuarioBean.getUsuario().getNivel() == 1) {
            mv.addObject("usuarioLogado", usuarioBean.getUsuario());
        } else {
            mv = new ModelAndView("redirect:/index");
        }
        return mv;
    }
}
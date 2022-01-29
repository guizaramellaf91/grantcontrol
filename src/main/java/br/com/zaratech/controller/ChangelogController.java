package br.com.zaratech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.bean.UsuarioBean;
import br.com.zaratech.model.ChangeLog;
import br.com.zaratech.repository.ChangeLogRepository;

@Controller
public class ChangelogController {

    private ChangeLogRepository changeLogRepository;
    private UsuarioBean usuarioBean;

    @Autowired
    private void handle(ChangeLogRepository changeLogRepository,
            UsuarioBean usuarioBean) {
        this.changeLogRepository = changeLogRepository;
        this.usuarioBean = usuarioBean;
    }

    @GetMapping("/changelog")
    public ModelAndView changeLog() {

        List<ChangeLog> changeLogs = changeLogRepository.findAllChangeLogs();
        ModelAndView mv = new ModelAndView("changelog");
        mv.addObject("listarChangeLogs", changeLogs);
        mv.addObject("usuarioLogado", usuarioBean.getUsuario());
        return mv;
    }
}
package br.com.zaratech.service.impl;

import static br.com.zaratech.util.GrantControlConstants.ATIVO_STRING_UM;
import static br.com.zaratech.util.GrantControlConstants.DASH_BOARD_TELA;
import static br.com.zaratech.util.GrantControlConstants.GOOGLE_CHARTS_CLIENTS;
import static br.com.zaratech.util.GrantControlConstants.MODEL_VIEW_CADASTRAR_USUARIO;
import static br.com.zaratech.util.GrantControlConstants.MODEL_VIEW_DETALHES_USUARIO;
import static br.com.zaratech.util.GrantControlConstants.MODEL_VIEW_ESQUECI_MINHA_SENHA;
import static br.com.zaratech.util.GrantControlConstants.MODEL_VIEW_LOGIN;
import static br.com.zaratech.util.GrantControlConstants.MODEL_VIEW_REDIRECT_CADASTRAR_USUARIO;
import static br.com.zaratech.util.GrantControlConstants.MODEL_VIEW_REDIRECT_ESQUECI_MINHA_SENHA;
import static br.com.zaratech.util.GrantControlConstants.MODEL_VIEW_REDIRECT_INDEX;
import static br.com.zaratech.util.GrantControlConstants.MODEL_VIEW_REDIRECT_LOGIN;
import static br.com.zaratech.util.GrantControlConstants.MODEL_VIEW_REDIRECT_USUARIOS;
import static br.com.zaratech.util.GrantControlConstants.PAGE_SIZE_LIST;
import static br.com.zaratech.util.GrantControlConstants.USER_ADMIN;
import static br.com.zaratech.util.GrantControlConstants.USER_EDIT_USER;
import static br.com.zaratech.util.GrantControlConstants.USUARIOS;
import static br.com.zaratech.util.GrantControlConstants.USUARIO_LOGADO;
import static br.com.zaratech.util.GrantControlConstants.USUARIO_USUARIOS;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.zaratech.bean.DtoMaps;
import br.com.zaratech.bean.UsuarioBean;
import br.com.zaratech.dto.TipoDepartamentoDTO;
import br.com.zaratech.dto.UsuarioDTO;
import br.com.zaratech.model.PagerModel;
import br.com.zaratech.model.ParametrosSistema;
import br.com.zaratech.model.TipoDepartamento;
import br.com.zaratech.model.Usuario;
import br.com.zaratech.repository.ParametrosRepository;
import br.com.zaratech.repository.TipoDepartamentoRepository;
import br.com.zaratech.repository.UsuarioRepository;
import br.com.zaratech.security.Scrypt;
import br.com.zaratech.service.UsuarioService;
import br.com.zaratech.util.SendMail;
import br.com.zaratech.util.ValidaPermissoesUsuario;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {

    public static String idRecuperacao;
    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int[] PAGE_SIZES = { 5, 10 };

    private UsuarioBean usuarioBean;
    private UsuarioRepository usuarioRepository;
    private TipoDepartamentoRepository tipoDepartamentoRepository;
    private ParametrosRepository parametrosRepository;
    private SendMail sendMail;

    private ModelAndView mv = new ModelAndView("usuario/usuarios");

    @Autowired
    private DtoMaps dtoMaps;

    @Autowired
    private void handle(UsuarioBean usuarioBean, UsuarioRepository usuarioRepository,
            TipoDepartamentoRepository tipoDepartamentoRepository,
            ParametrosRepository parametrosRepository,
            SendMail sendMail) {

        this.usuarioBean = usuarioBean;
        this.usuarioRepository = usuarioRepository;
        this.tipoDepartamentoRepository = tipoDepartamentoRepository;
        this.parametrosRepository = parametrosRepository;
        this.sendMail = sendMail;
    }

    private ParametrosSistema chartsClients() {
        ParametrosSistema paramSystem = parametrosRepository.findByChave(GOOGLE_CHARTS_CLIENTS);
        return !Objects.isNull(paramSystem) &&
                paramSystem.getValor().equals(ATIVO_STRING_UM) ? paramSystem : null;
    }

    public ModelAndView usuarios(@RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("page") Optional<Integer> page) {

        mv = new ModelAndView(USUARIO_USUARIOS);

        if (ValidaPermissoesUsuario.usuarioAdmin(usuarioBean.getUsuario())) {

            int evalPageSize;
            int evalPag = page.isPresent() ? page.get() - 1 : 0;
            int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : evalPag;

            final ParametrosSistema pageSizeList = parametrosRepository.findByChave(PAGE_SIZE_LIST);
            if (pageSizeList != null && pageSizeList.getChave() != null && pageSizeList.getValor() != null) {
                evalPageSize = pageSize.orElse(Integer.valueOf(pageSizeList.getValor()));
            } else {
                evalPageSize = 5;
            }

            mv.addObject(DASH_BOARD_TELA, !Objects.isNull(chartsClients()));

            usuarioBean.setPageSize(evalPageSize);
            int pag = page.isPresent() ? page.get() : 0;
            usuarioBean.setPage((page.orElse(0) < 1) ? INITIAL_PAGE : pag);

            final ParametrosSistema userEditUser = parametrosRepository.findByChave(USER_EDIT_USER);

            mv.addObject(USER_EDIT_USER, userEditUser != null && userEditUser.getChave() != null
                    && userEditUser.getValor().equals(ATIVO_STRING_UM));

            Page<Usuario> usuarios = usuarioRepository.findAllUsuariosWithoutAdmin(
                    PageRequest.of(evalPage, evalPageSize, Sort.by("ultimoAcesso").descending()));
            PagerModel pager = new PagerModel(usuarios.getTotalPages(), usuarios.getNumber(), BUTTONS_TO_SHOW);
            mv.addObject("selectedPageSize", evalPageSize);
            mv.addObject("pageSizes", PAGE_SIZES);
            mv.addObject("pager", pager);
            mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
            mv.addObject(USUARIOS, usuarios);
            mv.addObject("quantidadeUsuarios", usuarioRepository.totalUsuarios());
            mv.addObject("quantidadeUsuariosAtivos", usuarioRepository.totalUsuariosAtivos());
            mv.addObject("quantidadeUsuariosInativos", usuarioRepository.totalUsuariosInativos());
            return mv;

        } else {
            return new ModelAndView("redirect:/index");
        }
    }

    public ModelAndView buscaUsuario(String nomeUsuario, @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("page") Optional<Integer> page) {

        if (ValidaPermissoesUsuario.usuarioAdmin(usuarioBean.getUsuario())) {

            int evalPageSize;
            int pag = page.isPresent() ? page.get() - 1 : 0;
            int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : pag;

            final ParametrosSistema pageSizeList = parametrosRepository.findByChave(PAGE_SIZE_LIST);
            if (pageSizeList != null && pageSizeList.getChave() != null && pageSizeList.getValor() != null) {
                evalPageSize = pageSize.orElse(Integer.valueOf(pageSizeList.getValor()));
            } else {
                evalPageSize = 5;
            }

            mv.addObject(DASH_BOARD_TELA, !Objects.isNull(chartsClients()));

            usuarioBean.setPageSize(evalPageSize);
            usuarioBean.setPage((page.orElse(0) < 1) ? INITIAL_PAGE : evalPage);

            final ParametrosSistema userEditUser = parametrosRepository.findByChave(USER_EDIT_USER);
            mv.addObject(USER_EDIT_USER, userEditUser != null && userEditUser.getChave() != null
                    && userEditUser.getValor().equals(ATIVO_STRING_UM));

            return returnMV(nomeUsuario, evalPageSize, evalPage);
        } else {
            return new ModelAndView(MODEL_VIEW_REDIRECT_INDEX);
        }
    }

    private ModelAndView returnMV(String nomeUsuario, int evalPageSize, int evalPage) {
        if (!nomeUsuario.isEmpty()) {
            return returnBuscaUsuario(nomeUsuario, mv, evalPageSize, evalPage);
        } else {
            return new ModelAndView(MODEL_VIEW_REDIRECT_USUARIOS);
        }
    }

    private ModelAndView returnBuscaUsuario(String nomeUsuario, ModelAndView mv, int evalPageSize, int evalPage) {
        List<Usuario> usuario = usuarioRepository.findByNomeContaining(nomeUsuario);

        for (Usuario u : usuario) {
            if (u.getLogin().equals(USER_ADMIN)) {
                usuario.remove(u);
            }
        }

        if (!usuario.isEmpty()) {
            Page<Usuario> usuarios = usuarioRepository.findAllUsuariosWithoutAdmin(
                    PageRequest.of(evalPage, evalPageSize, Sort.by("acessos").descending()));
            PagerModel pager = new PagerModel(usuarios.getTotalPages(), usuarios.getNumber(), BUTTONS_TO_SHOW);
            mv.addObject("selectedPageSize", evalPageSize);
            mv.addObject("pageSizes", PAGE_SIZES);
            mv.addObject("pager", pager);
            mv.addObject(USUARIOS, usuarios);
        } else {
            mv.addObject("searchUsuario", true);
            mv.addObject(USUARIOS, usuario);
        }
        mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
        mv.addObject("quantidadeUsuarios", usuarioRepository.totalUsuarios());
        mv.addObject("quantidadeUsuariosAtivos", usuarioRepository.totalUsuariosAtivos());
        mv.addObject("quantidadeUsuariosInativos", usuarioRepository.totalUsuariosInativos());
        return mv;
    }

    public ModelAndView cadastrar() {

        mv = new ModelAndView(MODEL_VIEW_CADASTRAR_USUARIO);

        if (ValidaPermissoesUsuario.usuarioAdmin(usuarioBean.getUsuario())) {

            List<TipoDepartamento> lsTipoDepartamento = tipoDepartamentoRepository.findAllTiposDepartamento();
            mv.addObject("lsTipoDepartamento", lsTipoDepartamento);
            mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
        } else {
            mv = new ModelAndView(MODEL_VIEW_REDIRECT_INDEX);
        }
        return mv;
    }

    public ModelAndView cadastrar(UsuarioDTO usuarioDTO, TipoDepartamentoDTO tipoDepartamentoDTO) {
        
        mv = new ModelAndView();

        try {

            Usuario u = usuarioRepository.findByLogin(usuarioDTO.getLogin());
            if (u == null) {
                u = usuarioRepository.findByEmail(usuarioDTO.getEmail());
            }

            if (u != null && u.getLogin().equals(usuarioDTO.getLogin())) {

                mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
                mv.addObject("msg", "Este login já existe para " + u.getNome());
                return new ModelAndView(MODEL_VIEW_REDIRECT_CADASTRAR_USUARIO);

            } else if (u != null && u.getEmail().equals(usuarioDTO.getEmail())) {

                mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
                mv.addObject("msg", "Este e-mail já existe para " + u.getNome());
                return new ModelAndView(MODEL_VIEW_REDIRECT_CADASTRAR_USUARIO);

            } else {

                usuarioDTO.setSenha(new BCryptPasswordEncoder().encode(usuarioDTO.getSenha()));
                usuarioDTO.setEmail(usuarioDTO.getEmail());
                usuarioDTO.setDepartamento(tipoDepartamentoDTO.getNomeDepartamento());
                Usuario usuario = dtoMaps.modelMapper().map(usuarioDTO, Usuario.class);
                usuarioRepository.save(usuario);
                sendMail.enviar(usuario.getEmail(), 0);
                log.info("\nUsuario " + usuario.getUsuarioId() + " - " + usuario.getNome() + " cadastrado!");
                return new ModelAndView(MODEL_VIEW_REDIRECT_USUARIOS);
            }
        } catch (Exception e) {
            mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
            return new ModelAndView(MODEL_VIEW_REDIRECT_CADASTRAR_USUARIO);
        }
    }

    public ModelAndView detalhesUsuario(long usuarioId) {

        mv = new ModelAndView(MODEL_VIEW_DETALHES_USUARIO);
        Usuario usuario = usuarioRepository.findByUsuarioId(usuarioId);

        if (usuario.getLogin().equals(usuarioBean.getUsuario().getLogin())
                || usuarioBean.getUsuario().getNivel() == 1) {

            if (usuario.getLogin().equals(USER_ADMIN) && !usuarioBean.getUsuario().getLogin().equals(USER_ADMIN)) {
                log.warn("impossivel burlar as regras de seguranca...");
                return new ModelAndView(MODEL_VIEW_LOGIN);
            }

            usuario.setSenha("");
            mv.addObject("usuario", usuario);
            mv.addObject(USUARIO_LOGADO, usuarioBean.getUsuario());
            mv.addObject("lsTipoDepartamento", tipoDepartamentoRepository.findAll());
        } else {
            log.warn("deslogando por regras de seguranca...");
            return new ModelAndView(MODEL_VIEW_LOGIN);
        }
        return mv;
    }

    public ModelAndView alterarUsuario(Long usuarioId, UsuarioDTO usuarioDTO, TipoDepartamentoDTO tipoDepartamentoDTO) {

        mv = new ModelAndView(MODEL_VIEW_REDIRECT_CADASTRAR_USUARIO);

        try {

            Usuario u = usuarioRepository.findByUsuarioId(usuarioId);

            if (!usuarioDTO.getSenha().equals("")) {
                usuarioDTO.setSenha(new BCryptPasswordEncoder().encode(usuarioDTO.getSenha()));
            } else {
                usuarioDTO.setSenha(u.getSenha());
            }

            if (usuarioDTO.getLogin().equals(u.getLogin())) {

                log.warn("Este login já existe!");
                return mv;
            } else {

                usuarioDTO.setDepartamento(tipoDepartamentoDTO.getNomeDepartamento());
                usuarioDTO.setAcessos(u.getAcessos());
                usuarioDTO.setUltimoAcesso(u.getUltimoAcesso());
                usuarioDTO.setDataAlteracao(new Date());
                Usuario usuario = dtoMaps.modelMapper().map(usuarioDTO, Usuario.class);
                usuarioRepository.save(usuario);

                log.info("Usuario " + usuarioDTO.getUsuarioId() + " - " + usuarioDTO.getNome() + " alterado por "
                        + usuarioBean.getUsuario().getNome());

                if (!u.getLogin().equals(usuarioBean.getUsuario().getLogin())) {
                    return new ModelAndView("redirect:/detalhesUsuario?usuarioId=" + usuarioDTO.getUsuarioId());
                } else {
                    return new ModelAndView(MODEL_VIEW_REDIRECT_LOGIN);
                }
            }
        } catch (Exception e) {
            log.error("Falha ao realizar alteracao do usuario! Motivo: " + e.getMessage());
            return mv;
        }
    }

    public ModelAndView recuperarAcesso(String id) {

        mv = new ModelAndView("usuario/recuperarAcesso");
        try {
            log.info("ID Recuperacao: " + idRecuperacao);
            return mv;
        } catch (Exception e) {
            log.error("recuperarAcesso: " + e.getMessage());
            mv = new ModelAndView(MODEL_VIEW_REDIRECT_LOGIN);
            return mv;
        }
    }

    public ModelAndView recuperarAcesso(String id, String senha) {

        Usuario u = null;
        try {
            u = usuarioRepository.findByUsuarioId(Integer.valueOf(Scrypt.decrypt(id)));
            log.info("ID Decrypt: " + u.getUsuarioId());
            u.setSenha(new BCryptPasswordEncoder().encode(senha));
            usuarioRepository.save(u);
            log.info("Senha alterada com sucesso!");

        } catch (NumberFormatException | InvalidKeyException | BadPaddingException | NoSuchPaddingException
                | IllegalBlockSizeException | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

        mv = new ModelAndView(MODEL_VIEW_LOGIN);
        mv.addObject("msg", "Senha alterada com sucesso!");
        return mv;
    }

    public ModelAndView esqueciMinhaSenha() {

        mv = new ModelAndView(MODEL_VIEW_ESQUECI_MINHA_SENHA);

        try {
            return mv;
        } catch (Exception e) {
            log.error("esqueciMinhaSenha: " + e.getMessage());
            mv = new ModelAndView(MODEL_VIEW_REDIRECT_ESQUECI_MINHA_SENHA);
            return mv;
        }
    }

    public ModelAndView esqueciMinhaSenha(String email) {

        if (email != null) {
            Usuario u = usuarioRepository.findByEmail(email);

            if (u != null) {

                log.info("Iniciando recuperacao de senha para " + u.getNome() + " | e-mail: " + u.getEmail());

                try {
                    idRecuperacao = Scrypt.encrypt(String.valueOf(u.getUsuarioId()));
                    log.info("Hash ID: " + idRecuperacao);
                } catch (InvalidKeyException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException
                        | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                        | UnsupportedEncodingException e) {
                    log.error(e.getMessage());
                }
                try {
                    sendMail.enviar(email, 1);
                } catch (UnsupportedEncodingException | MessagingException e) {
                    log.error(e.getMessage());
                }
                mv = new ModelAndView(MODEL_VIEW_LOGIN);
                mv.addObject("msg", "Recuperacao de senha enviada ao e-mail!");

            } else {
                mv = new ModelAndView(MODEL_VIEW_ESQUECI_MINHA_SENHA);
                mv.addObject("msg", "O e-mail não foi encontrado!");
            }
        } else {
            mv = new ModelAndView(MODEL_VIEW_ESQUECI_MINHA_SENHA);
            mv.addObject("msg", "Informe o e-mail para prosseguir!");
        }
        return mv;
    }
}
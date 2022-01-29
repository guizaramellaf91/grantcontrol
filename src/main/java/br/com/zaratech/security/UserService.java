
package br.com.zaratech.security;

import java.util.Date;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.zaratech.bean.UsuarioBean;
import br.com.zaratech.model.Usuario;
import br.com.zaratech.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
public class UserService implements UserDetailsService {

    UsuarioRepository usuarioRepository;
    UsuarioBean usuarioBean;

    @Autowired
    void handle(UsuarioRepository usuarioRepository, UsuarioBean usuarioBean) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioBean = usuarioBean;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException, DataAccessException {

        Usuario usuario = usuarioRepository.findByLogin(login);

        if (!Objects.isNull(usuario)) {

            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null,
                    usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (authentication instanceof Authentication) {
                usuarioBean.setUsuario(usuario);
                if (!usuarioBean.getUsuario().getLogin().equals("admin")) {
                    usuarioBean.getUsuario().setAcessos(usuarioBean.getUsuario().getAcessos() + 1);
                }
                usuarioBean.getUsuario().setUltimoAcesso(new Date());
                usuarioRepository.save(usuarioBean.getUsuario());
            }
            if (!usuario.isAtivo()) {
                log.info("Usuario inativo! Entre em contato com o administrador");
                throw new UsernameNotFoundException("Usuario inativo! Entre em contato com o administrador");
            }
        } else {
            throw new UsernameNotFoundException("Nenhum usuario encontrado!");
        }
        return new User(usuario.getUsername(), usuario.getPassword(), true, true, true, true, usuario.getAuthorities());
    }
}
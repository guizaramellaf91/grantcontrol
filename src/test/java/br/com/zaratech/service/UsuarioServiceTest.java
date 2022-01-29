package br.com.zaratech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@ActiveProfiles("test")
public class UsuarioServiceTest {

    private UsuarioService usuarioService = mock(UsuarioService.class);
    private Optional<Integer> page = Optional.of(1);
    private Optional<Integer> pageSize = Optional.of(5);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void usuariosTest() throws Exception {

        log.info("lista de usuarios com paginacao");

        when(usuarioService.usuarios(pageSize, page)).thenReturn(new ModelAndView());

        assertEquals(page, Optional.of(1));
        assertEquals(pageSize, Optional.of(5));
        verify(usuarioService, times(0)).usuarios(pageSize, page);
    }

    @Test
    public void buscaUsuarioTest() throws Exception {

        log.info("buscar usuario atraves do nome");

        assertEquals(page, Optional.of(1));

        when(usuarioService.buscaUsuario("nome", pageSize, page)).thenReturn(new ModelAndView());
    }

    @Test
    public void detalhesUsuarioTest() throws Exception {

        log.info("detalhes do usuario atraves do id");

        assertEquals(page, Optional.of(1));

        when(usuarioService.detalhesUsuario(anyLong())).thenReturn(new ModelAndView());
    }
}

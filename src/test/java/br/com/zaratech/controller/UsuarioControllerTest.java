package br.com.zaratech.controller;

import br.com.zaratech.grantcontrol.GrantControlApplication;
import br.com.zaratech.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@SpringBootTest(classes = GrantControlApplication.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    UsuarioController usuarioController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).setControllerAdvice(Exception.class).build();
    }

    @Test
    public void deveRetornarSucessoListarUsuarios() throws Exception {

        log.info("Test - listar todos usuarios com paginacao.");

        mockMvc.perform(get("/usuarios")
                        .param("pageSize", "5")
                        .param("page", "1")).andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deveRetornarSucessoBuscarUsuario() throws Exception {

        log.info("Test - buscar usuario atraves do nome com paginacao.");

        mockMvc.perform(get("/buscarUsuario")
                        .param("nomeUsuario", new String())
                        .param("pageSize", "5")
                        .param("page", "1")).andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deveRetornarSucessoCadastrarUsuario() throws Exception {

        log.info("Test - cadastrar usuario.");

        mockMvc.perform(post("/cadastrarUsuario")
                        .param("nomeUsuario", new String())
                        .param("pageSize", "5")
                        .param("page", "1")).andExpect(status().isOk())
                .andReturn();
    }
}
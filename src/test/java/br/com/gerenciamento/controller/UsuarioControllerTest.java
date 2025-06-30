package br.com.gerenciamento.controller;

import br.com.gerenciamento.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Teste 1: Verifica se a pagina de login principal e carregada.
    
    @Test
    void deveCarregarPaginaLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/login"))
                .andExpect(model().attributeExists("usuario"));
    }

    // Teste 2: Verifica se a pagina de cadastro de usuario e carregada.
    
    @Test
    void deveCarregarPaginaCadastro() throws Exception {
        mockMvc.perform(get("/cadastro"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/cadastro"))
                .andExpect(model().attributeExists("usuario"));
    }

    // Teste 3: Verifica se um novo usuario e salvo com sucesso.
    
    @Test
    void deveSalvarUsuarioComSucesso() throws Exception {
        mockMvc.perform(post("/salvarUsuario")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user", "integTestUser")
                .param("email", "integ@test.com")
                .param("senha", "123456")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    // Teste 4: Testa o processo de logout.
    
    @Test
    void deveRealizarLogout() throws Exception {
        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setUser("userLogout");

        mockMvc.perform(post("/logout")
                .sessionAttr("usuarioLogado", usuarioLogado)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("login/login"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(request().sessionAttributeDoesNotExist("usuarioLogado"));
    }
}
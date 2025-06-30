package br.com.gerenciamento.service;

import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.util.Util;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    private ServiceUsuario serviceUsuario;

    /**
     * Teste 1: Tenta salvar um usuario com todos os dados validos.
     * Espera-se que o usuario seja salvo sem lançar excecoes.
     */
    @Test
    void deveSalvarUsuarioValido() throws Exception {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setUser("testeValido");
        novoUsuario.setEmail("validotest@email.com");
        novoUsuario.setSenha("senha123");

        assertDoesNotThrow(() -> serviceUsuario.salvarUsuario(novoUsuario));
    }

    /**
     * Teste 2: Tenta salvar um usuario com um email que ja existe.
     * Espera-se que a excecao EmailExistsException seja lancada.
     */
    @Test
    void naoDeveSalvarUsuarioComEmailExistente() throws Exception {
        // Primeiro, salva um usuario para garantir que o email exista no banco.
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setUser("usuarioExistente");
        usuarioExistente.setEmail("existente@email.com");
        usuarioExistente.setSenha("senha123");
        serviceUsuario.salvarUsuario(usuarioExistente);

        // Agora, tenta salvar um novo usuario com o mesmo email.
        Usuario novoUsuarioComEmailRepetido = new Usuario();
        novoUsuarioComEmailRepetido.setUser("novoUsuario");
        novoUsuarioComEmailRepetido.setEmail("existente@email.com");
        novoUsuarioComEmailRepetido.setSenha("outraSenha");

        assertThrows(EmailExistsException.class, () -> {
            serviceUsuario.salvarUsuario(novoUsuarioComEmailRepetido);
        });
    }

    /**
     * Teste 3: Tenta salvar um usuário com um nome de usuario ('user') invalido (curto).
     * Espera-se que a validacao do modelo lance ConstraintViolationException.
     */
    @Test
    void naoDeveSalvarUsuarioComUserInvalido() {
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setUser("ab"); // Menos de 3 caracteres
        usuarioInvalido.setEmail("invalidouser@email.com");
        usuarioInvalido.setSenha("senha123");

        assertThrows(ConstraintViolationException.class, () -> {
            serviceUsuario.salvarUsuario(usuarioInvalido);
        });
    }

    /**
     * Teste 4: Testa a funcionalidade de login com um usuario e senha corretos.
     * Espera-se que o login seja bem-sucedido e retorne o usuario.
     */
    @Test
    void deveRealizarLoginComSucesso() throws Exception {
        String user = "loginSucesso";
        String senha = "senhaLogin";
        
        // Salva um usuario para o teste de login
        Usuario usuarioParaLogin = new Usuario();
        usuarioParaLogin.setUser(user);
        usuarioParaLogin.setEmail("loginsucesso@email.com");
        usuarioParaLogin.setSenha(senha);
        serviceUsuario.salvarUsuario(usuarioParaLogin);

        // Realiza o login
        Usuario usuarioLogado = serviceUsuario.loginUser(user, Util.md5(senha));

        assertNotNull(usuarioLogado);
        assertEquals(user, usuarioLogado.getUser());
    }
}
package br.com.gerenciamento.repository;

import br.com.gerenciamento.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario criarUsuario(String user, String email, String senha) {
        Usuario usuario = new Usuario();
        usuario.setUser(user);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        return usuarioRepository.save(usuario);
    }
    
    //Teste 1: Testa a busca de um usuario pelo seu email.
 
    @Test
    void deveEncontrarUsuarioPorEmail() {
        String email = "findbyemail@teste.com";
        criarUsuario("findUser", email, "senha123");
        
        Usuario usuarioEncontrado = usuarioRepository.findByEmail(email);
        assertNotNull(usuarioEncontrado);
        assertEquals(email, usuarioEncontrado.getEmail());
    }

    
     //Teste 2: Testa a busca de um usuario para login com credenciais corretas.
     
    @Test
    void deveEncontrarUsuarioParaLogin() {
        String user = "loginUserRepo";
        String senha = "loginSenhaRepo";
        criarUsuario(user, "login@repo.com", senha);

        Usuario usuarioEncontrado = usuarioRepository.buscarLogin(user, senha);
        assertNotNull(usuarioEncontrado);
        assertEquals(user, usuarioEncontrado.getUser());
    }

    
    //Teste 3: Testa a busca por um email que nao existe no banco.
     
    @Test
    void naoDeveEncontrarUsuarioPorEmailInexistente() {
        Usuario usuarioEncontrado = usuarioRepository.findByEmail("emailinexistente@naoexiste.com");
        assertNull(usuarioEncontrado);
    }

    
    //Teste 4: Testa a busca para login com a senha errada.
    
    @Test
    void naoDeveEncontrarUsuarioParaLoginInvalido() {
        String user = "loginUserInvalido";
        String senhaCorreta = "senhaCorreta";
        String senhaErrada = "senhaErrada";
        criarUsuario(user, "logininvalido@repo.com", senhaCorreta);

        Usuario usuarioEncontrado = usuarioRepository.buscarLogin(user, senhaErrada);
        assertNull(usuarioEncontrado);
    }
}
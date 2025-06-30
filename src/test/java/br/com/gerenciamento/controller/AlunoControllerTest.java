package br.com.gerenciamento.controller;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.service.ServiceAluno;
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
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ServiceAluno serviceAluno;
    
    //Teste 1: Verifica se a pagina de insercao de alunos e carregada corretamente.
     
    @Test
    void deveCarregarPaginaInserirAlunos() throws Exception {
        mockMvc.perform(get("/inserirAlunos"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/formAluno"))
                .andExpect(model().attributeExists("aluno"));
    }

    //Teste 2: Verifica se um aluno e salvo com sucesso ao enviar dados validos.
     
    @Test
    void deveInserirAlunoComSucesso() throws Exception {
        mockMvc.perform(post("/InsertAlunos")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("nome", "Aluno Teste Integração")
                .param("matricula", "2025INT1")
                .param("curso", Curso.INFORMATICA.name())
                .param("status", Status.ATIVO.name())
                .param("turno", Turno.NOTURNO.name())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/alunos-adicionados"));
    }

    
    //Teste 3: Verifica se a listagem de alunos eh carregada corretamente.
     
    @Test
    void deveCarregarListagemAlunos() throws Exception {
        mockMvc.perform(get("/alunos-adicionados"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/listAlunos"))
                .andExpect(model().attributeExists("alunosList"));
    }
    
    //Teste 4: Verifica se a pagina de edicao de um aluno e carregada com os dados corretos.
     
    @Test
    void deveCarregarPaginaEditarAluno() throws Exception {
        
        Aluno aluno = new Aluno();
        aluno.setNome("Aluno Para Editar");
        aluno.setMatricula("matEditar");
        aluno.setStatus(Status.ATIVO);
        aluno.setCurso(Curso.DIREITO);
        aluno.setTurno(Turno.NOTURNO);
        serviceAluno.save(aluno);

        mockMvc.perform(get("/editar/" + aluno.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/editar"))
                .andExpect(model().attribute("aluno", aluno));
    }
}
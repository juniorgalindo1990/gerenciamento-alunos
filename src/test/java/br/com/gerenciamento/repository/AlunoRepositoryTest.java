package br.com.gerenciamento.repository;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    private Aluno criarAluno(String nome, String matricula, Status status, Curso curso, Turno turno) {
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setMatricula(matricula);
        aluno.setStatus(status);
        aluno.setCurso(curso);
        aluno.setTurno(turno);
        return alunoRepository.save(aluno);
    }

    //Teste 1: Testa a busca por alunos com status ATIVO.
    
    @Test
    void deveEncontrarAlunosAtivos() {
        criarAluno("Ativo Um", "matAtivo1", Status.ATIVO, Curso.INFORMATICA, Turno.NOTURNO);
        criarAluno("Inativo Um", "matInativo1", Status.INATIVO, Curso.DIREITO, Turno.MATUTINO);
        criarAluno("Ativo Dois", "matAtivo2", Status.ATIVO, Curso.ADMINISTRACAO, Turno.NOTURNO);

        List<Aluno> alunosAtivos = alunoRepository.findByStatusAtivo();
        assertNotNull(alunosAtivos);
        assertFalse(alunosAtivos.isEmpty());
        assertTrue(alunosAtivos.stream().allMatch(a -> a.getStatus() == Status.ATIVO));
    }
    
    //Teste 2: Testa a busca por alunos com status INATIVO.
     
    @Test
    void deveEncontrarAlunosInativos() {
        criarAluno("Ativo Tres", "matAtivo3", Status.ATIVO, Curso.ENFERMAGEM, Turno.MATUTINO);
        criarAluno("Inativo Dois", "matInativo2", Status.INATIVO, Curso.CONTABILIDADE, Turno.NOTURNO);
        criarAluno("Inativo Tres", "matInativo3", Status.INATIVO, Curso.BIOMEDICINA, Turno.MATUTINO);

        List<Aluno> alunosInativos = alunoRepository.findByStatusInativo();
        assertNotNull(alunosInativos);
        assertFalse(alunosInativos.isEmpty());
        assertTrue(alunosInativos.stream().allMatch(a -> a.getStatus() == Status.INATIVO));
    }

        //Teste 3: Testa a busca por nome ignorando maiusculas e minusculas.
     
    @Test
    void deveEncontrarAlunoPorNome() {
        criarAluno("João da Silva", "matJoao", Status.ATIVO, Curso.DIREITO, Turno.NOTURNO);
        criarAluno("Maria da Silva", "matMaria", Status.ATIVO, Curso.INFORMATICA, Turno.MATUTINO);

        List<Aluno> alunosEncontrados = alunoRepository.findByNomeContainingIgnoreCase("silva");
        assertEquals(2, alunosEncontrados.size());
    }

    
    //Teste 4: Testa a funcionalidade basica de salvar e encontrar um aluno.
    
    @Test
    void deveSalvarEEncontrarAluno() {
        Aluno aluno = new Aluno();
        aluno.setNome("Carlos Pereira");
        aluno.setMatricula("matCarlos");
        aluno.setStatus(Status.ATIVO);
        aluno.setCurso(Curso.DIREITO);
        aluno.setTurno(Turno.NOTURNO);
        
        Aluno alunoSalvo = alunoRepository.save(aluno);
        assertNotNull(alunoSalvo.getId());

        Aluno alunoEncontrado = alunoRepository.findById(alunoSalvo.getId()).orElse(null);
        assertNotNull(alunoEncontrado);
        assertEquals("Carlos Pereira", alunoEncontrado.getNome());
    }
}
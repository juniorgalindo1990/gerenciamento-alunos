package br.com.gerenciamento.service;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AlunoServiceTest {

    @Autowired
    private ServiceAluno serviceAluno;

    /**
     * Teste 1: Salvar um aluno com todos os dados válidos.
     * O teste deve passar sem erros.
     */
    @Test
    void deveSalvarAlunoComDadosValidos() {
        // 1. Cenário
        Aluno aluno = new Aluno();
        aluno.setNome("Aluno Válido Teste");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("20250101");

        // 2. Ação
        serviceAluno.save(aluno);

        // 3. Verificação
        // Busca o aluno no banco para confirmar que ele foi salvo com os dados corretos.
        Aluno alunoSalvo = serviceAluno.findByNomeContainingIgnoreCase("Aluno Válido Teste").get(0);
        assertNotNull(alunoSalvo);
        assertEquals("20250101", alunoSalvo.getMatricula());
    }

    /**
     * Teste 2: Tentar salvar um aluno com um campo obrigatório (nome) nulo.
     * O teste deve capturar a exceção ConstraintViolationException.
     */
    @Test
    void naoDeveSalvarAlunoComNomeNulo() {
        // 1. Cenário
        Aluno aluno = new Aluno();
        aluno.setNome(null); // Nome é obrigatório
        aluno.setMatricula("20259988");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);

        // 2. Ação e Verificação
        // Espera-se que o sistema lance uma ConstraintViolationException por causa da validação @NotBlank no modelo Aluno.
        assertThrows(ConstraintViolationException.class, () -> {
            serviceAluno.save(aluno);
        }, "Deveria lançar ConstraintViolationException ao tentar salvar aluno com nome nulo.");
    }

    /**
     * Teste 3: Tentar salvar um aluno com um nome muito curto.
     * A validação no modelo Aluno exige um nome com no mínimo 5 caracteres.
     */
    @Test
    void naoDeveSalvarAlunoComNomeCurto() {
        // 1. Cenário
        Aluno aluno = new Aluno();
        aluno.setNome("Ana"); // Nome com menos de 5 caracteres
        aluno.setMatricula("20250303");
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.DIREITO);
        aluno.setStatus(Status.ATIVO);

        // 2. Ação e Verificação
        // A validação @Size(min=5) no modelo Aluno deve ser acionada.
        assertThrows(ConstraintViolationException.class, () -> {
            serviceAluno.save(aluno);
        }, "Deveria lançar ConstraintViolationException ao tentar salvar com nome curto.");
    }

    /**
     * Teste 4: Verificar se é possível encontrar um aluno pelo seu ID após salvá-lo.
     */
    @Test
    void deveEncontrarAlunoSalvoPorId() {
        // 1. Cenário
        Aluno alunoParaSalvar = new Aluno();
        alunoParaSalvar.setMatricula("20257766");
        alunoParaSalvar.setNome("Mariana Teste ID");
        alunoParaSalvar.setTurno(Turno.MATUTINO);
        alunoParaSalvar.setCurso(Curso.DIREITO);
        alunoParaSalvar.setStatus(Status.ATIVO);
        serviceAluno.save(alunoParaSalvar);

        // Pega o ID do aluno que acabamos de salvar.
        Long idDoAlunoSalvo = serviceAluno.findByNomeContainingIgnoreCase("Mariana Teste ID").get(0).getId();

        // 2. Ação
        Aluno alunoEncontrado = serviceAluno.getById(idDoAlunoSalvo);

        // 3. Verificação
        assertNotNull(alunoEncontrado);
        assertEquals("Mariana Teste ID", alunoEncontrado.getNome());
        assertEquals("20257766", alunoEncontrado.getMatricula());
    }
}
package br.com.gerenciamento.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertTrue;

// Inicia a aplicacao em uma porta aleatoria para o teste

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlunoAcceptanceTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\romil\\eclipse-workspace\\chromedriver-win64\\chromedriver.exe");
        this.driver = new ChromeDriver();
        this.baseUrl = "http://localhost:" + port;
    }

    // Teste de Aceitacao 1: Cadastra um novo aluno e verifica se ele aparece na lista.
    
    @Test
    void deveCadastrarUmNovoAlunoComSucesso() throws InterruptedException {
        
    	// Acao: Abre o navegador e vai para a pagina de inserir alunos
        driver.get(baseUrl + "/inserirAlunos");

        // Preenche o formulario
        driver.findElement(By.name("nome")).sendKeys("Aluno Selenium Teste");
        driver.findElement(By.name("matricula")).sendKeys("SELENIUM01");

        // Seleciona as opcoes nos menus dropdown
        Select cursoSelect = new Select(driver.findElement(By.name("curso")));
        cursoSelect.selectByValue("INFORMATICA");

        Select turnoSelect = new Select(driver.findElement(By.name("turno")));
        turnoSelect.selectByValue("NOTURNO");

        Select statusSelect = new Select(driver.findElement(By.name("status")));
        statusSelect.selectByValue("ATIVO");

        // Clica no botao de salvar
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        
        /*
         * Verificacao: Confirma se foi redirecionado para a pagina de listagem
         * e se o conteudo da pagina contem o nome do aluno cadastrado.
         */
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Aluno Selenium Teste"));
    }

    @AfterEach
    void tearDown() {
        // Fecha o navegador ao final de cada teste
        if (this.driver != null) {
            this.driver.quit();
        }
    }
}
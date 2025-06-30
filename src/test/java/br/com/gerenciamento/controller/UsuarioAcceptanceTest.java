package br.com.gerenciamento.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Inicia a aplicacao em uma porta aleatoria para o teste

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioAcceptanceTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        // Caminho para o seu chromedriver.exe
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\romil\\eclipse-workspace\\chromedriver-win64\\chromedriver.exe");
        this.driver = new ChromeDriver();
        this.baseUrl = "http://localhost:" + port;
    }

    // Teste de Aceitacao 2: Cadastra um novo usuario e depois faz login.
    
    @Test
    void deveCadastrarElogarComNovoUsuario() {
        // --- ETAPA 1: CADASTRO ---

        // Navega para a pagina de cadastro
        driver.get(baseUrl + "/cadastro");

        // Preenche o formulario de cadastro
        
        String user = "selUser" + System.currentTimeMillis(); 
        driver.findElement(By.name("email")).sendKeys(user + "@teste.com");
        driver.findElement(By.name("user")).sendKeys(user);
        driver.findElement(By.name("senha")).sendKeys("senha123");

        // Clica no botao de cadastrar
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // --- ETAPA 2: LOGIN ---

        // Adiciona uma espera explicita para o campo de login aparecer.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement userInputLogin = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("user")));

        // Apos o campo estar visivel, preenche o formulario de login.
        userInputLogin.sendKeys(user);
        driver.findElement(By.name("senha")).sendKeys("senha123");

        // Clica no botao de login
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verificacao: Confirma se o login foi bem-sucedido,
        // verificando o titulo da pagina de boas-vindas.
        String tituloDaPagina = driver.getTitle();
        assertEquals("Sistema de Gerenciamento de Alunos", tituloDaPagina);
    }

    @AfterEach
    void tearDown() {
        // Fecha o navegador ao final do teste
        if (this.driver != null) {
            this.driver.quit();
        }
    }
}
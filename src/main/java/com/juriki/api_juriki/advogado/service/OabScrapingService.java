package com.juriki.api_juriki.advogado.service;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.stereotype.Service;

import com.juriki.api_juriki.advogado.enums.EResultadoValidacaoOab;

import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class OabScrapingService {

    public EResultadoValidacaoOab validarOab(
            String numeroOab,
            String uf) {
    	


        WebDriverManager.chromedriver().setup();

    	ChromeOptions options = new ChromeOptions();
    	WebDriver driver = new ChromeDriver(options);
    	
        try {

            driver.get("https://cna.oab.org.br/");

            WebDriverWait wait =
                    new WebDriverWait(
                            driver,
                            Duration.ofSeconds(20)
                    );

            // CAMPO INSCRIÇÃO
            WebElement campoNumero =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.cssSelector(
                                            "input[name='registration']"
                                    )
                            )
                    );

            campoNumero.sendKeys(numeroOab);

            // SELECT UF
            WebElement selectUf =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.cssSelector(
                                            "select[name='sectional']"
                                    )
                            )
                    );

            Select select =
                    new Select(selectUf);

            select.selectByValue(uf);
            
            
            WebElement selectTipo =
                    wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("select[name='registrationType']")
                        )
                    );

            Select selectTipoInscricao =
                    new Select(selectTipo);

            // Advogado
            selectTipoInscricao.selectByValue("1");

            // BOTÃO PESQUISAR
            WebElement botao =
                    wait.until(
                            ExpectedConditions.elementToBeClickable(
                                    By.cssSelector(
                                            "button[type='submit']"
                                    )
                            )
                    );

            botao.click();
            
            
            Thread.sleep(3000);

            String pagina = driver.getPageSource();

            if (pagina.contains("Não sou um robô")) {

                return EResultadoValidacaoOab.CAPTCHA;
            }
            

            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("ul.pt-8 li")
                )
            );


            // ESPERA RESULTADO
            List<WebElement> resultados = 
                    driver.findElements(By.cssSelector("ul.pt-8 li"));

            for (WebElement resultado : resultados) {

                String texto = resultado.getText();

                if (texto.contains("UF: " + uf)) {

                    JavascriptExecutor js =
                            (JavascriptExecutor) driver;

                    js.executeScript(
                            "arguments[0].click();",
                            resultado
                    );

                    break;
                }
            }
            


            // ESPERA MODAL
            WebElement modal =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.cssSelector(
                                            ".modal-box"
                                    )
                            )
                    );

            // NOME
            String nome =
                    modal.findElement(
                            By.cssSelector(
                                    "p.text-2xl"
                            )
                    ).getText();

            // TIPO INSCRIÇÃO
            String tipoInscricao =
                    modal.findElement(
                            By.xpath(
                                    "//label[contains(text(),'Tipo de Inscrição')]/following-sibling::p"
                            )
                    ).getText();

            // SITUAÇÃO
            String situacao =
                    modal.findElement(
                            By.xpath(
                                    "//*[contains(text(),'SITUAÇÃO')]"
                            )
                    ).getText();

            System.err.println("Nome: " + nome);
            System.err.println("Tipo: " + tipoInscricao);
            System.err.println("Situação: " + situacao);

            // VALIDA TIPO
            if (!tipoInscricao.equalsIgnoreCase("ADVOGADO")) {

                return EResultadoValidacaoOab.INVALIDO;
            }

            // VALIDA SITUAÇÃO
            if (!situacao.contains("REGULAR")) {

                return EResultadoValidacaoOab.INVALIDO;
            }

            return EResultadoValidacaoOab.VALIDO;

        } catch (Exception e) {

            e.printStackTrace();

            return EResultadoValidacaoOab.ERRO;

        } finally {

            driver.quit();
        }
    }
}


package br.com.alura.solucaotabelafip;

import br.com.alura.solucaotabelafip.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SolucaoTabelaFipApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SolucaoTabelaFipApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var principal = new Principal();

        principal.exibeMenu();
    }
}

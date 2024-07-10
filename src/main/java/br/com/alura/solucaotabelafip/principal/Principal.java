package br.com.alura.solucaotabelafip.principal;

import br.com.alura.solucaotabelafip.model.Dados;
import br.com.alura.solucaotabelafip.model.DadosVeiculo;
import br.com.alura.solucaotabelafip.model.Modelos;
import br.com.alura.solucaotabelafip.model.Veiculo;
import br.com.alura.solucaotabelafip.service.ConsumoAPI;
import br.com.alura.solucaotabelafip.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private final String URL_BASE = "https://parallelum.com" +
            ".br/fipe/api/v1/";
    private ConverteDados conversor = new ConverteDados();
    private ConsumoAPI consumoAPI = new ConsumoAPI();

    public void exibeMenu() {
        var menu = """
                ** OPÇÕES **
                Carro
                Moto
                Caminhão
                
                Digite uma das opções para consultar: 
                """;

        System.out.println(menu);

        var opcao =  this.leitura.nextLine();
        String endereco = "https://parallelum.com.br/fipe/api/v1/";

        if(opcao.toLowerCase().contains("carro")) {
            endereco = this.URL_BASE + "carros/marcas/";
        } else if(opcao.toLowerCase().contains("moto")) {
            endereco = this.URL_BASE + "motos/marcas/";
        } else if(opcao.toLowerCase().contains("caminhao")) {
            endereco = this.URL_BASE + "caminhoes/marcas/";
        }

        var json = this.consumoAPI.obterDados(endereco);
        var marcas = this.conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca para consulta: ");
        var codigoMarca = this.leitura.nextLine();

        endereco = endereco + codigoMarca + "/modelos/";
        json = this.consumoAPI.obterDados(endereco);
        var modelosLista = this.conversor.obterDados(json, Modelos.class);
        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite um trecho do carro a ser buscado: ");
        var trechoModelo = this.leitura.nextLine();

        var modelosFiltrados = modelosLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(trechoModelo.toLowerCase()))
                        .toList();

        if(modelosFiltrados.isEmpty()) {
            System.out.println("Não encontrei nenhum carro com esse nome...");
        } else {
            modelosFiltrados.forEach(System.out::println);
        }

        System.out.println("Informe o código do modelo para consulta: ");
        var codigoModelo = this.leitura.nextLine();

        endereco = endereco + codigoModelo + "/anos/";
        json = this.consumoAPI.obterDados(endereco);
        var anosModelo = this.conversor.obterLista(json, Dados.class);

        List<DadosVeiculo> veiculos = new ArrayList<>();

        for (Dados dados : anosModelo) {
            json = this.consumoAPI.obterDados(endereco + dados.codigo());
            var dadoVeiculo = this.conversor.obterDados(json,
                    DadosVeiculo.class);

            veiculos.add(dadoVeiculo);
        }

        veiculos.stream()
                .sorted(Comparator.comparing(DadosVeiculo::anoModelo))
                .forEach(System.out::println);
    }

}


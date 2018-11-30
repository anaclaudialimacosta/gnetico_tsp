/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetico_tsp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author alima
 */
public class Genetico_tsp {
    
   
    
    public static Solucao selecao(ArrayList<Solucao> populacaoDeSolucoes, Grafo g){
       
        Solucao melhorEncontrada = new Solucao();
        
        melhorEncontrada.setCusto(Integer.MAX_VALUE); //Setando em Infinito. 
        
        for(Solucao s: populacaoDeSolucoes){
            s.calculaCusto(g);
            if(s.getCusto()< melhorEncontrada.getCusto()){
                melhorEncontrada.setPercurso(s.getPercurso());
                melhorEncontrada.setCusto(s.getCusto());
            }
        }
        return melhorEncontrada;
        
    } //Essa função tem como objetivo salvar a melhor solução encontrada em uma população de soluções
    
    public static Solucao geradorSolucaoaleatoria(Vertice inicial, Grafo G, int numVertices) {
        Solucao s = new Solucao();
        ArrayList<Vertice> percurso = new ArrayList<>();
        Integer[] jaAconteceu = new Integer[numVertices + 1];
        //System.out.println("Chamei função de Gerador Aleatório");
        for(int i=0;i<numVertices+1;i++){
            jaAconteceu[i] = 0; 
        }  //Inicialização
        int j=0;
        while (percurso.size() < numVertices) {
            //System.out.println("Entrei no While do Gerador Aleatório");
            int indice = (int) (Math.random()* numVertices + 1);
            //System.out.println("Indice Gerado: " +indice );
            if(jaAconteceu[indice]==0 && indice != 0){
                jaAconteceu[indice] =1;
                Vertice v = new Vertice(indice);
                //System.out.println("Vertice Encontrado");
                percurso.add(v);
            } 
        }
        s.setPercurso(percurso);
        return s;

    } //Essa função gera um caminho aleatório com vértices diferentes, mas que necessitam de validação pois não se sabe se os vértices escolhidos são adjacentes

    public static ArrayList<Solucao> construirPopulacaoInicial(float tam, Integer[][] matrizDeAdjacencia, int numVertices, Grafo g) { //O parâmetro tamanho corresponde ao tamanho da população inicial desejada

        ArrayList<Solucao> popInicial = new ArrayList<>();
        int ini = (int) Math.floor(Math.random() * numVertices); //Seleciona um Vertice Aleatório
        //System.out.println("Entrei em Construir Pop Ini");
        Vertice v = new Vertice(ini);

        int contSolucoesValidas = 0;
        while (contSolucoesValidas < tam) {

                Solucao s1 = geradorSolucaoaleatoria(v, g, numVertices);
                s1.validar(matrizDeAdjacencia, g.vertices);
                if(s1.validacao){
                    popInicial.add(s1);
                    contSolucoesValidas++;
                   // System.out.println("SOLUÇÃO VÁLIDA ENCONTRADAAAAAAAAAAAAAAAA");
                }
                
               
        }

        return popInicial;
    }

    public static String[][] lerGrafo() throws FileNotFoundException, IOException {
        String[][] buffer = new String[100][3];
        try (BufferedReader in = new BufferedReader(new FileReader("/C:\\Users\\alima\\Documents/entrada3.txt"))) {
            String str;
            int i = 0;
            while ((str = in.readLine()) != null) {
                String[] campos = str.split("\t");
                buffer[i] = campos;
                i++;
            }
        }
        return buffer;

    }//Essa função gera o Buffer que lê o arquivo de entrada

    public static ArrayList instanciandoListaDeArestas(String[][] buffer) {
        ArrayList<Aresta> ListaDeArestas = new ArrayList<>();
        int quantArestas = Integer.parseInt(buffer[0][1]);
        for (int i = 1; i <= quantArestas; i++) {
            Vertice origem = new Vertice(Integer.parseInt(buffer[i][0]));
            Vertice destino = new Vertice(Integer.parseInt(buffer[i][1]));
            Aresta a = new Aresta(origem, destino, Integer.parseInt(buffer[i][2]));
            ListaDeArestas.add(a);
        }
        return ListaDeArestas;
    }//Esta função retorna uma lista de arestas do grafo

    public static ArrayList instanciandoListaDeVertices(String[][] buffer) {
        ArrayList<Vertice> ListaDeVertices = new ArrayList();
        int quantV = Integer.parseInt(buffer[0][0]);
        for (int i = 1; i <= quantV; i++) {
            Vertice vm = new Vertice(i);
        }
        return ListaDeVertices;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String[][] buffer = new String[100][3];
        ArrayList<Aresta> listaDeArestas = new ArrayList<>();
        ArrayList<Vertice> listaDeVertices = new ArrayList();
        ArrayList<Solucao> popInicial = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        Grafo G = new Grafo();

        //Leitura do Arquivo de Entrada de Representação do Grafo
        buffer = lerGrafo();
        int quantVertices = Integer.parseInt(buffer[0][0]);
        int quantArestas = Integer.parseInt(buffer[0][1]);

        listaDeArestas = instanciandoListaDeArestas(buffer);
        listaDeVertices = instanciandoListaDeVertices(buffer);
        G.arestas = listaDeArestas;
        G.vertices = listaDeVertices;

        //Criar Matriz de Adjacencia para grafos não orientados
        Integer[][] matrizDeAdjacencia = new Integer[quantVertices + 1][quantVertices + 1];
        int i, j;

        for (i = 0; i <= quantVertices; i++) {
            for (j = 0; j <= quantVertices; j++) {
                matrizDeAdjacencia[i][j] = 0;
            }
        }//Inicializando As Posições Da Matriz

        for (Aresta a : listaDeArestas) {
            matrizDeAdjacencia[(a.getOrigem().getId())][(a.getDestino().getId())] = 1;
            matrizDeAdjacencia[(a.getDestino().getId())][(a.getOrigem().getId())] = 1;
        }//Marcando as posições em que os vertices são adjacentes 

        for (i = 1; i <= quantVertices; i++) {
            for (j = 1; j <= quantVertices; j++) {  //A linha 0 e Coluna 0 devem ser descartadas
                System.out.print(matrizDeAdjacencia[i][j]);
            }
            System.out.print("\n");
        }
        //Exibindo Matriz Gerada 

        System.out.println("Digite o tamanho da população inicial: ");
        int tam = scanner.nextInt();

        popInicial = construirPopulacaoInicial(tam, matrizDeAdjacencia, quantVertices, G);
        
       /* if(popInicial.get(0).getPercurso().isEmpty()){
            System.out.println("Percurso Vazios");
        }//Teste  */
       
        System.out.println("Solução Inicial Gerada Com Sucesso");
       
       

    }//Visializando matriz de adjacência construída

}

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
    
    public static final int k = 2;  //Constante K utilizada na Classificação dos Individuos
   
    
    public static Individuo melhorEncontrado(ArrayList<Individuo> populacaoDeSolucoes, Grafo g){
       
        Individuo melhorEncontrada = new Individuo();
        
        melhorEncontrada.setCusto(Integer.MAX_VALUE); //Setando em Infinito. 
        
        for(Individuo s: populacaoDeSolucoes){
            s.calculaCusto(g);
            if(s.getCusto()< melhorEncontrada.getCusto()){
                melhorEncontrada.setPercurso(s.getPercurso());
                melhorEncontrada.setCusto(s.getCusto());
            }
        }
        return melhorEncontrada;
        
    } //Essa função tem como objetivo salvar a melhor solução encontrada em uma população de soluções
    
    public static Individuo geradorSolucaoaleatoria(Vertice inicial, Grafo G, int numVertices) {
        Individuo s = new Individuo();
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

    public static ArrayList<Individuo> construirPopulacaoInicial(float tam, Integer[][] matrizDeAdjacencia, int numVertices, Grafo g) { //O parâmetro tamanho corresponde ao tamanho da população inicial desejada

        ArrayList<Individuo> popInicial = new ArrayList<>();
        int ini = (int) Math.floor(Math.random() * numVertices); //Seleciona um Vertice Aleatório
        //System.out.println("Entrei em Construir Pop Ini");
        Vertice v = new Vertice(ini);

        int contSolucoesValidas = 0;
        while (contSolucoesValidas < tam) {

                Individuo s1 = geradorSolucaoaleatoria(v, g, numVertices);
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
    
   /* public static void calculaFitness(Grafo g, ArrayList<Individuo> populacao){
        
        for(Individuo s : populacao){
            s.calculaCusto(g);
            s.feat = 1/s.getCusto();
        }
        
    }*/
    public static Individuo[] selecaoDosIndividuos(ArrayList<Individuo> populacao, Grafo g){
       
        ArrayList<Individuo> populacaoAuxiliar = new ArrayList<>();
        for(Individuo s: populacao) populacaoAuxiliar.add(s);
               
        /*if(populacao.size()>4){
            k = (int) Math.random()*(4)+ 1; 
        } else {
            k = (int) Math.random()*(populacao.size())+ 1; 
        }*/
        
        Individuo[] selecionados = new Individuo[k];
        for(int i=0;i<k;i++){
            selecionados[i] = melhorEncontrado(populacaoAuxiliar,g);
            populacaoAuxiliar.remove(selecionados[i]);
        }
            
     return selecionados;   
        
    }
    
    public static ArrayList<Individuo> crossover(ArrayList<Individuo> pop, Individuo[] selecionados, Grafo g, Integer[][] matrizDeAdjacencia){
        int quantVertices = g.vertices.size();
        Individuo pai1 = selecionados[0];
        Individuo pai2 = selecionados[1];
        Integer[] mascara = new Integer[quantVertices];
        ArrayList<Individuo> popGerada = new ArrayList<>();
        ArrayList<Vertice> percursoGerado = new ArrayList<>();
        Individuo s1 = new Individuo();
        int contadorSolucoesValidas =0 ;
        for(int i=0;i<quantVertices;i++){
            double prob = Math.random();
            mascara[i] = (prob>=0.5)? 1 : 0;  //operador ternário mais adequado nessa situação
        } //Gerando a mascara
        
        while(pop.size()> contadorSolucoesValidas){
            for(int i=0;i<quantVertices;i++){
                if(mascara[i] ==1) {
                    percursoGerado.add(pai1.getPercurso().get(i).getId(),pai1.getPercurso().get(i) );
                }else{
                    percursoGerado.add(pai2.getPercurso().get(i).getId(),pai2.getPercurso().get(i) );
                }
                s1.setPercurso(percursoGerado);
                s1.validar(matrizDeAdjacencia, percursoGerado);
                if(s1.validacao){
                    popGerada.add(s1);
                    contadorSolucoesValidas++;
                }
            
            }
        }
        
        return popGerada;
        
    }//Cruzamento através de recombinação uniforme
    

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String[][] buffer = new String[100][3];
        ArrayList<Aresta> listaDeArestas = new ArrayList<>();
        ArrayList<Vertice> listaDeVertices = new ArrayList();
        ArrayList<Individuo> pop = new ArrayList<>();
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

        /*for (i = 1; i <= quantVertices; i++) {
            for (j = 1; j <= quantVertices; j++) {  //A linha 0 e Coluna 0 devem ser descartadas
                System.out.print(matrizDeAdjacencia[i][j]);
            }
            System.out.print("\n");
        }
        //Exibindo Matriz Gerada */

        
        System.out.println("Digite o tamanho da população: ");
        int tam = scanner.nextInt();

        pop = construirPopulacaoInicial(tam, matrizDeAdjacencia, quantVertices, G);
        Individuo[] selecionados = new Individuo[k]; 
        
       /* if(popInicial.get(0).getPercurso().isEmpty()){
            System.out.println("Percurso Vazios");
        }//Teste  */
       
        //System.out.println("Solução Inicial Gerada Com Sucesso");
        System.out.println("Digite o número de iterações desejadas: ");
        int numIteracoes = scanner.nextInt();
        
        //Genetic
        for(i=0;i<numIteracoes;i++){
            selecionados = selecaoDosIndividuos(pop , G); //Selecao
            
        }
        
       

    }

}

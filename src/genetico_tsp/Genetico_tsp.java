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
        percurso.add(inicial);
        
        while (percurso.size() < numVertices) {
            System.out.println("Entrei no While do Gerador Aleatório");
            int indice = (int) (Math.random()* numVertices + 1);
            //System.out.println("Indice Gerado: " +indice );
            if(jaAconteceu[indice]==0 && indice != 0){
                jaAconteceu[indice]=1;
                Vertice v = new Vertice(indice);               
                percurso.add(v);
            } 
        }
        s.setPercurso(percurso);
        return s;

    } //Essa função gera um caminho aleatório com vértices diferentes, mas que necessitam de validação pois não se sabe se os vértices escolhidos são adjacentes

    public static ArrayList<Individuo> construirPopulacaoInicial(float tam, Integer[][] matrizDeAdjacencia, int numVertices, Grafo g) { //O parâmetro tamanho corresponde ao tamanho da população inicial desejada

        ArrayList<Individuo> popInicial = new ArrayList<>();
        
        
        int contSolucoesValidas = 0;
        while (contSolucoesValidas < tam) {
                int ini = (int) Math.floor(Math.random() * numVertices+1); //Seleciona um Vertice Aleatório
                System.out.println("Entrei em Construir Pop Ini");
                Vertice v = new Vertice(ini);
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
        int custo;
        for (int i = 1; i <= quantArestas; i++) {
            Vertice origem = new Vertice(Integer.parseInt(buffer[i][0]));
            Vertice destino = new Vertice(Integer.parseInt(buffer[i][1]));
            custo = Integer.parseInt(buffer[i][2]);
            Aresta a = new Aresta(origem, destino, custo);
            ListaDeArestas.add(a);
        }
        return ListaDeArestas;
    }//Esta função retorna uma lista de arestas do grafo

    public static ArrayList instanciandoListaDeVertices(String[][] buffer) {
        ArrayList<Vertice> ListaDeVertices = new ArrayList();
        int quantV = Integer.parseInt(buffer[0][0]);
        for (int i = 1; i <= quantV; i++) {
            Vertice vm = new Vertice(i);
            ListaDeVertices.add(vm);
        }
        return ListaDeVertices;
    }
    
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
        //System.out.println("Entrei no CrossOver");
        int quantVertices = g.vertices.size();
        Individuo pai1 = selecionados[0];
        Individuo pai2 = selecionados[1];
        Integer[] mascara = new Integer[quantVertices];
        ArrayList<Individuo> popGerada = new ArrayList<>();
        ArrayList<Vertice> percursoGerado = new ArrayList<>();
        Individuo s1 = new Individuo();
        int contadorSolucoesValidas =1;
        
        
        for(int i=0;i<quantVertices;i++){
            double prob = Math.random();
            mascara[i] = (prob>=0.5)? 1 : 0;  //operador ternário mais adequado nessa situação
            //System.out.println("Criei a máscara");
        } //Gerando a mascara
        
        popGerada.add(selecionados[0]);//Melhor solução permanece na solução gerada
        while(pop.size()> contadorSolucoesValidas){
            //System.out.println("SOS ENTREEEEEEEI");
            for(int i=0;i<quantVertices;i++){
                if(mascara[i] ==1) {
                    percursoGerado.add(pai1.getPercurso().get(i).getId(),pai1.getPercurso().get(i) );
                }else{
                    percursoGerado.add(pai2.getPercurso().get(i).getId(),pai2.getPercurso().get(i) );
                }
            }
                s1.setPercurso(percursoGerado);
                s1.validar(matrizDeAdjacencia, percursoGerado);
                if(s1.validacao){
                    //System.out.println("A solução gerada é vááááááálida");
                    popGerada.add(s1);
                    contadorSolucoesValidas++;
                }
            
            
        }
        //System.out.println("reotrnei no crossover");
        return popGerada;
        
    }//Cruzamento através de recombinação uniforme
    public static Individuo mutacao(Individuo ind){
        Individuo novoi = new Individuo();
        Integer[] arrayAux= new Integer[ind.getPercurso().size()];
        ArrayList<Vertice> percurso = new ArrayList<>();
        for(int i=0;i<ind.getPercurso().size();i++){
                arrayAux[i]=ind.getPercurso().get(i).getId();
            } // auxilia na troca, o arraylist não é fácil de fazer a troca de posições
        
        while(! novoi.validacao){
            int pos1,pos2;
            pos1 = (int) Math.random() * (ind.getPercurso().size() + 1);
            pos2 = (int) Math.random() * (ind.getPercurso().size() + 1);
            
            int aux = arrayAux[pos1];
            arrayAux[pos1] = arrayAux[pos2];
            arrayAux[pos2] = aux;
            
            for(int i=0;i<ind.getPercurso().size();i++){
                percurso.add(new Vertice(arrayAux[i]));
            } //criando percurso para novo individuo
            
           novoi.setPercurso(percurso);
        }
        return novoi;
        
    }//Essa função retorna um individuo que sofreu mutação, mas não valida se o novo individuo ainda possui um percurso válido, isso deve ser feito no main

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String[][] buffer = new String[100][3];
        Individuo[] selecionados = new Individuo[k]; 

        
        ArrayList<Aresta> listaDeArestas = new ArrayList<>();
        ArrayList<Vertice> listaDeVertices = new ArrayList();
        ArrayList<Individuo> pop = new ArrayList<>();
        ArrayList<Individuo> popGerada = new ArrayList<>();
        
        Scanner scanner = new Scanner(System.in);
        
        Grafo G = new Grafo();
        
        double probMutacao;
        //Leitura do Arquivo de Entrada de Representação do Grafo
  
        buffer = lerGrafo();
        int quantVertices = Integer.parseInt(buffer[0][0]);
        int quantArestas = Integer.parseInt(buffer[0][1]);
      
        listaDeArestas = instanciandoListaDeArestas(buffer);
        listaDeVertices = instanciandoListaDeVertices(buffer);
        G.arestas = listaDeArestas;
        G.vertices = listaDeVertices;
        
       // for(Aresta a: G.arestas) System.out.println("Info da Aresta: " + a.getOrigem().getId() +  "  " + a.getDestino().getId()+ "  "  + a.getCusto());

        //Criar Matriz de Adjacencia para grafos não orientados
        Integer[][] matrizDeAdjacencia = new Integer[quantVertices + 1][quantVertices + 1];
        int i, j;

        for (i = 0; i <= quantVertices; i++) {
            for (j = 0; j <= quantVertices; j++) {
                matrizDeAdjacencia[i][j] = 0;
            }
       }//Inicializando As Posições Da Matriz

        for (Aresta a : G.arestas) {
            matrizDeAdjacencia[(a.getOrigem().getId())][(a.getDestino().getId())] = 1;
            matrizDeAdjacencia[(a.getDestino().getId())][(a.getOrigem().getId())] = 1;
            
        }//Marcando as posições em que os vertices são adjacentes 
        //GRAFO NÃO ORIENTADO POIS MARCA TANTO DA ORIGEM PRO DESTINO QUANTO DO DESTINO PARA ORIGEM

        /*for (i = 1; i <= quantVertices; i++) {
            for (j = 1; j <= quantVertices; j++) {  //A linha 0 e Coluna 0 devem ser descartadas
                System.out.print(matrizDeAdjacencia[i][j]);
            }
            System.out.print("\n");
        }
        //Exibindo Matriz Gerada */

        
        System.out.println("Digite o tamanho da população: ");
        int tam = scanner.nextInt();
  
       
        System.out.println("Digite o número de iterações desejadas: ");
        int numIteracoes = scanner.nextInt();
        System.out.println("Digite a porcentagem da taxa de mutação");
        int  taxaMutacao = scanner.nextInt();
        
        
        pop = construirPopulacaoInicial(tam, matrizDeAdjacencia, quantVertices, G); //Construir População Inicial

         
        //Genetic
        for(i=0;i<numIteracoes;i++){
            
            System.out.println(i+"iteração");
            selecionados = selecaoDosIndividuos(pop , G); //Avalia e Seleciona os Individuos
            popGerada = crossover(pop, selecionados, G, matrizDeAdjacencia);
            probMutacao = Math.random();
            
            if(probMutacao<(taxaMutacao/100)){ //probabilidade de 20% de ter uma mutação
                Individuo novo = new Individuo();
                int indMutada = (int) Math.random()* popGerada.size()+1;
                novo = mutacao(popGerada.get(indMutada));
                novo.validar(matrizDeAdjacencia, listaDeVertices);
                if(novo.validacao){
                  System.out.println("Ocorreu Mutação");
                  popGerada.add(novo);
                  popGerada.remove(indMutada);
                }//A nova solução só será realmente alterada se for válida           
            }
            
            pop.clear();
            for(Individuo ii: popGerada) pop.add(ii);
           
        }
        
      Individuo sx = melhorEncontrado(pop,G); 
      
      System.out.println("-------------MELHOR SOLUÇÃO-----------------------\n");
      for(i=0;i<sx.getPercurso().size();i++){
          System.out.print(sx.getPercurso().get(i).getId());
          
      }
      //for(Aresta a: G.arestas) System.out.println("Info da Aresta: " + a.getOrigem().getId() +  "  " + a.getDestino().getId()+ "  "  + a.getCusto());
      sx.calculaCusto(G);
      System.out.println("Custo: "+ sx.getCusto());
      
        
       

    }

}

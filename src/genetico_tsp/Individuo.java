/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetico_tsp;

import java.util.ArrayList;

/**
 *
 * @author alima
 */
public class Individuo {
    
    
    private int custo;
    private ArrayList<Vertice> percurso;
    public boolean validacao;
    public float feat; //Era pra ser fitness, mas eu escrevi assim incialmente e agora to com preguiça de trocar
    
    public Individuo() {
        this.percurso= new ArrayList<>();
        this.validacao = false;
        this.feat = 0; //Esse ponto do código o limita a uma entrada com no máximo 200 vértices
        
    }

    public int getCusto() {
        return custo;
    }
    public int calculaCusto(Grafo g) {
        int cust =0;
        
       for(int i=0; i<this.percurso.size()-1;i++){
           Vertice u = this.percurso.get(i);
           Vertice v = this.percurso.get(i+1);
           for(Aresta a: g.arestas){
               Vertice v1 = a.getOrigem();
               Vertice v2 = a.getDestino();
               if(((v1.getId()==u.getId())&&(v2.getId()==v.getId())) || ((v1.getId()==v.getId())&&(v2.getId()==u.getId())) ){
                   cust = cust + a.getCusto();
                   //System.out.println("entrei aaaaaaqui");//não está entrando aqui
                   break;
               }
           }
       }
       
       //AQUI AINDA FALTA SOMAR O CUSTO DA ÚLTIMA ARESTA PARA VOLTAR PARA A PRIMEIRA
       
       this.custo = cust;
       return cust;
    }

    public void setCusto(int custo) {
        this.custo = custo;
    }

    public ArrayList<Vertice> getPercurso() {
        return percurso;
    }

    public void setPercurso(ArrayList<Vertice> percurso) {
        this.percurso = percurso;
    }
    
    public void validar(Integer[][] matrizDeAdjacencia, ArrayList<Vertice> listaDeVertices){
        /* Essa função foi criada para validar uma solução
        Na geração da solução já sabemos que os elementos formados serão diferentes
        E sabemos que a solução possui todos os vértices
        Mas não sabemos se o caminho realmente existe, pois não verificamos a adjacência dos caminhos encontrados 
        Desta forma, a validação será feita par a par
        Todas as soluções antes de serem assumidas como válidas  devem obrigatoriamente passar por esta validação*/
        
         boolean isValid = true;
         Integer[] representacaoCaminho = new Integer[this.getPercurso().size()];
         for(int i=0;i<this.getPercurso().size();i++){
             representacaoCaminho[i] = this.percurso.get(i).getId();
             
         }//copiar os ids dos vértices do percurso em ordem para um vetor de inteiros
         for(int i=0;i<this.getPercurso().size()-1;i++){
             
             int vi = representacaoCaminho[i];
             int vj = representacaoCaminho[i+1];
             
             if(vi==vj) isValid = false;
             
             if(matrizDeAdjacencia[vi][vj] != 1){ //Algum vértice não é  adjacente ao seu próximo?
                 if(matrizDeAdjacencia[representacaoCaminho[this.getPercurso().size()-1]][0] != 1){//O último não é adjacente ao primeiro?
                     //System.out.println("Solução inválida, Keep Trying");
                     isValid = false;
                 }
                 
             }
         }
         
         this.validacao = isValid;
         
    } 
    
}

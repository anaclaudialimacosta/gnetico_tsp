/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetico_tsp;

/**
 *
 * @author alima
 */
public class Aresta {
    private Vertice origem;
    private Vertice destino;
    private int custo;

    public Aresta(Vertice origem, Vertice destino, int custo) {
        this.origem = origem;
        this.destino = destino;
        this.custo = custo;
    }

    public Vertice getOrigem() {
        return origem;
    }

    public void setOrigem(Vertice origem) {
        this.origem = origem;
    }

    public Vertice getDestino() {
        return destino;
    }

    public void setDestino(Vertice destino) {
        this.destino = destino;
    }

    public int getCusto() {
        return custo;
    }

    public void setCusto(int custo) {
        this.custo = custo;
    }
    
            
}

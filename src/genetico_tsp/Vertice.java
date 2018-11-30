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
public class Vertice {
    private int id;
    public boolean visitado;
    
    
    public Vertice(int id) {
        this.id = id;
        this.visitado=false;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

   
    
}

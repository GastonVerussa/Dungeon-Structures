package estructurasGenerales.propositoEspecifico;

import estructurasGenerales.lineales.Cola;

public class NodoCP {
    
    private final Comparable prioridad;
    private final Cola items;
    private NodoCP siguienteNodo;
    
    public NodoCP(Comparable prioridad, NodoCP siguienteNodo){
        this.prioridad = prioridad;
        this.items = new Cola();
        this.siguienteNodo = siguienteNodo;
    }
    
    public Comparable getPrioridad(){
        return this.prioridad;
    }
    
    public Cola getItems(){
        return this.items;
    }
    
    public NodoCP getSiguienteNodo(){
        return this.siguienteNodo;
    }
    
    public void setSiguienteNodo(NodoCP siguienteNodo){
        this.siguienteNodo = siguienteNodo;
    }
}

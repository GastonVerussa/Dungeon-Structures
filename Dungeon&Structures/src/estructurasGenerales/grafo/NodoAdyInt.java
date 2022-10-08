package estructurasGenerales.grafo;

public class NodoAdyInt {
    
    private NodoVertInt vertice;
    private int etiqueta;
    private NodoAdyInt sigAdyacente;
    
    public NodoAdyInt(NodoVertInt vertice, int etiqueta, NodoAdyInt sigAdyacente){
        this.vertice = vertice;
        this.etiqueta = etiqueta;
        this.sigAdyacente = sigAdyacente;
    }
    
    public NodoVertInt getVertice(){
        return this.vertice;
    }
    
    public NodoAdyInt getSigAdyacente(){
        return this.sigAdyacente;
    }
    
    public int getEtiqueta(){
        return this.etiqueta;
    }
    
    public void setVertice(NodoVertInt vertice){
        this.vertice = vertice;
    }
    
    public void setSigAdyacente(NodoAdyInt sigAdyacente){
        this.sigAdyacente = sigAdyacente;
    }
    
    public void setEtiqueta(int etiqueta){
        this.etiqueta = etiqueta;
    }
}

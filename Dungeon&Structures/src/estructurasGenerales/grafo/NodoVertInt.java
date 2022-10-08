package estructurasGenerales.grafo;

public class NodoVertInt {
    
    private Object elem;
    private NodoVertInt sigVertice;
    private NodoAdyInt primerAdy;
    
    public NodoVertInt(Object elem, NodoVertInt sigVertice){
        this.elem = elem;
        this.sigVertice = sigVertice;
        this.primerAdy = null;
    }
    
    public Object getElem(){
        return this.elem;
    }
    
    public NodoVertInt getSigVertice(){
        return this.sigVertice;
    }
    
    public NodoAdyInt getPrimerAdy(){
        return this.primerAdy;
    }
    
    public void setElem(Object elem){
        this.elem = elem;
    }
    
    public void setSigVert(NodoVertInt sigVertice){
        this.sigVertice = sigVertice;
    }
    
    public void setPrimerAdy(NodoAdyInt primerAdy){
        this.primerAdy = primerAdy;
    }
}

package estructurasGenerales.propositoEspecifico;

public class NodoHashDicc {
    
    private Object clave;
    private Object dato;
    private NodoHashDicc enlace;
    
    public NodoHashDicc(Object clave, Object dato, NodoHashDicc enlace){
        this.clave = clave;
        this.dato = dato;
        this.enlace = enlace;
    }
    
    public Object getClave(){
        return this.clave;
    }
    
    public Object getDato(){
        return this.dato;
    }
    
    public NodoHashDicc getEnlace(){
        return this.enlace;
    }
    
    public void setClave(Object clave){
        this.clave = clave;
    }
    
    public void setDato(Object dato){
        this.dato = dato;
    }
    
    public void setEnlace(NodoHashDicc enlace){
        this.enlace = enlace;
    }
}

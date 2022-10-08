package estructurasGenerales.propositoEspecifico;

import estructurasGenerales.lineales.Lista;

public class NodoAVLMapeoM {
    
    private Comparable dominio;
    private Lista rango;
    private NodoAVLMapeoM izquierdo;
    private NodoAVLMapeoM derecho;
    private int altura;

    public NodoAVLMapeoM(Comparable dominio, Object elemRango){
        this.dominio = dominio;
        rango = new Lista();
        rango.insertar(elemRango, 1);
        izquierdo = null;
        derecho = null;
        altura = 0;
    }

    public Comparable getDominio() {
        return dominio;
    }

    public void setDominio(Comparable dominio) {
        this.dominio = dominio;
    }

    public Lista getRango() {
        return rango;
    }

    public boolean agregarRango(Object elemRango) {
        boolean exito = (rango.localizar(elemRango) <= 0);
        if(exito)rango.insertar(elemRango, 1);
        return exito;
    }

    public boolean eliminarRango(Object elemRango){
        int posicion = rango.localizar(elemRango);
        boolean exito = (posicion > 0);
        if(exito)rango.eliminar(posicion);
        return exito;
    }
    
    public void setRango(Lista rango){
        this.rango = rango;
    }
    
    public boolean rangoVacio(){
        return rango.esVacia();
    }
    
    public NodoAVLMapeoM getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoAVLMapeoM izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoAVLMapeoM getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoAVLMapeoM derecho) {
        this.derecho = derecho;
    }
    
    public int getAltura(){
        return altura;
    }
    
    public void recalcularAltura(){
        
        this.altura = 0;
        
        if(this.izquierdo != null){
            this.altura = this.izquierdo.getAltura() + 1;
        }
        
        if(this.derecho != null){
            if(this.derecho.getAltura() >= this.altura)
                this.altura = this.derecho.getAltura() + 1;
        }
    }
}

package clases.unidades;

public class CopiaItem {
    
    private final String codigo;
    private final String nombre;
    private final int precio;
    private int puntosAtk;
    private int puntosDef;

    public CopiaItem(String codigo, String nombre, int precio, int puntosAtk, int puntosDef) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.puntosAtk = puntosAtk;
        this.puntosDef = puntosDef;
    }

    public String getCodigo(){
        return codigo;
    }
    
    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public int getPuntosAtk() {
        return puntosAtk;
    }
    
    public int getPuntosDef() {
        return puntosDef;
    }
    
    public void desgastar(){
        puntosAtk -= 10;
        if(puntosAtk < 0){
            puntosAtk = 0;
        }
        puntosDef -= 10;
        if(puntosDef < 0){
            puntosDef = 0;
        }
    }
    
    public boolean estaRoto(){
        return puntosAtk == 0 && puntosDef == 0;
    }
    
    public String toStringCompleto(){
        String resultado;
        
        resultado = nombre;
        resultado += ", Codigo: " + codigo;
        resultado += ", Precio: " + precio + ", Puntos de ataque: " + puntosAtk;
        resultado += ", Puntos de defensa: " + puntosDef;
        
        return resultado;
    }
    
    @Override
    public String toString(){
        
        String resultado;
        
        resultado = nombre; //"Copia Item " + nombre;
        //resultado += ", Codigo: " + codigo;
        //resultado += ", Precio: " + precio + ", Puntos de ataque: " + puntosAtk;
        //resultado += ", Puntos de defensa: " + puntosDef;
        
        return resultado;
    }
}

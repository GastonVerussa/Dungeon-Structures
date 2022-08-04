package clases.unidades;

public class Item implements Comparable<Item>{
    
    private final String codigo;
    private String nombre;
    private int precio;
    private int puntosAtk;
    private int puntosDef;
    private int cantCopiasDisp;

    public Item(String codigo, String nombre, int precio, int puntosAtk, int puntosDef, int cantCopiasDisp){
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.puntosAtk = puntosAtk;
        this.puntosDef = puntosDef;
        this.cantCopiasDisp = cantCopiasDisp;
    }
    
    //  Constructor auxiliar que servira para crear items con el unico proposito de comparar el precio
    //      para listar items entre rangos de precios.
    public Item(int precio){
        //  Codigo con el String vacio, ya que el codigo afecta en la comparacion y
        //      el String vacio "" es el menor valor posible de un String
        this.codigo = "";
        this.precio = precio;
    }

    public String getCodigo(){
        return codigo;
    }
    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public int getPrecio(){
        return precio;
    }

    public void setPrecio(int precio){
        this.precio = precio;
    }

    public int getPuntosAtk(){
        return puntosAtk;
    }

    public void setPuntosAtk(int puntosAtk){
        this.puntosAtk = puntosAtk;
    }

    public int getPuntosDef(){
        return puntosDef;
    }

    public void setPuntosDef(int puntosDef){
        this.puntosDef = puntosDef;
    }

    public int getCantCopiasDisp(){
        return cantCopiasDisp;
    }
    
    public boolean restarCopia(){
        boolean exito;
        
        if(cantCopiasDisp == 0){
            exito = false;
        } else {
            exito = true;
            cantCopiasDisp--;
        }
        
        return exito;
    }
    
    public void setCantCopiasDisp(int cantCopiasDisp){
        this.cantCopiasDisp = cantCopiasDisp;
    }
    
    public CopiaItem conseguirCopia(){
        return new CopiaItem(codigo, nombre, precio, puntosAtk, puntosDef);
    }
    
    public String getNombreCompuesto(){
        return codigo + "(" + nombre + ")";
    }
    
    public String toStringCompleto(){
        String resultado;
        
        resultado = "Item " + nombre;
        resultado += ", Codigo: " + codigo;
        resultado += ", Precio: " + precio + ", Puntos de ataque: " + puntosAtk;
        resultado += ", Puntos de defensa: " + puntosDef;
        resultado += ", Copias: " + cantCopiasDisp;
        
        return resultado;
    }
    
    @Override
    public String toString(){
        
        String resultado;
        
        resultado = "Item " + nombre;
        resultado += ", Precio: " + precio;
        resultado += ", Copias: " + cantCopiasDisp;
        
        return resultado;
    }
    
    //  Implementa la funcion para comparar de la interfaz Comparable
    @Override
    public int compareTo(Item objeto){
        int resultado = precio - objeto.getPrecio();
        //  Si son del mismo precio, debemos diferenciarlos, ya que el arbol AVL no
        //      acepta repetidos, por lo que no podria haber dos items del mismo precio
        if(resultado == 0){
            //  Los diferenciamos por el codigo, ya que sabemos que es unico
            resultado = codigo.compareTo(objeto.getCodigo());
        }
        return resultado;
    }
}

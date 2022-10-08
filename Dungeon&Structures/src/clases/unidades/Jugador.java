package clases.unidades;

import estructurasGenerales.lineales.Lista;
import estructurasGenerales.propositoEspecifico.Conjunto;
import utiles.Aleatorio;

public class Jugador{
    
    private final String nombre;
    //  Posibilidades tipo: defensor 'D', guerrero 'G'
    private char tipo;
    private static Conjunto tipos = new Conjunto();
    private int dinero;
    private final Lista items;
    private int salud;
    private static final int SALUD_INICIAL = 100;
    //  Posibilidades categoria: novato 'N', aficionado 'A' o profesional 'P'
    private char categoria;
    private static Lista categorias = new Lista();
    private int cantDerrotas, cantVictorias;
    // private Equipo equipoActual;
    
    public Jugador(String nombre, String tipo, String categoria, int dinero){
        this.nombre = nombre;
        char tipoChar = tipo.toUpperCase().charAt(0);
        if(tipos.pertenece(tipoChar)){
            this.tipo = tipoChar;
        } else {
            this.tipo = 'D';
        }
        char categoriaChar = categoria.toUpperCase().charAt(0);
        if(categorias.localizar(categoriaChar) > 0){
            this.categoria = categoriaChar;
        } else {
            this.categoria = 'N';
        }
        this.dinero = dinero;
        this.salud = SALUD_INICIAL;
        this.cantDerrotas = 0;
        this.cantVictorias = 0;
        this.items = new Lista();
    }
    
    public Jugador(String nombre, char tipo, char categoria, int dinero){
        this.nombre = nombre;
        this.tipo = tipo;
        this.categoria = categoria;
        this.dinero = dinero;
        this.salud = SALUD_INICIAL;
        this.cantDerrotas = 0;
        this.cantVictorias = 0;
        this.items = new Lista();
    }
    
    public static void setDatosEstaticos(){
        tipos.agregar('D');
        tipos.agregar('G');
        categorias.insertar('N', 1);
        categorias.insertar('A', 2);
        categorias.insertar('P', 3);
    }

    public String getNombre(){
        return this.nombre;
    }
    
    public char getTipo(){
        return this.tipo; 
    }
    
    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public char getCategoria() {
        return categoria;
    }

    public void setCategoria(char categoria) {
        this.categoria = categoria;
    }

    public int getDinero() {
        return dinero;
    }

    public void modificarDinero(int cantidad) {
        dinero += cantidad;
        if(dinero < 0){
            dinero = 0;
        }
    }

    public int getSalud() {
        return salud;
    }

    public void setSalud(int salud) {
        this.salud = salud;
    }
    
    public void daniar(int cantidad){
        this.salud -= cantidad;
        if(this.salud < 0){
            salud = 0;
        }
    }

    public int getCantDerrotas() {
        return cantDerrotas;
    }

    public void sumarDerrota() {
        this.cantDerrotas++;
    }

    public int getCantVictorias() {
        return cantVictorias;
    }

    public void sumarVictoria() {
        this.cantVictorias++;
    }

    public Lista getItems() {
        return items;
    }

    //  O(1)
    public void insertarItem(CopiaItem item) {
        this.items.insertar(item, 1);
    }
    
    public boolean esDerrotado(){
        return this.salud == 0;
    }
    
    public int calcularAtk(){
        
        int resultado;
        
        if(tipo == 'D'){
            resultado = 25;
        } else {
            resultado = 100;
        }
        
        resultado *= categorias.localizar(categoria) + 3;
        
        for(int i = 1; i <= items.longitud(); i++){
            resultado += ((CopiaItem) items.recuperar(i)).getPuntosAtk();
        } 
        
        //  Como el metodo de Aleatorio debe ir entre rangos enteros lo llama entre 1 y 3
        Double aux = new Double(Aleatorio.doubleAleatorio(1, 3));
        //  Y luego lo divide por 2 (Siendo entonces entre 0.5 y 1.5)
        aux = aux/2;
        //  Lo multiplica por el resultado y lo guarda
        aux = resultado*aux;
        resultado = aux.intValue();
        
        return resultado;
    }
    
    public int calcularDef(){
        
        int resultado;
        
        if(tipo == 'D'){
            resultado = 90;
        } else {
            resultado = 25;
        }
        
        resultado *= categorias.localizar(categoria) + 3;
        
        for(int i = 1; i <= items.longitud(); i++){
            resultado += ((CopiaItem) items.recuperar(i)).getPuntosDef();
        }
        
        return resultado;
    }
    
    //  Orden N 2?? Se podra reducir? Quizas cambiando el clon de lista?
    public void desgastarItems(){
        
        int posActual = 1;
        while(posActual <= items.longitud()){
            CopiaItem aux = (CopiaItem) items.recuperar(posActual);
            aux.desgastar();
            if(aux.estaRoto()){
                System.out.println("Item " + aux.getNombre() + " de jugador " + nombre + " se ha roto.");
                items.eliminar(posActual);
            } else {
                posActual++;
            }
        }
    }
    
    public void restablecerSalud(){
        salud = SALUD_INICIAL;
    }
    
    public String toStringCompleto(){
        return this.toString();
    }
    
    @Override
    public String toString(){
        String resultado;
        resultado = "(";
        resultado += nombre + ": ";
        if(tipo == 'D'){
            resultado += "Defensor, ";
        } else {
            resultado += "Guerrero, ";
        }
        if(categoria == 'N'){
            resultado += "Novato, ";
        } else {
            if(categoria == 'A'){
                resultado += "Aficionado, ";
            } else {
                resultado += "Profesional, ";
            }
        }
        resultado += "$" + dinero + ", ";
        if(items.esVacia()){
            resultado += "Items: sin items, ";
        } else {
            resultado += "Items: " + items.toString() +  ", ";
        }
        resultado += "Salud: " + salud + ", ";
        resultado += "Derrotas: " + cantDerrotas + ", ";
        resultado += "Victorias: " + cantVictorias;
        resultado += ")";
        
        return resultado;
    }
}

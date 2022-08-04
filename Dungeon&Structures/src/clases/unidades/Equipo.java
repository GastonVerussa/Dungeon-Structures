package clases.unidades;

import clases.estructuras.Mapa;

public class Equipo {
    
    private final String nombre;
    //  Posibilidades categoria: novato 'N', aficionado 'A' o profesional 'P'
    private char categoria;
    private static final char[] categorias = {'N', 'A', 'P'}; 
    private String locacionActual;
    //  Arreglo con jugadores, al saber la cantidad de jugadores maxima, un arreglo es un
    //      poco mas eficiente que una lista y nunca desperdiciara mucho espacio de memoria
    private Jugador[] participantes;
    //  Variable para saber la cantidad de participantes
    private int cantParticipantes;
    //  Variable estatica que todos los equipos comparten para tener acceso al mapa
    private static Mapa mapaGeneral;
    
    public Equipo(String nombre){
        this.nombre = nombre;
        participantes = new Jugador[3];
        cantParticipantes = 0;
    }
    
    public Equipo(String nombre, Jugador participante1, Jugador participante2, Jugador participante3){
        this.nombre = nombre;
        participantes = new Jugador[3];
        cantParticipantes = 3;
        participantes[0] = participante1;
        participantes[1] = participante2;
        participantes[2] = participante3;
        //  Calculamos su categoria
        this.calcularCategoria();
        //  Lo ingresamos a una locacion al azar
        this.locacionActual = mapaGeneral.obtenerLocacionAleatoria(); 
    }

    //  Funcion estatica para setearle el mapa a todos los equipos
    public static void setMapa(Mapa map){
        mapaGeneral = map;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public char getCategoria() {
        return categoria;
    }

    public String getLocacionActual() {
        return locacionActual;
    }

    public void setLocacionActual(String locacionActual) {
        this.locacionActual = locacionActual;
    }

    public Jugador[] getParticipantes() {
        return participantes;
    }
    
    /*public boolean agregarJugador(Jugador participante){
    
    boolean exito = false;
    
    //  Mientras haya espacio
    if(!this.esCompleto()){
    //  Exito es true
    exito = true;
    //  Aumenta la cantidad de participantes
    cantParticipantes++;
    //  Y guarda el jugador donde corresponda
    participantes[cantParticipantes] = participante;
    //  Si con este jugador se completo el equipo
    if(this.esCompleto()){
    //  Calculamos su categoria
    this.calcularCategoria();
    //  Lo ingresamos a una locacion al azar
    this.locacionActual = mapaGeneral.obtenerLocacionAleatoria();
    }
    }
    
    return exito;
    }
    
    public boolean esCompleto(){
    return cantParticipantes >= 3;
    }*/
    
    private void calcularCategoria(){
        //  Al unirse en orden por cola de prioridad, el ultimo participantes siempre
        //      será el de menor categoria
        categoria = participantes[2].getCategoria();
        
        /*
        categoria = participantes[0].getCategoria();
        
        char aux = participantes[1].getCategoria();
        
        if(categoria == 'P'){
            if(aux != 'P'){
                categoria = aux;
            }
        } else {
            if(categoria == 'A'){
                if(aux == 'N'){
                    categoria = aux;
                }
            }
        }
        
        //////////////////////////////////
        
        categoria = participantes[0].getCategoria();
        
        char aux = participantes[1].getCategoria();
        
        if(categoria != 'N'){
            if(categoria == 'P'){
                categoria = aux;
            } else {
                if(aux == 'N'){
                    categoria = aux;
                }
            }
        
            char aux = participantes[2].getCategoria();
        
            if(categoria != 'N'){
                if(categoria == 'P'){
                    categoria = aux;
                } else {
                    if(aux == 'N'){
                        categoria = aux;
                    }
                }

                char aux = participantes[2].getCategoria();
            }
        }
        */
    }
    
    //  Funcion para devolver la categoria del equipo y nombre de los jugadores
    public String datosBasicos(){
        
        String resultado;
        
        /*if(!this.esCompleto()){
        resultado = "Equipo no completo";
        } else {*/
        resultado = "Equipo " + nombre;
        resultado += ". Categoria ";
        if(categoria == 'P'){
            resultado += "profesional. ";
        } else {
            if(categoria == 'A'){
                resultado += "aficionado. ";
            } else {
                resultado += "novato. ";
            }
        }
        resultado += "Jugadores: ";
        for(Jugador jugadorActual : participantes){
            resultado += jugadorActual.getNombre() + ", ";
        }
        //}
        
        return resultado;
    }
    
    @Override   
    public String toString(){
        
        String resultado;
        
        /*        if(cantParticipantes == 0){
        resultado = "Equipo vacio";
        } else {*/
        resultado = "\nEqupo " + nombre;
        //if(this.esCompleto()){
        resultado += ", categoria ";
        if(categoria == 'P'){
            resultado += "profesional, ";
        } else {
            if(categoria == 'A'){
                resultado += "aficionado, ";
            } else {
                resultado += "novato, ";
            }
        }
        //}
        resultado += ", en locacion " + locacionActual;
        resultado += ", Jugadores: ";
        for(int i = 0; i < cantParticipantes; i++){
            resultado += "\n  " + participantes[i].toString();
        }
        //}
        
        return resultado;
    }
}

package clases.estructuras;

import clases.unidades.Jugador;
import estructurasGenerales.lineales.Lista;
import estructurasGenerales.propositoEspecifico.DiccionarioAVL;

public class Jugadores {
    
    private final DiccionarioAVL tablaJugadores;
    private final RankingJugadores ranking;
    
    public Jugadores(){
        this.tablaJugadores = new DiccionarioAVL();
        this.ranking = new RankingJugadores();
    }
    
    public boolean agregarJugador(String nombre, String tipo, String categoria, int dinero){
        return tablaJugadores.insertar(nombre, new Jugador(nombre, tipo, categoria, dinero));
    }
    
    public boolean agregarJugador(Jugador jugador){
        return tablaJugadores.insertar(jugador.getNombre(), jugador);
    }
    
    public boolean eliminarJugador(String nombreJugador){
        return tablaJugadores.eliminar(nombreJugador);
    }
    
    public boolean existeJugador(String nombreJugador){
        return tablaJugadores.existeClave(nombreJugador);
    }
    
    public Jugador recuperarJugador(String nombreJugador){
        return (Jugador) tablaJugadores.obtenerInformacion(nombreJugador);
    }
    
    public Lista recuperarJugadores(){
        return tablaJugadores.listarDatos();
    }
    
    public String jugadoresSubcadena(String subcadena){
        return tablaJugadores.jugadoresSubcadena(subcadena);
    }
    
    //  Funcion para obtener un String con el ranking de los jugadores segun su 
    //      cantidad de victorias
    public String obtenerRanking(){
        return ranking.obtenerRanking(this.recuperarJugadores());
    }
    
    public boolean esVacio(){
        return tablaJugadores.esVacio();
    }
    
    @Override
    public String toString(){
        return tablaJugadores.toString();
    }
}

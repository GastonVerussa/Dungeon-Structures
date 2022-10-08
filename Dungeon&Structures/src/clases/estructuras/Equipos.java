package clases.estructuras;

import clases.unidades.Equipo;
import estructurasGenerales.lineales.Lista;
import estructurasGenerales.propositoEspecifico.DiccionarioHash;

public class Equipos {
    
    private DiccionarioHash tablaEquipos;
    
    public Equipos(){
        tablaEquipos = new DiccionarioHash();
    }
    
    public boolean insertar(Equipo equipo){
        return tablaEquipos.insertar(equipo.getNombre(), equipo);
    }
    
    public boolean eliminar(String nombre){
        return tablaEquipos.eliminar(nombre);
    }
    
    public Equipo recuperar(String nombre){
        return (Equipo) tablaEquipos.obtenerInformacion(nombre);
    }
    
    public boolean existe(String nombre){
        return tablaEquipos.existeClave(nombre);
    }
    
    public Lista recuperarEquipos(){
        return tablaEquipos.listarDatos();
    }
    
    /*
    public boolean estaEnEquipo(String nombre){
    
    boolean encontrado = false;
    
    Lista listaEquipos = tablaEquipos.listarDatos();
    Equipo aux;
    
    while(!listaEquipos.esVacia() && !encontrado){
    Equipo auxEquipo = (Equipo) listaEquipos.recuperar(1);
    listaEquipos.eliminar(1);
    
    Jugador[] jugadoresEquipo = auxEquipo.getParticipantes();
    
    for(Jugador jugadorActual : jugadoresEquipo){
    if(jugadorActual.getNombre().equals(nombre)){
    encontrado = true;
    }
    }
    }
    
    return encontrado;
    }*/
    
    @Override
    public String toString(){
        return tablaEquipos.toString();
    }
}

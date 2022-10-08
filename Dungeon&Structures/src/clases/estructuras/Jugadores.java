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
    
    public Lista jugadoresSubcadena(String subcadena){
        
        //  Lista resultado
        Lista resultado = new Lista();
        
        //  Lista auxiliar con todas las claves
        Lista listaAux = tablaJugadores.listarClaves();

        //  Variable para recorrer la estructura
        int pos = 1;
        //  Variable de control para poder terminar el while antes de recorrer todo
        boolean terminar = false;

        //  Mientras queden elementos por recorrer y terminar sea falso
        while(pos <= listaAux.longitud() && !terminar){
            //  Recupera el Jugador de la posicion actual
            String nombreJugador = (String) listaAux.recuperar(pos);
            //  Si el nombre empieza con la subcadena
            if(nombreJugador.startsWith(subcadena)){
                //  Lo agrega a la lista resultado, al final para respetar menor a mayor
                resultado.insertar(nombreJugador, resultado.longitud() + 1);
            } else {
                //  Si no empieza con la subcadena y ademas es mas grande que al subcadena.
                //      Como el metodo devuelve la lista de menor a mayor, y los recorremos
                //      en ese orden, podemos decir que ya no habran nombres que empiecen
                //      con la subcadena mas adelante.
                if(nombreJugador.compareTo(subcadena) > 0){
                    //  Por lo que pone en true el terminar.
                    terminar = true;
                }
            }
            //  Avanza al siguiente elemento
            pos++;
        }

        return resultado;
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

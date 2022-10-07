package clases.estructuras;

import clases.unidades.Jugador;
import lineales.dinamicas.Lista;
import propositoEspecifico.DiccionarioAVL;
import propositoEspecifico.MapeoAMuchosAVL;

public class Jugadores {
    
    private DiccionarioAVL tablaJugadores;
    
    public Jugadores(){
        this.tablaJugadores = new DiccionarioAVL();
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
        
        String resultado;
        
        //  Si esta vacio
        if(this.esVacio()){
            //  Devuelve lo siguiente
            resultado = "Ranking: No hay jugadores";
        } else {
            //  Si no esta vacio
            resultado = "Ranking: ";
            //  Consigue la lista de jugadores ordenada por nombre
            Lista listaJugadores = tablaJugadores.listarDatos();
            //  Variable para recorrer la lista
            Jugador aux;
            //  Arbol Heap Maximal que servira para ordenar los Jugadores por cantidad
            //      de victorias, lo crea con la capacidad justa para los jugadores.
            MapeoAMuchosAVL mapeoRanking = new MapeoAMuchosAVL();
            //  While para recorrer todos los jugadores
            while(!listaJugadores.esVacia()){
                //  Se podria recuperar siempre hacer un for recuperando i, pero entonces 
                //      cada bucle del for seria de orden(N), cuando al recuperar 
                //      y eliminar la primera posicion se convierte en orden(1)
                
                //  Recupera el primer elemento
                aux = (Jugador) listaJugadores.recuperar(1);
                //  Y lo elimina
                listaJugadores.eliminar(1);
                //  Inserta en el arbol un objeto de clase JugadorRanking, la cual
                //      implementa la interfaz Comparable, con el metodo compareTo 
                //      comparando simplemente por la cantidad de victorias
                mapeoRanking.asociar(aux.getCantVictorias(), aux.getNombre());
            }
            
            resultado = mapeoRanking.toString();
            
            /*
            //  Variable Jugador Ranking para recorrer el arbol
            JugadorRanking auxJugadorRanking;
            //  Variable para guardar cual fue la ultima cantidad de victorias registrada
            int ultimaCantidad = -1;
            //  Mientras el arbol no este vacio
            while(!mapeoRanking.esVacio()){
                //  Recupera la cima del nodo, el jugador con mayores victorias 
                //      entre los jugadores del arbol
                auxJugadorRanking = (JugadorRanking) mapeoRanking.recuperarCima();
                //  Lo elimina del arbol para avanzar al siguiente
                mapeoRanking.eliminarCima();
                //  Si es una nueva cantidad de victorias (Es una cantidad diferente
                //      a la ultima registrada)
                if(auxJugadorRanking.getCantVictorias() != ultimaCantidad){
                    //  Agrega al resultado una nueva linea donde iran todos los jugadores
                    //      que tengan esa cantidad de victorias
                    resultado += "\nJugadores con " + auxJugadorRanking.getCantVictorias() + " victorias: ";
                } else {
                    //  Si es la misma a la ultima registrada, significa que tiene que ir
                    //      en la misma linea del String al anterior, asi que solo
                    //      los separa con una coma
                    resultado += ", ";
                }
                //  Luego agrega el nombre del jugador
                resultado += auxJugadorRanking.getNombre();*/
        }
        
        return resultado;
    }
    
    public boolean esVacio(){
        return tablaJugadores.esVacio();
    }
    
    @Override
    public String toString(){
        return tablaJugadores.toString();
    }
}

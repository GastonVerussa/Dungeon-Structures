package clases.estructuras;

import grafos.GrafoEtiquetadoViejo;
import lineales.dinamicas.Lista;
import utiles.Aleatorio;

public class Mapa {
    
    private final GrafoEtiquetadoViejo grafo;
    
    public Mapa(){
        grafo = new GrafoEtiquetadoViejo();
    }
    
    public boolean insertarLocacion(String locacion){
        return grafo.insertarVertice(locacion);
    }
    
    public boolean insertarCamino(String origen, String destino, int distancia){
        return grafo.insertarArco(origen, destino, distancia);
    }
    
    // Funcion para conseguir una locacion aleatoria del mapa, servira para la formacion de equipos
    public String obtenerLocacionAleatoria(){
        
        String resultado;
        
        if(grafo.esVacio()){
            resultado = "Mapa vacio";
        } else {
            Lista listaLocaciones = grafo.listarEnAnchura();
            int cantidadLocaciones = listaLocaciones.longitud();
            resultado = (String) listaLocaciones.recuperar(Aleatorio.intAleatorio(1, cantidadLocaciones));
        }
        
        return resultado;
    }
    
    public Lista obtenerAdyacentes(String locacion){
        return grafo.obtenerAdyacentes(locacion);
    }
    
    //  Devuelve el camino mas corto segun distancia. 
    //      Lo devuelve en forma de lista, con el primer elemento siendo la distancia total,
    //       de no haber camino posible devuelve una lista vacia.
    //      
    public Lista obtenerCaminoMenosKm(String origen, String destino){
        
        //  Variable que guarda el resultado
        Lista resultado = new Lista();
        
        //  Consigue todos los caminos posibles del origen al destino
        Lista todosCaminos = grafo.caminosPosibles(origen, destino);
        
        //  Mientras queden caminos posibles por recorrer
        while(!todosCaminos.esVacia()){

            //  Recupera el primer camino posible en la lista, y lo reformetea.
            Lista caminoPosible = reformatearCamino((Lista) todosCaminos.recuperar(1));
            //  Luego lo borra
            todosCaminos.eliminar(1);
            
            //  Si todavia es el primer camino encontrado (resultado es vacio) o si la distancia de este
            //      camino es menor a la distancia del menor camino encontrado
            if(resultado.esVacia() || (int) caminoPosible.recuperar(1) < (int) resultado.recuperar(1)){
                //  De ser el caso, lo guarda como nuevo resultado
                resultado = caminoPosible;
            }
        }
        
        return resultado;
    }
    
    //  Devuelve una lista con el camino que pasa por menor locaciones entre dos locaciones
    //      en un formato distinto al que devuleve el grafo
    public Lista obtenerCaminoMenosLocaciones(String origen, String destino){
        return reformatearCamino(grafo.caminoMasCorto(origen, destino));
    }
    
    //  Devuelve una lista con los caminos que no superen el limte de distancia maximo
    //      Si no hay caminos posibles devuelve la lista vacia
    //      Los caminos devueltos usan el mismo formato de camino que el devuelto
    //          en obtenerCaminoMenosKM().
    public Lista obtenerCaminosLimiteKm(String origen, String destino, int limiteKm){
        
        //  Variable que guarda el resultado
        Lista resultado = new Lista();
        
        //  Consigue todos los caminos posibles del origen al destino
        Lista todosCaminos = grafo.caminosPosibles(origen, destino);
            
        //  Mientras queden caminos posibles por recorrer
        while(!todosCaminos.esVacia()){
            
            //  Recupera el primer camino posible en la lista, y lo reformetea.
            Lista caminoPosible = reformatearCamino((Lista) todosCaminos.recuperar(1));
            //  Luego lo borra
            todosCaminos.eliminar(1);
            
            //  Si la distancia total del camino es menor al limite propuesto
            if((int) caminoPosible.recuperar(1) < limiteKm){
                //  Lo agregamos a la lista a devolver, como no importa el orden lo 
                //      insertamos en la primera posicion, siendo la mas eficiente con orden 1
                resultado.insertar(caminoPosible, 1);
            }
        }
        
        return resultado;
    }
    
    public Lista obtenerCaminosNoPasenPorLocacion(String origen, String destino, String locacionAEvitar){
        
        //  Variable que guarda el resultado
        Lista resultado = new Lista();
        
        //  Consigue todos los caminos posibles del origen al destino
        Lista todosCaminos = grafo.caminosPosibles(origen, destino);
            
        //  Mientras queden caminos posibles por recorrer
        while(!todosCaminos.esVacia()){
            
            //  Variable para recorrer los caminos posibles, recuperamos el siguiente camino
            Lista caminoPosible = (Lista) todosCaminos.recuperar(1);
            //  Y lo eliminamos de la lista
            todosCaminos.eliminar(1);

            //  Si la locacion a evitar se encuentra en el camino, no hacemos nada
            if(caminoPosible.localizar(locacionAEvitar) < 0){
                //  Si no se encuentra, entonces cambiamos el formato y lo agregamos a la lista de resultado
                caminoPosible = reformatearCamino(caminoPosible);
                
                //  Como no importa el orden lo agregamos en el primer lugar, el mas eficiente de acceder
                resultado.insertar(caminoPosible, 1);
            }
        }
        
        return resultado;
    }
    
    public boolean existeLocacion(String nombre){
        return grafo.existeVertice(nombre);
    }
    
    public boolean existeCamino(String origen, String destino){
        return grafo.existeArco(origen, destino);
    }
    
    public void moidificarCamino(String origen, String destino, int distancia){
        grafo.eliminarArco(origen, destino);
        grafo.insertarArco(origen, destino, distancia);
    }
    
    public boolean eliminarLocacion(String nombre){
        return grafo.eliminarVertice(nombre);
    }
    
    public boolean eliminarCamino(String origen, String destino){
        return grafo.eliminarArco(origen, destino);
    }
    
    public int recuperarDistancia(String origen, String destino){
        return (int) grafo.recuperarEtiqueta(origen, destino);
    }
    
    public Lista recuperarLocaciones(){
        return grafo.listarEnAnchura();
    }
    
    //  Los caminos vienen en una lista en el formato { origen, etiqueta, vertice, etiqueta, ...., destino}
    //  Esta funcion logra modificarlos, sabiendo que las etiquetas son enteros, a una lista del formato
    //      { distanciaTotal, origen, vertice, vertice, ..., destino }
    //      ya que de los utilizaremos bastante y de este formato es mas simple de manejar.
    private Lista reformatearCamino(Lista camino){
        
        Lista resultado = new Lista();

        //  Si el camino esta vacio, entonces devuelve una lista vacia
        if(!camino.esVacia()){
            
            int distanciaTotal = 0;
            
            //  Recupera la primera locacion y la guarda en el resultado
            resultado.insertar(camino.recuperar(1), 1);
            camino.eliminar(1);

            //  Mientras todavia no haya terminado de recorrer el camino
            while(!camino.esVacia()){
                //  Recuperamos el primer elemento, que por el formato del camino sabemos que
                //      es una etiqueta, y por el formato del mapa sabemos que es un entero
                //      que representa la distancia entre la anterior locacion y la siguiente
                //  Y suma esa distancia a la distancia total
                distanciaTotal += (int) camino.recuperar(1);
                //  Elimina el primer elemento
                camino.eliminar(1);
                //  Recupera el siguiente elemento, que sabemos por el formato que es un vertice,
                //       es decir, la siguiente locacion y lo guarda en la lista auxiliar y lo elimina
                resultado.insertar(camino.recuperar(1), resultado.longitud() + 1);
                //  Y lo elimina
                camino.eliminar(1);
            }

            //  Al finalizar de trasladar el recorrido al resultado, agrega la distancia total
            //      como primer elemento del mismo
            resultado.insertar(distanciaTotal, 1);
            
        }
        
        return resultado;
    }
    
    @Override
    public String toString(){
        return grafo.toString();
    }
}

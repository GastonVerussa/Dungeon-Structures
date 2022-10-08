package clases.estructuras;

import estructurasGenerales.grafo.GrafoEtiquetadoIntMod;
import estructurasGenerales.lineales.Lista;
import utiles.Aleatorio;

public class Mapa {
    
    private final GrafoEtiquetadoIntMod grafo;
    
    public Mapa(){
        grafo = new GrafoEtiquetadoIntMod();
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
        return grafo.conseguirAdyacentes(locacion);
    }
    
    //  Devuelve el camino mas corto segun distancia. 
    //      Lo devuelve en forma de lista, con el primer elemento siendo la distancia total,
    //       de no haber camino posible devuelve una lista vacia.
    public Lista obtenerCaminoMenosKm(String origen, String destino){
        return grafo.caminoMasCortoEtiqueta(origen, destino);
    }
    
    //  Devuelve una lista con el camino que pasa por menor locaciones entre dos locaciones
    //      en un formato distinto al que devuleve el grafo
    public Lista obtenerCaminoMenosLocaciones(String origen, String destino){
        return grafo.caminoMasCorto(origen, destino);
    }
    
    //  Devuelve una lista con los caminos que no superen el limte de distancia maximo
    //      Si no hay caminos posibles devuelve la lista vacia
    //      Los caminos devueltos usan el mismo formato de camino que el devuelto
    //          en obtenerCaminoMenosKM().
    public Lista obtenerCaminosLimiteKm(String origen, String destino, int limiteKm){
        return grafo.caminosMenoresKm(origen, destino, limiteKm);
    }
    
    public Lista obtenerCaminosNoPasenPorLocacion(String origen, String destino, String locacionAEvitar){
        return grafo.caminosPosiblesSinVertice(origen, destino, locacionAEvitar);
    }
    
    public boolean existeLocacion(String nombre){
        return grafo.existeVertice(nombre);
    }
    
    public boolean existeCamino(String origen, String destino){
        return grafo.existeArco(origen, destino);
    }
    
    public boolean moidificarCamino(String origen, String destino, int distancia){
        return grafo.modificarArco(origen, destino, distancia);
    }
    
    public boolean eliminarLocacion(String nombre){
        return grafo.eliminarVertice(nombre);
    }
    
    public boolean eliminarCamino(String origen, String destino){
        return grafo.eliminarArco(origen, destino);
    }
    
    public int recuperarDistancia(String origen, String destino){
        return grafo.recuperarEtiqueta(origen, destino);
    }
    
    public Lista recuperarLocaciones(){
        return grafo.listarEnAnchura();
    }
    
    @Override
    public String toString(){
        return grafo.toString();
    }
}

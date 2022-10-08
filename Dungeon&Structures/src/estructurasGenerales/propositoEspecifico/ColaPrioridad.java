package estructurasGenerales.propositoEspecifico;

import estructurasGenerales.lineales.Cola;
import estructurasGenerales.lineales.Pila;
import estructurasGenerales.lineales.Lista;
import clases.unidades.Jugador;
import estructurasGenerales.propositoEspecifico.DiccionarioAVL;

public class ColaPrioridad {
    
    private NodoCP inicio;
    private Lista prioridades;
    private DiccionarioAVL categorias;
    
    //  Crea una cola de prioridad sin elementos.
    public ColaPrioridad(Lista prioridades, DiccionarioAVL categorias){
        this.prioridades = prioridades;
        this.categorias = categorias;
        inicio = null;
    }
    
    //  Recibe por parámetro el elemento y la prioridad del mismo. Se agrega el elemento en la cola
    //      detrás de los de prioridad mayor o igual y por delante de los de prioridad menor. Si la operación
    //      termina con éxito devuelve verdadero y falso en caso contrario.
    public boolean insertar(Object elemento, Comparable prioridad){
        
        //  Si la estructura esta vacia o la prioridad buscada es menor a la del incio
        if(this.esVacia() || prioridad.compareTo(inicio.getPrioridad()) < 0){
            //  Se crea un nuevo nodo con dicha prioridad como nuevo inicio, enlazado al anterior
            inicio = new NodoCP(prioridad, inicio);
            //  Y se pone el elemento en el nuevo inicio
            inicio.getItems().poner(elemento);
        } else {
            //  Si no es menor, se llama a la funcion recursiva auxiliar
            insertarAux(elemento, prioridad, inicio);
        }
        //  Al ser implementado con cola dinamica, siempre se podra poner nuevos elementos
        return true; 
    }
    
    //  Funcion recursiva privada auxiliar para insertar()
    private void insertarAux(Object elemento, Comparable prioridad, NodoCP nodo){
        
        //  Si la prioridad que se busca es la de este nodo
        if(prioridad.compareTo(nodo.getPrioridad()) == 0){
            //  Se pone el elemento en la cola de este nodo
            nodo.getItems().poner(elemento);
        } else {
            //  Si no lo es, se verifica si no hay siguiente nodo o (en caso de que haya) si su
            //      prioridad es mayor a la prioridad buscada
            if(nodo.getSiguienteNodo() == null || prioridad.compareTo(nodo.getSiguienteNodo().getPrioridad()) < 0){
                //  Cualquiera de esos casos significa que no hay elementos de la prioridad buscada,
                //      por ende, no hay nodo de la misma. Entonces debe crearse uno enlazado al viejo siguiente
                nodo.setSiguienteNodo(new NodoCP(prioridad, nodo.getSiguienteNodo()));
            }
            //  Luego se llama el insertar en ese nodo
            insertarAux(elemento, prioridad, nodo.getSiguienteNodo());
        }
    }

    //  Elimina el elemento de mayor prioridad. Si hay más de uno con igual prioridad, elimina el que
    //      llegó primero. Si la operación de eliminación termina con éxito devuelve verdadero y falso en caso
    //      contrario.
    public boolean eliminarFrente(){
        
        //  Variable para guardar el exito
        boolean exito = false;
        
        //  Si esta vacia, se devuelve falso
        if(!this.esVacia()){
            //  Si no, se saca el primer elemento del inicio
            inicio.getItems().sacar();
            //  Si era el unico elemento, ahora inicio esta vacio
            if(inicio.getItems().esVacia()){
                //  En ese caso, se pasa el siguiente nodo como nuevo inicio
                inicio = inicio.getSiguienteNodo();
            }
            //  Se elimino con exito
            exito = true;
        }
        
        return exito;
    }

    //  Devuelve el elemento de mayor prioridad. Si hay más de uno con igual prioridad, devuelve
    //      el que llegó primero. Precondición: la cola no está vacía (si está vacía no se puede asegurar el
    //      funcionamiento de la operación).
    public Object obtenerFrente(){
        Object resultado;
        if(this.esVacia()){
            resultado = null;
        } else {
            resultado = inicio.getItems().obtenerFrente();
        }
        return resultado;
    }

    //  Devuelve falso si hay al menos un elemento cargado en la estructura y verdadero en caso contrario.
    public boolean esVacia(){
        return inicio == null;
    }
    
    public void vaciar(){
        inicio = null;
    }
    
    @Override
    public ColaPrioridad clone(){
        
        //  Variable que guarda el resultado
        ColaPrioridad resultado = new ColaPrioridad(prioridades, categorias);
        
        //  Variable auxiliar para recorrer la estructura
        NodoCP aux = inicio;
        //  Pila para insertar las prioridades en orden inverso, mejorando la eficiencia
        Pila pilaAux = new Pila();

        //  Mientras queden nodos por recorrer
        while(aux != null){
            //  Los apila
            pilaAux.apilar(aux);
            //  Y pasa al siguiente
            aux = aux.getSiguienteNodo();
        }

        //  Mientras la pila tenga elementos
        while(!pilaAux.esVacia()){
            //  Obtiene el tope
            aux = (NodoCP) pilaAux.obtenerTope();
            //  Y desapila
            pilaAux.desapilar();

            //  Cola que clona la cola interna del nodo, para poder recorrerla sin
            //      modificar la original
            Cola colaAux = aux.getItems().clone();

            //  Mientras la cola auxiliar tenga elementos
            while(!colaAux.esVacia()){
                //  Los inserta en el mismo orden que los obtiene, con la prioridad correspondiente
                resultado.insertar(colaAux.obtenerFrente(), aux.getPrioridad());
                //  Y va sacando para pasar al siguiente
                colaAux.sacar();
            }
        }
        
        return resultado;
    }
    
    @Override
    public String toString(){
        
        String resultado;
        
        //  Si esta vacia
        if(this.esVacia()){
            //  Devuelve lo siguiente
            resultado = "No hay jugadores esperando a formar equipo";
        } else {
            //  Si no lo esta
            resultado = "Los jugadores esperando a formar equipo son: ";
            
            //  Variable auxiliar para recorrer la estructura
            NodoCP aux = inicio;
            
            //  Mientras queden nodos por recorrer
            while(aux != null){
                
                String categoriaActual = (String) categorias.obtenerInformacion((Comparable) prioridades.recuperar((int) aux.getPrioridad()));
                
                //  Agrega esto al string
                resultado += "\nJugadores de categoria " + categoriaActual + ": { ";
                //  Cola que clona la cola interna del nodo, para poder recorrerla sin
                //      modificar la original
                Cola colaAux = aux.getItems().clone();
                
                //  Mientras la cola auxiliar tenga elementos
                while(!colaAux.esVacia()){
                    //  Agrega el elemento al resultado
                    resultado += ((Jugador) colaAux.obtenerFrente()).getNombre() + ", ";
                    //  Y lo saca de la cola auxiliar
                    colaAux.sacar();
                }
                
                //  Cierra la llave con los elementos de su prioridad
                resultado += " }";
                
                //  Pasa al siguiente nodo, siguiente prioridad
                aux = aux.getSiguienteNodo();
            }
        }
        
        return resultado;
    }
}

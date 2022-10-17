package estructurasGenerales.propositoEspecifico;

import estructurasGenerales.lineales.Lista;
import estructurasGenerales.lineales.Cola;

public class DiccionarioAVL {
    
    private NodoAVLDicc raiz;
    
    // crea una estructura sin elementos.
    public DiccionarioAVL(){
        this.raiz = null;
    }
    
    //  Recibe la clave que es única y el dato (información asociada a ella). Si no existe en la estructura
    //      un elemento con igual clave, agrega el par (clave, dato) a la estructura. Si la operación termina con
    //      éxito devuelve verdadero y falso en caso contrario.
    public boolean insertar(Comparable clave,Object dato){
        boolean exito;
        if(this.esVacio()){
            this.raiz = new NodoAVLDicc(clave, dato);
            exito = true;
        } else {
            exito = insertarAux(clave, dato, this.raiz);
            if(exito){
                this.raiz = verificarBalance(this.raiz);
            }
        }
        return exito;
    }
    
    private boolean insertarAux(Comparable clave, Object dato, NodoAVLDicc nodoActual){
        
        boolean exito;

        Comparable claveActual = nodoActual.getClave();
        if(claveActual.compareTo(clave) == 0){
            exito = false;
        } else {
            if(clave.compareTo(claveActual) < 0){
                if(nodoActual.getIzquierdo() != null){
                    exito = insertarAux(clave, dato, nodoActual.getIzquierdo());
                    if(exito){
                        nodoActual.setIzquierdo(verificarBalance(nodoActual.getIzquierdo()));
                    }
                } else {
                    nodoActual.setIzquierdo(new NodoAVLDicc(clave, dato));
                    exito = true;
                }
            } else {
                if(nodoActual.getDerecho()!= null){
                    exito = insertarAux(clave, dato, nodoActual.getDerecho());
                    if(exito){
                        nodoActual.setDerecho(verificarBalance(nodoActual.getDerecho()));
                    }
                } else {
                    nodoActual.setDerecho(new NodoAVLDicc(clave, dato));
                    exito = true;
                }
            }
            if(exito){
                nodoActual.recalcularAltura();
            }
        }
        
        return exito;
    }
   
    //  Elimina el elemento cuya clave sea la recibida por parámetro. Si lo encuentra y la operación de
    //      eliminación termina con éxito devuelve verdadero y falso en caso contrario.
    public boolean eliminar(Comparable clave){
        
        boolean exito;
        
        if(this.esVacio()){
            exito = false;
        } else {
            if(clave.compareTo(raiz.getClave()) == 0){
                raiz = eliminarNodo(raiz);
                raiz = verificarBalance(raiz);
                exito = true;
            } else {
                exito = eliminarAux(clave, raiz);
                if(exito){
                    raiz = verificarBalance(raiz);
                }
            }
        }
        
        return exito;
    }
    
    private boolean eliminarAux(Comparable clave, NodoAVLDicc nodoActual){
        
        boolean exito;
        
        if(clave.compareTo(nodoActual.getClave()) < 0){
            if(nodoActual.getIzquierdo()== null){
                exito = false;
            } else {
                if(clave.compareTo(nodoActual.getIzquierdo().getClave()) == 0){
                    NodoAVLDicc nuevoHijo = eliminarNodo(nodoActual.getIzquierdo());
                    nodoActual.setIzquierdo(verificarBalance(nuevoHijo));
                    exito = true;
                } else {
                    exito = eliminarAux(clave, nodoActual.getIzquierdo());
                    if(exito){
                        nodoActual.setIzquierdo(verificarBalance(nodoActual.getIzquierdo()));
                    }
                }
            }
        } else {
            if(nodoActual.getDerecho() == null){
                exito = false;
            } else {
                if(clave.compareTo(nodoActual.getDerecho().getClave()) == 0){
                    NodoAVLDicc nuevoHijo = eliminarNodo(nodoActual.getDerecho());
                    nodoActual.setDerecho(verificarBalance(nuevoHijo));
                    exito = true;
                } else {
                    exito = eliminarAux(clave, nodoActual.getDerecho());
                    if(exito){
                        nodoActual.setDerecho(verificarBalance(nodoActual.getDerecho()));
                    }
                }
            }
        }
        
        return exito;
        
    }
    
    private NodoAVLDicc eliminarNodo(NodoAVLDicc nodoObjetivo){
        
        NodoAVLDicc nuevoHijo;
        
        if(nodoObjetivo.getIzquierdo() != null){
            if(nodoObjetivo.getDerecho() != null){
                //  Caso 3
                nuevoHijo = nodoObjetivo;
                if(nodoObjetivo.getIzquierdo().getDerecho() == null){
                    nodoObjetivo.setClave(nodoObjetivo.getIzquierdo().getClave());
                    nodoObjetivo.setIzquierdo(nodoObjetivo.getIzquierdo().getIzquierdo());
                } else {
                    nodoObjetivo.setClave(buscarSucesor(nodoObjetivo.getIzquierdo()));
                    nodoObjetivo.setIzquierdo(verificarBalance(nodoObjetivo.getIzquierdo()));
                }
                nodoObjetivo.recalcularAltura();
            } else {
                //  Caso 2
                nuevoHijo = nodoObjetivo.getIzquierdo();
            }
        } else {
            if(nodoObjetivo.getDerecho() != null){
                //  Caso 2
                nuevoHijo = nodoObjetivo.getDerecho();
            } else {
                //  Caso 1
                nuevoHijo = null;
            }
        }
        
        return nuevoHijo;
    }
    
    //  Busca el nuevo sucesor para el caso 3. Busca el mayor de los menores.
    private Comparable buscarSucesor(NodoAVLDicc nodoPadre){
        
        Comparable sucesor;
        NodoAVLDicc nodoHijo = nodoPadre.getDerecho();
        
        if(nodoHijo.getDerecho() != null){
            sucesor = buscarSucesor(nodoHijo);
            nodoPadre.setDerecho(verificarBalance(nodoHijo));
        } else {
            sucesor = nodoHijo.getClave();
            nodoPadre.setDerecho(nodoHijo.getIzquierdo());
            nodoPadre.recalcularAltura();
        }
        
        return sucesor;
    }
    
    //  Funcion privada que verifica el balance de un nodo y hace los cambios necesarios. Ademas devuelve
    //      la nueva raiz del sub-arbol
    private NodoAVLDicc verificarBalance(NodoAVLDicc nodo){
        
        NodoAVLDicc nuevaRaiz = nodo;
        
        if(nodo == null){
            nuevaRaiz = null;
        } else {
            int balance = getBalance(nodo);

            if(balance > 1){
                //  Nodo desbalanceado a izquierda
                if(getBalance(nodo.getIzquierdo()) == -1){
                    //  Hijo desbalanceado en sentido contrario, se necesita rotacion a izquierda con hijo
                    //      izquierdo de pivot
                    nodo.setIzquierdo(rotarIzquierda(nodo.getIzquierdo()));
                }
                //  Compartan o no sentido de desbalance nodo e hijo, se debe rotar a derecha con nodo de pivot
                nuevaRaiz = rotarDerecha(nodo);
            } else {
                if(balance < -1){
                    //  Nodo desbalanceado a derecha
                    if(getBalance(nodo.getDerecho()) == 1){
                        //  Hijo desbalanceado en sentido contrario, se necesita rotacion a derecha con
                        //      hijo derecho como pivot
                        nodo.setDerecho(rotarDerecha(nodo.getDerecho()));
                    }
                    //  Compartan o no sentido de desbalance nodo e hijo, se debe rotar a izquierda con nodo de pivot
                    nuevaRaiz = rotarIzquierda(nodo);
                }
            }   
        }
        //  Si no esta desbalanceado, no se hace nada.
        return nuevaRaiz;
    }
    
    private int getBalance(NodoAVLDicc nodo){
        
        int alturaIzq, alturaDer;
        
        if(nodo.getIzquierdo() == null){
            alturaIzq = -1;
        } else {
            alturaIzq = nodo.getIzquierdo().getAltura();
        }
        
        if(nodo.getDerecho() == null){
            alturaDer = -1;
        } else {
            alturaDer = nodo.getDerecho().getAltura();
        }
        
        return alturaIzq - alturaDer;
    }
    
    private NodoAVLDicc rotarDerecha(NodoAVLDicc pivot){
        
        NodoAVLDicc hijo = pivot.getIzquierdo();
        NodoAVLDicc aux = hijo.getDerecho();
        hijo.setDerecho(pivot);
        pivot.setIzquierdo(aux);
        pivot.recalcularAltura();
        hijo.recalcularAltura();
        return hijo;
    }
    
    private NodoAVLDicc rotarIzquierda(NodoAVLDicc pivot){
        
        NodoAVLDicc hijo = pivot.getDerecho();
        NodoAVLDicc aux = hijo.getIzquierdo();
        hijo.setIzquierdo(pivot);
        pivot.setDerecho(aux);
        pivot.recalcularAltura();
        hijo.recalcularAltura();
        return hijo;
    }
    
    //  Devuelve verdadero si en la estructura se encuentra almacenado un elemento con la clave recibida
    //      por parámetro, caso contrario devuelve falso
    public boolean existeClave(Comparable clave){
        return existeClaveAux(clave, raiz);
    }
    
    //  Funcion privada para pertenece()
    private boolean existeClaveAux(Comparable clave, NodoAVLDicc nodo){
        
        boolean esta;
        
        //  Si el nodo no existe
        if(nodo == null){
            //  Significa que el elemento no esta en el arbol.
            esta = false;
        } else {
            //  Si el nodo contiene el elemento buscado
            if(nodo.getClave().compareTo(clave) == 0){
                //  Devolvemos que existe
                esta = true;
            } else {
                //  En caso que no lo contenga, revisamos el nodo izquierdo o derecho,
                //      dependiendo de si es mayor o menor.
                if(nodo.getClave().compareTo(clave) < 0){
                    esta = existeClaveAux(clave, nodo.getDerecho());
                } else {
                    esta = existeClaveAux(clave, nodo.getIzquierdo());
                }
            }
        }
        
        return esta;
    }
    
    //  Si en la estructura se encuentra almacenado un elemento con la clave recibida por parámetro,
    //      devuelve la información asociada a ella. Precondición: si no existe un elemento con esa clave no se
    //      puede asegurar el funcionamiento de la operación.
    public Object obtenerInformacion(Comparable clave){
        return obtenerInformacionAux(clave, raiz);
    }
    
    private Object obtenerInformacionAux(Comparable clave, NodoAVLDicc nodo){
        
        Object resultado = null;
        
        //  Si el nodo no existe
        if(nodo != null){
            //  Si el nodo contiene el elemento buscado
            if(nodo.getClave().compareTo(clave) == 0){
                //  Devolvemos que existe
                resultado = nodo.getDato();
            } else {
                //  En caso que no lo contenga, revisamos el nodo izquierdo o derecho,
                //      dependiendo de si es mayor o menor.
                if(nodo.getClave().compareTo(clave) < 0){
                    resultado = obtenerInformacionAux(clave, nodo.getDerecho());
                } else {
                    resultado = obtenerInformacionAux(clave, nodo.getIzquierdo());
                }
            }
        }
        
        return resultado;
    }
    
    //  Recorre la estructura completa y devuelve una lista ordenada con las claves de los elementos que
    //      se encuentran almacenados en ella.
    public Lista listarClaves(){
        Lista resultado = new Lista();
        listarClavesAux(raiz, resultado);
        return resultado;
    }
    

    //  Funciona privada para llenar la lista, la llena en Inorden, ya que de esta forma quedan de menor a mayor.
    private void listarClavesAux(NodoAVLDicc nodo, Lista list){
        
        //  Si el nodo existe
        if(nodo != null){
            //  Llena la lista en Inorden
            listarClavesAux(nodo.getIzquierdo(), list);
            list.insertar(nodo.getClave(), list.longitud() + 1);
            listarClavesAux(nodo.getDerecho(), list);
        } 
    }
    
    //  Recorre la estructura completa y devuelve una lista ordenada con la información asociada de los
    //      elementos que se encuentran almacenados en ella.
    public Lista listarDatos(){
        Lista resultado = new Lista();
        listarDatosAux(raiz, resultado);
        return resultado;
    }
    
    
    //  Funciona privada para llenar la lista, la llena en Inorden, ya que de esta forma quedan de menor a mayor.
    private void listarDatosAux(NodoAVLDicc nodo, Lista list){
        
        //  Si el nodo existe
        if(nodo != null){
            //  Llena la lista en Inorden
            listarDatosAux(nodo.getIzquierdo(), list);
            list.insertar(nodo.getDato(), list.longitud() + 1);
            listarDatosAux(nodo.getDerecho(), list);
        } 
    }
    
    public String jugadoresSubcadena(String subcadena){
        return jugadoresSubcadenaAux(raiz, subcadena);
    }
    
    private String jugadoresSubcadenaAux(NodoAVLDicc nodoActual, String subcadena){
        
        String resultado = "";
        
        //  Si el nodo existe
        if(nodoActual != null){
            //  Llena la lista en Inorden
            String nombreJugador = (String) nodoActual.getClave();
            if((subcadena.compareTo(nombreJugador) <= 0)){
                resultado += jugadoresSubcadenaAux(nodoActual.getIzquierdo(), subcadena);
            }
            if(nombreJugador.startsWith(subcadena)){
                resultado += nombreJugador + ", ";
            }
            if((subcadena.compareTo(nombreJugador) >= 0)){
                resultado += jugadoresSubcadenaAux(nodoActual.getDerecho(), subcadena);
            }
        } 
        
        return resultado;
    }
    
    // devuelve falso si hay al menos un elemento cargado en la estructura y verdadero en caso contrario
    public boolean esVacio(){
        return this.raiz == null;
    }
    
    public void vaciar(){
        this.raiz = null;
    }
    
    //  Devuelve una copia del arbol, para clonarlo recorre la estructura en orden 
    //      por niveles, de esta forma, al ir insertando nodos en el clon, no será
    //      necesario hacer un rebalance, siendo la manera mas efectiva.
    @Override
    public DiccionarioAVL clone(){
        
        DiccionarioAVL resultado = new DiccionarioAVL();
        
        //  Si no esta vacía
        if(!this.esVacio()){
            
            //  Cola para ir almacenando los nodos
            Cola colaAuxiliar = new Cola();
            //  auxiliar para recorrer la estructura
            NodoAVLDicc nodoAux;

            //  Empiezo poniendo la raiz en la cola
            colaAuxiliar.poner(raiz);

            //  Mientras la cola no este vacía
            while(!colaAuxiliar.esVacia()){
                //  Consigo el frente de la cola y lo saco
                nodoAux = (NodoAVLDicc) colaAuxiliar.obtenerFrente();
                colaAuxiliar.sacar();
                //  Luego inserto ese elemento al final de la lista.
                resultado.insertar(nodoAux.getClave(), nodoAux.getDato());
                //  Y en caso de tener hijos los pongo en la cola
                if(nodoAux.getIzquierdo() != null) colaAuxiliar.poner(nodoAux.getIzquierdo());
                if(nodoAux.getDerecho() != null) colaAuxiliar.poner(nodoAux.getDerecho());
            }
        }
        
        return resultado;
    }
    
    //  Devuelve un String con los elementos del arbol, en orden por niveles para mas facil lectura.
    @Override
    public String toString(){  
        
        String resultado = "No hay jugadores cargados";
        
        if(!this.esVacio()){
            resultado = "Tabla AVL de jugadores: ";
            //  Cola para ir almacenando los nodos
            Cola colaAuxiliar = new Cola();
            //  auxiliar para recorrer la estructura
            NodoAVLDicc nodoAux;

            //  Empiezo poniendo la raiz en la cola
            colaAuxiliar.poner(raiz);

            //  Mientras la cola no este vacía
            while(!colaAuxiliar.esVacia()){
                //  Consigo el frente de la cola y lo saco
                nodoAux = (NodoAVLDicc) colaAuxiliar.obtenerFrente();
                colaAuxiliar.sacar();
                
                //  Variables para referencias mas facilmente a los hijos, ademas de no tener que llamar a la funcion multiples veces
                NodoAVLDicc izquierdo = nodoAux.getIzquierdo();
                NodoAVLDicc derecho = nodoAux.getDerecho();
                
                //  Luego agrego el elemento al String
                resultado += "\n Altura: " + nodoAux.getAltura() + ". Jugador " + nodoAux.getClave() + ". Hijos: ";
            
                //  Si el izquierdo existe
                if(izquierdo != null){
                    //  Agrega su elemento al String
                    resultado += izquierdo.getClave();
                }
                
                //  Coma para separar elementos o saber cual es hijo izquierdo y derecho
                resultado += " _ ";

                //  Si el derecho existe, lo agrega
                if(derecho != null){
                    resultado += derecho.getClave();
                }
                
                //  Y en caso de tener hijos los pongo en la cola
                if(izquierdo != null) colaAuxiliar.poner(nodoAux.getIzquierdo());
                if(derecho != null) colaAuxiliar.poner(nodoAux.getDerecho());
            }
        }
        
        return resultado;
    }
}

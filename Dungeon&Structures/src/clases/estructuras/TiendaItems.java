package clases.estructuras;

import lineales.dinamicas.Cola;
import lineales.dinamicas.Lista;
import propositoEspecifico.NodoAVLMapeoM;
import utiles.Dato;
import clases.unidades.Item;

public class TiendaItems {
    
    private NodoAVLMapeoM raiz;
    
    //  Crea un árbol sin elementos.
    public TiendaItems(){
        raiz = null;
    }
    
    public boolean insertar(Item objeto){
        return asociar(objeto.getPrecio(), objeto);
    }
    
    public boolean insertar(String codigo, String nombre, int precio, int puntosAtk, int puntosDef, int cantCopiasDisp){
        return asociar(precio, new Item(codigo, nombre, precio, puntosAtk, puntosDef, cantCopiasDisp));
    }
    
    //  Recibe un valor que representa a un elemento del dominio y un segundo valor 
    //      que representa a un elemento del rango. Si no existe otro par que contenga 
    //      a valorDominio, se agrega en el mapeo el par (valorDominio, {valorRango}), 
    //      donde el segundo término es un conjunto de rangos con un solo valor. 
    //      Si ya existe un par con valorDominio, se agrega valorRango al conjunto de rangos existente.
    //      Si la operación termina con éxito devuelve verdadero y falso en caso contrario.
    private boolean asociar (Comparable valorDominio, Object valorRango){
        boolean exito;
        if(this.esVacio()){
            this.raiz = new NodoAVLMapeoM(valorDominio, valorRango);
            exito = true;
        } else {
            //  Boolean para saber si para agregar el par, se agregó un nuevo nodo.
            //      En tal caso hay que verificar los balances y ajustar. En caso 
            //      de que no se haya agregado nodo es innecesario.
            Dato<Boolean> seAgregoNodo = new Dato(false);
            exito = asociarAux(valorDominio, valorRango, this.raiz, seAgregoNodo);
            if(seAgregoNodo.get()){
                this.raiz = verificarBalance(this.raiz);
            }
        }
        return exito;
    }
    
    private boolean asociarAux(Comparable valorDominio, Object valorRango, NodoAVLMapeoM nodoActual, Dato<Boolean> seAgregoNodo){
        
        boolean exito;

        Comparable dominioActual = nodoActual.getDominio();
        if(dominioActual.compareTo(valorDominio) == 0){
            nodoActual.agregarRango(valorRango);
            exito = true;
        } else {
            if(valorDominio.compareTo(dominioActual) < 0){
                if(nodoActual.getIzquierdo() != null){
                    exito = asociarAux(valorDominio, valorRango, nodoActual.getIzquierdo(), seAgregoNodo);
                    if(seAgregoNodo.get()){
                        nodoActual.setIzquierdo(verificarBalance(nodoActual.getIzquierdo()));
                    }
                    nodoActual.recalcularAltura();
                } else {
                    nodoActual.setIzquierdo(new NodoAVLMapeoM(valorDominio, valorRango));
                    nodoActual.recalcularAltura();
                    seAgregoNodo.set(true);
                    exito = true;
                }
            } else {
                if(nodoActual.getDerecho()!= null){
                    exito = asociarAux(valorDominio, valorRango, nodoActual.getDerecho(), seAgregoNodo);
                    if(seAgregoNodo.get()){
                        nodoActual.setDerecho(verificarBalance(nodoActual.getDerecho()));
                    }
                    nodoActual.recalcularAltura();
                } else {
                    nodoActual.setDerecho(new NodoAVLMapeoM(valorDominio, valorRango));
                    nodoActual.recalcularAltura();
                    seAgregoNodo.set(true);
                    exito = true;
                }
            }
        }
        
        return exito;
    }
    
    public boolean eliminar(Item objeto){
        return desasociar(objeto.getPrecio(), objeto);
    }
    
    //  Elimina valorRango del conjunto de rangos asociado a valorDominio. Caso 
    //      especial: si al eliminar valorRango del conjunto este quedara vacío,
    //      debe eliminar el par (valorDominio,{}) del mapeo. Si encuentra el par 
    //      y la operación de eliminación termina con éxito devuelve verdadero y 
    //      falso en caso contrario.
    private boolean desasociar(Comparable valorDominio, Object valorRango){
        
        boolean exito;
        
        if(this.esVacio()){
            exito = false;
        } else {
            Dato<Boolean> seEliminoNodo = new Dato(false);
            if(valorDominio.compareTo(raiz.getDominio()) == 0){
                exito = raiz.eliminarRango(valorRango);
                if(raiz.rangoVacio()){
                    raiz = eliminarNodo(raiz);
                    raiz = verificarBalance(raiz);
                }
            } else {
                exito = desasociarAux(valorDominio, valorRango, raiz, seEliminoNodo);
                if(seEliminoNodo.get()){
                    raiz = verificarBalance(raiz);
                }
            }
        }
        
        return exito;
    }
    
    private boolean desasociarAux(Comparable valorDominio, Object valorRango, NodoAVLMapeoM nodoActual, Dato<Boolean> seEliminoNodo){
        
        boolean exito;
        
        NodoAVLMapeoM hijo;
        
        if(valorDominio.compareTo(nodoActual.getDominio()) < 0){
            hijo = nodoActual.getIzquierdo();
            if(hijo == null){
                exito = false;
            } else {
                if(valorDominio.compareTo(hijo.getDominio()) == 0){
                    exito = hijo.eliminarRango(valorRango);
                    if(hijo.rangoVacio()){
                        nodoActual.setIzquierdo(eliminarNodo(hijo));
                        nodoActual.setIzquierdo(verificarBalance(nodoActual.getIzquierdo()));
                        seEliminoNodo.set(true);
                    }
                    exito = true;
                } else {
                    exito = desasociarAux(valorDominio, valorRango, nodoActual.getIzquierdo(), seEliminoNodo);
                    if(seEliminoNodo.get()){
                        nodoActual.setIzquierdo(verificarBalance(nodoActual.getIzquierdo()));
                    }
                }
            }
        } else {
            hijo = nodoActual.getDerecho();
            if(hijo == null){
                exito = false;
            } else {
                if(valorDominio.compareTo(hijo.getDominio()) == 0){
                    exito = hijo.eliminarRango(valorRango);
                    if(hijo.rangoVacio()){
                        nodoActual.setDerecho(eliminarNodo(hijo));
                        nodoActual.setDerecho(verificarBalance(nodoActual.getDerecho()));
                        seEliminoNodo.set(true);
                    }
                    exito = true;
                } else {
                    exito = desasociarAux(valorDominio, valorRango, nodoActual.getDerecho(), seEliminoNodo);
                    if(seEliminoNodo.get()){
                        nodoActual.setDerecho(verificarBalance(nodoActual.getDerecho()));
                    }
                }
            }
        }
        
        return exito;
        
    }
    
    private NodoAVLMapeoM eliminarNodo(NodoAVLMapeoM nodoObjetivo){
        
        NodoAVLMapeoM nuevoHijo;
        
        if(nodoObjetivo.getIzquierdo() != null){
            if(nodoObjetivo.getDerecho() != null){
                //  Caso 3
                nuevoHijo = nodoObjetivo;
                if(nodoObjetivo.getIzquierdo().getDerecho() == null){
                    nodoObjetivo.setDominio(nodoObjetivo.getIzquierdo().getDominio());
                    nodoObjetivo.setRango(nodoObjetivo.getIzquierdo().getRango());
                    nodoObjetivo.setIzquierdo(nodoObjetivo.getIzquierdo().getIzquierdo());
                } else {
                    NodoAVLMapeoM nodoSucesor = buscarSucesor(nodoObjetivo.getIzquierdo());
                    nodoObjetivo.setDominio(nodoSucesor.getDominio());
                    nodoObjetivo.setRango(nodoSucesor.getRango());
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
    private NodoAVLMapeoM buscarSucesor(NodoAVLMapeoM nodoPadre){
        
        NodoAVLMapeoM sucesor;
        NodoAVLMapeoM nodoHijo = nodoPadre.getDerecho();
        
        if(nodoHijo.getDerecho() != null){
            sucesor = buscarSucesor(nodoHijo);
            nodoPadre.setDerecho(verificarBalance(nodoHijo));
        } else {
            sucesor = nodoHijo;
            nodoPadre.setDerecho(nodoHijo.getIzquierdo());
            nodoPadre.recalcularAltura();
        }
        
        return sucesor;
    }
    
    //  Funcion privada que verifica el balance de un nodo y hace los cambios necesarios. Ademas devuelve
    //      la nueva raiz del sub-arbol
    private NodoAVLMapeoM verificarBalance(NodoAVLMapeoM nodo){
        
        NodoAVLMapeoM nuevaRaiz = nodo;
        
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
    
    private int getBalance(NodoAVLMapeoM nodo){
        
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
    
    private NodoAVLMapeoM rotarDerecha(NodoAVLMapeoM pivot){
        
        NodoAVLMapeoM hijo = pivot.getIzquierdo();
        NodoAVLMapeoM aux = hijo.getDerecho();
        hijo.setDerecho(pivot);
        pivot.setIzquierdo(aux);
        pivot.recalcularAltura();
        hijo.recalcularAltura();
        return hijo;
    }
    
    private NodoAVLMapeoM rotarIzquierda(NodoAVLMapeoM pivot){
        
        NodoAVLMapeoM hijo = pivot.getDerecho();
        NodoAVLMapeoM aux = hijo.getIzquierdo();
        hijo.setIzquierdo(pivot);
        pivot.setDerecho(aux);
        pivot.recalcularAltura();
        hijo.recalcularAltura();
        return hijo;
    }

    /*
    //  Si en el mapeo se encuentra almacenado algún par cuyo dominio es valorDominio, esta operación
    //      devuelve el valor de rango asociado a él. Precondición: valorDominio está en el mapeo (si no existe,
    //      no se puede asegurar el funcionamiento de la operación).
    public Lista obtenerValores(Comparable valorDominio){
        return obtenerValoresAux(valorDominio, raiz);
    }
    
    //  Funcion privada para pertenece()
    private Lista obtenerValoresAux(Comparable valorDominio, NodoAVLMapeoM nodo){
        
        Lista valoresRango;
        
        //  Si el nodo no existe
        if(nodo == null){
            //  Significa que el elemento no esta en el arbol.
            valoresRango = new Lista();
        } else {
            //  Si el nodo contiene el elemento buscado
            if(nodo.getDominio().compareTo(valorDominio) == 0){
                //  Devolvemos que pertenece
                valoresRango = nodo.getRango();
            } else {
                //  En caso que no lo contenga, revisamos el nodo izquierdo o derecho,
                //      dependiendo de si es mayor o menor.
                if(nodo.getDominio().compareTo(valorDominio) < 0){
                    valoresRango = obtenerValoresAux(valorDominio, nodo.getDerecho());
                } else {
                    valoresRango = obtenerValoresAux(valorDominio, nodo.getIzquierdo());
                }
            }
        }
        
        return valoresRango;
    }
    */
    
    //  Devuelve falso si hay al menos un elemento en el árbol y verdadero en caso contrario.
    public boolean esVacio(){
        return raiz == null;
    }
     
    /*
    //  Recorre el árbol completo y devuelve una lista ordenada con los elementos del dominio
    //      que se encuentran almacenados en él.
    public Lista obtenerConjuntoDominio(){
        Lista resultado = new Lista();
        obtenerConjuntoDominioAux(raiz, resultado);
        return resultado;
    }

    //  Funciona privada para llenar la lista de dominio, la llena en Inorden, ya que de esta forma quedan de menor a mayor.
    private void obtenerConjuntoDominioAux(NodoAVLMapeoM nodo, Lista listaDominio){
        
        //  Si el nodo existe
        if(nodo != null){
            //  Llena la lista en Inorden
            obtenerConjuntoRangoAux(nodo.getIzquierdo(), listaDominio);
            listaDominio.insertar(nodo.getDominio(), listaDominio.longitud() + 1);
            obtenerConjuntoRangoAux(nodo.getDerecho(), listaDominio);
        } 
    }
    */
    /*
    //  Recorre el árbol completo y devuelve una lista con todos los elementos del
    //      rango, pasando por todos los dominios.
    public Lista obtenerConjuntoRango(){
        Lista resultado = new Lista();
        obtenerConjuntoRangoAux(raiz, resultado);
        return resultado;
    }

    //  Funciona privada para llenar la lista, la llena en Inorden segun dominios, 
    //      los elementos del rango no estan ordenados.
    private void obtenerConjuntoRangoAux(NodoAVLMapeoM nodo, Lista listaRango){
        
        //  Si el nodo existe
        if(nodo != null){
            //  Llena la lista en Inorden
            obtenerConjuntoRangoAux(nodo.getIzquierdo(), listaRango);
            Lista rangoNodo = nodo.getRango();
            for(int i = 1; i <= rangoNodo.longitud(); i++){
                Object elemento = rangoNodo.recuperar(i);
                if(listaRango.localizar(elemento) < 0){
                    listaRango.insertar(elemento, 1);
                }
            }
            obtenerConjuntoRangoAux(nodo.getDerecho(), listaRango);
        } 
    }
    */
    public void vaciar(){
        raiz = null;
    }
    
    //  Devuelve una copia del mapeo, para clonarlo recorre la estructura en orden 
    //      por niveles, de esta forma, al ir insertando nodos en el clon, no será
    //      necesario hacer un rebalance, siendo la manera mas efectiva.
    @Override
    public TiendaItems clone(){
        
        TiendaItems resultado = new TiendaItems();
        
        //  Si no esta vacía
        if(!this.esVacio()){
            
            //  Cola para ir almacenando los nodos
            Cola colaAuxiliar = new Cola();
            //  auxiliar para recorrer la estructura
            NodoAVLMapeoM nodoAux;

            //  Empiezo poniendo la raiz en la cola
            colaAuxiliar.poner(raiz);

            //  Mientras la cola no este vacía
            while(!colaAuxiliar.esVacia()){
                //  Consigo el frente de la cola y lo saco
                nodoAux = (NodoAVLMapeoM) colaAuxiliar.obtenerFrente();
                colaAuxiliar.sacar();
                //  Recupero su rango y dominio
                Lista rango = nodoAux.getRango();
                Comparable valorDominio = nodoAux.getDominio();
                //  Recorro el rango y lo asocio en sentido inverso así
                //      quedan en el mismo orden al final
                for(int i = rango.longitud(); i >= 1; i--){
                    resultado.asociar(valorDominio, rango.recuperar(i));
                }
                //  Y en caso de tener hijos los pongo en la cola
                if(nodoAux.getIzquierdo() != null) colaAuxiliar.poner(nodoAux.getIzquierdo());
                if(nodoAux.getDerecho() != null) colaAuxiliar.poner(nodoAux.getDerecho());
            }
        }
        
        return resultado;
    }
    
    @Override
    public String toString(){
        
        String resultado;
        
        if(this.esVacio()){
            resultado = "Mapeo vacio";
        } else {
            resultado = toStringAux(raiz);
        }
        
        return resultado;
    }
    
        
    //  Funciona privada para llenar la lista de los elementos del dominio, la 
    //      llena en Inorden, ya que de esta forma quedan de menor a mayor.
    private String toStringAux(NodoAVLMapeoM nodo){
        
        String resultado = "";
        
        //  Si el nodo existe
        if(nodo != null){
            //  Llena la lista en Inorden
            resultado = toStringAux(nodo.getIzquierdo());
            
            //  Luego agrego el elemento al String
            resultado += "\n Altura: " + nodo.getAltura() + ". Items de coste " + nodo.getDominio().toString() + ": { ";

            Lista rango = nodo.getRango();

            for(int i = 1; i <= rango.longitud(); i++){
                resultado += ((Item) rango.recuperar(i)).getCodigo() + ", ";
            }
            
            //  Saca el ultimo ", "
            resultado = resultado.substring(0, resultado.length() - 2);
            resultado += " } ";
            
            resultado += "Hijos: ";
            if(nodo.getIzquierdo() != null){
                resultado += nodo.getIzquierdo().getDominio().toString();
            }
            resultado += ", ";
            if(nodo.getDerecho()!= null){
                resultado += nodo.getDerecho().getDominio().toString();
            }
            
            resultado += toStringAux(nodo.getDerecho());
        } 
        
        return resultado;
    }

    //  Devuelve una lista ordenada de items con precio menor o igual a monto
    public String conseguirItemsAccesibles(Comparable limite){
        return conseguirItemsRangoAux(0, limite, raiz);
    }
    
    //  Devuelve una lista ordenada de items con precio en el intervalo [minimo, maximo]
    public String conseguirItemsRango(int minimo, int maximo){
        //  Devuelve los items entre el rango de precio minimo y maximo
        return conseguirItemsRangoAux(minimo, maximo, raiz);
    }
    
    //  Funcion privada para conseguirItemsAccesibles() y conseguirItemsRango(), recorre en inorden los nodos
    //      con menor precio al limite
    private String conseguirItemsRangoAux(Comparable minimo, Comparable maximo, NodoAVLMapeoM nodo){
        
        String resultado = "";
        
        if(nodo != null){
            //  Variable para referenciar al elemento del nodo facilmente
            Comparable aux = nodo.getDominio();
            //  Revisamos el subarbol del hijo izquierdo
            //  Si el elemento del nodo es mayor o igual al limite superior del intervalo, no 
            //      revisamos el hijo derecho, ya que todos sus elementos serán mayores.
            if(aux.compareTo(minimo) > 0){
                //  De no ser mayor, revisamos el subarbol del hijo derecho
                resultado += conseguirItemsRangoAux(minimo, maximo, nodo.getIzquierdo());
            }
            //  Si el dominio del nodo es menor al limite, agregamos sus rangos a la lista
            if(aux.compareTo(minimo) >= 0 && aux.compareTo(maximo) <= 0){
                resultado += "Items de coste $" + aux + ": {";
                //  Lo insertamos al final de la lista
                Lista rango = nodo.getRango();
                for(int i = 1; i <= rango.longitud() - 1; i++){
                    resultado += ((Item) rango.recuperar(i)).getCodigo() + ", ";
                }
                resultado += ((Item) rango.recuperar(rango.longitud())).getCodigo() + "} \n";
            }
            //  Si el elemento del nodo es mayor o igual al limite superior del intervalo, no 
            //      revisamos el hijo derecho, ya que todos sus elementos serán mayores.
            if(aux.compareTo(maximo) < 0){
                //  De no ser mayor, revisamos el subarbol del hijo derecho
                resultado += conseguirItemsRangoAux(minimo, maximo, nodo.getDerecho());
            }
        }
        
        return resultado;
    }
    
    //  Devuelve una lista con todos los items que solo hay una copia
    public String recuperarItemsUnicos(){
        return recuperarItemsUnicosAux(raiz);
    }
    
    //  Funciona privada para recuperar items unicos
    private String recuperarItemsUnicosAux(NodoAVLMapeoM nodo){
        
        String stringItems = "";
        
        //  Si el nodo existe
        if(nodo != null){
            //  Llena la lista en Inorden
            stringItems += recuperarItemsUnicosAux(nodo.getIzquierdo());
            Lista rangoNodo = nodo.getRango();
            for(int i = 1; i <= rangoNodo.longitud(); i++){
                Item elemento = (Item) rangoNodo.recuperar(i);
                if(elemento.getCantCopiasDisp() == 1){
                    stringItems += ", " + elemento.getCodigo() ;
                }
            }
            stringItems += recuperarItemsUnicosAux(nodo.getDerecho());
        }
        
        return stringItems;
    }
}

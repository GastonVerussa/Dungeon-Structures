package clases.estructuras;
import clases.unidades.Jugador;
import estructurasGenerales.lineales.Lista;
import estructurasGenerales.propositoEspecifico.NodoAVLMapeoM;
import utiles.Dato;

public class RankingJugadores {
    
    private NodoAVLMapeoM raiz;
    
    public RankingJugadores(){
        raiz = null;
    }
    
    public String obtenerRanking(Lista listaJugadores){
        cargarJugadores(listaJugadores);
        
        String resultado;
        
        if(this.esVacio()){
            resultado = "Ranking: No hay jugadores";
        } else {
            resultado = "Ranking de jugadores: " + obtenerRankingAux(raiz);
        }
        
        this.vaciar();
        return resultado;
    }
    
    private void cargarJugadores(Lista listaJugadores){
        while(!listaJugadores.esVacia()){
            Jugador aux = (Jugador) listaJugadores.recuperar(1);
            this.asociar(aux.getCantVictorias(), aux.getNombre());
            listaJugadores.eliminar(1);
        }
    }
    
    //  Funciona privada para llenar la lista de los elementos del dominio, la 
    //      llena en orden reverso a Inorden, ya que de esta forma quedan de mayor a menor.
    private String obtenerRankingAux(NodoAVLMapeoM nodo){
        
        String resultado = "";
        
        //  Si el nodo existe
        if(nodo != null){
            //  Llena la lista en Inorden
            resultado = obtenerRankingAux(nodo.getDerecho());
            
            //  Luego agrego el elemento al String
            resultado += "\n Jugadores con " + nodo.getDominio().toString() + " victorias: { ";

            Lista rango = nodo.getRango();

            for(int i = 1; i <= rango.longitud(); i++){
                resultado += rango.recuperar(i).toString() + " _ ";
            }
            //  Saca el ultimo ", "
            resultado = resultado.substring(0, resultado.length() - 2);
            resultado += " } ";
            
            resultado += obtenerRankingAux(nodo.getIzquierdo());
        } 
        
        return resultado;
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
    
    private boolean esVacio(){
        return this.raiz == null;
    }

    private void vaciar(){
        this.raiz = null;
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
}

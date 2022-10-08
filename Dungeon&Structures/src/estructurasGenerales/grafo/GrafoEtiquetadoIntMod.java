package estructurasGenerales.grafo;

import estructurasGenerales.propositoEspecifico.Conjunto;
import estructurasGenerales.lineales.Lista;
import estructurasGenerales.lineales.Cola;
import utiles.Dato;

public class GrafoEtiquetadoIntMod {
    
    //  Variable para mantener puntero al inicio
    private NodoVertInt inicio;
    
    //  Constructor, lo crea vacio
    public GrafoEtiquetadoIntMod(){
        this.inicio = null;
        //Camino camino; 
    }
    
    //  Dado un elemento de TipoVertice se lo agrega a la estructura controlando 
    //      que no se inserten vértices repetidos. Si puede realizar la inserción
    //      devuelve verdadero, en caso contrario devuelve falso.
    public boolean insertarVertice(Object vertice){
        
        //  Variable para guardar el exito
        boolean exito = true;

        //  Si no existe
        if(ubicarVertice(vertice) == null){
            //  Lo crea como el primer vertice
            inicio = new NodoVertInt(vertice, inicio);
        } else {
            exito = false;
        }
        
        return exito;
    }
    

    //  Dado un elemento de TipoVertice se lo quita de la estructura. Si se encuentra el vértice, también
    //      deben eliminarse todos los arcos que lo tengan como origen o destino. Si se puede realizar la
    //      eliminación con éxito devuelve verdadero, en caso contrario devuelve falso.
    public boolean eliminarVertice(Object vertice){
        
        //  Variable para guardar el exito
        boolean exito;
        
        exito = eliminarVerticeAux(vertice, inicio, null);
        
        return exito;
    }
    
    private boolean eliminarVerticeAux(Object verticeObjetivo, NodoVertInt verticeActual, NodoVertInt verticePadre){
        boolean exito;
        //  Si el vertice actual es nulo, se llego al final sin encontrar el vertice, devuelve falso
        if(verticeActual == null){
            exito = false;
        } else {
            if(verticeActual.getElem().equals(verticeObjetivo)){
                //  Debemos borrar el vertice, pero primero debemos eliminar 
                //      todos los arcos que vienen hacia el vertice
                
                //  Consigue el primer adyacente del vertice
                NodoAdyInt auxAdy = verticeActual.getPrimerAdy();

                //  Mientras queden arcos por revisar
                while(auxAdy != null){
                    //  Elimina el arco opuesto (No es necesario eliminar el arco que 
                    //      empieza de este vertice, el garbage collector se encargara de eso)
                    eliminarArcoNodos(auxAdy.getVertice(), verticeActual, auxAdy.getVertice().getPrimerAdy(), null);
                    //  Va al siguiente
                    auxAdy = auxAdy.getSigAdyacente();
                }
                
                //  Luego borra el vertice
                if(verticeActual == inicio){
                    inicio = inicio.getSigVertice();
                } else {
                    verticePadre.setSigVert(verticeActual.getSigVertice());
                }
                
                exito = true;
            } else {
                exito = eliminarVerticeAux(verticeObjetivo, verticeActual.getSigVertice(), verticeActual);
            }
        }
        return exito;
    }
    
    //  Funcion para eliminar un arco en un solo sentido y recibiendo como parametros ya los vertices
    private boolean eliminarArcoNodos(NodoVertInt nodoOrigen, NodoVertInt nodoDestino, NodoAdyInt arcoActual, NodoAdyInt arcoPadre){
        boolean exito;
        //  Si llego al final, no hay arco en este sentido
        if(arcoActual == null){
            exito = false;
        } else {
            //  Si es el arco que se busca, se elmina
            if(arcoActual.getVertice() == nodoDestino){
                //  Si no tiene arco padre, es el primer arco
                if(arcoPadre == null){
                    //  Se reajusta el primer adyacente 
                    nodoOrigen.setPrimerAdy(arcoActual.getSigAdyacente());
                } else {
                    //  Si tiene padre, se ajusta el siguiente adyacente del padre
                    arcoPadre.setSigAdyacente(arcoActual.getSigAdyacente());
                }
                exito = true;
            } else {
                //  Si no es el arco buscado, se llama recursivamente para el siguiente arco
                exito = eliminarArcoNodos(nodoOrigen, nodoDestino, arcoActual.getSigAdyacente(), arcoActual);
            }
        }
        return exito;
    }
    
    //  Dados dos elementos de TipoVertice (origen y destino) agrega el arco en la estructura, 
    //      sólo si ambos vértices ya existen en el grafo. Si puede realizar la inserción 
    //      devuelve verdadero, en caso contrario devuelve falso.
    public boolean insertarArco(Object origen, Object destino, int etiqueta){
        
        //  Variable que guarda el exito
        boolean exito = false;
        
        Dato<NodoVertInt> nodoOrigen = new Dato(null), nodoDestino = new Dato(null);
        
        if(ubicarVertices(origen, destino, nodoOrigen, nodoDestino)){
            //  Si fueron encontrados los vertices, entonces 
            //      crea una variable para recorrer los adyacentes al vertice origen
            NodoAdyInt auxAdy = nodoOrigen.get().getPrimerAdy();
            boolean existeArco = false;

            // Solo se necesita checkear si existe en un sentido, ya que si no existe en un
            //      sentido, no deberia existir en el otro
            while(auxAdy != null && !existeArco){
                if(auxAdy.getVertice().getElem().equals(destino)){
                    existeArco = true;
                } else {
                    auxAdy = auxAdy.getSigAdyacente();
                }
            }

            if(!existeArco){
                nodoOrigen.get().setPrimerAdy(new NodoAdyInt(nodoDestino.get(), etiqueta, nodoOrigen.get().getPrimerAdy()));
                nodoDestino.get().setPrimerAdy(new NodoAdyInt(nodoOrigen.get(), etiqueta, nodoDestino.get().getPrimerAdy()));
                exito = true;
            }
        }
        
        return exito;
    }
    
    
    //  Dados dos elementos de TipoVertice (origen y destino) se quita de la estructura el arco que une
    //      ambos vértices. Si el arco existe y se puede realizar la eliminación con éxito devuelve verdadero, en
    //      caso contrario devuelve falso.
    public boolean eliminarArco(Object origen, Object destino){
        
        //  Variable que guarda el exito
        boolean exito = false;
        
        //  Si esta vacio claramente no existen los vertices, devuelve el exito falso
        if(!this.esVacio()){
            
            Dato<NodoVertInt> nodoOrigen = new Dato(null), nodoDestino = new Dato(null);
        
            if(ubicarVertices(origen, destino, nodoOrigen, nodoDestino)){
                
                //  Intenta eliminar el arco de origen a destino
                if(eliminarArcoNodos(nodoOrigen.get(), nodoDestino.get(), nodoOrigen.get().getPrimerAdy(), null)){
                    //  Si lo elimina, significa que existe el de destino a origen, asi que lo elimina
                    eliminarArcoNodos(nodoDestino.get(), nodoOrigen.get(), nodoDestino.get().getPrimerAdy(), null);
                }
            }
        }
        
        return exito;
    }

    //  Dado un elemento, devuelve verdadero si está en la estructura y falso en caso contrario.
    public boolean existeVertice(Object vertice){
        return ubicarVertice(vertice) != null;
    }
    
    //  Dados dos elementos de TipoVertice (origen y destino), devuelve verdadero 
    //      si existe un arco en la estructura que los une y falso en caso contrario.
    public boolean existeArco(Object origen, Object destino){
        
        //  Variable que guarda si existe
        boolean existe = false;
        
        //  Si esta vacio claramente no existen los vertices, devuelve el existe falso
        if(!this.esVacio()){
            
            Dato<NodoVertInt> nodoOrigen = new Dato(null), nodoDestino = new Dato(null);
        
            if(ubicarVertices(origen, destino, nodoOrigen, nodoDestino)){
                //  Variable que se usa para recorrer la estructura de adyacentes al origen
                NodoAdyInt auxAdy = nodoOrigen.get().getPrimerAdy();

                //  Mientras queden adyacentes por revisar y no se haya encontrado el
                //      nodo adyacente buscado
                while(auxAdy != null && !existe){
                    //  Si es el adyacente buscado
                    if(auxAdy.getVertice().getElem().equals(destino)){
                        //  Fue encontrado, por lo que existe es verdadero
                        existe = true;
                    } else {
                        //  Si no lo es, va al siguiente
                        auxAdy = auxAdy.getSigAdyacente();
                    }
                }
            }
        }
        
        return existe;
    } 
    
    public boolean modificarArco(Object origen, Object destino, int nuevaDistancia){
        
        boolean exito = false;
        
        if(!this.esVacio()){
            Dato<NodoVertInt> nodoOrigen = new Dato(null), nodoDestino = new Dato(null);
        
            if(ubicarVertices(origen, destino, nodoOrigen, nodoDestino)){
                //  Variable que se usa para recorrer la estructura de adyacentes al origen
                NodoAdyInt auxAdy = nodoOrigen.get().getPrimerAdy();
                
                //  Mientras queden adyacentes por revisar y no se haya encontrado el
                //      nodo adyacente buscado
                while(auxAdy != null && !exito){
                    //  Si es el adyacente buscado
                    if(auxAdy.getVertice().getElem().equals(destino)){
                        //  Fue encontrado, por lo que recupera la etiqueta
                        exito = true;
                        auxAdy.setEtiqueta(nuevaDistancia);
                    } else {
                        //  Si no lo es, va al siguiente
                        auxAdy = auxAdy.getSigAdyacente();
                    }
                }
            }
        }
        
        return exito;
    }

    //  Dados dos elementos de TipoVertice (origen y destino), devuelve el valor 
    //      de la etiqueta del arco en la estructura que los une. De no existir
    //      tal arco, devuleve -1.
    public int recuperarEtiqueta(Object origen, Object destino){
        
        //  Variable que guarda si existe
        int etiqueta = -1;
        
        //  Si esta vacio claramente no existen los vertices, devuelve -1
        if(!this.esVacio()){
            
            Dato<NodoVertInt> nodoOrigen = new Dato(null), nodoDestino = new Dato(null);
        
            if(ubicarVertices(origen, destino, nodoOrigen, nodoDestino)){
                //  Variable que se usa para recorrer la estructura de adyacentes al origen
                NodoAdyInt auxAdy = nodoOrigen.get().getPrimerAdy();
                
                //  Mientras queden adyacentes por revisar y no se haya encontrado el
                //      nodo adyacente buscado
                while(auxAdy != null && etiqueta == -1){
                    //  Si es el adyacente buscado
                    if(auxAdy.getVertice().getElem().equals(destino)){
                        //  Fue encontrado, por lo que recupera la etiqueta
                        etiqueta = auxAdy.getEtiqueta();
                    } else {
                        //  Si no lo es, va al siguiente
                        auxAdy = auxAdy.getSigAdyacente();
                    }
                }
            }
        }
        
        return etiqueta;
    } 
    
    //  Dados dos elementos de TipoVertice (origen y destino), devuelve verdadero si existe al menos
    //      un camino que permite llegar del vértice origen al vértice destino y falso en caso contrario.
    public boolean existeCamino(Object origen, Object destino){
        
        //  Variable que guarda si existe
        boolean existe = false;
        
        //  Si esta vacio claramente no existen los vertices, devuelve el existe falso
        if(!this.esVacio()){
            
            Dato<NodoVertInt> nodoOrigen = new Dato(null), nodoDestino = new Dato(null);
        
            if(ubicarVertices(origen, destino, nodoOrigen, nodoDestino)){
                //  De ser encontrados, crea una variable conjunto que guardara que
                //      vertices ya fueron visitados para evitar bucles.
                Conjunto verticesRecorridos = new Conjunto();
                //  Llama a la funcion auxiliar para saber si existe un camino
                existe = existeCaminoAux(nodoOrigen.get(), destino, verticesRecorridos);
            }
        }
        
        return existe;
    }
        
    //  Funcion privada auxiliar para la funcion de existe camino. Recorre recursivamente todos sus
    //      adyacentes que no hayan sido visitados
    private boolean existeCaminoAux(NodoVertInt vertice, Object destino, Conjunto verticesRecorridos){
        
        //  Variable para guardar el exito
        boolean existe = false;
        
        //  Se agrega el vertice en el que estamos para no visitarlo dos veces
        verticesRecorridos.agregar(vertice.getElem());
        
        if(vertice.getElem().equals(destino)){
            existe = true;
        } else {
            //  Auxiliar para recorrer los nodos adyacentes del nodo vertice
            NodoAdyInt aux = vertice.getPrimerAdy();

            //  Mientras tenga adyacentes que recorrer y no haya encontrado un camino 
            while(aux != null && !existe){
                
                //  Variable para referenciar mas facilmente el vertice del adyacente actual
                NodoVertInt auxVert = aux.getVertice();

                //  Nos fijamos si ya fue visitado para evitar bucles
                if(!verticesRecorridos.pertenece(auxVert.getElem())){
                    //  Si no esta entre los vertices viistados, entonces se llama
                    //      recursivamente para checkear este posible caminno, con el
                    //      nuevo vertice y un clon del conjunto, para que las llamadas
                    //      recursivas no modifique este clon. 
                    existe = existeCaminoAux(auxVert, destino, verticesRecorridos);
                }

                //  Luego avanza al siguiente adyacente al auxiliar
                aux = aux.getSigAdyacente();
            }
        }
        
        verticesRecorridos.quitar(vertice.getElem());
        
        return existe;
    } 
    
   
    //  Dados dos elementos de TipoVertice (origen y destino), devuelve un camino 
    //      (lista de vértices) que indique el camino que pasa por menos vértices 
    //      que permite llegar del vértice origen al vérticedestino. Si hay más 
    //      de un camino con igual cantidad de vértices, devuelve cualquiera de ellos. Si
    //      alguno de los vértices no existe o no hay camino posible entre ellos devuelve la lista vacía.
    public Lista caminoMasCorto(Object origen, Object destino){
        
        Lista resultado = new Lista();
        
        //  Si esta vacio claramente no existen los vertices, devuelve la lista vacia
        if(!this.esVacio()){
            
            Dato<NodoVertInt> nodoOrigen = new Dato(null), nodoDestino = new Dato(null);
        
            if(ubicarVertices(origen, destino, nodoOrigen, nodoDestino)){
                caminoMasCortoAux(nodoDestino.get(), destino, new Lista(), resultado);
            }
        }
        return resultado;
    }
       
    //  Funcion privada auxiliar de caminoMasCorto, devuleve el camino mas corto en el parametro resultado
    private void caminoMasCortoAux(NodoVertInt vertice, Object destino, Lista caminoRecorrido, Lista caminoPosible){
        
        //  Se agrega al camino recorrido
        caminoRecorrido.insertar(vertice.getElem(), caminoRecorrido.longitud() + 1);
         
        if(vertice.getElem().equals(destino)){
            caminoPosible.vaciar();
            for(int i = 1; i <= caminoRecorrido.longitud(); i++){
                caminoPosible.insertar(caminoRecorrido.recuperar(i), i);
            }
        } else {
            //  Variable para recorrer el arreglo
            NodoAdyInt aux = vertice.getPrimerAdy();

            //  Mientras no se hayan recorrido todos los adyacentes
            while(aux!=null){
                //  Si todavia no se encontro un camino posible, O si se encontro pero el camino actual es mas corto
                //      que la longitud del otro - 1 (ya que si al agregar el siguiente vertice queda igual que el camino mas corto
                //      no sirve)
                if((caminoPosible.esVacia()) || (caminoRecorrido.longitud() < caminoPosible.longitud() - 1)){
                    //  Nos fijamos si ya fue visitado para evitar bucles
                    if(caminoRecorrido.localizar(aux.getVertice().getElem()) < 0){
                        //  Si no esta entre los vertices visitados, entonces se llama
                        //      recursivamente para checkear este posible caminno.
                        caminoMasCortoAux(aux.getVertice(), destino, caminoRecorrido, caminoPosible);
                    } 
                }
                aux = aux.getSigAdyacente();
            }
        }
        
        caminoRecorrido.eliminar(caminoRecorrido.longitud());
    }
    
    //  Dados dos elementos de TipoVertice (origen y destino), devuelve un camino 
    //      (lista de vértices) que indique el camino que pasa por más vértices (sin ciclos) 
    //      que permite llegar del vértice origen al vértice destino. Si hay más de un camino 
    //      con igual cantidad de vértices, devuelve cualquiera de ellos. Si alguno de los 
    //      vértices no existe o no hay camino posible entre ellos devuelve la lista vacía.
    public Lista caminoMasLargo(Object origen, Object destino){
        
        Lista resultado = new Lista();
        
        //  Si esta vacio claramente no existen los vertices, devuelve la lista vacia
        if(!this.esVacio()){
            
            Dato<NodoVertInt> nodoOrigen = new Dato(null), nodoDestino = new Dato(null);
        
            if(ubicarVertices(origen, destino, nodoOrigen, nodoDestino)){
                Lista caminoRecorrido = new Lista();
                caminoMasLargoAux(nodoOrigen.get(), destino, caminoRecorrido, resultado);
            }
        }
        
        return resultado;
    }
       
    //  Funcion privada auxiliar de caminoMasLargo, devuleve el camino mas largo en el parametro resultado
    private void caminoMasLargoAux(NodoVertInt vertice, Object destino, Lista caminoRecorrido, Lista caminoPosible){
        
        //  Se agrega al camino recorrido
        caminoRecorrido.insertar(vertice.getElem(), caminoRecorrido.longitud() + 1);

        if(vertice.getElem().equals(destino)){
            if(caminoRecorrido.longitud() > caminoPosible.longitud()){
                caminoPosible.vaciar();
                for(int i = 1; i <= caminoRecorrido.longitud(); i++){
                    caminoPosible.insertar(caminoRecorrido.recuperar(i), i);
                }
            }
        } else {
            //  Variable para recorrer el arreglo
            NodoAdyInt aux = vertice.getPrimerAdy();

            //  Mientras no se hayan recorrido todos los adyacentes
            while(aux!=null){
                //  Nos fijamos si ya fue visitado para evitar bucles
                if(caminoRecorrido.localizar(aux.getVertice().getElem()) < 0){
                    //  Si no esta entre los vertices visitados, entonces se llama
                    //      recursivamente para checkear este posible caminno.
                    caminoMasLargoAux(aux.getVertice(), destino, caminoRecorrido, caminoPosible);
                }
                aux = aux.getSigAdyacente();
            }
        }
        
        caminoRecorrido.eliminar(caminoRecorrido.longitud());
    }
  
    //  Devuelve una lista con los vértices del grafo visitados según el recorrido 
    //      en profundidad explicado en la sección anterior.
    public Lista listarEnProfundidad(){
        
        Lista resultado = new Lista();
        
        NodoVertInt aux = inicio;
        
        while(aux != null){
            if(resultado.localizar(aux.getElem()) < 0){
                profundidadDesde(aux, resultado);
            }
            aux = aux.getSigVertice();
        }
        
        return resultado;
    }

    //  Funcion privada auxiliar para listarEnProfundidad
    private void profundidadDesde(NodoVertInt vertice, Lista visitados){
        
        visitados.insertar(vertice.getElem(), visitados.longitud() + 1);
        
        NodoAdyInt aux = vertice.getPrimerAdy();
        
        while(aux != null){
            if(visitados.localizar(aux.getVertice().getElem()) < 0){
                profundidadDesde(aux.getVertice(), visitados);
            }
            aux = aux.getSigAdyacente();
        }
    }
    
    //  Devuelve una lista con los vértices del grafo visitados según el recorrido 
    //      en anchura explicado en la sección anterior.
    public Lista listarEnAnchura(){
        
        Lista resultado = new Lista();
        
        NodoVertInt aux = inicio;
        
        while(aux != null){
            if(resultado.localizar(aux.getElem()) < 0){
                anchuraDesde(aux, resultado);
            }
            aux = aux.getSigVertice();
        }
        
        return resultado;
    }
        
    //  Funcion privada auxiliar para listarEnAnchura
    private void anchuraDesde(NodoVertInt vertice, Lista visitados){
        
        Cola colaAux = new Cola();
        
        visitados.insertar(vertice.getElem(), visitados.longitud() + 1);
        
        colaAux.poner(vertice);
        NodoVertInt auxVert;
        NodoAdyInt auxAdy;
        
        while(!colaAux.esVacia()){
            auxVert = (NodoVertInt) colaAux.obtenerFrente();
            colaAux.sacar();
            auxAdy = auxVert.getPrimerAdy();
            while(auxAdy != null){
                if(visitados.localizar(auxAdy.getVertice().getElem()) < 0){
                    visitados.insertar(auxAdy.getVertice().getElem(), visitados.longitud() + 1);
                    colaAux.poner(auxAdy.getVertice());
                }
                auxAdy = auxAdy.getSigAdyacente();
            }
        }
        
    }
    
    public boolean esVacio(){
        return this.inicio == null;
    }
    
    public void vaciar(){
        this.inicio = null;
    }
           
    //  Genera y devuelve un grafo que es equivalente (igual estructura y contenido 
    //      de los nodos) al original. 
    @Override
    public GrafoEtiquetadoIntMod clone(){
        
        //  Nuevo grafo que se pasara como resultado final
        GrafoEtiquetadoIntMod resultado = new GrafoEtiquetadoIntMod();
        
        //  Nodo vertice auxiliar que usaremos para recorrer la estructura
        NodoVertInt aux = inicio;
        
        //  Primero inserta todos los vertices, ya que si intentamos insertar algun arco
        //      a un vertice que no exista no funcionara
        while(aux != null){
            //  Insertarmos el vertice
            resultado.insertarVertice(aux.getElem());
            //  Y pasamos al siguiente
            aux = aux.getSigVertice();
        }
        
        //  Conjunto que servira para saber por que elemenots ya pasamos
        Conjunto visitados = new Conjunto();
        //  Volvemos al inicio
        aux = inicio;
        //  Nodo adyacente auxiliar que nos servira para recorrer todos los adyacentes
        //      de un nodo vertice dado, es decir, sus arcos
        NodoAdyInt auxAdy;
        
        //  Recorremos toda la estructura nuevamente
        while(aux != null){
            
            //  Agregamos el elemento al conjunto de visitados
            visitados.agregar(aux.getElem());
            //  Conseguimos su primer adyacente o arco
            auxAdy = aux.getPrimerAdy();
            
            //  Recorremos todos sus arcos
            while(auxAdy != null){
                //  Si el elemento del nodo al que va el arco ya fue visitado, esto significa
                //      que el arco desde alla hasta este vertice ya fue creado y por ende
                //      tambien el de este vertice hacia alla, ya que es un grafo y no digrafo
                
                //  Se podria evitar este if y este while y simplemente poner que intente insertar 
                //      todos los arcos en el primer while del metodo. Pero de esta forma
                //      intentariamos tantos insertar arco fallidos que no seria eficiente.
                if(!visitados.pertenece(auxAdy.getVertice().getElem())){
                    //  Pero si no fue visitado, entonces agrega el arco. 
                    resultado.insertarArco(aux.getElem(), auxAdy.getVertice().getElem(), auxAdy.getEtiqueta());
                }
                //  Avanza al siguiente arco
                auxAdy = auxAdy.getSigAdyacente();
            }
            //  Avanza al siguiente vertice
            aux = aux.getSigVertice();
        }
        
        return resultado;
    }

    //  Con fines de debugging, este método genera y devuelve una cadena String que muestra los
    //      vértices almacenados en el grafo y qué adyacentes tiene cada uno de ellos.
    @Override
    public String toString(){
        
        String resultado;
        
        if(this.esVacio()){
            resultado = "Mapa vacio";
        } else {
            
            resultado = "Formato del mapa. <Locacion> : {<Locacion_Destino>, <Distancia_KM>} -  ... \n";
            NodoVertInt aux = inicio;

            while(aux != null){
                
                resultado += "\n " + aux.getElem().toString() + ": ";
                
                NodoAdyInt auxAdy = aux.getPrimerAdy();
                
                while(auxAdy != null){
                    resultado += " { " + auxAdy.getVertice().getElem().toString() + ", " + auxAdy.getEtiqueta() + " } -";
                    auxAdy = auxAdy.getSigAdyacente();
                }
                
                aux = aux.getSigVertice();
            }
        }
        
        return resultado;
    }
    
    //  Funcion que dado el elemento de un vertice, devuelve los elementos de todos sus
    //      vertices adyacentes
    public Lista conseguirAdyacentes(Object vertice){
        
        //  Lista que se devolvera como resultado
        Lista resultado = new Lista();
        
        //  Nodo vertice auxiliar que guarda la posicion del vertice buscado
        NodoVertInt nodoVertice = ubicarVertice(vertice);
        
        //  Si existe
        if(nodoVertice != null){
            //  Nodo adyacente auxiliar para recorrer todos los adyacentes del vertice
            NodoAdyInt auxAdy = nodoVertice.getPrimerAdy();
            
            //  Mientras queden adyacentes por recorrer
            while(auxAdy != null){
                //  Inserta el elemento del vertice adyacente en la lista
                resultado.insertar(auxAdy.getVertice().getElem(), 1);
                //  Y pasa al siguiente adyacente
                auxAdy = auxAdy.getSigAdyacente();
            }
        }
        
        return resultado;
    }
    
    public Lista caminoMasCortoEtiqueta(Object origen, Object destino){
        
        Lista resultado = new Lista();
        
        //  Si esta vacio claramente no existen los vertices, devuelve la lista vacia
        if(!this.esVacio()){
            
            Dato<NodoVertInt> nodoOrigen = new Dato(null), nodoDestino = new Dato(null);
        
            if(ubicarVertices(origen, destino, nodoOrigen, nodoDestino)){
                //  Llama a la funcion privada para conseguir el camino mas corto
                caminoMasCortoEtiquetaAux(nodoOrigen.get(), destino, new Lista(), 0, resultado, new Dato<Integer>(-1));
            }
        }
        
        return resultado;
    }
       
    //  Funcion privada auxiliar de caminoMasCorto, devuleve el camino mas corto
    private void caminoMasCortoEtiquetaAux(NodoVertInt vertice, Object destino, Lista recorridoActual, int distanciaActual, Lista caminoPosible, Dato<Integer> menorDistancia){

        //  Se agrega al camino recorrido
        recorridoActual.insertar(vertice.getElem(), recorridoActual.longitud() + 1);
        
        if(menorDistancia.get() < 0 || distanciaActual < menorDistancia.get()){
            if(vertice.getElem().equals(destino)){
                caminoPosible.vaciar();
                for(int i = 1; i <= recorridoActual.longitud(); i++){
                    caminoPosible.insertar(recorridoActual.recuperar(i), i);
                }
                menorDistancia.set(distanciaActual);
            } else {
                NodoAdyInt aux = vertice.getPrimerAdy();
                while(aux != null){
                    if(recorridoActual.localizar(aux.getVertice().getElem()) < 0){
                        caminoMasCortoEtiquetaAux(aux.getVertice(), destino, recorridoActual, distanciaActual + aux.getEtiqueta(), caminoPosible, menorDistancia);
                    }
                    aux = aux.getSigAdyacente();
                }
            }
        }
        
        recorridoActual.eliminar(recorridoActual.longitud());
    }
    
    //  Funcion que devuelve todos los caminos posibles entre dos elementos, sin que superen un limite de distancia
    public Lista caminosMenoresKm(Object origen, Object destino, int limiteKm){
        
        Lista resultado = new Lista();
        
        if(!this.esVacio()){
            
            Dato<NodoVertInt> nodoOrigen = new Dato(null), nodoDestino = new Dato(null);
        
            if(ubicarVertices(origen, destino, nodoOrigen, nodoDestino)){
                //  Llama a la funcion privada para conseguir el camino mas corto
                caminosMenoresKmAux(nodoOrigen.get(), destino, limiteKm, new Lista(), 0, resultado);
            }
        }
        
        return resultado;
    }
    
    private void caminosMenoresKmAux(NodoVertInt vertice, Object destino, int limiteKm, Lista recorridoActual, int distanciaActual, Lista resultado){
        
        
        if(distanciaActual < limiteKm){
            //  Se agrega al camino recorrido
            recorridoActual.insertar(vertice.getElem(), recorridoActual.longitud() + 1);
            if(vertice.getElem().equals(destino)){
                resultado.insertar(recorridoActual.clone(), 1);
            } else {
                NodoAdyInt aux = vertice.getPrimerAdy();
                
                while(aux != null){
                    if(recorridoActual.localizar(aux.getVertice().getElem()) < 0){
                        caminosMenoresKmAux(aux.getVertice(), destino, limiteKm, recorridoActual, distanciaActual + aux.getEtiqueta(), resultado);
                    }
                    aux = aux.getSigAdyacente();
                }
            }
            recorridoActual.eliminar(recorridoActual.longitud());
        }
    }
    
    public Lista caminosPosiblesSinVertice(Object origen, Object destino, Object verticeIgnorada){
        
        Lista resultado = new Lista();
        
        if(!this.esVacio()){
            
            Dato<NodoVertInt> nodoOrigen = new Dato(null), nodoDestino = new Dato(null);
        
            if(ubicarVertices(origen, destino, nodoOrigen, nodoDestino)){
                //  Llama a la funcion privada para conseguir el camino mas corto
                caminosPosiblesSinVerticeAux(nodoOrigen.get(), destino, verticeIgnorada, new Lista(), resultado);
            }
        }
        
        return resultado;
    }
    
    private void caminosPosiblesSinVerticeAux(NodoVertInt vertice, Object destino, Object verticeIgnorada, Lista recorridoActual, Lista resultado){
        
        Object elemento = vertice.getElem();
        
        if(!elemento.equals(verticeIgnorada) && recorridoActual.localizar(elemento) < 0){
            recorridoActual.insertar(vertice.getElem(), recorridoActual.longitud() + 1);

            if(vertice.getElem().equals(destino)){
                resultado.insertar(recorridoActual.clone(), 1);
            } else {
                NodoAdyInt aux = vertice.getPrimerAdy();

                while(aux != null){
                    caminosPosiblesSinVerticeAux(aux.getVertice(), destino, verticeIgnorada, recorridoActual, resultado);
                    aux = aux.getSigAdyacente();
                }
            }

            recorridoActual.eliminar(recorridoActual.longitud());
        }
    }
    
    private NodoVertInt ubicarVertice(Object vertice){
        
        NodoVertInt resultado = null;
        
        //  Si esta vacio, no se hace nada, devuelve null
        if(!this.esVacio()){
            //  Aux sirve para recorrer la estructura buscando el vertice correcto
            NodoVertInt aux = inicio;

            //  Mientras no haya recorrido toda la estructura ni haya encontrado el elemento
            while(aux != null && resultado == null){
                //  Si aux es el vertice que buscamos
                if(aux.getElem().equals(vertice)){
                    //  Lo devolvemos como resultado
                    resultado = aux;
                } else {
                    //  Si no es el vertice que buscamos, pasamos al siguiente
                    aux = aux.getSigVertice();
                }
            }
        }
        
        return resultado;
    }
    
    private boolean ubicarVertices(Object origen, Object destino, Dato<NodoVertInt> nodoOrigen, Dato<NodoVertInt> nodoDestino){

        //  Variable para saber cuando fueron encontrados ambos vertices y no recorrer
        //      de mas la estructura y lo devuelve al final
        boolean encontrados = false;
        
        //  Variable para recorrer la estructura
        NodoVertInt aux = inicio;

        //  Mientras no haya recorrido toda la estructura o no haya encontrado ambos vertices
        while(aux != null && !encontrados){
            //  Si es el vertice origen que se buscaba
            if(aux.getElem().equals(origen)){
                //  Lo guardamos en su variable
                nodoOrigen.set(aux);
                //  Y si ya se encontro el vertice de destino
                if(nodoDestino.get() != null){
                    //  Se pone en true encontrados
                    encontrados = true;
                }
            } else {
                //  Si no es el origen, revisamos si es el destino
                if(aux.getElem().equals(destino)){
                    //  Si lo es, entonces lo guardamos en su variable
                    nodoDestino.set(aux);
                    //  Y si tambien se encontro el vertice de origen
                    if(nodoOrigen.get() != null){
                        //  Entonces se pone en true encontrados
                        encontrados = true;
                    }
                }
            }
            //  Luego seguimos al siguiente vertice
            aux = aux.getSigVertice();
        }
        
        return encontrados;
    }
}
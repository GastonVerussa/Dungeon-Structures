package estructurasGenerales.propositoEspecifico;

import clases.unidades.Equipo;
import clases.unidades.Jugador;
import estructurasGenerales.FuncionesHash.*;
import estructurasGenerales.lineales.Lista;
import estructurasGenerales.lineales.Pila;

public class DiccionarioHash {
    
    private final int TAMANIO;
    private final NodoHashDicc[] tabla;
    private int cant;
    
    
    //  Crea una estructura sin elementos.
    public DiccionarioHash(){
        TAMANIO = 30;
        tabla = new NodoHashDicc[TAMANIO];
        cant = 0;
    }

    //  Crea una estructura sin elementos.
    public DiccionarioHash(int tamanio){
        this.TAMANIO = tamanio;
        tabla = new NodoHashDicc[tamanio];
        cant = 0;
    }
    
    //  Recibe la clave que es única y el dato (información asociada a ella). Si no existe en la estructura
    //      un elemento con igual clave, agrega el par (clave, dato) a la estructura. Si la operación termina con
    //      éxito devuelve verdadero y falso en caso contrario.
    public boolean insertar(Object clave, Object dato){
        
        //  Variable que guarda el resultado
        boolean exito = true;
        
        //  Calcula la posicion
        int posicion = calcularPosicion(clave);
        //  Auxiliar para revisar la lista en la posicion
        NodoHashDicc auxiliar = tabla[posicion];
        
        //  Mientras no se haya llegado al final de la lista ni haya ocurrido un error
        while(auxiliar != null && exito){
            //  Si encuentra el elemento
            if(auxiliar.getClave().equals(clave)){
                //  Entonces esta repetido, por lo que no hay exito
                exito = false;
            } else {
                //  Si no es el elemento, se consigue el enlace para seguir revisando
                auxiliar = auxiliar.getEnlace();
            }
        }
        
        //  Si hubo exito, es decir que no esta repetido el elemento
        if(exito){
            //  Entonces asigna el inicio de la lista de la posicion como un nuevo nodo y lo enlaza al viejo inicio
            tabla[posicion] = new NodoHashDicc(clave, dato, tabla[posicion]);
            //  Aumenta la cantidad
            cant++;
        }
        
        return exito;
    }

    //  Elimina el elemento cuya clave sea la recibida por parámetro. Si lo encuentra y la operación de
    //      eliminación termina con éxito devuelve verdadero y falso en caso contrario.
    public boolean eliminar(Object clave){
        
        //  Variable que guarda el resultado, si la tabla esta vacia, exito es falso
        boolean exito = false;
        
        // Si la tabla no esta vacia
        if(!this.esVacio()){
            //  Calcula la posicion
            int posicion = calcularPosicion(clave);
            //  Auxiliar para revisar la lista en la posicion
            NodoHashDicc auxiliar = tabla[posicion];
            //  Si hay al menos un elemento en la posicion
            if(auxiliar != null){
                //  Caso base, si el primer nodo en la posicion tiene el elemento
                if(auxiliar.getClave().equals(clave)){
                    //  Su enlace pasa a ser la cabecera, en caso de que sea el unico nodo sera null
                    tabla[posicion] = auxiliar.getEnlace();
                    //  Se reduce la cantidad y se pasa el exito a verdadero
                    cant--;
                    exito = true;
                } else {
                    //  Si no es el primer nodo, se revisan los enlaces hasta que no hayan mas
                    //      o la funcion haya tenido exito (el elemento fue encontrado y borrado)
                    while(auxiliar.getEnlace() != null && !exito){
                        //  Si encuentra el elemento
                        if(auxiliar.getEnlace().getClave().equals(clave)){
                            //  Lo borra, reduce la cantidad y pasa el exito a verdadero
                            auxiliar.setEnlace(auxiliar.getEnlace().getEnlace());
                            cant--;
                            exito = true;
                        } else {
                            //  Si no es el elemento, se consigue el enlace para seguir revisando
                            auxiliar = auxiliar.getEnlace();
                        }
                    }
                }
            }
        }
        
        return exito;
    }

    //  Devuelve verdadero si en la estructura se encuentra almacenado un elemento con la clave recibida
    //      por parámetro, caso contrario devuelve falso.
    public boolean existeClave(Object clave){
        
        //  Variable que guarda el exito
        boolean exito = false;
        
        //  Calcula la posicion
        int posicion = calcularPosicion(clave);
        
        //  Variable para recorrer los enlaces
        NodoHashDicc auxiliar = tabla[posicion];
        
        //  Recorre los enlaces hasta llegar el final o haber tenido exito buscando el elemento
        while(auxiliar != null && !exito){
            //  Si encuentra el elemento
            if(auxiliar.getClave().equals(clave)){
                //  Entonces esta la clave buscada, hay exito
                exito = true;
            } else {
                //  Si no es el elemento, se consigue el enlace para seguir revisando
                auxiliar = auxiliar.getEnlace();
            }
        }
        
        return exito;
    }
    
    //  Si en la estructura se encuentra almacenado un elemento con la clave recibida por parámetro,
    //      devuelve la información asociada a ella. Precondición: si no existe un elemento con esa clave no se
    //      puede asegurar el funcionamiento de la operación.
    public Object obtenerInformacion(Object clave){
        
        //  Variable que guarda el exito
        Object resultado = null;
        
        //  Calcula la posicion
        int posicion = calcularPosicion(clave);
        
        //  Variable para recorrer los enlaces
        NodoHashDicc auxiliar = tabla[posicion];
        
        //  Recorre los enlaces hasta llegar el final o haber tenido exito buscando el elemento
        while(auxiliar != null && resultado == null){
            //  Si encuentra el elemento
            if(auxiliar.getClave().equals(clave)){
                //  Entonces esta la clave buscada, consigue su dato
                resultado = auxiliar.getDato();
            } else {
                //  Si no es el elemento, se consigue el enlace para seguir revisando
                auxiliar = auxiliar.getEnlace();
            }
        }
        
        return resultado;
    }
    
    //  Recorre la estructura completa y devuelve una lista ordenada con las claves de los elementos que
    //      se encuentran almacenados en ella.
    public Lista listarClaves(){
                
        //  Lista que se pasara como resultaod final
        Lista resultado = new Lista();
        
        //  Entero para saber en que posicion nos encontramos
        int posicion = 0;
        //  Auxiliar para recorrer la estructura
        NodoHashDicc auxiliar;
        
        //  Mientras la longitud de la lista sea menor a la cantidad de elementos en la tabla,
        //      Esto significa que faltan elementos por enlistar.
        while(resultado.longitud() < this.cant){
            //  Auxiliar toma el nodo en la posicion actual
            auxiliar = tabla[posicion];
            //  Mientras queden nodos por enlistar en la posicion
            while(auxiliar != null){
                //  Lo inserta al final de la lista
                resultado.insertar(auxiliar.getClave(), resultado.longitud() + 1);
                //  Y va al siguiente nodo
                auxiliar = auxiliar.getEnlace();
            }
            //  Cuando termina de reivsar todos los nodos de la posicion, pasa a la siguiente posicion
            posicion++;
        }
        
        return resultado;
    }

    //  Recorre la estructura completa y devuelve una lista ordenada con la información asociada de los
    //      elementos que se encuentran almacenados en ella.
    public Lista listarDatos(){
                        
        //  Lista que se pasara como resultaod final
        Lista resultado = new Lista();
        
        //  Entero para saber en que posicion nos encontramos
        int posicion = 0;
        //  Auxiliar para recorrer la estructura
        NodoHashDicc auxiliar;
        
        //  Mientras la longitud de la lista sea menor a la cantidad de elementos en la tabla,
        //      Esto significa que faltan elementos por enlistar.
        while(resultado.longitud() < this.cant){
            //  Auxiliar toma el nodo en la posicion actual
            auxiliar = tabla[posicion];
            //  Mientras queden nodos por enlistar en la posicion
            while(auxiliar != null){
                //  Lo inserta al final de la lista
                resultado.insertar(auxiliar.getDato(), resultado.longitud() + 1);
                //  Y va al siguiente nodo
                auxiliar = auxiliar.getEnlace();
            }
            //  Cuando termina de reivsar todos los nodos de la posicion, pasa a la siguiente posicion
            posicion++;
        }
        
        return resultado;
    }

    //  Devuelve falso si hay al menos un elemento cargado en la estructura y verdadero en caso contrario.
    public boolean esVacio(){
        return this.cant == 0;
    }
    
    public void vaciar(){
        cant = 0;
        for(int i = 0; i < TAMANIO; i++){
            tabla[i] = null;
        }
    }
    
    private int calcularPosicion(Object elemento){
        
        int resultado;    

        int elementoInt;
        
        //  Si no es int, le aplica una funcion para sumar los valores de todos los caracteres
        //      de su toString(), devolviendo un entero
        if(!elemento.getClass().getSimpleName().equals("Integer")){
            elementoInt = FuncionesString.hashCadena(elemento.toString(), TAMANIO);
        } else {
            //  Si es un int, simplemente obtiene su valor
            elementoInt = (int) elemento;
        }
        
        //  Luego aplica una funcion hash al valor obtenido
        
        //  Si no es muy grande (No le saca al menos dos digitos de diferencia al tamaño)
        if(elementoInt < TAMANIO * 100){
            //  Aplica al funcion cuadrado, esperando evitar conflictor
            resultado = FuncionesEnteros.hashCuadrado(elementoInt, TAMANIO);
        } else {
            //  Si es muy grande, aplica doblamiento, ya que es mas simple
            resultado = FuncionesEnteros.hashDoblamiento(elementoInt, TAMANIO);
        }

        return resultado;
    }
    
    @Override
    public DiccionarioHash clone(){
        
        //  Diccionario clon que se pasara como resultado final
        DiccionarioHash resultado = new DiccionarioHash();
        
        //  Entero para saber en que posicion nos encontramos
        int posicion = 0;
        //  Entero para saber cuantos elementos ya copiamos
        int elementosCopiados = 0;
        //  Auxiliar para recorrer la estructura
        NodoHashDicc auxiliar;
        //  Pila para copiar correcatmente las celdas con varios nodos, ya que van en orden inverso
        Pila pilaAuxiliar = new Pila();
        
        //  Mientras la cantidad de elementos copiados sea menor a la cantidad 
        //      de elementos en la tabla, esto significa que faltan elementos por copiar.
        while(elementosCopiados < this.cant){
            //  Auxiliar toma el nodo en la posicion actual
            auxiliar = tabla[posicion];
            //  Mientras queden nodos por copiar en la posicion
            while(auxiliar != null){
                //  Lo agrega a la pila
                pilaAuxiliar.apilar(auxiliar);
                //  Aumenta la cantidad de elementos copiados
                elementosCopiados++;
                //  Y va al siguiente nodo
                auxiliar = auxiliar.getEnlace();
            }
            while(!pilaAuxiliar.esVacia()){
                //  Auxiliar obtiene el tope y desapila
                auxiliar = (NodoHashDicc) pilaAuxiliar.obtenerTope();
                pilaAuxiliar.desapilar();
                //  Lo agrega al clon
                resultado.insertar(auxiliar.getClave(), auxiliar.getDato());
            }
            pilaAuxiliar.vaciar();
            //  Cuando termina de reivsar todos los nodos de la posicion, pasa a la siguiente posicion
            posicion++;
        }
        
        return resultado;
    }
    
    @Override
    public String toString(){
        
        //  String que se pasara como resultado final
        String resultado;
        
        if(this.esVacio()){
            resultado = "No hay equipos formados";
        } else {
            resultado = "Los equipos formados son: ";
            //  Entero para saber en que posicion nos encontramos
            int posicion = 0;
            //  Entero para saber cuantos elementos ya copiamos al String
            int elementosCopiados = 0;
            //  Auxiliar para recorrer la estructura
            NodoHashDicc auxiliar;

            //  Mientras la cantidad de elementos copiados sea menor a la cantidad 
            //      de elementos en la tabla, esto significa que faltan elementos por copiar.
            while(elementosCopiados < this.cant){
                //  Auxiliar toma el nodo en la posicion actual
                auxiliar = tabla[posicion];
                if(auxiliar != null)resultado += "\nElementos en posicion numero " + posicion + ": ";
                //  Mientras queden nodos por copiar en la posicion
                while(auxiliar != null){
                    //  Lo agrega al String
                    resultado += "\n Equipo " + auxiliar.getClave() + ". Formado por ";
                    Equipo equipo = ((Equipo) auxiliar.getDato());
                    Jugador[] jugadores = equipo.getParticipantes();
                    for(int i = 0; i < jugadores.length - 1; i++){
                        resultado += jugadores[i].getNombre() + " _ ";
                    }
                    resultado += " y " + jugadores[jugadores.length - 1].getNombre();
                    //  Aumenta la cantidad de elementos copiados
                    elementosCopiados++;
                    //  Y va al siguiente nodo
                    auxiliar = auxiliar.getEnlace();
                }
                //  Cuando termina de reivsar todos los nodos de la posicion, pasa a la siguiente posicion
                posicion++;
            }
        }
        
        return resultado;
    }
}

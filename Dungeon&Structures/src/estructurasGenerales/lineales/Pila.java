package estructurasGenerales.lineales;

public class Pila {
    
    // Atributos
    private Nodo tope;
    
    // Crea y devuelve la pila vacía.
    public Pila(){
        this.tope = null;
    }
    
    // Pone el elemento nuevoElem en el tope de la pila. Devuelve verdadero si el 
    //      elemento se pudo apilar y falso en caso contrario.
    public boolean apilar(Object nuevoElem){
        
        // Creo un nuevo nodo para el elemento, con enlace al tope actual
        Nodo nuevo = new Nodo(nuevoElem, this.tope);
        
        // Lo seteo como el nuevo tope
        this.tope = nuevo;
        
        // Vuelve siempre verdadero, ya que no falla por tamaño de pila completa
        return true;
    }

    // Saca el elemento del tope de la pila. Devuelve verdadero si la pila no estaba vacía al momento de
    //      desapilar (es decir que se pudo desapilar) y falso en caso contrario.
    public boolean desapilar(){
        
        // Booleano de control para saber si hubo exito
        boolean exito;
        
        // Checkea si la pila esta vacia
        if(this.esVacia()){
            // En caso de estar vacía falla.
            exito = false;
        } else {
            // Si no esta vacía, desapila el tope
            this.tope = this.tope.getEnlace();
            exito = true;
        }
        
        // Retorna el resultado
        return exito;
    }

    // Devuelve el elemento en el tope de la pila. Precondición: la pila no está vacía.
    public Object obtenerTope(){
        return this.tope.getElem();
    }
    
    // Devuelve verdadero si la pila no tiene elementos y falso en caso contrario.
    public boolean esVacia(){
        return this.tope == null;
    }

    // Saca todos los elementos de la pila.
    public void vaciar(){
        
        //  Desapila hasta que este vacía
        tope = null;
    }
    
    // Devuelve una copia exacta de los datos en la estructura original, y respetando el orden de los mismos,
    //      en otra estructura del mismo tipo
    @Override
    public Pila clone(){
        
        // Se crea una pilaFinal que será la que se devuelva y una auxiliar, que
        //      servirá en el proceso explicado a continuación
        Pila resultado = new Pila();
        
        cloneAux(tope, resultado);
        
        return resultado;
    }
    
    private void cloneAux(Nodo nodoActual, Pila resultado){
        if(nodoActual != null){
            cloneAux(nodoActual.getEnlace(), resultado);
            resultado.apilar(nodoActual.getElem());
        }
    }
    
    // Devuelve una cadena de caracteres formada por todos los elementos de la pila 
    //      para poder mostrarla por pantaalla. Es recomendable utilizar este 
    //      método únicamente en la etapa de prueba y luego comentar el código.

    @Override
    public String toString(){
        
        // String que se retornara al final
        String resultado;
        
        if(this.esVacia()){
            // Si la pila está vacía, devuelve este mensaje
            resultado = "Pila vacía";
        } else {
            // Si no está vacía, abre el corchete
            resultado = "[";
            
            // Nodo auxiliar para recorrer los elementos
            Nodo aux = this.tope;
            
            // Mientras el nodo no sea nulo
            while(aux != null){
                // Agrega el elemento al String
                resultado += aux.getElem().toString();
                // Y pasa a su enlace
                aux = aux.getEnlace();
                
                // Verifica para no agregar ", " al ultimo elemento
                if(aux != null)resultado += ", ";
            }
            
             // Cierra el corchete
            resultado += "]";
        }
        
        return resultado;
    }
}

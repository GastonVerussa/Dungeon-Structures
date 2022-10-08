package estructurasGenerales.propositoEspecifico;

import estructurasGenerales.lineales.Lista;

public class Conjunto {
    
    //  Se podria usar el nodo y no directamente la Lista para mayor eficiencia
    //      en el metodo eliminar, ya que se deberia usar el metodo de localizar 
    //      y luego el de eliminar con su posicion
    //      recorriendo la estructura dos veces en vez de una sola.
    
    //  Lista que contendra todos los elementos del conjunto
    private Lista elementos;
    
    //  Crea un conjunto sin elementos.
    public Conjunto(){
        this.elementos = new Lista();
    }

    //  Recibe un valor de tipo elemento. Si no existe previamente en el conjunto y 
    //      lo puede agregar con éxito devuelve verdadero y falso en caso contrario.
    public boolean agregar(Object valor){
        
        //  Variable para guardar el exito
        boolean exito;
        
        //  Variable para guardar la posicion del valor en la lista
        int posicion = elementos.localizar(valor);
        
        if(posicion != -1){
            //  El valor ya existe, por lo que no se puede volver a agregar
            exito = false;
        } else {
            //  Si no existe, entonces es cargado al comienzo de la lista
            exito = true;
            elementos.insertar(valor, 1);
        }
        
        return exito;
    }
    
    //  Elimina al elemento del conjunto. Si lo encuentra y la operación de eliminación 
    //      termina con éxito devuelve verdadero y falso en caso contrario.
    public boolean quitar(Object valor){
        
        //  Variable para guardar el exito
        boolean exito;
        
        //  Variable para guardar la posicion del elemento en la lista
        int posicion = elementos.localizar(valor);
        
        if(posicion == -1){
            //  El elemento no esta en la lista, falla
            exito = false;
        } else {
            //  Si el elemento esta en la lista, lo elimina y guarda el exito
            exito = elementos.eliminar(posicion);
        }
        
        return exito;
    }

    //  Si en el conjunto se encuentra almacenado el elemento ingresado por parámetro, 
    //      esta operación devuelve verdadero y falso en caso contrario.
    public boolean pertenece(Object valor){
        //  El metodo localizar de la lista devuelve -1 cuando el valor no pertenece a ella
        return elementos.localizar(valor) != -1;
    }

    //  Devuelve falso si hay al menos un elemento y verdadero en caso contrario.
    public boolean esVacio(){
        return elementos.esVacia();
    }

    //  Devuelve un Conjunto clon del original
    @Override
    public Conjunto clone(){
        
        //  Variable que se devolvera como resultado
        Conjunto resultado = new Conjunto();
        
        //  Lista auxiliar, clon de la lista de elementos
        Lista auxLista = elementos.clone();
        
        //  Mientras no este vacia, quedan elementos por clonar
        while(!auxLista.esVacia()){
            //  Agrega el primer elemento de la lista, el mas rapido para encontrar, al clon
            resultado.agregar(auxLista.recuperar(1));
            //  Y lo elimina de la lista
            auxLista.eliminar(1);
        }
        
        return resultado;
    }
    
    //  Devuelve un String con la informacion de los elementos que contiene
    @Override
    public String toString(){
        
        //  Variable que se devolvera como resultado
        String resultado;
        
        //  Si la lista esta vacia
        if(elementos.esVacia()){
            //  Entonces el resultado es el siguiente
            resultado = "Conjunto vacio";
        } else {
            //  Si no, entonces envia el String que devuelva la Lista
            resultado = elementos.toString();
        }
        
        return resultado;
    }
}

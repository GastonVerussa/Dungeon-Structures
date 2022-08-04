package clases.estructuras;

import clases.unidades.Item;
import conjuntistas.ArbolAVL;
import lineales.dinamicas.Lista;

public class TiendaItems {
    
    private final ArbolAVL arbol;

    public TiendaItems() {
        this.arbol = new ArbolAVL();
    }
    
    public boolean insertar(String codigo, String nombre, int precio, int puntosAtk, int puntosDef, int cantCopiasDisp){
        return arbol.insertar(new Item(codigo, nombre, precio, puntosAtk, puntosDef, cantCopiasDisp));
    }
    
    public boolean insertar(Item objeto){
        return arbol.insertar(objeto);
    }
    
    public boolean eliminar(Item objeto){
        return arbol.eliminar(objeto);
    }
    
    public boolean esVacia(){
        return arbol.esVacio();
    }
    
    //  Devuelve una lista ordenada de items con precio menor o igual a monto
    public Lista conseguirItemsAccesibles(int monto){
        //  Devuelve los items entre el rango de precio 0 y monto + 1 (Se le suma
        //      uno ya que el constructor crea item con el String de menor valor 
        //      al comparar)
        return arbol.listarRango(new Item(0), new Item(monto + 1));
    }
    
    //  Devuelve una lista ordenada de items con precio en el intervalo [minimo, maximo]
    public Lista conseguirItemsRango(int minimo, int maximo){
        //  Devuelve los items entre el rango de precio minimo y maximo + 1 (Se le suma
        //      uno ya que el constructor crea item con el String de menor valor 
        //      al comparar)
        return arbol.listarRango(new Item(minimo), new Item(maximo + 1));
    }
    
    public Lista recuperarItemsUnicos(){
        
        Lista resultado = new Lista();
        Lista listaItems = arbol.listar();
        Item aux;
        
        while(!listaItems.esVacia()){
            aux = (Item) listaItems.recuperar(1);
            listaItems.eliminar(1);
            if(aux.getCantCopiasDisp() == 1){
                resultado.insertar(aux, 1);
            }
        }
        
        return resultado;
    }
    
    //  Orden(N)
    public Lista recuperarItems(){
        return arbol.listar();
    }
    
    @Override
    public String toString(){
        return arbol.toString();
    }
}

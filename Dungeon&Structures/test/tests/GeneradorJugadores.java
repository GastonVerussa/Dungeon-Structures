package tests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import estructurasGenerales.lineales.Lista;
import estructurasGenerales.propositoEspecifico.Conjunto;

public class GeneradorJugadores {
    
    private static final String[] sustantivos = {"espada", "arco", "hacha", "escudo", "maza", "ballesta", "casco"};
    private static final String[] adjetivos1 = {"resplandeciente", "legendaria", "bipolar", "oscura", "plateada", "espectral", "runica"};
    private static final String[] adjetivos2 = {"del desierto", "de las profunidades", "del bosque", "enana", "elfica", "de la tormenta", "destructora"};
    
    private static final String[] nombres = {"Nehnid", "Eised", "Buo", "Nucrol", "Bragrem", "Gler", "Olban", "Stam", "Chiang"};
    private static final String[] profesiones = {"gladiador", "espadachin", "estratega", "explorador", "mago", "hechicero", "barbaro", "picaro"};
    private static final String[] adjetivos = {"valiente", "cobarde", "experto", "novato", "despiadado", "noble", "heroico"};
    
    private static final String DIRECCION = System.getProperty("user.dir") + "\\";
    
    public static void main(String[] args){
        
        try {
            crearJugadores();
            
            /*
            BufferedReader entrada = null;
            try {
            //  FORMATO DE ITEMS: I: codigo; nombre; precio; puntos de ataque; puntos de defensa; copias disponibles
            
            String direccion = System.getProperty("user.dir") + "\\";
            System.out.println("\n------------------");
            System.out.println("Cargando items");
            System.out.println("--------------------\n");
            entrada = new BufferedReader(new FileReader(direccion + "items.txt"));
            String cadena = null;
            PrintWriter salida = null;
            Lista listaCodigosItems = new Lista();
            
            salida = new PrintWriter(new FileOutputStream(direccion + "jugadores.txt"));
            
            cadena = entrada.readLine();
            
            while(cadena != null){
            String[] tipoYObjeto = cadena.split(":");
            
            if(!tipoYObjeto[0].equals("I")){
            System.out.println("Error cargando item, formato equivocado");
            } else {
            String[] objetoString = tipoYObjeto[1].split(";");
            if(objetoString.length != 6){
            System.out.println("Error cargando item, formato equivocado");
            } else {
            String codigo = objetoString[0].trim();
            listaCodigosItems.insertar(codigo, 1);
            }
            }
            
            cadena = entrada.readLine();
            }   System.out.println("\n------------------");
            System.out.println("Fin carga de items");
            System.out.println("--------------------\n");
            entrada.close();
            
            //  FORMATO DE JUGADORES: J: nombre; tipo; categoria; dinero; lista de items
            //  FORMATO DE lista de items: <codigoItem, codigoItem, ... >
            
            
            } catch (FileNotFoundException ex) {
            Logger.getLogger(GeneradorJugadores.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex){
            Logger.getLogger(GeneradorJugadores.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GeneradorJugadores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void crearJugadores() throws FileNotFoundException{
        
        Lista listaCodigosItems = obtenerCodigos();
        
        System.out.println(listaCodigosItems.toString());
        
        //  FORMATO DE JUGADORES: J: nombre; tipo; categoria; dinero; lista de items
        //  FORMATO DE lista de items: <codigoItem, codigoItem, ... >
        
        PrintWriter salida = new PrintWriter(new FileOutputStream(DIRECCION + "jugadores.txt"));
        
        final String[] categorias = {"Profesional", "Aficionado", "Novato"};
        final String[] tipos = {"Guerrero", "Defensor"};
        final int DINERO_MAXIMO = 2000;
        final int ITEMS_MAXIMOS = 5;
        
        final int CANTIDAD_JUGADORES = 60;
        
        Random aleatorio = new Random();
        
        Conjunto conjuntoNombres = new Conjunto();
        
        for(int i = 0; i < CANTIDAD_JUGADORES; i++){
            String nombre = "";
            do{
                nombre = nombres[aleatorio.nextInt(nombres.length)];
                nombre += ", el " + profesiones[aleatorio.nextInt(profesiones.length)]; 
                nombre += " " + adjetivos[aleatorio.nextInt(adjetivos.length)];
            } while(conjuntoNombres.pertenece(nombre));
            conjuntoNombres.agregar(nombre);
            
            String categoria = categorias[aleatorio.nextInt(categorias.length)];
            String tipo = tipos[aleatorio.nextInt(tipos.length)];
            int dinero = aleatorio.nextInt(DINERO_MAXIMO / 50) * 50;
            int cantItems = aleatorio.nextInt(ITEMS_MAXIMOS);
            
            String items = "<";
            
            for(int n = 0; n < cantItems; n++){
                items += listaCodigosItems.recuperar(aleatorio.nextInt(listaCodigosItems.longitud()));
                if(n + 1 != cantItems)items += ", ";
            }
            
            items += ">";
            
            salida.println("J: " + nombre + "; " + tipo + "; " + categoria + "; " + dinero + "; " + items);
        }
        
        salida.close();
    }
    
    public static Lista obtenerCodigos(){

        Lista listaCodigosItems = new Lista();
        BufferedReader entrada = null;
        try {
            //  FORMATO DE ITEMS: I: codigo; nombre; precio; puntos de ataque; puntos de defensa; copias disponibles
            
            
            System.out.println("\n------------------");
            System.out.println("Cargando items");
            System.out.println("--------------------\n");
            entrada = new BufferedReader(new FileReader(DIRECCION + "items.txt"));
            String cadena = null;
            PrintWriter salida = null;
            
            cadena = entrada.readLine();
            
            while(cadena != null){
                String[] tipoYObjeto = cadena.split(":");
                
                if(!tipoYObjeto[0].equals("I")){
                    System.out.println("Error cargando item, formato equivocado");
                } else {
                    String[] objetoString = tipoYObjeto[1].split(";");
                    if(objetoString.length != 6){
                        System.out.println("Error cargando item, formato equivocado");
                    } else {
                        String codigo = objetoString[0].trim();
                        listaCodigosItems.insertar(codigo, 1);
                    }
                }
                
                cadena = entrada.readLine();
            }   System.out.println("\n------------------");
            System.out.println("Fin carga de items");
            System.out.println("--------------------\n");
            entrada.close();
            
            //  FORMATO DE JUGADORES: J: nombre; tipo; categoria; dinero; lista de items
            //  FORMATO DE lista de items: <codigoItem, codigoItem, ... >
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GeneradorJugadores.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex){
            Logger.getLogger(GeneradorJugadores.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaCodigosItems;
    }
    
    
    public static void crearItems() throws FileNotFoundException {
        
        PrintWriter salida;
                
        salida = new PrintWriter(new FileOutputStream(DIRECCION + "items.txt"));
        
        //  FORMATO DE ITEMS: I: codigo; nombre; precio; puntos de ataque; puntos de defensa; copias disponibles
        
        final int ATK_MINIMO = 5;
        final int ATK_MAXIMO = 50;
        final int DEF_MINIMO = 5;
        final int DEF_MAXIMO = 50;
        final int COPIAS_MAXIMAS = 100;
        
        final int CANTIDAD_ITEMS = 20;
        
        Conjunto conjuntoNombres = new Conjunto();
        
        Random aleatorio = new Random();
        
        for(int numeroItemActual = 1; numeroItemActual <= CANTIDAD_ITEMS; numeroItemActual++){
            String codigo;
            if(numeroItemActual < 10){
                codigo = "E00" + numeroItemActual;
            } else {
                if(numeroItemActual < 100){
                    codigo = "E0" + numeroItemActual;
                } else {
                    codigo = "E" + numeroItemActual;
                }
            }
            String nombreItem = "";
            do{
                nombreItem = sustantivos[aleatorio.nextInt(sustantivos.length)];
                nombreItem += " " + adjetivos1[aleatorio.nextInt(adjetivos1.length)];
                nombreItem += " " + adjetivos2[aleatorio.nextInt(adjetivos2.length)];
            }while(conjuntoNombres.pertenece(nombreItem));
            conjuntoNombres.agregar(nombreItem);
            
            int puntosATK = ATK_MINIMO + (aleatorio.nextInt((ATK_MAXIMO - ATK_MINIMO)/5) * 5);
            int puntosDEF = DEF_MINIMO + (aleatorio.nextInt((DEF_MAXIMO - DEF_MINIMO)/5) * 5);
            int copias = aleatorio.nextInt(COPIAS_MAXIMAS);
            
            int precio = (puntosATK + puntosDEF) * aleatorio.nextInt(3, 5);
            
            salida.println("I: " + codigo + "; " + nombreItem + "; " + precio + "; " + puntosATK + "; " + puntosDEF + "; " + copias);
        }
        
        salida.close();
        
    }
}

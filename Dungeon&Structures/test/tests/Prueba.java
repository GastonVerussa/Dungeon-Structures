package tests;

import clases.unidades.Item;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Prueba {
    
    public static void main(String[] args){
        try {
            
            System.out.println("********  Test Archivos ********");
            
            //  FORMATO DE ITEMS: I: codigo; nombre; precio; puntos de ataque; puntos de defensa; copias disponibles
            System.out.println("Cargando items");

            BufferedReader entrada = new BufferedReader(new FileReader("E:\\Prueba\\items.txt"));
            String cadena = entrada.readLine();

            while(cadena != null){
                System.out.println("cadena leida: \"" + cadena + "\"");
                String[] tipoYObjeto = cadena.split(": ");
                System.out.print("Primer arreglo: ");
                for (String tipoYObjeto1 : tipoYObjeto) {
                    System.out.print("\"" + tipoYObjeto1 + "\", ");
                }
                System.out.println();

                if(!tipoYObjeto[0].equals("I")){
                    System.out.println("Error cargando item, formato equivocado");
                } else {
                    String[] objetoString = tipoYObjeto[1].split("; ");
                    System.out.println("Segundo arreglo: ");
                    for (String objetoString1 : objetoString) {
                        System.out.print("\"" + objetoString1 + "\", ");
                    }
                    System.out.println();
                    if(objetoString.length != 6){
                        System.out.println("Error cargando item, formato equivocado");
                    } else {
                        String codigo = objetoString[0];
                        String nombre = objetoString[1];
                        int precio = Integer.valueOf(objetoString[2]);
                        int puntosAtk = Integer.valueOf(objetoString[3]);
                        int puntosDef = Integer.valueOf(objetoString[4]);
                        int copias = Integer.valueOf(objetoString[5]);
                        Item objeto = new Item(codigo, nombre, precio, puntosAtk, puntosDef, copias);
                        System.out.println("Item encontrado: " + objeto.toString());
                    }
                }

                cadena = entrada.readLine();
            }
            
            entrada.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

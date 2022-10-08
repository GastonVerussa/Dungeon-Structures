/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utiles;

/**
 *
 * @author gasto
 */
public class Dato<Tipo>{
    
    private Tipo valor;

    public Dato(Tipo valor){
        this.valor = valor;
    }

    public Tipo get(){
        return this.valor;
    }

    public void set(Tipo valor){
        this.valor = valor;
    }
}

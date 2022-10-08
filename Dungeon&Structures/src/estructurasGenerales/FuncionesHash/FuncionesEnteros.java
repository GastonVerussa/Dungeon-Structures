package estructurasGenerales.FuncionesHash;

public class FuncionesEnteros {
    
    public static int hashDoblamiento(int elemento, int tamanioArreglo){
        int suma = 0;
        int cantidadDigitos = calcularDigitos(tamanioArreglo);
        int auxiliar = (int) Math.pow(10, cantidadDigitos); 
        while(elemento != 0){
            suma += elemento % auxiliar;
            elemento = elemento / auxiliar;
        }
        return suma % tamanioArreglo;
    }
    
    public static int hashCuadrado(int elemento, int tamanioArreglo){
        int resultado;
        
        int numero = elemento * elemento;
        int cantidadDigitosArreglo = calcularDigitos(tamanioArreglo);
        resultado = digitosCentrales(numero, cantidadDigitosArreglo + 1);
        
        resultado = resultado % tamanioArreglo;
        
        return resultado;
    }
    
    private static int digitosCentrales(int elemento, int cantDigitosDeseada){
        int resultado = elemento;
        int cantDigitosElemento = calcularDigitos(elemento);
        if(cantDigitosElemento > cantDigitosDeseada){
            int diferencia = cantDigitosDeseada - cantDigitosElemento;
            for(int i = 0; i < diferencia/2; i++){
                resultado = resultado / 10;
            }
            Double auxiliar = new Double(Math.pow(10, cantDigitosDeseada));
            resultado = resultado % auxiliar.intValue();
        }
        return resultado;
    }
    
    private static int calcularDigitos(int elemento){
        int cantidadDigitos = 0;
        while(elemento != 0){
            elemento = elemento / 10;
            cantidadDigitos++;
        }
        return cantidadDigitos;
    }
}

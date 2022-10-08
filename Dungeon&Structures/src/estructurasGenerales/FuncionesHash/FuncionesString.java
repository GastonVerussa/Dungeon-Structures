package estructurasGenerales.FuncionesHash;

public class FuncionesString {
    
    public static int hashCadena(String cadena, int tamanioArreglo){
        
        int suma = 0;
        int longitudCadena = cadena.length();
        
        //  Suma el valor de unicode de todos los caracteres
        for(int i = 0; i < longitudCadena; i++){
            suma += cadena.codePointAt(i);
        }
        
        return suma;
    }
}

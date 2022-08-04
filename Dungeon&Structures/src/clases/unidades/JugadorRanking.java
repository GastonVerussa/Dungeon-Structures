package clases.unidades;

public class JugadorRanking implements Comparable<JugadorRanking>{
    
    private final String nombre;
    private final int cantVictorias;

    public JugadorRanking(String nombre, int cantVictorias) {
        this.nombre = nombre;
        this.cantVictorias = cantVictorias;
    }

    public String getNombre() {
        return nombre;
    }
    
    public int getCantVictorias() {
        return cantVictorias;
    }

    @Override
    public int compareTo(JugadorRanking jugador){
        return this.cantVictorias - jugador.getCantVictorias();
    }
    
    @Override
    public String toString(){
        return "Jugador Ranking " + nombre + ", " + cantVictorias + " victorias";
    }
}

package dungeon.structures;

import clases.estructuras.*;
import clases.unidades.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import estructurasGenerales.lineales.Lista;
import estructurasGenerales.propositoEspecifico.ColaPrioridad;
import estructurasGenerales.propositoEspecifico.DiccionarioAVL;
import estructurasGenerales.propositoEspecifico.DiccionarioHash;
import clases.estructuras.TiendaItems;
import estructurasGenerales.propositoEspecifico.MapeoAMuchosAVL;
import utiles.Aleatorio;
import utiles.TecladoIn;

public class DungeonStructures {

    private static Jugadores jugadores;
    private static ColaPrioridad colaEspera;
    private static Equipos equipos;
    private static Mapa mapa;
    private static TiendaItems tiendaItems;
    //  Tabla hash para mantener los items ordenados por codigo y facilitar y mejorar
    //      la eficiencia en varias ocasiones
    private static DiccionarioHash tablaItems;
    //  Estructura de jugadores que mantiene los jugadores que ya estan participando
    //      esto quiere decir que estan en la cola de espera o forman parte de un
    //      equipo. Se utiliza para asegurar que un jugador entre a un unico equipo,
    //      lo cual se podria hacer revisando todos los equipos y revisando todos los
    //      jugadores en cola, pero sería mucho menos eficiente.
    private static Jugadores jugadoresEnColaOEquipo;
    //  Atributo para guardar la direccion
    private static String direccion;
    //  Salida para escribir en el archivo LOG.txt
    private static PrintWriter salida;
    //  Estructuras auxiliares para facilitar traducciones de categorias y tipos
    //      Ademas permite facil modificacion en caso de cambiar algo de esto.
    private static DiccionarioAVL categorias;
    private static Lista prioridades;
    private static DiccionarioAVL tipos;

    public static void main(String[] args) {
        
        iniciarEstructuras();

        cargaInicial();
        
        System.out.println("********  Bienvenido a Dungeon & Structures. ********");
        
        int op;
        do {
            op = menu();
            switch (op) {
                case 1:
                    menuABMJugadores();
                    break;
                case 2:
                    menuABMItems();
                    break;
                case 3:
                    menuABMMapa();
                    break;
                case 4:
                    menuSumarJugadorEspera();
                    break;
                case 5:
                    menuCrearEquipo();
                    break;
                case 6:
                    menuBatalla();
                    break;
                case 7:
                    menuConsultasEquipos();
                    break;
                case 8:
                    menuConsultasItems();
                    break;
                case 9:
                    menuConsultasJugadores();
                    break;
                case 10:
                    menuConsultasLocaciones();
                    break;
                case 11:
                    menuConsultasGenerales();
                    break;
                case 12:
                    System.out.println("\n\n" + estadoSistema());
                    break;
            }
        } while (op != 0);
         
        System.out.println("********  Gracias por jugar Dungeon & Structures ********");
        salida.println("\n\n-------------------------------------\n");
        salida.println("Estado del sistema luego al finalizar la ejecucion:");
        salida.println("\n-------------------------------------\n\n" + estadoSistema());
        salida.close();
    }
    
    private static void iniciarEstructuras(){
        
        equipos = new Equipos();
        jugadores = new Jugadores();
        mapa = new Mapa();
        tiendaItems = new TiendaItems();
        tablaItems = new DiccionarioHash();
        direccion = System.getProperty("user.dir") + "\\";
        jugadoresEnColaOEquipo = new Jugadores();
        categorias = new DiccionarioAVL();
        tipos = new DiccionarioAVL();
        prioridades = new Lista();
        categorias.insertar('P', "Profesional");
        categorias.insertar('A', "Aficionado");
        categorias.insertar('N', "Novato");
        prioridades.insertar('P', 1);
        prioridades.insertar('A', 2);
        prioridades.insertar('N', 3);
        colaEspera = new ColaPrioridad(prioridades);
        tipos.insertar('G', "Guerrero");
        tipos.insertar('D', "Defensor");
        
        Equipo.setMapa(mapa);
        Jugador.setDatosEstaticos();   
        
        try {
            salida = new PrintWriter(new FileOutputStream(direccion + "LOG.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DungeonStructures.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static int menu(){
        int op;
        String[] opciones = {
            "ABM de jugadores",
            "ABM de items",
            "ABM de Mapa",
            "Sumar jugador a la cola de espera",
            "Crear un equipo",
            "Iniciar batalla entre equipos",
            "Consultas sobre equipos",
            "Consultas sobre items",
            "Consultas sobre jugadores",
            "Consultas sobre locaciones",
            "Consultas generales",
            "Mostrar sistema"
        };

        System.out.println();
        System.out.println("------------------  MENÚ  ---------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ".- " + opciones[i]);
        }
        System.out.println("0.- Salir");
        System.out.println("-----------------------------------------------");

        do {
            op = TecladoIn.readLineInt();
            if (op < 0 || op > opciones.length) {
                System.out.println("El valor ingresado no es válido");
            }
        } while (op < 0 || op > opciones.length);
        return op;
    }
    
    private static void menuABMJugadores(){
        int op;
        String[] opciones = {
            "Agregar Jugador",
            "Eliminar Jugador",
            "Modificar Jugador existente"
        };

        do {
            
        System.out.println();
        System.out.println("------------------  Menu para ABM de jugadores  ---------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ".- " + opciones[i]);
        }
        System.out.println("0.- Volver");
        System.out.println("-----------------------------------------------");

        do {
            op = TecladoIn.readLineInt();
            if (op < 0 || op > opciones.length) {
                System.out.println("El valor ingresado no es válido");
            }
        } while (op < 0 || op > opciones.length);

            switch (op) {
                case 1:
                    altaJugador();
                    break;
                case 2:
                    bajaJugador();
                    break;
                case 3:
                    modificarJugador();
                    break;
            }
        
        } while (op != 0);
    }
    
    private static void altaJugador(){
        
        String nombre;
        char tipo;
        char categoria;
        int dineroInicial;
        
        System.out.println("Escriba los siguientes datos del jugador a agregar: ");
        
        System.out.print("Nombre: ");
        nombre = TecladoIn.readLine();
        while(jugadores.existeJugador(nombre)){
            System.out.println("Error, Jugador " + nombre + " ya existe.");
            System.out.print("Nombre: ");
            nombre = TecladoIn.readLine();
        }
            
        System.out.print("Tipo: ");
        tipo = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
        while(!existeTipo(tipo)){ 
            System.out.println("Error, tipo de jugador no existente. Ingrese uno de los siguientes: " + tipos.listarDatos().toString());
            System.out.print("Tipo: ");
            tipo = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
        }
        
        System.out.print("Categoria: ");
        categoria = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
        while(!existeCategoria(categoria)){
            System.out.println("Error, categoria no existente. Ingrese uno de los siguientes: " + categorias.listarDatos().toString());
            System.out.print("Nombre: ");
            categoria = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
        }
        
        System.out.print("Dinero inicial: ");
        dineroInicial = TecladoIn.readLineInt();
        
        Jugador nuevoJugador = new Jugador(nombre, tipo, categoria, dineroInicial);
        
        jugadores.agregarJugador(nuevoJugador);
        System.out.println("Se creo el jugador " + nuevoJugador.getNombre());
        salida.println("Se creo el jugador " + nuevoJugador.getNombre());
        
        char opcion;
        
        do{
            System.out.println("Desea sumarle un nuevo item? (S / N)");
            opcion = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
            if(opcion == 'S'){
                System.out.println("Ingrese codigo del item a regalar: ");
                String codigoItem = TecladoIn.readLine();
                regalarItem(nuevoJugador, codigoItem);
            }
        } while(opcion != 'N');
    }
    
    private static void modificarJugador(){
        String nombre;
        System.out.print("Escriba el nombre del jugador a modificar: ");
        nombre = TecladoIn.readLine();
        if(!jugadores.existeJugador(nombre)){
            System.out.print("Error. No existe ningun jugador llamado: " + nombre);
        } else {
            Jugador jugador = jugadores.recuperarJugador(nombre);
            int op;
            String[] opciones = {
                "Cambiar tipo",
                "Cambiar categoria",
                "Sumar dinero",
                "Restar dinero",
                "Agregar item",
                "Sacar item"
            };

            do {

            System.out.println();
            System.out.println("---- Que desea modificar?  -----");
            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ".- " + opciones[i]);
            }
            System.out.println("0.- Nada. Volver");
            System.out.println("-----------------");

            do {
                op = TecladoIn.readLineInt();
                if (op < 0 || op > opciones.length) {
                    System.out.println("El valor ingresado no es válido");
                }
            } while (op < 0 || op > opciones.length);

                switch (op) {
                    case 1:
                        System.out.print("Inserte nuevo tipo: ");
                        char tipo = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
                        while(!existeTipo(tipo)){ 
                            System.out.println("Error, tipo de jugador no existente. Ingrese uno de los siguientes: " + tipos.listarDatos().toString());
                            System.out.print("Tipo: ");
                            tipo = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
                        }
                        System.out.print("Tipo de jugador de " + nombre + " modificado de " + nombreTipo(jugador.getTipo()) + " a " + nombreTipo(tipo));
                        salida.print("Tipo de jugador de " + nombre + " modificado de " + nombreTipo(jugador.getTipo()) + " a " + nombreTipo(tipo));
                        jugador.setTipo(tipo);
                        break;
                    case 2:
                        System.out.print("Inserte nueva categoria: ");
                        char categoria = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
                        while(!existeCategoria(categoria)){ 
                            System.out.println("Error, categoria de jugador no existente. Ingrese uno de los siguientes: " + categorias.listarDatos().toString());
                            System.out.print("Categoria: ");
                            categoria = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
                        }
                        System.out.print("Categoria de jugador de " + nombre + " modificado de " + nombreCategoria(jugador.getCategoria()) + " a " + nombreCategoria(categoria));
                        salida.print("Categoria de jugador de " + nombre + " modificado de " + nombreCategoria(jugador.getCategoria()) + " a " + nombreCategoria(categoria));
                        jugador.setCategoria(categoria);
                        break;
                    case 3:
                        System.out.println("Inserte cantidad de dinero a sumar: ");
                        int ganancia = TecladoIn.readLineInt();
                        jugador.modificarDinero(ganancia);
                        System.out.print("El jugador " + nombre + " ha recibido $" + ganancia);
                        salida.print("El jugador " + nombre + " ha recibido $" + ganancia);
                        break;
                    case 4:
                        System.out.println("Inserte cantidad de dinero a restar: ");
                        int perdida = TecladoIn.readLineInt();
                        jugador.modificarDinero(perdida * (-1));
                        System.out.print("El jugador " + nombre + " ha perdido $" + perdida);
                        salida.print("El jugador " + nombre + " ha perdido $" + perdida);
                        break;
                    case 5:
                        System.out.println("Ingrese codigo de item a agregar: ");
                        String codigoItem = TecladoIn.readLine();
                        comprarItem(jugador, codigoItem);
                        break;
                    case 6:
                        Lista itemsJugador = jugador.getItems();
                        if(itemsJugador.esVacia()){
                            System.out.println("Jugador sin objetos. No hay objetos que eliminar.");
                        } else {
                            System.out.println("Objetos del jugador: ");
                            for(int i = 1; i <= itemsJugador.longitud(); i++){
                                CopiaItem objeto = (CopiaItem) itemsJugador.recuperar(i);
                                System.out.println(i + ": " + objeto.toStringCompleto());
                            }
                            int posicionItem;
                            do{
                                System.out.println("Ingrese la posicion del item a eliminar (entre 1 y " + itemsJugador.longitud() + "): ");
                                posicionItem = TecladoIn.readLineInt();
                            }while(posicionItem < 1 || posicionItem > itemsJugador.longitud());
                            CopiaItem itemEliminado = (CopiaItem) itemsJugador.recuperar(posicionItem);
                            System.out.println("Copia de item del jugador " + nombre + " eliminado: " + itemEliminado.toStringCompleto());
                            salida.println("Copia de item del jugador " + nombre + " eliminado: " + itemEliminado.toStringCompleto());
                            itemsJugador.eliminar(posicionItem);
                        }
                        break;
                }
            } while (op != 0);
        }
    }
    
    private static void bajaJugador(){
        String nombre;
        System.out.print("Escriba el nombre del jugador a eliminar: ");
        nombre = TecladoIn.readLine();
        if(jugadores.eliminarJugador(nombre)){
            System.out.print("Jugador " + nombre + " eliminado con exito");
            salida.print("Se eliminó el jugador " + nombre);
        } else {
            System.out.print("Error. No existe un jugador llamado " + nombre + ", elimiacion cancelada");
        }
    }
    
    private static void menuABMItems(){
        int op;
        String[] opciones = {
            "Agregar Item",
            "Eliminar Item",
            "Modificar Item existente"
        };

        do {
            
        System.out.println();
        System.out.println("------------------  Menu para ABM de Items  ---------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ".- " + opciones[i]);
        }
        System.out.println("0.- Volver");
        System.out.println("-----------------------------------------------");

        do {
            op = TecladoIn.readLineInt();
            if (op < 0 || op > opciones.length) {
                System.out.println("El valor ingresado no es válido");
            }
        } while (op < 0 || op > opciones.length);

            switch (op) {
                case 1:
                    altaItem();
                    break;
                case 2:
                    bajaItem();
                    break;
                case 3:
                    modificarItem();
                    break;
            }
        
        } while (op != 0);
    }
    
    private static void altaItem(){
        
        String codigo;
        String nombre;
        int precio;
        int puntosAtk;
        int puntosDef;
        int cantCopiasDisp;
        
        System.out.println("Escriba los siguientes datos del item a agregar: ");
        
        System.out.print("Codigo: ");
        codigo = TecladoIn.readLine();
        while(tablaItems.existeClave(codigo)){
            System.out.println("Error, codigo de Item " + codigo + " ya utilizado.");
            System.out.print("Codigo: ");
            codigo = TecladoIn.readLine();
        }
            
        System.out.print("Nombre: ");
        nombre = TecladoIn.readLine();
        
        System.out.print("Precio: ");
        precio = TecladoIn.readLineInt();
        
        System.out.print("Puntos de Ataque: ");
        puntosAtk = TecladoIn.readLineInt();
        
        System.out.print("Puntos de defensa: ");
        puntosDef = TecladoIn.readLineInt();
        
        System.out.print("Cantidad de copias iniciales: ");
        cantCopiasDisp = TecladoIn.readLineInt();
        
        Item nuevoItem = new Item(codigo, nombre, precio, puntosAtk, puntosDef, cantCopiasDisp);
        
        insertarItem(nuevoItem);
        
        System.out.println("Se creo el item " + nuevoItem.getNombreCompuesto());
        salida.println("Se creo el item " + nuevoItem.getNombreCompuesto());
    }
    
    private static void modificarItem(){
        String codigo;
        System.out.print("Escriba el codigo del item a modificar: ");
        codigo = TecladoIn.readLine();
        if(!tablaItems.existeClave(codigo)){
            System.out.print("Error. No existe ningun item con el codigo: " + codigo);
        } else {
            Item objeto = (Item) tablaItems.obtenerInformacion(codigo);
            int op;
            String[] opciones = {
                "Cambiar nombre",
                "Cambiar precio",
                "Cambiar puntos de ataque",
                "Cambiar puntos de defensa",
                "Cambiar cantidad de copias disponibles"
            };

            do {

            System.out.println();
            System.out.println("---- Que desea modificar?  -----");
            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ".- " + opciones[i]);
            }
            System.out.println("0.- Nada. Volver");
            System.out.println("-----------------");

            do {
                op = TecladoIn.readLineInt();
                if (op < 0 || op > opciones.length) {
                    System.out.println("El valor ingresado no es válido");
                }
            } while (op < 0 || op > opciones.length);

                switch (op) {
                    
                    case 1:
                        System.out.print("Inserte nuevo nombre: ");
                        objeto.setNombre(TecladoIn.readLine());
                        break;
                    case 2:
                        System.out.print("Inserte nuevo precio: ");
                        tiendaItems.eliminar(objeto);
                        objeto.setPrecio(TecladoIn.readLineInt());
                        tiendaItems.insertar(objeto);
                        break;
                    case 3:
                        System.out.print("Inserte nuevos puntos de ataque: ");
                        objeto.setPuntosAtk(TecladoIn.readLineInt());
                        break;
                    case 4:
                        System.out.print("Inserte nuevos puntos de defensa: ");
                        objeto.setPuntosDef(TecladoIn.readLineInt());
                        break;
                    case 5:
                        System.out.print("Inserte nueva cantidad de copias disponible: ");
                        objeto.setCantCopiasDisp(TecladoIn.readLineInt());
                        break;
                }
            } while (op != 0);
        }
    }
    
    private static void bajaItem(){
        String codigo;
        System.out.print("Escriba el codigo del item a eliminar: ");
        codigo = TecladoIn.readLine();
        if(tablaItems.existeClave(codigo)){
            Item objeto = (Item) tablaItems.obtenerInformacion(codigo);
            System.out.print("Item " + objeto.getNombreCompuesto() + " eliminado con exito");
            salida.print("Se eliminó el item " + objeto.getNombreCompuesto());
            eliminarItem(objeto);
        } else {
            System.out.print("Error. No existe un item con el codigo " + codigo + ", elimiacion cancelada");
        }
    }
    
    private static void menuABMMapa(){
        int op;
        String[] opciones = {
            "Agregar Locacion",
            "Eliminar Locacion",
            "Agregar Camino",
            "Eliminar Camino",
            "Modificar Camino"
        };

        do {
            
        System.out.println();
        System.out.println("------------------  Menu para ABM del Mapa  ---------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ".- " + opciones[i]);
        }
        System.out.println("0.- Volver");
        System.out.println("-----------------------------------------------");

        do {
            op = TecladoIn.readLineInt();
            if (op < 0 || op > opciones.length) {
                System.out.println("El valor ingresado no es válido");
            }
        } while (op < 0 || op > opciones.length);

            switch (op) {
                case 1:
                    altaLocacion();
                    break;
                case 2:
                    bajaLocacion();
                    break;
                case 3:
                    altaCamino();
                    break;
                case 4:
                    bajaCamino();
                    break;
                case 5:
                    modificarCamino();
                    break;
            }
        
        } while (op != 0);
    }
    
    private static void altaLocacion(){
        
        String nombre;
        
        System.out.print("Escriba el nombre de la locacion a agregar: ");
        nombre = TecladoIn.readLine();
        while(mapa.existeLocacion(nombre)){
            System.out.println("Error, locacion " + nombre + " ya existe.");
            System.out.print("Escriba el nombre de la locacion a agregar: ");
            nombre = TecladoIn.readLine();
        }
        
        mapa.insertarLocacion(nombre);
        System.out.println("Se creo la locacion  " + nombre);
        salida.println("Se creo la locacion " + nombre);
        
    }
    
    private static void bajaLocacion(){
        String nombre;
        System.out.print("Escriba el nombre de la locacion a eliminar: ");
        nombre = TecladoIn.readLine();
        if(mapa.eliminarLocacion(nombre)){
            System.out.print("Locacion " + nombre + " eliminada con exito");
            salida.print("Se eliminó la locacion " + nombre);
        } else {
            System.out.print("Error. No existe la locacion " + nombre + ", elimiacion cancelada");
        }
    }
    
    private static void altaCamino(){
        
        String origen;
        String destino;
        
        System.out.print("Escriba el nombre de la locacion de origen del camino a agregar: ");
        origen = TecladoIn.readLine();
        while(!mapa.existeLocacion(origen)){
            System.out.println("Error, locacion " + origen + " no existe.");
            System.out.print("Escriba el nombre de la locacion de origen del camino a agregar: ");
            origen = TecladoIn.readLine();
        }
        
        System.out.print("Escriba el nombre de la locacion de destino del camino a agregar: ");
        destino = TecladoIn.readLine();
        while(!mapa.existeLocacion(destino)){
            System.out.println("Error, locacion " + destino + " no existe.");
            System.out.print("Escriba el nombre de la locacion de destino del camino a agregar: ");
            destino = TecladoIn.readLine();
        }
        
        if(mapa.existeCamino(origen, destino)){
            System.out.println("Error, ya existe un camino entre " + origen + " y " + destino);
        } else {
            System.out.print("Escriba la distancia del camino a agregar: ");
            int distancia = TecladoIn.readLineInt();
            
            mapa.insertarCamino(origen, destino, distancia);
            System.out.println("Se creo el camino desde " + origen + " hasta " + destino + " con " + distancia + "km de distancia");
            salida.println("Se creo el camino desde " + origen + " hasta " + destino + " con " + distancia + "km de distancia");
        }
        
    }
    
    private static void bajaCamino(){
        
        String origen;
        String destino;
        
        System.out.print("Escriba el nombre de la locacion de origen del camino a eliminar: ");
        origen = TecladoIn.readLine();
        while(!mapa.existeLocacion(origen)){
            System.out.println("Error, locacion " + origen + " no existe.");
            System.out.print("Escriba el nombre de la locacion de origen del camino a eliminar: ");
            origen = TecladoIn.readLine();
        }
        
        System.out.print("Escriba el nombre de la locacion de destino del camino a eliminar: ");
        destino = TecladoIn.readLine();
        while(!mapa.existeLocacion(destino)){
            System.out.println("Error, locacion " + destino + " no existe.");
            System.out.print("Escriba el nombre de la locacion de destino del camino a eliminar: ");
            destino = TecladoIn.readLine();
        }
        
        if(!mapa.existeCamino(origen, destino)){
            System.out.println("Error, no existe un camino entre " + origen + " y " + destino);
        } else {
            mapa.eliminarCamino(origen, destino);
            System.out.println("Se elimino el camino desde " + origen + " hasta " + destino);
            salida.println("Se elimino el camino desde " + origen + " hasta " + destino);
        }
    }
      
    private static void modificarCamino(){
        
        String origen;
        String destino;
        
        System.out.print("Escriba el nombre de la locacion de origen del camino a modificar: ");
        origen = TecladoIn.readLine();
        while(!mapa.existeLocacion(origen)){
            System.out.println("Error, locacion " + origen + " no existe.");
            System.out.print("Escriba el nombre de la locacion de origen del camino a modificar: ");
            origen = TecladoIn.readLine();
        }
        
        System.out.print("Escriba el nombre de la locacion de destino del camino a modificar: ");
        destino = TecladoIn.readLine();
        while(!mapa.existeLocacion(destino)){
            System.out.println("Error, locacion " + destino + " no existe.");
            System.out.print("Escriba el nombre de la locacion de destino del camino a modificar: ");
            destino = TecladoIn.readLine();
        }
        
        if(!mapa.existeCamino(origen, destino)){
            System.out.println("Error, no existe un camino entre " + origen + " y " + destino);
        } else {
            System.out.print("Escriba la nueva distancia del camino: ");
            int distancia = TecladoIn.readLineInt();
            int distanciaActual = mapa.recuperarDistancia(origen, destino);
            
            System.out.println("Se modifico la distancia del camino desde " + origen + " hasta " + destino + ". De " +  distanciaActual + "km a " + distancia + "km.");
            salida.println("Se modifico la distancia del camino desde " + origen + " hasta " + destino + ". De " +  distanciaActual + "km a " + distancia + "km.");
            mapa.moidificarCamino(origen, destino, distancia);
        }
    }
    
    private static void menuSumarJugadorEspera(){
        
        int op;
        String[] opciones = {
            "Sumar de a un jugador a la cola de espera",
            "Sumar todos los jugadores a la cola de espera",
        };

        do {
            
        System.out.println();
        System.out.println("------------------  Menu manejo cola de espera ---------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ".- " + opciones[i]);
        }
        System.out.println("0.- Volver");
        System.out.println("-----------------------------------------------");

        do {
            op = TecladoIn.readLineInt();
            if (op < 0 || op > opciones.length) {
                System.out.println("El valor ingresado no es válido");
            }
        } while (op < 0 || op > opciones.length);

            switch (op) {
                case 1:
                    sumarDeAUnJugadorCola();
                    break;
                case 2:
                    sumarTodosLosJugadoresCola();
                    break;
            }
        
        } while (op != 0);
    }
    
    private static void sumarDeAUnJugadorCola(){
        
        char opcion;
        
        do{
            System.out.print("Desea agregar un nuevo jugador a la cola de espera? (S / N): ");
            opcion = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
            if(opcion == 'S'){
                System.out.print("Ingrese nombre del jugador a ingresar en la cola: ");
                String nombre = TecladoIn.readLine();
                if(!jugadores.existeJugador(nombre)){
                    System.out.println("Error, no existe un jugador con el nombre " + nombre);
                } else {
                    if(jugadoresEnColaOEquipo.existeJugador(nombre)){
                        System.out.println("Error, jugador " + nombre + " ya se encuentra en la cola de espera o en un equipo");
                    } else {
                        System.out.println("Jugador " + nombre + " agregado a la cola de espera correctamente");
                        Jugador jugador = jugadores.recuperarJugador(nombre);
                        sumarJugadorCola(jugador);
                    }
                }
            }
        } while(opcion != 'N');
    }
        
    private static void sumarTodosLosJugadoresCola(){
        Lista listaJugadores = jugadores.recuperarJugadores();
        while(!listaJugadores.esVacia()){
            int posicion = Aleatorio.intAleatorio(1, listaJugadores.longitud());
            Jugador jugador = (Jugador) listaJugadores.recuperar(posicion);
            listaJugadores.eliminar(posicion);
            if(!jugadoresEnColaOEquipo.existeJugador(jugador.getNombre())){
                System.out.println("Jugador " + jugador.getNombre() + " agregado a la cola de espera correctamente");
                sumarJugadorCola(jugador);
            }
        }
        System.out.println("Se han sumado todos los jugadores disponibles a la cola");
    }
    
    
    private static void menuCrearEquipo(){
        
        char opcion;
        
        do{
            System.out.println("Desea crear un nuevo equipo? (S / N)");
            opcion = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
            if(opcion == 'S'){
                System.out.println();
                System.out.println("Ingrese nombre del equipo a crear: ");
                String nombre = TecladoIn.readLine();
                if(equipos.existe(nombre)){
                    System.out.println("Error, ya existe un equipo llamado " + nombre);
                } else {
                    if(crearEquipo(nombre)){
                        String mensaje = "Equipo " + nombre + " creado correctamente.";
                        Jugador[] jugadoresEquipo = equipos.recuperar(nombre).getParticipantes();
                        mensaje += " Cons los jugadores: " + jugadoresEquipo[0].getNombre();
                        mensaje += ", " + jugadoresEquipo[1].getNombre() + ", " + jugadoresEquipo[2].getNombre();
                        System.out.println(mensaje);
                        salida.println(mensaje);
                    } else {
                        System.out.println("Error, no hay suficientes jugadores esperando para crear un nuevo equipo.");
                    }
                }
            }
        } while(opcion != 'N');
        
    }
    
    private static void menuBatalla(){
        
        System.out.print("Escriba el nombre del primer equipo a batallar (\"cancelar\" para cancelar): ");
        String nombreEquipo1 = TecladoIn.readLine();
        while(!nombreEquipo1.equals("cancelar") && !equipos.existe(nombreEquipo1)){
            System.out.println("Error, equipo " + nombreEquipo1 + " no existe.");
            System.out.print("Escriba el nombre del primer equipo a batallar (\"cancelar\" para cancelar): ");
            nombreEquipo1 = TecladoIn.readLine();
        }
        
        if(nombreEquipo1.equals("cancelar")){
            System.out.println("Combate cancelado.");
        } else {
            System.out.print("Escriba el nombre del segundo equipo a batallar (\"cancelar\" para cancelar): ");
            String nombreEquipo2 = TecladoIn.readLine();
            while(!nombreEquipo2.equals("cancelar") && !equipos.existe(nombreEquipo2)){
                System.out.println("Error, equipo " + nombreEquipo2 + " no existe.");
                System.out.print("Escriba el nombre del segundo equipo a batallar (\"cancelar\" para cancelar): ");
                nombreEquipo2 = TecladoIn.readLine();
            }
            if(nombreEquipo2.equals("cancelar")){
                System.out.println("Combate cancelado.");
            } else {
                batallaEquipos(equipos.recuperar(nombreEquipo1), equipos.recuperar(nombreEquipo2));
            }
        }
    }

    private static void menuConsultasEquipos(){
        
        int op;
        String[] opciones = {
            "Consultar un equipo",
            "Conseguir ubicacion de todos los equipos"
        };

        do {
            
        System.out.println();
        System.out.println("------------------  Menu consultas Items  ---------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ".- " + opciones[i]);
        }
        System.out.println("0.- Volver");
        System.out.println("-----------------------------------------------");

        do {
            op = TecladoIn.readLineInt();
            if (op < 0 || op > opciones.length) {
                System.out.println("El valor ingresado no es válido");
            }
        } while (op < 0 || op > opciones.length);

            switch (op) {
                case 1:
                    consultarEquipo();
                    break;
                case 2:
                    consultarUbicacionEquipos();
                    break;
            }
        
        } while (op != 0);
    }

    private static void consultarEquipo(){
        
        char opcion;
        
        do{
            System.out.println("Desea consultar un nuevo equipo? (S / N)");
            opcion = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
            if(opcion == 'S'){
                String nombre;

                System.out.print("Escriba el nombre del equipo a consultar: ");
                nombre = TecladoIn.readLine();
                if(!equipos.existe(nombre)){
                    System.out.println("Error, equipo " + nombre + " no existe.");
                } else {
                    System.out.println(equipos.recuperar(nombre).datosBasicos());
                }
            }
        } while(opcion != 'N');
    }
    
    private static void consultarUbicacionEquipos(){

        Lista listaEquipos = equipos.recuperarEquipos().clone();
        
        MapeoAMuchosAVL mapeoLocaciones = new MapeoAMuchosAVL();
        
        while(!listaEquipos.esVacia()){
            Equipo equipoActual = (Equipo) listaEquipos.recuperar(1);
            listaEquipos.eliminar(1);
            mapeoLocaciones.asociar(equipoActual.getLocacionActual(), equipoActual.getNombre());
        }
        
        System.out.println("La ubicacion de todos los equipos es la siguiente: ");
        System.out.println(mapeoLocaciones.toString());
        
    }
    
    private static void menuConsultasItems(){
        int op;
        String[] opciones = {
            "Items mas baratos que un valor",
            "Items entre un rango de valores",
            "Conseguir atributos de un item"
        };

        do {
            
        System.out.println();
        System.out.println("------------------  Menu consultas Items  ---------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ".- " + opciones[i]);
        }
        System.out.println("0.- Volver");
        System.out.println("-----------------------------------------------");

        do {
            op = TecladoIn.readLineInt();
            if (op < 0 || op > opciones.length) {
                System.out.println("El valor ingresado no es válido");
            }
        } while (op < 0 || op > opciones.length);

            switch (op) {
                case 1:
                    consultarItemsPresupuesto();
                    break;
                case 2:
                    consultarItemsRango();
                    break;
                case 3:
                    consultarItemCodigo();
                    break;
            }
        
        } while (op != 0);
    }
    
    private static void consultarItemsPresupuesto(){
        
        System.out.print("Escriba el precio maximo a consultar de los objetos: ");
        int presupuesto = TecladoIn.readLineInt();
        
        String stringItems = tiendaItems.conseguirItemsAccesibles(presupuesto);
        
        if(stringItems.equals("")){
            System.out.println("No existen items accesibles con un presupuesto de " + presupuesto);
        } else {
            System.out.println("Los items accesibles con un presupuesto de " + presupuesto + " son: ");
            System.out.println(stringItems);

            System.out.println("\n Desea comprar algun item? (S / N)");
            char opcion = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
            if(opcion == 'S'){
                System.out.print("Escriba el nombre del jugador que comprara: ");
                String nombreJugador = TecladoIn.readLine();
                while(!jugadores.existeJugador(nombreJugador)){
                    System.out.println("Error, jugador " + nombreJugador + " no existe.");
                    System.out.print("Escriba el nombre del jugador que comprara: ");
                    nombreJugador = TecladoIn.readLine();
                }
                Jugador jugadorCompra = jugadores.recuperarJugador(nombreJugador);
                while(opcion != 'N'){
                    if(opcion == 'S'){
                        System.out.print("Escriba el codigo del objeto a comprar ($" + jugadorCompra.getDinero() + " restantes del jugador): ");
                        String codigoItem = TecladoIn.readLine();
                        comprarItem(jugadorCompra, codigoItem);
                    }
                    System.out.println("Desea seguir comprando? (S / N)");
                    opcion = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
                }
            }
        }
    }
    
    private static void consultarItemsRango(){
        
        System.out.print("Escriba el precio minimo a consultar de los objetos: ");
        int precioMinimo = TecladoIn.readLineInt();
        
        System.out.print("Escriba el precio maximo a consultar de los objetos: ");
        int precioMaximo = TecladoIn.readLineInt();
        
        String stringItems = tiendaItems.conseguirItemsRango(precioMinimo, precioMaximo);
        
        if(stringItems.equals("")){
            System.out.println("No existen items con precio entre $" + precioMinimo + " y $" + precioMaximo);
        } else {
            System.out.println("Los items con precio entre $" + precioMinimo + " y $" + precioMaximo + " son: ");
            System.out.println(stringItems);
        }
    }
    
    private static void consultarItemCodigo(){
        
        System.out.print("Escriba el codigo del item a consultar: ");
        String codigo = TecladoIn.readLine();
        while(!tablaItems.existeClave(codigo)){
            System.out.println("Error, item con codigo " + codigo + " no existe.");
            System.out.print("Escriba el codigo del item a consultar: ");
            codigo = TecladoIn.readLine();
        }
        
        Item objeto = (Item) tablaItems.obtenerInformacion(codigo);
        
        System.out.println(objeto.toStringCompleto());
    }

    private static void menuConsultasJugadores(){
        int op;
        String[] opciones = {
            "Conseguir atributos de un jugador",
            "Conseguir todos los jugadores que empiezan con subcadena"
        };

        do {
            
        System.out.println();
        System.out.println("------------------  Menu consultas Jugadores  ---------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ".- " + opciones[i]);
        }
        System.out.println("0.- Volver");
        System.out.println("-----------------------------------------------");

        do {
            op = TecladoIn.readLineInt();
            if (op < 0 || op > opciones.length) {
                System.out.println("El valor ingresado no es válido");
            }
        } while (op < 0 || op > opciones.length);

            switch (op) {
                case 1:
                    consultarJugador();
                    break;
                case 2:
                    consultarJugadoresSubcadena();
                    break;
            }
        
        } while (op != 0);
    }

    private static void consultarJugador(){
        
        char opcion = 'S';
        
        do{
            if(opcion == 'S'){
                System.out.print("Escriba el nombre del jugador que desea consultar: ");
                String nombreJugador = TecladoIn.readLine();
                if(!jugadores.existeJugador(nombreJugador)){
                    System.out.println("Error, jugador " + nombreJugador + " no existe.");
                } else {
                    System.out.println("Jugador " + jugadores.recuperarJugador(nombreJugador).toStringCompleto());
                }
            }
            System.out.println("Desea consultar otro jugador? (S / N)");
            opcion = Character.toUpperCase(TecladoIn.readLineNonwhiteChar());
        }while(opcion != 'N');
    }
    
    private static void consultarJugadoresSubcadena(){
        
        System.out.print("Escriba la subcadena que desea consultar: ");
        String subcadena = TecladoIn.readLine();
        
        System.out.print("Jugadores que comienzan con la subcadena \"" + subcadena + "\" son: ");
        Lista listaNombreJugadores = jugadores.jugadoresSubcadena(subcadena);
        System.out.print("(");
        while(!listaNombreJugadores.esVacia()){
            String jugadorActual = (String) listaNombreJugadores.recuperar(1);
            listaNombreJugadores.eliminar(1);
            System.out.print(jugadorActual + ", ");
        }
        System.out.print(")");
    }
    
    private static void menuConsultasLocaciones(){
        int op;
        String[] opciones = {
            "Conseguir locaciones adyacentes a una locacion",
            "Obtener recorrido de menor distancia entre dos locaciones",
            "Obtener recorrido con menor locaciones entre dos locaciones",
            "Obtener todos los recorridos entre dos locaciones que no superen cierta distancia",
            "Obtener todos los recorridos entre dos locaciones que no pasen por una tercera locacion"
        };

        do {
            
        System.out.println();
        System.out.println("------------------  Menu consultas Locaciones  ---------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ".- " + opciones[i]);
        }
        System.out.println("0.- Volver");
        System.out.println("-----------------------------------------------");

        do {
            op = TecladoIn.readLineInt();
            if (op < 0 || op > opciones.length) {
                System.out.println("El valor ingresado no es válido");
            }
        } while (op < 0 || op > opciones.length);

            switch (op) {
                case 1:
                    consultarAdyacentes();
                    break;
                case 2:
                    consultarCaminoMasCorto();
                    break;
                case 3:
                    consultarCaminoMenosLocaciones();
                    break;
                case 4:
                    consultarCaminosSegunDistancia();
                    break;
                case 5:
                    consultarCaminosSinLocacion();
                    break;
            }
        
        } while (op != 0);
    }

    private static void consultarAdyacentes(){
        
        System.out.print("Escriba el nombre de la locacion a consultar: ");
        String nombre = TecladoIn.readLine();
        while(!mapa.existeLocacion(nombre)){
            System.out.println("Error, locacion " + nombre + " no existe.");
            System.out.print("Escriba el nombre de la locacion a consultar: ");
            nombre = TecladoIn.readLine();
        }
        
        Lista listaLocaciones = mapa.obtenerAdyacentes(nombre);
        
        System.out.println("Locaciones adyacentes a " + nombre + ": " + listaLocaciones.toString());
    }
    
    private static void consultarCaminoMasCorto(){
        
        System.out.print("Escriba el nombre de la locacion de origen del recorrido: ");
        String origen = TecladoIn.readLine();
        while(!mapa.existeLocacion(origen)){
            System.out.println("Error, locacion " + origen + " no existe.");
            System.out.print("Escriba el nombre de la locacion de origen del recorrido: ");
            origen = TecladoIn.readLine();
        }
        
        System.out.print("Escriba el nombre de la locacion de destino del recorrido: ");
        String destino = TecladoIn.readLine();
        while(!mapa.existeLocacion(destino)){
            System.out.println("Error, locacion " + destino + " no existe.");
            System.out.print("Escriba el nombre de la locacion de destino del recorrido: ");
            destino = TecladoIn.readLine();
        }
        
        Lista recorrido = mapa.obtenerCaminoMenosKm(origen, destino);
        
        if(recorrido.esVacia()){
            System.out.println("No existe un camino posible entre " + origen + " y " + destino);
        } else {
            System.out.println("El camino mas corto entre " + origen + " y " + destino + " es el siguiente: ");
            System.out.println(recorrido.toString());
        }
        
    }
    
    private static void consultarCaminoMenosLocaciones(){
        
        System.out.print("Escriba el nombre de la locacion de origen del recorrido: ");
        String origen = TecladoIn.readLine();
        while(!mapa.existeLocacion(origen)){
            System.out.println("Error, locacion " + origen + " no existe.");
            System.out.print("Escriba el nombre de la locacion de origen del recorrido: ");
            origen = TecladoIn.readLine();
        }
        
        System.out.print("Escriba el nombre de la locacion de destino del recorrido: ");
        String destino = TecladoIn.readLine();
        while(!mapa.existeLocacion(destino)){
            System.out.println("Error, locacion " + destino + " no existe.");
            System.out.print("Escriba el nombre de la locacion de destino del recorrido: ");
            destino = TecladoIn.readLine();
        }
        
        Lista recorrido = mapa.obtenerCaminoMenosLocaciones(origen, destino);
        
        if(recorrido.esVacia()){
            System.out.println("No existe un camino posible entre " + origen + " y " + destino);
        } else {
            System.out.println("El camino mas corto entre " + origen + " y " + destino + " es el siguiente: ");
            System.out.println(recorrido.toString());
        }
    }
    
    private static void consultarCaminosSegunDistancia(){
        
        System.out.print("Escriba el nombre de la locacion de origen de los recorridos: ");
        String origen = TecladoIn.readLine();
        while(!mapa.existeLocacion(origen)){
            System.out.println("Error, locacion " + origen + " no existe.");
            System.out.print("Escriba el nombre de la locacion de origen de los recorridos: ");
            origen = TecladoIn.readLine();
        }
        
        System.out.print("Escriba el nombre de la locacion de destino de los recorrido: ");
        String destino = TecladoIn.readLine();
        while(!mapa.existeLocacion(destino)){
            System.out.println("Error, locacion " + destino + " no existe.");
            System.out.print("Escriba el nombre de la locacion de destino de los recorridos: ");
            destino = TecladoIn.readLine();
        }
        
        System.out.print("Escriba la cantidad de km maxima para los caminos: ");
        int distanciaMaxima = TecladoIn.readLineInt();
        
        Lista caminosCorrectos = mapa.obtenerCaminosLimiteKm(origen, destino, distanciaMaxima);
        
        if(caminosCorrectos.esVacia()){
            System.out.println("No existen caminos entre " + origen + " y " + destino + " con menos de " + distanciaMaxima + "km");
        } else {
            System.out.println("Los caminos entre " + origen + " y " + destino + " con menos de " + distanciaMaxima + "km son:");
            while(!caminosCorrectos.esVacia()){
                Lista recorrido = (Lista) caminosCorrectos.recuperar(1);
                caminosCorrectos.eliminar(1);
                System.out.println("-Camino: " + recorrido.toString());
            }
        }
    }
    
    private static void consultarCaminosSinLocacion(){
        
        System.out.print("Escriba el nombre de la locacion de origen de los recorridos: ");
        String origen = TecladoIn.readLine();
        while(!mapa.existeLocacion(origen)){
            System.out.println("Error, locacion " + origen + " no existe.");
            System.out.print("Escriba el nombre de la locacion de origen de los recorridos: ");
            origen = TecladoIn.readLine();
        }
        
        System.out.print("Escriba el nombre de la locacion de destino de los recorridos: ");
        String destino = TecladoIn.readLine();
        while(!mapa.existeLocacion(destino)){
            System.out.println("Error, locacion " + destino + " no existe.");
            System.out.print("Escriba el nombre de la locacion de destino de los recorridos: ");
            destino = TecladoIn.readLine();
        }
        
        System.out.print("Escriba el nombre de la locacion a ignorar por los recorrido: ");
        String locacionIgnorada = TecladoIn.readLine();
        while(!mapa.existeLocacion(destino) && (locacionIgnorada.equals(origen) || locacionIgnorada.equals(destino))){
            System.out.println("Error, locacion " + destino + " no existe o es la misma que el origen o destino.");
            System.out.print("Escriba el nombre de la locacion a ignorar por los recorrido: ");
            locacionIgnorada = TecladoIn.readLine();
        }
        
        Lista caminosCorrectos = mapa.obtenerCaminosNoPasenPorLocacion(origen, destino, locacionIgnorada);
        
        if(caminosCorrectos.esVacia()){
            System.out.println("No existen caminos entre " + origen + " y " + destino + " que no pasen por " + locacionIgnorada);
        } else {
            System.out.println("Los caminos entre " + origen + " y " + destino + " que no pasen por " + locacionIgnorada + " son:");
            while(!caminosCorrectos.esVacia()){
                Lista recorrido = (Lista) caminosCorrectos.recuperar(1);
                caminosCorrectos.eliminar(1);
                System.out.println("-Camino: " + recorrido.toString());
            }
        }
    }
    
    private static void menuConsultasGenerales(){
        
        int op;
        String[] opciones = {
            "Mostrar ranking jugadores segun cantidad de victorias",
            "Mostrar todos los items unicos disponibles"
        };

        do {
            
        System.out.println();
        System.out.println("------------------  Menu consultas generales  ---------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ".- " + opciones[i]);
        }
        System.out.println("0.- Volver");
        System.out.println("-----------------------------------------------");

        do {
            op = TecladoIn.readLineInt();
            if (op < 0 || op > opciones.length) {
                System.out.println("El valor ingresado no es válido");
            }
        } while (op < 0 || op > opciones.length);

            switch (op) {
                case 1:
                    consultarRanking();
                    break;
                case 2:
                    consultarItemsUnicos();
                    break;
            }
        
        } while (op != 0);
    }
    
    private static void consultarRanking(){
        System.out.println("El ranking de los jugadores segun su cantidad de victorias es el siguiente:");
        System.out.println(jugadores.obtenerRanking());
    }
    
    private static void consultarItemsUnicos(){
        String stringItemsUnicos = tiendaItems.recuperarItemsUnicos();
        if(stringItemsUnicos.equals("")){
            System.out.println("No existen items unicos (que solo tienen una copia disponible)");
        } else {
            System.out.print("Los items unicos (que solo tienen una copia disponible) son: { ");
            System.out.print(stringItemsUnicos);
            System.out.println(" }");
        }
    }
    
    private static void cargaInicial(){
        
        try {
            
            System.out.println("Comenzando carga inicial");
            
            cargarItems();
            cargarJugadores();
            cargarLocaciones();
            cargarCaminos();
            
            try {
                salida = new PrintWriter(new FileOutputStream(direccion + "LOG.txt"));
            } catch (FileNotFoundException ex) {
                System.out.println("Excepcion " + ex.toString());
                Logger.getLogger(DungeonStructures.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Carga inicial finalizada");
            System.out.println("\n-------------------------------------\n\n\n\n");
            
            salida.println("\n\n-------------------------------------\n");
            salida.println("Estado del sistema luego de la carga inicial:");
            salida.println("\n-------------------------------------\n\n" + estadoSistema());
            
            
        } catch (FileNotFoundException ex) {
            System.out.println("Error archivo no encontrado al cargar datos.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error de IO al cargar datos.");
            ex.printStackTrace();
        }
    }
    
    private static void cargarItems() throws FileNotFoundException, IOException{
        
        //  FORMATO DE ITEMS: I: codigo; nombre; precio; puntos de ataque; puntos de defensa; copias disponibles
        
        System.out.println("\n------------------");
        System.out.println("Cargando items");
        System.out.println("--------------------\n");    
        
        BufferedReader entrada = new BufferedReader(new FileReader(direccion + "items.txt"));
        String cadena = entrada.readLine();
        
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
                    String nombre = objetoString[1].trim();
                    int precio = Integer.valueOf(objetoString[2].trim());
                    int puntosAtk = Integer.valueOf(objetoString[3].trim());
                    int puntosDef = Integer.valueOf(objetoString[4].trim());
                    int copias = Integer.valueOf(objetoString[5].trim());
                    Item objeto = new Item(codigo, nombre, precio, puntosAtk, puntosDef, copias);
                    if(insertarItem(objeto)){
                        System.out.println("Item cargado " + objeto.toStringCompleto());
                    } else {
                        System.out.println("Error cargando item " + objeto.getNombreCompuesto());
                    }
                }
            }
            
            cadena = entrada.readLine();
        }
        
        
        System.out.println("\n------------------");
        System.out.println("Fin carga de items");
        System.out.println("--------------------\n");      
        
        entrada.close();
    }
    
    private static void cargarJugadores() throws FileNotFoundException, IOException{
        
        //  FORMATO DE JUGADORES: J: nombre; tipo; categoria; dinero; lista de items
        //  FORMATO DE lista de items: <codigoItem, codigoItem, ... >
        
        System.out.println("\n------------------");
        System.out.println("Cargando jugadores");
        System.out.println("--------------------\n");      
        
        BufferedReader entrada = new BufferedReader(new FileReader(direccion + "jugadores.txt"));
        String cadena = entrada.readLine();
        
        while(cadena != null){
            String[] tipoYObjeto = cadena.split(":");

            if(!tipoYObjeto[0].trim().equals("J")){
                System.out.println("Error cargando jugador, formato equivocado");
            } else {
                String[] objetoString = tipoYObjeto[1].split(";");
                if(objetoString.length != 5){
                    System.out.println("Error cargando jugador, formato equivocado");
                } else {
                    String nombre = objetoString[0].trim();
                    String tipo = objetoString[1].trim();
                    String categoria = objetoString[2].trim();
                    int dinero = Integer.valueOf(objetoString[3].trim());
                    String stringItems = objetoString[4].trim();
                    
                    Jugador jugador = new Jugador(nombre, tipo, categoria, dinero);
                    
                    stringItems = stringItems.substring(1, stringItems.length() - 1);
                    String[] items = stringItems.split(", ");
                    
                    for (String items1 : items) {
                        regalarItem(jugador, items1.trim());
                    }
                    if(jugadores.agregarJugador(jugador)){
                        System.out.println("Jugador cargado " + jugador.toStringCompleto());
                    } else {
                        System.out.println("Error cargando jugador " + nombre);
                    }
                }
            }
            
            cadena = entrada.readLine();
        }
        
        System.out.println("\n------------------");
        System.out.println("Fin carga de jugadores");
        System.out.println("--------------------\n");      
        
        entrada.close();
    }
    
    
    
    private static void cargarLocaciones() throws FileNotFoundException, IOException{
        
        //  FORMATO DE Locaciones: L: nombre
        System.out.println("\n------------------");
        System.out.println("Cargando locaciones");
        System.out.println("--------------------\n");      
        
        BufferedReader entrada = new BufferedReader(new FileReader(direccion + "locaciones.txt"));
        String cadena = entrada.readLine();
        
        while(cadena != null){
            String[] tipoYObjeto = cadena.split(": ");
            
            if(!tipoYObjeto[0].trim().equals("L")){
                System.out.println("Error cargando locacion, formato equivocado");
            } else {
                String[] objetoString = tipoYObjeto[1].split("; ");
                if(objetoString.length != 1){
                    System.out.println("Error cargando item, formato equivocado");
                } else {
                    String nombre = objetoString[0].trim();
                    if(mapa.insertarLocacion(nombre)){
                        System.out.println("Locacion cargada " + nombre);
                    } else {
                        System.out.println("Error cargando locacion " + nombre);
                    }
                }
            }
            
            cadena = entrada.readLine();
        }
        
        System.out.println("\n------------------");
        System.out.println("Fin carga de locaciones");
        System.out.println("--------------------\n");      
        
        entrada.close();
    }
    
    private static void cargarCaminos() throws FileNotFoundException, IOException{
        
        //  FORMATO DE Caminos: C: nombre origen; nombre destino; distancia
        
        System.out.println("\n------------------");
        System.out.println("Cargando caminos");
        System.out.println("--------------------\n");      
        
        BufferedReader entrada = new BufferedReader(new FileReader(direccion + "caminos.txt"));
        String cadena = entrada.readLine();
        
        while(cadena != null){
            String[] tipoYObjeto = cadena.split(":");
            
            if(!tipoYObjeto[0].trim().equals("C")){
                System.out.println("Error cargando camino, formato equivocado");
            } else {
                String[] objetoString = tipoYObjeto[1].split(";");
                if(objetoString.length != 3){
                    System.out.println("Error cargando item, formato equivocado");
                } else {
                    String origen = objetoString[0].trim();
                    String destino = objetoString[1].trim();
                    int distancia = Integer.valueOf(objetoString[2].trim());
                    if(mapa.insertarCamino(origen, destino, distancia)){
                        System.out.println("Camino cargado. Desde " + origen + " hasta " + destino + ", " + distancia + "km");
                    } else {
                        System.out.println("Error cargando camino desde " + origen + " hasta " + destino);
                    }
                }
            }
            cadena = entrada.readLine();
        }
        
        System.out.println("\n------------------");
        System.out.println("Fin carga de caminos");
        System.out.println("--------------------\n");      

        entrada.close();
    }
    
    private static boolean insertarItem(Item item){
        boolean exito1 = tiendaItems.insertar(item);
        boolean exito2 = tablaItems.insertar(item.getCodigo(), item);
        return exito1 && exito2;
    }
    
    private static boolean eliminarItem(Item item){
        boolean exito2 = tablaItems.eliminar(item.getCodigo());
        boolean exito1 = tiendaItems.eliminar(item);
        return exito1 && exito2;
    }
    
    private static boolean comprarItem(Jugador jugador, String codigoItem){
        
        boolean exito;
        
        if(!tablaItems.existeClave(codigoItem)){
            exito = false;
            System.out.println("Error, no existe item con el codigo " + codigoItem);
        } else {
            Item itemAuxiliar = (Item) tablaItems.obtenerInformacion(codigoItem);

            if(jugador.getDinero() < itemAuxiliar.getPrecio()){
                exito = false;
                System.out.println("Error, al jugador no le alcanza el dinero para comprar el objeto");
            } else {
                exito = itemAuxiliar.restarCopia();

                if(exito){
                    jugador.insertarItem(new CopiaItem(itemAuxiliar.getCodigo(), itemAuxiliar.getNombre(), itemAuxiliar.getPrecio(), itemAuxiliar.getPuntosAtk(), itemAuxiliar.getPuntosDef()));
                    jugador.modificarDinero(itemAuxiliar.getPrecio() * (-1));
                    System.out.println("Jugador " + jugador.getNombre() + " ha comprado exitosamente el objeto " + itemAuxiliar.getNombreCompuesto());
                    salida.println("Jugador " + jugador.getNombre() + " ha comprado exitosamente el objeto " + itemAuxiliar.getNombreCompuesto());
                } else {
                    System.out.println("Error, no quedan mas copias del item " + itemAuxiliar.getCodigo() + "(" + itemAuxiliar.getNombre() + ")");
                }
            }
        }
        
        return exito;
    }
    
    //  Le agrega una copia de un item de forma gratuita a un jugador
    //      (Ignora el dinero, pero si checkea que hayan copias disponibles)
    private static boolean regalarItem(Jugador jugador, String codigoItem){
        
        boolean exito;
        
        if(!tablaItems.existeClave(codigoItem)){
            exito = false;
            System.out.println("Error, no existe item con el codigo " + codigoItem);
        } else {
            Item itemAuxiliar = (Item) tablaItems.obtenerInformacion(codigoItem);

            exito = (itemAuxiliar.getCantCopiasDisp() != 0);

            if(exito){
                jugador.insertarItem(new CopiaItem(itemAuxiliar.getCodigo(), itemAuxiliar.getNombre(), itemAuxiliar.getPrecio(), itemAuxiliar.getPuntosAtk(), itemAuxiliar.getPuntosDef()));
                System.out.println("Al jugador " + jugador.getNombre() + " se le ha agregado el objeto " + itemAuxiliar.getNombreCompuesto());
                salida.println("Al jugador " + jugador.getNombre() + " se le ha agregado el objeto " + itemAuxiliar.getNombreCompuesto());
            } else {
                System.out.println("Error, no quedan mas copias del item " + itemAuxiliar.getNombreCompuesto());
            }
        }
        
        return exito;
    }
    
    //  Funcion privada para crear un equipo, devuelve el exito.
    //      Precondicion: No exista un equipo con ese nombre
    private static boolean crearEquipo(String nombre){
        
        //  Variable para saber si hubo exito
        boolean exito;
        
        //  Si no hay jugadores esperando
        if(colaEspera.esVacia()){
            //  Falla
            exito = false;
        } else {
            //  Si hay, obtiene el primero
            Jugador jugador1 = (Jugador) colaEspera.obtenerFrente();
            //  Y lo saca de la cola
            colaEspera.eliminarFrente();
            //  Si solo habia uno
            if(colaEspera.esVacia()){
                //  No son suficientes, falla
                exito = false;
                //  Y vuelve a agregar al jugador
                sumarJugadorCola(jugador1);
            } else {
                //  Si hay, obtiene el nuevo primero
                Jugador jugador2 = (Jugador) colaEspera.obtenerFrente();
                //  Y lo elimina
                colaEspera.eliminarFrente();
                //  Si solo habian dos
                if(colaEspera.esVacia()){
                    //  Siguen sin ser suficientes, falla
                    exito = false;
                    //  Y los vuelve a sumar en el orden que los saco
                    sumarJugadorCola(jugador1);
                    sumarJugadorCola(jugador2);
                } else {
                    //  Si hay, significa que hay al menos tres jugadores esperando, tiene exito
                    exito = true;
                    //  Crea un nuevo equipo con el nombre pedido y los tres jugadores y lo
                    //      inserta el equipo a la estructura que contiene los equipos
                    Jugador jugador3 = (Jugador) colaEspera.obtenerFrente();
                    Equipo equipoNuevo = new Equipo(nombre, jugador1, jugador2, jugador3);
                    equipos.insertar(equipoNuevo);
                    //  Elimina el tercer jugador de la cola
                    colaEspera.eliminarFrente();
                }
            }
        }
        
        return exito;
    }
    
    private static void sumarJugadorCola(Jugador jugador){
        colaEspera.insertar(jugador, prioridadCategoria(jugador.getCategoria()));
        jugadoresEnColaOEquipo.agregarJugador(jugador);
    }
    
    private static void batallaEquipos(Equipo equipo1, Equipo equipo2){
        
        System.out.println("Iniciando batalla entre " + equipo1.getNombre() + " y " + equipo2.getNombre());
        salida.println("Iniciando batalla entre " + equipo1.getNombre() + " y " + equipo2.getNombre());
        if(!equipo1.getLocacionActual().equals(equipo2.getLocacionActual())){
            System.out.println("Error. No se encuentran en la misma locacion, cancelando batalla");
            salida.println("Error. No se encuentran en la misma locacion, cancelando batalla");
        } else {
            Equipo equipoInicial, equipoDefensor;
            //  El equipo de menor categoria es el que empieza, como la lista categorias
            //      devuelve la prioridad, significa que el de numero mas alto es el
            //      de menor prioridad
            if(prioridadCategoria(equipo1.getCategoria()) > prioridadCategoria(equipo2.getCategoria())){
                equipoInicial = equipo1;
                equipoDefensor = equipo2;
            } else {
                equipoInicial = equipo2;
                equipoDefensor = equipo1;
            }
            System.out.println("Categoria de " + equipo1.getNombre() + ": " + equipo1.getCategoria());
            System.out.println("Categoria de " + equipo2.getNombre() + ": " + equipo2.getCategoria());
            System.out.println("Comienza " + equipoInicial.getNombre());
            salida.println("Comienza " + equipoInicial.getNombre());
            
            //  Jugadores aun no derrotados de cada equipo
            Jugador[] jugadoresEquipoInicial = equipoInicial.getParticipantes();
            Jugador[] jugadoresEquipoDefensor = equipoDefensor.getParticipantes();

            Lista jugadoresRestantesInicial = new Lista();
            for (Jugador jugadorActual : jugadoresEquipoInicial) {
                jugadoresRestantesInicial.insertar(jugadorActual, 1);
            }
            
            Lista jugadoresRestantesDefensor = new Lista();
            for(Jugador jugadorActual : jugadoresEquipoDefensor) {
                jugadoresRestantesDefensor.insertar(jugadorActual, 1);
            }
            
            System.out.println("Jugadores de " + equipoInicial.getNombre() + ":");
            System.out.println(jugadoresRestantesInicial.toString());
            System.out.println();
            System.out.println("Jugadores de " + equipoDefensor.getNombre() + ":");
            System.out.println(jugadoresRestantesDefensor.toString());
            System.out.println();
            
            Equipo equipoGanador = null;
            Equipo equipoPerdedor = null;
            Jugador[] ganadores = null;
            Jugador[] perdedores = null;
            int ronda = 1;
            
            while(ronda < 3 && equipoGanador == null){
                System.out.println("\n\n--------------------------------------------------------------------------\n\n");
                System.out.println("Comienzo ronda " + ronda);
                System.out.println("\n\n--------------------------------------------------------------------------\n\n");
                
                System.out.println("\nTurno del equipo " + equipoInicial.getNombre() + "\n");
                int posAtacante = 1;
                while(posAtacante <= jugadoresRestantesInicial.longitud() && equipoGanador == null){
                    Jugador jugadorActual = (Jugador) jugadoresRestantesInicial.recuperar(posAtacante);
                    int posJugadorDefensor = Aleatorio.intAleatorio(1, jugadoresRestantesDefensor.longitud());
                    Jugador jugadorDefensor = (Jugador) jugadoresRestantesDefensor.recuperar(posJugadorDefensor);
                    realizarAtaque(jugadorActual, jugadorDefensor);
                    if(jugadorDefensor.esDerrotado()){
                        jugadoresRestantesDefensor.eliminar(posJugadorDefensor);
                        if(jugadoresRestantesDefensor.esVacia()){
                            equipoGanador = equipoInicial;
                            equipoPerdedor = equipoDefensor;
                            ganadores = jugadoresEquipoInicial;
                            perdedores = jugadoresEquipoDefensor;
                        }
                    }
                    posAtacante++;
                }
                
                if(equipoGanador == null)System.out.println("\nTurno del equipo " + equipoDefensor.getNombre() + "\n");
                posAtacante = 1;
                while(posAtacante <= jugadoresRestantesDefensor.longitud() && equipoGanador == null){
                    Jugador jugadorActual = (Jugador) jugadoresRestantesDefensor.recuperar(posAtacante);
                    int posJugadorDefensor = Aleatorio.intAleatorio(1, jugadoresRestantesInicial.longitud());
                    Jugador jugadorDefensor = (Jugador) jugadoresRestantesInicial.recuperar(posJugadorDefensor);
                    realizarAtaque(jugadorActual, jugadorDefensor);
                    if(jugadorDefensor.esDerrotado()){
                        jugadoresRestantesInicial.eliminar(posJugadorDefensor);
                        if(jugadoresRestantesInicial.esVacia()){
                            equipoGanador = equipoDefensor;
                            equipoPerdedor = equipoInicial;
                            ganadores = jugadoresEquipoDefensor;
                            perdedores = jugadoresEquipoInicial;
                        }
                    }
                    posAtacante++;
                }
                
            ronda++;
            }
            System.out.println("\n\n--------------------------------------------------------------------------\n\n");
            System.out.println("Fin de la pelea \n");
            
            if(equipoGanador == null){
                //  Caso Empate
                System.out.println("Ha sido un empate. Todos los participantes ganan $500");
                System.out.println("Estado de los participantes luego de esta dura batalla: ");
                System.out.println("Jugadores de " + equipoInicial.getNombre() + ":");
                for(Jugador jugadorActual : jugadoresEquipoInicial){
                    jugadorActual.modificarDinero(500);
                    jugadorActual.restablecerSalud();
                    System.out.println(jugadorActual.toString() + "\n");
                }
                System.out.println("Jugadores de " + equipoDefensor.getNombre() + ":");
                for(Jugador jugadorActual : jugadoresEquipoDefensor){
                    jugadorActual.modificarDinero(500);
                    jugadorActual.restablecerSalud();
                    System.out.println(jugadorActual.toString() + "\n");
                }
            } else {
                //  Gano un equipo
                System.out.println("Equipo " + equipoGanador.getNombre() + " ha ganado. Sus participantes ganan $1000 y los perdedores pierden $500. Se ha restablecido la salud de todos");
                System.out.println("Estado de los participantes luego de esta dura batalla: ");
                System.out.println("Jugadores de " + equipoGanador.getNombre() + ":");
                for(Jugador jugadorActual : ganadores){
                    jugadorActual.modificarDinero(1000);
                    jugadorActual.restablecerSalud();
                    System.out.println(jugadorActual.toString() + "\n");
                }
                System.out.println("Jugadores de " + equipoPerdedor.getNombre() + ":");
                for(Jugador jugadorActual : perdedores){
                    jugadorActual.modificarDinero(-500);
                    jugadorActual.restablecerSalud();
                    System.out.println(jugadorActual.toString() + "\n");
                }
                //  Mover equipo ganador a otra locacion
                Lista adyacentes;
                String nuevaLocacion;
                
                do{
                System.out.println("Equipo " + equipoGanador.getNombre() + " debe moverse. A donde desea moverse?");
                adyacentes = mapa.obtenerAdyacentes(equipoGanador.getLocacionActual());
                System.out.println("Opciones: " + adyacentes.toString());
                nuevaLocacion = TecladoIn.readLine();
                }while(adyacentes.localizar(nuevaLocacion) < 0);
                
                equipoGanador.setLocacionActual(nuevaLocacion);
            }
        }
        
    }
    
    private static void realizarAtaque(Jugador atacante, Jugador defensor){
        int atk = atacante.calcularAtk();
        int def = defensor.calcularDef();
        int danio = atk - def;
        System.out.println("Jugador " + atacante.getNombre() + " golpea a " + defensor.getNombre());
        salida.println("Jugador " + atacante.getNombre() + " golpea a " + defensor.getNombre());
        if(danio > 0){
            System.out.println("El ataque inflinge " + danio + " de danio (" + atk + " ataque contra " + def + " defensa)");
            salida.println("El ataque inflinge " + danio + " de danio (" + atk + " ataque contra " + def + " defensa)");
            defensor.daniar(danio);
            if(defensor.esDerrotado()){
                System.out.println(atacante.getNombre() + " derrota a " + defensor.getNombre() + ". Ganando $1000.");
                atacante.sumarVictoria();
                atacante.modificarDinero(1000);
                defensor.sumarDerrota();
            } else {
                System.out.println(defensor.getNombre() + " resulta lastimado, quedando con " + defensor.getSalud() + " puntos de salud.");
            }
        } else {
            System.out.println("El ataque no realiza danio (" + atk + " ataque contra " + def + " defensa)");
            salida.println("El ataque no realiza danio (" + atk + " ataque contra " + def + " defensa)");
        }
        atacante.desgastarItems();
    }
    
    private static String estadoSistema(){
        
        String resultado;
        
        resultado = "Items en la tienda \n\n-------------------------------------\n\n" + tiendaItems.toString();
        resultado += "\n\n--------------------------------------------------------------------------\n\n";
        //resultado += "Items por codigo \n\n-------------------------------------\n\n" + tablaItems.toString();
        //resultado += "\n\n--------------------------------------------------------------------------\n\n";
        resultado += "\nJugadores: \n\n-------------------------------------\n\n" + jugadores.toString();
        resultado += "\n\n--------------------------------------------------------------------------\n\n";
        resultado += "\n Mapa: \n\n-------------------------------------\n\n" + mapa.toString();
        resultado += "\n\n--------------------------------------------------------------------------\n\n";
        resultado += "\n Cola de espera: \n\n-------------------------------------\n\n" + colaEspera.toString();
        resultado += "\n\n--------------------------------------------------------------------------\n\n";
        resultado += "\n Equipos: \n\n-------------------------------------\n\n" + equipos.toString();
        resultado += "\n\n--------------------------------------------------------------------------\n\n";

        return resultado;
    }
    
    private static boolean existeCategoria(char categoria){
        return categorias.existeClave(categoria);
    }
    
    private static boolean existeTipo(char tipo){
        return tipos.existeClave(tipo);
    }
    
    private static String nombreTipo(char tipo){
        return (String) tipos.obtenerInformacion(tipo);
    }
    
    private static String nombreCategoria(char categoria){
        return (String) categorias.obtenerInformacion(categoria);
    }
    
    private static int prioridadCategoria(char categoria){
        return prioridades.localizar(categoria);
    }
}

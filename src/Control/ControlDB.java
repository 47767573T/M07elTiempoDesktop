package Control;


import Modelo.createSQLite;
import Modelo.insertSQLite;
import Modelo.selectSQLite;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;


public class ControlDB {


    static Random rnd = new Random();   //Crear nombres de base de datos aleatorias

    public static String ficheroDB = "jdbc:sqlite:moviesDB"+rnd.nextInt(5000)+".db";
    public static String nombreTablaPeliculas = "PELICULAS";
    public static String nombreTablaActores = "ACTORES";
    public static int lastIdCast = 1;
    public static int idIniPelicula = 600;
    public static int numPeliculas = 100;


    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);

        //:::::::::::::::::::::::::::::::::::::::PREPARAMOS Y CREAMOS LA TABLA ANTES DE INSERTAR REGISTROS
        String s = "";
        String api_key = "e6f2c549601727fca2e90f4291bbe34d";

        createSQLite.createTabla();


        //:::::::::::::::::::::::::::::::::::::::INTRODUCIMOS LOS REGISTROS
        for (int i = 0; i < numPeliculas; i++) {
            int peliculaIndex = idIniPelicula + i;
            String peliculaID = String.valueOf(peliculaIndex);
            String actoresURL = "https://api.themoviedb.org/3/movie/" + peliculaID + "/credits?api_key=" + api_key;
            String peliculasURL = "https://api.themoviedb.org/3/movie/" + peliculaID + "?api_key=" + api_key;
            try {

                s = getHTML(peliculasURL);
                jsonToTablaPelis(s, peliculaIndex);
                s = getHTML(actoresURL);
                jsonToTablaActores(s, peliculaIndex);


            } catch (Exception e) {
                System.out.println("La peli " + peliculaID + " no existeix ");
            }
        }
                //::::::::::::::::::::::::::::::::::::::::GENERAMOS LOS 2 MODOS DE CONSULTA
                //MODO 1:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                System.out.println("\nQUERY MODO 1:\n");

                System.out.println("Listado de peliculas: ");
                selectSQLite.movieList();

                System.out.println("Elija un ID de la pelicula del listado");
                int idPeli = scn.nextInt();
                selectSQLite.query1(idPeli);


                //MODO 2:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                System.out.println("\nQUERY MODO 1:\n");
                System.out.println("");
                selectSQLite.actorList();

                System.out.println("Elija un ID del actor del listado");
                int idActor = scn.nextInt();
                selectSQLite.query2(idActor);
    }


    public static void jsonToTablaPelis (String cadena, int id){

        Object jsonObj = JSONValue.parse(cadena);
        JSONObject jsonItem=(JSONObject)jsonObj;
        String titulo = (String) jsonItem.get("original_title");
        String tituloCorregido = correctComillas(titulo);

        String fecha = (String) jsonItem.get("release_date");

        insertSQLite.insertTablaPelis(id, tituloCorregido, fecha);
    }

    public static void jsonToTablaActores (String cadena, int idPeli){
        Object jsonObj = JSONValue.parse(cadena);
        JSONObject jsonItem=(JSONObject)jsonObj;
        JSONArray casting = (JSONArray)jsonItem.get("cast");

        for (int i = 0; i < casting.size(); i++) {

            JSONObject jo= (JSONObject)casting.get(i);
            String nombre = (String) jo.get("name");
            String nombreCorregido = correctComillas(nombre);

            long actor = (long) jo.get("id");
            String personaje = (String) jo.get("character");
            String personajeCorregido = correctComillas(personaje);

            insertSQLite.insertTablaActores(lastIdCast, nombreCorregido, actor, personajeCorregido, idPeli);
            lastIdCast++;

        }
    }

    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public static String correctComillas (String fraseConComillas){
        String comillas = "\'";

        if (fraseConComillas.contains(comillas)) {
        String fraseSinComillas = fraseConComillas.replace(comillas,"\"");
            return fraseSinComillas;
        }

        return fraseConComillas;
    }

}

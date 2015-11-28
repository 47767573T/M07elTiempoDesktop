package Control;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ControlDB {

    public static String baseURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    public static String ciudad = "Barcelona";
    public static String pais;
    public static ArrayList <String> tMax;
    public static ArrayList <String> tMin;
    public static ArrayList <String> tMed;
    public static ArrayList <String> estado;
    public static String appID = "f50a8337632b64b69714d5b175384000";


    public void meteoPeticion(String ciudad, int nDias) {
        this.ciudad = ciudad;

        String jsonPeticion = null;
        String url = baseURL+ciudad+"&mode=json&units=metric&cnt="+nDias+"&appid="+appID;

        try {
            jsonPeticion = getHTML(url);
        } catch (Exception e) {
            System.out.println("No existe la ciudad");
        }

        assert jsonPeticion != null;
        JSONObject meteoObj = (JSONObject) JSONValue.parse(jsonPeticion);

        //Extraemos los valores que nos intresan del JSON
        JSONArray meteoAL = (JSONArray) meteoObj.get("list");    //Listado con temperaturas y humedad
        pais = (String)meteoObj.get("country");

        for (int i = 0; i < meteoAL.size(); i++) {
            JSONObject meteoDay = (JSONObject) meteoAL.get(i);              //Objeto de la meteorologia diaria
            JSONObject meteoTemp = (JSONObject) meteoDay.get("temp");       //Objeto de la temperatura diaria
            JSONObject meteoEstado = (JSONObject) meteoDay.get("weather");  //Objeto del estado diario

            tMax.add((String) meteoTemp.get("min"));
            tMin.add((String) meteoTemp.get("max"));
            tMed.add((String) meteoTemp.get("day"));

            estado.add((String) meteoEstado.get("main"));
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
}

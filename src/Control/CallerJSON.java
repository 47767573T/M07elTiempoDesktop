package Control;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class CallerJSON {

    private static String baseURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    private static int totalDias = 10;
    private static String ciudad;
    private static String pais;
    private static ArrayList <String> tMax = new ArrayList<>();
    private static ArrayList <String> tMin = new ArrayList<>();
    private static ArrayList <String> tMed = new ArrayList<>();
    private static ArrayList <String> estado = new ArrayList<>();
    private static String appID = "f50a8337632b64b69714d5b175384000";


    public void meteoPeticion(String ciudad) {
        this.ciudad = ciudad;

        String jsonPeticion = null;
        String url = baseURL+ciudad+"&mode=json&units=metric&cnt="+totalDias+"&appid="+appID;

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

        for (Object aMeteoAL : meteoAL) {
            JSONObject meteoDay = (JSONObject) aMeteoAL;              //Objeto de la meteorologia diaria
            JSONObject meteoTemp = (JSONObject) meteoDay.get("temp");       //Objeto de la temperatura diaria

            tMax.add(String.valueOf(meteoTemp.get("min")));
            tMin.add(String.valueOf(meteoTemp.get("max")));
            tMed.add(String.valueOf(meteoTemp.get("day")));
        }
    }

    public static void borrarArrays(){
        tMax.clear();
        tMin.clear();
        tMed.clear();
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


    public static String gettMax(int index) {
        return tMax.get(index);
    }

    public static String gettMin(int index) {
        return tMin.get(index);
    }

    public static String gettMed(int index) {
        return tMed.get(index);
    }
}


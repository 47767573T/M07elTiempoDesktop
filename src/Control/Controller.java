package Control;

import javafx.collections.FXCollections;
import javafx.scene.control.*;

import java.util.ArrayList;

/**
 * Controlador de l'aplicacio
 */
public class Controller {



    public TextField tfCiudad;
    public TextField tfDias;
    public ListView<String> lvMeteo = new ListView<String>();
    public Button btnMostrar;
    public Label lbEstado;
    public Label lbTemp;
    public Label lbCiudad;
    public CallerJSON cj = new CallerJSON();
    public ArrayList <String> infoMeteo = new ArrayList<>();




    public void initialize(){

        //Muesta un mensaje inicial que da instrucciones sencillas
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion inicial");
        alert.setHeaderText("Te mostrare un listado de las temperaturas si me das la ciudad y los dias que necesitas");

        alert.showAndWait();

        tfCiudad.setText("Ciudad");
        tfDias.setText("Dias");
    }


    public void onMostrarMeteo(){
        cj.borrarArrays();
        cj.meteoPeticion(tfCiudad.getText());
        int dias = Integer.parseInt(tfDias.getText());
        System.out.println(dias);
        infoMeteo.clear();

        //Crea el ArrayList que mostrará en el ListView
        for (int i = 0; i < dias; i++) {

            infoMeteo.add((i+1)+" dia - Maxima temperatura: "+cj.gettMin(i)
                +", Minima temperatura: "+cj.gettMax(i));
            System.out.println(i);
        }
        System.out.println("tamaño:"+infoMeteo.size());
        lbTemp.setText("Temperatura media de hoy: " + cj.gettMed(0));

        lvMeteo.setItems(FXCollections.observableArrayList(infoMeteo));


    }

}
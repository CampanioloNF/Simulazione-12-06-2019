/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Model;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="boxIngrediente"
    private ComboBox<Condiment> boxIngrediente; // Value injected by FXMLLoader

    @FXML // fx:id="btnDietaEquilibrata"
    private Button btnDietaEquilibrata; // Value injected by FXMLLoader
    
    @FXML
    private TextArea txtResult;


    private int calorie;
    
    @FXML
    void doCalcolaDieta(ActionEvent event) {

    	txtResult.clear();
    	
    	Condiment c = boxIngrediente.getValue();
    	
    	if(c!=null) {
    		
    		for(Condiment con : model.dietaOttima(c))
    			txtResult.appendText("  - "+con.toString()+" con "+con.getCondiment_calories()+" cal, presente in "+con.getFoods()+" cibi\n" );
    		
    	}
    	else
    		txtResult.appendText("Scegliere un ingrediente");
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.clear();
    	
    	try {
    		
    		calorie = Integer.parseInt(txtCalorie.getText());
    		model.creaGrafo(calorie);
    		List<Condiment> ris = model.getListCondiment();
    		
    		txtResult.appendText("La lista di ingredienti è la seguente: \n\n");
    		for(Condiment con : ris)
    			txtResult.appendText("  - "+con.toString()+" con "+con.getCondiment_calories()+" cal, presente in "+con.getFoods()+" cibi\n" );
    		boxIngrediente.setItems(FXCollections.observableList(ris));
    		
    	}catch(NumberFormatException nfe) {
    		txtResult.appendText("Aggiungere un numero intero");
    		return;
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxIngrediente != null : "fx:id=\"boxIngrediente\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnDietaEquilibrata != null : "fx:id=\"btnDietaEquilibrata\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}

package view.menuManagement;

import bean.Menu;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import service.MenuService;
import utility.dataHandler.DataValidation;
import utility.popUp.AlertPopUp;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class Create implements Initializable {
    @FXML
    private TextField searchTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private Label menuImageLabel;

    @FXML
    private TextField priceTextField;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView menuImageView;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private ChoiceBox<String> availabilityChoiceBox;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton addButton;

    private static File staticFile;

    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> choiceBoxList = FXCollections.observableArrayList("Available", "Not Available");
        availabilityChoiceBox.setValue("Available");
        availabilityChoiceBox.setItems(choiceBoxList);
        loadData();
    }
    public  void loadData(){

    }
    private void clearLabels() {
        nameLabel.setText("");
        infoLabel.setText("");
        priceLabel.setText("");
        menuImageLabel.setText("");
    }
    private boolean menuValidation() {
        return DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(infoTextArea.getText())
                && DataValidation.TextFieldNotEmpty(priceTextField.getText())
                && DataValidation.ImageFieldNotEmpty(menuImageView)

                && DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(infoTextArea.getText(), 400)

                && DataValidation.isValidNumberFormat(priceTextField.getText());


    }

    private void menuValidationMessage() {

        if (!(DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(infoTextArea.getText())
                && DataValidation.TextFieldNotEmpty(priceTextField.getText())
                && DataValidation.ImageFieldNotEmpty(menuImageView))) {
            DataValidation.TextFieldNotEmpty(nameTextField.getText(), nameLabel, "Menu Name Required!");
            DataValidation.TextFieldNotEmpty(infoTextArea.getText(), infoLabel, "Menu Information Required!");
            DataValidation.TextFieldNotEmpty(priceTextField.getText(), priceLabel, "Menu Price Required!");
            DataValidation.ImageFieldNotEmpty(menuImageView, menuImageLabel, "Select a Image!");

        }
        if (!(DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(infoTextArea.getText(), 400))) {

            DataValidation.isValidMaximumLength(nameTextField.getText(), 45, nameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(infoTextArea.getText(), 400, infoLabel, "Field Limit 400 Exceeded!");
        }
        DataValidation.isValidNumberFormat(priceTextField.getText(), priceLabel, "Invalid Price Amount!");
    }
    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
    }

    @FXML
    private void chooseImage(ActionEvent event){
        menuImageLabel.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg"));
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            staticFile = file;
            Image image = new Image(file.toURI().toString());
            menuImageView.setImage(image);
        }
    }

    @FXML
    void addMenu(ActionEvent event) {

        clearLabels();
        if (menuValidation()) {
            Menu menu = new Menu();
            MenuService menuService = new MenuService();


            menu.setmName(nameTextField.getText());
            menu.setmInfo(infoTextArea.getText());
            menu.setmPrice(Double.valueOf(priceTextField.getText()));
            menu.setmAvailability(availabilityChoiceBox.getValue());
            menu.setmImage(menuImageView);


            if (menuService.insertMenuData(menu)) {
                AlertPopUp.insertSuccesfully("Menu");
                //loadData();
//                searchTable();
//                clearFields(event);
                //need to go back :D
            } else
                AlertPopUp.insertionFailed("Menu");

        } else
            menuValidationMessage();
    }
}

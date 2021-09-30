package view.supplierManagement;

import bean.Supplier;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.SupplierService;
import utility.dataHandler.DataValidation;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddSupplierController implements Initializable {
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField nicTextField;

    @FXML
    private ChoiceBox<String> typeChoiceBox;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label nicLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private JFXButton cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> supplierTypeList = FXCollections.observableArrayList("Raw Material", "Ready Made", "Raw & Ready Made");
        typeChoiceBox.setValue(supplierTypeList.get(0));
        typeChoiceBox.setItems(supplierTypeList);
    }

    @FXML
    void clearFields(ActionEvent event) {
        nameTextField.setText("");
        nicTextField.setText("");
        emailTextField.setText("");
        addressTextField.setText("");
        phoneTextField.setText("");
        clearLabels();
    }

    private void clearLabels() {
        nameLabel.setText("");
        nicLabel.setText("");
        emailLabel.setText("");
        addressLabel.setText("");
        phoneLabel.setText("");
    }



    private boolean fieldValidation() {

        if (!(DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(nicTextField.getText())
                && DataValidation.TextFieldNotEmpty(phoneTextField.getText())
                && DataValidation.TextFieldNotEmpty(addressTextField.getText())
                && DataValidation.TextFieldNotEmpty(emailTextField.getText())

                && DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(emailTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(addressTextField.getText(), 100)

                && DataValidation.isValidNIC(nicTextField)
                && DataValidation.isValidEmail(emailTextField.getText())
                && DataValidation.isValidPhoneNo(phoneTextField.getText()))) {

            DataValidation.TextFieldNotEmpty(nameTextField.getText(), nameLabel, "Supplier Name Required!");
            DataValidation.TextFieldNotEmpty(nicTextField.getText(), nicLabel, "Supplier NIC Required!");
            DataValidation.TextFieldNotEmpty(phoneTextField.getText(), phoneLabel, "Supplier Contact Number Required!");
            DataValidation.TextFieldNotEmpty(addressTextField.getText(), addressLabel, "Supplier Address Required!");
            DataValidation.TextFieldNotEmpty(emailTextField.getText(), emailLabel, "Supplier Email Required!");

            DataValidation.isValidMaximumLength(nameTextField.getText(), 45, nameLabel, "Field Character Limit 45 Exceeded");
            DataValidation.isValidMaximumLength(emailTextField.getText(), 45, nameLabel, "Field Character Limit 45 Exceeded");
            DataValidation.isValidMaximumLength(addressTextField.getText(), 100, nameLabel, "Field Character Limit 100 Exceeded");

            DataValidation.isValidEmail(emailTextField.getText(), emailLabel, "Invalid Email Address!");
            DataValidation.isValidNIC(nicTextField, nicLabel, "Invalid NIC!");
            DataValidation.isValidPhoneNo(phoneTextField.getText(), phoneLabel, "Invalid Phone Number!");
            return false;

        }else
            return true;
    }

    @FXML
    void addSupplier(ActionEvent event){
        clearLabels();
        if (fieldValidation()) {
            Supplier supplier = new Supplier();
            SupplierService supplierService = new SupplierService();

            supplier.setName(nameTextField.getText());
            supplier.setNic(nicTextField.getText());
            supplier.setEmail(emailTextField.getText());
            supplier.setAddress(addressTextField.getText());
            supplier.setPhoneNumber(phoneTextField.getText());
            supplier.setType(typeChoiceBox.getValue());

            if (supplierService.insertSupplierData(supplier)) {
                AlertPopUp.insertSuccesfully("Supplier");
                clearFields(event);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("supplierDetails.fxml"));
                try {
                    Parent root = (Parent) loader.load();
                    SupplerDetailController supplerDetailController = loader.getController();

                    supplerDetailController.setState(true);
                    closeStage();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else
                AlertPopUp.insertionFailed("Supplier");
        }
    }
    private void closeStage(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void closeStage(ActionEvent actionEvent){
        // get a handle to the stage
        closeStage();
    }
}

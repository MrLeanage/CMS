package view.supplierManagement;

import bean.Employee;
import bean.Supplier;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.SupplierService;
import utility.dataHandler.DataValidation;
import utility.dataHandler.UtilityMethod;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UpdateSupplierController implements Initializable {
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

    @FXML
    private TextField idTextField;

    private static Supplier selectedSupplier;

    private static ArrayList<String> nicList = new ArrayList<>();

    private static ArrayList<String> phoneList  = new ArrayList<>();

    private static ArrayList<String> emailList  = new ArrayList<>();

    private ObservableList<Supplier> supplierObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInitData();
    }

    private void setInitData(){
        ObservableList<String> supplierTypeList = FXCollections.observableArrayList("Raw Material", "Ready Made", "Raw & Ready Made");
        typeChoiceBox.setValue(supplierTypeList.get(0));
        typeChoiceBox.setItems(supplierTypeList);

        idTextField.setText(selectedSupplier.getId());
        nameTextField.setText(selectedSupplier.getName());
        nicTextField.setText(selectedSupplier.getNic());
        emailTextField.setText(selectedSupplier.getEmail());
        addressTextField.setText(selectedSupplier.getAddress());
        phoneTextField.setText(selectedSupplier.getPhoneNumber());
        typeChoiceBox.setValue(selectedSupplier.getType());

        SupplierService supplierService = new SupplierService();
        supplierObservableList = supplierService.loadAllSupplierData();
        for(Supplier supplier : supplierObservableList){
            nicList.add(supplier.getNic().toLowerCase());
            phoneList.add(supplier.getPhoneNumber());
            emailList.add(supplier.getEmail().toLowerCase());
        }

    }
    @FXML
    void clearFields(ActionEvent event) {
        idTextField.setText(selectedSupplier.getId());
        nameTextField.setText(selectedSupplier.getName());
        nicTextField.setText(selectedSupplier.getNic());
        emailTextField.setText(selectedSupplier.getEmail());
        addressTextField.setText(selectedSupplier.getAddress());
        phoneTextField.setText(selectedSupplier.getPhoneNumber());
        typeChoiceBox.setValue(selectedSupplier.getType());
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
        boolean specificNICFieldValidation = true;
        boolean specificPhoneFieldValidation = true;
        boolean specificEmailFieldValidation = true;

        for(Supplier supplier : supplierObservableList){
            if(supplier.getId().equals(selectedSupplier.getId())){
                if((!(supplier.getPhoneNumber()).equals(phoneTextField.getText()))
                        && UtilityMethod.checkDataAvailability(phoneList, phoneTextField.getText().toLowerCase()))
                    specificPhoneFieldValidation = false;

                if(!(supplier.getNic().equals(nicTextField.getText()))
                        && UtilityMethod.checkDataAvailability(nicList, nicTextField.getText().toLowerCase()))
                    specificNICFieldValidation = false;

                if(!(supplier.getEmail().equals(emailTextField.getText()))
                        && UtilityMethod.checkDataAvailability(emailList, emailTextField.getText().toLowerCase()))
                    specificEmailFieldValidation = false;
            }


        }

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
                && DataValidation.isValidPhoneNo(phoneTextField.getText())
                && specificNICFieldValidation
                && specificPhoneFieldValidation
                && specificEmailFieldValidation)) {

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

            for(Supplier supplier : supplierObservableList){
                if(supplier.getId().equals(selectedSupplier.getId())){
                    if((!(supplier.getPhoneNumber()).equals(phoneTextField.getText()))
                            && UtilityMethod.checkDataAvailability(phoneList, phoneTextField.getText().toLowerCase()))
                        checkPhoneAvailability();

                    if(!(supplier.getNic().equals(nicTextField.getText()))
                            && UtilityMethod.checkDataAvailability(nicList, nicTextField.getText().toLowerCase()))
                        checkNICAvailability();

                    if(!(supplier.getEmail().equals(emailTextField.getText()))
                            && UtilityMethod.checkDataAvailability(emailList, emailTextField.getText().toLowerCase()))
                        checkEmailAvailability();
                }
            }

            return false;

        }else
            return true;
    }

    @FXML
    void updateSupplier(ActionEvent event){
        clearLabels();
        if (fieldValidation()) {
            Supplier supplier = new Supplier();
            SupplierService supplierService = new SupplierService();

            supplier.setId(idTextField.getText());
            supplier.setName(nameTextField.getText());
            supplier.setNic(nicTextField.getText());
            supplier.setEmail(emailTextField.getText());
            supplier.setAddress(addressTextField.getText());
            supplier.setPhoneNumber(phoneTextField.getText());
            supplier.setType(typeChoiceBox.getValue());

            if (supplierService.updateSupplierData(supplier)) {
                AlertPopUp.updateSuccesfully("Supplier");
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
                AlertPopUp.updateFailed("Supplier");
        }
    }

    @FXML
    private void checkNICAvailability() {
        for(Supplier supplier : supplierObservableList){
            //set nothing if
            if((supplier.getNic().equals(nicTextField.getText()) && supplier.getId().equals(selectedSupplier.getId()))){
                nicLabel.setText("");
            }else{
                DataValidation.checkNICAvailability(nicTextField, nicLabel, nicList);
            }
        }

    }

    @FXML
    private void checkPhoneAvailability() {

        for(Supplier supplier : supplierObservableList){
            if(((supplier.getPhoneNumber()).equals(phoneTextField.getText()) && supplier.getId().equals(selectedSupplier.getId()))){
                phoneLabel.setText("");
            }else{
                DataValidation.checkPhoneAvailability(phoneTextField, phoneLabel, phoneList);
            }
        }

    }

    @FXML
    private void checkEmailAvailability() {

        for(Supplier supplier : supplierObservableList){
            if(((supplier.getEmail()).equals(emailTextField.getText()) && supplier.getId().equals(selectedSupplier.getId()))){
                emailLabel.setText("");
            }else{
                DataValidation.checkEmailAvailability(emailTextField, emailLabel, emailList);
            }
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

    public static void setData(Supplier supplier){
        selectedSupplier = supplier;
    }
}

package view.employeeManagement;

import bean.Designation;
import bean.Employee;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import service.DesignationService;
import service.EmployeeService;
import utility.dataHandler.DataValidation;
import utility.dataHandler.UtilityMethod;
import utility.navigation.Navigation;
import utility.popUp.AlertPopUp;
import view.appHome.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Create implements Initializable {
    @FXML
    private Navigation navigation;
    @FXML
    private TextField searchTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private ChoiceBox<String> employeeTypeChoiceBox;

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label designationLabel;

    @FXML
    private TextField nicTextField;

    @FXML
    private Label nicLabel;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton cancelButton;

    private static ArrayList<String> nicList = new ArrayList<>();

    private static ArrayList<String> phoneList  = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInitData();
    }

    public void setInitData(){
        DesignationService designationService = new DesignationService();
        ObservableList<String> designationList = FXCollections.observableArrayList();
        for(Designation designation : designationService.loadAllDesignationData()){
            if(designation.getdStatus().equals("Active"))
                designationList.add(designation.getdName());
        }
        employeeTypeChoiceBox.setValue(designationList.get(0));
        employeeTypeChoiceBox.setItems(designationList);

        EmployeeService employeeService = new EmployeeService();
        for(Employee employee : employeeService.loadAllEmployeeData()){
            nicList.add(employee.geteNIC().toLowerCase());
            phoneList.add("0"+employee.geteContactNo().toString());
        }

    }

    @FXML
    private void storeEmp(ActionEvent event){
        clearLabels();
        if (employeeValidation()) {
            Employee employee = new Employee();
            EmployeeService employeeService = new EmployeeService();
            employee.seteName(nameTextField.getText());
            employee.seteNIC(nicTextField.getText());
            employee.seteContactNo(Integer.valueOf(phoneTextField.getText()));
            employee.seteDesignation(employeeTypeChoiceBox.getValue());


            if (employeeService.insertEmployeeData(employee)) {
                AlertPopUp.insertSuccesfully("Employee");
               // loadData();
                //searchTable();
                clearFields(event);
                Stage stage = EmployeeDetailController.getPrimaryStage();
                stage.close();
            } else
                AlertPopUp.insertionFailed("Employee");

        } else
            employeeValidationMessage();
    }
    @FXML
    void clearFields(ActionEvent event) {
        nameTextField.setText("");
        nicTextField.setText("");
        phoneTextField.setText("");
        resetComponents();
        clearLabels();
    }

    private void clearLabels() {
        nameLabel.setText("");
        nicLabel.setText("");
        phoneLabel.setText("");
    }

    private boolean employeeValidation() {
        return DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(nicTextField.getText())
                && DataValidation.TextFieldNotEmpty(phoneTextField.getText())

                && DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(nicTextField.getText(), 12)
                && DataValidation.isValidMaximumLength(phoneTextField.getText(), 10)

                && DataValidation.isValidNIC(nicTextField)
                && DataValidation.isValidPhoneNo(phoneTextField.getText())

                && !UtilityMethod.checkDataAvailability(nicList, nicTextField.getText().toLowerCase())
                && !UtilityMethod.checkDataAvailability(phoneList, phoneTextField.getText().toLowerCase());


    }

    private void employeeValidationMessage() {
        System.out.println("executed");
        if (!(DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(nicTextField.getText())
                && DataValidation.TextFieldNotEmpty(phoneTextField.getText()))) {
            DataValidation.TextFieldNotEmpty(nameTextField.getText(), nameLabel, "Employee Name Required!");
            DataValidation.TextFieldNotEmpty(nicTextField.getText(), nicLabel, "Employee NIC Required!");
            DataValidation.TextFieldNotEmpty(phoneTextField.getText(), phoneLabel, "Employee Contact Number Required!");

        }
        if (!(DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(nicTextField.getText(), 12)
                && DataValidation.isValidMaximumLength(phoneTextField.getText(), 10))) {

            DataValidation.isValidMaximumLength(nameTextField.getText(), 45, nameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(nicTextField.getText(), 12, nicLabel, "Field Limit 12 Exceeded!");
            DataValidation.isValidMaximumLength(phoneTextField.getText(), 10, phoneLabel, "Field Limit 10 Exceeded!");
        }
        if (!(DataValidation.isValidNIC(nicTextField)
                && DataValidation.isValidPhoneNo(phoneTextField.getText()))) {
            DataValidation.isValidNIC(nicTextField, nicLabel, "Invalid NIC !!");
            DataValidation.isValidPhoneNo(phoneTextField.getText(), phoneLabel, "Invalid Contact Number!!");
        }

        if (UtilityMethod.checkDataAvailability(nicList, nicTextField.getText().toLowerCase())
            || UtilityMethod.checkDataAvailability(phoneList, phoneTextField.getText().toLowerCase())) {
            checkNICAvailability();
            checkPhoneAvailability();
        }
    }
    private void resetComponents() {
        addButton.setVisible(true);
    }

    @FXML
    private void checkNICAvailability() {
        DataValidation.checkNICAvailability(nicTextField, nicLabel, nicList);
    }

    @FXML
    private void checkPhoneAvailability() {
        DataValidation.checkPhoneAvailability(phoneTextField, phoneLabel, phoneList);
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

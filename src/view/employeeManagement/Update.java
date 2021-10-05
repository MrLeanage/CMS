package view.employeeManagement;

import bean.Designation;
import bean.Employee;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.DesignationService;
import service.EmployeeService;
import utility.dataHandler.DataValidation;
import utility.dataHandler.UtilityMethod;
import utility.navigation.Navigation;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Update implements Initializable {
    private Navigation navigation;
    @FXML
    private TextField searchTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private TextField nicTextField;

    @FXML
    private Label nicLabel;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private ChoiceBox<String> employeeTypeChoiceBox;

    public static Employee selectedEmployee = null;

    @FXML
    private AnchorPane detailAnchorPane;

    private static ArrayList<String> nicList = new ArrayList<>();

    private static ArrayList<String> phoneList  = new ArrayList<>();

    private ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameTextField.setText(selectedEmployee.geteName());
        nicTextField.setText(selectedEmployee.geteNIC());
        phoneTextField.setText("0"+selectedEmployee.geteContactNo());
        employeeTypeChoiceBox.setValue(selectedEmployee.geteDesignation());
        setInitData();
    }
    public void setInitData(){
        DesignationService designationService = new DesignationService();
        ObservableList<String> designationList = FXCollections.observableArrayList();
        for(Designation designation : designationService.loadAllDesignationData()){
            if(designation.getdStatus().equals("Active"))
                designationList.add(designation.getdName());
        }
        employeeTypeChoiceBox.setValue(selectedEmployee.geteDesignation());
        employeeTypeChoiceBox.setItems(designationList);

        EmployeeService employeeService = new EmployeeService();
        employeeObservableList = employeeService.loadAllEmployeeData();
        for(Employee employee : employeeObservableList){
            nicList.add(employee.geteNIC().toLowerCase());
            phoneList.add("0"+employee.geteContactNo().toString());
        }

    }
    @FXML
    public static void setData(Employee employee){
        Update.selectedEmployee = employee;
    }

    @FXML
    public static Employee getData(){
        return Update.selectedEmployee;
    }

    @FXML
    void updateEmployee(ActionEvent event) {
        clearLabels();
        if (employeeValidation()) {
            Employee employee = new Employee();
            EmployeeService employeeService = new EmployeeService();

            employee.seteID(selectedEmployee.geteID());
            employee.seteName(nameTextField.getText());
            employee.seteNIC(nicTextField.getText());
            employee.seteContactNo(Integer.valueOf(phoneTextField.getText()));
            employee.seteDesignation(employeeTypeChoiceBox.getValue());

            if (employeeService.updateEmployeeData(employee)) {
                AlertPopUp.updateSuccesfully("Employee");
                clearFields(event);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("employeeDetail.fxml"));
                try {
                    Parent root = (Parent) loader.load();
                    EmployeeDetailController employeeDetailController = loader.getController();

                    employeeDetailController.setState(true);
                    closeStage();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else
                AlertPopUp.updateFailed("Employee");
        } else
            employeeValidationMessage();
    }

    @FXML
    void clearFields(ActionEvent event) {
        nameTextField.setText(selectedEmployee.geteName());
        nicTextField.setText(selectedEmployee.geteNIC());
        phoneTextField.setText("0"+selectedEmployee.geteContactNo());
        employeeTypeChoiceBox.setValue(selectedEmployee.geteDesignation());
        clearLabels();
    }

    private void clearLabels() {
        nameLabel.setText("");
        nicLabel.setText("");
        phoneLabel.setText("");
    }

    private boolean employeeValidation() {

        boolean specificNICFieldValidation = true;
        boolean specificPhoneFieldValidation = true;

        for(Employee employee : employeeObservableList){
            if(employee.geteID().equals(selectedEmployee.geteID())){
                if((!("0"+employee.geteContactNo().toString()).equals(phoneTextField.getText()))
                && UtilityMethod.checkDataAvailability(phoneList, phoneTextField.getText().toLowerCase()))
                    specificPhoneFieldValidation = false;

                if(!(employee.geteNIC().equals(nicTextField.getText()))
                        && UtilityMethod.checkDataAvailability(nicList, nicTextField.getText().toLowerCase()))
                    specificNICFieldValidation = false;
            }


        }
        return DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(nicTextField.getText())
                && DataValidation.TextFieldNotEmpty(phoneTextField.getText())

                && DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(nicTextField.getText(), 12)
                && DataValidation.isValidMaximumLength(phoneTextField.getText(), 10)

                && DataValidation.isValidNIC(nicTextField)
                && DataValidation.isValidPhoneNo(phoneTextField.getText())

                && specificNICFieldValidation
                && specificPhoneFieldValidation;


    }

    private void employeeValidationMessage() {

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

        for(Employee employee : employeeObservableList){
            if(employee.geteID().equals(selectedEmployee.geteID())){
                if((!("0"+employee.geteContactNo().toString()).equals(phoneTextField.getText()))
                        && UtilityMethod.checkDataAvailability(phoneList, phoneTextField.getText().toLowerCase()))
                    checkPhoneAvailability();

                if(!(employee.geteNIC().equals(nicTextField.getText()))
                        && UtilityMethod.checkDataAvailability(nicList, nicTextField.getText().toLowerCase()))
                    checkNICAvailability();
            }


        }

    }

    @FXML
    private void checkNICAvailability() {
        for(Employee employee : employeeObservableList){
            //set nothing if
            if((employee.geteNIC().equals(nicTextField.getText()) && employee.geteID().equals(selectedEmployee.geteID()))){
                nicLabel.setText("");
            }else{
                DataValidation.checkNICAvailability(nicTextField, nicLabel, nicList);
            }
        }

    }

    @FXML
    private void checkPhoneAvailability() {

        for(Employee employee : employeeObservableList){
            if((("0"+employee.geteContactNo().toString()).equals(phoneTextField.getText()) && employee.geteID().equals(selectedEmployee.geteID()))){
                phoneLabel.setText("");
            }else{
                DataValidation.checkPhoneAvailability(phoneTextField, phoneLabel, phoneList);
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
}

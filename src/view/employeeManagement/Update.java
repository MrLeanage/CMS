package view.employeeManagement;

import bean.Employee;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.EmployeeService;
import utility.dataHandler.DataValidation;
import utility.navigation.Navigation;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
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
    private TextField designationTextField;

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
    private JFXButton updateButton;

    @FXML
    private JFXButton cancelButton;

    public static Employee selectedEmployee = null;
    @FXML
    private AnchorPane detailAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameTextField.setText(selectedEmployee.geteName());
        nicTextField.setText(selectedEmployee.geteNIC());
        phoneTextField.setText("0"+selectedEmployee.geteContactNo());
        designationTextField.setText(selectedEmployee.geteDesignation());
    }
    private void setFieldData(){

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
            employee.seteDesignation(designationTextField.getText());

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
        nameTextField.setText("");
        nicTextField.setText("");
        phoneTextField.setText("");
        designationTextField.setText("");
        clearLabels();
        selectedEmployee = null;
    }

    private void clearLabels() {
        nameLabel.setText("");
        nicLabel.setText("");
        phoneLabel.setText("");
        designationLabel.setText("");
    }

    private boolean employeeValidation() {


        return DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(nicTextField.getText())
                && DataValidation.TextFieldNotEmpty(phoneTextField.getText())
                && DataValidation.TextFieldNotEmpty(designationTextField.getText())

                && DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(nicTextField.getText(), 12)
                && DataValidation.isValidMaximumLength(phoneTextField.getText(), 10)
                && DataValidation.isValidMaximumLength(designationTextField.getText(), 30)

                && DataValidation.isValidNIC(nicTextField)
                && DataValidation.isValidPhoneNo(phoneTextField.getText());


    }

    private void employeeValidationMessage() {

        if (!(DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(nicTextField.getText())
                && DataValidation.TextFieldNotEmpty(phoneTextField.getText())
                && DataValidation.TextFieldNotEmpty(designationTextField.getText()))) {
            DataValidation.TextFieldNotEmpty(nameTextField.getText(), nameLabel, "Employee Name Required!");
            DataValidation.TextFieldNotEmpty(nicTextField.getText(), nicLabel, "Employee NIC Required!");
            DataValidation.TextFieldNotEmpty(phoneTextField.getText(), phoneLabel, "Employee Contact Number Required!");
            DataValidation.TextFieldNotEmpty(designationTextField.getText(), designationLabel, "Employee designation Required!");

        }
        if (!(DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(nicTextField.getText(), 12)
                && DataValidation.isValidMaximumLength(phoneTextField.getText(), 10)
                && DataValidation.isValidMaximumLength(designationTextField.getText(), 30))) {

            DataValidation.isValidMaximumLength(nameTextField.getText(), 45, nameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(nicTextField.getText(), 12, nicLabel, "Field Limit 12 Exceeded!");
            DataValidation.isValidMaximumLength(phoneTextField.getText(), 10, phoneLabel, "Field Limit 10 Exceeded!");
            DataValidation.isValidMaximumLength(designationTextField.getText(), 30, designationLabel, "Field Limit 30 Exceeded!");
        }
        if (!(DataValidation.isValidNIC(nicTextField)
                && DataValidation.isValidPhoneNo(phoneTextField.getText()))) {
            DataValidation.isValidNIC(nicTextField, nicLabel, "Invalid NIC !!");
            DataValidation.isValidPhoneNo(phoneTextField.getText(), phoneLabel, "Invalid Contact Number!!");
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

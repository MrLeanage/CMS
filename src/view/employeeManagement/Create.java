package view.employeeManagement;

import bean.Employee;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import service.EmployeeService;
import utility.dataHandler.DataValidation;
import utility.navigation.Navigation;
import utility.popUp.AlertPopUp;
import view.appHome.Main;

import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void storeEmp(ActionEvent event){
        clearLabels();
        if (employeeValidation()) {
            Employee employee = new Employee();
            EmployeeService employeeService = new EmployeeService();
            employee.seteName(nameTextField.getText());
            employee.seteNIC(nicTextField.getText());
            employee.seteContactNo(Integer.valueOf(phoneTextField.getText()));
            employee.seteDesignation(designationTextField.getText());

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
        designationTextField.setText("");
        resetComponents();
        clearLabels();
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
    private void resetComponents() {
        addButton.setVisible(true);
    }
}

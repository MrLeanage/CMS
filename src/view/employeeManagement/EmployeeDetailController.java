package view.employeeManagement;

import bean.Employee;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.EmployeeService;
import utility.dataHandler.PrintReport;
import utility.dataHandler.UtilityMethod;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class EmployeeDetailController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, String> IDColumn;

    @FXML
    private TableColumn<Employee, String> nameColumn;

    @FXML
    private TableColumn<Employee, String> NICColumn;

    @FXML
    private TableColumn<Employee, Integer> phoneColumn;

    @FXML
    private TableColumn<Employee, String> designationColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton printEmployeeButton;

    @FXML
    private Label employeeReportLabel;

    @FXML
    private Label employeeIDLabel;

    @FXML
    private TextField employeeIDTextBox;

    @FXML
    private Label employeeNameLabel;

    @FXML
    private TextField employeeNameTextBox;

    @FXML
    private ChoiceBox<String> employeeTypeChoiceBox;


    private static Employee selectedEmployee = null;

    private static Stage createStage;

    private static boolean refreshTable = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();

    }

    public void setInitData(){
        loadData();
        searchTable();
        refreshTable = false;
        clearFields();
    }

    private void loadData() {
        EmployeeService employeeService = new EmployeeService();
        ObservableList<Employee> employeeObservableList = employeeService.loadAllEmployeeData();
        Stage stage = new Stage();
        setCreateStage(stage);

        IDColumn.setCellValueFactory(new PropertyValueFactory<>("eID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("eName"));
        NICColumn.setCellValueFactory(new PropertyValueFactory<>("eNIC"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("eContactNo"));
        designationColumn.setCellValueFactory(new PropertyValueFactory<>("eDesignation"));

        employeeTable.setItems(null);
        employeeTable.setItems(employeeObservableList);
//        navigation
        searchTable();

        ObservableList<String> designationList = FXCollections.observableArrayList();
        for(Employee employee : employeeObservableList){
            designationList.add(employee.geteDesignation());
        }

        ObservableList<String> sortedDesignationList = UtilityMethod.removeStringDuplicates(designationList);
        sortedDesignationList.add("All");

        employeeTypeChoiceBox.setValue(sortedDesignationList.get(0));
        employeeTypeChoiceBox.setItems(sortedDesignationList);
    }
    private void setCreateStage(Stage stage) {
        EmployeeDetailController.createStage = stage;
    }
    static public Stage getPrimaryStage(){
        return EmployeeDetailController.createStage;
    }
    public void searchTable() {

        EmployeeService employeeService = new EmployeeService();

        SortedList<Employee> sortedData = employeeService.searchTable(searchTextField);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(employeeTable.comparatorProperty());
        //adding sorted and filtered data to the table
        employeeTable.setItems(sortedData);
    }


    @FXML
    private void addEmployee(ActionEvent event)throws IOException{
        Stage primaryStage= new Stage();
        Parent root =FXMLLoader.load(getClass().getResource("/view/employeeManagement/create.fxml"));
        Scene scene = new  Scene(root, 600, 500);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
    @FXML
    void updateEmployee(ActionEvent event) throws IOException {
        Stage updateStage= new Stage();
        Parent root =FXMLLoader.load(getClass().getResource("/view/employeeManagement/update.fxml"));
        Scene scene = new  Scene(root, 720, 350);
        updateStage.setScene(scene);
        Update.setData(EmployeeDetailController.selectedEmployee);
        updateStage.setResizable(false);
        updateStage.sizeToScene();
        updateStage.showAndWait();

        if(refreshTable)
            setInitData();
    }

    @FXML
    private void deleteEmployee(ActionEvent event) {
        if (selectedEmployee != null) {
            EmployeeService employeeService = new EmployeeService();
            Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Employee");
            if (action.get() == ButtonType.OK) {
                if (employeeService.deleteEmployeeData((selectedEmployee.geteID()))) {
                    AlertPopUp.deleteSuccesfull("Employee");
                    loadData();
                    searchTable();
                    clearFields(event);
                } else
                    AlertPopUp.deleteFailed("Employee");
            } else
                loadData();
        } else {
            AlertPopUp.selectRowToDelete("Employee");
        }
    }

    @FXML
    void setSelectedEmployeeDataToFields(MouseEvent event) {
        try {
            employeeIDLabel.setVisible(true);
            employeeNameLabel.setVisible(true);
            employeeIDTextBox.setVisible(true);
            employeeNameTextBox.setVisible(true);
            printEmployeeButton.setVisible(true);
            employeeReportLabel.setVisible(true);
            addButton.setVisible(false);
            updateButton.setVisible(true);
            selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

            employeeIDTextBox.setText(selectedEmployee.geteID());
            employeeNameTextBox.setText(selectedEmployee.geteName());
            Update.selectedEmployee= employeeTable.getSelectionModel().getSelectedItem();
        } catch (NullPointerException exception) {

        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        clearFields();
    }
    void clearFields() {

        employeeIDTextBox.setText("");
        employeeNameTextBox.setText("");

        resetComponents();
        selectedEmployee = null;
    }


//    private boolean employeeValidation() {
//
//
//        return DataValidation.TextFieldNotEmpty(nameTextField.getText())
//                && DataValidation.TextFieldNotEmpty(nicTextField.getText())
//                && DataValidation.TextFieldNotEmpty(phoneTextField.getText())
//                && DataValidation.TextFieldNotEmpty(designationTextField.getText())
//
//                && DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
//                && DataValidation.isValidMaximumLength(nicTextField.getText(), 12)
//                && DataValidation.isValidMaximumLength(phoneTextField.getText(), 10)
//                && DataValidation.isValidMaximumLength(designationTextField.getText(), 30)
//
//                && DataValidation.isValidNIC(nicTextField)
//                && DataValidation.isValidPhoneNo(phoneTextField.getText());
//
//
//    }
//
//    private void employeeValidationMessage() {
//
//        if (!(DataValidation.TextFieldNotEmpty(nameTextField.getText())
//                && DataValidation.TextFieldNotEmpty(nicTextField.getText())
//                && DataValidation.TextFieldNotEmpty(phoneTextField.getText())
//                && DataValidation.TextFieldNotEmpty(designationTextField.getText()))) {
//            DataValidation.TextFieldNotEmpty(nameTextField.getText(), nameLabel, "Employee Name Required!");
//            DataValidation.TextFieldNotEmpty(nicTextField.getText(), nicLabel, "Employee NIC Required!");
//            DataValidation.TextFieldNotEmpty(phoneTextField.getText(), phoneLabel, "Employee Contact Number Required!");
//            DataValidation.TextFieldNotEmpty(designationTextField.getText(), designationLabel, "Employee designation Required!");
//
//        }
//        if (!(DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
//                && DataValidation.isValidMaximumLength(nicTextField.getText(), 12)
//                && DataValidation.isValidMaximumLength(phoneTextField.getText(), 10)
//                && DataValidation.isValidMaximumLength(designationTextField.getText(), 30))) {
//
//            DataValidation.isValidMaximumLength(nameTextField.getText(), 45, nameLabel, "Field Limit 45 Exceeded!");
//            DataValidation.isValidMaximumLength(nicTextField.getText(), 12, nicLabel, "Field Limit 12 Exceeded!");
//            DataValidation.isValidMaximumLength(phoneTextField.getText(), 10, phoneLabel, "Field Limit 10 Exceeded!");
//            DataValidation.isValidMaximumLength(designationTextField.getText(), 30, designationLabel, "Field Limit 30 Exceeded!");
//        }
//        if (!(DataValidation.isValidNIC(nicTextField)
//                && DataValidation.isValidPhoneNo(phoneTextField.getText()))) {
//            DataValidation.isValidNIC(nicTextField, nicLabel, "Invalid NIC !!");
//            DataValidation.isValidPhoneNo(phoneTextField.getText(), phoneLabel, "Invalid Contact Number!!");
//        }
//    }
    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
        employeeIDLabel.setVisible(false);
        employeeNameLabel.setVisible(false);
        employeeIDTextBox.setVisible(false);
        employeeNameTextBox.setVisible(false);
        printEmployeeButton.setVisible(false);
        employeeReportLabel.setVisible(false);
    }
    public void setState(boolean refresh){
        refreshTable = refresh;
    }

    @FXML
    private void getSpecificEmployeeReport(ActionEvent event){
        if(selectedEmployee != null){
            PrintReport printReport = new PrintReport();
            printReport.printSpecificEmployeeReport(selectedEmployee);
        }
    }

    @FXML
    private void getEmployeeReport(ActionEvent event){
        PrintReport printReport = new PrintReport();
        printReport.printEmployeeReport(employeeTypeChoiceBox.getValue());
    }



}

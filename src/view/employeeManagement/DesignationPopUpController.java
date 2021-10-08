package view.employeeManagement;

import bean.Designation;
import bean.Employee;
import bean.User;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.DesignationService;
import service.EmployeeService;
import utility.dataHandler.DataValidation;
import utility.dataHandler.UtilityMethod;
import utility.popUp.AlertPopUp;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class DesignationPopUpController implements Initializable {

    @FXML
    private TableView<Designation> designationTable;

    @FXML
    private TableColumn<Designation, String> IDColumn;

    @FXML
    private TableColumn<Designation, String> nameColumn;

    @FXML
    private TableColumn<Designation, String> descriptionColumn;

    @FXML
    private TableColumn<Designation, String> statusColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label dIDLabel;

    @FXML
    private TextField dIDTextField;

    @FXML
    private TextField dTitleTextField;

    @FXML
    private TextArea dDescriptionTextArea;

    @FXML
    private ChoiceBox<String> dStatusChoiceBox;

    @FXML
    private Label dTitleValidationLabel;

    @FXML
    private Label dDescriptionValidationLabel;

    private static Designation selectedDesignation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInitData();
        ObservableList<String> choiceBoxList = FXCollections.observableArrayList("Active", "Disabled");
        dStatusChoiceBox.setValue(choiceBoxList.get(0));
        dStatusChoiceBox.setItems(choiceBoxList);
    }

    public void setInitData(){
        loadData();
        searchTable();
        clearFields();

    }

    public void loadData(){
        DesignationService designationService = new DesignationService();
        ObservableList<Designation> designationObservableList = designationService.loadAllDesignationData();

        IDColumn.setCellValueFactory(new PropertyValueFactory<>("dID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("dName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("dDescription"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("dStatus"));


        designationTable.setItems(null);
        designationTable.setItems(designationObservableList);
        searchTable();

    }

    public void searchTable() {

        DesignationService designationService = new DesignationService();

        SortedList<Designation> sortedData = designationService.searchTable(searchTextField);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(designationTable.comparatorProperty());
        //adding sorted and filtered data to the table
        designationTable.setItems(sortedData);
    }
    private boolean fieldValidation() {

        ObservableList<Designation> modelList = designationTable.getItems();
        ArrayList<String> designationList = new ArrayList<>();
        for (Designation designation : modelList) {
            designationList.add(designation.getdName().toLowerCase());
        }

        if (!(DataValidation.TextFieldNotEmpty(dTitleTextField.getText())
                && DataValidation.TextFieldNotEmpty(dDescriptionTextArea.getText())

                && DataValidation.isValidMaximumLength(dTitleTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(dDescriptionTextArea.getText(), 100))
                && !UtilityMethod.checkDataAvailability(designationList, dTitleTextField.getText().toLowerCase())
        ) {

            DataValidation.TextFieldNotEmpty(dTitleTextField.getText(), dTitleValidationLabel, "Designation Name Required!");
            DataValidation.TextFieldNotEmpty(dDescriptionTextArea.getText(), dDescriptionValidationLabel, "Designation Information Required!");

            DataValidation.isValidMaximumLength(dTitleTextField.getText(), 45, dTitleValidationLabel, "Field Character Limit 45 Exceeded");
            DataValidation.isValidMaximumLength(dDescriptionTextArea.getText(), 100, dDescriptionValidationLabel, "Field Character Limit 100 Exceeded");

            if (!UtilityMethod.checkDataAvailability(designationList, dTitleTextField.getText().toLowerCase())) {
                checkDesignationTitleAvailability();
            }

            return false;

        }else
            return true;
    }

    @FXML
    private void setSelectedData(MouseEvent event) {
        try {
            dIDLabel.setVisible(true);
            dIDTextField.setVisible(true);
            addButton.setVisible(false);
            updateButton.setVisible(true);
            selectedDesignation = designationTable.getSelectionModel().getSelectedItem();

            dIDTextField.setText(selectedDesignation.getdID());
            dTitleTextField.setText(selectedDesignation.getdName());
            dDescriptionTextArea.setText(selectedDesignation.getdDescription());
            dStatusChoiceBox.setValue(selectedDesignation.getdStatus());
        } catch (NullPointerException exception) {

        }
    }

    @FXML
    void addDesignation(ActionEvent event){
        clearLabels();
        if (fieldValidation()) {
            Designation designation = new Designation();
            DesignationService designationService = new DesignationService();

            designation.setdName(dTitleTextField.getText());
            designation.setdDescription(dDescriptionTextArea.getText());
            designation.setdStatus(dStatusChoiceBox.getValue());

            if (designationService.insertDesignationData(designation)) {
                AlertPopUp.insertSuccesfully("Designation");
                clearFields(event);
                setInitData();
            } else
                AlertPopUp.insertionFailed("Designation");
        }
    }

    @FXML
    void updateDesignation(ActionEvent event){
        clearLabels();
        boolean designationAvailability = false;
        if (selectedDesignation != null) {
            if (fieldValidation()) {
                EmployeeService employeeService = new EmployeeService();
                ObservableList<Employee> employeeObservableList = employeeService.loadAllEmployeeData();

                for(Employee employee : employeeObservableList){
                    if(employee.geteDesignation().equals(selectedDesignation.getdID()))
                        designationAvailability = true;
                }
                Designation designation = new Designation();
                DesignationService designationService = new DesignationService();

                designation.setdID(selectedDesignation.getdID());
                designation.setdName(dTitleTextField.getText());
                designation.setdDescription(dDescriptionTextArea.getText());
                designation.setdStatus(dStatusChoiceBox.getValue());

                if( dStatusChoiceBox.getValue().equals(dStatusChoiceBox.getValue())){

                    if (designationService.updateDesignationData(designation)) {
                        AlertPopUp.updateSuccesfully("Designation");
                        clearFields(event);
                        setInitData();
                    } else
                        AlertPopUp.updateFailed("Designation");
                }else{
                    if(!designationAvailability){
                        if (designationService.updateDesignationData(designation)) {
                            AlertPopUp.updateSuccesfully("Designation");
                            clearFields(event);
                            setInitData();
                        } else
                            AlertPopUp.updateFailed("Designation");
                    }else
                        AlertPopUp.failMessage("UpdateOrderController Failed", "You cannot UpdateOrderController Status of In Use Designation Title");
                }
            }
        }else {
            AlertPopUp.selectRowToUpdate("Designation");
        }

    }

    @FXML
    private void deleteDesignation(ActionEvent event) {
        if(selectedDesignation != null){
            if(selectedDesignation.getdStatus().equals("Disabled")){
                DesignationService designationService = new DesignationService();
                Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Designation");
                if (action.get() == ButtonType.OK) {
                    if (designationService.deleteDesignationData((selectedDesignation.getdID()))) {
                        AlertPopUp.deleteSuccesfull("Designation");
                        setInitData();
                    } else
                        AlertPopUp.deleteFailed("Designation");
                } else
                    loadData();
            }else{
                AlertPopUp.deleteFailed("You Cannot delete a Designation Title with Active Status");
            }
        } else {
            AlertPopUp.selectRowToDelete("Designation");
        }
    }

    @FXML
    private void checkDesignationTitleAvailability() {
        ObservableList<Designation> modelList = designationTable.getItems();
        ArrayList<String> designationList = new ArrayList<>();
        for (Designation designation : modelList) {
            designationList.add(designation.getdName().toLowerCase());
        }
        if (dTitleTextField.getText().isEmpty()) {
            dTitleValidationLabel.setStyle("-fx-text-fill: #ff0000 ");
            dTitleValidationLabel.setText("Title Name Cannot be empty");
        } else if (UtilityMethod.checkDataAvailability(designationList, dTitleTextField.getText().toLowerCase())) {
            dTitleValidationLabel.setStyle("-fx-text-fill: #ff0000 ");
            dTitleValidationLabel.setText("Title Name Already Available!!");
        } else {
            dTitleValidationLabel.setStyle("-fx-text-fill: #00B605 ");
            dTitleValidationLabel.setText("Title Name Available to use");

        }
    }

    private void clearLabels() {
        dTitleValidationLabel.setText("");
        dDescriptionValidationLabel.setText("");
    }

    @FXML
    void clearFields(ActionEvent event) {
        clearFields();
    }
    private void clearFields() {
        dTitleTextField.setText("");
        dDescriptionTextArea.setText("");
        resetComponents();
        selectedDesignation = null;
    }
    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
        dIDLabel.setVisible(false);
        dIDTextField.setVisible(false);
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

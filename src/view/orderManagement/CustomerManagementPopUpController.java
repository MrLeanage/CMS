package view.orderManagement;

import bean.*;
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
import service.CustomerService;
import service.OrderService;
import utility.dataHandler.DataValidation;
import utility.dataHandler.UtilityMethod;
import utility.popUp.AlertPopUp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerManagementPopUpController implements Initializable {

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, String> idColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> nicColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label cIDLabel;

    @FXML
    private TextField cIDTextField;

    @FXML
    private TextField cNameTextField;

    @FXML
    private TextArea cAddressTextArea;

    @FXML
    private Label cNameLabel;

    @FXML
    private Label cAddressLabel;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private TextField cNICTextField;

    @FXML
    private TextField cPhoneTextField;

    @FXML
    private Label cNICLabel;

    @FXML
    private Label cPhoneLabel;

    private static Customer selectedCustomer;

    private ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

    private String validationType = "add";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInitData();
    }

    public void setInitData(){
        loadData();
        searchTable();
        clearFields();

    }

    public void loadData(){
        CustomerService customerService = new CustomerService();
        customerObservableList = customerService.loadAllCustomerData();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("cID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("cName"));
        nicColumn.setCellValueFactory(new PropertyValueFactory<>("cNIC"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("cAddress"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("cPhone"));


        customerTable.setItems(null);
        customerTable.setItems(customerObservableList);
        searchTable();

    }

    public void searchTable() {

        CustomerService customerService = new CustomerService();

        SortedList<Customer> sortedData = customerService.searchTable(searchTextField);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(customerTable.comparatorProperty());
        //adding sorted and filtered data to the table
        customerTable.setItems(sortedData);
    }
    private boolean fieldValidation() {

        boolean generalFieldValidation = false;
        boolean specificFieldValidation = true;

        //validating Specific fields
        ArrayList<String> nicList = new ArrayList<>();
        for (Customer customer : customerObservableList) {
            nicList.add(customer.getcNIC().toLowerCase());
        }

        ArrayList<String> phoneList = new ArrayList<>();
        for (Customer customer : customerObservableList) {
            phoneList.add(customer.getcPhone().toLowerCase());
        }

        if(validationType.equals("add")){
            if(!UtilityMethod.checkDataAvailability(nicList, cNICTextField.getText().toLowerCase())
                    && !UtilityMethod.checkDataAvailability(phoneList, cPhoneTextField.getText().toLowerCase())){
                checkNICAvailability();
                checkPhoneAvailability();
            }else{
                specificFieldValidation = false;
            }
        }

        if(validationType.equals("update")){
            boolean specificPhoneFieldValidation = true;
            boolean specificNICFieldValidation = true;

            for(Customer customer : customerObservableList){
                if(customer.getcID().equals(selectedCustomer.getcID())){
                    if((!(customer.getcPhone()).equals(cPhoneTextField.getText()))
                            && UtilityMethod.checkDataAvailability(phoneList, cPhoneTextField.getText().toLowerCase()))
                        specificPhoneFieldValidation = false;

                    if(!(customer.getcNIC().equals(cNICTextField.getText()))
                            && UtilityMethod.checkDataAvailability(nicList, cNICTextField.getText().toLowerCase()))
                        specificNICFieldValidation = false;
                }
            }

            if(specificPhoneFieldValidation && specificNICFieldValidation){
                specificFieldValidation = true;
            }else{
                checkNICAvailability();
                checkPhoneAvailability();
            }
        }
        //validating general fields
        if (!(DataValidation.TextFieldNotEmpty(cNameTextField.getText())
                && DataValidation.TextFieldNotEmpty(cAddressTextArea.getText())
                && DataValidation.TextFieldNotEmpty(cNICTextField.getText())
                && DataValidation.TextFieldNotEmpty(cPhoneTextField.getText())

                && DataValidation.isValidMaximumLength(cNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(cAddressTextArea.getText(), 100)

                && DataValidation.isValidNIC(cNICTextField)
                && DataValidation.isValidPhoneNo(cPhoneTextField.getText()))){

            DataValidation.TextFieldNotEmpty(cNameTextField.getText(), cNameLabel, "Customer Name Required!");
            DataValidation.TextFieldNotEmpty(cAddressTextArea.getText(), cAddressLabel, "Customer Address Required!");
            DataValidation.TextFieldNotEmpty(cNICTextField.getText(), cNICLabel, "Customer NIC Required!");
            DataValidation.TextFieldNotEmpty(cPhoneTextField.getText(), cPhoneLabel, "Customer Contact Number Required!");

            DataValidation.isValidMaximumLength(cNameTextField.getText(), 45, cNameLabel, "Field Character Limit 45 Exceeded");
            DataValidation.isValidMaximumLength(cAddressTextArea.getText(), 100, cAddressLabel, "Field Character Limit 100 Exceeded");

            DataValidation.isValidNIC(cNICTextField, cNICLabel, "Invalid Customer NIC!");
            DataValidation.isValidPhoneNo(cPhoneTextField.getText(), cPhoneLabel, "Invalid Customer Contact Number!");
        }else
            generalFieldValidation =  true;

        //returning true if both validations are true else false
        return (generalFieldValidation && specificFieldValidation);
    }

    @FXML
    private void setSelectedData(MouseEvent event) {
        try {
            validationType = "update";

            cIDLabel.setVisible(true);
            cIDTextField.setVisible(true);
            addButton.setVisible(false);
            updateButton.setVisible(true);
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

            cIDTextField.setText(selectedCustomer.getcID());
            cNameTextField.setText(selectedCustomer.getcName());
            cNICTextField.setText(selectedCustomer.getcNIC());
            cAddressTextArea.setText(selectedCustomer.getcAddress());
            cPhoneTextField.setText(selectedCustomer.getcPhone());
        } catch (NullPointerException exception) {

        }
    }

    @FXML
    void addCustomer(ActionEvent event){
        clearLabels();
        if (fieldValidation()) {
            Customer customer = new Customer();
            CustomerService customerService = new CustomerService();

            customer.setcName(cNameTextField.getText());
            customer.setcNIC(cNICTextField.getText());
            customer.setcAddress(cAddressTextArea.getText());
            customer.setcPhone(cPhoneTextField.getText());

            if (customerService.insertCustomerData(customer)) {
                AlertPopUp.insertSuccesfully("Customer");
                clearFields(event);
                setInitData();
            } else
                AlertPopUp.insertionFailed("Customer");
        }
    }

    @FXML
    void updateCustomer(ActionEvent event){
        clearLabels();
        boolean designationAvailability = false;
        if (selectedCustomer != null) {
            if (fieldValidation()) {

                Customer customer = new Customer();
                CustomerService customerService = new CustomerService();

                customer.setcID(cIDTextField.getText());
                customer.setcName(cNameTextField.getText());
                customer.setcNIC(cNICTextField.getText());
                customer.setcAddress(cAddressTextArea.getText());
                customer.setcPhone(cPhoneTextField.getText());

                if (customerService.updateCustomerData(customer)) {
                    AlertPopUp.updateSuccesfully("Customer");
                    clearFields(event);
                    setInitData();
                } else
                    AlertPopUp.updateFailed("Customer");
            }
        }else {
            AlertPopUp.selectRowToUpdate("Customer");
        }

    }

    @FXML
    private void deleteCustomer(ActionEvent event) {
        if(selectedCustomer != null){
            OrderService orderService = new OrderService();
            boolean customerStatus = true;
            for(Order order : orderService.loadAllOrderData()){
                if(order.getoCID().equals(selectedCustomer.getcID()))
                    customerStatus = false;
            }
            if(customerStatus){
                Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Customer");
                if (action.get() == ButtonType.OK) {
                    CustomerService customerService = new CustomerService();
                    if (customerService.deleteCustomerData((selectedCustomer.getcID()))) {
                        AlertPopUp.deleteSuccesfull("Customer");
                        setInitData();
                    } else
                        AlertPopUp.deleteFailed("Customer");
                } else
                    loadData();
            }else{
                AlertPopUp.deleteFailed("You Cannot delete Customer info which is already used in an Order");
            }
        } else {
            AlertPopUp.selectRowToDelete("Customer");
        }
    }


    @FXML
    private void checkNICAvailability(){
        ArrayList<String> nicList = new ArrayList<>();
        for (Customer customer : customerObservableList) {
            nicList.add(customer.getcNIC().toLowerCase());
        }
        if(validationType.equals("add")){
            DataValidation.checkNICAvailability(cNICTextField, cNICLabel, nicList);
        }else{
            for(Customer customer : customerObservableList){
                if(((customer.getcNIC().toLowerCase()).equals(cNICTextField.getText().toLowerCase()) && customer.getcID().equals(selectedCustomer.getcID()))){
                    cNICLabel.setText("");
                }else{
                    DataValidation.checkNICAvailability(cNICTextField, cNICLabel, nicList);
                }
            }
        }
    }

    @FXML
    private void checkPhoneAvailability() {

        ArrayList<String> phoneList = new ArrayList<>();
        for (Customer customer : customerObservableList) {
            phoneList.add(customer.getcPhone().toLowerCase());
        }
        if (validationType.equals("add")) {
            DataValidation.checkPhoneAvailability(cPhoneTextField, cPhoneLabel, phoneList);
        }else{
            for(Customer customer : customerObservableList){
                if(((customer.getcPhone()).equals(cPhoneTextField.getText()) && customer.getcID().equals(selectedCustomer.getcID()))){
                    cPhoneLabel.setText("");
                }else{
                    DataValidation.checkPhoneAvailability(cPhoneTextField, cPhoneLabel, phoneList);
                }
            }
        }

    }



    private void clearLabels() {
        cNameLabel.setText("");
        cAddressLabel.setText("");
        cNICLabel.setText("");
        cPhoneLabel.setText("");
    }

    @FXML
    void clearFields(ActionEvent event) {
        clearFields();
    }
    private void clearFields() {
        cIDTextField.setText("");
        cNameTextField.setText("");
        cAddressTextArea.setText("");
        cNICTextField.setText("");
        cPhoneTextField.setText("");
        resetComponents();
        clearLabels();
        validationType = "add";
        selectedCustomer = null;
    }
    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
        cIDTextField.setVisible(false);
        cIDLabel.setVisible(false);
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

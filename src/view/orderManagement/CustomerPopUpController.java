package view.orderManagement;

import bean.Customer;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.CustomerService;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerPopUpController implements Initializable {
    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, String> idColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> nicColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label cIDLabel;

    @FXML
    private TextField cIDTextField;

    @FXML
    private TextField cNameTextField;

    @FXML
    private TextField cNICTextField;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private TextField cPhoneTextField;

    private static Customer selectedCustomer;

    private static String actionType = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        searchTable();
    }

    public void loadData(){
        CustomerService customerService = new CustomerService();
        ObservableList<Customer> customerObservableList = customerService.loadAllCustomerData();

        for(Customer customer : customerObservableList){
            System.out.println("Info :" +customer.getcNIC());
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("cID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("cName"));
        nicColumn.setCellValueFactory(new PropertyValueFactory<>("cNIC"));
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

    @FXML
    private void setSelectedData(MouseEvent event) {
        try {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

            cIDTextField.setText(selectedCustomer.getcID());
            cNameTextField.setText(selectedCustomer.getcName());
            cNICTextField.setText(selectedCustomer.getcNIC());
            cPhoneTextField.setText(selectedCustomer.getcPhone());
        } catch (NullPointerException exception) {

        }
    }

    @FXML
    void addSelectedCustomer(ActionEvent event) {
        if (selectedCustomer != null) {
            if(actionType.equals("add")){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("addOrder.fxml"));
                try {
                    Parent root = (Parent) loader.load();
                    AddOrderController addOrderController = loader.getController();

                    addOrderController.setCustomer(selectedCustomer);
                    selectedCustomer = null;
                    closeStage();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("updateOrder.fxml"));
                try {
                    Parent root = (Parent) loader.load();
                    UpdateOrderController updateOrderController = loader.getController();

                    updateOrderController.setCustomer(selectedCustomer);
                    selectedCustomer = null;
                    closeStage();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

        } else
            AlertPopUp.selectRow("Customer to Add");
    }

    public static void setData(String type){
        actionType = type;
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

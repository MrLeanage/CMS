package view.orderManagement;


import bean.Customer;
import bean.Order;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.CustomerService;
import service.OrderService;
import utility.dataHandler.DataValidation;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateOrderController implements Initializable {

    @FXML
    private TextField nicTextField;

    @FXML
    private Label customerValidationLabel;

    @FXML
    private TextArea noteTextArea;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private Label noteLabel;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private TextField dateTextField;

    private static Order selectedOrder = null;

    private static Customer selectedCustomer = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> statusList = FXCollections.observableArrayList("Pending", "Processing", "Delivered");

        CustomerService customerService = new CustomerService();
        selectedCustomer = customerService.loadSpecificCustomer(selectedOrder.getoCID());

        nicTextField.setText(selectedCustomer.getcNIC());
        customerNameTextField.setText(selectedOrder.getoCName());
        noteTextArea.setText(selectedOrder.getoNotes());
        dateTextField.setText(selectedOrder.getoDate());
        statusChoiceBox.setValue(selectedOrder.getoStatus());
        statusChoiceBox.setItems(statusList);
    }

    @FXML
    public static void setData(Order order){
        UpdateOrderController.selectedOrder = order;
    }

    @FXML
    public static Order getData(){
        return UpdateOrderController.selectedOrder;
    }

    @FXML
    void updateOrder(ActionEvent event) {
        clearLabels();
        if (orderValidation()) {
            Order order = new Order();
            OrderService orderService = new OrderService();

            order.setoID(selectedOrder.getoID());
            order.setoCID(selectedCustomer.getcID());
            order.setoNotes(noteTextArea.getText());
            order.setoDate(dateTextField.getText());
            order.setoStatus(statusChoiceBox.getValue());

            if (orderService.updateOrderData(order)) {
                AlertPopUp.updateSuccesfully("Order");
                clearFields(event);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("orderDetail.fxml"));
                try {
                    Parent root = (Parent) loader.load();
                    OrderDetailController orderDetailController = loader.getController();

                    orderDetailController.setState(true);
                    closeStage();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else
                AlertPopUp.updateFailed("Order");
        }
    }
    @FXML
    void clearFields(ActionEvent event) {
        nicTextField.setText(selectedCustomer.getcNIC());
        customerNameTextField.setText(selectedOrder.getoCName());
        noteTextArea.setText(selectedOrder.getoNotes());
        dateTextField.setText(selectedOrder.getoDate());
        statusChoiceBox.setValue(selectedOrder.getoStatus());
        clearLabels();
    }

    private void clearLabels() {
        customerValidationLabel.setText("");
        noteLabel.setText("");
    }

    private boolean orderValidation() {
        if(!(DataValidation.TextFieldNotEmpty(nicTextField.getText())
                && DataValidation.isValidMaximumLength(noteTextArea.getText(), 400))){
            DataValidation.TextFieldNotEmpty(nicTextField.getText(), customerValidationLabel, "Please Select a Customer");

            DataValidation.isValidMaximumLength(noteTextArea.getText(), 400, noteLabel, "Field Limit 400 Exceeded!");
            return false;
        }else{
            return true;
        }
    }

    @FXML
    private void browseCustomer(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader();
        CustomerPopUpController.setData("update");
        loader.setLocation(getClass().getResource("customerPopUp.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(CustomerPopUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.setResizable(false);
        stage.sizeToScene();

        stage.showAndWait();

        if(selectedCustomer != null){
            nicTextField.setText(selectedCustomer.getcNIC());
            customerNameTextField.setText(selectedCustomer.getcName());
        }

    }

    public void setCustomer(Customer customer){
        selectedCustomer = customer;
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

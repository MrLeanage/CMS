package view.orderManagement;


import bean.Order;
import bean.Product;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.OrderService;
import utility.dataHandler.DataValidation;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Create implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField productIDTextField;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label productIDLabel;

    @FXML
    private TextArea noteTextArea;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label noteLabel;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    public static Product selectedProduct = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        SpinnerValueFactory<Integer> quantityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        quantitySpinner.setValueFactory(quantityValueFactory);

        ObservableList<String> statusList = FXCollections.observableArrayList("Pending", "Processing", "Delivered");
        statusChoiceBox.setValue("Pending");
        statusChoiceBox.setItems(statusList);


    }
    @FXML
    void addOrder(ActionEvent event) {

        clearLabels();
        if (orderValidation()) {
            Order order = new Order();
            OrderService orderService = new OrderService();
            order.setoProductID(productIDTextField.getText());
            order.setoCustomerName(customerNameTextField.getText());
            order.setoQuantity(quantitySpinner.getValue());
            order.setoNotes(noteTextArea.getText());
            order.setoStatus(statusChoiceBox.getValue());


            if (orderService.insertOrderData(order)) {
                AlertPopUp.insertSuccesfully("Order");
                clearFields(event);
            } else
                AlertPopUp.insertionFailed("Order");

        } else
            orderValidationMessage();
    }
    @FXML
    void clearFields(ActionEvent event) {
        productIDTextField.setText("");
        customerNameTextField.setText("");
        noteTextArea.setText("");
        resetComponents();
        clearLabels();
        selectedProduct = null;
    }

    private void clearLabels() {
        productIDLabel.setText("");
        customerNameLabel.setText("");
        noteLabel.setText("");
    }

    private boolean orderValidation() {
        return DataValidation.TextFieldNotEmpty(productIDTextField.getText())
                && DataValidation.TextFieldNotEmpty(customerNameTextField.getText())

                && DataValidation.isValidMaximumLength(customerNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(noteTextArea.getText(), 400);
    }

    private void orderValidationMessage() {

        if (!(DataValidation.TextFieldNotEmpty(productIDTextField.getText())
                && DataValidation.TextFieldNotEmpty(customerNameTextField.getText()))) {
            DataValidation.TextFieldNotEmpty(productIDTextField.getText(), productIDLabel, "Please Select a Menu!");
            DataValidation.TextFieldNotEmpty(customerNameTextField.getText(), customerNameLabel, "Customer Name Required!");

        }
        if (!(DataValidation.isValidMaximumLength(customerNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(noteTextArea.getText(), 400))) {

            DataValidation.isValidMaximumLength(customerNameTextField.getText(), 45, customerNameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(noteTextArea.getText(), 400, noteLabel, "Field Limit 400 Exceeded!");
        }
    }
    private void resetComponents() {
        quantitySpinner.getValueFactory().setValue(1);
        addButton.setVisible(true);
        updateButton.setVisible(false);
    }

    public void setProduct(Product product){
        selectedProduct = product;
        productIDTextField.setText(selectedProduct.getpID());
    }

    @FXML
    private void browseProduct(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("productPopUpAdd.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(ProductPopUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.setResizable(false);
        stage.sizeToScene();

        stage.showAndWait();

        if(selectedProduct != null)
            productIDTextField.setText(selectedProduct.getpID());
    }

}

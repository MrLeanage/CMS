package view.orderManagement;

import bean.Menu;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.OrderService;
import utility.dataHandler.DataValidation;
import utility.popUp.AlertPopUp;
import view.supplierManagement.SupplerDetailController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Update implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField menuIDTextField;

    @FXML
    private Label menuIDLabel;

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
    private JFXButton cancelButton;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    public static Menu selectedMenu = null;

    private static Order selectedOrder = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> quantityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        quantitySpinner.setValueFactory(quantityValueFactory);
        ObservableList<String> statusList = FXCollections.observableArrayList("Pending", "Processing", "Delivered");
        statusChoiceBox.setValue("Pending");
        statusChoiceBox.setItems(statusList);
        menuIDTextField.setText(selectedOrder.getoMenuID());
        customerNameTextField.setText(selectedOrder.getoCustomerName());
        quantitySpinner.getValueFactory().setValue(selectedOrder.getoQuantity());
        noteTextArea.setText(selectedOrder.getoNotes());
        statusChoiceBox.setValue(selectedOrder.getoStatus());
    }

    @FXML
    public static void setData(Order order){
        Update.selectedOrder = order;
    }

    @FXML
    public static Order getData(){
        return Update.selectedOrder;
    }

    @FXML
    void updateOrder(ActionEvent event) {
        clearLabels();
        if (orderValidation()) {
            Order order = new Order();
            OrderService orderService = new OrderService();

            order.setoID(selectedOrder.getoID());
            order.setoMenuID(menuIDTextField.getText());
            order.setoCustomerName(customerNameTextField.getText());
            order.setoQuantity(quantitySpinner.getValue());
            order.setoNotes(noteTextArea.getText());
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
        } else
            orderValidationMessage();
    }
    @FXML
    void clearFields(ActionEvent event) {
        menuIDTextField.setText("");
        customerNameTextField.setText("");
        noteTextArea.setText("");
        clearLabels();
        selectedOrder = null;
    }

    private void clearLabels() {
        menuIDLabel.setText("");
        customerNameLabel.setText("");
        noteLabel.setText("");
    }

    private boolean orderValidation() {
        return DataValidation.TextFieldNotEmpty(menuIDTextField.getText())
                && DataValidation.TextFieldNotEmpty(customerNameTextField.getText())

                && DataValidation.isValidMaximumLength(customerNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(noteTextArea.getText(), 400);
    }

    private void orderValidationMessage() {

        if (!(DataValidation.TextFieldNotEmpty(menuIDTextField.getText())
                && DataValidation.TextFieldNotEmpty(customerNameTextField.getText()))) {
            DataValidation.TextFieldNotEmpty(menuIDTextField.getText(), menuIDLabel, "Please Select a Menu!");
            DataValidation.TextFieldNotEmpty(customerNameTextField.getText(), customerNameLabel, "Customer Name Required!");

        }
        if (!(DataValidation.isValidMaximumLength(customerNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(noteTextArea.getText(), 400))) {

            DataValidation.isValidMaximumLength(customerNameTextField.getText(), 45, customerNameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(noteTextArea.getText(), 400, noteLabel, "Field Limit 400 Exceeded!");
        }
    }

    @FXML
    private void browseMenu(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("menuPopUp.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(MenuPopUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.setResizable(false);
        stage.sizeToScene();

        stage.showAndWait();

        if(selectedMenu != null)
            menuIDTextField.setText(selectedMenu.getmID());
    }

    public void setMenu(Menu menu){
        selectedMenu = menu;
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

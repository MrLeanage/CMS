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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.MenuService;
import service.OrderService;
import utility.dataHandler.DataValidation;
import utility.navigation.Navigation;
import utility.popUp.AlertPopUp;

import java.io.File;
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
    private TextField menuIDTextField;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label menuIDLabel;

    @FXML
    private TextArea noteTextArea;

    @FXML
    private Label quantityLabel;

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

    public static Menu selectedMenu = null;

    private static Order selectedOrder = null;

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
            order.setoMenuID(menuIDTextField.getText());
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
        menuIDTextField.setText("");
        customerNameTextField.setText("");
        noteTextArea.setText("");
        resetComponents();
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
    private void resetComponents() {
        quantitySpinner.getValueFactory().setValue(1);
        addButton.setVisible(true);
        updateButton.setVisible(false);
    }

    public void setMenu(Menu menu){
        selectedMenu = menu;
        menuIDTextField.setText(selectedMenu.getmID());
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

}

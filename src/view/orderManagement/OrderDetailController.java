package view.orderManagement;

import bean.Employee;
import bean.Menu;
import bean.Order;
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
import service.OrderService;
import utility.dataHandler.DataValidation;
import utility.dataHandler.PrintReport;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDetailController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, String> IDColumn;

    @FXML
    private TableColumn<Order, String> customerNameColumn;

    @FXML
    private TableColumn<Order, String> menuNameColumn;

    @FXML
    private TableColumn<Order, String> notesColumn;

    @FXML
    private TableColumn<Order, Integer> quantityColumn;

    @FXML
    private TableColumn<Order, String> statusColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField menuIDTextField;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton addButton;
    @FXML
    private Label orderIDLabel;

    @FXML
    private TextField orderIDTextBox;

    @FXML
    private Label customerNameLabel;

    @FXML
    private TextField customerNameTextBox;

    @FXML
    private JFXButton printQuotationButton;

    @FXML
    private Label orderQuotationLabel;

    @FXML
    private ChoiceBox<String> deliveryStatusChoiceBox;



    public static Menu selectedMenu = null;

    private static Order selectedOrder = null;

    private static boolean refreshTable = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        ObservableList<String> typeList = FXCollections.observableArrayList("Pending", "Processing", "Delivered");
        deliveryStatusChoiceBox.setValue(typeList.get(0));
        deliveryStatusChoiceBox.setItems(typeList);
    }

    public void setInitData(){
        loadData();
        searchTable();
        refreshTable = false;
        clearFields();
    }

    private void loadData() {
        OrderService orderService = new OrderService();
        ObservableList<Order> orderObservableList = orderService.loadAllOrderData();

        IDColumn.setCellValueFactory(new PropertyValueFactory<>("oID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("oCustomerName"));
        menuNameColumn.setCellValueFactory(new PropertyValueFactory<>("oMenuName"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("oNotes"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("oQuantity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("oStatus"));

        orderTable.setItems(null);
        orderTable.setItems(orderObservableList);

        searchTable();
    }

    public void searchTable() {

        OrderService orderService = new OrderService();

        SortedList<Order> sortedData = orderService.searchTable(searchTextField);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(orderTable.comparatorProperty());
        //adding sorted and filtered data to the table
        orderTable.setItems(sortedData);
    }

    @FXML
    void addOrder(ActionEvent event) throws IOException {
        Stage primaryStage= new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/orderManagement/create.fxml"));
        Scene scene = new  Scene(root, 650, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    @FXML
    void updateOrder(ActionEvent event) throws IOException {
        Stage updateStage= new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/orderManagement/update.fxml"));
        Scene scene = new  Scene(root, 720, 350);
        updateStage.setScene(scene);
        updateStage.setResizable(false);
        updateStage.sizeToScene();
        updateStage.showAndWait();
        if(refreshTable)
            setInitData();
    }

    @FXML
    private void deleteOrder(ActionEvent event) {
        if (selectedOrder != null) {
            OrderService orderService = new OrderService();
            Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Order");
            if (action.get() == ButtonType.OK) {
                if (orderService.deleteOrderData((selectedOrder.getoID()))) {
                    AlertPopUp.deleteSuccesfull("Order");
                    loadData();
                    searchTable();
                    clearFields(event);
                } else
                    AlertPopUp.deleteFailed("Order");
            } else
                loadData();
        } else {
            AlertPopUp.selectRowToDelete("Order");
        }
    }

    @FXML
    void setSelectedOrderDataToFields(MouseEvent event) {
        try {
            addButton.setVisible(false);
            updateButton.setVisible(true);
            orderIDLabel.setVisible(true);
            orderIDTextBox.setVisible(true);
            customerNameTextBox.setVisible(true);
            customerNameLabel.setVisible(true);
            printQuotationButton.setVisible(true);
            orderQuotationLabel.setVisible(true);

            selectedOrder = orderTable.getSelectionModel().getSelectedItem();

            orderIDTextBox.setText(selectedOrder.getoID());
            customerNameTextBox.setText(selectedOrder.getoCustomerName());

            Update.setData(selectedOrder);//fine
        } catch (NullPointerException exception) {

        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        clearFields();
    }
    void clearFields() {
        customerNameTextBox.setText("");
        orderIDTextBox.setText("");
        resetComponents();
        selectedOrder = null;
    }
    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
        orderIDTextBox.setVisible(false);
        customerNameTextBox.setVisible(false);
        printQuotationButton.setVisible(false);
        orderQuotationLabel.setVisible(false);
        customerNameLabel.setVisible(false);
        orderIDLabel.setVisible(false);
    }


    public void setMenu(Menu menu){
        selectedMenu = menu;
        menuIDTextField.setText(selectedMenu.getmID());
    }

    public void setState(boolean refresh){
        refreshTable = refresh;
    }

    @FXML
    private void getOrderQuotation(ActionEvent event){
        if(selectedOrder != null){
            PrintReport printReport = new PrintReport();
            printReport.printOrderQuotation(selectedOrder);
        }
    }

    @FXML
    private void getOrderDeliveryReport(ActionEvent event){
        PrintReport printReport = new PrintReport();
        printReport.printOrderDeliveryReport(deliveryStatusChoiceBox.getValue());
    }
}

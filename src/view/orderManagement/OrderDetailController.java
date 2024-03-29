package view.orderManagement;

import bean.Order;
import bean.Product;
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
import service.OrderService;
import utility.dataHandler.PrintReport;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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
    private TableColumn<Order, String> totalPriceColumn;

    @FXML
    private TableColumn<Order, String> notesColumn;

    @FXML
    private TableColumn<Order, String> dateColumn;

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



    public static Product selectedProduct = null;

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

        /**
         * private String oID = null;
         *     private String oCID = null;
         *     private String oCName = null;
         *     private String oNotes = null;
         *     private LinkedList<CartItem> oItemList = new LinkedList<>();
         *     private String oDate = null;
         *     private String oStatus = null;
         */
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("oID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("oCName"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("oNotes"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("oDate"));
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
        Parent root = FXMLLoader.load(getClass().getResource("/view/orderManagement/addOrder.fxml"));
        Scene scene = new  Scene(root, 720, 500);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.showAndWait();

        loadData();
    }

    @FXML
    void updateOrder(ActionEvent event) throws IOException {
        if(selectedOrder != null){
            Stage updateStage= new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/view/orderManagement/updateOrder.fxml"));
            Scene scene = new  Scene(root, 720, 350);
            updateStage.setScene(scene);
            updateStage.setResizable(false);
            updateStage.sizeToScene();
            updateStage.showAndWait();
            if(refreshTable)
                setInitData();
        }else{
            AlertPopUp.selectRowToUpdate("Order");
        }

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
            customerNameTextBox.setText(selectedOrder.getoCName());

            UpdateOrderController.setData(selectedOrder);//fine
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


    public void setMenu(Product product){
        selectedProduct = product;
        menuIDTextField.setText(selectedProduct.getpID());
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

    @FXML
    void manageCustomer(ActionEvent event) throws IOException{
        Stage primaryStage= new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/orderManagement/customerManagementPopUp.fxml"));
        Scene scene = new  Scene(root, 720, 500);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.showAndWait();

        setInitData();
    }
}

package view.orderManagement;


import bean.Product;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ProductService;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class ProductPopUpController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> IDColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> infoColumn;

    @FXML
    private TableColumn<Product, Integer> qtyColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, String> availabilityColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private TextField availabilityTextField;

    @FXML
    private JFXButton cancelButton;

    private static Product selectedProduct;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();

    }

    private void loadData() {
        ProductService productService = new ProductService();
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        for(Product product : productService.loadAllProductData()){
            if(product.getpAvailability().equals("Available"))
                productObservableList.add(product);
        }


        IDColumn.setCellValueFactory(new PropertyValueFactory<>("pID"));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("pName"));
        infoColumn.setCellValueFactory(new PropertyValueFactory<>("pInfo"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("pQuantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("pPrice"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("pAvailability"));

        priceColumn.setCellFactory(tc -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty) ;
                if (empty) {
                    setText(null);
                } else {
                    setText("Rs "+ new DecimalFormat("#####.00").format(value));
                    setAlignment(Pos.valueOf("TOP_RIGHT"));
                }
            }
        });
        productTable.setItems(null);
        productTable.setItems(productObservableList);
        searchTable();
    }

    public void searchTable() {
        ProductService productService = new ProductService();

        SortedList<Product> sortedData = productService.searchTable(searchTextField) ;

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());
        //adding sorted and filtered data to the table
        ObservableList<Product> list = FXCollections.observableArrayList();
        for(Product product :sortedData){
            if(product.getpAvailability().equals("Available"))
                list.add(product);
        }
        productTable.setItems(list);
    }

    @FXML
    void addSelectedMenu(ActionEvent event) {
        if (selectedProduct != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateOrder.fxml"));
            try {
                Parent root = (Parent) loader.load();
                UpdateOrderController updateOrderController = loader.getController();

               // updateOrderController.setProduct(selectedProduct);
                selectedProduct = null;
                closeStage();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else
            AlertPopUp.selectRow("Product to Add");
    } 
    @FXML
    void setSelectedMenuDataToFields(MouseEvent event) {
        try {

            selectedProduct = productTable.getSelectionModel().getSelectedItem();
            nameTextField.setText(selectedProduct.getpName());
            infoTextArea.setText(selectedProduct.getpInfo());
            priceTextField.setText(selectedProduct.getpPrice().toString());
            availabilityTextField.setText(selectedProduct.getpAvailability());
//            menuImageView.setImage(selectedProduct.getpQuantity());
        } catch (NullPointerException exception) {

        }
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

package view.productManagement;

import bean.Product;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import service.ProductService;
import utility.dataHandler.DataValidation;
import utility.navigation.Navigation;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class ProductDetailController implements Initializable {
    private Navigation navigation;

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
    private Label nameLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private TextField priceTextField;

    @FXML
    private Label priceLabel;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private ChoiceBox<String> availabilityChoiceBox;

    @FXML
    private Spinner<Integer> qtySpinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadData();
        ObservableList<String> choiceBoxList = FXCollections.observableArrayList("Available", "Not Available");
        availabilityChoiceBox.setValue("Available");
        availabilityChoiceBox.setItems(choiceBoxList);

        SpinnerValueFactory<Integer> quantityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        qtySpinner.setValueFactory(quantityValueFactory);
    }

    private void loadData() {
        ProductService productService = new ProductService();
        ObservableList<Product> menuObservableList = productService.loadAllProductData();

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
        productTable.setItems(menuObservableList);
        searchTable();
    }

    public void searchTable() {
        ProductService productService = new ProductService();

        SortedList<Product> sortedData = productService.searchTable(searchTextField);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());
        //adding sorted and filtered data to the table
        productTable.setItems(sortedData);
    }

    @FXML
    void addMenu(ActionEvent event){
        clearLabels();
        if (productValidation()) {
            Product product = new Product();
            ProductService productService = new ProductService();


            product.setpName(nameTextField.getText());
            product.setpInfo(infoTextArea.getText());
            product.setpPrice(Double.valueOf(priceTextField.getText()));
            product.setpAvailability(availabilityChoiceBox.getValue());
            product.setpQuantity(qtySpinner.getValue());


            if (productService.insertProductData(product)) {
                AlertPopUp.insertSuccesfully("Product");
                loadData();
                searchTable();
                clearFields(event);
            } else
                AlertPopUp.insertionFailed("Product");

        }
    }

    private boolean productValidation() {

        if(!(DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(infoTextArea.getText())
                && DataValidation.TextFieldNotEmpty(priceTextField.getText())

                && DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(infoTextArea.getText(), 400)

                && DataValidation.isValidNumberFormat(priceTextField.getText()))){

            DataValidation.TextFieldNotEmpty(nameTextField.getText(), nameLabel, "Product Name Required!");
            DataValidation.TextFieldNotEmpty(infoTextArea.getText(), infoLabel, "Product Information Required!");
            DataValidation.TextFieldNotEmpty(priceTextField.getText(), priceLabel, "Product Price Required!");

            DataValidation.isValidMaximumLength(nameTextField.getText(), 45, nameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(infoTextArea.getText(), 400, infoLabel, "Field Limit 400 Exceeded!");

            DataValidation.isValidNumberFormat(priceTextField.getText(), priceLabel, "Invalid Price Amount!");

            return  false;
        }else{
            return true;
        }
    }

    @FXML
    void updateMenu(ActionEvent event) throws IOException {

    }

    @FXML
    private void deleteMenu(ActionEvent event) {

    }

    @FXML
    void setSelectedMenuDataToFields(MouseEvent event) {

    }

    @FXML
    void clearFields(ActionEvent event) {
        nameTextField.setText("");
        infoTextArea.setText("");
        priceTextField.setText("");
        resetComponents();
        clearLabels();
    }

    private void clearLabels() {
        nameLabel.setText("");
        infoLabel.setText("");
        priceLabel.setText("");
    }

    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
        qtySpinner.getValueFactory().setValue(1);
    }


}

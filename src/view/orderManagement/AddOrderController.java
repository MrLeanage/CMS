package view.orderManagement;


import bean.CartItem;
import bean.Customer;
import bean.Order;
import bean.Product;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.util.Callback;
import service.OrderService;
import service.ProductService;
import utility.dataHandler.DataValidation;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddOrderController implements Initializable {

    @FXML
    private TextArea cNoteTextArea;

    @FXML
    private Spinner<Integer> pQTYSpinner;

    @FXML
    private TextField cNameTextField;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    @FXML
    private TextField cNICTextField;

    @FXML
    private TableView<CartItem> cartTable;

    @FXML
    private TableColumn<CartItem, String> indexColumn;

    @FXML
    private TableColumn<CartItem, String> itemIDColumn;

    @FXML
    private TableColumn<CartItem, String> itemNameColumn;

    @FXML
    private TableColumn<CartItem, Integer> itemQTYColumn;

    @FXML
    private TableColumn<CartItem, Double> itemPriceColumn;

    @FXML
    private TableColumn<CartItem, Double> itemTotalColumn;

    @FXML
    private TableColumn<CartItem, String> itemActionColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> pIDColumn;

    @FXML
    private TableColumn<Product, String> pNameColumn;

    @FXML
    private TableColumn<Product, Integer> pAvailableQTYColumn;

    @FXML
    private TableColumn<Product, Double> pUPriceColumn;
    @FXML
    private TextField pIDTextField;

    @FXML
    private TextField pNameTextField;

    @FXML
    private Label productValidationLabel;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label customerValidationLabel;

    @FXML
    private Label cNoteValidationLabel;

    @FXML
    private TextField oDateTextField;

    @FXML
    private TextField oUnitTextField;

    @FXML
    private TextField pUnitPriceTextField;

    @FXML
    private Label cartTotalLabel;

    @FXML
    private JFXButton cancelButton;

    private static Product selectedProduct = null;

    private static Customer selectedCustomer = null;

    private LinkedList<Product> productLinkedList = new LinkedList<>();

    private LinkedList<CartItem> cartItemLinkedList = new LinkedList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadProductData("init");

        //ObservableList<Product> list = FXCollections.observableArrayList(productLinkedList);


        //initializing UI Component Data
        SpinnerValueFactory<Integer> quantityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        pQTYSpinner.setValueFactory(quantityValueFactory);

        ObservableList<String> statusList = FXCollections.observableArrayList("Pending", "Processing", "Delivered");
        statusChoiceBox.setValue("Pending");
        statusChoiceBox.setItems(statusList);

        oDateTextField.setText(LocalDate.now().toString());
        oUnitTextField.setText("0 Units");
        cartTotalLabel.setText("Rs 0.00");


    }

    //load Product Table Data and setting Search Field Properties
    private void loadProductData(String type){
        ProductService productService = new ProductService();
        ObservableList<Product> products = productService.loadAllProductData();
        if(type.equals("init")){
            for(Product product : productService.loadAllProductData()){
                if(product.getpAvailability().equals("Available"))
                    productLinkedList.add(product);
            }
        }

        ObservableList<Product> productObservableList = FXCollections.observableArrayList(productLinkedList);


        pIDColumn.setCellValueFactory(new PropertyValueFactory<>("pID"));
        pNameColumn.setCellValueFactory(new PropertyValueFactory<>("pName"));
        pAvailableQTYColumn.setCellValueFactory(new PropertyValueFactory<>("pQuantity"));
        pUPriceColumn.setCellValueFactory(new PropertyValueFactory<>("pPrice"));

        productTable.setItems(null);
        productTable.setItems(productObservableList);
        searchTable(productObservableList);



    }

    //setting Product search Field Properties
    public void searchTable(ObservableList<Product> searchData) {
        ProductService productService = new ProductService();
        SortedList<Product> sortedData = productService.searchCartProductTable(searchData, searchTextField) ;

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());
        //adding sorted and filtered data to the table

        productTable.setItems(sortedData);
    }

    //setting selected Product data to fields
    @FXML
    void setSelectedProductDataToFields(MouseEvent event) {
        try {
            selectedProduct = productTable.getSelectionModel().getSelectedItem();
            pIDTextField.setText(selectedProduct.getpID());
            pNameTextField.setText(selectedProduct.getpName());
            pUnitPriceTextField.setText(selectedProduct.getpPrice().toString());
            pQTYSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, selectedProduct.getpQuantity(), 1));
        } catch (NullPointerException exception) {

        }
    }

    //addling selected product to cart
    @FXML
    private void addItemToCart(ActionEvent actionEvent){
        clearProductSelectionLabel();
        if(selectedProduct != null){
            CartItem cartItem = new CartItem(pIDTextField.getText(),
                    pNameTextField.getText(),
                    pQTYSpinner.getValue(),
                    Double.valueOf(pUnitPriceTextField.getText()));
            cartItemLinkedList.add(cartItem);

            LinkedList<Product> tempProductList = new LinkedList<>(productLinkedList);
            productLinkedList.clear();
            for(Product product : tempProductList){
                if(product.getpID().equals(selectedProduct.getpID())){
                    //deducting quantity of selected Item in table List
                    Product addProduct = new Product();
                    addProduct.setpID(product.getpID());
                    addProduct.setpName(product.getpName());
                    addProduct.setpInfo(product.getpInfo());
                    addProduct.setpQuantity(product.getpQuantity() - pQTYSpinner.getValue());
                    addProduct.setpPrice(product.getpPrice());
                    addProduct.setpAvailability(product.getpAvailability());
                    productLinkedList.add(addProduct);
                }else{
                    productLinkedList.add(product);
                }
            }
            setCartData();
            //refreshing product table
            loadProductData("refresh");
            clearProductFields(actionEvent);

        }else{
            productValidationLabel.setText("Please Select Product to add");
        }


    }

    //adding data to cart
    private void setCartData(){
        ObservableList<CartItem> cartItemObservableList = FXCollections.observableArrayList();
        cartItemObservableList.addAll(cartItemLinkedList);

        indexColumn.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        itemIDColumn.setCellValueFactory(new PropertyValueFactory<>("itemProductID"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemProductName"));
        itemQTYColumn.setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("itemUnitPrice"));
        itemTotalColumn.setCellValueFactory(new PropertyValueFactory<>("itemTotalPrice"));
        itemActionColumn.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        indexColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(cartTable.getItems().indexOf(p.getValue()) + 1 +" "));
        indexColumn.setSortable(false);
        Callback<TableColumn<CartItem, String>, TableCell<CartItem, String>> parentCellFactory
                =
                new Callback<TableColumn<CartItem, String>, TableCell<CartItem, String>>() {
                    @Override
                    public TableCell call(final TableColumn<CartItem, String> param) {
                        final TableCell<CartItem, String> cell = new TableCell<CartItem, String>() {

                            final Button btn = new Button("Remove");
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {

                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        int index = cartTable.getItems().indexOf(getTableView().getItems().get(getIndex()));

                                        //Removing Specific item from billing List
                                        CartItem cartItem = cartItemLinkedList.get(index);
                                        cartItemLinkedList.remove(index);
                                        LinkedList<Product> tempList = new LinkedList<>(productLinkedList);
                                        productLinkedList.clear();

                                        //re-stocking product Items from cart to product table
                                        for(Product product : tempList){
                                            if(product.getpID().equals(cartItem.getItemProductID())){
                                                Product updateProduct = new  Product();
                                                updateProduct.setpID(product.getpID());
                                                updateProduct.setpName( product.getpName());
                                                updateProduct.setpInfo(product.getpInfo());
                                                updateProduct.setpPrice(product.getpPrice());
                                                updateProduct.setpQuantity(product.getpQuantity() + cartItem.getItemQuantity());

                                                updateProduct.setpAvailability(product.getpAvailability());

                                                productLinkedList.add(updateProduct);
                                            }else{
                                                productLinkedList.add(product);
                                            }
                                        }
                                        tempList.clear();
                                        loadProductData("refresh");
                                        try {
                                            setCartData();
                                        } catch ( Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        itemActionColumn.setCellFactory(parentCellFactory);

        cartTable.setItems(null);
        cartTable.setItems(cartItemObservableList);

        oUnitTextField.setText(cartItemLinkedList.size() + " Units");

        double totalPrice = 0;
        for(CartItem cartItem : cartItemLinkedList){
            totalPrice += cartItem.getItemTotalPrice();
        }

        cartTotalLabel.setText("Rs "+totalPrice+"0");
    }

    //Adding order with cart items
    @FXML
    void addOrder(ActionEvent event) {

        clearCartLabels();

        if(cartItemLinkedList.size() > 0){
            if (orderValidation()) {
                Order order = new Order();
                OrderService orderService = new OrderService();
                order.setoCID(selectedCustomer.getcID());
                if(!DataValidation.TextAreaNotEmpty(cNoteTextArea)){
                    order.setoNotes("Not Specified");
                }else{
                    order.setoNotes(cNoteTextArea.getText());
                }

                order.setoDate(LocalDate.now().toString());
                order.setoStatus(statusChoiceBox.getValue());
                order.setoItemList(cartItemLinkedList);


                if (orderService.insertOrder(order)) {
                    AlertPopUp.insertSuccesfully("Order");
                    clearCartFields(event);
                    clearCart(event);
                    closeStage();
                } else
                    AlertPopUp.insertionFailed("Order");

            }
        }else{
            AlertPopUp.noRecordFound("No Order Items found in the Cart, Please add Items to proceed with Order");
        }
    }

    //clearing product fields
    @FXML
    void clearProductFields(ActionEvent event) {
        pIDTextField.setText("");
        pNameTextField.setText("");
        pUnitPriceTextField.setText("");
        pQTYSpinner.getValueFactory().setValue(1);
        productValidationLabel.setText("");
        selectedProduct = null;
        clearProductSelectionLabel();
    }

    //clearing cart
    @FXML
    void clearCart(ActionEvent event){
        cartItemLinkedList.clear();
        setCartData();
    }

    //clearing cart fields
    @FXML
    void clearCartFields(ActionEvent event){
        cNICTextField.setText("");
        cNameTextField.setText("");
        cNoteTextArea.setText("");
        statusChoiceBox.setValue("Pending");
        clearCartLabels();
        setCartData();
    }

    //clearing cart labels
    private void clearCartLabels() {
        customerValidationLabel.setText("");
        cNoteValidationLabel.setText("");
    }

    //clearing product validation labels
    private void clearProductSelectionLabel() {
        productValidationLabel.setText("");
    }

    //order field validation
    private boolean orderValidation() {
        if(!(DataValidation.TextFieldNotEmpty(cNICTextField.getText())

                && DataValidation.isValidMaximumLength(cNICTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(cNoteTextArea.getText(), 400))){
            DataValidation.TextFieldNotEmpty(cNICTextField.getText(), customerValidationLabel, "Please Select a Customer!..");

            return false;
        }else
            return true;
    }

    //browse Customer
    @FXML
    private void browseCustomer(ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader();
        CustomerPopUpController.setData("add");
        loader.setLocation(getClass().getResource("customerPopUp.fxml"));
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

        if(selectedCustomer != null){
            cNICTextField.setText(selectedCustomer.getcNIC());
            cNameTextField.setText(selectedCustomer.getcName());
        }

    }

    //set customer when loading page
    public void setCustomer(Customer customer){
        selectedCustomer = customer;
    }

    //close stage
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

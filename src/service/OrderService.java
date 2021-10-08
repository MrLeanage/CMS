package service;


import bean.CartItem;
import bean.Customer;
import bean.Order;
import bean.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import utility.dataHandler.UtilityMethod;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;
import utility.query.OrderQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class OrderService {
    private Connection connection;
    public OrderService(){
        connection = DBConnection.getDBConnection();
    }

    //load all orders with product list
    public ObservableList<Order> loadAllOrderData() {
        ObservableList<Order> orderObservableList = FXCollections.observableArrayList();

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(OrderQuery.LOAD_ALL_ORDER_DATA);
            CustomerService customerService = new CustomerService();
            while (resultSet.next()) {
                LinkedList<CartItem> cartItems = new LinkedList<>(loadAllOrderProduct(UtilityMethod.addPrefix("O", resultSet.getString(1))));
                Customer customer = customerService.loadSpecificCustomer(UtilityMethod.addPrefix("C", resultSet.getString(2)));
                orderObservableList.add(new Order(resultSet.getString(1), resultSet.getString(2), customer.getcName(), resultSet.getString(3), cartItems, resultSet.getString(4), resultSet.getString(5)));
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            AlertPopUp.sqlQueryError(sqlException);
        }
        return orderObservableList;
    }


    //load specific Order
    public Order loadSpecificOrder(String oID) {
        Order order = null;

        try {
            CustomerService customerService = new CustomerService();
            PreparedStatement preparedStatement = connection.prepareStatement(OrderQuery.LOAD_SPECIFIC_ORDER_DATA);
            preparedStatement.setInt(1, UtilityMethod.seperateID(oID));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LinkedList<CartItem> cartItems = new LinkedList<>(loadAllOrderProduct(UtilityMethod.addPrefix("O", resultSet.getString(1))));
                Customer customer = customerService.loadSpecificCustomer(UtilityMethod.addPrefix("C", resultSet.getString(2)));
                order = new Order(resultSet.getString(1), resultSet.getString(2), customer.getcName(), resultSet.getString(3), cartItems, resultSet.getString(4), resultSet.getString(5));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return order;
    }

    //load all products in an order
    public LinkedList<CartItem> loadAllOrderProduct(String oIID) {
        LinkedList<CartItem> cartItemLinkedList = new LinkedList<>();

        try {
            ProductService productService = new ProductService();
            PreparedStatement preparedStatement = connection.prepareStatement(OrderQuery.LOAD_ALL_ORDER_PRODUCT_DATA);
            preparedStatement.setInt(1, UtilityMethod.seperateID(oIID));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = productService.loadSpecificProduct(UtilityMethod.addPrefix("P", resultSet.getString(3)));
                cartItemLinkedList.add( new CartItem(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), product.getpName(), resultSet.getInt(4), resultSet.getDouble(5)));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return cartItemLinkedList;
    }

    //inserting order
    public boolean insertOrder(Order order) {
        boolean status = true;
            if(insertOrderData(order)) {
                ProductService productService = new ProductService();
                for(CartItem cartItem : order.getoItemList()){
                    if(insertOrderItem(cartItem, getLastRecordID())){
                        Product product = productService.loadSpecificProduct(cartItem.getItemProductID());
                        Product temp = product;
                        temp.setpQuantity(product.getpQuantity() - cartItem.getItemQuantity());
                        status =  productService.updateProductData(temp);
                    }else
                        status =  false;
                }
            }else
                status =  false;
            return status;
    }

    //adding order data - info
    private boolean insertOrderData(Order order){
        try {
            PreparedStatement psOrder = connection.prepareStatement(OrderQuery.INSERT_ORDER_DATA);

            psOrder.setInt(1, order.getIntegeroCID());
            psOrder.setString(2, order.getoNotes());
            psOrder.setString(3, order.getoDate());
            psOrder.setString(4, order.getoStatus());
            psOrder.execute();

            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    //getting last added record after adding order data
    private int getLastRecordID(){
        int lastOrderID = 0;
        try{
            PreparedStatement psOrderRecord = connection.prepareStatement(OrderQuery.GET_LAST_RECORD_ID);
            ResultSet resultSet = psOrderRecord.executeQuery();

            while(resultSet.next()){
                lastOrderID = resultSet.getInt(1);
            }
        }catch (SQLException exception){
            AlertPopUp.sqlQueryError(exception);
        }
        return lastOrderID;
    }

    //inserting order product list under order ID
    private boolean insertOrderItem(CartItem cartItem, Integer orderID){
        PreparedStatement psOrderItem = null;
        try{
            psOrderItem = connection.prepareStatement(OrderQuery.INSERT_ORDER_ITEM_DATA);

            psOrderItem.setInt(1, orderID);
            psOrderItem.setInt(2, UtilityMethod.seperateID(cartItem.getItemProductID()));
            psOrderItem.setInt(3, cartItem.getItemQuantity());
            psOrderItem.setDouble(4, cartItem.getItemUnitPrice());

            psOrderItem.execute();
            return true;
        }catch(SQLException exception){
            AlertPopUp.sqlQueryError(exception);
            return false;
        }

    }

    //update order Info
    public boolean updateOrderData(Order order) {
        PreparedStatement psOrder = null;
        try {

            psOrder = connection.prepareStatement(OrderQuery.UPDATE_ORDER_DATA);

            psOrder.setInt(1, order.getIntegeroCID());
            psOrder.setString(2, order.getoNotes());
            psOrder.setString(3, order.getoDate());
            psOrder.setString(4, order.getoStatus());
            psOrder.setInt(5, UtilityMethod.seperateID(order.getoID()));
            psOrder.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    //deleting Order
    public boolean deleteOrderData(String oID) {
        PreparedStatement psOrder = null;
        boolean orderDeletionStatus = false;

        boolean deleteStatus = false;
        try {
            psOrder = connection.prepareStatement(OrderQuery.DELETE_ORDER_DATA);
            psOrder.setInt(1, UtilityMethod.seperateID(oID));
            psOrder.execute();
            orderDeletionStatus = true;
        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            orderDeletionStatus = false;
        }
        if(orderDeletionStatus)
            deleteStatus = deleteOrderProductData(oID);
        return deleteStatus;
    }

    public boolean deleteOrderProductData(String oID) {
        PreparedStatement psOrder = null;
        try {
            psOrder = connection.prepareStatement(OrderQuery.DELETE_ORDER_PRODUCT_DATA);
            psOrder.setInt(1, UtilityMethod.seperateID(oID));
            psOrder.execute();
            return true;
        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }


    public SortedList<Order> searchTable(TextField searchTextField) {
        //Retreiving all data from database
        ObservableList<Order> orderData = loadAllOrderData();
        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Order> filteredData = new FilteredList<>(orderData, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(order -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                 if (order.getoCName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (order.getoStatus().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 }else if (UtilityMethod.addPrefix("OR",order.getoID()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 } else {
                    //have no matchings
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Order> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }

}

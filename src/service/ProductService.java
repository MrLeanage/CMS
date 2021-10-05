package service;


import bean.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import utility.dataHandler.UtilityMethod;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;
import utility.query.ProductQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductService {
    private Connection connection;
    public ProductService(){
        connection = DBConnection.getDBConnection();
    }

    public ObservableList<Product> loadAllProductData() {
        ObservableList<Product> productObservableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(ProductQuery.LOAD_ALL_PRODUCT_DATA);
            while (resultSet.next()) {
                productObservableList.add(new Product(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getDouble(5), resultSet.getString(6)));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return productObservableList;
    }

    public Product loadSpecificProduct(String mID) {
        Product product = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ProductQuery.LOAD_SPECIFIC_PRODUCT_DATA);
            preparedStatement.setInt(1, UtilityMethod.seperateID(mID));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                product = new Product(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getDouble(5), resultSet.getString(6));
            }
        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return product;
    }

    public boolean insertProductData(Product product){
        try {
            PreparedStatement psSupply = connection.prepareStatement(ProductQuery.INSERT_PRODUCT_DATA);
            psSupply.setString(1, product.getpName());
            psSupply.setString(2, product.getpInfo());
            psSupply.setInt(3, product.getpQuantity());
            psSupply.setDouble(4, product.getpPrice());
            psSupply.setString(5, product.getpAvailability());
            psSupply.execute();

            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public SortedList<Product> searchTable(TextField searchTextField) {
        //Retreiving all data from database
        ObservableList<Product> menuData = loadAllProductData();
        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Product> filteredData = new FilteredList<>(menuData, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                 if (product.getpID().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (product.getpName().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (product.getpAvailability().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 } else {
                    //have no matchings
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Product> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }

}

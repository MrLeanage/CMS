package service;

import bean.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import utility.dataHandler.UtilityMethod;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;
import utility.query.CustomerQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerService {
    private Connection connection = null;
    public CustomerService(){
        connection = DBConnection.getDBConnection();
    }
    public ObservableList<Customer> loadAllCustomerData() {
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(CustomerQuery.LOAD_ALL_CUSTOMER_DATA);
            while (resultSet.next()) {
                customerObservableList.add(new Customer(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5)
                        )
                );
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return customerObservableList;
    }

    public Customer loadSpecificCustomer(String id) {
        Customer customer = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CustomerQuery.LOAD_SPECIFIC_CUSTOMER_DATA);
            preparedStatement.setInt(1, UtilityMethod.seperateID(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customer = new Customer(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
                );
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return customer;
    }

    public boolean insertCustomerData(Customer customer) {
        try {

            PreparedStatement psSupply = connection.prepareStatement(CustomerQuery.INSERT_CUSTOMER_DATA);
            psSupply.setString(1, customer.getcName());
            psSupply.setString(2, customer.getcNIC());
            psSupply.setString(3, customer.getcAddress());
            psSupply.setString(4, customer.getcPhone());
            psSupply.execute();

            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean updateCustomerData(Customer customer) {
        try {

            PreparedStatement psSupply = connection.prepareStatement(CustomerQuery.UPDATE_CUSTOMER_DATA);

            psSupply.setString(1, customer.getcName());
            psSupply.setString(2, customer.getcNIC());
            psSupply.setString(3, customer.getcAddress());
            psSupply.setString(4, customer.getcPhone());
            psSupply.setInt(5, customer.getIntegercID());
            psSupply.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean deleteCustomerData(String id) {
        try {
            PreparedStatement psSupply = connection.prepareStatement(CustomerQuery.DELETE_CUSTOMER_DATA);
            psSupply.setInt(1, UtilityMethod.seperateID(id));
            psSupply.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public SortedList<Customer> searchTable(TextField searchTextField) {
        //Retreiving all data from database
        ObservableList<Customer> customerData = loadAllCustomerData();
        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Customer> filteredData = new FilteredList<>(customerData, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getcID().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (customer.getcName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (customer.getcNIC().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (customer.getcPhone().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else {
                    //have no matchings
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Customer> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }
}

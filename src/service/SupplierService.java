package service;

import bean.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import utility.dataHandler.UtilityMethod;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;
import utility.query.SupplierQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierService {
    private Connection connection = null;
    public SupplierService(){
        connection = DBConnection.getDBConnection();
    }
    public ObservableList<Supplier> loadAllSupplierData() {
        ObservableList<Supplier> supplierObservableList = FXCollections.observableArrayList();

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(SupplierQuery.LOAD_ALL_SUPPLIER_DATA);
            while (resultSet.next()) {
                supplierObservableList.add(new Supplier(
                        resultSet.getString(1),

                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)
                        )
                );
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return supplierObservableList;
    }

    public Supplier loadSpecificSupplier(int id) {
        Supplier supplier = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SupplierQuery.LOAD_SPECIFIC_SUPPLIER_DATA);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                supplier = new Supplier(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)
                );
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return supplier;
    }

    public boolean insertSupplierData(Supplier supplier) {
        try {

            PreparedStatement psSupply = connection.prepareStatement(SupplierQuery.INSERT_SUPPLIER_DATA);
            psSupply.setString(1, supplier.getName());
            psSupply.setString(2, supplier.getNic());
            psSupply.setString(3, supplier.getEmail());
            psSupply.setString(4, supplier.getAddress());
            psSupply.setString(5, supplier.getPhoneNumber());
            psSupply.setString(6, supplier.getType());
            psSupply.execute();

            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean updateSupplierData(Supplier supplier) {
        try {

            PreparedStatement psSupply = connection.prepareStatement(SupplierQuery.UPDATE_SUPPLIER_DATA);

            psSupply.setString(1, supplier.getName());
            psSupply.setString(2, supplier.getNic());
            psSupply.setString(3, supplier.getEmail());
            psSupply.setString(4, supplier.getAddress());
            psSupply.setString(5, supplier.getPhoneNumber());
            psSupply.setString(6, supplier.getType());
            psSupply.setInt(7, supplier.getIntegerId());
            psSupply.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean deleteSupplierData(String id) {
        try {
            PreparedStatement psSupply = connection.prepareStatement(SupplierQuery.DELETE_SUPPLIER_DATA);
            psSupply.setInt(1, UtilityMethod.seperateID(id));
            psSupply.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public SortedList<Supplier> searchTable(TextField searchTextField) {
        //Retreiving all data from database
        ObservableList<Supplier> supplierData = loadAllSupplierData();
        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Supplier> filteredData = new FilteredList<>(supplierData, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplier -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                if (supplier.getId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (supplier.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (supplier.getNic().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (supplier.getEmail().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (supplier.getType().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (supplier.getPhoneNumber().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else {
                    //have no matchings
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Supplier> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }
}

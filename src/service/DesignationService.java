package service;


import bean.Designation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import utility.dataHandler.UtilityMethod;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;
import utility.query.DesignationQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DesignationService {
    private Connection connection;
    public DesignationService(){
        connection = DBConnection.getDBConnection();
    }

    public ObservableList<Designation> loadAllDesignationData() {
        ObservableList<Designation> designationObservableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(DesignationQuery.LOAD_ALL_DESIGNATION_DATA);
            while (resultSet.next()) {
                designationObservableList.add(new Designation(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return designationObservableList;
    }

    public Designation loadSpecificDesignation(String dID) {
        Designation designation = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DesignationQuery.LOAD_SPECIFIC_DESIGNATION_DATA);
            preparedStatement.setInt(1, UtilityMethod.seperateID(dID));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                designation = new Designation(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
            }
        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return designation;
    }

    public boolean insertDesignationData(Designation designation) {
        PreparedStatement psDesignation = null;
        try {
            psDesignation = connection.prepareStatement(DesignationQuery.INSERT_DESIGNATION_DATA);

            psDesignation.setString(1, designation.getdName());
            psDesignation.setString(2, designation.getdDescription());
            psDesignation.setString(3, designation.getdStatus());
            psDesignation.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean updateDesignationData(Designation designation) {
        PreparedStatement psDesignation = null;
        try {
            psDesignation = connection.prepareStatement(DesignationQuery.UPDATE_DESIGNATION_DATA);

            psDesignation.setString(1, designation.getdName());
            psDesignation.setString(2, designation.getdDescription());
            psDesignation.setString(3, designation.getdStatus());
            psDesignation.setInt(4, UtilityMethod.seperateID(designation.getdID()));
            psDesignation.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public boolean deleteDesignationData(String dID) {
        PreparedStatement psDesignation = null;
        try {

            psDesignation = connection.prepareStatement(DesignationQuery.DELETE_DESIGNATION_DATA);

            psDesignation.setInt(1, UtilityMethod.seperateID(dID));
            psDesignation.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
            return false;
        }
    }

    public SortedList<Designation> searchTable(TextField searchTextField) {
        //Retreiving all data from database
        ObservableList<Designation> designationData = loadAllDesignationData();
        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Designation> filteredData = new FilteredList<>(designationData, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(designation -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                 if (designation.getdID().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (designation.getdName().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (designation.getdStatus().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 } else {
                    //have no matchings
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Designation> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }

}

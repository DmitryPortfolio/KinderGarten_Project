/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import static org.Controllers.AttendanceController.getData;
import static org.connector.connector.ConnectDB;
import org.model.ModelAttendance;
import org.model.ModelGroups;

/**
 * FXML Controller class
 *
 * @author Пользователь
 */
public class GroupsController implements Initializable {

    @FXML
    private TableView<ModelGroups> TvGroups;
    private TableColumn<ModelGroups, Integer> TcNomer;
    @FXML
    private TableColumn<ModelGroups, String> TcNameGroup;
    @FXML
    private TableColumn<ModelGroups, String> TcYear;
ObservableList<ModelGroups> list;
    @FXML
    private Button BtnAdd;
     int idGroup;
     GroupsAddController controller;
    @FXML
    private Button BtnEdt;
    @FXML
    private Button BtnDelete;
    @FXML
    private Button BtnPrint;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          ConnectDB();
        UpdateTable();
    }    
    
    
    
    
    
    public static ObservableList<ModelGroups> getData() {
        ObservableList<ModelGroups> ModelGroups = FXCollections.observableArrayList();
        Connection conn = ConnectDB();

        try {

            PreparedStatement ps = conn.prepareStatement("select *from groups");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModelGroups.add(new ModelGroups(Integer.parseInt(
                        rs.getString("ID")),
                        rs.getString("Name_groups"),                       
                        rs.getString("Year")));

            }
        } catch (SQLException ex) {

        }
        return ModelGroups;
    }

    public void UpdateTable() {
      
        TcNameGroup.setCellValueFactory(new PropertyValueFactory<ModelGroups, String>("Name_groups"));
        TcYear.setCellValueFactory(new PropertyValueFactory<ModelGroups, String>("Year"));
      
        
        list = getData();
        TvGroups.setItems(list);

        
 
    }

    @FXML
    private void Add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLGroupsAdd.fxml"));
        Pane page = (Pane) loader.load();
GroupsAddController addController = loader.getController();
        

        Stage duStage = new Stage();
       duStage.setTitle("Добавить запись");
        duStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/add.png")));

        Scene scene = new Scene(page);
        duStage.setScene(scene);
        duStage.setResizable(false);
        duStage.showAndWait();
        UpdateTable();
    }

    @FXML
    private void Edt(ActionEvent event) throws IOException {
         ModelGroups selectedAdress = TvGroups.getSelectionModel().getSelectedItem();

        if (selectedAdress == null) {
           showAlert("Выберите запись для редактирования.");
            return;
        }
        
          FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLGroupsAdd.fxml"));
        Pane page = (Pane) loader.load();
GroupsAddController addController = loader.getController();
         addController.setModelOperatorToEdit(selectedAdress);

        Stage duStage = new Stage();
        duStage.setTitle("Редактировать запись");
        duStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/edit.png")));

        Scene scene = new Scene(page);
        duStage.setScene(scene);
        duStage.setResizable(false);
        duStage.showAndWait();
        UpdateTable();
    }

    @FXML
    private void Delete(ActionEvent event) {
        ModelGroups selected = TvGroups.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Выберите строку для удаления");
            alert.setHeaderText("");
            alert.setTitle("Ошибка");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы действительно хотите удалить запись?", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("");
            alert.setTitle("Подтверждение");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                int del = selected.getID();
                String sql = "DELETE FROM groups WHERE ID = ?";

                try (Connection conn = ConnectDB();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1, del);
                    pstmt.executeUpdate();
                    System.out.println("Запись удалена");
                    UpdateTable();
                } catch (SQLException e) {
                    System.out.println("Ошибка при удалении записи: " + e.getMessage());
                    e.printStackTrace();
                    showAlert("Ошибка при удалении записи. В этой группе находится воспиттаник. Для удаления записи перенесите воспитанника в другую группу");
                }
            }
        }
    }
    
     private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void Print(ActionEvent event) throws JRException {
          Connection conn = ConnectDB();
            JasperDesign jDesign = JRXmlLoader.load(getClass().getResourceAsStream("/org/Reports/Groups.jrxml"));
            JasperReport jReport = JasperCompileManager.compileReport(jDesign);
            JasperPrint jPrint = JasperFillManager.fillReport(jReport, null, conn);
            JasperViewer viewer = new JasperViewer(jPrint, false);
            viewer.setTitle("Report groups");
            viewer.show();
    }

    
    
    
    }

 
    



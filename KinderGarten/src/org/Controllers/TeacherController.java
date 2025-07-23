package org.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
import static org.connector.connector.ConnectDB;

import org.model.ModelTeacher;

public class TeacherController implements Initializable {

    @FXML
    private TableView<ModelTeacher> TvTeacher;
    @FXML
    private TableColumn<ModelTeacher, String> TcFIO;
    @FXML
    private TableColumn<ModelTeacher, String> TcTelefon;
    @FXML
    private TableColumn<ModelTeacher, String> TcSpecialization;
 

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    private ObservableList<ModelTeacher> list;
    private Connection conn;

    @FXML
    private ComboBox<String> CbFiltrSpecialization;

    @FXML
    private CheckBox CbFiltrOfon;
    
    private FilteredList<ModelTeacher> filteredData;
    private ObservableList<ModelTeacher> originalList;
    @FXML
    private TableColumn<ModelTeacher, String> TcAdres;
    @FXML
    private Button BtnPrint;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConnectDB();
        loadSpecializations();
        UpdateTable();
        disableFilters();

        CbFiltrOfon.setOnAction(this::handleFilterToggleAction);
        CbFiltrSpecialization.setOnAction(this::handleSpecializationFilter);
      
    }

    private void loadSpecializations() {
        ObservableList<String> specializations = FXCollections.observableArrayList();
        try (Connection conn = ConnectDB();
             PreparedStatement ps = conn.prepareStatement("select * from teacher")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                specializations.add(rs.getString("Specialization"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Ошибка при загрузке специализаций: " + e.getMessage());
        }
        CbFiltrSpecialization.setItems(specializations);
      
    }



    public static ObservableList<ModelTeacher> getData() {
        ObservableList<ModelTeacher> ModelTeacher = FXCollections.observableArrayList();
        Connection conn = ConnectDB();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT ID, FIO, Telefon, Adres, Specialization FROM teacher");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ModelTeacher.add(new ModelTeacher(
                        Integer.parseInt(rs.getString("ID")),
                        rs.getString("FIO"),
                        rs.getString("Telefon"),
                        rs.getString("Adres"),
                        rs.getString("Specialization")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ModelTeacher;
    }

    public void UpdateTable() {

        TcFIO.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        TcTelefon.setCellValueFactory(new PropertyValueFactory<>("Telefon"));
        TcAdres.setCellValueFactory(new PropertyValueFactory<>("Adres"));
        TcSpecialization.setCellValueFactory(new PropertyValueFactory<>("Specialization"));
        
        list = getData();
        originalList = FXCollections.observableArrayList(list);
        filteredData = new FilteredList<>(originalList, p -> true);
        TvTeacher.setItems(filteredData);
    }

    @FXML
    private void handleSpecializationFilter(ActionEvent event) {
        applyFilters();
    }

    private void handleGroupFilter(ActionEvent event) {
        applyFilters();
    }

    private void applyFilters() {
        filteredData.setPredicate(teacher -> {
            if (!CbFiltrOfon.isSelected()) {
                return true;
            }

            if (CbFiltrSpecialization.getValue() != null && !CbFiltrSpecialization.getValue().isEmpty()) {
                if (!teacher.getSpecialization().equalsIgnoreCase(CbFiltrSpecialization.getValue())) {
                    return false;
                }
            }
            return true;
        });
    }

    @FXML
    void handleFilterToggleAction(ActionEvent event) {
        if (CbFiltrOfon.isSelected()) {
            enableFilters();
        } else {
            disableFilters();
            resetFilters();
        }
    }

    private void enableFilters() {
        CbFiltrSpecialization.setDisable(false);
        CbFiltrSpecialization.setOnAction(this::handleSpecializationFilter);

    }

    private void disableFilters() {
        CbFiltrSpecialization.setDisable(true);
        CbFiltrSpecialization.setOnAction(null);

    }

    private void resetFilters() {
        CbFiltrSpecialization.getSelectionModel().clearSelection();
        filteredData.setPredicate(p -> true);
    }

    @FXML
    private void Add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLTeacherAdd.fxml"));
        Pane page = (Pane) loader.load();

        TeacherAddController addController = loader.getController();


        Stage duStage = new Stage();
duStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/add.png")));
     
        duStage.setTitle("Добавить запись");
        Scene scene = new Scene(page);
        duStage.setScene(scene);
        duStage.setResizable(false);
        duStage.showAndWait();
        UpdateTable();
    }

    @FXML
    private void Edit(ActionEvent event) throws IOException {
        ModelTeacher selectedTeacher = TvTeacher.getSelectionModel().getSelectedItem();

        if (selectedTeacher == null) {
            showAlert("Выберите учителя для редактирования.");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLTeacherAdd.fxml"));
        Pane page = (Pane) loader.load();

        TeacherAddController addController = loader.getController();
        addController.setModelTeacherToEdit(selectedTeacher);

        Stage duStage = new Stage();
duStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/edit.png")));
     
        duStage.setTitle("Редактировать запись");
        Scene scene = new Scene(page);
        duStage.setScene(scene);
        duStage.setResizable(false);
        duStage.showAndWait();
        UpdateTable();
    }


    @FXML
    private void Delete(ActionEvent event) {
        ModelTeacher selectedTeacher = TvTeacher.getSelectionModel().getSelectedItem();

        if (selectedTeacher == null) {
            showAlert("Выберите учителя для удаления.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Вы уверены, что хотите удалить этого учителя?");
        alert.setContentText("Нажмите OK для подтверждения, Cancel для отмены.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteModelTeacher(selectedTeacher.getID());
            UpdateTable();
        }
    }

    private void deleteModelTeacher(int id) {
        String sql = "DELETE FROM teacher WHERE ID = ?";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Учитель удален");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            JasperDesign jDesign = JRXmlLoader.load(getClass().getResourceAsStream("/org/Reports/Teacher.jrxml"));
            JasperReport jReport = JasperCompileManager.compileReport(jDesign);
            JasperPrint jPrint = JasperFillManager.fillReport(jReport, null, conn);
            JasperViewer viewer = new JasperViewer(jPrint, false);
            viewer.setTitle("Report teacher");
            viewer.show();  
    }


}
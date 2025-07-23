
package org.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.DatePicker;
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
import org.connector.connector;
import static org.connector.connector.ConnectDB;
import org.model.ModelAttendance;

public class AttendanceController implements Initializable {

    @FXML
    private TableView<ModelAttendance> TvAttendance;
    @FXML
    private TableColumn<ModelAttendance, String> TcIdChildren;
    @FXML
    private TableColumn<ModelAttendance, String> TcDate;
    @FXML
    private TableColumn<ModelAttendance, String> TCStatus;

    @FXML
    private DatePicker DateStart;
    @FXML
    private DatePicker DateEnd;
    @FXML
    private ComboBox<String> CbFioChildren;
    @FXML
    private Button BtnAdd;
    @FXML
    private ComboBox<String> CbStatus;

    @FXML
    private Button BtnEdit;

    private ObservableList<ModelAttendance> list;
    private Connection conn;
    private AttendanceAddController controller;
    public ObservableList<String> comboboxList = FXCollections.observableArrayList();
    @FXML
    private Button BtnDelete;
    Connection Conn;
    connector con = new connector();
    @FXML
    private CheckBox CbFltrOfon;
    
    private FilteredList<ModelAttendance> filteredData;
    private ObservableList<ModelAttendance> originalList;
    @FXML
    private Button BtnPrint;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CbFioChildren.setItems(FXCollections.observableArrayList(getFIO()));
        comboboxList.addAll("Присутствует", "Отсутствует (Уважительная причина)", "Отсутствует (Неуважительная причина)");
        CbStatus.setItems(comboboxList);

        DateStart.setValue(LocalDate.now());
        ConnectDB();
        UpdateTable();
        disableFilters();

        CbFltrOfon.setOnAction(this::handleFilterToggleAction);

    }

    public static ObservableList<ModelAttendance> getData() {
        ObservableList<ModelAttendance> ModelAttendance = FXCollections.observableArrayList();
        Connection conn = ConnectDB();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT attendance.ID, children.ID AS ChildID, attendance.Date, attendance.Status FROM attendance\n"
                    + "INNER JOIN children\n"
                    + "ON attendance.Id_Children = children.ID\n");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ModelAttendance.add(new ModelAttendance(Integer.parseInt(
                        rs.getString("attendance.ID")),
                        rs.getString("ChildID"),
                        rs.getString("attendance.Date"),
                        rs.getString("attendance.Status")));

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ModelAttendance;
    }

    public void UpdateTable() {
        TcIdChildren.setCellValueFactory(cellData -> {
            String childId = cellData.getValue().getId_Children();
            String childFIO = getFIOById(Integer.parseInt(childId));
            return new SimpleStringProperty(childFIO);
        });
        TcDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        TCStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        list = getData();
        originalList = FXCollections.observableArrayList(list);
        filteredData = new FilteredList<>(originalList, p -> true);
        TvAttendance.setItems(filteredData);
    }

    private String getFIOById(int childId) {
        String fio = null;
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement("SELECT FIO FROM children WHERE ID = ?")) {
            pstmt.setInt(1, childId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                fio = rs.getString("FIO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fio;
    }

    public List<String> getFIO() {
        conn = ConnectDB();
        List<String> FIO = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from children");
            while (rs.next()) {
                FIO.add(rs.getString("FIO"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return FIO;
    }

    // Методы фильтрации
    @FXML
    private void OnFioChildren(ActionEvent event) {
        applyFilters();
    }

    @FXML
    private void OnStatus(ActionEvent event) {
        applyFilters();
    }

    @FXML
    private void OnDateStart(ActionEvent event) {
        applyFilters();
    }

    @FXML
    private void OnDateEnd(ActionEvent event) {
        applyFilters();
    }

    private void applyFilters() {
        filteredData.setPredicate(attendance -> {
            if (!CbFltrOfon.isSelected()) {
                return true;
            }
            if (CbFioChildren.getValue() != null && !CbFioChildren.getValue().isEmpty()) {
                String childId = attendance.getId_Children();
                String fio = getFIOById(Integer.parseInt(childId));
                if (!fio.equalsIgnoreCase(CbFioChildren.getValue())) {
                    return false;
                }
            }
            if (CbStatus.getValue() != null && !CbStatus.getValue().isEmpty()) {
                if (!attendance.getStatus().equalsIgnoreCase(CbStatus.getValue())) {
                    return false;
                }
            }
            if (DateStart.getValue() != null) {
                if (!isDateAfter(attendance.getDate(), DateStart.getValue())) {
                    return false;
                }
            }
            if (DateEnd.getValue() != null) {
                if (!isDateBefore(attendance.getDate(), DateEnd.getValue())) {
                    return false;
                }
            }

            return true;
        });
    }

    private boolean isDateBefore(Object partnerDate, LocalDate endDate) {
        if (partnerDate instanceof String) {
            try {
                LocalDate partnerLocalDate = LocalDate.parse((String) partnerDate);
                return !partnerLocalDate.isAfter(endDate);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean isDateAfter(Object partnerDate, LocalDate startDate) {
        if (partnerDate instanceof String) {
            try {
                LocalDate partnerLocalDate = LocalDate.parse((String) partnerDate);
                return !partnerLocalDate.isBefore(startDate);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @FXML
    void handleFilterToggleAction(ActionEvent event) {
        if (CbFltrOfon.isSelected()) {
            enableFilters();
        } else {
            disableFilters();
            resetFilters();
        }
    }

    private void enableFilters() {
        CbFioChildren.setDisable(false);
        CbStatus.setDisable(false);
        DateStart.setDisable(false);
        DateEnd.setDisable(false);

        CbFioChildren.setOnAction(this::OnFioChildren);
        CbStatus.setOnAction(this::OnStatus);
        DateStart.setOnAction(this::OnDateStart);
        DateEnd.setOnAction(this::OnDateEnd);
    }

    private void disableFilters() {
        CbFioChildren.setDisable(true);
        CbStatus.setDisable(true);
        DateStart.setDisable(true);
        DateEnd.setDisable(true);

        CbFioChildren.setOnAction(null);
        CbStatus.setOnAction(null);
        DateStart.setOnAction(null);
        DateEnd.setOnAction(null);
    }

    private void resetFilters() {
        CbFioChildren.getSelectionModel().clearSelection();
        CbStatus.getSelectionModel().clearSelection();
        DateStart.setValue(null);
        DateEnd.setValue(null);
        filteredData.setPredicate(p -> true);
    }

    @FXML
    private void Add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLAttendanceAdd.fxml"));
        Pane page = (Pane) loader.load();
        Stage addStage = new Stage();
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        controller = loader.getController();
        addStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/add.png")));
        addStage.setTitle("Добавить запись");
        addStage.setResizable(false);
        addStage.showAndWait();
        UpdateTable();
    }

    @FXML
    private void Edit(ActionEvent event) throws IOException {
        ModelAttendance selectedAdress = TvAttendance.getSelectionModel().getSelectedItem();
        
        if (selectedAdress == null) {
              showAlert("Выберите запись для редактирования.");
            return;
        }
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLAttendanceAdd.fxml"));
        Pane page = (Pane) loader.load();

        AttendanceAddController addController = loader.getController();
        addController.setModelAttendanceToEdit(selectedAdress);

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
    private void Delete(ActionEvent event) throws SQLException {
        ModelAttendance selected = TvAttendance.getSelectionModel().getSelectedItem();

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
                String sql = "DELETE FROM attendance WHERE ID = ?";

                try (Connection conn = ConnectDB();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1, del);
                    pstmt.executeUpdate();
                    System.out.println("Запись удалена");
                    UpdateTable();
                } catch (SQLException e) {
                    System.out.println("Ошибка при удалении записи: " + e.getMessage());
                    e.printStackTrace();
                    showAlert("Ошибка при удалении записи.");
                }
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText("");
        alert.setTitle("Ошибка");
        alert.showAndWait();
    }

    @FXML
    private void Print(ActionEvent event) throws JRException {
         Connection conn = ConnectDB();
            JasperDesign jDesign = JRXmlLoader.load(getClass().getResourceAsStream("/org/Reports/Attendance.jrxml"));
            JasperReport jReport = JasperCompileManager.compileReport(jDesign);
            JasperPrint jPrint = JasperFillManager.fillReport(jReport, null, conn);
            JasperViewer viewer = new JasperViewer(jPrint, false);
            viewer.setTitle("Report Attendance");
            viewer.show();
    }

}

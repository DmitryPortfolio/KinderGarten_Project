
package org.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import static org.connector.connector.ConnectDB;
import org.model.ModelAttendance;

public class AttendanceAddController implements Initializable {

    @FXML
    private DatePicker Date;
    @FXML
    private ComboBox<String> CbStatus;
    @FXML
    private ComboBox<String> FIOChldren;
    @FXML
    private Button BtnAdd;

    private boolean isEditMode = false;
    private Connection conn;
    private AttendanceController controller;
    
    public ObservableList<String> comboboxListStatus = FXCollections.observableArrayList();

    private Stage dialogStage;
    private Map<String, Integer> fioToIdMap = new HashMap<>();
    private ModelAttendance attendance;
    @FXML
    private Button BtnExit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadFIOs();
        Date.setValue(LocalDate.now());
        comboboxListStatus.addAll("Присутствует", "Отсутствует (Уважительная причина)", "Отсутствует (Неуважительная причина)");
        CbStatus.setItems(comboboxListStatus);
    }

    private void loadFIOs() {
        conn = ConnectDB();
        ObservableList<String> FIOs = FXCollections.observableArrayList();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT ID, FIO FROM children");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String fio = rs.getString("FIO");
                FIOs.add(fio);
                fioToIdMap.put(fio, id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        FIOChldren.setItems(FIOs);
    }
     public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

     void setModelAttendanceToEdit(ModelAttendance selectedAttendance) {
        this.attendance = selectedAttendance;
        isEditMode = true;

        String fio = getFIOById(Integer.parseInt(selectedAttendance.getId_Children()));

        FIOChldren.setValue(fio);
        Date.setValue(LocalDate.parse(selectedAttendance.getDate()));
        CbStatus.setValue(selectedAttendance.getStatus());
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

    @FXML
    private void Add(ActionEvent event) throws SQLException, IOException {
        String fio = FIOChldren.getValue();
        LocalDate dateValue = Date.getValue();
        String status = CbStatus.getValue();

        if (fio == null || dateValue == null || status == null) {
            System.out.println("Please fill in all fields.");
            return;
        }

        String date = dateValue.toString();
        Integer childrenId = fioToIdMap.get(fio);

        if (childrenId == null) {
            System.out.println("Children ID not found for FIO: " + fio);
            return;
        }

        if (isEditMode) {
            updateTarif(attendance.getID(), childrenId, date, status);
        } else {
            insertTarif(childrenId, date, status);
        }

        Stage stage = (Stage) BtnAdd.getScene().getWindow();
        stage.close();
    }

    private void insertTarif(Integer childrenId, String date, String status) {
        String sql = "INSERT INTO attendance (Id_Children, `Date`, Status) VALUES (?, ?, ?)";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, childrenId);
            pstmt.setString(2, date);
            pstmt.setString(3, status);

            pstmt.executeUpdate();
            System.out.println("Attendance record added.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

     private void updateTarif(int id, Integer childrenId, String date, String status) {
        String sql = "UPDATE attendance SET Id_Children = ?, `Date` = ?, Status = ? WHERE ID = ?";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, childrenId);
            pstmt.setString(2, date);
            pstmt.setString(3, status);
            pstmt.setInt(4, id);

            pstmt.executeUpdate();
            System.out.println("Attendance record updated.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void Exit(ActionEvent event) {
          Stage stage = (Stage) BtnExit.getScene().getWindow();
        stage.close();
    }
}


package org.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import static org.connector.connector.ConnectDB;
import org.model.ModelTimeTable;

public class TimetableController implements Initializable {

    @FXML
    private TableView<ModelTimeTable> TvTimeTable;
    @FXML
    private TableColumn<ModelTimeTable, String> TcDate;
    @FXML
    private TableColumn<ModelTimeTable, String> TcTime;
    @FXML
    private TableColumn<ModelTimeTable, String> TcNomerGroup;
    @FXML
    private TableColumn<ModelTimeTable, String> TCNomerClassroom;
    @FXML
    private TableColumn<ModelTimeTable, String> TcTeacher;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnEdt;
    @FXML
    private Button BtnDelete;
    @FXML
    private TableColumn<ModelTimeTable, String> TcLessons;
    @FXML
    private DatePicker DpDateS;
    @FXML
    private DatePicker DpDatePo;
    @FXML
    private ComboBox<String> CbFiltrLessons;
    @FXML
    private ComboBox<String> CbGroup;
    @FXML
    private ComboBox<String> CbTeacher;
    @FXML
    private CheckBox CbFiltrOFOn;

    private ObservableList<ModelTimeTable> list;
    private FilteredList<ModelTimeTable> filteredData;
    private Connection conn;

    private ObservableList<String> groupNames = FXCollections.observableArrayList();
    private ObservableList<String> teacherNames = FXCollections.observableArrayList();
    private ObservableList<String> lessonNames = FXCollections.observableArrayList();

    private boolean isFilterEnabled = false;
    @FXML
    private Button BtnPrint;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConnectDB();
        loadComboBoxData();
        loadTableData();
        disableFilters();
    }

    private void loadComboBoxData() {
        loadGroupNames();
        loadTeacherNames();
        loadLessonNames();

        CbGroup.setItems(groupNames);
        CbFiltrLessons.setItems(lessonNames);
        CbTeacher.setItems(teacherNames);
    }

    private void loadGroupNames() {
        String sql = "SELECT Name_groups FROM groups";
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                groupNames.add(rs.getString("Name_groups"));
            }

        } catch (SQLException e) {
            System.out.println("Error populating Group ComboBox: " + e.getMessage());
            showAlert("Ошибка при заполнении списка групп.");
        }
    }

    private void loadTeacherNames() {
        String sql = "SELECT FIO FROM teacher";
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                teacherNames.add(rs.getString("FIO"));
            }

        } catch (SQLException e) {
            System.out.println("Error populating Teacher ComboBox: " + e.getMessage());
            showAlert("Ошибка при заполнении списка учителей.");
        }
    }

    private void loadLessonNames() {
        String sql = "SELECT Name_Lessons FROM lessons";
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lessonNames.add(rs.getString("Name_Lessons"));
            }

        } catch (SQLException e) {
            System.out.println("Error populating Lesson ComboBox: " + e.getMessage());
            showAlert("Ошибка при заполнении списка занятий.");
        }
    }

    private ObservableList<ModelTimeTable> getData() {
        ObservableList<ModelTimeTable> data = FXCollections.observableArrayList();
        String sql = "SELECT ID, Date, Time, Id_Lessons, Id_Group, Id_Classroom, Id_Teacher FROM timetable";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                data.add(new ModelTimeTable(
                    Integer.parseInt(rs.getString("ID")),
                    rs.getString("Date"),
                    rs.getString("Time"),
                    rs.getString("Id_Lessons"),
                    rs.getString("Id_Group"),
                    rs.getString("Id_Classroom"),
                    rs.getString("Id_Teacher")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
            showAlert("Ошибка при загрузке данных.");
        }
        return data;
    }

    public void loadTableData() {
        list = getData();
        filteredData = new FilteredList<>(list, p -> true);

        TvTimeTable.setItems(filteredData);

        TcDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        TcTime.setCellValueFactory(new PropertyValueFactory<>("Time"));

        TcLessons.setCellValueFactory(cellData -> {
            String LessonsId = cellData.getValue().getId_Lessons();
            String LessonsName = getLessonsNameId(LessonsId);
            return new SimpleStringProperty(LessonsName);
        });

        TcNomerGroup.setCellValueFactory(cellData -> {
            String GroupId = cellData.getValue().getId_Group();
            String GroupName = getGroupNameId(GroupId);
            return new SimpleStringProperty(GroupName);
        });

        TCNomerClassroom.setCellValueFactory(cellData -> {
            String ClassroomId = cellData.getValue().getId_Classroom();
            String ClassroomName = getClassroomNameId(ClassroomId);
            return new SimpleStringProperty(ClassroomName);
        });

        TcTeacher.setCellValueFactory(cellData -> {
            String TeacherId = cellData.getValue().getId_Teacher();
            String TeacherName = getTeacherNameId(TeacherId);
            return new SimpleStringProperty(TeacherName);
        });
    }

    private String getGroupNameId(String id) {
        String name = "";
        String sql = "SELECT Name_groups FROM groups WHERE ID = ?";
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("Name_groups");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении имени группы: " + e.getMessage());
        }
        return name;
    }

    private String getLessonsNameId(String id) {
        String name = "";
        String sql = "SELECT Name_Lessons FROM lessons WHERE ID = ?";
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("Name_Lessons");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении имени занятия: " + e.getMessage());
        }
        return name;
    }

    private String getClassroomNameId(String id) {
        String name = "";
        String sql = "SELECT namber_room FROM classroom WHERE ID = ?";
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("namber_room");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении имени комнаты: " + e.getMessage());
        }
        return name;
    }

    private String getTeacherNameId(String id) {
        String name = "";
        String sql = "SELECT FIO FROM teacher WHERE ID = ?";
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("FIO");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении имени учителя: " + e.getMessage());
        }
        return name;
    }

    @FXML
    private void Add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLTimetableAdd.fxml"));
        Pane page = (Pane) loader.load();

        Stage duStage = new Stage();
        Scene scene = new Scene(page);
        duStage.setScene(scene);
        duStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/add.png")));
        duStage.setTitle("Добавить запись");
        duStage.setResizable(false);
        duStage.showAndWait();
        loadTableData();
    }

    @FXML
    private void Edt(ActionEvent event) throws IOException {
        ModelTimeTable selectedAdress = TvTimeTable.getSelectionModel().getSelectedItem();

        if (selectedAdress == null) {
            showAlert("Выберите запись для редактирования.");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/Views/FXMLTimetableAdd.fxml"));
        Pane page = (Pane) loader.load();

        TimetableAddController addController = loader.getController();
        addController.setModelTimetableToEdit(selectedAdress);

        Stage duStage = new Stage();
        duStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/img/edit.png")));
        duStage.setTitle("Редактировать запись");
        Scene scene = new Scene(page);
        duStage.setScene(scene);
        duStage.setResizable(false);
        duStage.showAndWait();
        loadTableData();
    }

    @FXML
    private void Delete(ActionEvent event) {
        ModelTimeTable selected = TvTimeTable.getSelectionModel().getSelectedItem();

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
                String sql = "DELETE FROM timetable WHERE ID = ?";
                try (Connection conn = ConnectDB();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, selected.getID());
                    pstmt.executeUpdate();
                    System.out.println("Запись удалена");
                } catch (SQLException e) {
                    System.out.println("Ошибка при удалении записи: " + e.getMessage());
                    showAlert("Ошибка при удалении записи.");
                    return;
                }
                loadTableData();
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
    private void FiltrOfon(ActionEvent event) {
        if (CbFiltrOFOn.isSelected()) {
            enableFilters();
            isFilterEnabled = true;
        } else {
            disableFilters();
            resetFilters();
            isFilterEnabled = false;
        }
    }

    private void enableFilters() {
        DpDateS.setDisable(false);
        DpDatePo.setDisable(false);
        CbFiltrLessons.setDisable(false);
        CbGroup.setDisable(false);
        CbTeacher.setDisable(false);

        DpDateS.setOnAction(this::applyFilters);
        DpDatePo.setOnAction(this::applyFilters);
        CbFiltrLessons.setOnAction(this::applyFilters);
        CbGroup.setOnAction(this::applyFilters);
        CbTeacher.setOnAction(this::applyFilters);

        isFilterEnabled = true;
    }

    private void disableFilters() {
        DpDateS.setDisable(true);
        DpDatePo.setDisable(true);
        CbFiltrLessons.setDisable(true);
        CbGroup.setDisable(true);
        CbTeacher.setDisable(true);

        DpDateS.setOnAction(null);
        DpDatePo.setOnAction(null);
        CbFiltrLessons.setOnAction(null);
        CbGroup.setOnAction(null);
        CbTeacher.setOnAction(null);

        isFilterEnabled = false;
    }

    private void resetFilters() {
        DpDateS.setValue(null);
        DpDatePo.setValue(null);
        CbFiltrLessons.setValue(null);
        CbGroup.setValue(null);
        CbTeacher.setValue(null);

        filteredData.setPredicate(s -> true);
    }

    private void applyFilters(ActionEvent event) {
        if (isFilterEnabled) {
            
            filteredData.setPredicate(timetable -> {
                boolean dateFilter = true;
                boolean groupFilter = true;
                boolean teacherFilter = true;
                boolean lessonFilter = true;

                if (DpDateS.getValue() != null && DpDatePo.getValue() != null) {
                    LocalDate startDate = DpDateS.getValue();
                    LocalDate endDate = DpDatePo.getValue();
                    LocalDate recordDate = LocalDate.parse(timetable.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);

                    dateFilter = !recordDate.isBefore(startDate) && !recordDate.isAfter(endDate);
                } else if (DpDateS.getValue() != null) {
                    LocalDate startDate = DpDateS.getValue();
                    LocalDate recordDate = LocalDate.parse(timetable.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
                    dateFilter = !recordDate.isBefore(startDate);
                } else if (DpDatePo.getValue() != null) {
                    LocalDate endDate = DpDatePo.getValue();
                    LocalDate recordDate = LocalDate.parse(timetable.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
                    dateFilter = !recordDate.isAfter(endDate);
                }

                if (CbGroup.getValue() != null && !CbGroup.getValue().isEmpty()) {
                    groupFilter = getGroupNameId(timetable.getId_Group()).equals(CbGroup.getValue());
                }

                if (CbTeacher.getValue() != null && !CbTeacher.getValue().isEmpty()) {
                    teacherFilter = getTeacherNameId(timetable.getId_Teacher()).equals(CbTeacher.getValue());
                }

                if (CbFiltrLessons.getValue() != null && !CbFiltrLessons.getValue().isEmpty()) {
                    lessonFilter = getLessonsNameId(timetable.getId_Lessons()).equals(CbFiltrLessons.getValue());
                }

                return dateFilter && groupFilter && teacherFilter && lessonFilter;
            });
        }
    }

    @FXML
    private void DateS(ActionEvent event) {
    }

    @FXML
    private void DatePo(ActionEvent event) {
    }

    @FXML
    private void FiltrLessons(ActionEvent event) {
    }

    @FXML
    private void FiltrGroup(ActionEvent event) {
    }

    @FXML
    private void FiltrTeacher(ActionEvent event) {
    }

    @FXML
    private void Print(ActionEvent event) throws JRException {
          Connection conn = ConnectDB();
            JasperDesign jDesign = JRXmlLoader.load(getClass().getResourceAsStream("/org/Reports/TimeTable.jrxml"));
            JasperReport jReport = JasperCompileManager.compileReport(jDesign);
            JasperPrint jPrint = JasperFillManager.fillReport(jReport, null, conn);
            JasperViewer viewer = new JasperViewer(jPrint, false);
            viewer.setTitle("Report timetable");
            viewer.show();  
    }
}

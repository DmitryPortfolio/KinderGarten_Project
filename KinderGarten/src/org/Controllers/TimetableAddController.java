package org.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static org.connector.connector.ConnectDB;
import org.model.ModelTimeTable;
import org.model.ModelLessons; // Import ModelLessons

/**
 * FXML Controller class
 *
 * @author Пользователь
 */
public class TimetableAddController implements Initializable {

    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnCancel;

    private ModelTimeTable timetableToEdit;
    private boolean isEditMode = false;
    @FXML
    private DatePicker dpData;
    @FXML
    private TextField tfTime;
    @FXML
    private ComboBox<String> cbNumGroup;
    @FXML
    private ComboBox<String> cbNumRoom;
    @FXML
    private ComboBox<String> cbTeacher;

    private ObservableList<String> groupNames = FXCollections.observableArrayList();
    private ObservableList<String> classroomNames = FXCollections.observableArrayList();
    private ObservableList<String> teacherNames = FXCollections.observableArrayList();
    private ObservableList<String> lessonNames = FXCollections.observableArrayList();

    private Map<String, String> groupIdMap = new HashMap<>();
    private Map<String, String> classroomIdMap = new HashMap<>();
    private Map<String, String> teacherIdMap = new HashMap<>();
    private Map<String, String> lessonIdMap = new HashMap<>();

    @FXML
    private ComboBox<String> CbLessons;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateComboBoxes();
        dpData.setValue(LocalDate.now());
    }

    private void populateComboBoxes() {
        loadGroupNames();
        loadTeacherNames();
        loadLessonNames();

        cbNumGroup.setItems(groupNames);

        loadRooms();
        cbTeacher.setItems(teacherNames);
        CbLessons.setItems(lessonNames);
    }

    private void loadGroupNames() {
        String sql = "SELECT ID, Name_groups FROM groups";
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("ID");
                String name = rs.getString("Name_groups");
                groupNames.add(name);
                groupIdMap.put(name, id);
            }

        } catch (SQLException e) {
            System.out.println("Error populating Group ComboBox: " + e.getMessage());
            showAlert("Ошибка при заполнении списка групп.");
        }
    }

    private static class RoomInfo {
        private final String id;
        private final String name;
        private final String number;

        public RoomInfo(String id, String name, String number) {
            this.id = id;
            this.name = name;
            this.number = number;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return name + " (" + number + ")";
        }
    }

    private ObservableList<RoomInfo> roomList = FXCollections.observableArrayList();
    private Map<String, String> roomIdMap = new HashMap<>();

    private void loadRooms() {
        String sql = "SELECT ID, Name_room, namber_room FROM classroom";
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("ID");
                String name = rs.getString("Name_room");
                String number = rs.getString("namber_room");
                RoomInfo roomInfo = new RoomInfo(id, name, number);
                roomList.add(roomInfo);
                roomIdMap.put(roomInfo.toString(), id);
            }

            cbNumRoom.setItems(FXCollections.observableArrayList(roomList.stream().map(RoomInfo::toString).toArray(String[]::new)));
        } catch (SQLException e) {
            System.out.println("Error populating Classroom ComboBox: " + e.getMessage());
            showAlert("Ошибка при заполнении списка кабинетов.");
        }
    }

    private void loadTeacherNames() {
        String sql = "SELECT ID, FIO FROM teacher";
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("ID");
                String name = rs.getString("FIO");
                teacherNames.add(name);
                teacherIdMap.put(name, id);
            }

        } catch (SQLException e) {
            System.out.println("Error populating Teacher ComboBox: " + e.getMessage());
            showAlert("Ошибка при заполнении списка учителей.");
        }
    }

    private void loadLessonNames() {
        String sql = "SELECT ID, Name_Lessons FROM lessons";
        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("ID");
                String name = rs.getString("Name_Lessons");
                lessonNames.add(name);
                lessonIdMap.put(name, id);
            }

        } catch (SQLException e) {
            System.out.println("Error populating Lesson ComboBox: " + e.getMessage());
            showAlert("Ошибка при заполнении списка уроков.");
        }
    }

    void setModelTimetableToEdit(ModelTimeTable selectedAdress) {
        this.timetableToEdit = selectedAdress;
        isEditMode = true;

        dpData.setValue(LocalDate.parse(selectedAdress.getDate()));
        tfTime.setText(selectedAdress.getTime());

        String groupId = selectedAdress.getId_Group();
        String classroomId = selectedAdress.getId_Classroom();
        String teacherId = selectedAdress.getId_Teacher();
        String lessonId = selectedAdress.getId_Lessons();

        cbNumGroup.setValue(getGroupName(groupId));

        RoomInfo roomInfo = findRoomInfoById(classroomId);
        if (roomInfo != null) {
            cbNumRoom.setValue(roomInfo.toString());
        } else {
            cbNumRoom.setValue(null);
        }

        cbTeacher.setValue(getTeacherName(teacherId));
        CbLessons.setValue(getLessonName(lessonId)); // Set lesson name
    }

    private String getGroupName(String id) {
        for (Map.Entry<String, String> entry : groupIdMap.entrySet()) {
            if (entry.getValue().equals(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String getClassroomName(String id) {
        for (Map.Entry<String, String> entry : classroomIdMap.entrySet()) {
            if (entry.getValue().equals(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String getTeacherName(String id) {
        for (Map.Entry<String, String> entry : teacherIdMap.entrySet()) {
            if (entry.getValue().equals(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String getLessonName(String id) {
        for (Map.Entry<String, String> entry : lessonIdMap.entrySet()) {
            if (entry.getValue().equals(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private RoomInfo findRoomInfoById(String id) {
        for (RoomInfo roomInfo : roomList) {
            if (roomInfo.getId().equals(id)) {
                return roomInfo;
            }
        }
        return null;
    }

    @FXML
    private void Aadd(ActionEvent event) {
        String date = dpData.getValue().toString();
        String time = tfTime.getText();

        String selectedGroupId = groupIdMap.get(cbNumGroup.getValue());
        String selectedRoom = cbNumRoom.getValue();
        String selectedTeacherId = teacherIdMap.get(cbTeacher.getValue());
        String selectedLessonId = lessonIdMap.get(CbLessons.getValue());

        LocalDate yearDate = dpData.getValue();
        String year = null;
        if (yearDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            year = yearDate.format(formatter);
        }

        if (date.isEmpty() || time.isEmpty() || selectedGroupId == null || selectedRoom == null || selectedTeacherId == null || selectedLessonId == null) {
            showAlert("Пожалуйста, заполните все поля.");
            return;
        }

        String selectedRoomId = roomIdMap.get(selectedRoom);

        if (isEditMode) {
            updateTarif(timetableToEdit.getID(), date, time, selectedLessonId, selectedGroupId, selectedRoomId, selectedTeacherId);
        } else {
            insertTarif(date, time, selectedLessonId, selectedGroupId, selectedRoomId, selectedTeacherId);
        }
        closeWindow(event);
    }

    private void insertTarif(String date, String time, String idLessons, String idGoup, String idRoom, String idTeacher) {
        String sql = "INSERT INTO timetable (`Date`, `Time`, Id_Lessons, Id_Group, Id_Classroom, Id_Teacher) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, date);
            pstmt.setString(2, time);
            pstmt.setString(3, idLessons);
            pstmt.setString(4, idGoup);
            pstmt.setString(5, idRoom);
            pstmt.setString(6, idTeacher);

            pstmt.executeUpdate();
            System.out.println("Тариф добавлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Ошибка при добавлении тарифа.");
        }
    }

    private void updateTarif(int id, String date, String time, String idLessons, String idGoup, String idRoom, String idTeacher) {
        String sql = "UPDATE timetable SET Date = ?, Time = ?, Id_Lessons = ?, Id_Group = ?, Id_Classroom = ?, Id_Teacher = ? WHERE ID = ?";

        try (Connection conn = ConnectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, date);
            pstmt.setString(2, time);
            pstmt.setString(3, idLessons);
            pstmt.setString(4, idGoup);
            pstmt.setString(5, idRoom);
            pstmt.setString(6, idTeacher);
            pstmt.setInt(7, id);

            pstmt.executeUpdate();
            System.out.println("Тариф обновлен");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Ошибка при обновлении тарифа.");
        }
    }

    @FXML
    private void Cancel(ActionEvent event) {
         Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
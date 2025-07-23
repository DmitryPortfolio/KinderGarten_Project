package org.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import static org.connector.connector.ConnectDB;
import org.model.ModelAttendance;
import org.model.ModelChildren;
import org.model.ModelGroups;
import org.model.ModelParents;

/**
 * FXML Controller class
 *
 * @author Пользователь
 */
public class ChilrenEditController implements Initializable {

    private Label LbNomer;

    private ComboBox<String> CbGroup;
    @FXML
    private TableView<ModelParents> TvParents;
    private TableColumn<ModelParents, Integer> TcNomer;
    @FXML
    private TableColumn<ModelParents, String> TcFIO;
    @FXML
    private TableColumn<ModelParents, String> TcTelefon;
    @FXML
    private TableColumn<ModelParents, String> TCAdres;
    @FXML
    private TableColumn<ModelParents, String> TcPassport;
    @FXML
    private TableView<ModelAttendance> TvAttendance;
    @FXML
    private TableColumn<ModelAttendance, String> TcDate;
    @FXML
    private TableColumn<ModelAttendance, String> TcStatus;
    ModelChildren Children;
    ObservableList<ModelGroups> list;
    ObservableList<ModelParents> list1;
    Connection conn;
    private Stage dialogStage;
    @FXML
    private Button BtnExit;
    @FXML
    private Label lbDatePost;
    @FXML
    private Label lbgroups;
    @FXML
    private Label LbFIO;
    @FXML
    private Label lbDateDeparture;
    @FXML
    private Label lbDatebithday;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConnectDB();
       
    }

    public void UpdateTable(int childrenId) {
       
        TcFIO.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        TcTelefon.setCellValueFactory(new PropertyValueFactory<>("Telefon"));
        TCAdres.setCellValueFactory(new PropertyValueFactory<>("Ardres"));
        TcPassport.setCellValueFactory(new PropertyValueFactory<>("Passport"));

        list1 = getParentsForChild(childrenId);
        TvParents.setItems(list1);

        loadAttendanceData(childrenId);
    }

    private ObservableList<ModelParents> getParentsForChild(int childrenId) {
        ObservableList<ModelParents> parentsList = FXCollections.observableArrayList();
        Connection conn = ConnectDB();
        try {
            String query = "SELECT * FROM parents WHERE Id_Children = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, childrenId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ModelParents parent = new ModelParents(
                        resultSet.getInt("ID"),
                        resultSet.getString("FIO"),
                        resultSet.getString("Telefon"),
                        resultSet.getString("Ardres"),
                        resultSet.getString("Passport"),
                        resultSet.getString("Id_Children")
                );
                parentsList.add(parent);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching parents for child: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return parentsList;
    }

    private void loadAttendanceData(int childrenId) {
        ObservableList<ModelAttendance> attendanceList = FXCollections.observableArrayList();
        Connection conn = ConnectDB();
        try {
            String query = "SELECT * FROM attendance WHERE Id_Children = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, childrenId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ModelAttendance attendance = new ModelAttendance(
                        resultSet.getInt("ID"),
                        getFIO (resultSet.getInt("Id_Children")),
                        resultSet.getString("Date"),
                        resultSet.getString("Status")
                );
                attendanceList.add(attendance);
            }

            TcDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
            TcStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
            TvAttendance.setItems(attendanceList);

        } catch (SQLException e) {
            System.err.println("Error fetching attendance data: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
        private String getFIO (int childrenId) {
        Connection conn = ConnectDB();
        String FIO = null;
        try {
            String query = "SELECT FIO FROM children WHERE ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, childrenId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                FIO = resultSet.getString("FIO");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching child name: " + e.getMessage());
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return FIO;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public List<String> getID() {
        conn = ConnectDB();
        List<String> Id = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from children");
            while (rs.next()) {
                Id.add(rs.getString("ID"));
            }
        } catch (SQLException ex) {

        }
        return Id;
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

        }
        return FIO;
    }

    public List<String> getDateBirthday() {
        conn = ConnectDB();
        List<String> Group = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from children");
            while (rs.next()) {
                Group.add(rs.getString("Birthday"));
            }
        } catch (SQLException ex) {

        }
        return Group;
    }

    public Date getDateReceipt() {
        conn = ConnectDB();
        List<String> DateReceipt = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from children");
            while (rs.next()) {
                DateReceipt.add(rs.getString("Date_receipt"));
            }
        } catch (SQLException ex) {

        }
        return (Date) DateReceipt;
    }

    public List<String> getDateDeparture() {
        conn = ConnectDB();
        List<String> Group = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from children");
            while (rs.next()) {
                Group.add(rs.getString("Date_departure"));
            }
        } catch (SQLException ex) {

        }
        return Group;
    }

    public List<String> getGroups() {
        conn = ConnectDB();
        List<String> Group = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from groups");
            while (rs.next()) {
                Group.add(rs.getString("Name_groups"));
            }
        } catch (SQLException ex) {

        }
        return Group;
    }

    public void setChilren(ModelChildren children) {
        this.Children = children;
       

        LbFIO.setText(children.getFIO());

        lbDatebithday.setText(children.getBirthday());
        lbDatePost.setText(children.getDate_receipt());
        lbDateDeparture.setText(children.getDate_departure());
        lbgroups.setText(children.getId_Groups());

        UpdateTable(children.getID());
    }

    @FXML
    private void Exit(ActionEvent event) {
          Stage stage = (Stage) BtnExit.getScene().getWindow();
        stage.close();
    }
}
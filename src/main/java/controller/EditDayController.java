package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import connection.ConnectionManager;
import entity.EntityWorkDay;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.Hours;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
public class EditDayController {


    @FXML
    private Label mainLabel;

    @FXML
    private Label labelFrom;

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXTimePicker fromTimePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXTimePicker toTimePicker;

    @FXML
    private Button addButton;

    @FXML
    private Button beforebutton;


    public  EntityWorkDay entityWorkDay;

    @FXML
    void initialize() {


        System.out.println(fromDatePicker.toString());
        addButton.setOnAction(event -> {
            try {
                editDatabase();
                informationAboutAddingToTheDatabase();
                closeWindow();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(entityWorkDay.toString());
    System.out.println(entityWorkDay.getHourTableDayId() + "dupa");
});
    }

    public void editMethod(EntityWorkDay entityWorkDay) {

        fromDatePicker.setValue(LocalDate.from(entityWorkDay.getHourTableFrom().toLocalDateTime()));
        fromTimePicker.setValue(LocalTime.from(entityWorkDay.getHourTableFrom().toLocalDateTime()));
        toDatePicker.setValue(LocalDate.from(entityWorkDay.getHourTableTo().toLocalDateTime()));
        toTimePicker.setValue(LocalTime.from(entityWorkDay.getHourTableTo().toLocalDateTime()));
        setEntityWorkDay(entityWorkDay);
    }

    private void informationAboutAddingToTheDatabase() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edycja pozycji");
        alert.setHeaderText("Edycja dnia powiodło się!");
        alert.setContentText("Zapis wykoanny poprawnie");
//        odswiezenie bazy danych po zamknięciu allertu

        alert.showAndWait();
    }

    public entity.EntityWorkDay getEntityWorkDay() {
        return entityWorkDay;
    }
//
//    protected static EntityWorkDay editableEntityWorkDay(int id, Timestamp from, Timestamp to, String workHour, String nadgodziny) {
//        entityWorkDay.setHourTableDayId(id);
//        entityWorkDay.setHourTableFrom(from);
//        entityWorkDay.setHourTableTo(to);
//        entityWorkDay.setHourTableNadgodziny(nadgodziny);
//        entityWorkDay.setHourTableWorkHour(workHour);
//        return entityWorkDay;
//    }

    private void editDatabase() throws ParseException {
        Time hourNadgodziny = null;
        Time workHour = null;

        try {
            LocalDateTime fromLocalDateTime = LocalDateTime.of(fromDatePicker.getValue().getYear(), fromDatePicker.getValue().getMonth(), fromDatePicker.getValue().getDayOfMonth(), fromTimePicker.getValue().getHour(), fromTimePicker.getValue().getMinute());
            System.out.println(fromLocalDateTime.toString() + " cipsko");
            LocalDateTime toLocalDateTime = LocalDateTime.of(toDatePicker.getValue().getYear(), toDatePicker.getValue().getMonth(), toDatePicker.getValue().getDayOfMonth(), toTimePicker.getValue().getHour(), toTimePicker.getValue().getMinute());
            System.out.println(toLocalDateTime.toString()+ "kupsko");
            System.out.println(fromLocalDateTime.toString());
            System.out.println(toLocalDateTime.toString());
            Connection connection = ConnectionManager.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(" UPDATE work.workhour SET hourTableFrom = '" + fromLocalDateTime + "', hourTableTo = '" + toLocalDateTime +"' ,hourTableWorkHour = timediff(`hourTableTo`, `hourTableFrom`), hourTableNadgodziny = timediff(`hourTableWorkHour`, '8:00:00') WHERE hourTableDayId = " + entityWorkDay.getHourTableDayId()+"; ");

//            Reprezentuje zestaw wyników bazy danych
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
//            informationAboutFailureToAddToTheDatabase(e, "Nie udało się zakończyć metody dodającej do bazy danych!");
        }
    }
    private void closeWindow() {
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }
}

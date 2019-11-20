package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import entity.EntityWorkDay;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Setter;
import org.joda.time.Hours;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
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


    public static EntityWorkDay entityWorkDay;

    @FXML
    void initialize() {

    }

    public void editMethod(EntityWorkDay entityWorkDay) {
        Timestamp timestampFrom = entityWorkDay.getHourTableFrom();
        Timestamp timestampTo = entityWorkDay.getHourTableTo();

        fromDatePicker.setValue(LocalDate.from(entityWorkDay.getHourTableFrom().toLocalDateTime()));
        fromTimePicker.setValue(LocalTime.from(entityWorkDay.getHourTableFrom().toLocalDateTime()));
        toDatePicker.setValue(LocalDate.from(entityWorkDay.getHourTableTo().toLocalDateTime()));
        toTimePicker.setValue(LocalTime.from(entityWorkDay.getHourTableTo().toLocalDateTime()));
        System.out.println(entityWorkDay.toString());
        System.out.println(IndexController.getEditEntityWorkDay().toString());
//        "UPDATE `work`.`workhour` SET `hourTableFrom` = '" + +"', `hourTableTo` = '" + ds + "', `hourTableWorkHour` = '" + ds + "', `hourTableNadgodziny` = '" + ds + ""
//        ' WHERE (`hourTableDayId` = ' 8 ');

    }

    public void setEntityWorkDay(entity.EntityWorkDay EntityWorkDay) {
        IndexController.EditEntityWorkDay = EntityWorkDay;
    }

    public entity.EntityWorkDay getEntityWorkDay() {
        return entityWorkDay;
    }

    protected static EntityWorkDay editableEntityWorkDay(int id, Timestamp from, Timestamp to, String workHour, String nadgodziny) {
        entityWorkDay.setHourTableDayId(id);
        entityWorkDay.setHourTableFrom(from);
        entityWorkDay.setHourTableTo(to);
        entityWorkDay.setHourTableNadgodziny(nadgodziny);
        entityWorkDay.setHourTableWorkHour(workHour);
        return entityWorkDay;
    }


}

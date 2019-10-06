package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import entity.EntityWorkDay;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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



    public EntityWorkDay EntityWorkDay;

    @FXML
    void initialize() {
    }

    public void editMethod() {
        EntityWorkDay = getEntityWorkDay();
        fromDatePicker.setValue(LocalDate.from(EntityWorkDay.getHourTableFrom().toLocalDateTime()));
        fromTimePicker.setValue(LocalTime.ofSecondOfDay(EntityWorkDay.getHourTableFrom().getTime()));
        toDatePicker.setValue(LocalDate.from(EntityWorkDay.getHourTableTo().toLocalDateTime()));
        toTimePicker.setValue(LocalTime.ofSecondOfDay(EntityWorkDay.getHourTableTo().getTime()));

//        "UPDATE `work`.`workhour` SET `hourTableFrom` = '" + +"', `hourTableTo` = '" + ds + "', `hourTableWorkHour` = '" + ds + "', `hourTableNadgodziny` = '" + ds + ""
//        ' WHERE (`hourTableDayId` = ' 8 ');

    }

    public void setEntityWorkDay(entity.EntityWorkDay EntityWorkDay) {
        IndexController.EditEntityWorkDay = EntityWorkDay;
    }

    public entity.EntityWorkDay getEntityWorkDay() {
        return EntityWorkDay;
    }

}

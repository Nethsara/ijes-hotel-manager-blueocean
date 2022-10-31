package me.siyum.blueoceanhotel.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.siyum.blueoceanhotel.util.Navigation;
import me.siyum.blueoceanhotel.util.Routes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminPageFormController {
    public Circle btnMinimize;
    public Circle btnClose;
    public Label lblDateTime;

    public void initialize(){
        setDateAndTime();
    }
    public void closeOnAction(MouseEvent contextMenuEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void minimizeOnAction(MouseEvent contextMenuEvent) {
        Stage stage = (Stage) btnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    public void manageRoomsOnAction(Event mouseEvent) {
        try {
            Navigation.navigate(Routes.ROOMS, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Loading Error, Please contact system admin");
        }
    }

    public void manageReportsOnAction(Event mouseEvent) {
        try {
            Navigation.navigate(Routes.REPORTS, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Loading Error, Please contact system admin");
        }
    }

    public void manageMealsOnAction(Event mouseEvent) {
        try {
            Navigation.navigate(Routes.MEAL, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Loading Error, Please contact system admin");
        }
    }

    public void roomsManagerOnAction(MouseEvent mouseEvent) {
        manageRoomsOnAction(mouseEvent);
    }

    public void logoutOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.LOGIN, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Loading Error, Please contact system admin");
        }
    }

    public void logAsStaffOnAction(ActionEvent actionEvent) {
        try {
            Navigation.navigate(Routes.STAFF, actionEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Loading Error, Please contact system admin");
        }
    }

    private void setDateAndTime(){
        Timeline time = new Timeline(
                new KeyFrame(Duration.ZERO, e->{
                    DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    lblDateTime.setText(LocalDateTime.now().format(formatter));
                }), new KeyFrame(Duration.seconds(1)));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }
}

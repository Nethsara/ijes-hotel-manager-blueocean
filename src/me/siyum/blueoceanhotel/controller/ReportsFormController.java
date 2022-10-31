package me.siyum.blueoceanhotel.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.siyum.blueoceanhotel.util.Navigation;
import me.siyum.blueoceanhotel.util.Routes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportsFormController {
    public Button btnBackToDash;
    public Circle btnMinimize;
    public Circle btnClose;
    public Label lblDateTime;

    public void initialize(){
        setDateAndTime();
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

    public void closeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void minimizeOnAction(MouseEvent mouseEvent) {
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


    public void manageMealsOnAction(Event mouseEvent) {
        try {
            Navigation.navigate(Routes.MEAL, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Loading Error, Please contact system admin");
        }
    }

    public void adminDashboardOnAction(Event contextMenuEvent) {
        try {
            Navigation.navigate(Routes.ADMIN, contextMenuEvent);
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

}

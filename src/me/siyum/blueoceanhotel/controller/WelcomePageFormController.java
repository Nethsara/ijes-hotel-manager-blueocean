package me.siyum.blueoceanhotel.controller;

import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import me.siyum.blueoceanhotel.util.Navigation;
import me.siyum.blueoceanhotel.util.Routes;

import java.io.IOException;

public class WelcomePageFormController {
    public Circle btnMinimize;
    public Circle btnClose;

    public void startOnAction(Event event) {
        try {
            Navigation.navigate(Routes.LOGIN, event);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, Routes.LOGIN + " not found!");
        }
    }

    public void closeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void minimizeAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }
}

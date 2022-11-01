package me.siyum.blueoceanhotel.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.siyum.blueoceanhotel.util.Navigation;
import me.siyum.blueoceanhotel.util.Routes;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StaffPageFormController {
    public Circle btnMinimize;
    public Circle btnClose;
    public Label lblDateTime;
    public Label lblAvailableRoom;
    public Label lblAvailableReservedRoom;
    public Label lblTotalCustomers;
    public Label lblTotalMeals;

    public void initialize(){
        setDateAndTime();
        setData();
    }

    public void setData(){
        try {
            lblAvailableRoom.setText(RoomsController.getAllAvailableRoomsCount());
            lblAvailableReservedRoom.setText(RoomsController.getRoomReservedCount());
            lblTotalCustomers.setText(CustomerController.getCustomerCount());
            lblTotalMeals.setText(MealsController.getMealCount());
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Data loading error, Please check DB").show();
        }
    }
    public void closeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }


    public void minimizeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    public void customerManagementOnAction(MouseEvent mouseEvent) {try {
        Navigation.navigate(Routes.CUSTOMER, mouseEvent);
    } catch (IOException e) {
        new Alert(Alert.AlertType.ERROR, "Loading issue");
    }

    }

    public void roomsManagerOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.STAFFROOM, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Loading issue");
        }
    }

    public void logoutOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.LOGIN, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Loading Customer").show();
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

    public void makeAReservationOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.RESERVATIONS, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Loading Customer").show();
        }
    }

    public void roomManagerOnAction(MouseEvent mouseEvent) {
        roomsManagerOnAction(mouseEvent);
    }

    public void customerManagerOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.CUSTOMER, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Loading Customer").show();
        }
    }
}

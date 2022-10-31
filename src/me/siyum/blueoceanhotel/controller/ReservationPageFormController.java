package me.siyum.blueoceanhotel.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.siyum.blueoceanhotel.util.Navigation;
import me.siyum.blueoceanhotel.util.Routes;
import me.siyum.blueoceanhotel.view.tm.BookTm;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ReservationPageFormController {
    public TableView<BookTm> tblRoom;
    public TableColumn colID;
    public TableColumn colCustomer;
    public TableColumn colDate;
    public TableColumn colTime;
    public TableColumn colRoom;
    public TableColumn colMeal;
    public TableColumn colPrice;
    public TableColumn colAction;
    public TextField txtSearch;
    public Circle btnMinimize;
    public Circle btnClose;
    public Label lblDateTime;

    private String searchText = "";

    public void initialize() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colRoom.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        colMeal.setCellValueFactory(new PropertyValueFactory<>("mealID"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setDateAndTime();
        searchRooms(searchText);
        txtSearch.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    searchText = newValue;
                    searchRooms(searchText);
                });
    }

    private void setDateAndTime() {
        Timeline time = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    lblDateTime.setText(LocalDateTime.now().format(formatter));
                }), new KeyFrame(Duration.seconds(1)));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    private void searchRooms(String text) {
        String searchText = "%" + text + "%";
        try {

            ObservableList<BookTm> tmList = FXCollections.observableArrayList();
            ResultSet set = ReservationController.searchText(searchText);
            while (set.next()) {
                Button btn;
                if (set.getString(8).equalsIgnoreCase("1")) {
                    btn = new Button("Cancel Reservation");
                    btn.setStyle("-fx-base: #c0392b");
                } else {
                    btn = new Button("Cancelled");
                    btn.setStyle("-fx-base: #c0392b");
                    btn.setDisable(true);
                }
                BookTm tm = new BookTm(
                        set.getString(1),
                        set.getString(2),
                        set.getString(3),
                        set.getString(4),
                        set.getString(5),
                        set.getString(6),
                        set.getDouble(7),
                        btn);
                tmList.add(tm);
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to change the status of this room?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {
                        try {
                            boolean isChangedStatus = ReservationController.changeStatus(tm.getId());
                            System.out.println(isChangedStatus);
                            if (isChangedStatus) {
                                searchRooms(searchText);
                                new Alert(Alert.AlertType.INFORMATION, "Reservation Cancelled!").show();
                            } else {
                                searchRooms(searchText);
                                new Alert(Alert.AlertType.ERROR, "Status updated failed!").show();
                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            new Alert(Alert.AlertType.ERROR, "DB Error, Please contact Admin");

                        }

                    }
                });
            }
            tblRoom.setItems(tmList);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR, "DB Error, Please contact Admin");
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

    public void customerManagementOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.CUSTOMER, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }

    public void roomsManagerOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.STAFFROOM, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }

    public void logoutOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.LOGIN, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }

    public void customerManagerOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.CUSTOMER, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }

    public void roomManagerOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.STAFFROOM, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }

    public void newReservationOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.ROOMRESERVE, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }

    public void staffDashboardOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.STAFF, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }
}

package me.siyum.blueoceanhotel.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.siyum.blueoceanhotel.util.Navigation;
import me.siyum.blueoceanhotel.util.Routes;
import me.siyum.blueoceanhotel.view.tm.RoomTm;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class StaffRoomsFormController {


    public TableView<RoomTm> tblRoom;
    public TableColumn colID;
    public TextField txtSearch;
    public TableColumn colType;
    public TableColumn colPrice;
    public TableColumn colAvailability;
    public TableColumn colReserved;
    public TableColumn colActions;
    public Circle btnClose;
    public Circle btnMinimize;
    public Button btnBackToDash;
    public Label lblDateTime;


    private String searchText = "";

    public void initialize() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("maintenance"));
        colReserved.setCellValueFactory(new PropertyValueFactory<>("reserved"));
        colActions.setCellValueFactory(new PropertyValueFactory<>("btn"));
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

            ObservableList<RoomTm> tmList = FXCollections.observableArrayList();
            ResultSet set = RoomsController.searchText(searchText);
            while (set.next()) {
                Button btn;
                if (!set.getBoolean(4)) {
                    btn = new Button("Mark as Available");
                    btn.setStyle("-fx-base: #2ecc71");
                } else {
                    btn = new Button("Mark as Unavailable");
                    btn.setStyle("-fx-base: #c0392b");
                }
                RoomTm tm = new RoomTm(
                        set.getString(1),
                        set.getString(2),
                        set.getDouble(3),
                        set.getBoolean(4) ? "No" : "Yes",
                        set.getBoolean(5) ? "Yes" : "No",
                        btn);
                tmList.add(tm);
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to change the status of this room?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {
                        try {
                            boolean isChangedStatus = RoomsController.changeStatus(tm.getId());
                            System.out.println(isChangedStatus);
                            if (isChangedStatus) {
                                searchRooms(searchText);
                                new Alert(Alert.AlertType.INFORMATION, "Status updated success!").show();
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

    public void closeOnAction(Event mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void minimizeOnAction(Event mouseEvent) {
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

    public void makeAReservationOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.RESERVATIONS, mouseEvent);
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

    public void customerManagerOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.CUSTOMER, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }
}

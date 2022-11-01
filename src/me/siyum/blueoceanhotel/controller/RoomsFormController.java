package me.siyum.blueoceanhotel.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.siyum.blueoceanhotel.model.Room;
import me.siyum.blueoceanhotel.util.Navigation;
import me.siyum.blueoceanhotel.util.Routes;
import me.siyum.blueoceanhotel.view.tm.RoomTm;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class RoomsFormController {
    public TextField txtRoomID;
    public JFXRadioButton radionAv;
    public JFXRadioButton radioNA;
    public TextField txtPrice;
    public TextField txtSearch;
    public TableColumn colID;
    public TableColumn colType;
    public TableColumn colPrice;
    public TableColumn colAvailability;
    public TableColumn colActions;
    public Circle btnClose;
    public Circle btnMinimize;
    public JFXButton btnSaveRoom;
    public TableView<RoomTm> tblRoom;
    public TableColumn colReserved;
    public JFXRadioButton radionReserved;
    public JFXRadioButton radionNotReserved;
    public JFXComboBox cmbRoomType;
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
        loadRoomTypes();
        searchRooms(searchText);
        txtRoomID.setText(generateID());
        tblRoom.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (null != newValue) {// newValue!=null
                        setData(newValue);
                    }
                });
        txtSearch.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    searchText = newValue;
                    searchRooms(searchText);
                });
    }

    private String generateID() {
        ResultSet set = null;
        try {
            set = RoomsController.getLastID();
            if (set.next()) {
                String tempOrderId = set.getString(1);
                String[] array = tempOrderId.split("-");//[D,3]
                int tempNumber = Integer.parseInt(array[1]);
                int finalizeOrderId = tempNumber + 1;
                return String.format("R-%03d", finalizeOrderId);
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Loading Error, Please contact system admin");
        }
        return "R-001";
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

    private void setData(RoomTm tm) {
        txtRoomID.setText(tm.getId());
        txtPrice.setText(String.valueOf(tm.getPrice()));
        cmbRoomType.setValue(tm.getType());
        if (tm.isMaintenance().equalsIgnoreCase("Yes")) {
            radionAv.setSelected(true);
        } else {
            radioNA.setSelected(true);
        }

        if (tm.isReserved().equalsIgnoreCase("Yes")) {
            radionReserved.setSelected(true);
        } else {
            radionNotReserved.setSelected(true);
        }
        btnSaveRoom.setText("Update Room");
    }

    private void searchRooms(String text) {
        String searchText = "%" + text + "%";
        try {

            ObservableList<RoomTm> tmList = FXCollections.observableArrayList();
            ResultSet set = RoomsController.searchText(searchText);
            while (set.next()) {
                Button btn = new Button("❌");
                btn.setStyle("-fx-base: #c0392b; -fx-border-radius: 15; -fx-background-radius: 15;");
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
                            "are you sure whether do you want to delete this Room?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {

                        try {
                            boolean isDeleted = RoomsController.deleteRoom(tm.getId());
                            if (isDeleted) {
                                searchRooms(searchText);
                                new Alert(Alert.AlertType.INFORMATION, "Room Deleted!").show();
                            } else {
                                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
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

    public void saveRoomOnAction(ActionEvent actionEvent) {
        Room r = new Room(
                txtRoomID.getText(),
                (String) cmbRoomType.getValue(),
                Double.parseDouble(txtPrice.getText()),
                radionAv.isSelected(),
                radionReserved.isSelected()
        );

        if (btnSaveRoom.getText().equalsIgnoreCase("Save Room")) {
            try {
                boolean isSavedRoom = RoomsController.saveRoom(r);
                if (isSavedRoom) {
                    searchRooms(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Room Saved!").show();
                    txtRoomID.setText(generateID());
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR, "DB Error, Please contact Admin");
            }
        } else {
            try {
                boolean isUpdatedRoom = RoomsController.updateRoom(r);
                if (isUpdatedRoom) {
                    searchRooms(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Room Updated!").show();
                    txtRoomID.setText(generateID());
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again! Update Error").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR, "DB Error, Please contact Admin");
            }
        }
    }

    private void clearFields() {
        txtRoomID.clear();
        txtPrice.clear();
        radioNA.setSelected(false);
        radionAv.setSelected(false);
        radionReserved.setSelected(false);
        radionAv.setSelected(false);
        radionNotReserved.setSelected(false);
        cmbRoomType.getSelectionModel().clearSelection();
    }

    public void closeOnAction(Event mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void minimizeOnAction(Event mouseEvent) {
        Stage stage = (Stage) btnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    public void loadRoomTypes() {
        try {
            ObservableList<String> allRooms = RoomsController.getAllRooms();
            cmbRoomType.setItems(allRooms);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "DB Error, Please contact Admin");
        }
    }

    public void newRoomOnAction(ActionEvent actionEvent) {
        btnSaveRoom.setText("Save Room");
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

    public void adminDashboardOnAction(MouseEvent contextMenuEvent) {
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


    public void mealsManagerOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.MEAL, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Loading Error, Please contact system admin");
        }
    }
}


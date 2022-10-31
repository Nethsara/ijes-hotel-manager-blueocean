package me.siyum.blueoceanhotel.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.siyum.blueoceanhotel.model.Meal;
import me.siyum.blueoceanhotel.util.Navigation;
import me.siyum.blueoceanhotel.util.Routes;
import me.siyum.blueoceanhotel.view.tm.MealTm;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class MealsFormPageController {
    public Circle btnClose;
    public Circle btnMinimize;
    public Button btnBackToDash;
    public TextField txtPrice;
    public TextField txtSearch;
    public TableColumn colType;
    public TableColumn colPrice;
    public TableColumn colActions;
    public JFXComboBox<String> cmbMealTypes;
    public TextField txtMealID;
    public TextField txtMealName;
    public TableView<MealTm> tblMeals;
    public TableColumn colMealID;
    public JFXButton btnSaveMeal;
    public Label lblDateTime;

    private String searchText = "";

    public void initialize() {
        colMealID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colActions.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setDateAndTime();
        searchMeals(searchText);
        tblMeals.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (null != newValue) {// newValue!=null
                        setData(newValue);
                    }
                });
        txtSearch.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    searchText = newValue;
                    searchMeals(searchText);
                });

        txtMealID.setText(generateID());
    }

    private String generateID() {
        ResultSet set = null;
        try {
            set = MealsController.getLastID();
            if (set.next()) {
                String tempOrderId = set.getString(1);
                String[] array = tempOrderId.split("-");//[D,3]
                int tempNumber = Integer.parseInt(array[1]);
                int finalizeOrderId = tempNumber + 1;
                return String.format("M-%03d", finalizeOrderId);
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Loading Error, Please contact system admin");
        }
        return "M-001";
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

    private void setData(MealTm tm) {
        txtMealID.setText(tm.getId());
        txtPrice.setText(String.valueOf(tm.getPrice()));
        txtMealName.setText(tm.getType());
        btnSaveMeal.setText("Update Meal");
    }

    public void saveRoomOnAction(ActionEvent actionEvent) {

        Meal r = new Meal(
                txtMealID.getText(),
                Double.parseDouble(txtPrice.getText()),
                txtMealName.getText()
        );

        if (btnSaveMeal.getText().equalsIgnoreCase("Save Meal")) {
            try {
                boolean isSavedMeal = MealsController.saveMeal(r);
                if (isSavedMeal) {
                    searchMeals(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Meal Saved!").show();
                    txtMealID.setText(generateID());

                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                boolean isUpdatedRoom = MealsController.updateMeal(r);
                if (isUpdatedRoom) {
                    searchMeals(searchText);
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Meal Updated!").show();
                    txtMealID.setText(generateID());
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again! Update Error").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR, "DB Loading Error, Please contact Admin");
            }
        }
    }

    private void searchMeals(String text) {
        String searchText = "%" + text + "%";
        try {

            ObservableList<MealTm> tmList = FXCollections.observableArrayList();
            ResultSet set = MealsController.searchText(searchText);
            while (set.next()) {
                Button btn = new Button("❌");
                MealTm tm = new MealTm(
                        set.getString("id"),
                        set.getString("type"),
                        set.getDouble("price"),
                        btn
                );
                tmList.add(tm);
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "are you sure whether do you want to delete this Meal?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {
                        try {
                            boolean isDeleted = MealsController.deleteMeal(tm.getId());
                            if (isDeleted) {
                                searchMeals(searchText);
                                new Alert(Alert.AlertType.INFORMATION, "Meal Deleted!").show();
                            } else {
                                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            new Alert(Alert.AlertType.ERROR, "DB Loading Error, Please contact Admin");
                        }
                    }
                });
            }
            tblMeals.setItems(tmList);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR, "DB Error, Please contact Admin");
        }
    }

    private void clearFields() {
        txtMealName.clear();
        txtMealID.clear();
        txtPrice.clear();
    }

    public void newMealOnAction(ActionEvent actionEvent) {
        btnSaveMeal.setText("Save Meal");
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

    public void logAsStaffOnAction(ActionEvent actionEvent) {
        try {
            Navigation.navigate(Routes.STAFF, actionEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Loading Error, Please contact system admin");
        }
    }

}

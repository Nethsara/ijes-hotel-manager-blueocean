package me.siyum.blueoceanhotel.controller;

import com.jfoenix.controls.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.siyum.blueoceanhotel.model.Book;
import me.siyum.blueoceanhotel.util.CRUDUtil;
import me.siyum.blueoceanhotel.util.Navigation;
import me.siyum.blueoceanhotel.util.Routes;
import me.siyum.blueoceanhotel.view.tm.RoomTm;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class BookingPageFormController {
    public AnchorPane paneTbl;
    public TableColumn colID;
    public TableView tblReserve;
    public TableColumn colType;
    public TableColumn colPrice;
    public TableColumn colActions;
    public AnchorPane paneOptions;
    public JFXButton btnSelectMeals;
    public AnchorPane paneMeals;
    public JFXButton btnFinalize;
    public AnchorPane paneFinalPage;
    public TextField txtSearch;
    public JFXTextField txtDays;
    public JFXComboBox cmbRoomID;
    public JFXTextField txtType;
    public JFXTextField txtPricePerNight;
    public JFXToggleButton tglBtnMealSelect;
    public JFXComboBox cmbMealSelect;
    public JFXTextField txtMealPrice;
    public JFXTextField txtMealName;
    public JFXTextField txtFRoomID;
    public JFXTextField txtFRoomType;
    public JFXTextField txtFRoomPrice;
    public JFXTextField txtFDays;
    public JFXTextField txtFMealID;
    public JFXTextField txtFMealPrice;
    public JFXButton btnFinish;
    public JFXButton btnCancel;
    public JFXTextField txtFMealType;
    public Label lblTotal;
    public JFXComboBox cmbCust;
    public AnchorPane panePrint;
    public JFXButton btnPrint;
    public JFXTextField txtPRoomID;
    public JFXTextField txtPRoomType;
    public JFXTextField txtPRoomPrice;
    public JFXTextField txtPMealID;
    public JFXTextField txtPDays;
    public JFXTextField txtPMealPrice;
    public JFXTextField txtPMealType;
    public Label lblPrintPage;
    public ImageView imgStep1;
    public ImageView imgStep2;
    public ImageView imgStep3;
    public ImageView imgStep4;
    public Circle btnClose;
    public Circle btnMinimize;
    public Line line1;
    public Line line2;
    public Line line3;
    public Label lblDateandTime;
    public JFXCheckBox chkMeals;
    public JFXTextField txtFCustID;

    private String searchText = "";
    private RoomTm rt;

    public void initialize() {
        stepOne();
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colActions.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setDateAndTime();
        searchRooms(searchText);
        txtSearch.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    searchText = newValue;
                    searchRooms(searchText);
                });
    }

    private void searchRooms(String text) {
        String searchText = "%" + text + "%";
        try {
            ObservableList<RoomTm> tmList = FXCollections.observableArrayList();
            ResultSet set = RoomsController.searchText(searchText);
            while (set.next()) {
                Button btn = new Button("Room Unavailable");
                btn.setDisable(true);
                btn.setStyle("-fx-base: #c0392b; -fx-border-radius: 15; -fx-background-radius: 15;");

                if (set.getBoolean("status") && !set.getBoolean("reserved")) {
                    btn.setText("Book");
                    btn.setDisable(false);
                    btn.setStyle("-fx-base: #2980b9; -fx-border-radius: 15; -fx-background-radius: 15;");

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
                            "Want to Book Room : " + tm.getId() + " ?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {
                        if (btn.getText().equalsIgnoreCase("Book")) {
                            stepTwo(tm);
                            rt = tm;
                            btnSelectMeals.setOnAction(e -> {
                                stepThree();
                                btnFinalize.setOnAction(ev -> {
                                    stepFour();
                                    if (txtFCustID.getText().isEmpty() ||
                                            txtFDays.getText().isEmpty() ||
                                            txtFRoomID.getText().isEmpty()) {
                                        new Alert(Alert.AlertType.ERROR, "Please fill all details").show();
                                        return;
                                    }
                                    try {
                                        double total = Double.parseDouble(txtPricePerNight.getText()) *
                                                Double.parseDouble(txtDays.getText()) +
                                                Double.parseDouble(txtFMealPrice.getText().isEmpty() ?
                                                        String.valueOf(0) : txtFMealPrice.getText());
                                        lblTotal.setText(String.valueOf(total));
                                    } catch (NumberFormatException eq) {}
                                    btnFinish.setOnAction(evnt -> {
                                        Alert alertE = new Alert(Alert.AlertType.CONFIRMATION,
                                                "are you sure whether do you want to place this Order?",
                                                ButtonType.YES, ButtonType.NO);
                                        Optional<ButtonType> bt = alertE.showAndWait();
                                        if (bt.get() == ButtonType.NO) {
                                            return;
                                        }
                                        Book b = new Book(
                                                generateBookingID(),
                                                txtFRoomID.getText(),
                                                String.valueOf(java.time.LocalDate.now()),
                                                String.valueOf(java.time.LocalTime.now()),
                                                txtFRoomID.getText(),
                                                txtFMealID.getText(),
                                                Double.parseDouble(txtPricePerNight.getText()) + Double.parseDouble(txtFMealPrice.getText())
                                        );

                                        try {
                                            boolean isSavedBooking = RoomsReservationController.saveBooking(b);
                                            if (isSavedBooking) {
                                                RoomsController.changeReservationStatus(b.getRoomID());
                                                new Alert(Alert.AlertType.INFORMATION, "Placed SuccessFully!").showAndWait();
                                                preparePrintPage();
                                                panePrint.setVisible(true);
                                                searchRooms(searchText);
                                            } else {
                                                new Alert(Alert.AlertType.ERROR, "Error Placing!").show();
                                            }
                                        } catch (SQLException | ClassNotFoundException ex) {
                                            ex.printStackTrace();
                                        }
                                    });
                                });
                            });
                        }
                    }

                });
            }
            tblReserve.setItems(tmList);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error Connecting DB, Please contact system admin");
        }
    }

    public void stepOne() {
        line1.setOpacity(0.2);
        line2.setOpacity(0.2);
        line3.setOpacity(0.2);

        Image img = new Image("me/siyum/blueoceanhotel/assets/step1.gif");
        imgStep1.setImage(img);
        panePrint.setVisible(false);
        paneMeals.setVisible(false);
        paneFinalPage.setVisible(false);
        paneOptions.setVisible(false);
        paneTbl.setVisible(true);
    }

    public void stepTwo(RoomTm r) {
        line1.setOpacity(1);
        Image img = new Image("me/siyum/blueoceanhotel/assets/step1.png");
        imgStep1.setImage(img);

        Image img2 = new Image("me/siyum/blueoceanhotel/assets/step2.gif");
        imgStep2.setImage(img2);
        panePrint.setVisible(false);
        paneMeals.setVisible(false);
        paneFinalPage.setVisible(false);
        paneOptions.setVisible(true);
        paneTbl.setVisible(false);

        loadRooms();
        loadCustomers();
        cmbRoomID.setValue(r.getId());
        txtType.setText(r.getType());
        txtPricePerNight.setText(String.valueOf(r.getPrice()));

        cmbRoomID.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setRoomDetails();
            }
        }));
    }

    private void stepThree() {
        line2.setOpacity(1);
        Image img = new Image("me/siyum/blueoceanhotel/assets/step2.png");
        imgStep2.setImage(img);

        Image img2 = new Image("me/siyum/blueoceanhotel/assets/step3.gif");
        imgStep3.setImage(img2);
        panePrint.setVisible(false);
        paneMeals.setVisible(true);
        paneFinalPage.setVisible(false);
        paneOptions.setVisible(false);
        paneTbl.setVisible(false);

        cmbMealSelect.setDisable(true);
        txtMealName.setDisable(true);
        txtMealPrice.setDisable(true);
        chkMeals.setSelected(false);
        cmbMealSelect.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setMealDetails();
            }
        }));
    }

    private void stepFour() {
        line3.setOpacity(1);
        Image img = new Image("me/siyum/blueoceanhotel/assets/step3.png");
        imgStep3.setImage(img);

        Image img2 = new Image("me/siyum/blueoceanhotel/assets/step4.gif");
        imgStep4.setImage(img2);

        panePrint.setVisible(false);
        paneMeals.setVisible(false);
        paneFinalPage.setVisible(true);
        paneOptions.setVisible(false);
        paneTbl.setVisible(false);

        txtFDays.setText(txtDays.getText());
        txtFRoomID.setText(String.valueOf(cmbRoomID.getValue()));
        txtFMealID.setText(String.valueOf(cmbMealSelect.getValue()));
        txtFRoomPrice.setText(txtPricePerNight.getText());
        txtFRoomType.setText(txtType.getText());
        txtFMealPrice.setText(txtMealPrice.getText());
        txtFMealType.setText(txtMealName.getText());
        txtFCustID.setText(String.valueOf(cmbCust.getValue()));
    }

    private void setMealDetails() {
        try {
            ResultSet res = CRUDUtil.execute("SELECT * FROM meals WHERE id =?",
                    cmbMealSelect.getValue());
            if (res.next()) {
                txtMealPrice.setText(res.getString(2));
                txtMealName.setText(res.getString(3));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error on DB, Please contact system admin");
        }
    }

    private void loadMeals() {
        try {
            ObservableList<String> allMeals = MealsController.getAllMeals();
            cmbMealSelect.setItems(allMeals);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Meals Loading Error, Please contact system admin");

        }
    }

    private void loadCustomers() {
        try {
            ObservableList<String> allCust = CustomerController.getAllAvailableRooms();
            cmbCust.setItems(allCust);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Customers Loading Error, Please contact system admin");

        }
    }

    private void setRoomDetails() {
        try {
            ResultSet res = CRUDUtil.execute("SELECT * FROM rooms WHERE id =?",
                    cmbRoomID.getValue());
            if (res.next()) {
                txtType.setText(res.getString(2));
                txtPricePerNight.setText(res.getString(3));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Rooms Loading Error, Please contact system admin");
        }
    }

    private void loadRooms() {
        try {
            ObservableList<String> allRooms = RoomsController.getAllAvailableRooms();
            cmbRoomID.setItems(allRooms);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Rooms Loading Error, Please contact system admin");
        }
    }

    public String generateBookingID() {
        ResultSet set = null;
        try {
            set = RoomsReservationController.getLastID();

            if (set.next()) {
                String tempOrderId = set.getString(1);
                System.out.println(tempOrderId);
                String[] array = tempOrderId.split("-");//[D,3]
                int tempNumber = Integer.parseInt(array[1]);
                int finalizeOrderId = tempNumber + 1;
                return String.format("RR-%03d", finalizeOrderId);
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "DB Loading Error, Please contact system admin");
        }
        return "RR-001";
    }

    public void preparePrintPage() {
        txtPDays.setText(txtDays.getText());
        txtPRoomID.setText(String.valueOf(cmbRoomID.getValue()));
        txtPMealID.setText(String.valueOf(cmbMealSelect.getValue()));
        txtPRoomPrice.setText(txtPricePerNight.getText());
        txtPRoomType.setText(txtType.getText());
        txtPMealPrice.setText(txtMealPrice.getText());
        txtPMealType.setText(txtMealName.getText());
    }

    public void printOnAction(ActionEvent actionEvent) {
        stepOne();
    }

    public void cancelOnAction(ActionEvent actionEvent) {
        stepOne();
    }

    public void backToStepOneOnAction(ActionEvent actionEvent) {
        stepTwo(rt);
    }

    public void closeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void minimizeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    private void setDateAndTime() {
        Timeline time = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    lblDateandTime.setText(LocalDateTime.now().format(formatter));
                }), new KeyFrame(Duration.seconds(1)));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
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


    public void roomManagerOnAction(MouseEvent mouseEvent) {
        try {
            Navigation.navigate(Routes.STAFFROOM, mouseEvent);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading UI");
        }
    }

    public void stateChanged(ActionEvent actionEvent) {
        boolean selected = chkMeals.isSelected();
        System.out.println();
        if (selected) {
            loadMeals();
            cmbMealSelect.setDisable(false);
            txtMealName.setDisable(false);
            txtMealPrice.setDisable(false);
        } else {
            cmbMealSelect.setDisable(true);
            txtMealName.setDisable(true);
            txtMealPrice.setDisable(true);
        }
    }

    public void backToStep2(ActionEvent actionEvent) {
        stepThree();
    }
}

package me.siyum.blueoceanhotel.util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigation {
    private static Stage stage;

    public static void navigate(Routes routes, Event event) throws IOException {
        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();
        stage.close();
        switch (routes) {
            case ROOMS:
                initUI("RoomsPageForm");
                break;
            case LOGIN:
                initUI("LoginPageForm");
                break;
            case ADMIN:
                initUI("AdminPageForm");
                break;
            case STAFF:
                initUI("StaffPageForm");
                break;
            case REPORTS:
                initUI("ReportsPageForm");
                break;
            case STAFFROOM:
                initUI("StaffRoomsPageForm");
                break;
            case CUSTOMER:
                initUI("CustomerManagerPageForm");
                break;
            case RESERVATIONS:
                initUI("ReservationsPageForm");
                break;
            case ROOMRESERVE:
                initUI("BookingPageForm");
                break;
            case MEAL:
                initUI("MealsPageForm");
                break;
            default:initUI("WelcomePageForm");
        }
    }

    public static void initUI(String location) throws IOException {
        Scene scene = null;
        scene = new Scene(FXMLLoader.load(Navigation.class.getResource("../view/" + location + ".fxml")));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}

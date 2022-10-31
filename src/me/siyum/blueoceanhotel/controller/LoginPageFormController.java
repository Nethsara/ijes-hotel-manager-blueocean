package me.siyum.blueoceanhotel.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import me.siyum.blueoceanhotel.util.Navigation;
import me.siyum.blueoceanhotel.util.Routes;

import java.io.IOException;
import java.sql.SQLException;

public class LoginPageFormController {
    public Circle btnClose;
    public JFXTextField txtEmail;
    public JFXPasswordField txtPassword;
    public Label passwordStatus;
    public Circle btnMinimize;

    public void loginOnAction(Event event) {
        try {
            int loginStatus = LoginController.login(txtEmail.getText(), txtPassword.getText());
            if (loginStatus == -1) {
                passwordStatus.setText("Username or Password Incorrect!");
            } else if (loginStatus == 1) {
                Navigation.navigate(Routes.ADMIN, event);
            } else {
                Navigation.navigate(Routes.STAFF, event);
            }
        } catch (SQLException e) {
            passwordStatus.setText("Username or Password Incorrect!");
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class Not Found");
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Unknown Error, Please contact Admin");
        }
    }

    public void minimizeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    public void closeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}

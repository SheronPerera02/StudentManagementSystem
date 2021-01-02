package lk.ijse.studentManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.studentManagement.dto.AdminAccountDTO;
import lk.ijse.studentManagement.model.AdminAccountTM;
import lk.ijse.studentManagement.service.AdminAccountService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class
AdminAccountController implements Initializable {
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public JFXPasswordField txtVerifyPassword;

    public JFXButton btnAddOrUpdate;
    public JFXButton btnCancel;
    public JFXButton btnDelete;

    public TableView<AdminAccountTM> tblAdminAccount;

    public TableColumn<AdminAccountTM, String> colId;
    public TableColumn<AdminAccountTM, String> colUsername;
    public TableColumn<AdminAccountTM, String> colPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        loadAllAccounts();
        tblAdminAccount.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AdminAccountTM>() {
            @Override
            public void changed(ObservableValue<? extends AdminAccountTM> observable, AdminAccountTM oldValue, AdminAccountTM newValue) {
                AdminAccountTM selectedItem = tblAdminAccount.getSelectionModel().getSelectedItem();
                int selectedIndex = tblAdminAccount.getSelectionModel().getSelectedIndex();
                if (selectedItem != null) {
                    if (selectedIndex == 0) {
                        txtUserName.setDisable(true);
                        btnDelete.setVisible(false);
                        btnDelete.setDisable(true);
                    } else {
                        txtUserName.setDisable(false);
                        btnDelete.setVisible(true);
                        btnDelete.setDisable(false);
                    }
                    txtUserName.setText(selectedItem.getUsername());
                    btnAddOrUpdate.setText("Update");
                    btnAddOrUpdate.setStyle("-fx-background-color: #28a745");
                }
            }
        });
    }

    private void loadAllAccounts() {
        ArrayList<AdminAccountTM> allAdminAccounts = AdminAccountService.getAllAdminAccounts();
        ObservableList<AdminAccountTM> items = tblAdminAccount.getItems();
        if (allAdminAccounts != null) {
            items.addAll(allAdminAccounts);
        }
    }


    public void btnCancelOnAction(ActionEvent actionEvent) {
        tblAdminAccount.getSelectionModel().clearSelection();
        btnAddOrUpdate.setText("Add");
        btnAddOrUpdate.setStyle("-fx-background-color: #17a2b8");
        txtUserName.clear();
        txtPassword.clear();
        txtVerifyPassword.clear();
        txtUserName.setDisable(false);
        btnDelete.setVisible(false);
        btnDelete.setDisable(true);
        txtUserName.requestFocus();
    }

    public void btnAddOrUpdateOnAction(ActionEvent actionEvent) {
        if (btnAddOrUpdate.getText().contentEquals("Add")) {
            addAccount();
        } else {
            updateAccount();
        }
    }

    private void updateAccount() {
        Alert alert;
        if (verifyDetails()) {
            AdminAccountDTO dto = new AdminAccountDTO(
                    tblAdminAccount.getSelectionModel().getSelectedItem().getId(),
                    txtUserName.getText(),
                    txtPassword.getText()
            );
            boolean accountIsUpdated = AdminAccountService.updateAccount(dto);
            if (accountIsUpdated) {
                ObservableList<AdminAccountTM> items = tblAdminAccount.getItems();
                items.set(tblAdminAccount.getSelectionModel().getSelectedIndex(), new AdminAccountTM(
                        dto.getId(),
                        dto.getUsername(),
                        dto.getPassword()
                ));
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Account Updated\nSuccessfully!");
                alert.showAndWait();
            }
        }
    }

    private boolean verifyDetails() {
        Alert alert;
        if (
                txtUserName.getText().contentEquals("")
                        ||
                        txtPassword.getText().contentEquals("")
                        ||
                        txtVerifyPassword.getText().contentEquals("")
        ) {

            alert = new Alert(Alert.AlertType.INFORMATION, "Complete The Form!");
            alert.showAndWait();

        } else if (
                AdminAccountService.userNameNotEligible(txtUserName.getText())
                        &&
                        !txtUserName.getText().contentEquals("Main")
        ) {

            alert = new Alert(Alert.AlertType.INFORMATION, "Username Should Be Between 6-25 Characters!");
            alert.showAndWait();

        } else if (
                btnAddOrUpdate.getText().contentEquals("Update")
                        &&
                        AdminAccountService.userNameExists(txtUserName.getText())
                        &&
                        !tblAdminAccount.getSelectionModel().getSelectedItem().getUsername().contentEquals(txtUserName.getText())
        ) {

            alert = new Alert(Alert.AlertType.INFORMATION, "Username Already Taken!");
            alert.showAndWait();
            txtUserName.clear();

        } else if (
                btnAddOrUpdate.getText().contentEquals("Add")
                        &&
                        AdminAccountService.userNameExists(txtUserName.getText())
        ) {
            alert = new Alert(Alert.AlertType.INFORMATION, "Username Already Taken!");
            alert.showAndWait();
            txtUserName.clear();
        } else if (AdminAccountService.passwordsDoesNotMatch(txtPassword.getText(), txtVerifyPassword.getText())) {

            alert = new Alert(Alert.AlertType.INFORMATION, "Passwords Doesn't Match!");
            alert.showAndWait();
            txtPassword.clear();
            txtVerifyPassword.clear();

        } else {
            return true;
        }
        return false;
    }


    private void addAccount() {
        Alert alert;
        if (verifyDetails()) {
            String nextID = AdminAccountService.getNextID();
            if (nextID == null) nextID = "Log1";
            AdminAccountDTO dto = new AdminAccountDTO(
                    nextID,
                    txtUserName.getText(),
                    txtPassword.getText()
            );
            boolean accountIsAdded = AdminAccountService.addAccount(dto);
            if (accountIsAdded) {
                ObservableList<AdminAccountTM> items = tblAdminAccount.getItems();
                items.add(new AdminAccountTM(
                        dto.getId(),
                        dto.getUsername(),
                        dto.getPassword()
                ));
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Account added\nSuccessfully!");
                alert.showAndWait();
            }
        }
    }

    public void txtUserNameOnAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        txtVerifyPassword.requestFocus();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert;
        AdminAccountTM selectedItem = tblAdminAccount.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            boolean accountIdDeleted = AdminAccountService.deleteAccount(selectedItem.getId());
            if (accountIdDeleted) {
                ObservableList<AdminAccountTM> items = tblAdminAccount.getItems();
                items.remove(tblAdminAccount.getSelectionModel().getSelectedIndex());
                btnCancel.fire();
                alert = new Alert(Alert.AlertType.INFORMATION, "Account Deleted\nSuccessfully!");
                alert.showAndWait();
            }
        }
    }
}

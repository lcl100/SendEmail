package com.mycom.myapp.mailsendsystem.controller;

import com.mycom.myapp.mailsendsystem.tools.SimpleTools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SendAccountOptionFrameController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TextField addresserTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField serverTextField;

    @FXML
    private Button saveButton;

    public void initialize() {
        // 初始化图标设置
        new SimpleTools().setLabeledImage(
                new Labeled[]{saveButton, cancelButton},
                new String[]{"src/main/resources/images/save.png",
                        "src/main/resources/images/cancel.png"});
        // 从data.properties文件中读取配置
        Map dataMap = new SimpleTools().readReturnMap("src/main/resources/properties/data.properties");
        String addresser = (String) dataMap.get("addresser");
        String password = (String) dataMap.get("password");
        String server = (String) dataMap.get("server");
        addresserTextField.setText(addresser);
        passwordField.setText(password);
        serverTextField.setText(server);
    }

    @FXML
    void do_saveButton_event(ActionEvent event) {
        // 获取发件人邮箱
        String addresser = addresserTextField.getText();
        // 发件人登录密码
        String password = passwordField.getText();
        // 服务器，这里使用的是smtp.163.com，是网易邮箱的第三方服务器
        String server = serverTextField.getText();
        // 实例化一个Map
        Map dataMap = new HashMap();
        // 将键值保存在Map中
        dataMap.put("addresser", addresser);
        dataMap.put("password", password);
        dataMap.put("server", server);
        // 调用SimpleTools的dataWriteProperties()方法将输入写入properties文件中
        new SimpleTools().dataWriteProperties("src/main/resources/properties/data.properties", dataMap);
    }

    @FXML
    void do_cancelButton_event(ActionEvent event) {
        // 退出当前设置界面
        stage.close();
    }

}

package com.mycom.myapp.mailsendsystem;

import com.mycom.myapp.mailsendsystem.controller.SendAccountOptionFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // 设置标题
        this.primaryStage.setTitle("邮件发送器");
        // 在程序启动的时候加载主界面
        initMainFrame();
    }

    /**
     * 加载主界面
     */
    public void initMainFrame() {
        try {
            // 加载FXML界面文件
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/MainFrame.fxml"));
            Parent root = loader.load();

            // 实例化场景
            Scene scene = new Scene(root);
            // 将场景设置到舞台
            primaryStage.setScene(scene);

            // 展示舞台
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载设置界面
     */
    public void initSendAccountOptionFrame(){
        try {
            // 加载FXML文件
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/SendAccountOptionFrame.fxml"));
            Parent root = loader.load();

            // 实例化舞台
            Stage stage = new Stage();
            // 设置标题
            stage.setTitle("设置");
            // 设置该界面不可缩放
            stage.setResizable(true);
            // 设置该界面总是处于最顶端
            stage.setAlwaysOnTop(true);
            // 设置模态窗口,该窗口阻止事件传递到任何其他应用程序窗口
            stage.initModality(Modality.APPLICATION_MODAL);
            // 设置主容器为主界面舞台
            stage.initOwner(primaryStage);

            // 实例化场景
            Scene scene = new Scene(root);
            // 将场景设置到舞台上
            stage.setScene(scene);

            // 获取SendAccountOptionFrameController控制器
            SendAccountOptionFrameController controller = loader.getController();
            // 设置SendAccountOptionFrameController类的stage
            controller.setStage(stage);

            // 展示舞台
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

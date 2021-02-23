package com.mycom.myapp.mailsendsystem.tools;

import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 公共方法类
 *
 * @author lck100
 */
public class SimpleTools {
    /**
     * 操作结果：JavaFX设置按钮、标签等组件的图标
     *
     * @param labeleds   需要设置图标的按钮
     * @param imagePaths 图标的路径
     */
    public void setLabeledImage(Labeled[] labeleds, String[] imagePaths) {
        for (int i = 0; i < labeleds.length; i++) {
            labeleds[i].setGraphic(new ImageView(new Image("file:" + imagePaths[i])));
        }
    }

    /**
     * 操作结果：生成属性文件
     *
     * @param fileName 文件路径
     * @param maps     数据集合
     */
    public void dataWriteProperties(String fileName, Map<String, String> maps) {
        Properties properties = new Properties();
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            // 写入文件
            for (String key : maps.keySet()) {
                properties.setProperty(key, String.valueOf(maps.get(key)));
            }
            FileOutputStream fos = new FileOutputStream(file);
            properties.store(fos, null);//store(...)指定的流仍保持打开状态
            fos.flush();
            fos.close();// 关闭流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 操作结果：读取配置文件
     *
     * @param propertiesFilePath 文件路径
     * @return Map 数据集合
     */
    public Map readReturnMap(String propertiesFilePath) {
        Properties properties = new Properties();
        Map map = new HashMap();
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(propertiesFilePath));
            properties.load(inputStream);
            map.put("addresser", properties.getProperty("addresser"));
            map.put("password", properties.getProperty("password"));
            map.put("server", properties.getProperty("server"));
            inputStream.close();
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 操作结果：获取文件选择器所选中的文件
     *
     * @return File 文件选择器选中的文件
     */
    public File getSelectedFile() {
        String selectedFilePath = "";
        //实例化文件选择器
        FileChooser fileChooser = new FileChooser();
        //打开文件选择框
        File result = fileChooser.showOpenDialog(null);
        // 选择文件的路径
        if(result!=null){
            selectedFilePath = result.getAbsolutePath();
        }
        return new File(selectedFilePath);
    }
}

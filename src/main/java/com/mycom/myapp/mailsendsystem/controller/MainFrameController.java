package com.mycom.myapp.mailsendsystem.controller;

import com.mycom.myapp.mailsendsystem.MainApp;
import com.mycom.myapp.mailsendsystem.tools.JavaFXTools;
import com.mycom.myapp.mailsendsystem.tools.SimpleTools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * 主界面控制器
 * @author lck100
 */
public class MainFrameController {
    private SimpleTools simpleTools=new SimpleTools();
    private String selectedFilePath="";

    @FXML
    private HTMLEditor contentHTMLEditor;

    @FXML
    private Label appendixNameLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Button optionButton;

    @FXML
    private Button addAppendixButton;

    @FXML
    private TextField subjectTextField;

    @FXML
    private TextField addresseeTextField;

    @FXML
    private Label addresseeLabel;

    @FXML
    private Button resetButton;

    @FXML
    private Button sendButton;

    public void initialize() {
        // 初始化按钮图标
        simpleTools.setLabeledImage(
                new Labeled[]{
                        optionButton, sendButton, resetButton, exitButton, addAppendixButton},
                new String[]{
                        "src/main/resources/images/option.png",
                        "src/main/resources/images/send.png",
                        "src/main/resources/images/reset.png",
                        "src/main/resources/images/exit.png",
                        "src/main/resources/images/appendix.png"});
    }

    @FXML
    void do_optionButton_event(ActionEvent event) {
        // 加载设置界面
        new MainApp().initSendAccountOptionFrame();
    }

    @FXML
    void do_addAppendixButton_event(ActionEvent event) {
        File selectedFile = simpleTools.getSelectedFile();
        selectedFilePath = selectedFile.getAbsolutePath();
        appendixNameLabel.setText(selectedFile.getName());
    }

    @FXML
    void do_sendButton_event(ActionEvent event) throws UnsupportedEncodingException, MessagingException {
        Map dataMap = simpleTools.readReturnMap("src/main/resources/properties/data.properties");
        String addresser = (String) dataMap.get("addresser");
        String addressee = addresseeTextField.getText();
        String subject = subjectTextField.getText();
        String content = contentHTMLEditor.getHtmlText();
        String password = (String) dataMap.get("password");

        // 1.创建参数配置, 用于连接邮件服务器的参数配置
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", (String) dataMap.get("server"));
        properties.setProperty("mail.smtp.auth", "true");

        // 2.根据配置创建会话对象，用于和邮件服务器交互
        Session session = Session.getInstance(properties);
        session.setDebug(true);

        // 3.创建一封邮件
        MimeMessage message = null;
        if (selectedFilePath.equals("")) {
            message = createSimpleMimeMessage(session, addresser, addressee, subject, content);
        } else {
            message = createConflexMimeMessage(session, addresser, addressee, subject, content, selectedFilePath);
        }
        // 4.根据Session获取邮件传输对象
        Transport transport = session.getTransport();

        // 5.使用邮箱账号和密码连接邮件服务器，这里认证的邮箱必须与message中的发件人邮箱一致，否则报错
        transport.connect(addresser, password);

        // 6.发送邮件，发到所有的收件地址，message.getAllRecipients()获取到的是在创建邮件对象时添加的所有的收件人、抄送人、密送人
        transport.sendMessage(message, message.getAllRecipients());

        // 7.关闭连接
        transport.close();
    }

    @FXML
    void do_resetButton_event(ActionEvent event) {
        JavaFXTools javaFXTools = new JavaFXTools();
        javaFXTools.reset(addresseeTextField, subjectTextField);
        javaFXTools.reset(contentHTMLEditor);
        javaFXTools.reset(appendixNameLabel);
    }

    @FXML
    void do_exitButton_event(ActionEvent event) {
        // 退出程序
        System.exit(0);
    }

    /**
     * 操作结果：创建一个邮件发送对象
     *
     * @param session
     * @param sendMail    发信人
     * @param receiveMail 收信人
     * @param subject     邮件主题
     * @param content     邮件内容
     * @return
     * @throws UnsupportedEncodingException
     */
    public MimeMessage createSimpleMimeMessage(Session session,
                                               String sendMail,
                                               String receiveMail,
                                               String subject,
                                               String content)
            throws UnsupportedEncodingException, javax.mail.MessagingException {
        // 1.创建一封邮件
        MimeMessage mimeMessage = new MimeMessage(session);
        // 2.From：发件人
        mimeMessage.setFrom(new InternetAddress(sendMail, sendMail, "UTF-8"));
        // 3.To：收件人（可以增加多个收件人、抄送、密送）
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, new InternetAddress[]{new InternetAddress(receiveMail
                , receiveMail, "UTF-8")});
        // 4.邮件主题
        mimeMessage.setSubject(subject, "UTF-8");
        // 5.Content：邮件正文（可以用HTML标签）
        mimeMessage.setContent(content, "text/html;charset=UTF-8");
        // 6.设置发件时间
        mimeMessage.setSentDate(new Date());
        // 7.保存设置
        mimeMessage.saveChanges();

        return mimeMessage;
    }

    /**
     * 操作结果：创建一个邮件发送对象
     *
     * @param session     session
     * @param sendMail    发信人
     * @param receiveMail 收信人
     * @param subject     邮件主题
     * @param content     邮件内容
     * @param filePath    文件路径
     * @return 返回一个邮件发送对象
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public MimeMessage createConflexMimeMessage(Session session,
                                                String sendMail,
                                                String receiveMail,
                                                String subject,
                                                String content,
                                                String filePath)
            throws UnsupportedEncodingException, MessagingException {
        // 1.创建邮件对象
        MimeMessage message = new MimeMessage(session);

        // 2.Form：发件人
        message.setFrom(new InternetAddress(sendMail, sendMail, "UTF-8"));

        // 3.TO：收件人（可以增加多个收件人、抄送、密送）
        message.addRecipients(MimeMessage.RecipientType.TO, new InternetAddress[]{new InternetAddress(receiveMail,
                receiveMail, "UTF-8")});

        // 4.Subject：邮件主题
        message.setSubject(subject, "UTF-8");

        // 5.创建附件”节点“
        MimeBodyPart attachment = new MimeBodyPart();
        DataHandler dataHandler = new DataHandler(new FileDataSource(filePath));
        attachment.setDataHandler(dataHandler);
        attachment.setFileName(MimeUtility.encodeText(dataHandler.getName()));

        // 6.创建文本”节点“
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(content, "text/html;charset=UTF-8");

        // 7.设置文本和附件的关系（合成一个大的混合的节点）
        MimeMultipart totalPart = new MimeMultipart();
        totalPart.addBodyPart(text);
        totalPart.addBodyPart(attachment);// 如果有多个附件，可以创建多个多次添加
        totalPart.setSubType("mixed");

        // 8.设置整个邮件的关系（将最终的混合节点作为邮件的内容）
        message.setContent(totalPart);

        // 9.设置发件时间
        message.setSentDate(new Date());

        // 10.保存上面的所有设置
        message.saveChanges();

        return message;
    }

}

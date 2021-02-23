package com.mycom.myapp.mailsendsystem.tools;

import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.web.HTMLEditor;

public class JavaFXTools {
    /**
     * 操作结果：重置文本框和文本域
     *
     * @param textInputControls 文本框、文本域组
     */
    public void reset(TextInputControl... textInputControls) {
        for (TextInputControl textInputControl : textInputControls) {
            textInputControl.setText("");
        }
    }

    /**
     * 操作结果：重置HTML编辑器
     *
     * @param htmlEditors HTML编辑器组
     */
    public void reset(HTMLEditor... htmlEditors) {
        for (HTMLEditor htmlEditor : htmlEditors) {
            htmlEditor.setHtmlText("");
        }
    }

    /**
     * 操作结果：重置标签文本
     *
     * @param labels 标签组
     */
    public void reset(Label... labels) {
        for (Label label : labels) {
            label.setText("");
        }
    }
}

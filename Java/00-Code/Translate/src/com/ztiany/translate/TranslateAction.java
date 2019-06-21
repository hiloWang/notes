package com.ztiany.translate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;

import org.apache.http.util.TextUtils;

import java.awt.Color;
import java.io.UnsupportedEncodingException;

/**
 * 编写翻译插件博客：http://blog.csdn.net/lmj623565791/article/details/51548272
 * 官方文档：http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started.html，http://www.jetbrains.org/intellij/sdk/docs/tutorials.html
 * 翻译地址："http://fanyi.youdao.com/openapi.do?keyfrom=Skykai521&key=977124034&type=data&doctype=json&version=1.1&q=name"; 其中q为需要翻译的文字
 *
 * @author Ztiany
 *         Email ztiany3@gmail.com
 *         Date 17.10.15 15:40
 */
public class TranslateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //获取编辑器
        final Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        if (null == mEditor) {
            return;
        }
        SelectionModel model = mEditor.getSelectionModel();
        //获取选取的文字
        final String selectedText = model.getSelectedText();
        if (TextUtils.isEmpty(selectedText)) {
            return;
        }
        String baseUrl = "http://fanyi.youdao.com/openapi.do?keyfrom=Skykai521&key=977124034&type=data&doctype=json&version=1.1&q=";
        baseUrl += selectedText;
        HttpUtils.doGet(baseUrl, new HttpUtils.Callback() {
            @Override
            public void onResult(Result string) {
                String s = string.toString();
                try {
                    showResult(new String(s.getBytes(), "UTF8"), mEditor);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }


            @Override
            public void onError(Exception e) {
                showResult("请求错误", mEditor);
            }
        });
    }

    private void showResult(String result, Editor editor) {
        JBPopupFactory factory = JBPopupFactory.getInstance();

        factory.createHtmlTextBalloonBuilder(result, null, new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), null)
                .setFadeoutTime(5000)
                .createBalloon()
                .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
    }
}

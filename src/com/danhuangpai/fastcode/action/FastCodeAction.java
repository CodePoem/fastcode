package com.danhuangpai.fastcode.action;

import com.danhuangpai.fastcode.ui.FieldsDialog;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

import javax.swing.*;

/**
 * 插件程序入口类
 *
 * @author danhuangpai
 * @version 1.0.0 created at 2017/12/4 16:54
 */
public class FastCodeAction extends BaseGenerateAction {

    protected JFrame mDialog;
    protected static final Logger log = Logger.getInstance(FastCodeAction.class);

    public FastCodeAction() {
        super(null);
    }

    public FastCodeAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取编辑器中的文件
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);

        // 获取当前类
        PsiClass targetClass = getTargetClass(editor, file);
        // 获取元素操作的工厂类
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);

        log.info("show dialog");

        //展示对话框
        FieldsDialog fieldsDialog = new FieldsDialog(project, targetClass, factory, file);
        fieldsDialog.setSize(800, 500);
        fieldsDialog.setLocationRelativeTo(null);
        fieldsDialog.setVisible(true);
    }
}

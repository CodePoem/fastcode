package com.danhuangpai.fastcode.ui;


import com.danhuangpai.fastcode.model.MyAbstractTableModel;
import com.danhuangpai.fastcode.model.ShowSelectModel;
import com.danhuangpai.fastcode.struct.FieldsCreator;
import com.danhuangpai.fastcode.struct.MethodCreator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * 属性字段选择对话框类
 *
 * @author danhuangpai
 * @version 1.0.0 created at 2017/12/4 16:59
 */
public class FieldsDialog extends JDialog {
    private JPanel contentPane;
    private JPanel fieldPanel;
    private JButton buttonCancel;
    private JButton buttonOK;
    private JScrollPane fieldJScrollPane;
    private JTable table1;

    private Project mProject;
    private PsiFile mFile;
    private PsiClass mTargetClass;
    private PsiElementFactory mFactory;

    private MyAbstractTableModel mAbstractTableModel;
    private List<ShowSelectModel> mShowSelectModels;

    public FieldsDialog() {
        setContentPane(contentPane);
        setModal(true);
        initView();
        initListener();
    }

    public FieldsDialog(Project project, PsiClass targetClass, PsiElementFactory factory, PsiFile... files) {
        this.mProject = project;
        this.mFile = files[0];
        this.mTargetClass = targetClass;
        this.mFactory = factory;
        setContentPane(contentPane);
        setModal(true);
        initView();
        initListener();
    }

    private void initView() {

        String currentClassName = mTargetClass.getName();
        setTitle("勾选需要的属性");

        //构造面板内容
        mShowSelectModels = new ArrayList<ShowSelectModel>();

        // 获取所有属性
        PsiField[] fields = mTargetClass.getAllFields();
        for (PsiField field : fields) {
            //只获取当前类本身的属性 排除继承来的或者其他方式获得的属性
            if (!mTargetClass.equals(field.getContainingClass())) {
                continue;
            }

            //获取属性名和类型名
            String fieldName = field.getNameIdentifier().getText();
            String typeName;
            PsiTypeElement typeElement = field.getTypeElement();
            if (typeElement == null) {
                continue;
            }
            PsiElement firstChild = typeElement.getFirstChild();
            if (firstChild == null) {
                continue;
            }
            typeName = firstChild.getText();
            ShowSelectModel tempShowSelectModel = new ShowSelectModel(fieldName, typeName);
            mShowSelectModels.add(tempShowSelectModel);
        }

        mAbstractTableModel = new MyAbstractTableModel(mShowSelectModels);
        table1.setModel(mAbstractTableModel);


        //支持滚动
        fieldJScrollPane.setViewportView(table1);
    }

    private void initListener() {

        //默认按键设置
        getRootPane().setDefaultButton(buttonOK);

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void onOK() {
        // add your code here
        //对勾选操作 过滤属性数据
        makeSureShowList();
        // 生成属性静态字符串变量
        new FieldsCreator(mShowSelectModels, mProject, mTargetClass, mFactory, mFile).execute();
        // 生成方法代码
        new MethodCreator(mShowSelectModels, mProject, mTargetClass, mFactory, mFile).execute();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void makeSureShowList() {
        ListIterator<ShowSelectModel> it = mShowSelectModels.listIterator();
        int currentIndex = 0;
        while (it.hasNext()) {
            ShowSelectModel tempShowSelectModel = it.next();
            Boolean checked = (Boolean) mAbstractTableModel.getValueAt(currentIndex, 0);
            currentIndex++;
            if (!checked) {
                it.remove();
            }
        }
    }

    public static void main(String[] args) {
        FieldsDialog dialog = new FieldsDialog();
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        System.exit(0);
    }
}

package com.danhuangpai.fastcode.struct;

import com.danhuangpai.fastcode.model.ShowSelectModel;
import com.danhuangpai.fastcode.utils.CreateUtils;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.List;

/**
 * 字段构造器类
 *
 * @author danhuangpai
 * @version 1.0.0 created at 2017/12/4 16:59
 */
public class FieldsCreator extends WriteCommandAction.Simple {

    private Project mProject;
    private PsiFile mFile;
    private PsiClass mTargetClass;
    private PsiElementFactory mFactory;
    private String mCommandName;
    private String mName;
    private String mGroupID;
    private PsiMethod getPsiMethod;
    private PsiMethod setPsiMethod;

    private List<ShowSelectModel> mShowSelectModels;

    protected FieldsCreator(Project project, String commandName, PsiFile... files) {
        super(project, commandName, files);
        this.mProject = project;
        this.mCommandName = commandName;
        this.mFile = files[0];
    }

    protected FieldsCreator(Project project, String name, String groupID, PsiFile... files) {
        super(project, name, groupID, files);
        this.mProject = project;
        this.mName = name;
        this.mGroupID = groupID;
        this.mFile = files[0];
    }

    public FieldsCreator(List<ShowSelectModel> showSelectModels, Project project, PsiClass targetClass, PsiElementFactory factory, PsiFile... files) {
        super(project, files);
        this.mShowSelectModels = showSelectModels;
        this.mProject = project;
        this.mFile = files[0];
        this.mTargetClass = targetClass;
        this.mFactory = factory;
    }

    @Override
    protected void run() throws Throwable {

        int size = mShowSelectModels.size();

        for (int i = 0; i < size; i++) {
            String fieldName = mShowSelectModels.get(i).getmFieldName();
            //去重
            PsiField psiField = mTargetClass.findFieldByName("ATTRIBUTE_" + fieldName.toUpperCase(), false);
            if (psiField == null) {
                // 生成字段模版代码
                String feildString = CreateUtils.createStaticFinalField(this, fieldName);
                // 将代码添加到当前类里
                mTargetClass.add(mFactory.createFieldFromText(feildString, mTargetClass));
            }
        }

        // 导入需要的类
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
        styleManager.optimizeImports(mFile);
        styleManager.shortenClassReferences(mTargetClass);
    }
}

package com.danhuangpai.fastcode.struct;

import com.danhuangpai.fastcode.model.ShowSelectModel;
import com.danhuangpai.fastcode.utils.CreateUtils;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.List;

public class MethodCreator extends WriteCommandAction.Simple {

    private Project mProject;
    private PsiFile mFile;
    private PsiClass mTargetClass;
    private PsiElementFactory mFactory;
    private String mCommandName;
    private String mName;
    private String mGroupID;

    private List<ShowSelectModel> mShowSelectModels;

    protected MethodCreator(Project project, String commandName, PsiFile... files) {
        super(project, commandName, files);
        this.mProject = project;
        this.mCommandName = commandName;
        this.mFile = files[0];
    }

    protected MethodCreator(Project project, String name, String groupID, PsiFile... files) {
        super(project, name, groupID, files);
        this.mProject = project;
        this.mName = name;
        this.mGroupID = groupID;
        this.mFile = files[0];
    }

    public MethodCreator(List<ShowSelectModel> showSelectModels, Project project, PsiClass targetClass, PsiElementFactory factory, PsiFile... files) {
        super(project, files);
        this.mShowSelectModels = showSelectModels;
        this.mProject = project;
        this.mFile = files[0];
        this.mTargetClass = targetClass;
        this.mFactory = factory;
    }

    @Override
    protected void run() throws Throwable {
        // 获取所有属性
        PsiField[] fields = mTargetClass.getAllFields();
        // 筛选属性
        List<PsiField> psiFieldList = CreateUtils.filterPsiField(fields);

        // 生成模版代码
        String setAttributeMethodString = CreateUtils.createSetAttributeMethodContent(mShowSelectModels);
        String getAttributeMethodString = CreateUtils.createGetAttributeMethodContent(mShowSelectModels);

        // 将代码添加到当前类里
        mTargetClass.add(mFactory.createMethodFromText(setAttributeMethodString, mTargetClass));
        mTargetClass.add(mFactory.createMethodFromText(getAttributeMethodString, mTargetClass));

        // 导入需要的类
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
        styleManager.optimizeImports(mFile);
        styleManager.shortenClassReferences(mTargetClass);
    }


}

package com.danhuangpai.fastcode.struct;

import com.danhuangpai.fastcode.model.ShowSelectModel;
import com.danhuangpai.fastcode.utils.CreateUtils;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.List;

/**
 * 方法构造器类
 *
 * @author danhuangpai
 * @version 1.0.0 created at 2017/12/4 16:59
 */
public class MethodCreator extends WriteCommandAction.Simple {

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

    public PsiClass getmTargetClass() {
        return mTargetClass;
    }

    public void setmTargetClass(PsiClass mTargetClass) {
        this.mTargetClass = mTargetClass;
    }

    public PsiMethod getGetPsiMethod() {
        return getPsiMethod;
    }

    public void setGetPsiMethod(PsiMethod getPsiMethod) {
        this.getPsiMethod = getPsiMethod;
    }

    public PsiMethod getSetPsiMethod() {
        return setPsiMethod;
    }

    public void setSetPsiMethod(PsiMethod setPsiMethod) {
        this.setPsiMethod = setPsiMethod;
    }

    @Override
    protected void run() throws Throwable {

        //判断包含关系 必须先进行这步操作
        boolean isContainsGetMethod = CreateUtils.containGetAttributeAtMethod(this, mTargetClass, "getAttribute");
        boolean isContainsSetMethod = CreateUtils.containsSetAttributeAtMethod(this, mTargetClass, "setAttribute");

        // 获取所有属性
        PsiField[] fields = mTargetClass.getAllFields();
        // 筛选属性
        List<PsiField> psiFieldList = CreateUtils.filterPsiField(fields);

        if (!isContainsGetMethod) {
            // 生成getAttribute方法模版代码
            String getAttributeMethodString = CreateUtils.createGetAttributeMethodContent(this, mShowSelectModels);
            // 将代码添加到当前类里
            mTargetClass.add(mFactory.createMethodFromText(getAttributeMethodString, mTargetClass));
        }
        if (!isContainsSetMethod) {
            // 生成setAttribute模版代码
            String setAttributeMethodString = CreateUtils.createSetAttributeMethodContent(this, mShowSelectModels);
            // 将代码添加到当前类里
            mTargetClass.add(mFactory.createMethodFromText(setAttributeMethodString, mTargetClass));
        }

        // 导入需要的类
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
        styleManager.optimizeImports(mFile);
        styleManager.shortenClassReferences(mTargetClass);
    }


}

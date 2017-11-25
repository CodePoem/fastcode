package com.danhuangpai.fastcode.utils;

import com.danhuangpai.fastcode.model.ShowSelectModel;
import com.danhuangpai.fastcode.setting.Constant;
import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author danhuangpai
 * @version 1.0.0 created at 2017/11/25 13:28
 */
public class CreateUtils {

    /**
     * 通过目标注解过滤属性数组
     *
     * @param fields 过滤前属性数组
     * @return 过滤后属性数组
     */
    public static List<PsiField> filterPsiField(PsiField[] fields) {
        List<PsiField> filterFiledsArrayList = new ArrayList<>();
        //遍历当前类的属性 找到加了目标注解的属性
        for (PsiField psiField : filterFiledsArrayList) {
            //获取属性修饰列表
            PsiModifierList modifierList = psiField.getModifierList();
            //从属性修饰列表中获取注解数组
            PsiAnnotation[] annotations = modifierList.getAnnotations();
            //遍历注解数组 查找目标注解
            for (PsiAnnotation annotation : annotations) {
                //获取注解名
                String annotationName = annotation.getNameReferenceElement().getQualifiedName();
                //查找到目标注解就加入筛选组
                if (Constant.ANNOTATION_NAME.equals(annotationName)) {
                    filterFiledsArrayList.add(psiField);
                }
            }
        }
        return filterFiledsArrayList;
    }

    /**
     * 构建setAttribute方法内容
     *
     * @param showSelectModels 需要构建的属性对象列表
     * @return 构建字符串
     */
    public static String createSetAttributeMethodContent(List<ShowSelectModel> showSelectModels) {
        // 将需要生成的方法写在StringBuilder里
        StringBuilder methodStringBuilder = new StringBuilder();
        int size = showSelectModels.size();
        methodStringBuilder.append("public void setAttribute(String attributeName,Object value){");

        for (int i = 0; i < showSelectModels.size(); i++) {
            if (i != 0) {
                methodStringBuilder.append(" else ");
            }
            appendFeild(methodStringBuilder, showSelectModels.get(i).getmFieldName(), showSelectModels.get(i).getType());
        }

        methodStringBuilder.append("\n}\n");
        return methodStringBuilder.toString();
    }

    /**
     * 构造一个属性判断设置内容
     *
     * @param methodStringBuilder 添加前构造内容StringBuilder
     * @param feildName           属性名
     * @param typeName            类型名
     * @return 添加后构造内容StringBuilder
     */
    public static StringBuilder appendFeild(StringBuilder methodStringBuilder, String feildName, String typeName) {
        String upFeildName = captureName(feildName);
        methodStringBuilder.append("if (\"");
        methodStringBuilder.append(feildName);
        methodStringBuilder.append("\".equals(attributeName)){\n");
        methodStringBuilder.append("\tset");
        methodStringBuilder.append(upFeildName);
        methodStringBuilder.append("((");
        methodStringBuilder.append(typeName);
        methodStringBuilder.append(") value);\n");
        methodStringBuilder.append("}");
        return methodStringBuilder;
    }

    /**
     * 首字母大写
     *
     * @param name 变换前字符串
     * @return 变换后字符串
     */
    public static String captureName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * 判断方法名重复
     *
     * @param mTargetClass 当前类
     * @param methodName   方法名
     * @return true：repeat false：no repeat
     */
    public static boolean isRepeatMethod(PsiClass mTargetClass, String methodName) {
        int repeatParameterNum = 0;
        //获取当前类所有的方法
        PsiMethod[] methods = mTargetClass.getAllMethods();
        //遍历方法
        for (PsiMethod method : methods) {
            if (methodName.equals(method.getNameIdentifier().getText())) {
                PsiParameterList psiParameters = method.getParameterList();
                int parametersCount = psiParameters.getParametersCount();
                PsiParameter[] parameters = psiParameters.getParameters();
                if (parameters[0] != null) {
                    PsiTypeElement typeElement = parameters[0].getTypeElement();
                    String typeName = typeElement.getInnermostComponentReferenceElement().getText();
                    if ("String".equals(typeName)) {
                        repeatParameterNum++;
                    }
                }
                if (parameters[1] != null) {
                    PsiTypeElement typeElement = parameters[1].getTypeElement();
                    String typeName = typeElement.getInnermostComponentReferenceElement().getText();
                    if ("Object".equals(typeName)) {
                        repeatParameterNum++;
                    }
                }
                if (parametersCount == 2 && repeatParameterNum == 2) {
                    return true;
                }
            }
        }
        return false;
    }

}

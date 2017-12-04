package com.danhuangpai.fastcode.utils;

import com.danhuangpai.fastcode.model.ShowSelectModel;
import com.danhuangpai.fastcode.setting.Constant;
import com.danhuangpai.fastcode.struct.FieldsCreator;
import com.danhuangpai.fastcode.struct.MethodCreator;
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
     * 构建静态字符串变量
     *
     * @param fieldsCreator 属性字段构造器
     * @param feildName     需要构建的属性字段名
     * @return 构建字符串
     */
    public static String createStaticFinalField(FieldsCreator fieldsCreator, String feildName) {
        StringBuilder feildStringBuilder = new StringBuilder();
        feildStringBuilder.append("private static final String ATTRIBUTE_" + feildName.toUpperCase() + " = \"" + feildName + "\";");
        return feildStringBuilder.toString();
    }

    /**
     * 构建setAttribute方法内容
     *
     * @param methodCreator    方法构造器
     * @param showSelectModels 需要构建的属性对象列表
     * @return 构建字符串
     */
    public static String createSetAttributeMethodContent(MethodCreator methodCreator, List<ShowSelectModel> showSelectModels) {
        // 将需要生成的方法写在StringBuilder里
        StringBuilder methodStringBuilder = new StringBuilder();
        //如果是继承父类方法或实现接口方法 插入@Override
        boolean isParentMethod = isSupperMethod(methodCreator.getmTargetClass(), methodCreator.getSetPsiMethod());
        boolean isInterfaceMethod = isInterfaceMethod(methodCreator.getSetPsiMethod());
        if (isParentMethod || isInterfaceMethod) {
            methodStringBuilder.append("@Override\n");
        }
        methodStringBuilder.append("public void setAttribute(String attributeName,Object value){\n");
        //继承父类方法插入super
        if (isParentMethod && !isInterfaceMethod) {
            methodStringBuilder.append("\tsuper.setAttribute(attributeName,value);\n");
        }
        for (int i = 0; i < showSelectModels.size(); i++) {
            if (i != 0) {
                methodStringBuilder.append(" else ");
            }
            appendSetAttributeFeild(methodStringBuilder, showSelectModels.get(i).getmFieldName(), showSelectModels.get(i).getType());
        }
        methodStringBuilder.append("\n}\n");
        return methodStringBuilder.toString();
    }

    /**
     * 构建getAttribute方法内容
     *
     * @param methodCreator    方法构造器
     * @param showSelectModels 需要构建的属性对象列表
     * @return 构建字符串
     */

    public static String createGetAttributeMethodContent(MethodCreator methodCreator, List<ShowSelectModel> showSelectModels) {
        // 将需要生成的方法写在StringBuilder里
        StringBuilder methodStringBuilder = new StringBuilder();
        //如果是继承父类方法或实现接口方法 插入@Override
        boolean isParentMethod = isSupperMethod(methodCreator.getmTargetClass(), methodCreator.getGetPsiMethod());
        boolean isInterfaceMethod = isInterfaceMethod(methodCreator.getGetPsiMethod());
        if (isParentMethod || isInterfaceMethod) {
            methodStringBuilder.append("@Override\n");
        }
        methodStringBuilder.append("public Object getAttribute(String attributeName){\n");
        //继承父类方法插入super
        if (isParentMethod && !isInterfaceMethod) {
            methodStringBuilder.append("\tsuper.getAttribute(attributeName);\n");
        }
        for (int i = 0; i < showSelectModels.size(); i++) {
            if (i != 0) {
                methodStringBuilder.append(" else ");
            }
            appendGetAttributeFeild(methodStringBuilder, showSelectModels.get(i).getmFieldName());
        }
        methodStringBuilder.append("\nreturn null;");
        methodStringBuilder.append("\n}\n");
        return methodStringBuilder.toString();
    }

    /**
     * 构造一个SetAttribute属性判断设置内容
     *
     * @param methodStringBuilder 添加前构造内容StringBuilder
     * @param feildName           属性名
     * @param typeName            类型名
     * @return 添加后构造内容StringBuilder
     */
    public static StringBuilder appendSetAttributeFeild(StringBuilder methodStringBuilder, String feildName, String typeName) {
        String upFeildName = captureName(feildName);
        methodStringBuilder.append("if (");
        methodStringBuilder.append("ATTRIBUTE_" + feildName.toUpperCase());
        methodStringBuilder.append(".equals(attributeName)){\n");
        methodStringBuilder.append("\tset");
        methodStringBuilder.append(upFeildName);
        methodStringBuilder.append("((");
        methodStringBuilder.append(typeName);
        methodStringBuilder.append(") value);\n");
        methodStringBuilder.append("}");
        return methodStringBuilder;
    }

    /**
     * 构造一个GetAttribute属性判断设置内容
     *
     * @param methodStringBuilder 添加前构造内容StringBuilder
     * @param feildName           属性名
     * @return 添加后构造内容StringBuilder
     */
    public static StringBuilder appendGetAttributeFeild(StringBuilder methodStringBuilder, String feildName) {
        String upFeildName = captureName(feildName);
        methodStringBuilder.append("if (");
        methodStringBuilder.append("ATTRIBUTE_" + feildName.toUpperCase());
        methodStringBuilder.append(".equals(attributeName)){\n");
        methodStringBuilder.append("\treturn get");
        methodStringBuilder.append(upFeildName);
        methodStringBuilder.append("();\n");
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
     * 文件名去掉文件后缀
     *
     * @param name 变换前文件名字符串
     * @return 变换后文件名字符串
     */
    public static String deteleFileExtension(String name) {
        String[] afterNames = name.split("\\.");
        return afterNames[0];
    }

    /**
     * 是否包含SetAttribute方法
     *
     * @param mTargetClass 当前类
     * @param methodName   方法名
     * @return true 包含 false不包含
     */
    public static boolean containsSetAttributeAtMethod(MethodCreator methodCreator, PsiClass mTargetClass, String methodName) {
        PsiMethod[] methods = mTargetClass.getAllMethods();
        int repeatParameterNum = 0;
        //遍历方法
        for (PsiMethod method : methods) {
            if (methodName.equals(method.getNameIdentifier().getText())) {
                PsiParameterList psiParameters = method.getParameterList();
                int parametersCount = psiParameters.getParametersCount();
                PsiParameter[] parameters = psiParameters.getParameters();
                if (parametersCount != 2) {
                    continue;
                }
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
                if (repeatParameterNum == 2) {
                    //如果是继承得来或是实现接口得来的 就缓存方法
                    if (isSupperMethod(mTargetClass, method) || isInterfaceMethod(method)) {
                        methodCreator.setSetPsiMethod(method);
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否包含GetAttribute方法
     *
     * @param mTargetClass 当前类
     * @param methodName   方法名
     * @return true 包含 false不包含
     */
    public static boolean containGetAttributeAtMethod(MethodCreator methodCreator, PsiClass mTargetClass, String methodName) {
        PsiMethod[] methods = mTargetClass.getAllMethods();
        int repeatParameterNum = 0;
        //遍历方法
        for (PsiMethod method : methods) {
            if (methodName.equals(method.getNameIdentifier().getText())) {
                PsiParameterList psiParameters = method.getParameterList();
                int parametersCount = psiParameters.getParametersCount();
                PsiParameter[] parameters = psiParameters.getParameters();
                if (parametersCount != 1) {
                    continue;
                }
                if (parameters[0] != null) {
                    PsiTypeElement typeElement = parameters[0].getTypeElement();
                    String typeName = typeElement.getInnermostComponentReferenceElement().getText();
                    if ("String".equals(typeName)) {
                        repeatParameterNum++;
                    }
                }
                if (repeatParameterNum == 1) {
                    //如果是继承得来或是实现接口得来的 就缓存方法
                    if (isSupperMethod(mTargetClass, method) || isInterfaceMethod(method)) {
                        methodCreator.setGetPsiMethod(method);
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是父类中的方法
     *
     * @param mTargetClass 判断类
     * @param method       类方法
     * @return true：非本类方法 false：本类方法
     */
    public static boolean isSupperMethod(PsiClass mTargetClass, PsiMethod method) {
        if (method == null) {
            return false;
        }
        if (isInterfaceMethod(method)) {
            return false;
        }
        return !mTargetClass.equals(method.getContainingClass());
    }

    /**
     * 判断目标方法是否是实现接口
     *
     * @param method 目标方法
     * @return true：实现接口 false：非实现接口
     */
    public static boolean isInterfaceMethod(PsiMethod method) {
        if (method == null) {
            return false;
        }
        PsiClass psiClass = method.getContainingClass();
        if (psiClass == null) {
            return false;
        }
        return psiClass.isInterface();
    }

}

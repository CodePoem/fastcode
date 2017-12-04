package com.danhuangpai.fastcode.model;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * 自定义抽象表格类
 *
 * @author danhuangpai
 * @version 1.0.0 created at 2017/12/4 16:58
 */
public class MyAbstractTableModel extends AbstractTableModel {

    /**
     * 表头数据
     */
    private String[] mHead;
    /**
     * 每一列的数据类型
     */
    private Class[] mTypeArray;
    /**
     * 表的内容数据
     */
    Object[][] mData;

    public MyAbstractTableModel() {
        // 定义表头数据
        mHead = new String[]{"Select", "FieldName", "Type"};
        // 定义表格每一列的数据类型
        mTypeArray = new Class[]{Boolean.class,
                Object.class, Object.class};
        // 定义表的内容数据
        Object[] data1 = {new Boolean(true), "Field1", "String"};
        Object[] data2 = {new Boolean(true), "Field2", "String"};
        Object[] data3 = {new Boolean(true), "Field3", "String"};
        Object[] data4 = {new Boolean(true), "Field4", "String"};
        Object[] data5 = {new Boolean(true), "Field5", "String"};

        mData = new Object[][]{data1, data2, data3, data4, data5};
    }

    public MyAbstractTableModel(List<ShowSelectModel> showSelectModelList) {
        // 定义表头数据
        mHead = new String[]{"Select", "FieldName", "Type"};
        // 定义表格每一列的数据类型
        mTypeArray = new Class[]{Boolean.class, Object.class, Object.class};
        int size = showSelectModelList.size();
        mData = new Object[size][];
        // 定义表的内容数据
        for (int i = 0; i < size; i++) {
            Object[] tempData = {new Boolean(true), showSelectModelList.get(i).getmFieldName(), showSelectModelList.get(i).getType()};
            mData[i] = tempData;
        }
    }


    /**
     * 获得表格的列数
     *
     * @return 表格的列数
     */
    @Override
    public int getColumnCount() {
        return mHead.length;
    }

    /**
     * 获得表格的行数
     *
     * @return 表格的行数
     */
    @Override
    public int getRowCount() {
        return mData.length;
    }

    /**
     * 获得表格的列名称
     *
     * @param column 列索引
     * @return 表格的列名称
     */
    @Override
    public String getColumnName(int column) {
        return mHead[column];
    }

    /**
     * 获得表格的单元格的数据
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return mData[rowIndex][columnIndex];
    }

    /**
     * 单元格编辑性
     *
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @return 单元格编辑性 true：可编辑 false：不可编辑
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return true;
        }
        return false;
    }


    /**
     * 替换单元格的值
     *
     * @param aValue      替换后的值
     * @param rowIndex    要替换单元格的行索引
     * @param columnIndex 要替换单元格的列索引
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        mData[rowIndex][columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }


    /**
     * 实现了如果是boolean自动转成JCheckbox
     * 需要自己的celleditor这么麻烦吧。jtable自动支持Jcheckbox，
     * 只要覆盖tablemodel的getColumnClass返回一个boolean的class， jtable会自动画一个Jcheckbox给你，
     * 你的value是true还是false直接读table里那个cell的值就可以
     *
     * @param columnIndex 行索引
     * @return 单元格数据类型
     */
    @Override
    public Class getColumnClass(int columnIndex) {
        // 返回每一列的数据类型
        return mTypeArray[columnIndex];
    }
}

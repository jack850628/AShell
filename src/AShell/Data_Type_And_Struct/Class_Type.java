package AShell.Data_Type_And_Struct;

import AShell.CommandResolve.CommandArray;


public class Class_Type {
    public Value_Array ValueArray;//如果是靜態類別就是存放靜態類別成員，如果是動態就是動態類別成員
    public CommandArray CodeArray=null;//給靜態類別存放動態區段的程式碼，提供類別實例化時使用
    public Class_Type Parent=null;//父類別指標
    public String ParentAddress=null;//父類別記憶體位置
    /**
     * Class_Type建構式
     * @param Previous_Floor  如果是靜態類別就是該類別所存在的變數清單，如果是動態就是該類別的靜態變數清單
     */
    public Class_Type(Value_Array Previous_Floor){
        this.ValueArray=new Value_Array(Previous_Floor);
    }
    /**
     * 表示變數清單是SClass的
     **/
    public void isSClass(){
        this.ValueArray.isSClass();
    }
}

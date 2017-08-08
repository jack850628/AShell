package AShell.Data_Type_And_Struct;

import AShell.CommandResolve.CommandArray;

/**
 * 函數類型類別
 */
public class Function {
    static int i=0;
    public Value_Array ValueArray=new Value_Array(null);//存放函數參數用
    public Value_Array Closure_ValueArray;//閉包變數單，也就是該函數所存在的變數清單
    public CommandArray CodeArray=new CommandArray();//存放函數中程式碼用
    //public ArrayList<Tag> Tag=new ArrayList<>();//用來存放標籤名稱與行數的陣列
    public Function(Value_Array Closure_ValueArray){
        this.Closure_ValueArray=Closure_ValueArray;
    }
}

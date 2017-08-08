package AShell.Data_Type_And_Struct;

import java.lang.reflect.Method;
/**
 * native函數類型類別
 */
public class Native_Function {
    public Method native_function;//native函數
    public Value_Array ValueArray;//native函數所在的變數清單
    public Object instanct_Class;//native函數所在的java類別實例化
    public Native_Function(Method native_function,Value_Array ValueArray,Object instanct_Class){
        this.native_function=native_function;
        this.ValueArray=ValueArray;
        this.instanct_Class=instanct_Class;
    }
}

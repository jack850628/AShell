package AShell.Memory_Management;

import AShell.Data_Type_And_Struct.Value;
import AShell.Data_Type_And_Struct.Value_Array;


public class ValueListSet {
    //把B變數清單的所有變數記憶體位置複製給A變數清單
    public static Value_Array ListSet(Value_Array ValueArrayA,Value_Array ValueArrayB){
        if(ValueArrayB!=null)//這是為了預防陣列的size函數，目前陣列的size函數的閉包清單預設是\null
            ValueArrayB.stream().forEach((Value) -> {
                /*if(!Value.Name.toString().matches("base|this|super"))
                    Memory_Management.Reference_person_PP(Value.Tent.toString());//判斷所訂的值是不是記憶體參考*/
                ValueArrayA.add(Value);
            });
        return ValueArrayA;
    }
    //依照條件(剔除名稱等於Condition其中一個元素的變數)把B變數清單的所有變數記憶體位置複製給A變數清單
    public static Value_Array Condition_ListSet(String[] Condition,Value_Array ValueArrayA,Value_Array ValueArrayB){
        for(Value value:ValueArrayB)Tag:{
            for(String condition:Condition){
                if(condition.equals(value.Name.toString()))
                    break Tag;
            }
            ValueArrayA.add(value);
        }
        return ValueArrayA;
    }
}

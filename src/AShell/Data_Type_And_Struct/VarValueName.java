package AShell.Data_Type_And_Struct;
//暫存要建立的變數名稱以及要存放在的變數清單
public class VarValueName {
    public StringBuilder name;//要建立的變數名稱
    public Value_Array ValueArray;//要存放在的變數清單
    public VarValueName(StringBuilder name,Value_Array ValueArray){
        this.name=name;
        this.ValueArray=ValueArray;
    }
}

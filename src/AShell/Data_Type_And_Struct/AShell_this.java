package AShell.Data_Type_And_Struct;

import AShell.AShell;

//提供AShell調用java函數時所需要用到的相關物件
public class AShell_this {
    public AShell AS;//AShell語言解析器的主類別
    public AShell.Run_Point RP;//執行續指標，用來記錄一個AShell執行續在執行續清單裡的位置
    public Value_Array ValueArray;//建立這個函數在AShell時的AShell執行緒的變數清單
    public AShell_this(AShell AS,AShell.Run_Point RP,Value_Array ValueArray){
        this.AS=AS;
        this.RP=RP;
        this.ValueArray=ValueArray;
    }
}

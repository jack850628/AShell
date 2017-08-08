package AShell.ValueProcess;

import java.util.ArrayList;

public class Count_Array extends ArrayList<Count>{
    public boolean SpecialLogic=false;//特殊邏輯判斷，也就是判斷是否為使用python那種的邏輯判斷式(例如：[1<2<3])
    public Count_Array(){}
    public Count_Array(boolean SpecialLogic){
        this.SpecialLogic=SpecialLogic;
    }
}

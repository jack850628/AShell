package AShell.Memory_Management;

import AShell.AShell;
import AShell.Data_Type_And_Struct.Value_Array;
import AShell.Data_Type_And_Struct.Class_Type;
import AShell.ValueProcess.VarMode;
import AShell.ValueProcess.VarStrDW;


public class GetParent {
    /**
     * 取得要建立的類別名稱以及繼承的父類別
     * @param AS AShell語言解析器的主類別
     * @param RP 執行續指標，用來記錄一個AShell執行續在執行續清單裡的位置
     * @param ClassName 存放類別名稱的StringBuilder容器
     * @param ClassName_AND_Parent 要解析的class名稱以及父類別
     * @param ValueArray 變數陣列
     * @return 父類別的SClass
     * @throws Exception 父類別取得失敗
     */
    public static ReturnParentStruct Get_Parent(AShell AS,AShell.Run_Point RP,StringBuilder ClassName,String ClassName_AND_Parent,Value_Array ValueArray) throws Exception{
        int index=0;
        for(;index<ClassName_AND_Parent.length();index++){//取得類別名稱
            if(ClassName_AND_Parent.charAt(index)==':'){
                index++;
                break;
            }
            if(ClassName_AND_Parent.charAt(index)!=' ')
                ClassName.append(ClassName_AND_Parent.charAt(index));
        }
        if(index==ClassName_AND_Parent.length())
            return null;
        String ParentAddress;
        return new ReturnParentStruct(
                ParentAddress=new VarStrDW(AS,RP,ClassName_AND_Parent.substring(index, ClassName_AND_Parent.length()),ValueArray, VarMode.Mode.Intermediary).Str.toString(),
                Memory_Management.get_Class(ParentAddress));
    }
    public static class ReturnParentStruct{
        public Class_Type Parent;
        public String ParentAddress;
        public ReturnParentStruct(String ParentAddress,Class_Type Parent){
            this.ParentAddress=ParentAddress;
            this.Parent=Parent;
        }
    }
}

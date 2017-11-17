package AShell.ValueProcess;

import AShell.AShell;
import AShell.CommandResolve.Command;
import AShell.Data_Type_And_Struct.Code_String;
import AShell.Data_Type_And_Struct.Function;
import AShell.Data_Type_And_Struct.Type_String;
import AShell.Data_Type_And_Struct.Value;
import AShell.Data_Type_And_Struct.Value_Array;
import AShell.Memory_Management.Memory_Management;
import java.util.ArrayList;

//建立AShell陣列或Map，在AShell裡Map和Array可以混合使用
public class Array_Create {
    /**
     * 同步方式建立AShell陣列，為了確保同一個陣列都會建立在同一個區段，所以已同步機制的方式限制同一時間只能有一個陣列被建立
     * @param AS AShell語言解析器的主類別
     *  @param RP 執行續指標，用來記錄一個AShell執行續在執行續清單裡的位置
     * @param Variable 指令緩衝變數
     * @param ValueArray 該執行續的變數清單
     * @param i 指向正在解析中的字元
     * @param Str 解析中的AShell程式碼
     * @return 目前解析完的字元位置
     * @throws Exception 陣列建立失敗原因
     */
    static class ElementItem{//存放Array項目在AShell陣列記憶體位址的內容空間與要每個元素要處理的程式碼
        StringBuilder itemStrBui;//在AShell陣列記憶體位址的內容空間
        StringBuilder itemKey;//在AShell的Map記憶體位址的Key
        String Content;//元素要處理的程式碼
        String Key_Value;//AShell的Map的Key的內容
        ElementItem(StringBuilder itemKey,StringBuilder itemStrBui,String Key_Value,String Content){
            this.itemKey=itemKey;
            this.itemStrBui=itemStrBui;
            this.Content=Content;
            this.Key_Value=Key_Value;
        }
    }
    private static final char[] LOCK=new char[0];//除了用來當ArrayCreate函數的同步所外就沒其他功能了
    static int ArrayCreate(AShell AS,AShell.Run_Point RP,StringBuilder Variable,Value_Array ValueArray,int i,String Str) throws Exception{
        Value_Array VA = new Value_Array(null);//第一維陣列的第一個元素的變數清單，主要用來存放size函數
        StringBuilder address=null;//陣列第一個元素的記憶體位置
        ArrayList<ElementItem> Element_Value=new ArrayList<>();//陣列的元素清單
        String Key_Value=null;
        synchronized(LOCK){
            Exit:while(true){
                i++;
                String str=Str.substring(i, i+1);
                switch (str) {
                    case ","://當讀完一個元素時
                        //先建立元素的位置在處理元素的內容，這樣就可以確保每一維的元素空間都相鄰在一起
                        StringBuilder itemStrBui=new StringBuilder();//在AShell陣列記憶體位址的內容空間
                        StringBuilder AShell_Map_Key=(Key_Value!=null&&!Key_Value.equals(""))?new StringBuilder():null;//AShell的Map的Key的內容空間
                        if(Element_Value.isEmpty())//當Element_Value為空就代表現在處理的是第一個元素
                            address=Memory_Management.Array_Builder(AShell_Map_Key,itemStrBui,VA);//取得陣列第一個元素的記憶體位置
                        else
                            Memory_Management.Array_Builder(AShell_Map_Key,itemStrBui,null);//建立一個新的記憶體位置
                        Element_Value.add(new ElementItem(AShell_Map_Key,itemStrBui,Key_Value,Variable.toString()));//將元素內容放加入到元素清單
                        Key_Value=null;
                        Variable.delete(0, Variable.length());
                        continue;
                    case "="://代表之前讀到的字串是Key
                        if(Str.substring(i+1, i+2).equals(">")){
                            i++;
                            Key_Value=Variable.toString();
                            Variable.delete(0, Variable.length());
                            continue;
                        }
                        break;
                    case "{":
                    {
                        Variable.append(str);
                        int count=0;
                        Out:while (true) {
                            i++;
                            str=Str.substring(i, i+1);
                            Variable.append(str);
                            switch (str) {
                                case "{":
                                    count++;
                                    break;
                                case "}":
                                    if (count--==0) 
                                        break Out;
                                    break;
                                case "\"":
                                    while(true){
                                        i++;
                                        try{
                                            str=Str.substring(i, i+1);
                                        }catch(Exception e){
                                            throw new Exception("語法錯誤，字串沒有結束");
                                        }
                                        Variable.append(str);
                                        if(str.equals("\""))
                                            break;
                                        else if(str.equals("\\")){
                                            Variable.append(Str.substring(++i, i+1));
                                        }
                                    }
                                    break;
                            }
                        }
                        continue;
                    }
                    case "}":
                        break Exit;
                    case "(":
                    {
                        Variable.append(str);
                        int count=0;
                        Out:while (true) {
                            i++;
                            str=Str.substring(i, i+1);
                            Variable.append(str);
                            switch (str) {
                                case "(":
                                    count++;
                                    break;
                                case ")":
                                    if (count--==0) 
                                        break Out;
                                    break;
                                case "\"":
                                    while(true){
                                        i++;
                                        try{
                                            str=Str.substring(i, i+1);
                                        }catch(Exception e){
                                            throw new Exception("語法錯誤，字串沒有結束");
                                        }
                                        Variable.append(str);
                                        if(str.equals("\""))
                                            break;
                                        else if(str.equals("\\")){
                                            Variable.append(Str.substring(++i, i+1));
                                        }
                                    }
                                    break;
                            }
                        }
                        continue;
                    }
                    case "\"":
                        Variable.append(str);
                        while(true){
                            i++;
                            try{
                                str=Str.substring(i, i+1);
                            }catch(Exception e){
                                throw new Exception("語法錯誤，字串沒有結束");
                            }
                            Variable.append(str);
                            if(str.equals("\""))
                                break;
                            else if(str.equals("\\"))
                                Variable.append(Str.substring(++i, i+1));
                        }
                        continue;
                }
                Variable.append(str);
            }
            {
                StringBuilder AShell_Map_Key=(Key_Value!=null&&!Key_Value.equals(""))?new StringBuilder():null,temp=new StringBuilder();
                if(Element_Value.isEmpty())//當Element_Value為空就代表現在處理的是第一個元素
                    address=Memory_Management.Array_Builder(AShell_Map_Key,temp,VA);//取得陣列第一個元素的記憶體位置
                else
                    Memory_Management.Array_Builder(AShell_Map_Key,temp,null);//建立一個新的記憶體位置
                Element_Value.add(new ElementItem(AShell_Map_Key,temp,Key_Value,Variable.toString()));//將元素內容放加入到元素清單
            }
        }
        Variable.delete(0, Variable.length());
        //先建立元素的位置在處理元素的內容，這樣就可以確保每一維元的素空間都相鄰在一起
        for(ElementItem EI : Element_Value){//當記憶體位至都建立好了之後，就開始處理元素內容
            StringBuilder temp=new VarStrDW(AS,RP, EI.Content, ValueArray, VarMode.Mode.Intermediary).Str;
            EI.itemStrBui.append((temp.toString().equals(""))?Type_String.NULL:temp);
            if(EI.Key_Value!=null&&!EI.Key_Value.equals("")){
                temp=new VarStrDW(AS,RP, EI.Key_Value, ValueArray, VarMode.Mode.Intermediary).Str;
                if(!temp.toString().matches("\".*\""))//判斷是否為AShell字串類型
                    throw new Exception("map的key必須是字串類型");
                EI.itemKey.append(temp);
            }
        }
        //-----------------------------建立陣列大小函數-----------------------------------------------------------------------------
        Function size=new Function(null,"");
        size.CodeArray.add(new Command(new StringBuilder(Code_String.RETURN+" "+Element_Value.size()),0));
        VA.add(new Value(new StringBuilder("size"),Memory_Management.Function_Builder(size,1)));
        Memory_Management.set_Array_Sise(address.toString(), Element_Value.size());
        //------------------------------------------------------------------------------------------------------------------------------------
        Variable.append(address);
        return i;
    }
}

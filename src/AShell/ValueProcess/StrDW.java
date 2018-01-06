package AShell.ValueProcess;

import AShell.AShell;
import AShell.CommandResolve.StringScan;
import AShell.Data_Type_And_Struct.Code_String;
import AShell.Data_Type_And_Struct.IsNumeric;
import AShell.Data_Type_And_Struct.Type_String;
import AShell.Memory_Management.Memory_Management;
import AShell.Data_Type_And_Struct.Value_Array;
import java.util.ArrayList;
import java.util.Stack;
/**
 *字串處理用類別
 *這是專門處理字串用的類別 **/
public class StrDW {
        AShell AS;
        AShell.Run_Point RP;
	/**儲存處理完成的字串**/
	public StringBuilder Str;
	/**變數名稱紀錄**/
	StringBuilder Variable=new StringBuilder("");
	/**Count值陣列**/
	ArrayList<Count_Array> CountRecord=new ArrayList<>();
	/**負號判斷**/
	/**音效名稱紀錄紀錄**/
	StringBuilder SoundName=new StringBuilder("");
        Value_Array BUValueArray;
        Stack<String> AShell_Memory_Type=new  Stack<>();//AShell記憶體類型暫存堆疊，用來將一次性使用的AShell記憶體類型回收用的
        /**
                    *變數與字串處理類別，用於將字串完整輸出時  
                    * @param AS AShell語言解析器的主類別
                    * @param RP 執行續指標，用來記錄一個AShell執行續在執行續清單裡的位置
                    * @param Str 欲處理的程式碼
                    * @param ValueArray 所在執行續的變數清單
                    * @throws java.lang.Exception 拋出錯誤給執行續接收
                    **/
	public StrDW(AShell AS,AShell.Run_Point RP,String Str,Value_Array ValueArray) throws Exception{
            //System.out.println("Strdw "+Str);
		//階層關西：邏輯運算子>引號>字串||變數
                this.AS=AS;
                this.RP=RP;
                BUValueArray=ValueArray;
                CountRecord.add(new Count_Array());
                CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
		for(int i=0;i<Str.length();i++){
                        String str=Str.substring(i, i+1);
                    switch (str) {
                        case "+":
                        case "^":
                        case "?":
                        case ":":
                        case "-":
                        case "~":
                            if(IsNumeric.isNumericTest(Variable.toString())){
                                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                Variable.delete(0, Variable.length());
                            }else
                                Results(ValueArray);
                            Count_Set.CountSet(CountRecord,str);
                            CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
                            ValueArray=BUValueArray;
                            break;
                        case ".":
                            if(IsNumeric.isNumericTest(Variable.toString())){
                                if(Str.substring(i+1, i+2).equals(".")){
                                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                    Variable.delete(0, Variable.length());
                                    Count_Set.CountSet(CountRecord,str+str);
                                    i++;
                                    CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
                                    ValueArray=BUValueArray;
                                }else
                                    Variable.append(".");
                            }else{
                                Results(ValueArray);
                                if(Str.substring(i+1, i+2).equals(".")){
                                    Count_Set.CountSet(CountRecord,str+str);
                                    i++;
                                    ValueArray=BUValueArray;
                                }else{
                                    StringBuilder Value=CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value;
                                    if((Value.toString().matches(Type_String.OBJECT_M)))
                                        ValueArray=Memory_Management.get_Object(Value.toString()).ValueArray;
                                    else if(Value.toString().matches(Type_String.CLASS_M))
                                        ValueArray=Memory_Management.get_Class(Value.toString()).ValueArray;
                                    else if(Value.toString().matches(Type_String.ARRAY_M))
                                        ValueArray=Memory_Management.get_Array_ValueArray(Value.toString());
                                    else if(Value.toString().matches(Type_String.FUNCTION_M)||Value.toString().matches(Type_String.NATIVE_FUNCTION_M))
                                        throw new Exception("物件存取運算子不能使用於函數記憶體類型");
                                    else
                                        throw new Exception("'"+Value.toString()+"'不是有效的AShell記憶體類型");
                                    Count_Set.CountSet(CountRecord,str);
                                }
                                CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
                            }
                            break;
                        case "&":
                        case "|":
                        case "*":
                        case "/":
                        case "%":
                            if(IsNumeric.isNumericTest(Variable.toString())){
                                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                Variable.delete(0, Variable.length());
                            }else
                                Results(ValueArray);
                            if(str.equals(Str.substring(i+1, i+2))){
                                Count_Set.CountSet(CountRecord,str+str);
                                i++;
                            }else
                                Count_Set.CountSet(CountRecord,str);
                            CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
                            ValueArray=BUValueArray;
                            break;
                        case "!":
                        case "=":
                        case ">":
                        case "<":
                            if(IsNumeric.isNumericTest(Variable.toString())){
                                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                Variable.delete(0, Variable.length());
                            }else
                                Results(ValueArray);
                            switch (Str.substring(i+1, i+2)) {
                                case "=":
                                    Count_Set.CountSet(CountRecord,str+"=");
                                    i++;
                                    break;
                                case ">"://判斷是否為 >>和<<或<>和><(等價於!=)
                                    Count_Set.CountSet(CountRecord,str+">");
                                    i++;
                                    break;
                                case "<"://判斷是否為 >>和<<或<>和><(等價於!=)
                                    Count_Set.CountSet(CountRecord,str+"<");
                                    i++;
                                    break;
                                default:
                                    Count_Set.CountSet(CountRecord,str);
                            }
                            CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
                            ValueArray=BUValueArray;
                            break;
                        case "{":
                            if(!Variable.toString().equals(""))
                                throw new Exception("語法錯誤，'"+Variable+"'後面不應該接著大括號");
                            /**大括號紀錄**/
                            try{
                                i=Array_Create.ArrayCreate(AS, RP, Variable, ValueArray, i, Str);
                            }catch(Exception e){
                                while(!AShell_Memory_Type.isEmpty())//將一次性使用的AShell記憶體回收
                                    Memory_Management.Cut_To_Arguments(AShell_Memory_Type.pop());//檢查是否有參照指數為零的，並將其回收
                                throw e;
                            }
                            //System.out.println("LOG:"+Variable.toString());
                            break;
                        case "[":
                            if(IsNumeric.isNumericTest(Variable.toString())){
                                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                Variable.delete(0, Variable.length());
                            }else
                                Results(ValueArray);
                            if(!CountRecord.get(CountRecord.size()-1).get(CountRecord.
                                    get(CountRecord.size()-1).size()-1).Value.toString().equals("")){
                                /**中括號紀錄**/
                                int Record=1;
                                //Variable.append(str);
                                while(Record!=0){
                                    i++;
                                    str=Str.substring(i, i+1);
                                    switch (str) {
                                        case "[":
                                            Record++;
                                            break;
                                        case "]":
                                            if(--Record==0)
                                                continue;
                                            else
                                                break;
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
                                                else if(str.equals("\\")){
                                                    Variable.append(Str.substring(++i, i+1));
                                                }
                                            }
                                            continue;
                                        default:
                                            break;
                                    }
                                    Variable.append(str);
                                }
                                int Array_Index;
                                VarStrDW SD=new VarStrDW(AS,RP, Variable.toString(), BUValueArray, VarMode.Mode.Intermediary);
                                if(SD.Str.toString().equals("null")){
                                        while(!AShell_Memory_Type.isEmpty())//將一次性使用的AShell記憶體回收
                                            Memory_Management.Cut_To_Arguments(AShell_Memory_Type.pop());//檢查是否有參照指數為零的，並將其回收
                                        throw new ArithmeticException("陣列參數為null");
                                }
                                try{
                                    if(SD.Str.toString().matches("\".*\"")){//判斷是否為AShell字串類型
                                        ResultsValue.MapResults(AS,RP,ValueArray,CountRecord,false,SD.Str.toString());
                                        Variable.delete(0, Variable.length());
                                    }else{ 
                                        if(SD.Str.toString().equals(Type_String.TRUE)){
                                           Array_Index=1;
                                        }else
                                            Array_Index=(int)(long)Math.floor(IsNumeric.isNumeric(
                                                    SD.Str.toString()));
                                        Variable.delete(0, Variable.length());
                                        if(CountRecord.get(CountRecord.size()-1).get(CountRecord.
                                                get(CountRecord.size()-1).size()-1).Value.toString().matches(Type_String.ARRAY_M))//如果要取的不是字串中的字元
                                            ResultsValue.ArrayResults(AS,RP,ValueArray,CountRecord,false,Array_Index);
                                        else{
                                            StringBuilder Temp=CountRecord.get(CountRecord.size()-1).get(CountRecord.
                                                get(CountRecord.size()-1).size()-1).Value,
                                                    ASChar=StringScan.get_AShell_String_Character_Array_for_StrDW(Temp,Array_Index);
                                            Temp.delete(0,Temp.length());
                                            Temp.append(ASChar);
                                        }
                                    }
                                }catch(Exception e){
                                    while(!AShell_Memory_Type.isEmpty())//將一次性使用的AShell記憶體回收
                                        Memory_Management.Cut_To_Arguments(AShell_Memory_Type.pop());//檢查是否有參照指數為零的，並將其回收
                                    throw e;
                                }
                            }else{
                                CountRecord.add(new Count_Array(true));
                                CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
                                Variable.delete(0, Variable.length());
                            }
                            break;
                        case "]":
                            if(IsNumeric.isNumericTest(Variable.toString())){
                                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                Variable.delete(0, Variable.length());
                            }else
                                Results(ValueArray);
                            Count_Set.CountSet(CountRecord,null);
                            Count_Set.Count(CountRecord,true);//開始計算
                            CountRecord.get(CountRecord.size()-2).get(CountRecord.get(CountRecord.size()-2).size()-1).Value.append(CountRecord.get(CountRecord.size()-1).get(0).Value);
                            CountRecord.remove(CountRecord.size()-1);
                            break;
                        case "(":
                            if(IsNumeric.isNumericTest(Variable.toString())){
                                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                Variable.delete(0, Variable.length());
                            }else
                                Results(ValueArray);
                            if(CountRecord.get(CountRecord.size()-1).get(CountRecord.
                                    get(CountRecord.size()-1).size()-1).Value.toString().matches(Type_String.MEMORY_TYPE_FIRST+"("+Type_String.CLASS_N+"|"+Type_String.FUNCTION_N+"|"+Type_String.NATIVE_FUNCTION_N+")@\\d+")){
                                /**函數紀錄**/
                                ArrayList<StringBuilder> Args=new ArrayList<>();
                                //Variable.append(str);
                                Exit:while(true){
                                    i++;
                                    str=Str.substring(i, i+1);
                                    switch (str) {
                                        case ",":
                                            if(Args.isEmpty())//如果Args陣列是空的就代表著呼叫函數時第一個參數是空白的
                                                    Args.add(new StringBuilder());//把第一個參數建起來並保持空白
                                            Args.add(new StringBuilder());
                                            continue;
                                        case "(":
                                            {
                                                if(Args.isEmpty())
                                                    Args.add(new StringBuilder(str));
                                                else
                                                    Args.get(Args.size()-1).append(str);
                                                int count=0;
                                                Out:while (true) {
                                                    i++;
                                                    str=Str.substring(i, i+1);
                                                    Args.get(Args.size()-1).append(str);
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
                                                                Args.get(Args.size()-1).append(str);
                                                                if(str.equals("\""))
                                                                    break;
                                                                else if(str.equals("\\")){
                                                                    Args.get(Args.size()-1).append(Str.substring(++i, i+1));
                                                                }
                                                            }
                                                            break;
                                                    }
                                                }
                                                continue;
                                            }
                                        case ")":
                                            break Exit;
                                        case "{":
                                        {
                                            if(Args.isEmpty())
                                                Args.add(new StringBuilder(str));
                                            else
                                                Args.get(Args.size()-1).append(str);
                                            int count=0;
                                            Out:while (true) {
                                                    i++;
                                                    str=Str.substring(i, i+1);
                                                    Args.get(Args.size()-1).append(str);
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
                                                                Args.get(Args.size()-1).append(str);
                                                                if(str.equals("\""))
                                                                    break;
                                                                else if(str.equals("\\")){
                                                                    Args.get(Args.size()-1).append(Str.substring(++i, i+1));
                                                                }
                                                            }
                                                            break;
                                                    }
                                                }
                                            continue;
                                        }
                                        case "\"":
                                            if(Args.isEmpty())
                                                Args.add(new StringBuilder(str));
                                            else
                                                Args.get(Args.size()-1).append(str);
                                            while(true){
                                                i++;
                                                try{
                                                    str=Str.substring(i, i+1);
                                                }catch(Exception e){
                                                    throw new Exception("語法錯誤，字串沒有結束");
                                                }
                                                Args.get(Args.size()-1).append(str);
                                                if(str.equals("\""))
                                                    break;
                                                else if(str.equals("\\")){
                                                    Args.get(Args.size()-1).append(Str.substring(++i, i+1));
                                                }
                                            }
                                            continue;
                                    }
                                    if(!str.equals(" ")&&!str.equals("  ")){
                                        if(Args.isEmpty())
                                            Args.add(new StringBuilder(str));
                                        else
                                            Args.get(Args.size()-1).append(str);
                                    }
                                }
                                ResultsValue.FunctionResults(AS,RP,BUValueArray,ValueArray,AShell_Memory_Type,Args,Variable,CountRecord,false);
                            }else if(CountRecord.get(CountRecord.size()-1).get(CountRecord.
                                    get(CountRecord.size()-1).size()-1).Value.toString().equals("")){
                                CountRecord.add(new Count_Array());
                                CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
                                Variable.delete(0, Variable.length());
                            }else
                                throw new Exception("'"+CountRecord.get(CountRecord.size()-1).get(CountRecord.
                                    get(CountRecord.size()-1).size()-1).Value+"'不是有效的函數或類別記憶體");
                            break;
                        case ")":
                            if(IsNumeric.isNumericTest(Variable.toString())){
                                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                Variable.delete(0, Variable.length());
                            }else
                                Results(ValueArray);
                            Count_Set.CountSet(CountRecord,null);
                            Count_Set.Count(CountRecord,true);//開始計算
                            CountRecord.get(CountRecord.size()-2).get(CountRecord.get(CountRecord.size()-2).size()-1).Value.append(CountRecord.get(CountRecord.size()-1).get(0).Value);
                            CountRecord.remove(CountRecord.size()-1);
                            break;
                        default:
                            if(str.equals("\"")){
                                OUTER:
                                while (true) {
                                    i++;
                                    str=Str.substring(i, i+1);
                                    switch (str) {
                                        case "\"":
                                            break OUTER;
                                        case "\\":
                                            str=Str.substring(++i, i+1);
                                            switch (str) {
                                                case "n":
                                                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append("\n");
                                                    break;
                                                case "b":
                                                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append("\b");
                                                    break;
                                                case "r":
                                                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append("\r");
                                                    break;
                                                case "t":
                                                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append("\t");
                                                    break;
                                                case "f":
                                                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append("\f");
                                                    break;
                                                default:
                                                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(str);
                                                    break;
                                            }
                                            break;
                                        default:
                                            CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(str);
                                            break;
                                    }
                                }
                            }else if(!str.equals(" "))
                                Variable.append(str);
                            else{
                                switch (Variable.toString()) {
                                        case Code_String.IS:
                                        case Code_String.AND:
                                        case Code_String.OR:
                                        case Code_String.NOT:
                                            Count_Set.CountSet(CountRecord,Variable.toString());
                                            Variable.delete(0, Variable.length());
                                            CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
                                            ValueArray=BUValueArray;
                                            break;
                                        default:
                                            if(IsNumeric.isNumericTest(Variable.toString())){
                                                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                                Variable.delete(0, Variable.length());
                                            }else
                                                Results(ValueArray);
                                            break;
                                    }
                            }
                            break;
                    }
		}
		if(IsNumeric.isNumericTest(Variable.toString())){
                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                    Variable.delete(0, Variable.length());
		}else
                    Results(ValueArray);
		Count_Set.CountSet(CountRecord,null);
		Count_Set.Count(CountRecord,true);//開始計算
		this.Str=CountRecord.get(0).get(0).Value;
                if(this.Str.toString().matches(Type_String.MEMORY_TYPE))//判斷所訂的值是不是記憶體參考
                    Memory_Management.Add_To_Arguments(this.Str.toString());//將參考指數加一
                while(!AShell_Memory_Type.isEmpty())//將一次性使用的AShell記憶體回收
                    Memory_Management.Cut_To_Arguments(AShell_Memory_Type.pop());//檢查是否有參照指數為零的，並將其回收
                CountRecord.get(0).clear();
		CountRecord.clear();
	}
	/**取得變數值
	 * @param ValueArray 變數陣列**/
	private void Results(Value_Array ValueArray) throws Exception{
                try{
                    ResultsValue.GeneralResults(AS,RP,ValueArray,AShell_Memory_Type,Variable,CountRecord,false);
                }catch(Exception e){
                    while(!AShell_Memory_Type.isEmpty())//將一次性使用的AShell記憶體回收
                        Memory_Management.Cut_To_Arguments(AShell_Memory_Type.pop());//檢查是否有參照指數為零的，並將其回收
                    throw e;
                }
	}
}

package AShell.ValueProcess;

import AShell.AShell;
import AShell.CommandResolve.StringScan;
import AShell.Data_Type_And_Struct.Code_String;
import AShell.Data_Type_And_Struct.IsNumeric;
import AShell.Data_Type_And_Struct.Type_String;
import AShell.Memory_Management.Memory_Management;
import AShell.Data_Type_And_Struct.VarValueName;
import AShell.Data_Type_And_Struct.Value_Array;
import java.util.ArrayList;
import java.util.Stack;
public class VarStrDW {
        AShell AS;
        AShell.Run_Point RP;
	public StringBuilder Str=new StringBuilder("");
	StringBuilder Variable=new StringBuilder("");
	ArrayList<VarValueName> ValueName=new ArrayList<>();
	ArrayList<Count_Array> CountRecord=new ArrayList<>();
        Value_Array BUValueArray;
        Stack<String> AShell_Memory_Type=new  Stack<>();//AShell記憶體類型暫存堆疊，用來將一次性使用的AShell記憶體類型回收用的
        /**
         * 
         * @param AS 為了取得與執行續變數清單而設的參數
         *  @param RP 執行續指標，用來記錄一個AShell執行續在執行續清單裡的位置
         * @param Str 指令
         * @param ValueArray 變數清單
         * @param Mode 設定變數的模式，var模式是將變數的記憶體位置整個換掉，而不是只是修改變數內容而已
        * @throws java.lang.Exception 拋出錯誤給執行續接收
         */
	public VarStrDW(AShell AS,AShell.Run_Point RP,String Str,Value_Array ValueArray,VarMode.Mode Mode) throws Exception{
            //new VarCountRecordCreate(Str);
            //System.out.println("var "+Str);
                int EgualIndex = -1;//赴值運算子位置
                String ArrayAddress = null;//用於陣列赴值時，紀錄要赴值的陣列記憶體位置
                if(Mode!=VarMode.Mode.Intermediary)//如果是具有赴值能力的模式
                    EgualIndex=getEgualIndex(Str,Mode);//取得程式碼中等於的位置
                this.AS=AS;
                this.RP=RP;
                BUValueArray=ValueArray;//原始變數清單備份
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
                            if(IsNumeric.isNumericTest(Variable.toString())){//點的前面是數字，也就是說這次獨到的點可能是小數點
                                if(Str.substring(i+1, i+2).equals(".")){
                                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                    Variable.delete(0, Variable.length());
                                    Count_Set.CountSet(CountRecord,str+str);
                                    i++;
                                    CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
                                    ValueArray=BUValueArray;
                                }else
                                    Variable.append(".");//如果是小數點就把小數點放回去
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
                        case "=":
                            if(Str.substring(i+1, i+2).equals("=")){
                                if(IsNumeric.isNumericTest(Variable.toString())){
                                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                    Variable.delete(0, Variable.length());
                                }else
                                    Results(ValueArray);
                                Count_Set.CountSet(CountRecord,str+"=");
                                i++;
                                CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
                            }else{
                                if(Variable.toString().equals(""))//當變數名稱為空時，那就是要赴值於陣列之中
                                    Variable.append(ArrayAddress);
                                ValueName.add(new VarValueName(Variable,ValueArray));
                                //System.err.println("Log: "+Variable.toString());
                                Variable=new StringBuilder();
                                CountRecord.get(CountRecord.size()-1).clear();
                                CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
                            }
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
                            /**中括號紀錄**/
                            int Record=1;
                            if(Mode==VarMode.Mode.Var&&EgualIndex==Str.length()){//如果是要宣告空陣列，在Var模式下如果沒有赴值運算子(=) getEgualIndex()會回傳整行程式碼的長度(Str.length())
                                Variable.append(str);
                                while(Record!=0){
                                    i++;
                                    str=Str.substring(i, i+1);
                                    switch (str) {
                                        case "[":
                                            Record++;
                                            break;
                                        case "]":
                                            Record--;
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
                            }else{
                                if(IsNumeric.isNumericTest(Variable.toString())){
                                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                    Variable.delete(0, Variable.length());
                                }else
                                    Results(ValueArray);
                                if(!CountRecord.get(CountRecord.size()-1).get(CountRecord.
                                    get(CountRecord.size()-1).size()-1).Value.toString().equals("")){
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
                                    VarStrDW SD=new VarStrDW(AS,RP, Variable.toString(), BUValueArray, VarMode.Mode.Intermediary);//處理陣列參數
                                    if(SD.Str.toString().equals("null")){
                                            while(!AShell_Memory_Type.isEmpty())//將一次性使用的AShell記憶體回收
                                                Memory_Management.Cut_To_Arguments(AShell_Memory_Type.pop());//檢查是否有參照指數為零的，並將其回收
                                            throw new ArithmeticException("陣列參數為null");
                                    }
                                    try{
                                        if(SD.Str.toString().matches("\".*\"")){//判斷是否為AShell字串類型
                                            if(Mode==VarMode.Mode.Intermediary||i>EgualIndex)
                                                ResultsValue.MapResults(AS,RP,ValueArray,CountRecord,true,SD.Str.toString());
                                            else
                                                ArrayAddress=ResultsValue.MapResults(AS,RP,ValueArray,CountRecord,true,SD.Str.toString());
                                            Variable.delete(0, Variable.length());
                                        }else{ 
                                            if(SD.Str.toString().equals(Type_String.TRUE)){
                                                Array_Index=1;
                                            }else
                                                Array_Index=(int)(long)Math.floor(IsNumeric.isNumeric(
                                                        SD.Str.toString()));
                                            Variable.delete(0, Variable.length());
                                            if(CountRecord.get(CountRecord.size()-1).get(CountRecord.
                                                    get(CountRecord.size()-1).size()-1).Value.charAt(0)!='\"'){//如果要取的不是字串中的字元
                                                if(Mode==VarMode.Mode.Intermediary||i>EgualIndex)
                                                    ResultsValue.ArrayResults(AS,RP,ValueArray,CountRecord,true,Array_Index);
                                                else
                                                    ArrayAddress=ResultsValue.ArrayResults(AS,RP,ValueArray,CountRecord,true,Array_Index);
                                            }else{
                                                if(i<=EgualIndex)
                                                    throw new ArithmeticException("字串內的字元不可被更改");
                                                StringBuilder Temp=CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value;
                                                StringBuilder ASChar=StringScan.get_AShell_String_Character_Array(Temp,Array_Index);
                                                
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
                            }
                            break;
                        case "]":
                            if(IsNumeric.isNumericTest(Variable.toString())){
                                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                Variable.delete(0, Variable.length());
                            }else
                                Results(ValueArray);
                            Count_Set.CountSet(CountRecord,null);
                            Count_Set.Count(CountRecord,false);//開始計算
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
                                ResultsValue.FunctionResults(AS,RP,BUValueArray,ValueArray,AShell_Memory_Type,Args,Variable,CountRecord,true);
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
                            Count_Set.Count(CountRecord,false);//開始計算
                            CountRecord.get(CountRecord.size()-2).get(CountRecord.get(CountRecord.size()-2).size()-1).Value.append(CountRecord.get(CountRecord.size()-1).get(0).Value);
                            CountRecord.remove(CountRecord.size()-1);
                            break;
                        default:
                            if(str.equals("\"")){
                                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(str);
                                while(true){
                                    i++;
                                    str=Str.substring(i, i+1);
                                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(str);
                                    if(str.equals("\"")){
                                        break;
                                    }else if(str.equals("\\"))
                                        CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Str.substring(++i, i+1));
                                }
                            }else{
                                if(!str.equals(" "))
                                    Variable.append(str);
                                else{
                                    if(Variable.toString().matches(String.format("%s|%s|%s|%s",Code_String.IS,Code_String.NOT,Code_String.AND,Code_String.OR))){
                                        Count_Set.CountSet(CountRecord,Variable.toString());
                                        Variable.delete(0, Variable.length());
                                        CountRecord.get(CountRecord.size()-1).add(new Count(new StringBuilder(""),null));
                                        ValueArray=BUValueArray;
                                    }else if(Mode==VarMode.Mode.Intermediary||i>EgualIndex){
                                        if(IsNumeric.isNumericTest(Variable.toString())){
                                            CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
                                            Variable.delete(0, Variable.length());
                                        }else
                                            Results(ValueArray);
                                    }
                                }
                            }
                            break;
                    }
		}
                if(Mode!=VarMode.Mode.Intermediary){
                    if(!ValueName.isEmpty()){//如果變數名稱陣列大小為0，這就代表著輸入的變數名稱只有一個且後面沒有=，也就是要建立一個值為空的變數或空值陣列
                                if(IsNumeric.isNumericTest(Variable.toString())){
						CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
						Variable.delete(0, Variable.length());
				}else
					Results(ValueArray);
				Count_Set.CountSet(CountRecord,null);
				Count_Set.Count(CountRecord,false);//開始計算
                                this.Str.append(CountRecord.get(0).get(0).Value);
                                if(this.Str.toString().equals(""))
                                    throw new Exception("赴值語法錯誤");
                                if(Mode!=VarMode.Mode.Unvar){
                                    if(this.Str.toString().matches(Type_String.MEMORY_TYPE))//判斷所訂的值是不是記憶體參考
                                        Memory_Management.Add_To_Arguments(this.Str.toString());//將參考指數加一
                                    ResultsValue.SetValue(AS,ValueName,this.Str,Mode);
                                }else
                                    throw new Exception("反宣告語法錯誤");
                    }else
                        if(Mode!=VarMode.Mode.Unvar)
                            ResultsValue.SetNullValue(AS,RP,ValueArray,Variable);
                        else
                            ResultsValue.UnVar(AS,ValueArray, Variable);
                }else{
                    if(IsNumeric.isNumericTest(Variable.toString())){
			CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Variable);
			Variable.delete(0, Variable.length());
                    }else
			Results(ValueArray);
                    Count_Set.CountSet(CountRecord,null);
                    Count_Set.Count(CountRecord,false);//開始計算
                    this.Str.append(CountRecord.get(0).get(0).Value);
                    if(this.Str.toString().matches(Type_String.MEMORY_TYPE))//判斷所訂的值是不是記憶體參考
                        Memory_Management.Add_To_Arguments(this.Str.toString());//將參考指數加一
                }
                while(!AShell_Memory_Type.isEmpty())//將一次性使用的AShell記憶體回收
                    Memory_Management.Cut_To_Arguments(AShell_Memory_Type.pop());//檢查是否有參照指數為零的，並將其回收
                CountRecord.get(0).clear();
		CountRecord.clear();
	}
        /**
         * 從程式碼中找出赴值運算子的位置
         * @param Str AShell程式碼
         * @param Mode 現在VarStrDW執行中的模式
         * @return 赴值運算子的位置，不過如果是Var模式且沒有找到等於則回傳整行程式碼的長度，這樣就像是=在程式碼的最後面
         */
        private int getEgualIndex(String Str,VarMode.Mode Mode){
            char c;
            for(int i=Str.length()-1;i>=0;i--){//會是從尾端開始找是因為=運算子可以不只使用一個
                c=Str.charAt(i);
                    switch (c) {
                        case '=':
                            if(Str.charAt(i-1)!='='&&Str.charAt(i-1)!='<'&&Str.charAt(i-1)!='>'&&Str.charAt(i-1)!='!')
                                return i;//將等於的位置回傳
                            else
                                i--;
                            break;
                        case '>':
                            i--;
                            break;
                        case '\"':
                            while(true){
                                i--;
                                c=Str.charAt(i);
                                if(c=='\"'){
                                    if(Str.charAt(i-1)!='\\')
                                        break;
                                    else
                                        i--;
                                }
                            }
                    }
            }
            return (Mode==VarMode.Mode.Var)?Str.length():-1;//如果是Var模式且沒有找到等於則回傳整行程式碼的長度，這樣就像是=在程式碼的最後面
        }
        /*private int getEgualIndex(String Str,VarMode.Mode Mode){
            char c;
            for(int i=0;i<Str.length();i++){
                c=Str.charAt(i);
                switch (c) {
                    case '=':
                        if(Str.charAt(i+1)!='='&&Str.charAt(i+1)!='>')
                            return i;//將等於的位置回傳
                        else
                            i++;
                        break;
                    case '!':
                    case '<':
                    case '>':
                        i++;
                        break;
                    case '\"':
                        while(true){
                            c=Str.charAt(++i);
                            if(c=='\"')
                               break;
                            else if(c=='\\')
                               i++;
                       }
                    }
            }
            return (Mode==VarMode.Mode.Var)?Str.length():-1;//如果是Var模式且沒有找到等於則回傳整行程式碼的長度，這樣就像是=在程式碼的最後面
        }*/
	private void Results(Value_Array ValueArray) throws Exception{
                try{
                    ResultsValue.GeneralResults(AS,RP,ValueArray,AShell_Memory_Type,Variable,CountRecord,true);
                }catch(Exception e){
                    while(!AShell_Memory_Type.isEmpty())//將一次性使用的AShell記憶體回收
                        Memory_Management.Cut_To_Arguments(AShell_Memory_Type.pop());//檢查是否有參照指數為零的，並將其回收
                    throw e;
                }
	}
}

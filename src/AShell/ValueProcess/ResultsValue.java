package AShell.ValueProcess;

import AShell.AShell;
import AShell.CommandResolve.ArrayNameResolve;
import AShell.CommandResolve.Command;
import AShell.Data_Type_And_Struct.AShell_this;
import AShell.Memory_Management.CensorValue;
import AShell.Memory_Management.CensorValueReturn;
import AShell.Data_Type_And_Struct.Class_Type;
import AShell.Data_Type_And_Struct.Code_String;
import AShell.Data_Type_And_Struct.Function;
import AShell.Data_Type_And_Struct.IsNumeric;
import AShell.Data_Type_And_Struct.Native_Function;
import AShell.Data_Type_And_Struct.Type_String;
import AShell.Memory_Management.Memory_Management;
import AShell.Data_Type_And_Struct.Value;
import AShell.Data_Type_And_Struct.VarValueName;
import AShell.Data_Type_And_Struct.Value_Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Stack;


public class ResultsValue {
	/**取得變數值
                    * @param AS AShell語言解析器的主類別
                    *  @param RP 執行續指標，用來記錄一個AShell執行續在執行續清單裡的位置
	 * @param Variable 變數名稱
	 * @param ValueArray 變數陣列
	 * @param Original 輸出型態
                    * @return  回傳所求變數內的植
                    * @throws java.lang.Exception 拋出變數未宣告之錯誤給執行續接收
                    */
	public static StringBuilder Results(AShell AS,AShell.Run_Point RP,StringBuilder Variable,Value_Array ValueArray,boolean Original) throws Exception{
		if(!Variable.toString().matches("|"+Type_String.ASHELL_TYPE)){
                    while(ValueArray!=null){
                        for(int i=0;i<ValueArray.size();i++){
                                if(ValueArray.get(i).Name.toString().equals(Variable.toString())){
                                        if(Original)
                                                return ValueArray.get(i).Tent;
                                        else{
                                                StringBuilder Str=new StrDW(AS,RP,ValueArray.get(i).Tent.toString(),ValueArray).Str;//如果回傳的是AShell記憶體類型的話，參考指數一定會被加一
                                                if(Str.toString().matches(Type_String.MEMORY_TYPE))//所以判斷所訂的值是不是記憶體參考
                                                    Memory_Management.Cut_To_Arguments_Not_Recycle(Str.toString());//將參考指數減一，但不回收
                                                return Str;
                                        }
                                }
                        }
                        ValueArray=ValueArray.Previous_Floor;
                    }
                }else
                    return Variable; 
		throw new Exception("變數'"+Variable.toString()+"'未宣告。");
		//return Variable;
	}
	public static void GeneralResults(AShell AS,AShell.Run_Point RP,Value_Array ValueArray,Stack<String> AShell_Memory_Type,StringBuilder Variable,
                ArrayList<Count_Array> CountRecord,boolean Original) throws Exception{
                String results;
                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(results=Results(AS,RP, Variable, ValueArray, Original).toString());
                if(results.matches(Type_String.MEMORY_TYPE)){//判斷取得值是不是記憶體參考
                    Memory_Management.Add_To_Arguments(results);//將參考指數加一
                    AShell_Memory_Type.push(results);//放入AShell記憶體類型暫存堆疊
                }
		Variable.delete(0, Variable.length());
		//return Variable;
	}
        /**
         * 取得陣列中的元素
         * @param AS AShell語言解析器的主類別
         *  @param RP 執行續指標，用來記錄一個AShell執行續在執行續清單裡的位置
         * @param ValueArray 變數陣列
         * @param CountRecord 運算式解析串列
         * @param Original 輸出型態
         * @param Array_Index 要取得的AShell陣列引數
         * @return 取得的值所在的AShell_Array記憶體位至，主要是用在陣列赴值用
         * @throws Exception AShell陣列查找失敗
         */
	public static String ArrayResults(AShell AS,AShell.Run_Point RP,Value_Array ValueArray,
                ArrayList<Count_Array> CountRecord,boolean Original,int Array_Index) throws Exception{
                String address=Memory_Management.move(
                        CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.toString(), Array_Index);
                StringBuilder Tent=Memory_Management.get_Array_Item(address);
                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.delete(0, 
                        CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.length());
                if(Original)
                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Tent);
                else
                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(new StrDW(AS,RP, Tent.toString(), ValueArray).Str);
                return address;
	}
            /**
         * 取得Map中的元素
         * @param AS AShell語言解析器的主類別
         *  @param RP 執行續指標，用來記錄一個AShell執行續在執行續清單裡的位置
         * @param ValueArray 變數陣列
         * @param CountRecord 運算式解析串列
         * @param Original 輸出型態
         * @param Map_Key 要取得的AShell的Map的Key
         * @return 取得的值所在的AShell_Array記憶體位至，主要是用在陣列赴值用
         * @throws Exception AShell陣列查找失敗
         */
	public static String MapResults(AShell AS,AShell.Run_Point RP,Value_Array ValueArray,
                ArrayList<Count_Array> CountRecord,boolean Original,String Map_Key) throws Exception{
                String address=Memory_Management.move(
                        CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.toString(), Map_Key);
                StringBuilder Tent=Memory_Management.get_Array_Item(address);
                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.delete(0, 
                        CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.length());
                if(Original)
                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(Tent);
                else
                    CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value.append(new StrDW(AS,RP, Tent.toString(), ValueArray).Str);
                return address;
	}
        public static void FunctionResults(AShell AS,AShell.Run_Point RP,Value_Array BUValueArray,Value_Array ValueArray,Stack<String> AShell_Memory_Type,ArrayList<StringBuilder> Args,StringBuilder Variable,
                ArrayList<Count_Array> CountRecord,boolean Original) throws Exception{
                StringBuilder Tent=CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Value;
                for(int i=0;i<Args.size();i++){
                    //將函數參數轉換成暫存狀態
                    Args.set(i, new VarStrDW(AS,RP, Args.get(i).toString(), BUValueArray, VarMode.Mode.Intermediary).Str);
                    //System.out.println("arg:"+ANR.Args.get(i).toString());
                }
                CensorValueReturn CVR;
                Function fun = null;
                Native_Function Java_Function = null;
                if(Tent.toString().startsWith("AShell_C")){
                    Class_Type AShObject,Class;
                    Class=Memory_Management.get_Class(Tent.toString());
                    AShObject=new Class_Type(Class.ValueArray);
                    AShObject.Parent=Class.Parent;
                    Variable.delete(0, Variable.length());
                    Variable.append(Memory_Management.Object_Builder(AShObject,1));//防止因為建構的時候以及隨後馬上調用類別內的動態成員函數使用this變數而被意外回收，所以定參照指數者為1，當結束StdDW或VarStdDW時就會取消參照
                    AShObject.ValueArray.add(new Value(new StringBuilder(Type_String.THIS),new StringBuilder(Variable)));
                    Super(AS,RP,AShObject);
                    AS.Run_Function(AShObject.ValueArray,Class.CodeArray,RP);
                    CVR=CensorValue.CensorValue(Type_String.INSTANCE_FUNCTION_NAME,AShObject.ValueArray,false);
                    String Assage=CVR.ValueArray.get(CVR.index).Tent.toString();
                    if(Assage.startsWith("AShell_F"))
                        fun=Memory_Management.get_Function(CVR.ValueArray.get(CVR.index).Tent.toString());
                    else
                        Java_Function=Memory_Management.get_Native_Function(Assage);
                }else if(Tent.toString().startsWith("AShell_F"))
                    fun=Memory_Management.get_Function(Tent.toString());
                else
                    Java_Function=Memory_Management.get_Native_Function(Tent.toString());//從記憶體管理器中取得java函數
                if(fun!=null){//AShell函數呼叫
                    if(Args.size()>fun.ValueArray.size())
                        throw new Exception(((!Tent.toString().startsWith("AShell_C"))?"函數":"建構式")+"並非"+Args.size()+"個參數類型的。");
                    ValueArray=new Value_Array(fun.Closure_ValueArray);//建立一個新的變數清單給函數的執行續用，取用至閉包變數清單
                    StringBuilder ValueTentTemp;
                    for(int i=0;i<fun.ValueArray.size();i++){
                        StringBuilder arg=fun.ValueArray.get(i).Name;
                        if(i<Args.size()){
                            if(!Args.get(i).toString().equals("")){//如果呼叫函數時，第 i 個參數不是空白的
                                ValueArray.add(new Value(arg,new StringBuilder(Args.get(i))));
                            }else{
                                ValueTentTemp=fun.ValueArray.get(i).Tent;
                                if(ValueTentTemp.toString().matches(Type_String.MEMORY_TYPE))//判斷所訂的值是不是記憶體參考
                                    Memory_Management.Add_To_Arguments(ValueTentTemp.toString());//將參考指數加一
                                ValueArray.add(new Value(arg,new StringBuilder(ValueTentTemp)));
                            }
                        }else{
                            ValueTentTemp=fun.ValueArray.get(i).Tent;
                            if(ValueTentTemp.toString().matches(Type_String.MEMORY_TYPE))//判斷所訂的值是不是記憶體參考
                                Memory_Management.Add_To_Arguments(ValueTentTemp.toString());//將參考指數加一
                            ValueArray.add(new Value(arg,new StringBuilder(ValueTentTemp)));
                        }
                    }
                    String Function_Return=AS.Run_Function(ValueArray,fun.CodeArray,RP);//函數回值是AShell記憶體類型的話，該參照指數就會被加一，以防止因為變數清單被銷毀而被GC回收
                    if(Tent.toString().startsWith("AShell_F")){//如果是函數呼叫
                        Variable.delete(0, Variable.length());
                        Variable.append(Function_Return);
                    }else{
                        if(Function_Return.matches(Type_String.MEMORY_TYPE))//判斷函數回傳值是不是AShell記憶體類型
                            AShell_Memory_Type.push(Function_Return);//放入AShell記憶體類型暫存堆疊
                    }
                    ValueArray.clear(AS,RP);//呼叫GC並執行完的將函數的變數清單銷毀
                }else{//native(java)函數呼叫
                    AShellType[] NFArgs=new AShellType[Args.size()];//native函數中的AShell呼叫參數清單
                    String AShellArg;
                    for(int i=0;i<Args.size();i++){
                        AShellArg=Args.get(i).toString();
                        NFArgs[i]=new AShellType().setAShell_Value((!AShellArg.equals(""))?AShellArg:Type_String.NULL);//將AShell函數呼叫參數的值轉換成java字串
                    }
                    try{
                        if(Tent.toString().startsWith("AShell_N")){
                            Variable.delete(0, Variable.length());
                            Variable.append(((AShellType)Java_Function.native_function.invoke(Java_Function.instanct_Class,new Object[]{new AShell_this(AS,RP,Java_Function.ValueArray),NFArgs})).AShell_Value);//調用函數並取得回傳值
                        }else
                            Java_Function.native_function.invoke(Java_Function.instanct_Class,new Object[]{new AShell_this(AS,RP,Java_Function.ValueArray),NFArgs});//調用native類型建構函數
                        //invoke函數第一個參數是表示要呼叫的函數所在類別的實例化物件，如果是靜態不須實例化的話則可以放入null
                    }catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
                        throw (Exception) e.getCause();//取得發生錯誤的原因
                    }finally{
                        for(int i=0;i<Args.size();i++){//將AShell函數參數中的AShell記憶體的指數減一
                            AShellArg=Args.get(i).toString();
                            if(AShellArg.matches(Type_String.MEMORY_TYPE))//判斷所訂的值是不是記憶體參考
                                Memory_Management.Cut_To_Arguments(AShellArg);//將參考指數減一
                        }
                    }
                }
                Tent.delete(0, Tent.length());
                if(Original)
                   Tent.append(Variable);
                else{
                    StringBuilder Str;
                    Tent.append(Str=new StrDW(AS,RP, Variable.toString(), ValueArray).Str);
                    if(Str.toString().matches(Type_String.MEMORY_TYPE))
                        Memory_Management.Cut_To_Arguments(Str.toString());//將參考指數減一
                }
                if(Tent.toString().matches(Type_String.MEMORY_TYPE))//判斷取得值是不是記憶體參考
                    AShell_Memory_Type.push(Tent.toString());//放入AShell記憶體類型暫存堆疊
                Variable.delete(0, Variable.length());
                //return Variable;
        }
        private static void Super(AShell AS,AShell.Run_Point RP,Class_Type AShObject) throws Exception{//建立父類別
            if(AShObject.Parent!=null){
                Class_Type Parent_Class=new Class_Type(AShObject.Parent.ValueArray);//建立父類別的實體
                Parent_Class.Parent=AShObject.Parent.Parent;
                StringBuilder Memory=Memory_Management.Object_Builder(Parent_Class,1);//目前暫時創建父類別時把參照指數定為1，參照者為super變數
                Parent_Class.ValueArray.add(new Value(new StringBuilder(Type_String.THIS),new StringBuilder(Memory)));
                AShObject.ValueArray.add(new Value(new StringBuilder(Type_String.SUPER),new StringBuilder(Memory)));
                Function _inst_=new Function(AShObject.ValueArray.Reference(),"");
                _inst_.CodeArray.add(new Command(new StringBuilder(Type_String.SUPER+"."+Type_String.INSTANCE_FUNCTION_NAME+"()"),0));
                AShObject.ValueArray.add(new Value(new StringBuilder(Type_String.INSTANCE_FUNCTION_NAME),Memory_Management.Function_Builder(_inst_,1)));//放入預設的空建構子
                AShObject.ValueArray.add(new Value(new StringBuilder(Type_String.DESTRUCTOR_FUNCTION_NAME),Memory_Management.Function_Builder(new Function(AShObject.ValueArray.Reference(),""),1)));//放入預設的空解構子
                Super(AS,RP,Parent_Class);//建立父類別的父類別
                AS.Run_Function(Parent_Class.ValueArray,AShObject.Parent.CodeArray,RP);
                for(int i=0;i<Parent_Class.ValueArray.size();i++){//繼承變數實做
                    if(!Parent_Class.ValueArray.get(i).Name.toString().matches(Type_String.THIS+"|"+Type_String.SUPER+"|"+Type_String.DESTRUCTOR_FUNCTION_NAME+"|"+Type_String.INSTANCE_FUNCTION_NAME)){
                        Value V;
                        if((V=Parent_Class.ValueArray.get(i)).Tent.toString().matches(Type_String.MEMORY_TYPE))//判斷原變數內容是不是記憶體參考
                            Memory_Management.Add_To_Arguments(V.Tent.toString());//將參考指數加一
                        AShObject.ValueArray.add(V);//從父變數清單複製一份變數加入倒子變數清單，這個功能就等同於覆寫
                    }
                }
            }else{//當沒有父類別時
                AShObject.ValueArray.add(new Value(new StringBuilder(Type_String.INSTANCE_FUNCTION_NAME),Memory_Management.Function_Builder(new Function(AShObject.ValueArray.Reference(),""),1)));//放入預設的空建構子
                AShObject.ValueArray.add(new Value(new StringBuilder(Type_String.DESTRUCTOR_FUNCTION_NAME),Memory_Management.Function_Builder(new Function(AShObject.ValueArray.Reference(),""),1)));//放入預設的空解構子
            }
        }
        
	/**建立變數
     * @param AS AShell語言解析器的主類別
     * @param ValueName 欲建立的變數之清單
     * @param Str 將賦予給變數的內容
     * @param  Mode 為Var的話，變數存在的話就從變數清單去除掉，然後重新建立，否則為General只更換該變數的值
     * @throws java.lang.Exception
         **/
	public static void SetValue(AShell AS,ArrayList<VarValueName> ValueName,StringBuilder Str,VarMode.Mode Mode) throws Exception{
		CensorValueReturn CVR;
		for(VarValueName SV:ValueName){
			if(!SV.name.toString().startsWith("AShell")){//如果變數名稱不是記憶體位置
                                if(!SV.name.toString().matches(Type_String.VALUE_NAME)||SV.name.toString().matches(Type_String.NULL+"|"+Type_String.TRUE+"|"+Type_String.FALSE))//正規表示法，第一個為A-Z、a_z、_、$，接下來的字為A-Z、a-z、0-9、_、$，後面的*為重複無限次，等價於{0,}({m,n}為重複m到n次)。
                                    throw new ArithmeticException("變數名稱'"+SV.name.toString()+"'不被允許。");
                                CVR=CensorValue.CensorValue(SV.name.toString(),SV.ValueArray,Mode==VarMode.Mode.Var);
                                    if(CVR.Result){
                                        Value Address;
                                        if((Address=CVR.ValueArray.get(CVR.index)).Tent.toString().matches(Type_String.MEMORY_TYPE))//判斷原變數內容是不是記憶體參考
                                            Memory_Management.Cut_To_Arguments(Address.Tent.toString());//將參考指數減一
                                        if(Mode==VarMode.Mode.Var){
                                            /*因為覆寫功能並不完善所以先註解掉(在子類別使用super呼叫父類別的函數不會是原本的函數，而是被覆寫過後子類別函數，所以並不完善)
                                            if(!SV.ValueArray.SClass&&SV.ValueArray.get(CVR.index).Tent.toString().matches(Type_String.FUNCTION_M+"|"+Type_String.NATIVE_FUNCTION_M)
                                                    &&Str.toString().matches(Type_String.FUNCTION_M+"|"+Type_String.NATIVE_FUNCTION_M))//當在var模式下發現要宣告的變數已經存在且是函數 且  宣告的變數是函數
                                                SV.ValueArray.get(CVR.index).Tent=Str;//覆寫掉已存在的函數，因為AShell的父類別變數是複製變數記憶體到子類別的變數清單，所以當找到有一樣名稱的函數時就可以判斷說是來自父類別的，就算只是在同一層變數清單中宣告同樣名稱的函數兩次用複寫的也比較好，省記憶體
                                            else*/
                                            SV.ValueArray.set(CVR.index,new Value(SV.name,Str));
                                        }else{
                                            Address.Tent=Str;
                                        }
                                    }else
                                        SV.ValueArray.add(new Value(SV.name,Str));
			}else{
				StringBuilder Tent;//移動記憶體指標並取得該記憶體中的值
                                if((Tent=Memory_Management.get_Array_Item(SV.name.toString())).toString().matches(Type_String.MEMORY_TYPE))//判斷原變數內容是不是記憶體參考
                                    Memory_Management.Cut_To_Arguments(Tent.toString());//將參考指數減一
                                Tent.delete(0, Tent.length());
                                Tent.append(Str);
			}/*else
                            throw new ArithmeticException("左值型態錯誤。");*/
		}
	}
	/**建立一個值為空的變數或空值陣列
     * @param AS AShell解析器
     * @param RP 執行續指標，用來記錄一個AShell執行續在執行續清單裡的位置
     * @param ValueArray 所在執行續之變數清單
     * @param Variable 變數名稱
     * @throws java.lang.Exception
         **/
        private static final char[] LOCK=new char[0];//除了用來當ArrayCreate函數的同步所外就沒其他功能了
	public static void SetNullValue(AShell AS,AShell.Run_Point RP,Value_Array ValueArray,StringBuilder Variable) throws Exception{
		CensorValueReturn CVR;
		ArrayNameResolve ANR=new ArrayNameResolve(Variable.toString());
		if(!ANR.Name.toString().matches(Type_String.VALUE_NAME)||ANR.Name.toString().matches(Type_String.NULL+"|"+Type_String.TRUE+"|"+Type_String.FALSE))//正規表示法，第一個為A-Z、a_z、_、$，接下來的字為A-Z、a-z、0-9、_、$，後面的*為重複無限次，等價於{0,}({m,n}為重複m到n次)。
			throw new ArithmeticException("變數名稱'"+ANR.Name.toString()+"'不被允許。");
		if(ANR.Type==0){
			CVR=CensorValue.CensorValue(Variable.toString(),ValueArray,true);
                        if(CVR.Result){
                            ValueArray.set(CVR.index,new Value(Variable,new StringBuilder(Type_String.NULL)));
                        }else
                            ValueArray.add(new Value(Variable,new StringBuilder(Type_String.NULL)));
		}else{
			ArrayList<Integer> ArraySpecification=new ArrayList<>();//要建立的AShell陣列規格
			for(StringBuilder SB:ANR.Args){//將取得到的陣列參數轉成數字
				int Number_doping=(int) (long)Math.floor(IsNumeric.isNumeric(
						new VarStrDW(AS,RP, SB.toString(), ValueArray,VarMode.Mode.Intermediary).Str.toString()));
				if(Number_doping<=0)
					throw new Exception("陣列'"+ANR.Name.toString()+"'參數小於等於 0");
				ArraySpecification.add(Number_doping);
			}
                        //將陣列初始化
                        /**       AShell的陣列形式意示圖(以二維陣列為例) : 
            +-------------+--------------+--------------+--------------+-------------+--------------+-------------+-------------+
      資料  | 記憶體位置C | 記憶體位置F  |    資料A     |     資料B    |    資料C    |     資料D    |     資料E   |     資料F   |
	        |-------------|--------------|--------------|--------------|-------------|--------------|-------------|-------------|
 記憶體位置 | 記憶體位置A | 記憶體位置B  | 記憶體位置C  | 記憶體位置D  | 記憶體位置E | 記憶體位置F  | 記憶體位置G | 記憶體位置H |
	        +-------------+--------------+--------------+--------------+-------------+--------------+-------------+-------------+       
   **/ 
                        int frequency=1;//這是用來判斷每一維要建立幾個變數記憶體
                        StringBuilder passge = null/*紀錄上一維陣列的起始記憶體位置*/, ArrayStyle=null/*紀錄這個陣列的開頭*/;
                        synchronized(LOCK){
                            for(int i=0;i<ArraySpecification.size();i++){
                                frequency*=ArraySpecification.get(i);
                                StringBuilder first=null;//紀錄這一維陣列的起始記憶體位置
                                for(int j=0,index=0;j<frequency;j++){
                                    if(j%ArraySpecification.get(i)==0){//如果這是該維陣列的第一個元素
                                        //-----------------------------建立陣列大小函數-----------------------------------------------------------------------------
                                        Value_Array VA = new Value_Array(null);
                                        Function size=new Function(null,"");
                                        size.CodeArray.add(new Command(new StringBuilder(Code_String.RETURN+" "+ArraySpecification.get(i)),0));
                                        VA.add(new Value(new StringBuilder("size"),Memory_Management.Function_Builder(size,1)));
                                        //------------------------------------------------------------------------------------------------------------------------------------
                                        StringBuilder assge=Memory_Management.Array_Builder(null,new StringBuilder((i+1==ArraySpecification.size())?Type_String.NULL:""),VA);
                                        Memory_Management.Add_To_Arguments(assge.toString());
                                        Memory_Management.set_Array_Sise(assge.toString(), ArraySpecification.get(i));
                                        if(i==0&&j==0)//如果這次是建立陣列的第一個值
                                            ArrayStyle=assge;//將陣列第一個值的記憶體位置指定給變數值
                                        else{
                                           Memory_Management.get_Array_Item(Memory_Management.move(passge.toString(), index)).append(assge);//在上一維對應的變數記憶體中放入這次所建立的變數記憶體位置
                                           index++;
                                        }
                                        if(j==0)//如果這次是建立這一維第一個變數記憶體位置
                                            first=assge;//將建立的地役體位置指定給<紀錄這一維陣列的起始記憶體位置>變數
                                    }else
                                        Memory_Management.Array_Builder(null,new StringBuilder((i+1==ArraySpecification.size())?Type_String.NULL:""),null);
                                }
                                passge=first;
                            }
                        }
                        ArraySpecification.clear();
			CVR=CensorValue.CensorValue(ANR.Name.toString(),ValueArray,true);
                        if(CVR.Result)
                            ValueArray.set(CVR.index,new Value(ANR.Name,ArrayStyle));
                        else
                            ValueArray.add(new Value(ANR.Name,ArrayStyle));
		}
	}
	public static void UnVar(AShell AS,Value_Array ValueArray,StringBuilder Variable) throws Exception{
		if(!Variable.toString().matches(Type_String.VALUE_NAME)||Variable.toString().matches(Type_String.NULL+"|"+Type_String.TRUE+"|"+Type_String.FALSE))//正規表示法，第一個為A-Z、a_z、_、$，接下來的字為A-Z、a-z、0-9、_、$，後面的*為重複無限次，等價於{0,}({m,n}為重複m到n次)。
			throw new ArithmeticException("變數名稱'"+Variable+"'不被允許。");
		CensorValueReturn CVR=CensorValue.CensorValue(Variable.toString(),ValueArray,false);
                if(CVR.Result){
                        Value value=CVR.ValueArray.remove(CVR.index);
                        if(value.Tent.toString().matches(Type_String.MEMORY_TYPE))//判斷原變數內容是不是記憶體參考
                            Memory_Management.Cut_To_Arguments(value.Tent.toString());//將參考指數減一
                }else
                        throw new ArithmeticException("變數名稱'"+Variable.toString()+"'不存在。");
        }
}

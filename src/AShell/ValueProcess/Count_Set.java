package AShell.ValueProcess;

import AShell.Data_Type_And_Struct.Code_String;
import AShell.Data_Type_And_Struct.IsNumeric;
import AShell.Data_Type_And_Struct.Type_String;
import static java.lang.Math.pow;
import java.util.ArrayList;
/** 因為先乘除後加減的關西，所以這個函數是用來處理乘與除的
	 * 在ACMD上，1+2*2-3會被儲存成
	 * +---+---+---+----+
	 * | 1 | 2 | 2 | 3  |
	 * +---+---+---+----+
	 * | + | * | - |null|
	 * +---+---+---+----+**/
public class Count_Set {
	/** 最優先處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param Operator 下一個運算子*/
	public static void CountSet(ArrayList<Count_Array> CountRecord,String Operator){
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                    if(CountRecord.get(CountRecord.size()-1).
                            get(CountRecord.get(CountRecord.size()-1).size()-2).
                            Operator!=null){
                        switch (CountRecord.get(CountRecord.size()-1).
                                get(CountRecord.get(CountRecord.size()-1).size()-2).
                                Operator) {
                            case ".":
                                CountRecord.get(CountRecord.size()-1).remove(CountRecord.get(CountRecord.size()-1).size()-2);
                        }
                    }
		}
                CountRecord.get(CountRecord.size()-1).get(CountRecord.get(CountRecord.size()-1).size()-1).Operator=Operator;
	}
        /** 第一優先處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @return 接下來須被運算的項目在陣列中的位置
     * @throws java.lang.Exception 運算子'!'使用錯誤
     */
        public static int CountSet1(ArrayList<Count_Array> CountRecord,int index) throws Exception{
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                    if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                        return ++index;
                    switch (CountRecord.get(CountRecord.size()-1).
                            get(index).
                            Operator) {
                        case "+":
                        {
                            switch (CountRecord.get(CountRecord.size()-1).get(index).Value.toString()) {
                                case "":
                                    switch(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString()){
                                        case "":
                                            CountSet1(CountRecord,index+1);
                                        default:
                                            BooleanChange(CountRecord,index);
                                            double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(String.valueOf(a).matches(Type_String.INTEGER)?String.format("%.0f", a):a);
                                            CountRecord.get(CountRecord.size()-1).remove(index);
                                    }
                                    break;
                                default:
                                    index++;
                            }
                            break;
                        }
                        case "-":
                        {
                            switch (CountRecord.get(CountRecord.size()-1).get(index).Value.toString()) {
                                case "":
                                    switch(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString()){
                                        case "":
                                            CountSet1(CountRecord,index+1);
                                        default:
                                            BooleanChange(CountRecord,index);
                                            double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(String.valueOf(a).matches(Type_String.INTEGER)?String.format("%.0f", -a):-a);
                                            CountRecord.get(CountRecord.size()-1).remove(index);
                                    }
                                    break;
                                default:
                                    index++;
                            }
                            break;
                        }
                        case "!":
                        case Code_String.NOT:
                        {
                            switch (CountRecord.get(CountRecord.size()-1).get(index).Value.toString()) {
                                case "":
                                    switch(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString()){
                                        case "":
                                            CountSet1(CountRecord,index+1);
                                        default:
                                            if(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString().matches(Type_String.FALSE+"|[-]?0")){
                                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.TRUE);
                                            }else{
                                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.FALSE);
                                            }
                                            CountRecord.get(CountRecord.size()-1).remove(index);
                                    }
                                    break;
                                default:
                                    throw new Exception("語法錯誤，運算子'"+CountRecord.get(CountRecord.size()-1).get(index).Operator+"'使用錯誤");
                            }
                            break;
                        }
                        case "~":
                        {
                            switch (CountRecord.get(CountRecord.size()-1).get(index).Value.toString()) {
                                case "":
                                    switch(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString()){
                                        case "":
                                            CountSet1(CountRecord,index+1);
                                        default:
                                            BooleanChange(CountRecord,index);
                                            long a=(long)IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(~a);
                                            CountRecord.get(CountRecord.size()-1).remove(index);
                                    }
                                    break;
                                default:
                                    throw new Exception("語法錯誤，運算子'~'使用錯誤");
                            }
                            break;
                        }
                        default:
                            index++;
                    }
		}
                return index;
	}
        /** 第二優先處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @return 負數處理判斷，用來判斷這個這次運算子後面的 - 是負號還是減號 ，如果為null就代表著已經是運算式的末端，後面沒東西了*/
	public static int CountSet2(ArrayList<Count_Array> CountRecord,int index){
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                        if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                            return ++index;
                        switch (CountRecord.get(CountRecord.size()-1).
                                get(index).
                                Operator) {
                            case "**":
                            {
                                if (CountRecord.get(CountRecord.size()-1).get(index+1).Operator!=null&&
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Operator.equals("**"))
                                    CountSet2(CountRecord,index+1);
                                else{
                                    BooleanChange(CountRecord,index);
                                    double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                        b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                    a=pow(a,b);
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(String.valueOf(a).matches(Type_String.INTEGER)?String.format("%.0f", a):a);
                                    CountRecord.get(CountRecord.size()-1).remove(index);
                                }
                                break;
                            }
                            default:
                                index++;
                        }
		}
            return index;
	}
        /** 第三優先處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @param isStrDW 判斷是否為StrDW，會有這個變數是因為在StrDW中所有字串的"都會在進到這個函數前被去除掉，所以都會以前後項都沒有"的方式去處理，為了避免前巷項或後項是null或true或false而被加上了"才多設了這個變數
     * @return 負數處理判斷，用來判斷這個這次運算子後面的 - 是負號還是減號 ，如果為null就代表著已經是運算式的末端，後面沒東西了*/
	public static int CountSet3(ArrayList<Count_Array> CountRecord,int index,boolean isStrDW){
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                        if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                            return ++index;
                        switch (CountRecord.get(CountRecord.size()-1).
                                get(index).
                                Operator) {
                            case "*":
                            {
                                BooleanChange(CountRecord,index);
                                double b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                                if(!IsNumeric.isNumericTest(CountRecord.get(CountRecord.size()-1).get(index).Value.toString())){//檢查前是否第一項不為數字
                                    StringBuilder Temp=CountRecord.get(CountRecord.size()-1).get(index).Value;
                                    if(!isStrDW&&CountRecord.get(CountRecord.size()-1).get(index).Value.length()!=0
                                            &&CountRecord.get(CountRecord.size()-1).get(index).Value.charAt(0)=='\"'){//如果不是StrDW且字串長度不為零，且+運算子前項有用"包住，會有!StrDW是因為如果參數是 \" 到了StrDW前面的 \ 就會被拿掉
                                        Temp.deleteCharAt(0);//將後項前端的"去除掉
                                        Temp.deleteCharAt(Temp.length()-1);//將前項尾端的"去除掉
                                        repeat((long)b,Temp,CountRecord.get(CountRecord.size()-1).get(index+1).Value,isStrDW);
                                    }else{//如果+運算子前項沒有用"包住，或字串長度為零
                                        repeat((long)b,Temp,CountRecord.get(CountRecord.size()-1).get(index+1).Value,isStrDW);
                                    }
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.append("\"");
                                }else{
                                    double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString());
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                    a*=b;
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(String.valueOf(a).matches(Type_String.INTEGER)?String.format("%.0f", a):a);
                                }
                                CountRecord.get(CountRecord.size()-1).remove(index);
                                break;
                            }
                            case "/":
                            {
                                BooleanChange(CountRecord,index);
                                double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                a/=b;
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(String.valueOf(a).matches(Type_String.INTEGER)?String.format("%.0f", a):a);
                                CountRecord.get(CountRecord.size()-1).remove(index);
                                break;
                            }
                            case "//":
                            {
                                BooleanChange(CountRecord,index);
                                double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                a/=b;
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append((long)a);
                                CountRecord.get(CountRecord.size()-1).remove(index);
                                break;
                            }
                            case "%":
                            {
                                BooleanChange(CountRecord,index);
                                double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                a%=b;
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(String.valueOf(a).matches(Type_String.INTEGER)?String.format("%.0f", a):a);
                                CountRecord.get(CountRecord.size()-1).remove(index);
                                break;
                            }
                            case "%%":
                            {
                                BooleanChange(CountRecord,index);
                                double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                a%=b;
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append((long)a);
                                CountRecord.get(CountRecord.size()-1).remove(index);
                                break;
                            }
                            default:
                                index++;
                        }
		}
            return index;
	}
        /** 第四優先處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @param isStrDW 判斷是否為StrDW，會有這個變數是因為在StrDW中所有字串的"都會在進到這個函數前被去除掉，所以都會以前後項都沒有"的方式去處理，為了避免前巷項或後項是null或true或false而被加上了"才多設了這個變數
     * @return 接下來須被運算的項目在陣列中的位置*/
        public static int CountSet4(ArrayList<Count_Array> CountRecord,int index,boolean isStrDW){
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                    if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                        return ++index;
                    switch (CountRecord.get(CountRecord.size()-1).
                            get(index).
                            Operator) {
                        case "+":/*目前布林直無法進行加號運算*/
                            /**在StrDW中字串類型引號(")會再取值時被去除掉，所以當你要呼叫native write顯示例如"2"+"3"時，來到這裡時就會變成2+3，所以會顯示5，
                                                                    且如果是""，再來到這裡時也會變成什麼都沒有且長度為零的空字串**/
                            BooleanChange(CountRecord,index);
                            if(!IsNumeric.isNumericTest(CountRecord.get(CountRecord.size()-1).get(index).Value.toString())||
                                    !IsNumeric.isNumericTest(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString())){//檢查前後兩項是否有一項不為數字
                                if(!isStrDW&&CountRecord.get(CountRecord.size()-1).get(index).Value.length()!=0
                                        &&CountRecord.get(CountRecord.size()-1).get(index).Value.charAt(0)=='\"'){//如果不是StrDW且字串長度不為零，且+運算子前項有用"包住，會有!StrDW是因為如果參數是 \" 到了StrDW前面的 \ 就會被拿掉
                                    CountRecord.get(CountRecord.size()-1).get(index).Value.deleteCharAt(
                                            CountRecord.get(CountRecord.size()-1).get(index).Value.length()-1);//將前項尾端的"去除掉
                                    if(CountRecord.get(CountRecord.size()-1).get(index+1).Value.length()!=0
                                            &&CountRecord.get(CountRecord.size()-1).get(index+1).Value.charAt(0)=='\"'){//如果字串長度不為零，且+運算子後項也有用"包住
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.deleteCharAt(0);//將後項前端的"去除掉
                                        CountRecord.get(CountRecord.size()-1).get(index).Value.append(
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value);//兩項相接
                                    }else{//如果+運算子後項沒有用"包住，或字串長度為零
                                        CountRecord.get(CountRecord.size()-1).get(index).Value.append(
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value).append("\"");//兩項相接並在後項尾端加上"
                                    }
                                }else{//如果+運算子前項沒有用"包住，或字串長度為零
                                    if(!isStrDW&&CountRecord.get(CountRecord.size()-1).get(index+1).Value.length()!=0
                                            &&CountRecord.get(CountRecord.size()-1).get(index+1).Value.charAt(0)=='\"'){//如果不是StrDW且字串長度不為零，且+運算子後項有用"包住，會有!StrDW是因為如果參數是 \" 到了StrDW前面的 \ 就會被拿掉
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.deleteCharAt(0);//將後項前端的"去除掉
                                        CountRecord.get(CountRecord.size()-1).get(index).Value=new StringBuilder().append("\"").append(
                                            CountRecord.get(CountRecord.size()-1).get(index).Value).append(
                                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value);//兩項相接並在前項頂端加上"
                                    }else{//如果+運算子後項也沒有用"包住，或字串長度為零
                                        if(!isStrDW&&(CountRecord.get(CountRecord.size()-1).get(index).Value.toString().matches(Type_String.ASHELL_TYPE_EXCLUDE_DIGITAL)
                                                ||CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString().matches(Type_String.ASHELL_TYPE_EXCLUDE_DIGITAL)))//如果不是StrDW且前項是null或true或false或記憶體位置，或後項是null或true或false或記憶體位置
                                            CountRecord.get(CountRecord.size()-1).get(index).Value=new StringBuilder().append("\"").append(
                                                CountRecord.get(CountRecord.size()-1).get(index).Value).append(
                                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value).append("\"");//兩項相接並在前項頂端後項尾端加上"
                                        else
                                            CountRecord.get(CountRecord.size()-1).get(index).Value.append(
                                                CountRecord.get(CountRecord.size()-1).get(index+1).Value);//兩項相接
                                    }
                                }
                                CountRecord.get(CountRecord.size()-1).get(index).Operator=CountRecord.get(CountRecord.size()-1).get(index+1).Operator;
                                CountRecord.get(CountRecord.size()-1).remove(index+1);
                            }else{
                                double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                        b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                a+=b;
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(String.valueOf(a).matches(Type_String.INTEGER)?String.format("%.0f", a):a);
                                CountRecord.get(CountRecord.size()-1).remove(index);
                            }
                            break;
                        case ".."://用於兩個字元相連，就連數字也行
                            if(!IsNumeric.isNumericTest(CountRecord.get(CountRecord.size()-1).get(index).Value.toString())||
                                    !IsNumeric.isNumericTest(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString())){//檢查前後兩項是否有一項不為數字
                                if(!isStrDW&&CountRecord.get(CountRecord.size()-1).get(index).Value.length()!=0
                                        &&CountRecord.get(CountRecord.size()-1).get(index).Value.charAt(0)=='\"'){//如果不是StrDW且字串長度不為零，且+運算子前項有用"包住，會有!StrDW是因為如果參數是 \" 到了StrDW前面的 \ 就會被拿掉
                                    CountRecord.get(CountRecord.size()-1).get(index).Value.deleteCharAt(
                                            CountRecord.get(CountRecord.size()-1).get(index).Value.length()-1);//將前項尾端的"去除掉
                                    if(CountRecord.get(CountRecord.size()-1).get(index+1).Value.length()!=0
                                            &&CountRecord.get(CountRecord.size()-1).get(index+1).Value.charAt(0)=='\"'){//如果字串長度不為零，且+運算子後項也有用"包住
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.deleteCharAt(0);//將後項前端的"去除掉
                                        CountRecord.get(CountRecord.size()-1).get(index).Value.append(
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value);//兩項相接
                                    }else{//如果+運算子後項沒有用"包住，或字串長度為零
                                        CountRecord.get(CountRecord.size()-1).get(index).Value.append(
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value).append("\"");//兩項相接並在後項尾端加上"
                                    }
                                }else{//如果+運算子前項沒有用"包住，或字串長度為零
                                    if(!isStrDW&&CountRecord.get(CountRecord.size()-1).get(index+1).Value.length()!=0
                                            &&CountRecord.get(CountRecord.size()-1).get(index+1).Value.charAt(0)=='\"'){//如果不是StrDW且字串長度不為零，且+運算子後項有用"包住，會有!StrDW是因為如果參數是 \" 到了StrDW前面的 \ 就會被拿掉
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.deleteCharAt(0);//將後項前端的"去除掉
                                        CountRecord.get(CountRecord.size()-1).get(index).Value=new StringBuilder().append("\"").append(
                                            CountRecord.get(CountRecord.size()-1).get(index).Value).append(
                                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value);//兩項相接並在前項頂端加上"
                                    }else{//如果+運算子後項也沒有用"包住，或字串長度為零
                                        if(!isStrDW&&(CountRecord.get(CountRecord.size()-1).get(index).Value.toString().matches(Type_String.ASHELL_TYPE_EXCLUDE_DIGITAL)
                                                ||CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString().matches(Type_String.ASHELL_TYPE_EXCLUDE_DIGITAL)))//如果不是StrDW且前項是null或true或false或記憶體位置，或後項是null或true或false或記憶體位置或負整數
                                            CountRecord.get(CountRecord.size()-1).get(index).Value=new StringBuilder().append("\"").append(
                                                CountRecord.get(CountRecord.size()-1).get(index).Value).append(
                                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value).append("\"");//兩項相接並在前項頂端後項尾端加上"
                                        else{
                                            CountRecord.get(CountRecord.size()-1).get(index).Value.append(
                                                CountRecord.get(CountRecord.size()-1).get(index+1).Value);//兩項相接
                                        }
                                    }
                                }
                                CountRecord.get(CountRecord.size()-1).get(index).Operator=CountRecord.get(CountRecord.size()-1).get(index+1).Operator;
                                CountRecord.get(CountRecord.size()-1).remove(index+1);
                            }else{
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value=new StringBuilder().append("\"").append(
                                                CountRecord.get(CountRecord.size()-1).get(index).Value).append(
                                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value).append("\"");//兩項相接並在前項頂端後項尾端加上"
                                CountRecord.get(CountRecord.size()-1).remove(index);
                            }
                            break;
                        case "-":
                            BooleanChange(CountRecord,index);
                            double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                            a-=b;
                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(String.valueOf(a).matches(Type_String.INTEGER)?String.format("%.0f", a):a);
                            CountRecord.get(CountRecord.size()-1).remove(index);
                            break;
                        default:
                            index++;
                    }
		}
                return index;
	}
        /** 第五優先處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @return 接下來須被運算的項目在陣列中的位置*/
        public static int CountSet5(ArrayList<Count_Array> CountRecord,int index){
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                    if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                        return ++index;
                    switch (CountRecord.get(CountRecord.size()-1).
                            get(index).
                            Operator) {
                        case "<<":
                        {
                            BooleanChange(CountRecord,index);
                            long a=(long)IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=(long)IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(a<<b);
                            CountRecord.get(CountRecord.size()-1).remove(index);
                            break;
                        }
                        case ">>":
                        {
                            BooleanChange(CountRecord,index);
                            long a=(long)IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=(long)IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(a>>b);
                            CountRecord.get(CountRecord.size()-1).remove(index);
                            break;
                        }
                        default:
                            index++;
                    }
		}
                return index;
	}
        
        /**特殊判斷模式的判斷方式
[2<3>4]
會被表示成
+---+---+----+
| 2 | 3 | 4  | <-數值
+---+---+----+
| < | > |null| <-運算子
+---+---+----+
經過第一次計算，2<3為真，所以會變成
+---+----+
| 3 | 4  |
+---+----+
| > |null|
+---+----+
數值並不會被改為true，因為接下來還有判斷要做
不過3>4為假，所以拋出false錯誤
**/
        
        /** 第六優先處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @return 接下來須被運算的項目在陣列中的位置*/
        public static int CountSet6(ArrayList<Count_Array> CountRecord,int index) throws Exception{
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                    if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                        return ++index;
                    switch (CountRecord.get(CountRecord.size()-1).
                            get(index).
                            Operator) {
                        case ">=":
                        {
                            BooleanChange(CountRecord,index);
                            double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                            if(!CountRecord.get(CountRecord.size()-1).SpecialLogic){//如果不是特殊邏輯模式
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append((a>=b)?Type_String.TRUE:Type_String.FALSE);
                            }else{
                                if(!(a>=b))
                                    throw new Exception("false");
                                if(CountRecord.get(CountRecord.size()-1).get(index+1).Operator==null||!CountRecord.get(CountRecord.size()-1).get(index+1).Operator.matches(">|>=|<|<=")){//如果接下來沒有運算子 或 接下來的運算子不是同層級的運算子
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                     CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.TRUE);
                                }
                            }
                            CountRecord.get(CountRecord.size()-1).remove(index);
                            break;
                        }
                        case "<=":
                        {
                            BooleanChange(CountRecord,index);
                            double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                            if(!CountRecord.get(CountRecord.size()-1).SpecialLogic){//如果不是特殊邏輯模式
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append((a<=b)?Type_String.TRUE:Type_String.FALSE);
                            }else{
                                if(!(a<=b))
                                    throw new Exception("false");
                                if(CountRecord.get(CountRecord.size()-1).get(index+1).Operator==null||!CountRecord.get(CountRecord.size()-1).get(index+1).Operator.matches(">|>=|<|<=")){//如果接下來沒有運算子 或 接下來的運算子不是同層級的運算子
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                     CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.TRUE);
                                }
                            }
                            CountRecord.get(CountRecord.size()-1).remove(index);
                            break;
                        }
                        case ">":
                        {
                            BooleanChange(CountRecord,index);
                            double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                            if(!CountRecord.get(CountRecord.size()-1).SpecialLogic){//如果不是特殊邏輯模式
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append((a>b)?Type_String.TRUE:Type_String.FALSE);
                            }else{
                                if(!(a>b))
                                    throw new Exception("false");
                                if(CountRecord.get(CountRecord.size()-1).get(index+1).Operator==null||!CountRecord.get(CountRecord.size()-1).get(index+1).Operator.matches(">|>=|<|<=")){//如果接下來沒有運算子 或 接下來的運算子不是同層級的運算子
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                     CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.TRUE);
                                }
                            }
                            CountRecord.get(CountRecord.size()-1).remove(index);
                            break;
                        }
                        case "<":
                        {
                            BooleanChange(CountRecord,index);
                            double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                            if(!CountRecord.get(CountRecord.size()-1).SpecialLogic){//如果不是特殊邏輯模式
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append((a<b)?Type_String.TRUE:Type_String.FALSE);
                            }else{
                                if(!(a<b))
                                    throw new Exception("false");
                                if(CountRecord.get(CountRecord.size()-1).get(index+1).Operator==null||!CountRecord.get(CountRecord.size()-1).get(index+1).Operator.matches(">|>=|<|<=")){//如果接下來沒有運算子 或 接下來的運算子不是同層級的運算子
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                     CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.TRUE);
                                }
                            }
                            CountRecord.get(CountRecord.size()-1).remove(index);
                            break;
                        }
                        default:
                            index++;
                    }
		}
                return index;
	}
        
        /** 第七優先處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @return 接下來須被運算的項目在陣列中的位置*/
        public static int CountSet7(ArrayList<Count_Array> CountRecord,int index) throws Exception{
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                    if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                        return ++index;
                    switch (CountRecord.get(CountRecord.size()-1).
                            get(index).
                            Operator) {
                        case "==":
                        case Code_String.IS:
                            BooleanChange(CountRecord,index);
                            if(!IsNumeric.isNumericTest(CountRecord.get(CountRecord.size()-1).get(index).Value.toString())||
                                    !IsNumeric.isNumericTest(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString())){
                                String item2=CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString();
                                if(!CountRecord.get(CountRecord.size()-1).SpecialLogic){//如果不是特殊邏輯模式
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(
                                        (item2.equals(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()))?Type_String.TRUE:Type_String.FALSE);
                                }else{
                                    if(!item2.equals(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()))
                                        throw new Exception("false");
                                    if(CountRecord.get(CountRecord.size()-1).get(index+1).Operator==null||!CountRecord.get(CountRecord.size()-1).get(index+1).Operator.matches(Code_String.IS+"|==|!=|<>|><")){//如果接下來沒有運算子 或 接下來的運算子不是同層級的運算子
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                         CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.TRUE);
                                    }
                                }
                                CountRecord.get(CountRecord.size()-1).remove(index);
                            }else{
                                double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                       b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                                if(!CountRecord.get(CountRecord.size()-1).SpecialLogic){//如果不是特殊邏輯模式
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.append((a==b)?Type_String.TRUE:Type_String.FALSE);
                                }else{
                                    if(!(a==b))
                                        throw new Exception("false");
                                    if(CountRecord.get(CountRecord.size()-1).get(index+1).Operator==null||!CountRecord.get(CountRecord.size()-1).get(index+1).Operator.matches(Code_String.IS+"|==|!=|<>|><")){//如果接下來沒有運算子 或 接下來的運算子不是同層級的運算子
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.TRUE);
                                    }
                                }
                                CountRecord.get(CountRecord.size()-1).remove(index);
                            }
                            break;
                        case "!=":
                        case "<>":
                        case "><":
                            BooleanChange(CountRecord,index);
                            if(!IsNumeric.isNumericTest(CountRecord.get(CountRecord.size()-1).get(index).Value.toString())||
                                    !IsNumeric.isNumericTest(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString())){
                                String item2=CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString();
                                if(!CountRecord.get(CountRecord.size()-1).SpecialLogic){//如果不是特殊邏輯模式
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(
                                        (!item2.equals(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()))?Type_String.TRUE:Type_String.FALSE);
                                }else{
                                    if(item2.equals(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()))
                                        throw new Exception("false");
                                    if(CountRecord.get(CountRecord.size()-1).get(index+1).Operator==null||!CountRecord.get(CountRecord.size()-1).get(index+1).Operator.matches(Code_String.IS+"|==|!=|<>|><")){//如果接下來沒有運算子 或 接下來的運算子不是同層級的運算子
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                         CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.TRUE);
                                    }
                                }
                                CountRecord.get(CountRecord.size()-1).remove(index);
                            }else{
                                double a=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                       b=IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                                if(!CountRecord.get(CountRecord.size()-1).SpecialLogic){//如果不是特殊邏輯模式
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.append((a!=b)?Type_String.TRUE:Type_String.FALSE);
                                }else{
                                    if(!(a!=b))
                                        throw new Exception("false");
                                    if(CountRecord.get(CountRecord.size()-1).get(index+1).Operator==null||!CountRecord.get(CountRecord.size()-1).get(index+1).Operator.matches(Code_String.IS+"|==|!=|<>|><")){//如果接下來沒有運算子 或 接下來的運算子不是同層級的運算子
                                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                         CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.TRUE);
                                    }
                                }
                                CountRecord.get(CountRecord.size()-1).remove(index);
                            }
                            break;
                        default:
                            index++;
                    }
		}
                return index;
	}
        
        
        /** 第八優先處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @return 接下來須被運算的項目在陣列中的位置*/
        public static int CountSet8(ArrayList<Count_Array> CountRecord,int index){
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                    if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                        return ++index;
                    switch (CountRecord.get(CountRecord.size()-1).
                            get(index).
                            Operator) {
                        case "&":
                            BooleanChange(CountRecord,index);
                            long a=(long)IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=(long)IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(a&b);
                            CountRecord.get(CountRecord.size()-1).remove(index);
                            break;
                        default:
                            index++;
                    }
		}
                return index;
	}
        /** 第九優先處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @return 接下來須被運算的項目在陣列中的位置*/
        public static int CountSet9(ArrayList<Count_Array> CountRecord,int index){
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                    if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                        return ++index;
                    switch (CountRecord.get(CountRecord.size()-1).
                            get(index).
                            Operator) {
                        case "^":
                            BooleanChange(CountRecord,index);
                            long a=(long)IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=(long)IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(a^b);
                            CountRecord.get(CountRecord.size()-1).remove(index);
                            break;
                        default:
                            index++;
                    }
		}
                return index;
	}
        /** 第十優先處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @return 接下來須被運算的項目在陣列中的位置*/
        public static int CountSet10(ArrayList<Count_Array> CountRecord,int index){
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                    if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                        return ++index;
                    switch (CountRecord.get(CountRecord.size()-1).
                            get(index).
                            Operator) {
                        case "|":
                            BooleanChange(CountRecord,index);
                            long a=(long)IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index).Value.toString()),
                                    b=(long)IsNumeric.isNumeric(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString());
                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(a|b);
                            CountRecord.get(CountRecord.size()-1).remove(index);
                            break;
                        default:
                            index++;
                    }
		}
                return index;
	}
        
        /** 第十一優先處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @return 接下來須被運算的項目在陣列中的位置*/
        public static int CountSet11(ArrayList<Count_Array> CountRecord,int index){
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                    if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                        return ++index;
                    switch (CountRecord.get(CountRecord.size()-1).
                            get(index).
                            Operator) {
                        case "&&":
                        case Code_String.AND:
                            if(CountRecord.get(CountRecord.size()-1).get(index).Value.toString().matches(Type_String.TRUE+"|1")&&
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString().matches(Type_String.TRUE+"|1")){
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.TRUE);
                            }else{
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.FALSE);
                            }
                            CountRecord.get(CountRecord.size()-1).remove(index);
                            break;
                        default:
                            index++;
                    }
		}
                return index;
	}
        /** 第十二處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @return 接下來須被運算的項目在陣列中的位置*/
        public static int CountSet12(ArrayList<Count_Array> CountRecord,int index){
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                    if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                        return ++index;
                    switch (CountRecord.get(CountRecord.size()-1).
                            get(index).
                            Operator) {
                        case "||":
                        case Code_String.OR:
                            if(CountRecord.get(CountRecord.size()-1).get(index).Value.toString().matches(Type_String.TRUE+"|1")||
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString().matches(Type_String.TRUE+"|1")){
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.TRUE);
                            }else{
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                            CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append(Type_String.FALSE);
                            }
                            CountRecord.get(CountRecord.size()-1).remove(index);
                            break;
                        default:
                            index++;
                    }
		}
                return index;
	}
        /** 最後處理的運算子
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置
     * @return 接下來須被運算的項目在陣列中的位置*/
        public static int CountSet13(ArrayList<Count_Array> CountRecord,int index){
		if(CountRecord.get(CountRecord.size()-1).size()>1){
                    if(CountRecord.get(CountRecord.size()-1).get(index).Operator==null)
                        return ++index;
                    switch (CountRecord.get(CountRecord.size()-1).
                            get(index).
                            Operator) {
                        case "?":
                            if(CountRecord.get(CountRecord.size()-1).get(index).Value.toString().matches(Type_String.TRUE+"|1")){
                                    CountRecord.get(CountRecord.size()-1).remove(index);//因為?前的交件判斷區域已經沒用了，所以將它刪除
                                    int count=0,i=index;//count是用來紀錄條件為真的區域中的條件運算式，i是用來指向目前掃描的位置
                                    while(true){//判斷條件為真的區域
                                        if(CountRecord.get(CountRecord.size()-1).get(i).Operator.equals("?"))
                                            count++;
                                        else if(CountRecord.get(CountRecord.size()-1).get(i).Operator.equals(":")){
                                            if(count--==0){
                                                CountRecord.get(CountRecord.size()-1).get(i).Operator=null;//因為條件為真區域最後一項的Operator會是條件為假區域的開頭運算子:，但是條件為假區域接下來就要被刪除，所以就將:改為null
                                                break;
                                            }
                                        }
                                        i++;
                                    }
                                    i++;//將指針指向條件為假的區域
                                    while(i<CountRecord.get(CountRecord.size()-1).size())//將條件為假的區域全部刪除
                                        CountRecord.get(CountRecord.size()-1).remove(i);
                            }else{
                                    CountRecord.get(CountRecord.size()-1).remove(index);//因為?前的交件判斷區域已經沒用了，所以將它刪除
                                    int count=0;//count是用來紀錄條件為真的區域中的條件運算式
                                    while(true){//判斷條件為真的區域，並刪除
                                        if(CountRecord.get(CountRecord.size()-1).get(index).Operator.equals("?")){
                                            CountRecord.get(CountRecord.size()-1).remove(index);
                                            count++;
                                        }else if(CountRecord.get(CountRecord.size()-1).get(index).Operator.equals(":")){
                                            CountRecord.get(CountRecord.size()-1).remove(index);
                                            if(count--==0)
                                                break;
                                        }
                                    }
                            }
                            break;
                        default:
                            index++;
                    }
		}
                return index;
	}
        /**將布林值遠換成整數
     * @param CountRecord 需要被運算的陣列
     * @param index 須被運算的項目在陣列中的位置*/
        private static void BooleanChange(ArrayList<Count_Array> CountRecord,int index){
            if(CountRecord.get(CountRecord.size()-1).get(index).Value.toString().equals(Type_String.TRUE)){
                        CountRecord.get(CountRecord.size()-1).get(index).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index).Value.length());
                                CountRecord.get(CountRecord.size()-1).get(index).Value.append("1");
                    }else if(CountRecord.get(CountRecord.size()-1).get(index).Value.toString().equals(Type_String.FALSE)){
                        CountRecord.get(CountRecord.size()-1).get(index).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index).Value.length());
                                CountRecord.get(CountRecord.size()-1).get(index).Value.append("0");
                    }
                    if(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString().equals(Type_String.TRUE)){
                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append("1");
                    }else if(CountRecord.get(CountRecord.size()-1).get(index+1).Value.toString().equals(Type_String.FALSE)){
                        CountRecord.get(CountRecord.size()-1).get(index+1).Value.delete(0,
                                    CountRecord.get(CountRecord.size()-1).get(index+1).Value.length());
                                CountRecord.get(CountRecord.size()-1).get(index+1).Value.append("0");
                    }
        }
        /**使AShell字串可以重複的函數
         * @param frequency 重複次數
         * @param  Str 要重複的文字
         * @param  Str2 儲存結果文字
         * @param isStrDW 判斷是否為StrDW，會有這個變數是因為在StrDW中所有字串的"都會在進到這個函數前被去除掉，所以都會以前後項都沒有"的方式去處理，為了避免前巷項或後項是null或true或false而被加上了"才多設了這個變數
         **/
        private static void repeat(long frequency,StringBuilder Str,StringBuilder Str2,boolean isStrDW){
                Str2.delete(0, Str2.length());
                if(!isStrDW)
                    Str2.append("\"");
                if(frequency<=0)
                    return;
		for(int i=0;i<frequency;i++)
                    Str2.append(Str);
	}
        /** 運算處理
     * @param CountRecord 需要被運算的陣列
     * @param isStrDW 判斷是否為StrDW*/
        public static void Count(ArrayList<Count_Array> CountRecord,boolean isStrDW) throws Exception{
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
			count=CountSet1(CountRecord,count);
            
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
			count=CountSet2(CountRecord,count);
                
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
			count=CountSet3(CountRecord,count,isStrDW);
            
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
			count=CountSet4(CountRecord,count,isStrDW);
            
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
			count=CountSet5(CountRecord,count);
                
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
                    try{
			count=CountSet6(CountRecord,count);
                    }catch(Exception e){
                        if(e.getMessage().equals("false")){//當特殊判斷為假時(只有特殊判斷才會回傳這種錯誤)
                            CountRecord.get(CountRecord.size()-1).remove(count);
                            CountRecord.get(CountRecord.size()-1).get(count).Value.delete(0, CountRecord.get(CountRecord.size()-1).get(count).Value.length());
                            CountRecord.get(CountRecord.size()-1).get(count).Value.append(Type_String.FALSE);//將運涮結果定為假
                            while(count<CountRecord.get(CountRecord.size()-1).size()-1){//清除剩餘的特殊判斷式
                                if(CountRecord.get(CountRecord.size()-1).get(count).Operator==null||!CountRecord.get(CountRecord.size()-1).get(count).Operator.matches(">|>=|<|<="))//如果接下來沒有運算子 或 接下來的運算子不是同層級的運算子
                                    break;//那就代表特殊判段已經結束，但不代表接下來不會再有特殊判斷
                                CountRecord.get(CountRecord.size()-1).get(count).Operator=CountRecord.get(CountRecord.size()-1).get(count+1).Operator;
                                CountRecord.get(CountRecord.size()-1).remove(count+1);
                            }
                        }
                    }
                
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
                    try{
			count=CountSet7(CountRecord,count);
                    }catch(Exception e){
                        if(e.getMessage().equals("false")){//當特殊判斷為假時(只有特殊判斷才會回傳這種錯誤)
                            CountRecord.get(CountRecord.size()-1).remove(count);
                            CountRecord.get(CountRecord.size()-1).get(count).Value.delete(0, CountRecord.get(CountRecord.size()-1).get(count).Value.length());
                            CountRecord.get(CountRecord.size()-1).get(count).Value.append(Type_String.FALSE);//將運涮結果定為假
                            while(count<CountRecord.get(CountRecord.size()-1).size()-1){//清除剩餘的特殊判斷式
                                if(CountRecord.get(CountRecord.size()-1).get(count).Operator==null||!CountRecord.get(CountRecord.size()-1).get(count).Operator.matches(Code_String.IS+"|==|!=|<>|><"))//如果接下來沒有運算子 或 接下來的運算子不是同層級的運算子
                                    break;//那就代表特殊判段已經結束，但不代表接下來不會再有特殊判斷
                                CountRecord.get(CountRecord.size()-1).get(count).Operator=CountRecord.get(CountRecord.size()-1).get(count+1).Operator;
                                CountRecord.get(CountRecord.size()-1).remove(count+1);
                            }
                        }
                    }
                
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
			count=CountSet8(CountRecord,count);
                
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
			count=CountSet9(CountRecord,count);
                
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
			count=CountSet10(CountRecord,count);
                
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
			count=CountSet11(CountRecord,count);
                
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
			count=CountSet12(CountRecord,count);
                
                for(int count=0;count<CountRecord.get(CountRecord.size()-1).size()-1;)
			count=CountSet13(CountRecord,count);
        }
}

package AShell.ValueProcess;

import AShell.Data_Type_And_Struct.Code_String;
import AShell.Data_Type_And_Struct.IsNumeric;
import java.util.ArrayList;
public class VarCountRecordCreate {
	StringBuilder Variable=new StringBuilder("");
	ArrayList<Count> CountRecord=new ArrayList<>();
        /**
         * 將指令進行轉換
         * @param Str 指令
        * @throws java.lang.Exception 拋出錯誤給執行續接收
         */
	public VarCountRecordCreate(String Str) throws Exception{
		for(int i=0;i<Str.length();i++){
                    String str=Str.substring(i, i+1);
                    switch (str) {
                        case "+":
                        case "^":
                        case "?":
                        case ":":
                        case "-":
                        case "~":
                        case "{":
                        case "[":
                        case "]":
                        case "(":
                        case ")":
                        case ",":
                            if(CountRecord.isEmpty()||CountRecord.get(CountRecord.size()-1).Operator!=null){
                                CountRecord.add(new Count(Variable,str));
                                Variable=new StringBuilder("");
                            }else
                                CountRecord.get(CountRecord.size()-1).Operator=str;
                            break;
                        case ".":
                            if(Str.charAt(i+1)=='.'){
                                if(CountRecord.isEmpty()||CountRecord.get(CountRecord.size()-1).Operator!=null){
                                    CountRecord.add(new Count(Variable,str+'.'));
                                    Variable=new StringBuilder("");
                                }else
                                    CountRecord.get(CountRecord.size()-1).Operator=str+'.';
                            }else
                                if(!IsNumeric.isNumericTest(Variable.toString())){
                                    if(CountRecord.isEmpty()||CountRecord.get(CountRecord.size()-1).Operator!=null){
                                        CountRecord.add(new Count(Variable,str));
                                        Variable=new StringBuilder("");
                                    }else
                                        CountRecord.get(CountRecord.size()-1).Operator=str;
                                }
                        case "&":
                        case "|":
                        case "*":
                        case "/":
                        case "%":
                            if(CountRecord.isEmpty()||CountRecord.get(CountRecord.size()-1).Operator!=null){
                                if(str.equals(Str.substring(i+1, i+2))){
                                    CountRecord.add(new Count(Variable,str+str));
                                    i++;
                                }else
                                    CountRecord.add(new Count(Variable,str));
                                Variable=new StringBuilder("");
                            }else{
                                if(str.equals(Str.substring(i+1, i+2))){
                                    CountRecord.get(CountRecord.size()-1).Operator=str+str;
                                    i++;
                                }else
                                   CountRecord.get(CountRecord.size()-1).Operator=str;
                            }
                            break;
                        case "!":
                        case ">":
                        case "<":
                            if(CountRecord.isEmpty()||CountRecord.get(CountRecord.size()-1).Operator!=null){
                                switch (Str.substring(i+1, i+2)) {
                                    case "=":
                                        CountRecord.add(new Count(Variable,str+"="));
                                        i++;
                                        break;
                                    case ">"://判斷是否為 >>和<<或<>和><(等價於!=)
                                        CountRecord.add(new Count(Variable,str+">"));
                                        i++;
                                        break;
                                    case "<"://判斷是否為 >>和<<或<>和><(等價於!=)
                                        CountRecord.add(new Count(Variable,str+"<"));
                                        i++;
                                        break;
                                    default:
                                        CountRecord.add(new Count(Variable,str));
                                }
                                Variable=new StringBuilder("");
                            }else{
                                switch (Str.substring(i+1, i+2)) {
                                    case "=":
                                        CountRecord.get(CountRecord.size()-1).Operator=str+"=";
                                        i++;
                                        break;
                                    case ">"://判斷是否為 >>和<<或<>和><(等價於!=)
                                        CountRecord.get(CountRecord.size()-1).Operator=str+">";
                                        i++;
                                        break;
                                    case "<"://判斷是否為 >>和<<或<>和><(等價於!=)
                                        CountRecord.get(CountRecord.size()-1).Operator=str+"<";
                                        i++;
                                        break;
                                    default:
                                        CountRecord.get(CountRecord.size()-1).Operator=str;
                                }
                            }
                            break;
                        case "=":
                            if(CountRecord.isEmpty()||CountRecord.get(CountRecord.size()-1).Operator!=null){
                                if(Str.substring(i+1, i+2).equals("=")){
                                    CountRecord.add(new Count(Variable,str+str));
                                    i++;
                                }else
                                    CountRecord.add(new Count(Variable,str));
                                Variable=new StringBuilder("");
                            }else{
                                if(Str.substring(i+1, i+2).equals("=")){
                                    CountRecord.get(CountRecord.size()-1).Operator=str+str;
                                    i++;
                                }else
                                    CountRecord.get(CountRecord.size()-1).Operator=str;
                            }
                            break;
                        default:
                            if(str.equals("\"")){
                                Variable.append(str);
                                while(true){
                                    i++;
                                    str=Str.substring(i, i+1);
                                   Variable.append(str);
                                    if(str.equals("\"")){
                                        break;
                                    }else if(str.equals("\\"))
                                        Variable.append(Str.substring(++i, i+1));
                                }
                            }else{
                                if(!str.equals(" "))
                                    Variable.append(str);
                                else{
                                    if(Variable.toString().matches(String.format("%s|%s|%s|%s",Code_String.IS,Code_String.NOT,Code_String.AND,Code_String.OR))){
                                        if(CountRecord.isEmpty()||CountRecord.get(CountRecord.size()-1).Operator!=null)
                                            CountRecord.add(new Count(new StringBuilder(""),Variable.toString()));
                                        else
                                            CountRecord.get(CountRecord.size()-1).Operator=Variable.toString();
                                        Variable.delete(0, Variable.length());
                                    }else{
                                        if(!Variable.toString().equals("")){
                                            CountRecord.add(new Count(Variable,null));
                                            Variable=new StringBuilder("");
                                        }
                                    }
                                }
                            }
                            break;
                    }
		}
                CountRecord.add(new Count(Variable,null));
                /*System.err.print("Log:");
                for(Count c:CountRecord){
                    System.err.print(" "+c.Value+" "+c.Operator);
                }
                 System.err.println();*/
	}
}

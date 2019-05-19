package AShell.CommandResolve;

import AShell.Data_Type_And_Struct.Type_String;
import AShell.ValueProcess.AShellType;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class StringScan {
    /*用於無參數指令的判斷*/
    public static boolean startsWith(String value, String prefix) {
        char ta[] = value.toCharArray();
        char pa[] = prefix.toCharArray();
        int to = 0;
        int po = 0;
        int pc = prefix.length();
        int vc = value.length();
        if (vc < pc) 
            return false;
        for (;--pc >= 0;vc--)//檢查value開頭是否等於prefix
            if (ta[to++] != pa[po++])
                return false;
        for (;--vc >= 0;to++) //如果value後面還有東西，那就檢查value後面是否等不於空白以及Tap
            if (ta[to] != ' '&&ta[to] != '	')
                return false;
        return true;
    }
    /*用於return指令的判斷，因為return可以有參數也可以無參數*/
    public static boolean startsWith_for_return(String value, String prefix) {
        char ta[] = value.toCharArray();
        char pa[] = prefix.toCharArray();
        int to = 0;
        int po = 0;
        int pc = prefix.length();
        int vc = value.length();
        if (vc < pc) 
            return false;
        for (;--pc >= 0;vc--) 
            if (ta[to++] != pa[po++])
                return false;
        return !(vc!=0&&ta[to]!=' ');//如果return後面還有東西，那就檢查return後面是否接著一個空格
    }
    public static StringBuilder Auto_Type_Change(String value){//自動類型轉換
            if(value.matches(Type_String.ASHELL_TYPE))
                return new StringBuilder(value);
            return to_AShell_String(value);
    }
    public static StringBuilder to_AShell_String(char Str){
        return to_AShell_String(String.valueOf(Str));
    }
    /*這是專門處理Read指令的讀取值並把"改成\"用的*/
    public static StringBuilder to_AShell_String(String Str){
		/*if(IsNumeric.isNumericTest(Str))
			return new StringBuilder(Str);*/
		StringBuilder SB=new StringBuilder("\"");
                for(int i=0;i<Str.length();i++){
                    String str=Str.substring(i, i+1);
                    switch (str) {
                        case "\"":
                            SB.append("\\\"");
                            break;
                        case "\\":
                            SB.append("\\\\");
                            break;
                        case "\n":
                            SB.append("\\n");
                            break;
                        case "\t":
                            SB.append("\\t");
                            break;
                        case "\b":
                            SB.append("\\b");
                            break;
                        case "\r":
                            SB.append("\\r");
                            break;
                        case "\f":
                            SB.append("\\f");
                            break;
                        default:
                            SB.append(str);
                            break;
                    }
                }
		SB.append("\"");
		return SB;
    }
    
    /**
     * 解析一行程式碼
     * @param command 程式碼清單
     * @param codeLine 程式碼
     * @param LineNumbers 該程式碼行數
     * @throws Exception 解析錯誤
     */
    public void Porser_Code_Line(CommandArray command, String codeLine, int LineNumbers) throws Exception{
        do{
            StringBuilder Com=this.StrBlankDeal_with(codeLine);
            if(Com!=null){//當Com為null就代表這行為空白或只有註解，並沒有程式碼
                if(this.brackets!=0||this.append){//如果括弧樹區間不等於零或加入在指令後端為真
                    if(this.add){//如果建立新指令為真
                        this.add=false;
                        command.add(new Command(Com,LineNumbers));
                    }else
                        command.get(command.size()-1).Command.append(Com);
                    this.append=false;
                }else
                    command.add(new Command(Com,LineNumbers));
            }
        }while(!this.line_end);
    }
    
    /*用來過濾程式碼中開頭的空白、tab還有程式碼中的註解用的函數
    因為AShell是具有多執行緒能力的語言，所以會有發生同時間有兩個執行續使用註解、空白過濾器，因此過濾器不設定成靜態*/
    public boolean Annotation=false;//跨行註解判斷值
    public int brackets=0;//判斷括弧數量
    public boolean add=false,append=false;//判斷指令是要add在陣列中還是要abbend在上一個指令後
    public boolean line_end = false;//表示是否已經讀完該行程式碼
    
    private int command_char_index;//表示目前讀取到程式碼的哪一個字元
    
    public int get_command_char_index(){
        return command_char_index;
    }
    public StringBuilder StrBlankDeal_with(String command_str) throws Exception{
            if(line_end){
                line_end = false;
                command_char_index = 0;
            }
            StringBuilder SB=null;
            boolean Ignore=true;//空白過濾變數，如果為true就代表還沒遇上程式碼
            for(;command_char_index<command_str.length();command_char_index++){
                String str=command_str.substring(command_char_index, command_char_index+1);
                //System.out.print(str);
                    if(str.equals("#")&&!Annotation){
                        command_char_index = command_str.length();
                        break;
                    }else if(str.equals("/")&&!Annotation){
                        if(command_char_index+1<command_str.length()&&command_str.substring(command_char_index+1, command_char_index+2).equals("*")){
                            command_char_index++;
                            Annotation=true;
                            continue;//會在這裡是因為如果/不是註解的話那就不能被去除掉
                        }
                    }else if(str.equals("*")&&Annotation){
                        if(command_char_index+1<command_str.length()&&command_str.substring(command_char_index+1, command_char_index+2).equals("/")){
                            command_char_index++;
                            Annotation=false;
                        }
                        continue;//會在這裡是因為會進來這裡就代表著註解已經開始了，所以掃描到的*一定是在註解中，一定要去除
                    }else if(str.equals(";")&&!Annotation&&brackets == 0){
                        command_char_index++;
                        return SB;
                    }else if(str.equals("(")&&!Annotation){
                        if(brackets++==0&&!append)//如果這是第一個括弧 且 前面沒有遇到其他括弧區間的結尾
                            add=true;//那就代表著是一個新的指令
                    }else if(str.equals(")")&&!Annotation){
                        if(--brackets==0)//如果讀到全部的括弧了
                            append=true;//那就代表這是指令的尾端
                    }else if(str.equals("[")&&!Annotation){
                        if(brackets++==0&&!append)//如果這是第一個括弧 且 前面沒有遇到其他括弧區間的結尾
                            add=true;//那就代表著是一個新的指令
                    }else if(str.equals("]")&&!Annotation){
                        if(--brackets==0)//如果讀到全部的括弧了
                            append=true;//那就代表這是指令的尾端
                    }else if(str.equals("{")&&!Annotation){
                        if(brackets++==0&&!append)//如果這是第一個括弧 且 前面沒有遇到其他括弧區間的結尾
                            add=true;//那就代表著是一個新的指令
                    }else if(str.equals("}")&&!Annotation){
                        if(--brackets==0)//如果讀到全部的括弧了
                            append=true;//那就代表這是指令的尾端
                    }else{
                            if(Annotation)
                                    continue;//如果是註解的話就跳過
                            if(str.equals("\"")){
                                    if(Ignore){
                                        SB=new StringBuilder();
                                        Ignore=false;
                                    }
                                    SB.append(str);
                                    while(true){
                                        command_char_index++;
                                        try{
                                            str=command_str.substring(command_char_index, command_char_index+1);
                                        }catch(Exception e){
                                            throw new Exception("語法錯誤，字串沒有結束");
                                        }
                                        SB.append(str);
                                        if(str.equals("\"")){
                                            break;
                                        }else if(str.equals("\\"))
                                            SB.append(command_str.substring(++command_char_index, command_char_index+1));
                                    }
                                    continue;
                            }
                    }
                    if(!Ignore||!str.matches("\\s")){
                            if(Ignore){
                                SB=new StringBuilder();
                                Ignore=false;
                            }
                            SB.append(str);
                    }
		}
                //System.out.println();
                line_end = true;
                return SB;
	}
    /*判斷指令是要使用var還是使用StrDW，判斷原理是這行程式碼是否有單一 =*/
    public static boolean EqualsScan(String Str) throws Exception{
        char c;
        for(int i=0;i<Str.length();i++){
            c=Str.charAt(i);
            switch (c) {
                case '!':
                case '>':
                case '<':
                    //畔對是否為 !=  >=  <=
                    //if(Str.substring(i+1, i+2).equals("="))
                        i++;
                    break;
                case '=':
                    if(Str.charAt(i+1)!='='&&Str.charAt(i+1)!='>')
                        return true;
                    else
                        i++;
                    break;
                case '\"':
                    while(true){
                        try{
                            c=Str.charAt(++i);
                        }catch(Exception e){
                            throw new Exception("語法錯誤，字串沒有結束");
                        }
                        if(c=='\"')
                            break;
                        else if(c=='\\')
                            i++;
                    }   
                    break;
                default:
                    break;
            }
        }
        return false;
    }
    
    public static ArrayList<StringBuilder> strSplit(String Str) throws Exception{
        ArrayList<StringBuilder> AStr=new ArrayList<>();
        int AC=0;
        StringBuilder SB=new StringBuilder("");
        for(int i=0;i<Str.length();i++){
            String str=Str.substring(i, i+1);
                if(str.equals(",")&&AC==0){
                        AStr.add(SB);
                         SB=new StringBuilder("");
                }else if(str.equals("(")||str.equals("{")){
                        SB.append(str);
                        AC++;
                }else if(str.equals(")")||str.equals("}")){
                        SB.append(str);
                        AC--;
                }else if(str.equals("\"")){
                        SB.append(str);
                        while(true){
                            i++;
                            try{
                                str=Str.substring(i, i+1);
                            }catch(Exception e){
                                throw new Exception("語法錯誤，字串沒有結束");
                            }
                            SB.append(str);
                            if(str.equals("\""))
                                break;
                            else if(str.equals("\\")){
                                 SB.append(Str.substring(++i, i+1));
                            }
                        }
                }else
                    SB.append(str);
        }
        AStr.add(SB);
        return AStr;
    }
    /**
         *取得AShrell字串長度
         * @param Str AShell字串
         * @return 要取得的字串長度
         * @throws Exception 類型錯誤或超出範圍
         */
    public static int get_AShell_String_Length(StringBuilder Str) throws Exception{
        int length=0;
        if(Str.charAt(0)!='\"')
            throw new Exception("型態錯誤，參數必須為String類型");
        for(int i=1;i<Str.length()-1;i++){
            if(Str.charAt(i)!='\\')//如果取到的字串不是跳脫字元
                length++;
            else{
                length++;
                i++;
            }
        }
        return length;
    }
    /**
         * 從AShrell字串中取得一個字元
         * @param Str AShell字串
         * @param index 要取得的字串位置
         * @return 要取得的字串
         * @throws Exception 類型錯誤或超出範圍
         */
    public static StringBuilder get_AShell_String_Character_Array(StringBuilder Str,int index) throws Exception{
        if(Str.charAt(0)!='\"')
            throw new Exception("型態錯誤，參數必須為String類型");
        if(index < 0)
            index += get_AShell_String_Length(Str);
        for(int i=1,count=0;i<Str.length()-1;i++,count++){
            if(Str.charAt(i)!='\\'){//如果取到的字串不是跳脫字元
                if(count==index)
                    return new StringBuilder().append('\"').append(Str.charAt(i)).append('\"');
            }else{
                if(count==index)
                    return new StringBuilder().append('\"').append(Str.substring(i,i+2)).append('\"');
                i++;
            }
        }
        throw new Exception("陣列引數超出範圍，長度是"+(Str.length()-2)+"但引數是"+index);
    }
    /**
         * 從AShrell字串中取得一個字元(用於StrDW)
         * @param Str AShell字串
         * @param index 要取得的字串位置
         * @return 要取得的字串
         * @throws Exception 類型錯誤或超出範圍
         */
    public static StringBuilder get_AShell_String_Character_Array_for_StrDW(StringBuilder Str,int index) throws Exception{
        for(int i=0,count=0;i<Str.length();i++,count++){
            if(Str.charAt(i)!='\\'){//如果取到的字串不是跳脫字元
                if(count==index)
                    return new StringBuilder().append(Str.charAt(i));
            }else{
                if(count==index)
                    return new StringBuilder(Str.substring(i,i+2));
                i++;
            }
        }
        throw new Exception("陣列引數超出範圍");
    }
    public static boolean matchFunctionLanbda(StringBuilder Str) throws Exception{
        for(int i=0;i<Str.length();i++)
            switch(Str.charAt(i)){
                case '[':
                {
                    char c;
                    int index=1;
                    loop:while(true){
                        if(++i<Str.length())
                            c=Str.charAt(i);
                        else
                            throw new Exception("語法錯誤，中括號不對稱");
                        switch(c){
                            case '[':
                                index++;
                                break;
                            case ']':
                                if(--index==0)
                                    break loop;
                                break;
                            case '\"':
                            {
                                while(true){
                                    if(++i<Str.length())
                                        c=Str.charAt(i);
                                    else
                                        throw new Exception("語法錯誤，字串沒有結束");
                                    if(c=='\"')
                                        break;
                                    else if(c=='\\')
                                        i++;
                                }  
                                break;
                            }
                        }
                    }
                    break;
                }
                case '(':
                {
                    char c;
                    int index=1;
                    loop:while(true){
                        if(++i<Str.length())
                            c=Str.charAt(i);
                        else
                            throw new Exception("語法錯誤，括號不對稱");
                        switch(c){
                            case '(':
                                index++;
                                break;
                            case ')':
                                if(--index==0)
                                    break loop;
                                break;
                            case '\"':
                            {
                                while(true){
                                    if(++i<Str.length())
                                        c=Str.charAt(i);
                                    else
                                        throw new Exception("語法錯誤，字串沒有結束");
                                    if(c=='\"')
                                        break;
                                    else if(c=='\\')
                                        i++;
                                }  
                                break;
                            }
                        }
                    }
                    break;
                }
                case '=':
                    return true;
            }
        return false;
    }
}
package AShell.Native_Class;

import AShell.Data_Type_And_Struct.AShell_this;
import AShell.ValueProcess.AShellType;


public class AShell_Stdio {
    /**
     * 提供給AShell的print函數
     * @param AShThis AShell執行續相關物件
     * @param Args AShell函數參數，是一個不定長度參數，AShell與java溝通用的類型交換類別
     * @return AShell與java溝通用的類型交換類別
     * @throws java.lang.Exception 類型轉換錯誤
     */
    public static AShellType print(AShell_this AShThis,AShellType[] Args) throws Exception{
        if(Args.length==0)
            throw new Exception("參數數量不可以為0");
        AShThis.AS.print.Print(Args[0].enforce_to_java_String());
        return new AShellType();
    }
    /*public static AShellType printf(AShell_this AShThis,AShellType[] Args) throws Exception{//格式化輸出
        String form=Args[0].to_java_String();
        StringBuilder buffer=new StringBuilder();
        char temp;
        for(int i=0;i<form.length();i++){
            temp=form.charAt(i);
            if(temp=='{'){
                StringBuilder SB=new StringBuilder();
                while(true){
                    temp=form.charAt(++i);
                    if(temp=='}')
                        break;
                    else
                        SB.append(temp);
                }
                int index=Integer.parseInt(SB.toString())+1;
                if(index>=Args.length||index<=0)
                    throw new Exception("超出引數範圍");
                buffer.append(Args[index].enforce_to_java_String());
            }else if(temp=='\\')
                buffer.append(form.charAt(++i));
            else
                buffer.append(temp);
        }
         AShThis.AS.Print(buffer.toString(),null);
        return new AShellType();
    }*/
    public static AShellType error(AShell_this AShThis,AShellType[] Args) throws Exception{
        if(Args.length==0)
            throw new Exception("參數數量不可以為0");
        AShThis.AS.error.Error(Args[0].enforce_to_java_String());
        return new AShellType();
    }
    public static AShellType scan(AShell_this AShThis,AShellType[] Args) throws Exception{
        return new AShellType(AShThis.AS.read.Rand(0));
    }
    public static AShellType getch(AShell_this AShThis,AShellType[] Args) throws Exception{
        return new AShellType(AShThis.AS.read.Rand(1));
    }
    public static AShellType clear(AShell_this AShThis,AShellType[] Args){
        AShThis.AS.clear.Clear();
        return new AShellType();
    }
}

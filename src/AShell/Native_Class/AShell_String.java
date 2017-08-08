package AShell.Native_Class;

import AShell.Data_Type_And_Struct.AShell_this;
import AShell.ValueProcess.AShellType;

public class AShell_String {
    public static AShellType strlen(AShell_this AShThis,AShellType[] Args) throws Exception{
        if(Args.length==0)
            throw new Exception("參數數量不可以為0");
        return new AShellType(Args[0].to_java_String().length());
    }
    public static AShellType charAt(AShell_this AShThis,AShellType[] Args) throws Exception{
        if(Args.length<2)
            throw new Exception("參數數量不可小於2");
        return new AShellType(Args[0].to_java_String().charAt((int)Args[1].to_java_long()));
    }
}

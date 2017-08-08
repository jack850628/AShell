package AShell.Native_Class;

import AShell.Data_Type_And_Struct.AShell_this;
import AShell.Data_Type_And_Struct.Type_String;
import AShell.ValueProcess.AShellType;

public class AShell_Type {
    public static AShellType toDigital(AShell_this AShThis,AShellType[] Args) throws Exception{
        if(Args.length==0)
            throw new Exception("參數數量不可以為0");
        if(Args[0].Type==AShellType.java_Type.java_String){
            String AShStr=Args[0].to_java_String();
            if(AShStr.matches(Type_String.DIGITAL))
                return new AShellType().put_AShell_Value_Auto(AShStr);
            return new AShellType(0);
        }else if(Args[0].Type==AShellType.java_Type.java_double||Args[0].Type==AShellType.java_Type.java_long)
            return Args[0];
        else if(Args[0].Type==AShellType.java_Type.java_bloolean)
            if(Args[0].to_java_boolean())
                return new AShellType(1);
        return new AShellType(0);
    }
    public static AShellType getType(AShell_this AShThis,AShellType[] Args) throws Exception{
        if(Args.length==0)
            throw new Exception("參數數量不可以為0");
        if(Args[0].Type==AShellType.java_Type.java_String){
            String AShStr=Args[0].to_java_String();
            if(AShStr.matches(Type_String.CLASS_M))
                return new AShellType(4);
            else if(AShStr.matches(Type_String.OBJECT_M))
                return new AShellType(5);
            else if(AShStr.matches(Type_String.FUNCTION_M))
                return new AShellType(6);
            else if(AShStr.matches(Type_String.NATIVE_FUNCTION_M))
                return new AShellType(7);
            else if(AShStr.matches(Type_String.ARRAY_M))
                return new AShellType(8);
            else
                return new AShellType(3);
        }else if(Args[0].Type==AShellType.java_Type.java_long)
            return new AShellType(0);
        else if(Args[0].Type==AShellType.java_Type.java_double)
            return new AShellType(1);
        else if(Args[0].Type==AShellType.java_Type.java_bloolean)
            return new AShellType(2);
        else //if(Args[0].Type==AShellType.java_Type.java_null)
            return new AShellType(9);
    }
}

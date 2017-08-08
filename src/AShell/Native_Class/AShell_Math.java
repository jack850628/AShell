package AShell.Native_Class;

import AShell.Data_Type_And_Struct.AShell_this;
import AShell.ValueProcess.AShellType;
import java.util.Random;

public class AShell_Math {
    private static final Random random=new Random();
    public static AShellType rand(AShell_this AShThis,AShellType[] Args) throws Exception{
        if(Args.length==0)
            throw new Exception("參數數量不可以為0");
        return new AShellType(random.nextInt((int)Args[0].to_java_long()));
    }
}

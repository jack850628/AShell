package AShell.Native_Class;

import AShell.Data_Type_And_Struct.AShell_this;
import AShell.Memory_Management.Memory_Management;
import AShell.ValueProcess.AShellType;

public class AShell_AShlib {
    public synchronized static AShellType free(AShell_this AShThis,AShellType[] Args) throws Exception{
        if(Args.length==0)
            throw new Exception("參數數量不可以為0");
        Memory_Management.remove(AShThis.AS,AShThis.RP,Args[0].to_java_String());
        return new AShellType();
    }
    public synchronized static AShellType gc(AShell_this AShThis,AShellType[] Args) throws Exception{
        Memory_Management.Garbage_Collection_Start(AShThis.AS,AShThis.RP);
        return new AShellType();
    }
}

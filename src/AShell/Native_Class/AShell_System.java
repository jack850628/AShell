package AShell.Native_Class;

import AShell.CallSystem;
import AShell.Data_Type_And_Struct.AShell_this;
import AShell.ValueProcess.AShellType;
import java.io.File;

public class AShell_System {
    public static AShellType finish(AShell_this AShThis,AShellType[] Args){
        AShThis.AS.Stop();
        AShThis.AS.stop.Stop();
        return new AShellType();
    }
    public static AShellType system(AShell_this AShThis,AShellType[] Args) throws Exception{
        return new AShellType(CallSystem.System(Args[0].to_java_String()));
    }
    public static AShellType setPath(AShell_this AShThis,AShellType[] Args) throws Exception{
        if(Args.length==0)
            throw new Exception("參數數量不可以為0");
        File f=new File(Args[0].to_java_String());
        if(f.isDirectory()){
                AShThis.AS.RuningPath.delete(0, AShThis.AS.RuningPath.length());
                AShThis.AS.RuningPath.append(f.getPath());
                CallSystem.chdir(f.getPath());
                return new AShellType(true);
        }else
            return new AShellType(false);
    }
    public static AShellType getPath(AShell_this AShThis,AShellType[] Args) throws Exception{
            return new AShellType(AShThis.AS.RuningPath.toString());
    }
    public static AShellType getScriptFilePath(AShell_this AShThis,AShellType[] Args) throws Exception{
            return new AShellType(AShThis.RP.getScriptPath());
    }
    public static AShellType getArgs(AShell_this AShThis,AShellType[] Args) throws Exception{
            return new AShellType().put_AShell_Memory_Type(AShThis.AS.AShellArgs.toString(),true);
    }
}

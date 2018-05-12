package AShell.Native_Class;

import AShell.AShell;
import AShell.Data_Type_And_Struct.AShell_this;
import AShell.ValueProcess.AShellType;

public class AShell_Threading {
    AShell.Run_Point RP;
    public AShellType _start(AShell_this AShThis,AShellType[] Args){
        if(RP==null||!RP.RunState)
            RP=AShThis.AS.Thread_Run(AShThis.ValueArray,AShThis.RP);
        return new AShellType();
    }
    public AShellType _wait(AShell_this AShThis,AShellType[] Args) throws Exception{
        AShThis.AS.Thread_Wait(RP,Args[0].to_java_long());
        return new AShellType();
    }
    public AShellType notifyall(AShell_this AShThis,AShellType[] Args) throws Exception{
        AShThis.AS.Thread_Notifyall();
        return new AShellType();
    }
    public AShellType notify(AShell_this AShThis,AShellType[] Args) throws Exception{
        AShThis.AS.Thread_Notify(RP);
        return new AShellType();
    }
}

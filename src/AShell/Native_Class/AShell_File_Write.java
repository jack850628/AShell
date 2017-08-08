package AShell.Native_Class;

import AShell.Data_Type_And_Struct.AShell_this;
import AShell.ValueProcess.AShellType;
import java.io.File;
import java.io.FileWriter;

public class AShell_File_Write {
    private FileWriter FW;
    public AShellType _inst_(AShell_this AShThis,AShellType[] Args) throws Exception{
        return Open(AShThis,Args);
    }
    public AShellType Open(AShell_this AShThis,AShellType[] Args) throws Exception{
        FW=new FileWriter(new File(Args[0].to_java_String()),Args[1].to_java_boolean());
        return new AShellType();
    }
    public AShellType Write(AShell_this AShThis,AShellType[] Args) throws Exception{
        FW.write(Args[0].enforce_to_java_String());
        return new AShellType();
    }
    public AShellType Close(AShell_this AShThis,AShellType[] Args) throws Exception{
        FW.close();
        return new AShellType();
    }
}

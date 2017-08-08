package AShell.Native_Class;

import AShell.Data_Type_And_Struct.AShell_this;
import AShell.ValueProcess.AShellType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AShell_File_Read {
    private BufferedReader BR;
    public AShellType _inst_(AShell_this AShThis,AShellType[] Args) throws Exception{
        return Open(AShThis,Args);
    }
    public AShellType Open(AShell_this AShThis,AShellType[] Args) throws Exception{
        BR = new BufferedReader(new FileReader(new File(Args[0].to_java_String())));
        return new AShellType();
    }
    public AShellType ReadLine(AShell_this AShThis,AShellType[] Args) throws Exception{
        String Str=BR.readLine();
        return (Str!=null)?new AShellType(Str):new AShellType();
    }
    public AShellType Read(AShell_this AShThis,AShellType[] Args) throws Exception{
        int Char=BR.read();
        return (Char!=-1)?new AShellType((char)Char):new AShellType();
    }
    public AShellType Close(AShell_this AShThis,AShellType[] Args) throws Exception{
        BR.close();
        return new AShellType();
    }
}

package AShell.AShellException;
//當查無AShell記憶體位置時會引發的錯誤
public class AShellMempryValueNotFindException extends Exception{
    public AShellMempryValueNotFindException(String Message){
        super(Message);
    }
}

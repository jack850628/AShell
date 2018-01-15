package AShell.CommandResolve;

public class Command {
    public StringBuilder Command;
    public StringBuilder erroeShowCommand;//當發生錯誤時要顯示的程式碼
    public CommandArray ComArray=null;
    public int LineNumbers;//程式碼所在的行數，如果是由AShell解析器自動加入的程式碼，該程式碼的行數就會是0
    public Command(StringBuilder Command,int LineNumbers){
        this.Command=Command;
        this.erroeShowCommand=Command;
        this.LineNumbers=LineNumbers;
    }
    public Command(StringBuilder Command,StringBuilder erroeShowCommand,int LineNumbers){
        this.Command=Command;
        this.erroeShowCommand=erroeShowCommand;
        this.LineNumbers=LineNumbers;
    }
    public Command(Command CA){
        this.Command=CA.Command;
        this.LineNumbers=CA.LineNumbers;
    }
    public void setComArray(CommandArray ComArray){
        this.ComArray=ComArray;
    }
}

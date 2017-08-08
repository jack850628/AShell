package AShell.CommandResolve;

public class Command {
    public StringBuilder Command;
    public CommandArray ComArray=null;
    public int LineNumbers;//程式碼所在的行數，如果是由AShell解析器自動加入的程式碼，該程式碼的行數就會是0
    public Command(StringBuilder Command,int LineNumbers){
        this.Command=Command;
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

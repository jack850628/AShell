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
        this.erroeShowCommand=CA.erroeShowCommand;
        //this.ComArray=CA.ComArray;//這行目前用不到，因為目前的版本中在class和function裡begin、if等等的區塊程式碼在class和function被執行到之前都不會被折疊，故CA.ComArray一定都是null
        this.LineNumbers=CA.LineNumbers;
    }
    public void setComArray(CommandArray ComArray){
        this.ComArray=ComArray;
    }
}

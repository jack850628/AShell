import AShell.*;
import AShell.Data_Type_And_Struct.Value_Array;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author 暘修
 */
public class call_AShell {
    static AShell AS;
    static Scanner scan=new Scanner(System.in);
    public static void main(String[] args) {
        /**args陣列內容說明
         * rgs[0] 模式判別 false為互動式命令列界面模式 true為腳本檔執行模式
         * rga[1] 路徑值
         * 之後的陣列內容都是AShell的腳本參數
         */
        AS=new AShell(
            (String str)->{
                System.out.print(str);
            },
            (String str)->{
                System.err.print(str);
            },
            ()->{
                 CFunction.clearScreen();
            },
            (int mode)->{
                if(mode==0)
                    return scan.nextLine();
                else
                    return String.valueOf(CFunction.Cgetch());
            },
            ()->{scan.close();}
             ,
                args.length>2?Arrays.copyOfRange(args, 2, args.length):null
        );
        if(args.length<=1||args[0].equals("false")){
            System.out.println("    ___   _____ __         ____\n"
                              +"   /   | / ___// /_  ___  / / /\n"
                              +"  / /| | \\__ \\/ __ \\/ _ \\/ / /\n"
                              +" / ___ |___/ / / / /  __/ / /\n"
                              +"/_/  |_/____/_/ /_/\\___/_/_/ \n"
                              +"(c)copyright 2017 jack850628 "+AS.getVar()
                              +"\n如果不知道如何使用，可以輸入'help'指令來查看用法:)\n");
             /*System.out.print("\n\n請按任意鍵結束. . .");
            CFunction.Cgetch();*/
             interactive_Mode_Run IMR=new interactive_Mode_Run(AS);
             Value_Array ValueArray=AS.interactive_Mode_Start(()->{
                 synchronized(IMR){
                     IMR.notify();
                 }
             },(args.length>1&&!args[1].equals("(null)"))?args[1]:System.getProperty("user.dir"));
             IMR.ValueArray=ValueArray;
             IMR.Start();
            return;
        }
        AS.Start_Run(new File(args[1]));
    }
}

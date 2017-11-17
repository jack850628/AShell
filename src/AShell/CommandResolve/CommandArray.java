package AShell.CommandResolve;

import AShell.Data_Type_And_Struct.Tag;
import java.util.ArrayList;

public class CommandArray extends ArrayList<Command>{
    public String fileName;
    public ArrayList<Tag> tag=new ArrayList<>();
    /**
     * 指令陣列建構式
     * @param fileName 該指令來源的檔案名稱 
     */
    public CommandArray(String fileName){
        this.fileName=fileName;
    }
}

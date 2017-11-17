package AShell.CommandResolve;

import AShell.Data_Type_And_Struct.Code_String;
import AShell.Data_Type_And_Struct.Tag;

public class CreateSyntaxTree {
    public static void CST(CommandArray ComArray){
         StringBuilder temp;
        for(int i=0;i<ComArray.size();i++){
            temp=ComArray.get(i).Command;
            if(temp.toString().startsWith(":")){
                ComArray.tag.add(new Tag(temp.toString().substring(1).trim(),i));
                ComArray.remove(i--);//將標籤從程式碼中移除，因為已經記錄了原本標籤所在的行數，且使用goto時取出元標籤所在行數時會馬上被for加一而指向標籤下方的程式碼，所以移除吊飾沒差的
            }else if(temp.toString().startsWith(Code_String.FUNCTION+" ")||temp.toString().startsWith(Code_String.CLASS+" ")){
                int fun=0;
                while(true){
                    temp=ComArray.get(++i).Command;
                    if(temp.toString().startsWith(Code_String.FUNCTION+" ")||temp.toString().startsWith(Code_String.CLASS+" "))
                        fun++;
                    else if(StringScan.startsWith(temp.toString(),Code_String.ENDFU)||StringScan.startsWith(temp.toString(),Code_String.ENDCL))
                        if(fun--==0)
                            break;
                }
            }else if(StringScan.startsWith(temp.toString(),Code_String.BEGIN)||temp.toString().startsWith(Code_String.IF+" ")||temp.toString().startsWith(Code_String.ELSEIF+" ")||StringScan.startsWith(temp.toString(),Code_String.ELSE)||temp.toString().startsWith(Code_String.FOR+" ")||
                    temp.toString().startsWith(Code_String.WHILE+" ")||StringScan.startsWith(temp.toString(),Code_String.DO)||StringScan.startsWith(temp.toString(),Code_String.TRY)||temp.toString().startsWith(Code_String.CATCH+" ")||StringScan.startsWith(temp.toString(),Code_String.FINALLY)){
               CommandArray CA=new CommandArray(ComArray.fileName);
               if(!StringScan.startsWith(temp.toString(),Code_String.DO))//因為如果是do-while，那麼程式碼區段就要放在dwhile那行，所以排除do
                    ComArray.get(i).setComArray(CA);
                int con=0;
                boolean loop=false;//這個變數為真的話，就代表著迴圈區塊中還包著其他的迴圈區塊
                while(true){
                    temp=ComArray.get(i+1).Command;
                    if(temp.toString().startsWith(":"))
                        CA.tag.add(new Tag(temp.toString().substring(1).trim(),CA.size()));
                    else if(temp.toString().startsWith(Code_String.FUNCTION+" ")||temp.toString().startsWith(Code_String.CLASS+" ")){
                        int fun=0;
                        while(true){
                            CA.add(ComArray.remove(i+1));
                            temp=ComArray.get(i+1).Command;
                            if(temp.toString().startsWith(Code_String.FUNCTION+" ")||temp.toString().startsWith(Code_String.CLASS+" "))
                                fun++;
                            else if(StringScan.startsWith(temp.toString(),Code_String.ENDFU)||StringScan.startsWith(temp.toString(),Code_String.ENDCL))
                                if(fun--==0)
                                    break;
                        }
                    }else if(StringScan.startsWith(temp.toString(),Code_String.BEGIN)||temp.toString().startsWith(Code_String.IF+" ")||temp.toString().startsWith(Code_String.FOR+" ")||
                            temp.toString().startsWith(Code_String.WHILE+" ")||StringScan.startsWith(temp.toString(),Code_String.DO)||StringScan.startsWith(temp.toString(),Code_String.TRY)){
                            con++;
                            loop=true;
                    }else if(con==0&&(temp.toString().startsWith(Code_String.ELSEIF+" ")||StringScan.startsWith(temp.toString(),Code_String.ELSE)||
                            temp.toString().startsWith(Code_String.CATCH+" ")||StringScan.startsWith(temp.toString(),Code_String.FINALLY))){
                                break;
                    }else if(StringScan.startsWith(temp.toString(),Code_String.ENDIF)||StringScan.startsWith(temp.toString(),Code_String.ENDTR)){
                            if(con--==0){
                                i++;//這個是為了讓endif或endtr留在指令列中作為i結束判斷
                                break;
                            }
                    }else if(StringScan.startsWith(temp.toString(),Code_String.ENDBE)||StringScan.startsWith(temp.toString(),Code_String.ENDWH)||StringScan.startsWith(temp.toString(),Code_String.ENDFO)){
                            if(con--==0){
                                ComArray.remove(i+1);
                                break;
                            }
                    }else if(temp.toString().startsWith(Code_String.DWHILE+" "))
                            if(con--==0){
                                ComArray.remove(i);//將do那行刪除
                                ComArray.get(i).setComArray(CA);//將程式碼區段就要放在dwhile這行
                                break;
                            }
                    CA.add(ComArray.remove(i+1));
                }
                if(loop)
                    CST(CA);
            }
        }
    }
}

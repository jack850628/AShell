
import AShell.AShell;
import AShell.CommandResolve.Command;
import AShell.CommandResolve.CommandArray;
import AShell.CommandResolve.StringScan;
import AShell.Data_Type_And_Struct.Code_String;
import AShell.Data_Type_And_Struct.Value_Array;
//互動式命令列界面類別
public class interactive_Mode_Run {
    AShell AS;
    Value_Array ValueArray;
    CommandArray command=new CommandArray();
    StringScan SS=new StringScan();//實例化空白、註解過濾器
    int LineNumbers=1;
    public interactive_Mode_Run(AShell AS){
        this.AS=AS;
    }
    void Start(){
        StringBuilder SB = null;
        String Com = null;
        //------------加入Stdio函式庫---------------
        command.add(new Command(new StringBuilder("using Stdio.ash"),0));
        command.add(new Command(new StringBuilder("native help(\"AShell.Native_Class.AShell_InteractiveModeFunction\")"),0));
        command.add(new Command(new StringBuilder("native exit(\"AShell.Native_Class.AShell_InteractiveModeFunction\")"),0));
        command.add(new Command(new StringBuilder("native ver(\"AShell.Native_Class.AShell_InteractiveModeFunction\")"),0));
        AS.ComLineRun(ValueArray,command);
        synchronized(this){
            try {
                this.wait();
            } catch (InterruptedException e) {}
        }
        command.clear();
        //---------------------------------------------------
        while(true){
            System.out.print(SS.brackets!=0||SS.Annotation?"...":">>>");
            try {
                if((SB=SS.StrBlankDeal_with(StringRead.Read()))!=null)
                    Com=SB.toString();
                else
                    continue;//用來防止空白輸入造成的大間隔(空白輸出)
            } catch (Exception e) {
               AS.print.Print("錯誤！"+e.getMessage()+"\n");
               continue;
            }
            if(SS.brackets!=0||SS.append){//如果括弧樹區間不等於零或加入在指令後端為真
                if(SS.add){//如果建立新指令為真
                    SS.add=false;
                    command.add(new Command(SB,LineNumbers));
                }else
                    command.get(command.size()-1).Command.append(SB);
                SS.append=false;
            }else
                command.add(new Command(SB,LineNumbers));
            char ch=' ';
            try{
                ch=Com.charAt(0);
            }catch(Exception e){}
            if(ch=='b'){
                if(SS.brackets==0&&StringScan.startsWith(Com,Code_String.BREAK)){
                    System.out.println(Code_String.BREAK+"無法單獨使用，須和"+Code_String.WHILE+"一同使用。");
                }else if(StringScan.startsWith(Com,Code_String.BEGIN)){
                            int setIf=0;
                            while(true){
                                System.out.print("...");
                                try {
                                    SB=SS.StrBlankDeal_with(StringRead.Read());
                                    LineNumbers++;
                                } catch (Exception e) {
                                    AS.error.Error("錯誤！"+e.getMessage()+"\n");
                                    continue;
                                }
                                if(SB!=null){//當Com為null就代表這行為空白或只有註解，並沒有程式碼
                                    if(SS.brackets!=0||SS.append){//如果括弧樹區間不等於零或加入在指令後端為真
                                        if(SS.add){//如果建立新指令為真
                                            SS.add=false;
                                            command.add(new Command(SB,LineNumbers));
                                        }else
                                            command.get(command.size()-1).Command.append(SB);
                                        SS.append=false;
                                    }else
                                        command.add(new Command(SB,LineNumbers));
                                }else
                                    continue;
                                if(StringScan.startsWith(SB.toString(),Code_String.BEGIN))
                                    setIf++;
                                else if(StringScan.startsWith(SB.toString(),Code_String.ENDBE))
                                    if(setIf--==0)
                                         break;
                            }
                            ch=' ';
                    }else
                    ch=' ';
            }else if(ch=='c'){
                if(SS.brackets==0&&StringScan.startsWith(Com,Code_String.CONTINUE)){
                        System.out.println(Code_String.CONTINUE+"無法單獨使用，須和"+Code_String.WHILE+"一同使用。");
                }else if(Com.startsWith(Code_String.CLASS+" ")){
                        int setCl=0;
                        while(true){
                            System.out.print("...");
                            try {
                                SB=SS.StrBlankDeal_with(StringRead.Read());
                                LineNumbers++;
                            } catch (Exception e) {
                                AS.error.Error("錯誤！"+e.getMessage()+"\n");
                                continue;
                            }
                            if(SB!=null){//當Com為null就代表這行為空白或只有註解，並沒有程式碼
                                if(SS.brackets!=0||SS.append){//如果括弧樹區間不等於零或加入在指令後端為真
                                    if(SS.add){//如果建立新指令為真
                                        SS.add=false;
                                        command.add(new Command(SB,LineNumbers));
                                    }else
                                        command.get(command.size()-1).Command.append(SB);
                                    SS.append=false;
                                }else
                                    command.add(new Command(SB,LineNumbers));
                            }else
                                continue;
                            if(SB.toString().startsWith(Code_String.CLASS+" "))
                                setCl++;
                            else if(StringScan.startsWith(SB.toString(),Code_String.ENDCL))
                                if(setCl--==0)
                                    break;
                        }
                        ch=' ';
		}else if(SS.brackets==0&&Com.startsWith(Code_String.CATCH+" ")){
                            System.out.println(Code_String.CATCH+"無法單獨使用，須和"+Code_String.TRY+"一同使用。");
                }else
			ch=' ';
            }else if(ch=='d'){
                    if(StringScan.startsWith(Com,Code_String.DO)){
                            int setDo=0;
                            while(true){
                                System.out.print("...");
                                try {
                                    SB=SS.StrBlankDeal_with(StringRead.Read());
                                    LineNumbers++;
                                } catch (Exception e) {
                                    AS.error.Error("錯誤！"+e.getMessage()+"\n");
                                    continue;
                                }
                                if(SB!=null){//當Com為null就代表這行為空白或只有註解，並沒有程式碼
                                    if(SS.brackets!=0||SS.append){//如果括弧樹區間不等於零或加入在指令後端為真
                                        if(SS.add){//如果建立新指令為真
                                            SS.add=false;
                                            command.add(new Command(SB,LineNumbers));
                                        }else
                                            command.get(command.size()-1).Command.append(SB);
                                        SS.append=false;
                                    }else
                                        command.add(new Command(SB,LineNumbers));
                                }else
                                    continue;
                                if(StringScan.startsWith(SB.toString(),Code_String.DO))
                                    setDo++;
                                else if(SB.toString().startsWith(Code_String.DWHILE+" "))
                                    if(setDo--==0)
                                         break;
                            }
                            ch=' ';
                    }else if(SS.brackets==0&&Com.startsWith(Code_String.DWHILE+" "))
                            System.out.println(Code_String.DWHILE+"無法單獨使用，須和"+Code_String.DO+"一同使用。");
                    else
                            ch=' ';
            }else if(ch=='e'){
                    if(SS.brackets==0&&StringScan.startsWith(Com,Code_String.ELSE))
                            System.out.println(Code_String.ELSE+"無法單獨使用，須和"+Code_String.IF+"一同使用。");
                    else if(SS.brackets==0&&Com.startsWith(Code_String.ENDIF+" "))
                            System.out.println(Code_String.ENDIF+"無法單獨使用，須和"+Code_String.IF+"一同使用。");
                    else if(SS.brackets==0&&StringScan.startsWith(Com,Code_String.ENDIF))
                            System.out.println(Code_String.ENDIF+"無法單獨使用，須和"+Code_String.IF+"一同使用。");
                    else if(SS.brackets==0&&StringScan.startsWith(Com,Code_String.ENDWH))
                            System.out.println(Code_String.ENDWH+"無法單獨使用，須和"+Code_String.WHILE+"一同使用。");
                    else if(SS.brackets==0&&StringScan.startsWith(Com,Code_String.ENDCL))
                            System.out.println(Code_String.ENDCL+"無法單獨使用，須和"+Code_String.CLASS+"一同使用。");
                    else if(SS.brackets==0&&StringScan.startsWith(Com,Code_String.ENDTR))
                            System.out.println(Code_String.ENDTR+"無法單獨使用，須和"+Code_String.TRY+"一同使用。");
                    else
                            ch=' ';
            }else if(ch=='f'){
                    if(Com.startsWith(Code_String.FOR+" ")){
                            int setFor=0;
                            while(true){
                                System.out.print("...");
                                try {
                                    SB=SS.StrBlankDeal_with(StringRead.Read());
                                    LineNumbers++;
                                } catch (Exception e) {
                                    AS.error.Error("錯誤！"+e.getMessage()+"\n");
                                    continue;
                                }
                                if(SB!=null){//當Com為null就代表這行為空白或只有註解，並沒有程式碼
                                    if(SS.brackets!=0||SS.append){//如果括弧樹區間不等於零或加入在指令後端為真
                                        if(SS.add){//如果建立新指令為真
                                            SS.add=false;
                                            command.add(new Command(SB,LineNumbers));
                                        }else
                                            command.get(command.size()-1).Command.append(SB);
                                        SS.append=false;
                                    }else
                                        command.add(new Command(SB,LineNumbers));
                                }else
                                    continue;
                                if(SB.toString().startsWith(Code_String.FOR+" "))
                                    setFor++;
                                else if(StringScan.startsWith(SB.toString(),Code_String.ENDFO))
                                    if(setFor--==0)
                                         break;
                            }
                            ch=' ';
                    }else if(Com.startsWith(Code_String.FUNCTION+" ")){
                            int setFun=0;
                            while(true){
                                System.out.print("...");
                                try {
                                    SB=SS.StrBlankDeal_with(StringRead.Read());
                                    LineNumbers++;
                                } catch (Exception e) {
                                    AS.error.Error("錯誤！"+e.getMessage()+"\n");
                                    continue;
                                }
                                if(SB!=null){//當Com為null就代表這行為空白或只有註解，並沒有程式碼
                                    if(SS.brackets!=0||SS.append){//如果括弧樹區間不等於零或加入在指令後端為真
                                        if(SS.add){//如果建立新指令為真
                                            SS.add=false;
                                            command.add(new Command(SB,LineNumbers));
                                        }else
                                            command.get(command.size()-1).Command.append(SB);
                                        SS.append=false;
                                    }else
                                        command.add(new Command(SB,LineNumbers));
                                }else
                                    continue;
                                if(SB.toString().startsWith(Code_String.FUNCTION+" "))
                                    setFun++;
                                else if(StringScan.startsWith(SB.toString(),Code_String.ENDFU))
                                    if(setFun--==0)
                                         break;
                            }
                            ch=' ';
                    }if(SS.brackets==0&&StringScan.startsWith(Com,Code_String.FINALLY))
                        System.out.println(Code_String.FINALLY+"無法單獨使用，須和"+Code_String.TRY+"一同使用。");
                    else
                            ch=' ';
            }else if(ch=='i'){
                    if(Com.startsWith(Code_String.IF+" ")){
                            int setIf=0;
                            while(true){
                                System.out.print("...");
                                try {
                                    SB=SS.StrBlankDeal_with(StringRead.Read());
                                    LineNumbers++;
                                } catch (Exception e) {
                                    AS.error.Error("錯誤！"+e.getMessage()+"\n");
                                    continue;
                                }
                                if(SB!=null){//當Com為null就代表這行為空白或只有註解，並沒有程式碼
                                    if(SS.brackets!=0||SS.append){//如果括弧樹區間不等於零或加入在指令後端為真
                                        if(SS.add){//如果建立新指令為真
                                            SS.add=false;
                                            command.add(new Command(SB,LineNumbers));
                                        }else
                                            command.get(command.size()-1).Command.append(SB);
                                        SS.append=false;
                                    }else
                                        command.add(new Command(SB,LineNumbers));
                                }else
                                    continue;
                                if(SB.toString().startsWith(Code_String.IF+" "))
                                    setIf++;
                                else if(StringScan.startsWith(SB.toString(),Code_String.ENDIF))
                                    if(setIf--==0)
                                         break;
                            }
                            ch=' ';
                    }else
                            ch=' ';
            }else if(ch=='t'){
                    if(StringScan.startsWith(Com,Code_String.TRY)){
                            int setTry=0;
                            while(true){
                                System.out.print("...");
                                try {
                                    SB=SS.StrBlankDeal_with(StringRead.Read());
                                    LineNumbers++;
                                } catch (Exception e) {
                                    AS.error.Error("錯誤！"+e.getMessage()+"\n");
                                    continue;
                                }
                                if(SB!=null){//當Com為null就代表這行為空白或只有註解，並沒有程式碼
                                    if(SS.brackets!=0||SS.append){//如果括弧樹區間不等於零或加入在指令後端為真
                                        if(SS.add){//如果建立新指令為真
                                            SS.add=false;
                                            command.add(new Command(SB,LineNumbers));
                                        }else
                                            command.get(command.size()-1).Command.append(SB);
                                        SS.append=false;
                                    }else
                                        command.add(new Command(SB,LineNumbers));
                                }else
                                    continue;
                                if(StringScan.startsWith(SB.toString(),Code_String.TRY))
                                    setTry++;
                                else if(StringScan.startsWith(SB.toString(),Code_String.ENDTR))
                                    if(setTry--==0)
                                         break;
                            }
                            ch=' ';
                    }else
                            ch=' ';
            }else if(ch=='w'){
                    if(Com.startsWith(Code_String.WHILE+" ")){
                            int setWh=0;
                            while(true){
                                System.out.print("...");
                                try {
                                    SB=SS.StrBlankDeal_with(StringRead.Read());
                                    LineNumbers++;
                                } catch (Exception e) {
                                    AS.error.Error("錯誤！"+e.getMessage()+"\n");
                                    continue;
                                }
                                if(SB!=null){//當Com為null就代表這行為空白或只有註解，並沒有程式碼
                                    if(SS.brackets!=0||SS.append){//如果括弧樹區間不等於零或加入在指令後端為真
                                        if(SS.add){//如果建立新指令為真
                                            SS.add=false;
                                            command.add(new Command(SB,LineNumbers));
                                        }else
                                            command.get(command.size()-1).Command.append(SB);
                                        SS.append=false;
                                    }else
                                        command.add(new Command(SB,LineNumbers));
                                }else
                                    continue;
                                if(SB.toString().startsWith(Code_String.WHILE+" "))
                                    setWh++;
                                else if(StringScan.startsWith(SB.toString(),Code_String.ENDWH))
                                    if(setWh--==0)
                                         break;
                            }
                            ch=' ';
                    }else
                            ch=' ';
            }else if(SS.brackets==0&&Com.startsWith(":")){
                    System.out.println("標籤在一般模式下無法使用，只能在腳本檔或多行輸入模式中使用。");
            }else
                    ch=' ';
            if(ch==' '){
                if(SS.brackets==0){//如果括弧樹區間不等於零
                    LineNumbers=1;
                    SS.add=false;
                    AS.ComLineRun(ValueArray,command);
                    synchronized(this){
                        try {
                            this.wait();
                        } catch (InterruptedException e) {}
                    }
                }
            }else
                command.clear();
        }
    } 
}

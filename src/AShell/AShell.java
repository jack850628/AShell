package AShell;

import AShell.AShellException.AShellException;
import AShell.CommandResolve.CreateSyntaxTree;
import AShell.Memory_Management.Memory_Management;
import AShell.Memory_Management.GetParent;
import AShell.CommandResolve.ForArgsScan;
import AShell.Data_Type_And_Struct.Value_Array;
import AShell.CommandResolve.Command;
import AShell.CommandResolve.ArrayNameResolve;
import AShell.CommandResolve.CommandArray;
import AShell.CommandResolve.FunctionNameResolve;
import AShell.CommandResolve.StringScan;
import AShell.Data_Type_And_Struct.AShell_this;
import AShell.Data_Type_And_Struct.Class_Type;
import AShell.Data_Type_And_Struct.Code_String;
import AShell.Data_Type_And_Struct.Function;
import AShell.Data_Type_And_Struct.Value;
import AShell.Data_Type_And_Struct.Native_Function;
import AShell.Data_Type_And_Struct.Type_String;
import AShell.ValueProcess.StrDW;
import AShell.ValueProcess.VarStrDW;
import AShell.InterFact.Clear;
import AShell.InterFact.Read;
import AShell.InterFact.Print;
import AShell.InterFact.Error;
import AShell.InterFact.Stop;
import AShell.ValueProcess.AShellType;
import AShell.ValueProcess.VarMode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
//import java.util.Properties;

public class AShell {
    private String version="1.1.9";
    private boolean interactiveMode=false;//互動模式判斷
    Value_Array ValueArray=new Value_Array(null);
    private Thread_List ThreadList=new Thread_List();//執行續記錄清單，清單中都紀錄正在執行中的執行續
    public StringBuilder RuningPath=new StringBuilder();//現在執行中環境的路徑
    private final LinkedHashSet<String> AShellLibrariesPath=new LinkedHashSet<>()/*"native/"*/;//預設函式庫路徑
    private final LinkedHashSet<URL> NativeURLPath=new LinkedHashSet<>();//Native Class來源路徑
    private final HashMap<String,Class> javaClass=new HashMap<>();//已經載入java clss
    //public final String Sound_Path="C:\\AShell\\AShell\\sound";
    private final String PathType="\\";//資料夾分隔符號類型
    //private final int SystemPathType=3;//作業系統磁碟跟目錄文字長度，例如Windows的C:\長度是3，Linux則是 / 長度是1
    public StringBuilder AShellArgs;//儲存呼叫AShell腳本時候的參數陣列
    //-----------------------------------IO--------------------------------------------------
    public Print print;
    public Error error;
    public Stop stop;
    public Read read;
    public Clear clear;
    //-------------------------------------------------------------------------------------------
    //long sLog;
    public AShell(Print print,Error error,Clear clear,Read read,Stop stop,String args[]){
        this.print=print;
        this.error=error;
        this.stop=stop;
        this.read=read;
        this.clear=clear;
        AShellLibrariesPath.add("C:\\AShell\\using_file");
        try{
            NativeURLPath.add(new File("C:\\AShell\\").toURL());
        }catch(MalformedURLException e){}
        //----------------------------------------------將base主類別加入記憶體清單中-----------------------------------------------------//
        Class_Type base = new Class_Type(null);
        base.ValueArray=ValueArray;
        ValueArray.add(new Value(new StringBuilder("base"),Memory_Management.Object_Builder(base,1)));
        //----------------------------------------------------------------------------------------------------------------------------------------------//
        //----------------------------------------------將參數主類別加入記憶體清單中----------------------------------------------------//
        if(args!=null){
            Value_Array VA = new Value_Array(null);//第一維陣列的第一個元素的變數清單，主要用來存放size函數
            Function size=new Function(null,"");
            size.CodeArray.add(new Command(new StringBuilder(Code_String.RETURN+" "+args.length),0));
            VA.add(new Value(new StringBuilder("size"),Memory_Management.Function_Builder(size,1)));
            AShellArgs=Memory_Management.Array_Builder(null,StringScan.to_AShell_String(args[0]),VA,1);
            try {
                Memory_Management.set_Array_Sise(AShellArgs.toString(), args.length);
            } catch (Exception ex) {}
            for(int i=1;i<args.length;i++)
                Memory_Management.Array_Builder(null,StringScan.to_AShell_String(args[i]),null);
        }else
            AShellArgs=new StringBuilder(Type_String.NULL);
        //----------------------------------------------------------------------------------------------------------------------------------------------//
    }
    public Value_Array interactive_Mode_Start(Stop stop,String Run_path){
        this.RuningPath.append(Run_path);
        CallSystem.chdir(Run_path);
        this.stop=stop;
        interactiveMode=true;
        return ValueArray;
    }
    public String getVar(){
        return "Ver:"+version;
    }
    private String Read(int mode){
        return read.Rand(mode);
    }
    /*private void Thread_Damage(Run r){
        if(r.r!=null)
            Thread_Damage(r.r);
        r.run=false;
        r.interrupt();
    }*/
    public void Stop(){
        //try{
        ThreadList.stream().forEach((Thread) -> {
            //Thread_Damage(r);
            while(Thread.Run!=null){
                try{
                    Thread.RunState=false;
                    Thread.Run.interrupt();
                }catch(Exception e){
                    //System.err.println("Log:"+(e.getMessage()));
                    //Read(1);
                }
                Thread.Run=Thread.Run.BackThread;
            }
        });
        /*}catch(Exception e){
            System.err.println("Log:"+(e.getMessage()));
            Read(1);
        }*/
        ThreadList.clear();
        if(!interactiveMode){
            RuningPath.delete(0, RuningPath.length());
            ValueArray.clear();
        }
    }
    public String Run_Function(Value_Array ValueArray,CommandArray command,AShell.Run_Point RP) throws AShellException{
       Run run=new Run(ValueArray,command,RP);
       run.run();
       //run.start();
       //run.join();
       if(run.SESC.state==SubEndStateCode.State.Exception)
           throw new AShellException(run.SESC);
       return run.SESC.Message;   
    }
   public void Start_Run( File fileName){
       try{
             RuningPath.append(fileName.getParent());
             CallSystem.chdir(fileName.getParent());
       }catch(Exception e){
           error.Error("發生錯誤！"+e.getMessage()+"\n");
           print.Print("請按任意鍵結束...");
           Read(1);
       }
       Run_Point RP = new Run_Point();
       CommandArray command=new CommandArray(fileName.getName());
       command.add(new Command(new StringBuilder("using "+AutoLibraryConfig.LIBRARY_NAME),0));
       command.add(new Command(new StringBuilder("call "+fileName.getName()),0));
       Run run=new Run(ValueArray,command,RP);
           //RP.Run=run;這動作在建構式裡已經做過了
       run.scriptPath=RuningPath.toString();
       run.com.scriptPath=run.scriptPath;
       run.NotIsFunction=true;
       ThreadList.add(RP);
       run.start();
       /*Com com=new Com(new Run_Point(),null,null);
       com.setText("call "+fileNane.getName());
        try {
            com.command(ValueArray);
            //sLog = System.currentTimeMillis();
        } catch (Exception e) {
            Print("發生錯誤！"+e.getMessage()+"\n",null);
            Print("請按任意鍵結束...",null);
            Read(1);
        }*/
   }
   public void ComLineRun(Value_Array ValueArray, CommandArray command){
	   CreateSyntaxTree.CST(command);
           Run_Point RP = new Run_Point();
	   Run run=new Run(ValueArray,command,RP);
           //RP.Run=run;這動作在建構式裡已經做過了
           run.scriptPath=RuningPath.toString();
           run.com.scriptPath=run.scriptPath;
	   run.NotIsFunction=true;
           ThreadList.add(RP);
           run.start();
   }
   private class Com{
        Value_Array ValueArray;
        Run_Point RP;
        public Run Thread=null; 
        private String com;
        String scriptPath;//腳本所在路徑
       /**將執行續傳入Eview的方法
	 * @param r：現在執行的執行序**/
	public Com(String scriptPath,Run_Point RP,Run Thread,Value_Array ValueArray){
                this.scriptPath=scriptPath;
                this.RP=RP;
                this.ValueArray=ValueArray;
		this.Thread=Thread;
	}
       private void setText(String com){
            this.com=com;
       }
       private String getText(){
           return com;
       }
       private void var(AShell.Run_Point RP,Value_Array ValueArray,String Str,VarMode.Mode Mode) throws Exception{
			ArrayList<StringBuilder> AStr=StringScan.strSplit(Str);
                        for(StringBuilder str:AStr){
                            new VarStrDW(AShell.this,RP,str.toString(),ValueArray,Mode);
                        }
	}
	/*private void write(AShell.Run_Point RP,Value_Array ValueArray,String Str) throws Exception{
		Print(new StrDW(AShell.this,RP,Str,ValueArray,0).Str.toString(),r);
	}*/
       private void command(Value_Array ValueArray) throws Exception {
		//ll.removeViewAt(--i);//會--i的原因是因為原本指標指向了下一個輸入View，但是因為不要留下輸入紀錄，所以需要刪除紀錄View，所以先把指標移向紀錄View再刪除，等等因刪除紀錄View的空格就會被輸入View取代。
		//System.out.println(getText());
                //setText(StringScan.StrBlankDeal_with(getText()).toString());
		char ch=' ';
		try{
			ch=getText().charAt(0);
		}catch(Exception e){}
		// Just ignore the [Enter] key
                    switch (ch) {
                        case 'a':
                            if(getText().startsWith(Code_String.ADDSO+" ")){//增加內建腳本來源路徑(AddSource的縮寫)
                                File Source=new File(new StrDW(AShell.this,RP,getText().substring(Code_String.ADDSO.length()+1).trim(),ValueArray).Str.toString());
                                if(!Source.exists())
                                    throw new Exception("路徑'"+Source.getPath()+"'不存在。");
                                if(!Source.isDirectory())
                                    throw new Exception("'"+Source.getPath()+"'不是資料夾。");
                                AShellLibrariesPath.add(Source.getPath());
                            }else if(getText().startsWith(Code_String.ADDNPH+" ")){//增加Native Class來源路徑(AddNativePath的縮寫)
                                File Source=new File(new StrDW(AShell.this,RP,getText().substring(Code_String.ADDNPH.length()+1).trim(),ValueArray).Str.toString());
                                if(!Source.exists())
                                    throw new Exception("路徑'"+Source.getPath()+"'不存在。");
                                if(!Source.isDirectory())
                                    throw new Exception("'"+Source.getPath()+"'不是資料夾。");
                                NativeURLPath.add(Source.toURL());
                            }else
                                ch=' ';
                            break;
                        case 'c':
                            if(getText().startsWith(Code_String.CALL+" ")){
                                if(scriptPath==null)//當scriptPath為null就代表呼叫cll的地方不再腳本區塊的最外層
                                    throw new Exception("函數或類別中不可以使用call。");
                                StringBuilder FileName=new StringBuilder(scriptPath).append(PathType).append(getText().substring(Code_String.CALL.length()+1).trim());
                                if(FileName.length()<4||!FileName.substring(FileName.length()-4, FileName.length()).equals(".ash"))
                                    FileName.append(".ash");
                                if(!ValueArray.UsingAndCallTable.contains(FileName.toString())){
                                    File scriptFile=new File(FileName.toString());
                                    if(scriptFile.isFile()){
                                        ValueArray.UsingAndCallTable.add(FileName.toString());
                                        Run run=new Run(new FileReader(FileName.toString()),FileName.toString(),scriptFile.getParent(),RP,ValueArray/*,false*/);
                                        run.start();
                                        //if(r!=null){
                                            synchronized(Thread){
                                                try {
                                                    Thread.wait();
                                                } catch (InterruptedException e) {}
                                            }
                                        /*}else{
                                            RP.Run=run;
                                           ThreadList.add(RP);//r==null就代表著這是執行最一開始的執行續，所以將最一開始的執行續放入執行續清單
                                        }*/
                                    }else
                                        throw new Exception("未發現檔名為'"+getText().substring(Code_String.CALL.length()+1).trim()+"'的腳本檔。");
                                }
                            }else
                                ch=' ';
                            break;
                        case 'n':
                            if(getText().startsWith(Code_String.NATIVE+" ")){//讓AShell可以使用jav函數
                            /**
                                                                        AShell中調用java函數的語法(以Stdio.print為例子)

                                                                                native print("AShell_Stdio")

                                                                        其中native關鍵字表明是要呼叫已經用java寫好的內建函數
                                                                        後面的函數名稱要跟java函數的名稱一樣，有分大小寫
                                                                        函數參數是表明要呼叫的java函數所在的java類別

                                                                        則對應這個例子的java函數如下

                                                                        /**
                                                                        * 提供給AShell的print函數
                                                                        * @param AS AShell語言解析器的主類別
                                                                        *  @param RP 執行續指標，用來記錄一個AShell執行續在執行續清單裡的位置
                                                                        * @param ValueArray 建立這個函數在AShell時的AShell執行緒的變數清單
                                                                        * @param Args AShell函數參數，是一個不定長度參數，AShell與java溝通用的類型交換類別
                                                                        * @return AShell與java溝通用的類型交換類別
                                                                        * @throws java.lang.Exception 類型轉換錯誤
                                                                        */
                               /*
                                                                        public class AShell_Stdio {
                                                                            public static String print(AShell AS,AShell.Run_Point RP,Value_Array ValueArray,String[] Args){
                                                                                ....
                                                                                }
                                                                }*/
                                ArrayNameResolve ANR=new ArrayNameResolve(getText().substring(Code_String.NATIVE.length()+1,getText().length()).trim());//取得函數名稱以及參數
                                if(ANR.Args.isEmpty())
                                    throw new Exception("必須提供一個字串參數來表明java函數所在的java類別");
                                Object instance_java_Class=null;
                                String javaClassName=(new StrDW(AShell.this,RP,ANR.Args.get(0).toString(),ValueArray)).Str.toString();
                                if(ValueArray.instance_Class_Map.containsKey(javaClassName))//查看該AShell變數清單中是否存在著已經實例化的該native函數的java類別
                                    instance_java_Class=ValueArray.instance_Class_Map.get(javaClassName);//有的話，就從HashMap中取出
                                else//如果沒有
                                    try{
                                        Class JClass;
                                        if(javaClass.containsKey(javaClassName))//檢查是否已經有載入過該java class
                                            JClass=javaClass.get(javaClassName);
                                        else{
                                            URL[] urlarr=new URL[NativeURLPath.size()];
                                            NativeURLPath.toArray(urlarr);
                                            JClass=new URLClassLoader(urlarr).loadClass(javaClassName);
                                            javaClass.put(javaClassName, JClass);
                                        }
                                        instance_java_Class=JClass.newInstance();//取得java函數所在的類別併實例化
                                        //instance_java_Class=Class.forName("AShell.Native_Class."+javaClassName).newInstance();//取得java函數所在的類別併實例化
                                        ValueArray.instance_Class_Map.put(javaClassName, instance_java_Class);//放入至/查看該AShell變數清單中的HashMap
                                    }catch(ClassNotFoundException | InstantiationException | IllegalAccessException | NoClassDefFoundError e){
                                        throw new Exception("java類別'"+e.getMessage()+"'未找到");
                                    }
                                /**
                                  Value_Array VA=ValueArray;
                                do{//檢查哪一層有已經實例化的該native函數的java類別
                                    if(VA.instance_Class_Map.containsKey(javaClassName)){//查看該AShell變數清單中是否存在著已經實例化的該native函數的java類別
                                        instance_java_Class=VA.instance_Class_Map.get(javaClassName);//有的話，就從HashMap中取出
                                        break;
                                    }
                                }while((VA=VA.Previous_Floor)!=null);
                                if(instance_java_Class==null)//如果沒有
                                    try{
                                        Class JClass;
                                        if(javaClass.containsKey(javaClassName))//檢查是否已經有載入過該java class
                                            JClass=javaClass.get(javaClassName);
                                        else{
                                            URL[] urlarr=new URL[NativeURLPath.size()];
                                            NativeURLPath.toArray(urlarr);
                                            JClass=new URLClassLoader(urlarr).loadClass(javaClassName);
                                            javaClass.put(javaClassName, JClass);
                                        }
                                        instance_java_Class=JClass.newInstance();//取得java函數所在的類別併實例化
                                        //instance_java_Class=Class.forName("AShell.Native_Class."+javaClassName).newInstance();//取得java函數所在的類別併實例化
                                        ValueArray.instance_Class_Map.put(javaClassName, instance_java_Class);//放入至/查看該AShell變數清單中的HashMap
                                    }catch(ClassNotFoundException | InstantiationException | IllegalAccessException | NoClassDefFoundError e){
                                        throw new Exception("java類別'"+e.getMessage()+"'未找到");
                                    }
                                 */
                                Method Java_Function = null;
                                try{
                                    Java_Function = instance_java_Class.getClass().getMethod(ANR.Name.toString(),new Class[]{AShell_this.class,AShellType[].class});//取得java函數，其中函數的規格上面的註解有說明
                                }catch(NoSuchMethodException | SecurityException e){
                                    throw new Exception("java函數'"+e.getMessage()+"'未找到");
                                }
                                new VarStrDW(AShell.this,RP,ANR.Name.toString()+"="+Memory_Management.Native_Function_Builder(new Native_Function(Java_Function,ValueArray.Reference(),instance_java_Class)),ValueArray,VarMode.Mode.Var);
                            /*}else if(getText().startsWith("npcall ")){
                                String FireName=path.toString()+PathType+getText().substring(7).trim();
                                if(new File(FireName).isFile()||new File(FireName+=".ash").isFile()){
                                    Run_Point RP=new Run_Point();
                                    Run run=new Run(new FileReader(FireName),RP,ValueListSet.ListSet(new Value_Array(null), ValueArray),true);
                                    RP.Run=run;
                                    run.start();
                                    ThreadList.add(RP);
                                }else
                                    throw new Exception("未發現檔名為'"+getText().substring(7).trim()+"'的腳本檔。");*/
                            }else
                                ch=' ';
                            break;
                        case 'u':
                            if(getText().startsWith(Code_String.USING+" ")){
                                /*try{
                                    InputStream is=AShell.this.getClass().getResourceAsStream(def_path+getText().substring(6).trim());
                                    Run run=new Run(is,r,ValueArray);
                                    run.start();
                                    if(r!=null){
                                        synchronized(r){
                                            try {
                                                r.wait();
                                            } catch (InterruptedException e) {}
                                        }
                                    }else
                                        ThreadList.add(run);//r!=null就代表著這一次的呼叫是從開始按鈕發出，所以將執行續加入執行序列
                                }catch(Exception e){
                                    //System.out.println(e.getLocalizedMessage());
                                    throw new Exception("未發現檔名為'"+getText().substring(6).trim()+"'的內建腳本檔。");
                                }*/
                                StringBuilder FileName=new StringBuilder(PathType).append(getText().substring(Code_String.USING.length()+1).trim());
                                if(FileName.length()<4||!FileName.substring(FileName.length()-4, FileName.length()).equals(".ash"))
                                    FileName.append(".ash");
                                int index=1;
                                boolean isUsing=false;
                                for(String SourcePath:AShellLibrariesPath){
                                    FileName.insert(0 ,SourcePath);
                                    if(!ValueArray.UsingAndCallTable.contains(FileName.toString())){
                                        File scriptFile=new File(FileName.toString());
                                        if(scriptFile.isFile()){
                                            ValueArray.UsingAndCallTable.add(FileName.toString());
                                            Run run=new Run(new FileReader(FileName.toString()),FileName.toString(),scriptFile.getParent(),RP,ValueArray/*,false*/);
                                            run.start();
                                            //if(r!=null){
                                                synchronized(Thread){
                                                    try {
                                                        Thread.wait();
                                                    } catch (InterruptedException e) {}
                                                }
                                            //}else
                                            //    ThreadList.add(RP);//r!=null就代表著這一次的呼叫是從開始按鈕發出，所以將執行續加入執行序列
                                            break;
                                        }else
                                            if(!isUsing&&index==AShellLibrariesPath.size())
                                                throw new Exception("未發現檔名為'"+getText().substring(Code_String.USING.length()+1).trim()+"'的內建腳本檔。");
                                    }else
                                        isUsing=true;
                                    index++;
                                    FileName.delete(0, SourcePath.length());
                                }
                            }else if(getText().startsWith(Code_String.UNVAR+" ")){
                                var(RP,ValueArray,getText().substring(Code_String.UNVAR.length()+1).trim(),VarMode.Mode.Unvar);
                            }else
                                ch=' ';
                            break;
                        case 'v':
                            if(getText().startsWith(Code_String.VAR+" ")){
                                //System.out.println("Var開始:"+(System.currentTimeMillis()-sLog));
                                var(RP,ValueArray,getText().substring(Code_String.VAR.length()+1).trim(),VarMode.Mode.Var);
                                //System.out.println("Var結束:"+(System.currentTimeMillis()-sLog));
                            }else
                                ch=' ';
                            break;
                        default:
                            ch=' ';
                            break;
                    }
                    if(ch==' '){
                        if(StringScan.EqualsScan(getText()))
                            var(RP,ValueArray,getText(),VarMode.Mode.General);
                        else{
                            StringBuilder Str;
                            if(!interactiveMode)
                                Str=new StrDW(AShell.this,RP,getText(),ValueArray).Str;
                            else
                                print.Print((Str=new VarStrDW(AShell.this,RP,getText(),ValueArray,VarMode.Mode.Intermediary).Str).toString()+"\n");
                            if(Str.toString().matches(Type_String.MEMORY_TYPE))
                                Memory_Management.Cut_To_Arguments(Str.toString());//將參考指數減一
                        }
                    }
                //System.gc();
       }
   }
        public static class  Run_Point{//執行續指標，用來記錄一個AShell執行續在執行續清單裡的位置
            public boolean RunState=true;//執行狀態
            static int count=0;//執行續編號遞增值
            int index;//執行續編號
            AShell.Run Run=null;//用來記錄該執行續現在執行中的java執行續
            public Run_Point(){
                this.index=Run_Point.count++;
            }
        }
        public Run_Point Thread_Run(Value_Array ValueArray){//產生新的AShell執行續
             Run_Point RP=new Run_Point();
            CommandArray command=new CommandArray("Thread"+RP.index);
            //command.add(new CommandArray(new StringBuilder("Stdio.println(\"\\n\\nLog: \".."+Type_String.THIS+".run)")));
             command.add(new Command(new StringBuilder("run()"),0));
             Run run=new Run(ValueArray,command,RP);
             run.NotIsFunction=true;//設定為非函數執行狀態
             RP.Run=run;
             //System.out.println("LOG1:"+ThreadList+" "+ThreadList.size());
             ThreadList.add(RP);
             //System.out.println("LOG2:"+ThreadList+" "+ThreadList.size());
             run.start();
             return RP;
        }
        public void Thread_Wait(Run_Point RP,long t){//Wait AShell執行續
            synchronized(RP.Run){//目前發現似乎無法同步正在執行中的執行緒，原因好像是嘗試同步的執行緒與正在執行中要被執行緒本身互斥了，因此同步失敗，所以執行續只能暫停自己不能暫停別人
                try {
                    RP.Run.wait(t);
                } catch (InterruptedException e) {}
                //System.err.println("java:we");
            }
        }
        public void Thread_Notify(Run_Point RP){//Notify AShell執行續
            synchronized(RP.Run){
                RP.Run.notify();
            }
        }
        public void Thread_Notifyall(){ //Notify AShell執行續
            ThreadList.stream().forEach((r) -> {
                synchronized(r.Run){
                    r.Run.notify();
                }
            });
        }
        static class try_Count {
            public static enum State{Break,Continue,Tag,Exception,Return,None};//區塊結束狀態碼的所有狀態
            void set_Try(String Code,int LineNumbers,String fileName,String Message){
                    this.state=State.Exception;
                    this.Code=Code;
                    this.LineNumbers=LineNumbers;
                    this.fileName=fileName;
                    this.Message=StringScan.Auto_Type_Change(Message).toString();
            }
            void set_Returm(String Message){
                if(this.state!=State.Exception){
                    this.state=State.Return;
                    this.Message=Message;
                }
            }
            void set_Tag(String Message/*int Index*/){
                if(this.state!=State.Exception){
                    this.state=State.Tag;
                    this.Message=Message;
                    //this.Index=Index;
                }
            }
            void set_Break(){
                if(this.state!=State.Exception&&this.state!=State.Return&&this.state!=State.Tag){
                    this.state=State.Break;
                }
            }
            void set_Continue(){
                if(this.state!=State.Exception&&this.state!=State.Return&&this.state!=State.Tag){
                    this.state=State.Continue;
                }
            }
            State state=State.None;//try狀態，Exception有層級最大，Return和Tag第二高
            String Message;//儲存例外資訊，以AShell類型存放，Tag標籤名稱
            String Code;//發生錯誤的程式碼
            //int Index;//當遇到tag時儲存Tag中的跳轉程式碼行數
            int LineNumbers;//發生錯誤的程式碼的行數
            String fileName;//發生錯誤的程式碼所在的檔案名稱
        }
        static class If_Count {
            public If_Count(int ifc){//這個是IF的狀態紀錄，用來記錄IF下面哪一段指令需要執行與跳過
                    this.ifc=ifc;
            }
            public int ifc;//執行紀錄(1=已執行、0=未執行、-1=IF已執行結束)
        }
    	class Run extends Thread{
                String scriptPath=null;//腳本所在路徑
                Value_Array ValueArray;
                SubEndStateCode SESC=new SubEndStateCode();//區塊結束狀態碼
		//ArrayList<If_Count> ifArray=new ArrayList<>();//if的堆疊，用來存放被執行到的IF的狀態
                //ArrayList<try_Count> tryArray=new ArrayList<>();//try的堆疊，用來存放被執行到的TRY的狀態
		//ArrayList<WhileC> whileArray=new ArrayList<>();//While的堆疊，用來存放被執行到的While的狀態
		//ArrayList<WhileC> dowhileArray=new ArrayList<>();//Do-While的堆疊，用來存放被執行到的Do-While的狀態
		CommandArray command;//用來存放讀出的批次指令
		//ArrayList<Integer> LoopCount=new ArrayList<>();//迴圈型態堆疊，紀錄當前所執行的迴圈型態(0是IF，1是WHILE，2是DO-DWHILE)
                Com com;
                Run_Point RP=null;
		Run BackThread=null;//如果是上一個批次檔呼叫這次的腳本檔，則這個變數就會是上一個腳本檔的執行續
                //StringBuilder Return =new StringBuilder("null");//函數的回傳直，當函數執行續發生例外時，會充當錯誤訊息回傳直
                boolean NotIsFunction=false;//判斷是否要清除指令陣列，當這個變數為真時，就代表著這個執行續不是函數的
                //boolean isNpcall=false;//判斷執行續是不是用npcall指令創造
                //int setFun=0;//函數淵告判斷值，0為沒再宣告，1為宣告中，1以上為宣告函數中的函數
                //int funidx;//函數指數，用來記錄現在宣告中的函數在變數陣列中的位置
                public Run(FileReader fr,String fileName,String scriptPath,Run_Point RP,Value_Array ValueArray/*,boolean isNpcall*/) throws IOException, Exception{
                        NotIsFunction=true;
                        this.ValueArray=ValueArray;
                        this.scriptPath=scriptPath;
                        StringScan SS=new StringScan();//實例化空白、註解過濾器
                        command=new CommandArray(fileName);
                        try (BufferedReader br = new BufferedReader(fr)) {
                            String s;
                            StringBuilder Com;
                            int LineNumbers=1;
                            //System.out.println("S---------------------------------------------------");
                            while((s=br.readLine())!=null){//讀批次檔
                                Com=SS.StrBlankDeal_with(s);
                                if(Com!=null){//當Com為null就代表這行為空白或只有註解，並沒有程式碼
                                    if(SS.brackets!=0||SS.append){//如果括弧樹區間不等於零或加入在指令後端為真
                                        if(SS.add){//如果建立新指令為真
                                            SS.add=false;
                                            command.add(new Command(Com,LineNumbers));
                                        }else
                                            command.get(command.size()-1).Command.append(Com);
                                        SS.append=false;
                                    }else
                                        command.add(new Command(Com,LineNumbers));
                                }
                                LineNumbers++;
                            }
                            fr.close();
                            //System.out.println("E---------------------------------------------------");
                            SS.Annotation=false;//關閉多行註解
                        }
                        CreateSyntaxTree.CST(command);
                        /*for(CommandArray a:command)
                            System.err.println(a.Command.toString());*/
                        this.BackThread=RP.Run;
                        RP.Run=this;
                        this.RP=RP;
                        com=new Com(scriptPath,RP,this,ValueArray);
                        //this.isNpcall=isNpcall;
		}
               /*public Run(InputStream is,Run r,Value_Array ValueArray) throws IOException, Exception{//using指令用
                        Clear=true;
                        this.ValueArray=ValueArray;
                        Tag=new ArrayList<>();
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                            String s;
                            StringBuffer Com;
                            //System.out.println("S---------------------------------------------------");
                            while((s=br.readLine())!=null){//讀批次檔
                                Com=StringScan.StrBlankDeal_with(s);
                                if(Com!=null){//當Com為null就代表這行為空白或只有註解，並沒有程式碼
                                    if(StringScan.brackets!=0||StringScan.append){//如果括弧樹區間不等於零或加入在指令後端為真
                                        if(StringScan.add){//如果建立新指令為真
                                            StringScan.add=false;
                                            command.add(new CommandArray(Com));
                                        }else
                                            command.get(command.size()-1).Command.append(Com);
                                        StringScan.append=false;
                                    }else
                                        command.add(new CommandArray(Com));
                                }
                            }
                            //System.out.println("E---------------------------------------------------");
                            StringScan.Annotation=false;//關閉多行註解
                        }
                        CreateSyntaxTree.CST(command,Tag);
                        if(r!=null)
                            ThreadList.set( ThreadList.indexOf(r), this);
			this.r=r;
                        com=new Com(this,ValueArray);
		}*/
                public Run(Value_Array ValueArray,CommandArray command,Run_Point RP){//function呼叫 與 互動式命令列界指令執行 與 AShell啟動時 使用
                        this.ValueArray=ValueArray;
                        this.command=command;
                        this.BackThread=RP.Run;
                        RP.Run=this;
                        this.RP=RP;
                        com=new Com(scriptPath,RP,this,ValueArray);
                }
                int getTag(CommandArray command,String tag){
                    int Number=-2;//會用-2是因為當標籤放在程式碼中第一行的話，在程式碼清單中會是第零行，然後讀出來會被減一所以會變成-1
                    for(int i=0;i<command.tag.size();i++){
                        if(tag.equals(command.tag.get(i).Name)){
                            Number=command.tag.get(i).Number-1;
                            break;
                        }
                    }
                    return Number;
                }
		@Override
		public void run() {
                        If_Count IfState=new If_Count(-1);//用來存放被執行到的IF的狀態
                        try_Count Try=new try_Count();//用來存放被執行到的TRY的狀態
                        for(int ComLenght=0;ComLenght<command.size()&&RP.RunState;ComLenght++){
                            //System.out.println("Log:    "+command.get(ComLenght).Command.toString());
				try{
					if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.BEGIN)){
                                                SESC.Main=false;
                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                if(SESC.state==SubEndStateCode.State.Break)
                                                        throw new Exception("非迴圈區塊不可以使用break指令");
                                                else if(SESC.state==SubEndStateCode.State.Continue)
                                                        throw new Exception("非迴圈區塊不可以使用continue指令");
                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2)
                                                        throw new Exception("標籤\""+SESC.Message+"\"未宣告。");
                                                    SESC.Message=Type_String.NULL;
                                                    ComLenght=Number;
                                                }else if(SESC.state==SubEndStateCode.State.Exception)
                                                        throw new Exception(SESC.Message);
                                                else if(SESC.state==SubEndStateCode.State.Return){
                                                        if(SESC.Message.equals(""))
                                                                SESC.Message=Type_String.NULL;
                                                        break;
                                                }
                                                SESC.Main=true;
					}else if(command.get(ComLenght).Command.toString().startsWith(Code_String.IF+" ")){
                                            if(new VarStrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.IF.length()+1).trim(),ValueArray,VarMode.Mode.Intermediary).Str.toString().matches(Type_String.TRUE+"|1")){
								IfState.ifc=1;
                                                                SESC.Main=false;
                                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                                if(SESC.state==SubEndStateCode.State.Break)
                                                                    throw new Exception("非迴圈區塊不可以使用break指令");
                                                                else if(SESC.state==SubEndStateCode.State.Continue)
                                                                    throw new Exception("非迴圈區塊不可以使用continue指令");
                                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                                    int Number=getTag(command,SESC.Message);
                                                                    if(Number==-2)
                                                                        throw new Exception("標籤\""+SESC.Message+"\"未宣告。");
                                                                    SESC.Message=Type_String.NULL;
                                                                    ComLenght=Number;
                                                                }else if(SESC.state==SubEndStateCode.State.Exception)
                                                                        throw new Exception(SESC.Message);
                                                                else if(SESC.state==SubEndStateCode.State.Return){
                                                                    if(SESC.Message.equals(""))
                                                                        SESC.Message=Type_String.NULL;
                                                                    break;
                                                                }
							}else{
								IfState.ifc=0;
							}
					}else if(command.get(ComLenght).Command.toString().startsWith(Code_String.ELSEIF+" ")){
						if(IfState.ifc==0){
                                                    if(new VarStrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.ELSEIF.length()+1).trim(),ValueArray,VarMode.Mode.Intermediary).Str.toString().matches(Type_String.TRUE+"|1")){
                                                                IfState.ifc=1;
                                                                SESC.Main=false;
								Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                                if(SESC.state==SubEndStateCode.State.Break)
                                                                    throw new Exception("非迴圈區塊不可以使用break指令");
                                                                else if(SESC.state==SubEndStateCode.State.Continue)
                                                                    throw new Exception("非迴圈區塊不可以使用continue指令");
                                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                                    int Number=getTag(command,SESC.Message);
                                                                    if(Number==-2)
                                                                        throw new Exception("標籤\""+SESC.Message+"\"未宣告。");
                                                                    SESC.Message=Type_String.NULL;
                                                                    ComLenght=Number;
                                                                }else if(SESC.state==SubEndStateCode.State.Exception)
                                                                        throw new Exception(SESC.Message);
                                                                else if(SESC.state==SubEndStateCode.State.Return){
                                                                    if(SESC.Message.equals(""))
                                                                        SESC.Message=Type_String.NULL;
                                                                    break;
                                                                }
							}
                                                }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ELSE)){
						if(IfState.ifc==0){//ifArray.get(ifArray.size()-1).ifc==0為在ELSE上面的IF和ELSEIF條件都不為真的話
                                                        SESC.Main=false;
							Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                                if(SESC.state==SubEndStateCode.State.Break)
                                                                    throw new Exception("非迴圈區塊不可以使用"+Code_String.BREAK+"指令");
                                                                else if(SESC.state==SubEndStateCode.State.Continue)
                                                                    throw new Exception("非迴圈區塊不可以使用"+Code_String.CONTINUE+"指令");
                                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                                    int Number=getTag(command,SESC.Message);
                                                                    if(Number==-2)
                                                                        throw new Exception("標籤\""+SESC.Message+"\"未宣告。");
                                                                    SESC.Message=Type_String.NULL;
                                                                    ComLenght=Number;
                                                                }else if(SESC.state==SubEndStateCode.State.Exception)
                                                                        throw new Exception(SESC.Message);
                                                                else if(SESC.state==SubEndStateCode.State.Return){
                                                                    if(SESC.Message.equals(""))
                                                                        SESC.Message=Type_String.NULL;
                                                                    break;
                                                                }
                                                }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDIF)){
                                                SESC.Main=true;
						IfState.ifc=-1;
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.TRY)){
                                                SESC.Main=false;
                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                //System.err.println("LOG: "+Return);
                                                if(SESC.state==SubEndStateCode.State.Break)
                                                    Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,"非迴圈區塊不可以使用"+Code_String.BREAK+"指令");
                                                else if(SESC.state==SubEndStateCode.State.Continue)
                                                    Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,"非迴圈區塊不可以使用"+Code_String.CONTINUE+"指令");
                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                    Try.set_Tag(SESC.Message);
                                                }else if(SESC.state==SubEndStateCode.State.Exception)
                                                    Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,SESC.Message);
                                                else if(SESC.state==SubEndStateCode.State.Return)
                                                    Try.set_Returm(SESC.Message);
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.CATCH+" ")){
                                                if(Try.state==try_Count.State.Exception){
                                                    Try.state=try_Count.State.None;
                                                    Run_Begin_or_If_or_Try_or_Catch_or_Finally(true,command.get(ComLenght).Command.substring(Code_String.CATCH.length()+1).trim()+"="+Try.Message
                                                            ,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                    if(SESC.state==SubEndStateCode.State.Break)
                                                        Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,"非迴圈區塊不可以使用"+Code_String.BREAK+"指令");
                                                    else if(SESC.state==SubEndStateCode.State.Continue)
                                                        Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,"非迴圈區塊不可以使用"+Code_String.CONTINUE+"指令");
                                                    else if(SESC.state==SubEndStateCode.State.Tag)
                                                        Try.set_Tag(SESC.Message);
                                                    else if(SESC.state==SubEndStateCode.State.Exception)
                                                        Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,SESC.Message);
                                                    else if(SESC.state==SubEndStateCode.State.Return)
                                                        Try.set_Returm(SESC.Message);
                                                }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.FINALLY)){
                                                    Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                    if(SESC.state==SubEndStateCode.State.Break)
                                                        Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,"非迴圈區塊不可以使用"+Code_String.BREAK+"指令");
                                                    else if(SESC.state==SubEndStateCode.State.Continue)
                                                        Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,"非迴圈區塊不可以使用"+Code_String.CONTINUE+"指令");
                                                    else if(SESC.state==SubEndStateCode.State.Tag){
                                                        Try.set_Tag(SESC.Message);
                                                    }else if(SESC.state==SubEndStateCode.State.Exception)
                                                        Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,SESC.Message);
                                                    else if(SESC.state==SubEndStateCode.State.Return)
                                                        Try.set_Returm(SESC.Message);
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDTR)){
                                                if(Try.state==try_Count.State.Exception){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Exception, Try.Code,Try.LineNumbers,SESC.fileName, new StrDW(AShell.this,RP,Try.Message,ValueArray).Str.toString());
                                                    throw new Exception(SESC.Message);
                                                }else if(Try.state==try_Count.State.Return){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Return, null,0,SESC.fileName, (!Try.Message.equals(""))?Try.Message:Type_String.NULL);
                                                    break;
                                                }else if(Try.state==try_Count.State.Tag){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Tag, null,0,SESC.fileName, Type_String.NULL);
                                                    int Number=getTag(command,Try.Message);
                                                    if(Number==-2)
                                                        throw new Exception("標籤\""+Try.Message+"\"未宣告。");
                                                    ComLenght=Number;
                                                }
                                                Try.state=try_Count.State.None;
                                                SESC.Main=true;
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.THROW+" "))
                                                throw new Exception(new StrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.THROW.length()+1).trim(),ValueArray).Str.toString());
                                        else if(command.get(ComLenght).Command.toString().startsWith(Code_String.FOR+" ")){
                                                //System.out.println("for判斷開始:"+(System.currentTimeMillis()-sLog));
                                                SESC.Main=false;
                                                ForArgsScan FAS=new ForArgsScan(command.get(ComLenght).Command.substring(Code_String.FOR.length()+1).trim());
                                                try{
                                                    RunWHILE_FOR(true,FAS.Args.get(0).toString(),FAS.Args.get(1).toString(),FAS.Args.get(2).toString(),
                                                            new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                }catch(final Exception e){
                                                    SESC.Main=true;
                                                    throw e;
                                                }
                                                if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2)
                                                        throw new Exception("標籤\""+SESC.Message+"\"未宣告。");
                                                    SESC.Message=Type_String.NULL;
                                                    ComLenght=Number;
                                                }else if(SESC.state==SubEndStateCode.State.Exception)
                                                    throw new Exception(SESC.Message);
                                                else if(SESC.state==SubEndStateCode.State.Return){
                                                    if(SESC.Message.equals(""))
                                                        SESC.Message=Type_String.NULL;
                                                    break;
                                                }
                                                SESC.Main=true;
                                                //System.out.println("for判斷結束:"+(System.currentTimeMillis()-sLog));
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.WHILE+" ")){
                                                //System.out.println("While判斷開始:"+(System.currentTimeMillis()-sLog));
                                                SESC.Main=false;
                                                try{
                                                    RunWHILE_FOR(false,null,command.get(ComLenght).Command.substring(Code_String.WHILE.length()+1).trim(),null,
                                                            new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                }catch(final Exception e){
                                                    SESC.Main=true;
                                                    throw e;
                                                }
                                                if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2)
                                                        throw new Exception("標籤\""+SESC.Message+"\"未宣告。");
                                                    SESC.Message=Type_String.NULL;
                                                    ComLenght=Number;
                                                }else if(SESC.state==SubEndStateCode.State.Exception)
                                                    throw new Exception(SESC.Message);
                                                else if(SESC.state==SubEndStateCode.State.Return){
                                                    if(SESC.Message.equals(""))
                                                        SESC.Message=Type_String.NULL;
                                                    break;
                                                }
                                                SESC.Main=true;
                                                //System.out.println("While判斷結束:"+(System.currentTimeMillis()-sLog));
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.DWHILE+" ")){//這裡不只用StringScan.startsWith()的原因是，在生成代碼樹的時候會把dwhile後面的條件移到do的後面
                                                SESC.Main=false;
                                                try{
                                                    RunDWHILE(command.get(ComLenght).Command.substring(Code_String.DWHILE.length()+1).trim(),
                                                            new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                }catch(final Exception e){
                                                    SESC.Main=true;
                                                    throw e;
                                                }
                                                if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2)
                                                        throw new Exception("標籤\""+command.get(ComLenght).Command.substring(Code_String.GOTO.length()+1).trim()+"\"未宣告。");
                                                    ComLenght=Number;
                                                }else if(SESC.state==SubEndStateCode.State.Exception)
                                                    throw new Exception(SESC.Message);
                                                else if(SESC.state==SubEndStateCode.State.Return){
                                                    if(SESC.Message.equals(""))
                                                        SESC.Message=Type_String.NULL;
                                                    break;
                                                }
                                                SESC.Main=true;
					}else if(command.get(ComLenght).Command.toString().startsWith(Code_String.GOTO+" ")){
                                            int Number=getTag(command,command.get(ComLenght).Command.substring(Code_String.GOTO.length()+1).trim());
                                            if(Number==-2)
                                                    throw new Exception("標籤\""+command.get(ComLenght).Command.substring(Code_String.GOTO.length()+1).trim()+"\"未宣告。");
                                            ComLenght=Number;
				    }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.FUNCTION+" ")){
                                            FunctionNameResolve FNR=new FunctionNameResolve(command.get(ComLenght).Command.substring(Code_String.FUNCTION.length()+1).trim());
                                            Function fun=new Function(ValueArray.Reference(),command.fileName);
                                            new VarStrDW(AShell.this,RP,FNR.Name.append("=")
                                                    .append(Memory_Management.Function_Builder(fun)).toString(),ValueArray,VarMode.Mode.Var);
                                            for(StringBuilder parameter: FNR.Args){//建立函數參數
                                                String[] arg=parameter.toString().split("=",2);//將函數參數跟=(賦值運算子)符號做分離
                                                if(arg.length==1)
                                                    fun.ValueArray.add(new Value(new StringBuilder(arg[0]),new StringBuilder(Type_String.NULL)));
                                                else
                                                    fun.ValueArray.add(new Value(new StringBuilder(arg[0]),
                                                            new VarStrDW(AShell.this,RP,arg[1],ValueArray,VarMode.Mode.Intermediary).Str));
                                            }
                                            int setFun=0;
                                            if(FNR.Lanbda!=null){
                                                FNR.Lanbda.insert(0, Code_String.RETURN+" ");
                                                fun.CodeArray.add(new Command(FNR.Lanbda,command.get(ComLenght).Command,command.get(ComLenght).LineNumbers));
                                            }else
                                                while(true){
                                                    ComLenght++;
                                                    if(command.get(ComLenght).Command.toString().startsWith(Code_String.FUNCTION+" ")&&!StringScan.matchFunctionLanbda(command.get(ComLenght).Command))
                                                        setFun++;
                                                    else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDFU))
                                                        if(setFun--==0)
                                                            break;
                                                    fun.CodeArray.add(new Command(command.get(ComLenght)));
                                                }
                                            CreateSyntaxTree.CST(fun.CodeArray);
				    }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.CLASS+" ")){
                                            Class_Type Class;
                                            GetParent.ReturnParentStruct RPS;
                                            StringBuilder name=new StringBuilder();
                                            RPS=GetParent.Get_Parent(AShell.this,RP,name, command.get(ComLenght).Command.substring(Code_String.CLASS.length()+1), ValueArray);
                                            Class=new Class_Type(ValueArray);
                                            Class.isSClass();
                                            new VarStrDW(AShell.this,RP,name.append("=")
                                                    .append(Memory_Management.Class_Builder(Class)).toString(),ValueArray,VarMode.Mode.Var);
                                            //ValueListSet.Condition_ListSet(new String[]{"_inst_","_dest_","this","super"},Class.ValueArray, ValueArray);
                                            //------------------------------放入建構式與解構式-------------------------------------//AShell的建構式名稱為_inst_，也就是instance的前四個字母，解構式名稱為_dest_，也就是destructor的前四個字母
                                            /*for(CensorValueReturn FunCVR:CensorValue.CensorValues(new String[]{"_inst_","_dest_","this","super"},Class.ValueArray))
                                                if(FunCVR.Result)
                                                    Class.ValueArray.remove(FunCVR.index);
                                            Class.ValueArray.add(new Value(new StringBuilder(Type_String.INSTANCE_FUNCTION_NAME),Memory_Management.Function_Builder(new Function(Class.ValueArray))));//放入預設的空建構子
                                            Class.ValueArray.add(new Value(new StringBuilder(Type_String.DESTRUCTOR_FUNCTION_NAME),Memory_Management.Function_Builder(new Function(Class.ValueArray))));//放入預設的空建構子*/
                                            //-----------------------------------------------------------------------------------
                                            if(RPS!=null){
                                                for(int i=0;i<RPS.Parent.ValueArray.size();i++){//靜態繼承變數實做
                                                    Value V;
                                                    if((V=RPS.Parent.ValueArray.get(i)).Tent.toString().matches(Type_String.MEMORY_TYPE))//判斷原變數內容是不是記憶體參考
                                                        Memory_Management.Add_To_Arguments(V.Tent.toString());//將參考指數加一
                                                    //if(!Parent.ValueArray.get(i).Name.toString().matches(Type_String.INSTANCE_FUNCTION_NAME+"|"+Type_String.DESTRUCTOR_FUNCTION_NAME))//如果變數不是建構式或解構式
                                                         Class.ValueArray.add(V);//從父變數清單複製一份變數加入倒子變數清單，這個功能就等同於覆寫
                                                }
                                                Class.Parent=RPS.Parent;
                                                Class.ParentAddress=RPS.ParentAddress;
                                            }
                                            Class.CodeArray=new CommandArray(command.fileName);
                                            CommandArray SCA=new CommandArray(command.fileName);
                                            while(true){
                                                ComLenght++;
                                                if(command.get(ComLenght).Command.toString().startsWith(Code_String.CLASS+" ")){
                                                    int CountClass=0;
                                                    while(true){
                                                        Class.CodeArray.add(new Command(command.get(ComLenght)));
                                                        ComLenght++;
                                                        if(command.get(ComLenght).Command.toString().startsWith(Code_String.CLASS+" "))
                                                            CountClass++;
                                                        else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDCL)){
                                                            if(CountClass--==0)
                                                                break;
                                                        }
                                                    }
                                                }else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDCL))
                                                     break;
                                                else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.STATIC)){
                                                    int CountStatic=0;
                                                    while(true){
                                                        ComLenght++;
                                                        if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.STATIC))
                                                            CountStatic++;
                                                        else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDST))
                                                            if(CountStatic--==0)
                                                                break;
                                                        SCA.add(new Command(command.get(ComLenght)));
                                                    }
                                                    continue;
                                                }
                                                Class.CodeArray.add(new Command(command.get(ComLenght)));
                                            }
                                            CreateSyntaxTree.CST(Class.CodeArray);
                                            CreateSyntaxTree.CST(SCA);
                                            Run_Function(Class.ValueArray,SCA,RP);
				    }else if(StringScan.startsWith_for_return(command.get(ComLenght).Command.toString(),Code_String.RETURN)){
                                        String Return=new VarStrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.RETURN.length()).trim(),ValueArray,VarMode.Mode.Intermediary).Str.toString();
                                        SESC.setSubEndStateCode(SubEndStateCode.State.Return, null,0,command.fileName, (!Return.equals(""))?Return:Type_String.NULL);
                                        break;
				    }else{//如果沒在執行或正在執行IF回迴圈且當下指向條件為1(無須跳過，0為須要跳過)則執行
                                                //System.out.println("呼叫一般指令("+command.get(ComLenght)+")開始:"+(System.currentTimeMillis()-sLog));
                                                    com.setText(command.get(ComLenght).Command.toString());
                                                    com.command(ValueArray);
                                                    //System.out.println("呼叫一般指令("+command.get(ComLenght)+")結束:"+(System.currentTimeMillis()-sLog));
                                    }
				}catch (final AShellException e){//接收傳送從函數拋出的錯誤
                                    SESC.setSubEndStateCode(e.SESC.state, e.SESC.Code,e.SESC.LineNumbers,e.SESC.fileName, e.SESC.Message);
                                    if(NotIsFunction){//當執行續不是函數的時
                                        Stop();
                                        error.Error("錯誤！檔案"+SESC.fileName+"裡，程式碼第"+SESC.LineNumbers+"行:"+SESC.Code+"中，"+e.getMessage()+"\n");
                                        if(!interactiveMode){
                                            print.Print("請按任意鍵結束...");
                                            Read(1);
                                        }
                                        stop.Stop();
                                    }else//當執行續是函數的時
                                        break;
                                }catch (final Exception e) {//接收傳送從函數拋出的錯誤
                                    if(NotIsFunction){//當執行續不是函數的時
                                        Stop();
                                        if(SESC.Main)
                                            error.Error("錯誤！檔案"+command.fileName+"裡，程式碼第"+command.get(ComLenght).LineNumbers+"行:"+command.get(ComLenght).erroeShowCommand.toString()+"中，"+e.getMessage()+"\n");
                                        else
                                            error.Error("錯誤！檔案"+SESC.fileName+"裡，程式碼第"+SESC.LineNumbers+"行:"+SESC.Code+"中，"+e.getMessage()+"\n");
                                        if(!interactiveMode){
                                            print.Print("請按任意鍵結束...");
                                            Read(1);
                                        }
                                        stop.Stop();
                                    }else{//當執行續是函數的時
                                        if(SESC.state!=SubEndStateCode.State.Exception)
                                            SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                        break;
                                    }
                                }
			}
                        /*if(isNpcall)
                            ValueArray.clear();*/
                        if(NotIsFunction)
                            CommandClear(command);
                            //此次批次結束後如果r不等於null則代表此次的批次是上一個批次(r)所呼叫，所以將上一個批次恢復運作
                        if(BackThread!=null){
                            if(RP!=null&&RP.RunState)
                                RP.Run=BackThread;
                            synchronized(BackThread){
                                BackThread.notify();
                            }
                        }else{
                            if(RP.RunState){//要RunState為真是因為當為假時一定是發生錯誤或呼叫了finally()，所以不需要再重複調用Stop()了
                                RP.RunState=false;
                                    //System.err.println("LOGE1:"+ThreadList+" "+ThreadList.size());
                                ThreadList.remove(RP);
                                    // System.err.println("LOGE2:"+ThreadList+" "+ThreadList.size());
                                if(ThreadList.isEmpty()){
                                    Stop();
                                    stop.Stop();
                                }
                            }
                        }
                        
		}
                //清除指令陣列用函數
                private void CommandClear(CommandArray command){
                    /**for(CommandArray CA:command){
                		if(CA.ComArray!=null)
                			CommandClear(CA.ComArray);
                	}//這個是下面三行的原型**/
                    command.stream().filter((CA) -> (CA.ComArray!=null)).forEach((CA) -> {
                        CommandClear(CA.ComArray);
                    });
                    command.clear();
                    command.tag.clear();
                }
                //if 或 try 或 catch 或 finally執行用函數
                /**
                 ** @param Catch 判斷是否為catch
                 * @param Exception_Value catch的例外變數
                 * @param ValueArray 變數清單
                 * @param command 指令陣列
                 * @return 執行結束狀態，[b]是break狀態，[c]是continue狀態，[t]是標籤也就是遇到了goto狀態在[t]後面會緊接著標籤所在行數，[e]是執行完結束狀態，[y]是標籤也就是遇到了例外狀態在[t]後面會緊接著throw指令後的指令運算結果
                 **/
                private void Run_Begin_or_If_or_Try_or_Catch_or_Finally(boolean Catch,String Exception_Value,Value_Array ValueArray,CommandArray command){
                    if(Catch){
                            com.setText(Exception_Value);
                        try {
                            com.command(ValueArray);
                        } catch (Exception e) {
                            SESC.setSubEndStateCode(SubEndStateCode.State.Exception, Code_String.CATCH+" "+Exception_Value,0,command.fileName, e.getMessage());
                            return;
                        }
                    }
                    If_Count IfState=new If_Count(-1);//用來存放被執行到的IF的狀態
                    try_Count Try=new try_Count();//用來存放被執行到的TRY的狀態
                    for(int ComLenght=0;ComLenght<command.size();ComLenght++){
                                if(!RP.RunState){
                                        ValueArray.clear();
                                        SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,command.fileName, Type_String.NULL);
					return;
                                }
				try{
					if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.BEGIN)){
                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
						if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2){
                                                        ValueArray.clear();
                                                        return;
                                                    }
                                                    ComLenght=Number;
                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                    ValueArray.clear();
                                                    return;
                                                }
					}else if(command.get(ComLenght).Command.toString().startsWith(Code_String.IF+" ")){
						if(new VarStrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.IF.length()+1).trim(),ValueArray,VarMode.Mode.Intermediary).Str.toString().matches(Type_String.TRUE+"|1")){
							IfState.ifc=1;
                                                        Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                        if(SESC.state==SubEndStateCode.State.Tag){
                                                                int Number=getTag(command,SESC.Message);
                                                                if(Number==-2){
                                                                    ValueArray.clear();
                                                                    return;
                                                                }
                                                                ComLenght=Number;
                                                        }else if(SESC.state!=SubEndStateCode.State.End){
                                                                ValueArray.clear();
                                                                return;
                                                        }
						}else{
							IfState.ifc=0;
						}
					}else if(command.get(ComLenght).Command.toString().startsWith(Code_String.ELSEIF+" ")){
                                            if(IfState.ifc==0)
						if(new VarStrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.ELSEIF.length()+1).trim(),ValueArray,VarMode.Mode.Intermediary).Str.toString().matches(Type_String.TRUE+"|1")){
							IfState.ifc=1;
                                                        Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                        if(SESC.state==SubEndStateCode.State.Tag){
                                                                int Number=getTag(command,SESC.Message);
                                                                if(Number==-2){
                                                                    ValueArray.clear();
                                                                    return;
                                                                }
                                                                ComLenght=Number;
                                                        }else if(SESC.state!=SubEndStateCode.State.End){
                                                                ValueArray.clear();
                                                                return;
                                                        }
						}
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ELSE)){
						if(IfState.ifc==0){//ifArray.get(ifArray.size()-1).ifc==0為在ELSE上面的IF和ELSEIF條件都不為真的話
                                                        Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                        if(SESC.state==SubEndStateCode.State.Tag){
                                                                int Number=getTag(command,SESC.Message);
                                                                if(Number==-2){
                                                                    ValueArray.clear();
                                                                    return;
                                                                }
                                                                ComLenght=Number;
                                                        }else if(SESC.state!=SubEndStateCode.State.End){
                                                                ValueArray.clear();
                                                                return;
                                                        }
                                                }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDIF))
						IfState.ifc=-1;
					else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.TRY)){
                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                if(SESC.state==SubEndStateCode.State.Break)
                                                        Try.set_Break();
                                                else if(SESC.state==SubEndStateCode.State.Continue)
                                                        Try.set_Continue();
                                                else if(SESC.state==SubEndStateCode.State.Exception)
                                                        Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,SESC.Message);
                                                else if(SESC.state==SubEndStateCode.State.Return)
                                                        Try.set_Returm(SESC.Message);
                                                else if(SESC.state==SubEndStateCode.State.Tag)
                                                        Try.set_Tag(SESC.Message);
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.CATCH+" ")){
                                                if(Try.state==try_Count.State.Exception){
                                                    Try.state=try_Count.State.None;
                                                    Run_Begin_or_If_or_Try_or_Catch_or_Finally(true,command.get(ComLenght).Command.substring(Code_String.CATCH.length()+1).trim()+"="+Try.Message
                                                            ,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                    if(SESC.state==SubEndStateCode.State.Break)
                                                            Try.set_Break();
                                                    else if(SESC.state==SubEndStateCode.State.Continue)
                                                            Try.set_Continue();
                                                    else if(SESC.state==SubEndStateCode.State.Exception)
                                                            Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,SESC.Message);
                                                    else if(SESC.state==SubEndStateCode.State.Return)
                                                            Try.set_Returm(SESC.Message);
                                                    else if(SESC.state==SubEndStateCode.State.Tag)
                                                            Try.set_Tag(SESC.Message);
                                                }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.FINALLY)){
                                                    Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                    if(SESC.state==SubEndStateCode.State.Break)
                                                            Try.set_Break();
                                                    else if(SESC.state==SubEndStateCode.State.Continue)
                                                            Try.set_Continue();
                                                    else if(SESC.state==SubEndStateCode.State.Exception)
                                                            Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,SESC.Message);
                                                    else if(SESC.state==SubEndStateCode.State.Return)
                                                            Try.set_Returm(SESC.Message);
                                                    else if(SESC.state==SubEndStateCode.State.Tag)
                                                            Try.set_Tag(SESC.Message);
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDTR)){
                                                if(null!=Try.state)
                                                    switch (Try.state) {
                                                        case Exception:
                                                            SESC.setSubEndStateCode(SubEndStateCode.State.Exception, Try.Code,Try.LineNumbers,Try.fileName, new StrDW(AShell.this,RP,Try.Message,ValueArray).Str.toString());
                                                            ValueArray.clear();
                                                            return;
                                                        case Return:
                                                            SESC.setSubEndStateCode(SubEndStateCode.State.Return, null,0,Try.fileName, (!Try.Message.equals(""))?Try.Message:Type_String.NULL);
                                                            ValueArray.clear();
                                                            return;
                                                        case Tag:
                                                            int Number=getTag(command,Try.Message);
                                                            if(Number==-2){
                                                                ValueArray.clear();
                                                                SESC.setSubEndStateCode(SubEndStateCode.State.Tag, null,0,Try.fileName, Try.Message);
                                                                return;
                                                            }
                                                            ComLenght=Number;
                                                            SESC.setSubEndStateCode(SubEndStateCode.State.Tag, null,0,Try.fileName, Type_String.NULL);
                                                            break;
                                                        case Break:
                                                            SESC.setSubEndStateCode(SubEndStateCode.State.Break, null,0,Try.fileName, null);
                                                            ValueArray.clear();
                                                            return;
                                                        case Continue:
                                                            SESC.setSubEndStateCode(SubEndStateCode.State.Continue, null,0,Try.fileName, null);
                                                            ValueArray.clear();
                                                            return;
                                                        default:
                                                            break;
                                                    }
                                                Try.state=try_Count.State.None;
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.THROW+" ")){
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, 
                                                        new StrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.THROW.length()+1).trim(),ValueArray).Str.toString());
                                                ValueArray.clear();
                                                return;
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.FOR+" ")){
                                                //System.out.println("for判斷開始:"+(System.currentTimeMillis()-sLog));
                                                ForArgsScan FAS=new ForArgsScan(command.get(ComLenght).Command.substring(Code_String.FOR.length()+1).trim());
                                                try{
                                                    RunWHILE_FOR(true,FAS.Args.get(0).toString(),FAS.Args.get(1).toString(),FAS.Args.get(2).toString(),
                                                            new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                }catch(final Exception e){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                                    ValueArray.clear();
                                                    return;
                                                }
                                                if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2){
                                                        ValueArray.clear();
                                                        return;
                                                    }
                                                    ComLenght=Number;
                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                    ValueArray.clear();
                                                    return;
                                                }
                                                //System.out.println("for判斷結束:"+(System.currentTimeMillis()-sLog));
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.WHILE+" ")){
                                                //System.out.println("While判斷開始:"+(System.currentTimeMillis()-sLog));
                                                try{
                                                    RunWHILE_FOR(false,null,command.get(ComLenght).Command.substring(Code_String.WHILE.length()+1).trim(),null,
                                                            new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                }catch(final Exception e){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                                    ValueArray.clear();
                                                    return;
                                                }
                                                if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2){
                                                        ValueArray.clear();
                                                        return;
                                                    }
                                                    ComLenght=Number;
                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                    ValueArray.clear();
                                                    return;
                                                }
                                                //System.out.println("While判斷結束:"+(System.currentTimeMillis()-sLog));
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.DWHILE)){
                                            try{
						RunDWHILE(command.get(ComLenght).Command.substring(Code_String.DWHILE.length()+1).trim(),
                                                        new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                            }catch(final Exception e){
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                                ValueArray.clear();
                                                return;
                                            }
                                            if(SESC.state==SubEndStateCode.State.Tag){
                                                int Number=getTag(command,SESC.Message);
                                                if(Number==-2){
                                                    ValueArray.clear();
                                                    return;
                                                }
                                                ComLenght=Number;
                                            }else if(SESC.state!=SubEndStateCode.State.End){
                                                ValueArray.clear();
                                                return;
                                            }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.BREAK)){
                                                ValueArray.clear();
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Break, null,0,command.fileName, null);
						return;
				    }else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.CONTINUE)){
                                                ValueArray.clear();
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Continue, null,0,command.fileName, null);
						return;
				    }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.GOTO+" ")){
                                            int Number=getTag(command,command.get(ComLenght).Command.substring(Code_String.GOTO.length()+1).trim());
                                            if(Number==-2){
                                                ValueArray.clear();
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Tag, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, command.get(ComLenght).Command.substring(Code_String.GOTO.length()+1).trim());
                                                //SESC.setIndex(Number);
                                                return;
                                            }
                                            ComLenght=Number;
				    }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.FUNCTION+" ")){
                                            FunctionNameResolve FNR=new FunctionNameResolve(command.get(ComLenght).Command.substring(Code_String.FUNCTION.length()+1).trim());
                                            Function fun=new Function(ValueArray.Reference(),command.fileName);
                                            new VarStrDW(AShell.this,RP,FNR.Name.append("=")
                                                    .append(Memory_Management.Function_Builder(fun)).toString(),ValueArray,VarMode.Mode.Var);
                                            for(StringBuilder parameter: FNR.Args){//建立函數參數
                                                String[] arg=parameter.toString().split("=",2);//將函數參數跟=(賦值運算子)符號做分離
                                                if(arg.length==1)
                                                    fun.ValueArray.add(new Value(new StringBuilder(arg[0]),new StringBuilder(Type_String.NULL)));
                                                else
                                                    fun.ValueArray.add(new Value(new StringBuilder(arg[0]),
                                                            new VarStrDW(AShell.this,RP,arg[1],ValueArray,VarMode.Mode.Intermediary).Str));
                                            }
                                            int setFun=0;
                                            if(FNR.Lanbda!=null){
                                                FNR.Lanbda.insert(0, Code_String.RETURN+" ");
                                                fun.CodeArray.add(new Command(FNR.Lanbda,command.get(ComLenght).Command,command.get(ComLenght).LineNumbers));
                                            }else
                                                while(true){
                                                    ComLenght++;
                                                    if(command.get(ComLenght).Command.toString().startsWith(Code_String.FUNCTION+" ")&&!StringScan.matchFunctionLanbda(command.get(ComLenght).Command))
                                                        setFun++;
                                                    else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDFU))
                                                        if(setFun--==0)
                                                            break;
                                                    fun.CodeArray.add(new Command(command.get(ComLenght)));
                                                }
                                            CreateSyntaxTree.CST(fun.CodeArray);
				    }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.CLASS+" ")){
                                            Class_Type Class;
                                            GetParent.ReturnParentStruct RPS;
                                            StringBuilder name=new StringBuilder();
                                            RPS=GetParent.Get_Parent(AShell.this,RP,name, command.get(ComLenght).Command.substring(Code_String.CLASS.length()+1), ValueArray);
                                            Class=new Class_Type(ValueArray);
                                            Class.isSClass();
                                            new VarStrDW(AShell.this,RP,name.append("=")
                                                    .append(Memory_Management.Class_Builder(Class)).toString(),ValueArray,VarMode.Mode.Var);
                                            //ValueListSet.Condition_ListSet(new String[]{"_inst_","_dest_","this","super"},Class.ValueArray, ValueArray);
                                            //------------------------------放入建構式與解構式-------------------------------------//AShell的建構式名稱為_inst_，也就是instance的前四個字母，解構式名稱為_dest_，也就是destructor的前四個字母
                                            /*for(CensorValueReturn FunCVR:CensorValue.CensorValues(new String[]{"_inst_","_dest_","this","super"},Class.ValueArray))
                                                if(FunCVR.Result)
                                                    Class.ValueArray.remove(FunCVR.index);
                                            Class.ValueArray.add(new Value(new StringBuilder(Type_String.INSTANCE_FUNCTION_NAME),Memory_Management.Function_Builder(new Function(Class.ValueArray))));//放入預設的空建構子
                                            Class.ValueArray.add(new Value(new StringBuilder(Type_String.DESTRUCTOR_FUNCTION_NAME),Memory_Management.Function_Builder(new Function(Class.ValueArray))));//放入預設的空建構子*/
                                            //-----------------------------------------------------------------------------------
                                            if(RPS!=null){
                                                for(int i=0;i<RPS.Parent.ValueArray.size();i++){//靜態繼承變數實做
                                                    Value V;
                                                    if((V=RPS.Parent.ValueArray.get(i)).Tent.toString().matches(Type_String.MEMORY_TYPE))//判斷原變數內容是不是記憶體參考
                                                        Memory_Management.Add_To_Arguments(V.Tent.toString());//將參考指數加一
                                                    //if(!Parent.ValueArray.get(i).Name.toString().matches(Type_String.INSTANCE_FUNCTION_NAME+"|"+Type_String.DESTRUCTOR_FUNCTION_NAME))//如果變數不是建構式或解構式
                                                         Class.ValueArray.add(V);//從父變數清單複製一份變數加入倒子變數清單，這個功能就等同於覆寫
                                                }
                                                Class.Parent=RPS.Parent;
                                                Class.ParentAddress=RPS.ParentAddress;
                                            }
                                            Class.CodeArray=new CommandArray(command.fileName);
                                            CommandArray SCA=new CommandArray(command.fileName);
                                            while(true){
                                                ComLenght++;
                                                if(command.get(ComLenght).Command.toString().startsWith(Code_String.CLASS+" ")){
                                                    int CountClass=0;
                                                    while(true){
                                                        Class.CodeArray.add(new Command(command.get(ComLenght)));
                                                        ComLenght++;
                                                        if(command.get(ComLenght).Command.toString().startsWith(Code_String.CLASS+" "))
                                                            CountClass++;
                                                        else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDCL)){
                                                            if(CountClass--==0)
                                                                break;
                                                        }
                                                    }
                                                }else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDCL))
                                                     break;
                                                else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.STATIC)){
                                                    int CountStatic=0;
                                                    while(true){
                                                        ComLenght++;
                                                        if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.STATIC))
                                                            CountStatic++;
                                                        else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDST))
                                                            if(CountStatic--==0)
                                                                break;
                                                        SCA.add(new Command(command.get(ComLenght)));
                                                    }
                                                    continue;
                                                }
                                                Class.CodeArray.add(new Command(command.get(ComLenght)));
                                            }
                                            CreateSyntaxTree.CST(Class.CodeArray);
                                            CreateSyntaxTree.CST(SCA);
                                            Run_Function(Class.ValueArray,SCA,RP);
				    }else if(StringScan.startsWith_for_return(command.get(ComLenght).Command.toString(),Code_String.RETURN)){
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Return, null,0,command.fileName, 
                                                        new VarStrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.RETURN.length()).trim(),ValueArray,VarMode.Mode.Intermediary).Str.toString());
                                                ValueArray.clear();
                                                return;
				    }else{//如果沒在執行或正在執行IF回迴圈且當下指向條件為1(無須跳過，0為須要跳過)則執行
                                                //System.out.println("呼叫一般指令("+command.get(ComLenght)+")開始:"+(System.currentTimeMillis()-sLog));
                                                    com.setText(command.get(ComLenght).Command.toString());
                                                    com.command(ValueArray);
                                                    //System.out.println("呼叫一般指令("+command.get(ComLenght)+")結束:"+(System.currentTimeMillis()-sLog));
                                    }
				}catch (final AShellException e){
                                    ValueArray.clear();
                                    SESC.setSubEndStateCode(e.SESC.state, e.SESC.Code,e.SESC.LineNumbers,e.SESC.fileName, e.SESC.Message);
                                    return;
                                }catch (final Exception e) {
                                    ValueArray.clear();
                                    SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                    return;
                                }
			}
                    ValueArray.clear();
                    SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,command.fileName,Type_String.NULL);
                }
                //while以及for執行用函數
                /**
                 * @param is_for 判斷是否為for迴圈
                 * @param Start for的起始值
                 * @param Boolean 迴圈的條件式
                 * @param add for的更新值
                 * @param ValueArray 變數清單
                 * @param command 指令陣列
                 * @return 執行結束狀態，[t]是標籤也就是遇到了goto狀態在[t]後面會緊接著標籤所在行數，[e]是執行完結束狀態，[y]是標籤也就是遇到了例外狀態在[t]後面會緊接著throw指令後的指令運算結果
                 **/
                private void RunWHILE_FOR(boolean is_for,String Start,String Boolean,String add,Value_Array ValueArray,CommandArray command) throws java.lang.Exception{
                    Value_Array JudgmentAreaValueArray=ValueArray;//建立判斷區域的變數清單
                    if(is_for&&!Start.equals("")){
                        com.setText(Start);
                        com.command(JudgmentAreaValueArray);
                    }
                    If_Count IfState=new If_Count(-1);//用來存放被執行到的IF的狀態
                    try_Count Try=new try_Count();//用來存放被執行到的TRY的狀態
                    while(new VarStrDW(AShell.this,RP,Boolean,JudgmentAreaValueArray,VarMode.Mode.Intermediary).Str.toString().matches(Type_String.TRUE+"|1")){
                        ValueArray=new Value_Array(JudgmentAreaValueArray);
                        for(int ComLenght=0;ComLenght<command.size();ComLenght++){
				if(!RP.RunState){
                                        ValueArray.clear();
                                        JudgmentAreaValueArray.clear();
                                        SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,command.fileName,Type_String.NULL);
					return;
                                }
				try{
					if(command.get(ComLenght).Command.toString().startsWith(":"))
						continue;
					if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.BEGIN)){
                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
						if(SESC.state==SubEndStateCode.State.Break){
                                                        ValueArray.clear();
                                                        JudgmentAreaValueArray.clear();
                                                        SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,SESC.fileName,Type_String.NULL);
                                                        return;
                                                }else if(SESC.state==SubEndStateCode.State.Continue)
                                                        break;
                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2){
                                                        ValueArray.clear();
                                                        JudgmentAreaValueArray.clear();
                                                        return;
                                                    }
                                                    ComLenght=Number;
                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                        ValueArray.clear();
                                                        JudgmentAreaValueArray.clear();
                                                        return;
                                                }
					}else if(command.get(ComLenght).Command.toString().startsWith(Code_String.IF+" ")){
						if(new VarStrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.IF.length()+1).trim(),ValueArray,VarMode.Mode.Intermediary).Str.toString().matches(Type_String.TRUE+"|1")){
								IfState.ifc=1;
                                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                                if(SESC.state==SubEndStateCode.State.Break){
                                                                    ValueArray.clear();
                                                                    JudgmentAreaValueArray.clear();
                                                                    SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,SESC.fileName,Type_String.NULL);
                                                                    return;
                                                                }else if(SESC.state==SubEndStateCode.State.Continue)
                                                                    break;
                                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                                    int Number=getTag(command,SESC.Message);
                                                                    if(Number==-2){
                                                                        ValueArray.clear();
                                                                        JudgmentAreaValueArray.clear();
                                                                        return;
                                                                    }
                                                                    ComLenght=Number;
                                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                                    ValueArray.clear();
                                                                    JudgmentAreaValueArray.clear();
                                                                    return;
                                                                }
							}else{
								IfState.ifc=0;
							}
					}else if(command.get(ComLenght).Command.toString().startsWith(Code_String.ELSEIF+" ")){
                                            if(IfState.ifc==0)
						if(new VarStrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.ELSEIF.length()+1).trim(),ValueArray,VarMode.Mode.Intermediary).Str.toString().matches(Type_String.TRUE+"|1")){
								IfState.ifc=1;
                                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                                if(SESC.state==SubEndStateCode.State.Break){
                                                                    ValueArray.clear();
                                                                    JudgmentAreaValueArray.clear();
                                                                    SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,SESC.fileName,Type_String.NULL);
                                                                    return;
                                                                }else if(SESC.state==SubEndStateCode.State.Continue)
                                                                    break;
                                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                                    int Number=getTag(command,SESC.Message);
                                                                    if(Number==-2){
                                                                        ValueArray.clear();
                                                                        JudgmentAreaValueArray.clear();
                                                                        return;
                                                                    }
                                                                    ComLenght=Number;
                                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                                    ValueArray.clear();
                                                                    JudgmentAreaValueArray.clear();
                                                                    return;
                                                                }
							}
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ELSE)){
						if(IfState.ifc==0){//ifArray.get(ifArray.size()-1).ifc==0為在ELSE上面的IF和ELSEIF條件都不為真的話
                                                        Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                                if(SESC.state==SubEndStateCode.State.Break){
                                                                    ValueArray.clear();
                                                                    JudgmentAreaValueArray.clear();
                                                                    SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,SESC.fileName,Type_String.NULL);
                                                                    return;
                                                                }else if(SESC.state==SubEndStateCode.State.Continue)
                                                                    break;
                                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                                    int Number=getTag(command,SESC.Message);
                                                                    if(Number==-2){
                                                                        ValueArray.clear();
                                                                        JudgmentAreaValueArray.clear();
                                                                        return;
                                                                    }
                                                                    ComLenght=Number;
                                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                                    ValueArray.clear();
                                                                    JudgmentAreaValueArray.clear();
                                                                    return;
                                                                }
                                                }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDIF)){
						IfState.ifc=-1;
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.TRY)){
                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                if(SESC.state==SubEndStateCode.State.Break)
                                                        Try.set_Break();
                                                else if(SESC.state==SubEndStateCode.State.Continue)
                                                        Try.set_Continue();
                                                else if(SESC.state==SubEndStateCode.State.Exception)
                                                        Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,SESC.Message);
                                                else if(SESC.state==SubEndStateCode.State.Return)
                                                        Try.set_Returm(SESC.Message);
                                                else if(SESC.state==SubEndStateCode.State.Tag)
                                                        Try.set_Tag(SESC.Message);
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.CATCH+" ")){
                                                if(Try.state==try_Count.State.Exception){
                                                    Try.state=try_Count.State.None;
                                                    Run_Begin_or_If_or_Try_or_Catch_or_Finally(true,command.get(ComLenght).Command.substring(Code_String.CATCH.length()+1).trim()+"="+Try.Message
                                                            ,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                    if(SESC.state==SubEndStateCode.State.Break)
                                                            Try.set_Break();
                                                    else if(SESC.state==SubEndStateCode.State.Continue)
                                                            Try.set_Continue();
                                                    else if(SESC.state==SubEndStateCode.State.Exception)
                                                            Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,SESC.Message);
                                                    else if(SESC.state==SubEndStateCode.State.Return)
                                                            Try.set_Returm(SESC.Message);
                                                    else if(SESC.state==SubEndStateCode.State.Tag)
                                                            Try.set_Tag(SESC.Message);
                                                }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.FINALLY)){
                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                    if(SESC.state==SubEndStateCode.State.Break)
                                                            Try.set_Break();
                                                    else if(SESC.state==SubEndStateCode.State.Continue)
                                                            Try.set_Continue();
                                                    else if(SESC.state==SubEndStateCode.State.Exception)
                                                            Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,SESC.Message);
                                                    else if(SESC.state==SubEndStateCode.State.Return)
                                                            Try.set_Returm(SESC.Message);
                                                    else if(SESC.state==SubEndStateCode.State.Tag)
                                                            Try.set_Tag(SESC.Message);
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDTR)){
                                                if(Try.state==try_Count.State.Exception){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Exception, Try.Code,Try.LineNumbers,Try.fileName, new StrDW(AShell.this,RP,Try.Message,ValueArray).Str.toString());
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }else if(Try.state==try_Count.State.Return){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Return, null,0,Try.fileName, (!Try.Message.equals(""))?Try.Message:Type_String.NULL);
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }else if(Try.state==try_Count.State.Tag){
                                                    int Number=getTag(command,Try.Message);
                                                    if(Number==-2){
                                                        ValueArray.clear();
                                                        JudgmentAreaValueArray.clear();
                                                        SESC.setSubEndStateCode(SubEndStateCode.State.Tag, null,0,Try.fileName, Try.Message);
                                                        return;
                                                    }
                                                    ComLenght=Number;
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Tag, null,0,Try.fileName, Type_String.NULL);
                                                }else if(Try.state==try_Count.State.Break){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,Try.fileName,Type_String.NULL); 
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }else if(Try.state==try_Count.State.Continue)
                                                    break;
                                                Try.state=try_Count.State.None;
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.THROW+" ")){
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, 
                                                        new StrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.THROW.length()+1).trim(),ValueArray).Str.toString());
                                                ValueArray.clear();
                                                JudgmentAreaValueArray.clear();
                                                return;
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.FOR+" ")){
                                                //System.out.println("for判斷開始:"+(System.currentTimeMillis()-sLog));
                                                ForArgsScan FAS=new ForArgsScan(command.get(ComLenght).Command.substring(Code_String.FOR.length()+1).trim());
                                                try{
                                                    RunWHILE_FOR(true,FAS.Args.get(0).toString(),FAS.Args.get(1).toString(),FAS.Args.get(2).toString(),
                                                            new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                }catch(final Exception e){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }
                                                if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2){
                                                        ValueArray.clear();
                                                        JudgmentAreaValueArray.clear();
                                                        return;
                                                    }
                                                    ComLenght=Number;
                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }
                                                //System.out.println("for判斷結束:"+(System.currentTimeMillis()-sLog));
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.WHILE+" ")){
                                                //System.out.println("While判斷開始:"+(System.currentTimeMillis()-sLog));
                                                try{
                                                    RunWHILE_FOR(false,null,command.get(ComLenght).Command.substring(Code_String.WHILE.length()+1).trim(),null,
                                                            new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                }catch(final Exception e){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }
                                                if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2){
                                                        ValueArray.clear();
                                                        JudgmentAreaValueArray.clear();
                                                        return;
                                                    }
                                                    ComLenght=Number;
                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }
                                                //System.out.println("While判斷結束:"+(System.currentTimeMillis()-sLog));
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.DWHILE+" ")){
                                            try{
						RunDWHILE(command.get(ComLenght).Command.substring(Code_String.DWHILE.length()+1).trim(),
                                                        new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                            }catch(final Exception e){
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                                ValueArray.clear();
                                                JudgmentAreaValueArray.clear();
                                                return;
                                            }
                                            if(SESC.state==SubEndStateCode.State.Tag){
                                                int Number=getTag(command,SESC.Message);
                                                if(Number==-2){
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }
                                                ComLenght=Number;
                                            }else if(SESC.state!=SubEndStateCode.State.End){
                                                ValueArray.clear();
                                                JudgmentAreaValueArray.clear();
                                                return;
                                            }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.BREAK)){
                                                ValueArray.clear();
                                                JudgmentAreaValueArray.clear();
                                                SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,command.fileName, Type_String.NULL);
						return;
				    }else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.CONTINUE))
						break;
				    else if(command.get(ComLenght).Command.toString().startsWith(Code_String.GOTO+" ")){
                                            int Number=getTag(command,command.get(ComLenght).Command.substring(Code_String.GOTO.length()+1).trim());
                                            if(Number==-2){
                                                ValueArray.clear();
                                                JudgmentAreaValueArray.clear();
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Tag, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, command.get(ComLenght).Command.substring(Code_String.GOTO.length()+1).trim());
                                                //SESC.setIndex(Number);
                                                return;
                                            }
                                            ComLenght=Number;
				    }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.FUNCTION+" ")){
                                            FunctionNameResolve FNR=new FunctionNameResolve(command.get(ComLenght).Command.substring(Code_String.FUNCTION.length()+1).trim());
                                            Function fun=new Function(ValueArray.Reference(),command.fileName);
                                            new VarStrDW(AShell.this,RP,FNR.Name.append("=")
                                                    .append(Memory_Management.Function_Builder(fun)).toString(),ValueArray,VarMode.Mode.Var);
                                            for(StringBuilder parameter: FNR.Args){//建立函數參數
                                                String[] arg=parameter.toString().split("=",2);//將函數參數跟=(賦值運算子)符號做分離
                                                if(arg.length==1)
                                                    fun.ValueArray.add(new Value(new StringBuilder(arg[0]),new StringBuilder(Type_String.NULL)));
                                                else
                                                    fun.ValueArray.add(new Value(new StringBuilder(arg[0]),
                                                            new VarStrDW(AShell.this,RP,arg[1],ValueArray,VarMode.Mode.Intermediary).Str));
                                            }
                                            int setFun=0;
                                            if(FNR.Lanbda!=null){
                                                FNR.Lanbda.insert(0, Code_String.RETURN+" ");
                                                fun.CodeArray.add(new Command(FNR.Lanbda,command.get(ComLenght).Command,command.get(ComLenght).LineNumbers));
                                            }else
                                                while(true){
                                                    ComLenght++;
                                                    if(command.get(ComLenght).Command.toString().startsWith(Code_String.FUNCTION+" ")&&!StringScan.matchFunctionLanbda(command.get(ComLenght).Command))
                                                        setFun++;
                                                    else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDFU))
                                                        if(setFun--==0)
                                                            break;
                                                    fun.CodeArray.add(new Command(command.get(ComLenght)));
                                                }
                                            CreateSyntaxTree.CST(fun.CodeArray);
				    }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.CLASS+" ")){
                                            Class_Type Class;
                                            GetParent.ReturnParentStruct RPS;
                                            StringBuilder name=new StringBuilder();
                                            RPS=GetParent.Get_Parent(AShell.this,RP,name, command.get(ComLenght).Command.substring(Code_String.CLASS.length()+1), ValueArray);
                                            Class=new Class_Type(ValueArray);
                                            Class.isSClass();
                                            new VarStrDW(AShell.this,RP,name.append("=")
                                                    .append(Memory_Management.Class_Builder(Class)).toString(),ValueArray,VarMode.Mode.Var);
                                            //ValueListSet.Condition_ListSet(new String[]{"_inst_","_dest_","this","super"},Class.ValueArray, ValueArray);
                                            //------------------------------放入建構式與解構式-------------------------------------//AShell的建構式名稱為_inst_，也就是instance的前四個字母，解構式名稱為_dest_，也就是destructor的前四個字母
                                            /*for(CensorValueReturn FunCVR:CensorValue.CensorValues(new String[]{"_inst_","_dest_","this","super"},Class.ValueArray))
                                                if(FunCVR.Result)
                                                    Class.ValueArray.remove(FunCVR.index);
                                            Class.ValueArray.add(new Value(new StringBuilder(Type_String.INSTANCE_FUNCTION_NAME),Memory_Management.Function_Builder(new Function(Class.ValueArray))));//放入預設的空建構子
                                            Class.ValueArray.add(new Value(new StringBuilder(Type_String.DESTRUCTOR_FUNCTION_NAME),Memory_Management.Function_Builder(new Function(Class.ValueArray))));//放入預設的空建構子*/
                                            //-----------------------------------------------------------------------------------
                                            if(RPS!=null){
                                                for(int i=0;i<RPS.Parent.ValueArray.size();i++){//靜態繼承變數實做
                                                    Value V;
                                                    if((V=RPS.Parent.ValueArray.get(i)).Tent.toString().matches(Type_String.MEMORY_TYPE))//判斷原變數內容是不是記憶體參考
                                                        Memory_Management.Add_To_Arguments(V.Tent.toString());//將參考指數加一
                                                    //if(!Parent.ValueArray.get(i).Name.toString().matches(Type_String.INSTANCE_FUNCTION_NAME+"|"+Type_String.DESTRUCTOR_FUNCTION_NAME))//如果變數不是建構式或解構式
                                                         Class.ValueArray.add(V);//從父變數清單複製一份變數加入倒子變數清單，這個功能就等同於覆寫
                                                }
                                                Class.Parent=RPS.Parent;
                                                Class.ParentAddress=RPS.ParentAddress;
                                            }
                                            Class.CodeArray=new CommandArray(command.fileName);
                                            CommandArray SCA=new CommandArray(command.fileName);
                                            while(true){
                                                ComLenght++;
                                                if(command.get(ComLenght).Command.toString().startsWith(Code_String.CLASS+" ")){
                                                    int CountClass=0;
                                                    while(true){
                                                        Class.CodeArray.add(new Command(command.get(ComLenght)));
                                                        ComLenght++;
                                                        if(command.get(ComLenght).Command.toString().startsWith(Code_String.CLASS+" "))
                                                            CountClass++;
                                                        else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDCL)){
                                                            if(CountClass--==0)
                                                                break;
                                                        }
                                                    }
                                                }else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDCL))
                                                     break;
                                                else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.STATIC)){
                                                    int CountStatic=0;
                                                    while(true){
                                                        ComLenght++;
                                                        if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.STATIC))
                                                            CountStatic++;
                                                        else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDST))
                                                            if(CountStatic--==0)
                                                                break;
                                                        SCA.add(new Command(command.get(ComLenght)));
                                                    }
                                                    continue;
                                                }
                                                Class.CodeArray.add(new Command(command.get(ComLenght)));
                                            }
                                            CreateSyntaxTree.CST(Class.CodeArray);
                                            CreateSyntaxTree.CST(SCA);
                                            Run_Function(Class.ValueArray,SCA,RP);
				    }else if(StringScan.startsWith_for_return(command.get(ComLenght).Command.toString(),Code_String.RETURN)){
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Return, null,0,command.fileName, 
                                                        new VarStrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.RETURN.length()).trim(),ValueArray,VarMode.Mode.Intermediary).Str.toString());
                                                ValueArray.clear();
                                                JudgmentAreaValueArray.clear();
                                                return;
				    }else{//如果沒在執行或正在執行IF回迴圈且當下指向條件為1(無須跳過，0為須要跳過)則執行
                                                //System.out.println("呼叫一般指令("+command.get(ComLenght)+")開始:"+(System.currentTimeMillis()-sLog));
                                                    com.setText(command.get(ComLenght).Command.toString());
                                                    com.command(ValueArray);
                                                    //System.out.println("呼叫一般指令("+command.get(ComLenght)+")結束:"+(System.currentTimeMillis()-sLog));
                                    }
				}catch (final AShellException e){
                                    ValueArray.clear();
                                    JudgmentAreaValueArray.clear();
                                    SESC.setSubEndStateCode(e.SESC.state, e.SESC.Code,e.SESC.LineNumbers,e.SESC.fileName, e.SESC.Message);
                                    return;
                                }catch (final Exception e) {
                                    ValueArray.clear();
                                    JudgmentAreaValueArray.clear();
                                    SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                    return;
                                }
			}
                        if(is_for&&!add.equals("")){
                            com.setText(add);
                            com.command(JudgmentAreaValueArray);
                        }
                        ValueArray.clear();
                    }
                    JudgmentAreaValueArray.clear();
                    SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,command.fileName, Type_String.NULL);
                }
                //dwhile執行用函數
                /**
                 * @param Boolean 迴圈的條件式
                 * @param ValueArray 變數清單
                 * @param command 指令陣列
                 * @return 執行結束狀態，[t]是標籤也就是遇到了goto狀態在[t]後面會緊接著標籤所在行數，[e]是執行完結束狀態，[y]是標籤也就是遇到了例外狀態在[t]後面會緊接著throw指令後的指令運算結果
                 **/
                private void RunDWHILE(String Boolean,Value_Array ValueArray,CommandArray command) throws java.lang.Exception{
                    Value_Array JudgmentAreaValueArray=ValueArray;//建立判斷區域的變數清單
                    If_Count IfState=new If_Count(-1);//用來存放被執行到的IF的狀態
                    try_Count Try=new try_Count();//用來存放被執行到的TRY的狀態
                    do{
                        ValueArray=new Value_Array(JudgmentAreaValueArray);
                        for(int ComLenght=0;ComLenght<command.size();ComLenght++){
				if(!RP.RunState){
                                        ValueArray.clear();
                                        JudgmentAreaValueArray.clear();
                                        SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,command.fileName,Type_String.NULL);
					return;
                                }
				try{
					if(command.get(ComLenght).Command.toString().startsWith(":"))
						continue;
					if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.BEGIN)){
                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
						if(SESC.state==SubEndStateCode.State.Break){
                                                        ValueArray.clear();
                                                        JudgmentAreaValueArray.clear();
                                                        SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,SESC.fileName,Type_String.NULL);
                                                        return;
                                                }else if(SESC.state==SubEndStateCode.State.Continue)
                                                        break;
                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2){
                                                        ValueArray.clear();
                                                        JudgmentAreaValueArray.clear();
                                                        return;
                                                    }
                                                    ComLenght=Number;
                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                        ValueArray.clear();
                                                        JudgmentAreaValueArray.clear();
                                                        return;
                                                }
					}else if(command.get(ComLenght).Command.toString().startsWith(Code_String.IF+" ")){
						if(new VarStrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.IF.length()+1).trim(),ValueArray,VarMode.Mode.Intermediary).Str.toString().matches(Type_String.TRUE+"|1")){
								IfState.ifc=1;
                                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                                if(SESC.state==SubEndStateCode.State.Break){
                                                                    ValueArray.clear();
                                                                    JudgmentAreaValueArray.clear();
                                                                    SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,SESC.fileName,Type_String.NULL);
                                                                    return;
                                                                }else if(SESC.state==SubEndStateCode.State.Continue)
                                                                    break;
                                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                                    int Number=getTag(command,SESC.Message);
                                                                    if(Number==-2){
                                                                        ValueArray.clear();
                                                                        JudgmentAreaValueArray.clear();
                                                                        return;
                                                                    }
                                                                    ComLenght=Number;
                                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                                    ValueArray.clear();
                                                                    JudgmentAreaValueArray.clear();
                                                                    return;
                                                                }
							}else{
								IfState.ifc=0;
							}
					}else if(command.get(ComLenght).Command.toString().startsWith(Code_String.ELSEIF+" ")){
                                            if(IfState.ifc==0)
						if(new VarStrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.ELSEIF.length()+1).trim(),ValueArray,VarMode.Mode.Intermediary).Str.toString().matches(Type_String.TRUE+"|1")){
								IfState.ifc=1;
                                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                                if(SESC.state==SubEndStateCode.State.Break){
                                                                    ValueArray.clear();
                                                                    JudgmentAreaValueArray.clear();
                                                                    SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,SESC.fileName,Type_String.NULL);
                                                                    return;
                                                                }else if(SESC.state==SubEndStateCode.State.Continue)
                                                                    break;
                                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                                    int Number=getTag(command,SESC.Message);
                                                                    if(Number==-2){
                                                                        ValueArray.clear();
                                                                        JudgmentAreaValueArray.clear();
                                                                        return;
                                                                    }
                                                                    ComLenght=Number;
                                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                                    ValueArray.clear();
                                                                    JudgmentAreaValueArray.clear();
                                                                    return;
                                                                }
							}
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ELSE)){
						if(IfState.ifc==0){//ifArray.get(ifArray.size()-1).ifc==0為在ELSE上面的IF和ELSEIF條件都不為真的話
                                                        Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                                if(SESC.state==SubEndStateCode.State.Break){
                                                                    ValueArray.clear();
                                                                    JudgmentAreaValueArray.clear();
                                                                    SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,SESC.fileName,Type_String.NULL);
                                                                    return;
                                                                }else if(SESC.state==SubEndStateCode.State.Continue)
                                                                    break;
                                                                else if(SESC.state==SubEndStateCode.State.Tag){
                                                                    int Number=getTag(command,SESC.Message);
                                                                    if(Number==-2){
                                                                        ValueArray.clear();
                                                                        JudgmentAreaValueArray.clear();
                                                                        return;
                                                                    }
                                                                    ComLenght=Number;
                                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                                    ValueArray.clear();
                                                                    JudgmentAreaValueArray.clear();
                                                                    return;
                                                                }
                                                }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDIF)){
						IfState.ifc=-1;
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.TRY)){
                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                if(null!=SESC.state)
                                                        switch (SESC.state) {
                                                            case Break:
                                                                Try.set_Break();
                                                                break;
                                                            case Continue:
                                                                Try.set_Continue();
                                                                break;
                                                            case Exception:
                                                                Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,SESC.Message);
                                                                break;
                                                            case Return:
                                                                Try.set_Returm(SESC.Message);
                                                                break;
                                                            case Tag:
                                                                Try.set_Tag(SESC.Message);
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.CATCH+" ")){
                                                if(Try.state==try_Count.State.Exception){
                                                    Try.state=try_Count.State.None;
                                                    Run_Begin_or_If_or_Try_or_Catch_or_Finally(true,command.get(ComLenght).Command.substring(Code_String.CATCH.length()+1).trim()+"="+Try.Message
                                                            ,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                    if(null!=SESC.state)
                                                            switch (SESC.state) {
                                                                case Break:
                                                                    Try.set_Break();
                                                                    break;
                                                                case Continue:
                                                                    Try.set_Continue();
                                                                    break;
                                                                case Exception:
                                                                    Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,SESC.Message);
                                                                    break;
                                                                case Return:
                                                                    Try.set_Returm(SESC.Message);
                                                                    break;
                                                                case Tag:
                                                                    Try.set_Tag(SESC.Message);
                                                                    break;
                                                                default:
                                                                    break;
                                                            }
                                                        }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.FINALLY)){
                                                Run_Begin_or_If_or_Try_or_Catch_or_Finally(false,null,new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                    if(null!=SESC.state)
                                                            switch (SESC.state) {
                                                                case Break:
                                                                    Try.set_Break();
                                                                    break;
                                                                case Continue:
                                                                    Try.set_Continue();
                                                                    break;
                                                                case Exception:
                                                                    Try.set_Try(SESC.Code,SESC.LineNumbers,SESC.fileName,SESC.Message);
                                                                    break;
                                                                case Return:
                                                                    Try.set_Returm(SESC.Message);
                                                                    break;
                                                                case Tag:
                                                                    Try.set_Tag(SESC.Message);
                                                                    break;
                                                                default:
                                                                    break;
                                                            }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDTR)){
                                                if(Try.state==try_Count.State.Exception){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Exception, Try.Code,Try.LineNumbers,Try.fileName, new StrDW(AShell.this,RP,Try.Message,ValueArray).Str.toString());
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }else if(Try.state==try_Count.State.Return){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Return, null,0,Try.fileName, (!Try.Message.equals(""))?Try.Message:Type_String.NULL);
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }else if(Try.state==try_Count.State.Tag){
                                                    int Number=getTag(command,Try.Message);
                                                    if(Number==-2){
                                                        ValueArray.clear();
                                                        JudgmentAreaValueArray.clear();
                                                        SESC.setSubEndStateCode(SubEndStateCode.State.Tag, null,0,Try.fileName, Try.Message);
                                                        return;
                                                    }
                                                    ComLenght=Number;
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Tag, null,0,Try.fileName, Type_String.NULL);
                                                }else if(Try.state==try_Count.State.Break){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,Try.fileName,Type_String.NULL); 
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }else if(Try.state==try_Count.State.Continue)
                                                    break;
                                                Try.state=try_Count.State.None;
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.THROW+" ")){
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, 
                                                        new StrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.THROW.length()+1).trim(),ValueArray).Str.toString());
                                                ValueArray.clear();
                                                JudgmentAreaValueArray.clear();
                                                return;
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.FOR+" ")){
                                                //System.out.println("for判斷開始:"+(System.currentTimeMillis()-sLog));
                                                ForArgsScan FAS=new ForArgsScan(command.get(ComLenght).Command.substring(Code_String.FOR.length()+1).trim());
                                                try{
                                                    RunWHILE_FOR(true,FAS.Args.get(0).toString(),FAS.Args.get(1).toString(),FAS.Args.get(2).toString(),
                                                            new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                }catch(final Exception e){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }
                                                if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2){
                                                        ValueArray.clear();
                                                        JudgmentAreaValueArray.clear();
                                                        return;
                                                    }
                                                    ComLenght=Number;
                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }
                                                //System.out.println("for判斷結束:"+(System.currentTimeMillis()-sLog));
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.WHILE+" ")){
                                                //System.out.println("While判斷開始:"+(System.currentTimeMillis()-sLog));
                                                try{
                                                    RunWHILE_FOR(false,null,command.get(ComLenght).Command.substring(Code_String.WHILE.length()+1).trim(),null,
                                                            new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                                }catch(final Exception e){
                                                    SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }
                                                if(SESC.state==SubEndStateCode.State.Tag){
                                                    int Number=getTag(command,SESC.Message);
                                                    if(Number==-2){
                                                        ValueArray.clear();
                                                        JudgmentAreaValueArray.clear();
                                                        return;
                                                    }
                                                    ComLenght=Number;
                                                }else if(SESC.state!=SubEndStateCode.State.End){
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }
                                                //System.out.println("While判斷結束:"+(System.currentTimeMillis()-sLog));
                                        }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.DWHILE+" ")){
                                            try{
						RunDWHILE(command.get(ComLenght).Command.substring(Code_String.DWHILE.length()+1).trim(),
                                                        new Value_Array(ValueArray),command.get(ComLenght).ComArray);
                                            }catch(final Exception e){
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                                ValueArray.clear();
                                                JudgmentAreaValueArray.clear();
                                                return;
                                            }
                                            if(SESC.state==SubEndStateCode.State.Tag){
                                                int Number=getTag(command,SESC.Message);
                                                if(Number==-2){
                                                    ValueArray.clear();
                                                    JudgmentAreaValueArray.clear();
                                                    return;
                                                }
                                                ComLenght=Number;
                                            }else if(SESC.state!=SubEndStateCode.State.End){
                                                ValueArray.clear();
                                                JudgmentAreaValueArray.clear();
                                                return;
                                            }
					}else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.BREAK)){
                                                ValueArray.clear();
                                                JudgmentAreaValueArray.clear();
                                                SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,command.fileName, Type_String.NULL);
						return;
				    }else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.CONTINUE))
						break;
				    else if(command.get(ComLenght).Command.toString().startsWith(Code_String.GOTO+" ")){
                                            int Number=getTag(command,command.get(ComLenght).Command.substring(Code_String.GOTO.length()+1).trim());
                                            if(Number==-2){
                                                ValueArray.clear();
                                                JudgmentAreaValueArray.clear();
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Tag, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, command.get(ComLenght).Command.substring(Code_String.GOTO.length()+1).trim());
                                                //SESC.setIndex(Number);
                                                return;
                                            }
                                            ComLenght=Number;
				    }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.FUNCTION+" ")){
                                            FunctionNameResolve FNR=new FunctionNameResolve(command.get(ComLenght).Command.substring(Code_String.FUNCTION.length()+1).trim());
                                            Function fun=new Function(ValueArray.Reference(),command.fileName);
                                            new VarStrDW(AShell.this,RP,FNR.Name.append("=")
                                                    .append(Memory_Management.Function_Builder(fun)).toString(),ValueArray,VarMode.Mode.Var);
                                            for(StringBuilder parameter: FNR.Args){//建立函數參數
                                                String[] arg=parameter.toString().split("=",2);//將函數參數跟=(賦值運算子)符號做分離
                                                if(arg.length==1)
                                                    fun.ValueArray.add(new Value(new StringBuilder(arg[0]),new StringBuilder(Type_String.NULL)));
                                                else
                                                    fun.ValueArray.add(new Value(new StringBuilder(arg[0]),
                                                            new VarStrDW(AShell.this,RP,arg[1],ValueArray,VarMode.Mode.Intermediary).Str));
                                            }
                                            int setFun=0;
                                            if(FNR.Lanbda!=null){
                                                FNR.Lanbda.insert(0, Code_String.RETURN+" ");
                                                fun.CodeArray.add(new Command(FNR.Lanbda,command.get(ComLenght).Command,command.get(ComLenght).LineNumbers));
                                            }else
                                                while(true){
                                                    ComLenght++;
                                                    if(command.get(ComLenght).Command.toString().startsWith(Code_String.FUNCTION+" ")&&!StringScan.matchFunctionLanbda(command.get(ComLenght).Command))
                                                        setFun++;
                                                    else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDFU))
                                                        if(setFun--==0)
                                                            break;
                                                    fun.CodeArray.add(new Command(command.get(ComLenght)));
                                                }
                                            CreateSyntaxTree.CST(fun.CodeArray);
				    }else if(command.get(ComLenght).Command.toString().startsWith(Code_String.CLASS+" ")){
                                            Class_Type Class;
                                            GetParent.ReturnParentStruct RPS;
                                            StringBuilder name=new StringBuilder();
                                            RPS=GetParent.Get_Parent(AShell.this,RP,name, command.get(ComLenght).Command.substring(Code_String.CLASS.length()+1), ValueArray);
                                            Class=new Class_Type(ValueArray);
                                            Class.isSClass();
                                            new VarStrDW(AShell.this,RP,name.append("=")
                                                    .append(Memory_Management.Class_Builder(Class)).toString(),ValueArray,VarMode.Mode.Var);
                                            //ValueListSet.Condition_ListSet(new String[]{"_inst_","_dest_","this","super"},Class.ValueArray, ValueArray);
                                            //------------------------------放入建構式與解構式-------------------------------------//AShell的建構式名稱為_inst_，也就是instance的前四個字母，解構式名稱為_dest_，也就是destructor的前四個字母
                                            /*for(CensorValueReturn FunCVR:CensorValue.CensorValues(new String[]{"_inst_","_dest_","this","super"},Class.ValueArray))
                                                if(FunCVR.Result)
                                                    Class.ValueArray.remove(FunCVR.index);
                                            Class.ValueArray.add(new Value(new StringBuilder(Type_String.INSTANCE_FUNCTION_NAME),Memory_Management.Function_Builder(new Function(Class.ValueArray))));//放入預設的空建構子
                                            Class.ValueArray.add(new Value(new StringBuilder(Type_String.DESTRUCTOR_FUNCTION_NAME),Memory_Management.Function_Builder(new Function(Class.ValueArray))));//放入預設的空建構子*/
                                            //-----------------------------------------------------------------------------------
                                            if(RPS!=null){
                                                for(int i=0;i<RPS.Parent.ValueArray.size();i++){//靜態繼承變數實做
                                                    Value V;
                                                    if((V=RPS.Parent.ValueArray.get(i)).Tent.toString().matches(Type_String.MEMORY_TYPE))//判斷原變數內容是不是記憶體參考
                                                        Memory_Management.Add_To_Arguments(V.Tent.toString());//將參考指數加一
                                                    //if(!Parent.ValueArray.get(i).Name.toString().matches(Type_String.INSTANCE_FUNCTION_NAME+"|"+Type_String.DESTRUCTOR_FUNCTION_NAME))//如果變數不是建構式或解構式
                                                         Class.ValueArray.add(V);//從父變數清單複製一份變數加入倒子變數清單，這個功能就等同於覆寫
                                                }
                                                Class.Parent=RPS.Parent;
                                                Class.ParentAddress=RPS.ParentAddress;
                                            }
                                            Class.CodeArray=new CommandArray(command.fileName);
                                            CommandArray SCA=new CommandArray(command.fileName);
                                            while(true){
                                                ComLenght++;
                                                if(command.get(ComLenght).Command.toString().startsWith(Code_String.CLASS+" ")){
                                                    int CountClass=0;
                                                    while(true){
                                                        Class.CodeArray.add(new Command(command.get(ComLenght)));
                                                        ComLenght++;
                                                        if(command.get(ComLenght).Command.toString().startsWith(Code_String.CLASS+" "))
                                                            CountClass++;
                                                        else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDCL)){
                                                            if(CountClass--==0)
                                                                break;
                                                        }
                                                    }
                                                }else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDCL))
                                                     break;
                                                else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.STATIC)){
                                                    int CountStatic=0;
                                                    while(true){
                                                        ComLenght++;
                                                        if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.STATIC))
                                                            CountStatic++;
                                                        else if(StringScan.startsWith(command.get(ComLenght).Command.toString(),Code_String.ENDST))
                                                            if(CountStatic--==0)
                                                                break;
                                                        SCA.add(new Command(command.get(ComLenght)));
                                                    }
                                                    continue;
                                                }
                                                Class.CodeArray.add(new Command(command.get(ComLenght)));
                                            }
                                            CreateSyntaxTree.CST(Class.CodeArray);
                                            CreateSyntaxTree.CST(SCA);
                                            Run_Function(Class.ValueArray,SCA,RP);
				    }else if(StringScan.startsWith_for_return(command.get(ComLenght).Command.toString(),Code_String.RETURN)){
                                                SESC.setSubEndStateCode(SubEndStateCode.State.Return, null,0,command.fileName, 
                                                        new VarStrDW(AShell.this,RP,command.get(ComLenght).Command.substring(Code_String.RETURN.length()).trim(),ValueArray,VarMode.Mode.Intermediary).Str.toString());
                                                ValueArray.clear();
                                                JudgmentAreaValueArray.clear();
                                                return;
				    }else{//如果沒在執行或正在執行IF回迴圈且當下指向條件為1(無須跳過，0為須要跳過)則執行
                                                //System.out.println("呼叫一般指令("+command.get(ComLenght)+")開始:"+(System.currentTimeMillis()-sLog));
                                                    com.setText(command.get(ComLenght).Command.toString());
                                                    com.command(ValueArray);
                                                    //System.out.println("呼叫一般指令("+command.get(ComLenght)+")結束:"+(System.currentTimeMillis()-sLog));
                                    }
				}catch (final AShellException e){
                                    ValueArray.clear();
                                    JudgmentAreaValueArray.clear();
                                    SESC.setSubEndStateCode(e.SESC.state, e.SESC.Code,e.SESC.LineNumbers,e.SESC.fileName, e.SESC.Message);
                                    return;
                                }catch (final Exception e) {
                                    ValueArray.clear();
                                    JudgmentAreaValueArray.clear();
                                    SESC.setSubEndStateCode(SubEndStateCode.State.Exception, command.get(ComLenght).erroeShowCommand.toString(),command.get(ComLenght).LineNumbers,command.fileName, e.getMessage());
                                    return;
                                }
			}
                        ValueArray.clear();
                    }while(new VarStrDW(AShell.this,RP,Boolean,JudgmentAreaValueArray,VarMode.Mode.Intermediary).Str.toString().matches(Type_String.TRUE+"|1"));
                    JudgmentAreaValueArray.clear();
                    SESC.setSubEndStateCode(SubEndStateCode.State.End, null,0,command.fileName, Type_String.NULL);
                }
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AShell.Native_Class;

import AShell.Data_Type_And_Struct.AShell_this;
import AShell.ValueProcess.AShellType;
/**
 *AShell互動式命令列界面附加函數
 */
public class AShell_InteractiveModeFunction {
    public static AShellType exit(AShell_this AShThis,AShellType[] Args) throws Exception{
        System.exit(0);
        return new AShellType();
    }
    public static AShellType help(AShell_this AShThis,AShellType[] Args) throws Exception{
        System.out.println("您現在使用的是AShell互動式命令列界面，在本界面中您可以將程式碼一行一行的輸入，每輸入一行，AShell會幫您馬上處理好，\n"
                          +"語法說明可以參考:http://blog.xuite.net/supmart/blog/429774559。\n"
                          +"另外，ver指令可以查看版本資訊，exit指令可以結束互動式命令列界面。");
        return new AShellType();
    }
    public static AShellType ver(AShell_this AShThis,AShellType[] Args) throws Exception{
        System.out.println("    ___   _____ __         ____\n"
                          +"   /   | / ___// /_  ___  / / /\n"
                          +"  / /| | \\__ \\/ __ \\/ _ \\/ / /\n"
                          +" / ___ |___/ / / / /  __/ / /\n"
                          +"/_/  |_/____/_/ /_/\\___/_/_/ \n"
                          +"(c)copyright 2017 jack850628 "+AShThis.AS.getVar());
        return new AShellType();
    }
}

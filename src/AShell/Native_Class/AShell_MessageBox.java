package AShell.Native_Class;

import AShell.Data_Type_And_Struct.AShell_this;
import AShell.ValueProcess.AShellType;
import javax.swing.*;

public class AShell_MessageBox{
	public AShellType showMessageBox(AShell_this AShthis,AShellType[] Args)throws Exception{
		if(Args.length==3)//showMessageBox(標題,內容,提示圖示樣式)
			JOptionPane.showMessageDialog(null, Args[1].enforce_to_java_String(),Args[0].enforce_to_java_String(),(int)Args[2].to_java_long());
		else if(Args.length==4)//showMessageBox(標題,內容,提示圖示樣式,自訂圖示(需要絕對路徑))
			JOptionPane.showMessageDialog(null, Args[1].enforce_to_java_String(),Args[0].enforce_to_java_String(),(int)Args[2].to_java_long(),new ImageIcon(Args[3].to_java_String()));
		else//showMessageBox(內容)
			JOptionPane.showMessageDialog(null, Args[0].enforce_to_java_String());
		return new AShellType(); 
	}
	public AShellType showConfirmBox(AShell_this AShthis,AShellType[] Args)throws Exception{
		if(Args.length==5)//showConfirmBox(標題,內容,按鈕樣式,提示圖示樣式,自訂圖示(需要絕對路徑))
			return new AShellType(JOptionPane.showConfirmDialog(null, Args[1].enforce_to_java_String(),Args[0].enforce_to_java_String(),(int)Args[2].to_java_long(),(int)Args[3].to_java_long(),new ImageIcon(Args[4].to_java_String()))); 
		else//showConfirmBox(標題,內容,按鈕樣式,提示圖示樣式)
			return new AShellType(JOptionPane.showConfirmDialog(null, Args[1].enforce_to_java_String(),Args[0].enforce_to_java_String(),(int)Args[2].to_java_long(),(int)Args[3].to_java_long())); 
	}
	public AShellType showInputBox(AShell_this AShthis,AShellType[] Args)throws Exception{
		String input;
		if(Args.length==6){//showInputBox(標題,內容,提示圖示樣式,自訂圖示(需要絕對路徑),{下拉式選單內容(陣列)},選單預設選項)
                        int AShArrayLength=AShellType.get_AShell_Array_Size(Args[4].to_java_String());
                        String[] menu=new String[AShArrayLength];
                        for(int i=0;i<AShArrayLength;i++){
                            menu[i]=AShellType.get_AShell_Array_Item(Args[4].to_java_String(), i).to_java_String();
                        }
			input=(String)JOptionPane.showInputDialog(null, Args[1].enforce_to_java_String(),Args[0].enforce_to_java_String(),(int)Args[2].to_java_long()
                                ,(Args[3].Type!=AShellType.java_Type.java_null)?new ImageIcon(Args[3].to_java_String()):null,menu,Args[5].enforce_to_java_String()); 
                        //return (input!=null)?new AShellType().put_AShell_Type_Auto(input):new AShellType();
                }else if(Args.length==3)//showInputBox(標題,內容,提示圖示樣式)
			input=JOptionPane.showInputDialog(null, Args[1].enforce_to_java_String(),Args[0].enforce_to_java_String(),(int)Args[2].to_java_long()); 
		else//showInputBox(內容)
			input=JOptionPane.showInputDialog(Args[0].enforce_to_java_String());
		return (input!=null)?new AShellType(input):new AShellType();
	}
        public AShellType showOptionBox(AShell_this AShthis,AShellType[] Args)throws Exception{
                int AShArrayLength=AShellType.get_AShell_Array_Size(Args[5].to_java_String());
                String[] menu=new String[AShArrayLength];
                for(int i=0;i<AShArrayLength;i++){
                    menu[i]=AShellType.get_AShell_Array_Item(Args[5].to_java_String(), i).to_java_String();
                }
                //showInputBox(標題,內容,按鈕樣式,提示圖示樣式,自訂圖示(需要絕對路徑),{按鈕文字(陣列，一個元素代表一個按鈕)},按鈕預設選項)
		return new AShellType(JOptionPane.showOptionDialog(null, Args[1].enforce_to_java_String(),Args[0].enforce_to_java_String(),(int)Args[2].to_java_long(),(int)Args[3].to_java_long()
                    ,(Args[4].Type!=AShellType.java_Type.java_null)?new ImageIcon(Args[4].to_java_String()):null,menu,Args[6].enforce_to_java_String())); 
	}
}
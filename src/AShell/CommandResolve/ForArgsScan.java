package AShell.CommandResolve;

import java.util.ArrayList;
//用來把for的條件以;分解的類別
public class ForArgsScan {
	public ArrayList<StringBuilder> Args=new ArrayList<>();
	public ForArgsScan(String Name){
            Args.add(new StringBuilder());
		for(int i=0;i<Name.length();i++){
                    String str=Name.substring(i, i+1);
                    switch (str) {
                        case ";":
                            Args.add(new StringBuilder());
                            break;
                        case "\"":
                            Args.get(Args.size()-1).append(str);
                            while(true){
                                i++;
                                str=Name.substring(i, i+1);
                                Args.get(Args.size()-1).append(str);
                                if(str.equals("\""))
                                    break;
                                else if(str.equals("\\"))
                                    Args.get(Args.size()-1).append(Name.substring(++i, i+1));
                            }
                            break;
                        default:
                            Args.get(Args.size()-1).append(str);
                            break;
                    }
		}
	}
}

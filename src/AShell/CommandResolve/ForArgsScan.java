package AShell.CommandResolve;

import java.util.ArrayList;
//用來把for的條件以;分解的類別
public class ForArgsScan {
	public ArrayList<StringBuilder> Args=new ArrayList<>();
	public ForArgsScan(String Name){
            Args.add(new StringBuilder());
            for(int i=0;i<Name.length();i++){
                char ch=Name.charAt(i);
                switch (ch) {
                    case '(':
                    case ')':
                        break;
                    case ';':
                        Args.add(new StringBuilder());
                        break;
                    case '\"':
                        Args.get(Args.size()-1).append(ch);
                        while(true){
                            i++;
                            ch=Name.charAt(i);
                            Args.get(Args.size()-1).append(ch);
                            if(ch == '\"')
                                break;
                            else if(ch == '\\')
                                Args.get(Args.size()-1).append(Name.charAt(++i));
                        }
                        break;
                    default:
                        Args.get(Args.size()-1).append(ch);
                        break;
                }
            }
	}
}

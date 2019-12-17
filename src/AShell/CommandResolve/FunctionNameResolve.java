package AShell.CommandResolve;

import java.util.ArrayList;

/**
 *取得要建立的函數名稱以及參數
 */
public class FunctionNameResolve {
	public ArrayList<StringBuilder> Args=new ArrayList<>();
	public StringBuilder Name=new StringBuilder();
        public StringBuilder Lanbda=null;
	public FunctionNameResolve(String Str){
            //System.out.println("Fun "+Str);
            int Record=0;
            boolean Results =false;
		for(int i=0;i<Str.length();i++){
                    String str=Str.substring(i, i+1);
			if(str.equals("(")){
				if(Results)
                                    if(Args.isEmpty())
                                        Args.add(new StringBuilder(str));
                                    else
                                        Args.get(Args.size()-1).append(str);
                                else
                                    Results=true;
				Record++;
			}else if(str.equals(")")){
                            if(--Record>0)
                                Args.get(Args.size()-1).append(str);
                        }else if(Record==0&&str.equals("=")){//抓取Landba形式宣告之=後面的運算式
                            if(i<Str.length()){
                                Lanbda=new StringBuilder(Str.substring(i+1,Str.length()));
                                return;
                            }
                        }else if(!Results&&str.equals("[")){//建立函數時，可以使用像是 function x[(1+1)]() 這樣的方式，所以這是用來讀取中誇號的內容
                            this.Name.append(str);
                            int count=1;
                            while(count!=0){
                                str=Str.substring(++i, i+1);
                                switch (str) {
                                    case "[":
                                        count++;
                                        break;
                                    case "]":
                                        count--;
                                        break;
                                    case "\"":
                                        this.Name.append(str);
                                        while(true){
                                            i++;
                                            str=Str.substring(i, i+1);
                                            this.Name.append(str);
                                            if(str.equals("\""))
                                                break;
                                            else if(str.equals("\\")){
                                                this.Name.append(Str.substring(++i, i+1));
                                            }
                                        }  
                                        continue;
                                }
                                this.Name.append(str);
                            }
                        }else if(str.equals(",")&&Record==1/*Record代表著函數的數量，大於一就代表著現在讀到的是函數參數中的函數呼叫*/){
                                if(Args.isEmpty())//如果Args陣列是空的就代表著呼叫函數時第一個參數是空白的
                                        Args.add(new StringBuilder());//把第一個參數建起來並保持空白
				Args.add(new StringBuilder());
			}else if(str.equals("{")){//會有這個是因為函數參數可以是陣列宣告
                            int Big_parantheses=1;//大括弧判斷
                            if(Args.isEmpty())
                                    Args.add(new StringBuilder(str));
                            else
                                    Args.get(Args.size()-1).append(str);
                            while(Big_parantheses!=0){
                                str=Str.substring(++i, i+1);
                                switch (str) {
                                    case "{":
                                        Big_parantheses++;
                                        break;
                                    case "}":
                                        Big_parantheses--;
                                        break;
                                    case "\"":
                                        Args.get(Args.size()-1).append(str);
                                        while(true){
                                            i++;
                                            str=Str.substring(i, i+1);
                                            Args.get(Args.size()-1).append(str);
                                            if(str.equals("\""))
                                                break;
                                            else if(str.equals("\\")){
                                                Args.get(Args.size()-1).append(Str.substring(++i, i+1));
                                            }
                                        }
                                        continue;
                                }
                                Args.get(Args.size()-1).append(str);
                            }
                        }else{
                                if(str.equals("\"")){
                                        if(Args.isEmpty())
                                            Args.add(new StringBuilder());
					 Args.get(Args.size()-1).append(str);
                                        while(true){
                                            i++;
                                            str=Str.substring(i, i+1);
                                             Args.get(Args.size()-1).append(str);
                                            if(str.equals("\"")){
                                                break;
                                            }else if(str.equals("\\"))
                                                Args.get(Args.size()-1).append(Str.substring(++i, i+1));
                                        }
				}else{
                                    if(!Results){
                                        if(!str.equals(" ")&&!str.equals("  "))
                                            this.Name.append(str);
                                    }else if(Record > 0){
                                            if(Args.isEmpty())
                                                Args.add(new StringBuilder(str));
                                            else
                                                Args.get(Args.size()-1).append(str);
                                    }
                                }
                        }
		}
                /*for(int i = 0;i < Args.size();i++){
                    String s = Args.get(i).toString().trim();
                    if(s.length() != Args.get(i).length()){
                        Args.get(i).delete(0, Args.get(i).length());
                        Args.get(i).append(s);
                    }
                }*/
	}
}

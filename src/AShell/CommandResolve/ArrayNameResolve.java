package AShell.CommandResolve;

import java.util.ArrayList;
/**���o�}�C�ѼƥΪ����O**/
public class ArrayNameResolve {
	public int Type=0;//0=Value,1=Array,2=Function
	public ArrayList<StringBuilder> Args=new ArrayList<>();
	/**�ܼƦW��**/
	public StringBuilder Name=new StringBuilder();
        public ArrayNameResolve(){}
	public ArrayNameResolve(String Name){
		int A=Name.indexOf("["),F=Name.indexOf("(");
                if(A!=-1 && ( F!=-1 && A<F || F==-1 && A>F)){
                    Type=1;
                    Array(Name);
                }else if(F!=-1 && ( A!=-1 && F<A || A==-1&& F>A)){
                    Type=2;
                    Fun(Name);
                }else
                    this.Name.append(Name);
	}
        private void Array(String Str){
            int Record=0;
            boolean Results =false;
		for(int i=0;i<Str.length();i++){
                    String str=Str.substring(i, i+1);
                    switch (str) {
                        case "[":
                            if(Record==0)
                                Args.add(new StringBuilder());
                            else if(Record>0)
                                Args.get(Args.size()-1).append(str);
                            Results=true;
                            Record++;
                            break;
                        case "]":
                            if(--Record>0)
                                Args.get(Args.size()-1).append(str);
                            break;
                        default:
                            if(str.equals("\"")){
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
                            }else if(!Results)
                                this.Name.append(str);
                            else
                                if(Record>0)
                                    Args.get(Args.size()-1).append(str);
                            break;
                    }
		}
        }
        private void Fun(String Str){//這個目前用於內建函數呼叫
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
                                if(str.equals("{"))
                                    Big_parantheses++;
                                else if(str.equals("}"))
                                    Big_parantheses--;
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
				}else if(!str.equals(" ")&&!str.equals("  ")){
                                    if(!Results)
                                            this.Name.append(str);
                                    else{
                                            if(Args.isEmpty())
                                                Args.add(new StringBuilder(str));
                                            else
                                                Args.get(Args.size()-1).append(str);
                                    }
                                }
                        }
		}
                //System.out.println("Fun END");
        }
}

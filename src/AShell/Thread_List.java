package AShell;

import java.util.LinkedList;

class Thread_List extends LinkedList<AShell.Run_Point>{//執行續記錄清單類，將所有存取方式都確保資料同步
    public synchronized boolean add(AShell.Run_Point r){
        return super.add(r);
    }
    public synchronized AShell.Run_Point get(int i){
        return super.get(i);
    }
    public synchronized AShell.Run_Point set(int i,AShell.Run_Point r){
        return super.set(i,r);
    }
    public synchronized int indexOf(AShell.Run r){
        return super.indexOf(r);
    }
    public synchronized int size(){
        return super.size();
    }
    public synchronized boolean isEmpty(){
        return super.isEmpty();
    }
    public synchronized AShell.Run get_Run(int index){
        int top=this.size(),botton=0,middle=(top-botton)/2;
        while(true){
            if(this.get(middle).index==index)
                return this.get(middle).Run;
            else if(this.get(middle).index>index){
                if(top==middle)//如果搜尋範圍沒有改變，就代表要找的記憶體位置不存在
                    break;
               top=middle;
                middle=((top-botton)/2)+botton;
            }else if(this.get(middle).index<index){
                if(botton==middle)//如果搜尋範圍沒有改變，就代表要找的記憶體位置不存在
                    break;
                botton=middle;
                middle=((top-botton)/2)+botton;
           }
        }
        return null;
    }
}

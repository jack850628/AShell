package AShell.Data_Type_And_Struct;

import AShell.AShell;
import AShell.Memory_Management.Memory_Management;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

//變數清單類別
public class Value_Array extends LinkedList<Value>{
    public HashMap<String,Object> instance_Class_Map = new HashMap<String,Object>();//存放這個AShell變數清單中已經實例化的java類別<java類別名稱,java類別實例化後的物件>
    public HashSet<String>        UsingAndCallTable  = new HashSet<String>();//紀錄已經using或call的腳本檔，防止重複調用，存放的內容是腳本的的完整路徑，所以就算是用claa去重複呼叫已經using的內建腳本也不會重複調用
    public Value_Array            Previous_Floor     = null;//紀錄該變數清單所在的執行緒的上一層執行緒的變數清單
    public boolean                SClass             = false;//判斷是否為SClass的變數清單，用在ResultsValue.SetValue函數的函數複寫判斷用，但是複寫能力不完善，現在功能是註解掉的狀態
    private int                   Arguments          = 1;//參考者的數量，紀錄有多少執行續參考
    /**
     * Value_Array建構式
     * @param _super 該變數清單所在的執行緒的上一層執行緒的變數清單
     */
    public Value_Array(Value_Array _super){
        super();
        if(_super!=null)
            this.Previous_Floor=_super.Reference();
    }
    /**
     * 定變數清單是SClass的
     **/
    public void isSClass(){
        this.SClass=true;
    }
    //變數清單參考函數
    public Value_Array Reference(){
        Arguments++;
        if(Previous_Floor!=null)
            Previous_Floor.Reference();
        return this;
    }
    public void Release_Arguments(){
            for(Value v:this)
                if(v.Tent.toString().matches(Type_String.MEMORY_TYPE))//判斷原變數內容是不是記憶體參考
                    try {
                        Memory_Management.Cut_To_Arguments(v.Tent.toString());//將參考指數減一
                    } catch (Exception ex) {}
    }
    @Override
    public synchronized boolean add(Value v){
        return super.add(v);
    }
    @Override
    public synchronized Value get(int i){
        return super.get(i);
    }
    @Override
    public synchronized Value remove(int i){
        return super.remove(i);
    }
    public void clear(){
        /*ValueArray.stream().forEach((value) -> {
            if(!value.Name.toString().matches("base|this|super"))
                Memory_Management.Reference_person_NN(value.Tent.toString());//判斷所訂的值是不是記憶體參考
        });*/
        if(Previous_Floor!=null)
            Previous_Floor.clear();
        int classAndFunctionForThis=0;//在這個變數清單建立的函數數量
        synchronized(this){
            for(Value V:this){
                try {
                    if(V.Tent.toString().matches(Type_String.CLASS_M)){//如果是類別
                        Memory_Management.Class_Memory_Type CMT=Memory_Management.getClassCMT(V.Tent.toString());
                        if(CMT.getArguments()==1&&CMT.getObject().ValueArray.Previous_Floor==this)//如果參照數量只有1且閉包變數清單是現在這個變數清單，也就是說是在這個變數清單建立的類別
                            classAndFunctionForThis++;
                    }else if(V.Tent.toString().matches(Type_String.FUNCTION_M)){//如果是函數
                        Memory_Management.Function_Memory_Type FMT=Memory_Management.getFunctionForFMT(V.Tent.toString());
                        if(FMT.getArguments()==1&&FMT.getObject().Closure_ValueArray==this)//如果參照數量只有1且閉包變數清單是現在這個變數清單，也就是說是在這個變數清單建立的函數
                            classAndFunctionForThis++;
                    }
                } catch (Exception ex) {
                    //Logger.getLogger(Value_Array.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(--Arguments==0||classAndFunctionForThis==Arguments){
                Release_Arguments();
                super.clear();
                instance_Class_Map.clear();
                UsingAndCallTable.clear();
            }
        }
    }
    /**
     * 具有垃圾回收能力的clear
     * @param AS AShell語言解析器的主類別
     *@param RP 執行續指標，用來記錄一個AShell執行續在執行續清單裡的位置
     */
    public void clear(AShell AS,AShell.Run_Point RP){
        this.clear();
        Memory_Management.Garbage_Collection_Start(AS, RP);
    }
}

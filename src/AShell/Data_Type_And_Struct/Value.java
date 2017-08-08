package AShell.Data_Type_And_Struct;

public class Value {
	public Value(StringBuilder Name,StringBuilder Tent/*,long Numbering*/){
		this.Name=Name;
		this.Tent=Tent;
                //this.Numbering=Numbering;
	}
	public StringBuilder Name,Tent;
        //public long Numbering;//用來辨識該變數是在哪個執行續生成的，另外 -1 用來表示空建構式與解構式，其目的是為了不被父類別取代且可以不用為了每個類別實體建立空建構式和解構式
}

package AShell.Memory_Management;

import AShell.Data_Type_And_Struct.Value_Array;


public class CensorValue {
        /**尋找變數清單中的一個變數
         * @param Name 欲尋找的變數名稱
         * @param ValueArray 變數可能存在的變數清單
         * @param single_floor 當為真時，指查詢一層變數清單就不再往上層查詢，使用時機其中一個就是var模式，欲創建變數是否存在只須要看該層即可
         * @return 查詢結果
         **/
	public static CensorValueReturn CensorValue(String Name,Value_Array ValueArray,boolean single_floor){
		CensorValueReturn CVR=new CensorValueReturn();
                while(ValueArray!=null){
                    for(int i=0;i<ValueArray.size();i++){
                            if(ValueArray.get(i).Name.toString().equals(Name)){
                                    CVR.Result=true;
                                    CVR.index=i;
                                    CVR.ValueArray=ValueArray;
                                    return CVR;
                            }
                    }
                    if(single_floor)
                        return CVR;
                    ValueArray=ValueArray.Previous_Floor;//如果這一層找不到該變數，就跳至上一層找
                }
                return CVR;
	}
        /**尋找變數清單中的多個變數
         * @param Names 欲尋找的變數名稱的集合
         * @param ValueArray 變數可能存在的變數清單
         * @param single_floor 當為真時，指查詢一層變數清單就不再往上層查詢，使用時機其中一個就是var模式，欲創建變數是否存在只須要看該層即可
         * @return 查詢結果的集合
         **/
        public static CensorValueReturn[] CensorValues(String[] Names,Value_Array ValueArray,boolean single_floor){
		CensorValueReturn CVR[]=new CensorValueReturn[Names.length];
                for(int c=0;c<CVR.length;c++)
                    CVR[c]=new CensorValueReturn();
                int count=0;//變數查詢紀錄，當查到一個變數時就會加一
                while(ValueArray!=null){
                    for(int i=0;i<ValueArray.size();i++){
                        for(int j=0;j<CVR.length;j++){
                            if(!CVR[j].Result&&ValueArray.get(i).Name.toString().equals(Names[j])){
                                    CVR[j].Result=true;
                                    CVR[j].index=i;
                                    CVR[j].ValueArray=ValueArray;
                                    count++;
                                    break;
                            }
                        }
                        if(count==CVR.length||single_floor)
                            return CVR;
                         ValueArray=ValueArray.Previous_Floor;//如果這一層還沒找到所有變數，就跳至上一層找
                    }
                }
                return CVR;
	}
}

package AShell.Data_Type_And_Struct;
/**字串與數字的轉換**/
public class IsNumeric {
	public static double isNumeric(String str){
		try{
			return Double.parseDouble(str);
		} catch(Exception e) {
			return 0.0;
		}
	}
	public static boolean isNumericTest(String str){
		return str.matches(Type_String.DIGITAL);
                /*try{
			Double.valueOf(str);
			return true;
		} catch(Exception e) {
			return false;
		}*/
	}
}

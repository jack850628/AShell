
public class StringRead {
    private static final StringBuilder temp = new StringBuilder();
    
    private static native String Read();
    public static String getString(){
        if(temp.length() == 0)
            temp.append(Read());
        String string = temp.substring(0);
        temp.delete(0, temp.length());
        return string;
    }
    public static void setString(String string){
        temp.append(string);
    }
    public static int get_temp_strlen(){
        return temp.length();
    }
    static {
        System.load("C:\\AShell\\StringRead.dll");
    }
}

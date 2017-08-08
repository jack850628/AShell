/**
 * 
 * 呼叫C語言用的類別
 */
public class CFunction {
    public static native void clearScreen();
    public static native char Cgetch();
    static {
        //System.loadLibrary("C_Function");
        System.load("C:\\AShell\\C_Function.dll");
    }
}
package AShell;

/**
 *
 * 呼叫終端機用的類別
 */
public class CallSystem {
    public static native int System(String arg);
    public static native int chdir(String arg);
    static{
        //System.loadLibrary("Call_System");
        System.load("C:\\AShell\\Call_System.dll");
    }
}

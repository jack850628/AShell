package AShell.ValueProcess;
/**
 * 用來判斷呼叫VarStrDW要用什麼模式
 */
public class VarMode {
    /**
     * 模式選項
     * Var：變數建立模式，如果要建立的變數已經存在於變數清單中，則將該變數刪除，在建立變數。
     * Unvar：刪除變數模式(反宣告)，將變數重變數清單中刪除。
     * General：一般模式，主要用於改變變數內的值。
     * Intermediary：中介模式，處理出來的值，作為函數參數使用。
     */
    public static enum Mode{Var,Unvar,General,Intermediary};
}

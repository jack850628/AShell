package AShell.Data_Type_And_Struct;

//AShell類型名稱字串
public class Type_String {
    public static final String FUNCTION_M                  = "AShell_Function@\\d+";
    public static final String FUNCTION_N                  = "Function";
    public static final String NATIVE_FUNCTION_M           = "AShell_NFunction@\\d+";
    public static final String NATIVE_FUNCTION_N           = "NFunction";
    public static final String OBJECT_M                    = "AShell_Object@\\d+";
    public static final String OBJECT_N                    = "Object";
    public static final String CLASS_M                     = "AShell_Class@\\d+";
    public static final String CLASS_N                     = "Class";
    public static final String ARRAY_M                     = "AShell_Array@\\d+";
    public static final String ARRAY_N                     = "Array";
    public static final String MEMORY_TYPE                 = FUNCTION_M+"|"+NATIVE_FUNCTION_M+"|"+OBJECT_M+"|"+CLASS_M+"|"+ARRAY_M;
    public static final String TRUE                        = "true";
    public static final String FALSE                       = "false";
    public static final String BOOLEAN_TYPE                = TRUE+"|"+FALSE;
    public static final String NULL                        = "null";
    public static final String THIS                        = "this";
    public static final String SUPER                       = "super";
    public static final String INFINITY                    = "[-+]?Infinity";
    public static final String NAN                         = "NaN";
    public static final String DIGITAL                     = "[-+]?\\d+[.]?\\d*|[-+]?\\d+\\.\\d+E\\d+|[-+]?Infinity";//數字(整數或浮點)
    public static final String POSITIVE_DIGITAL            = "[+]?\\d+[.]?\\d*|[+]?\\d+\\.\\d+E\\d+|[+]?Infinity";
    public static final String NEGATIVE_DIGITAL            = "-\\d+[.]?\\d*|-\\d+\\.\\d+E\\d+|-Infinity";
    public static final String INTEGER                     = "[-+]?\\d+";//整數
    public static final String POSITIVE_INTEGER            = "[+]?\\d+";
    public static final String NEGATIVE_INTEGER            = "-\\d+";
    public static final String FLOAT                       = "[-+]?\\d+\\.\\d*|[-+]?\\d+\\.\\d+E\\d+|[-+]?Infinity";
    public static final String POSITIVE_FLOAT              = "[+]?\\d+\\.\\d*|[+]?\\d+\\.\\d+E\\d+|[+]?Infinity";//正浮點數
    public static final String NEGATIVE_FLOAT              = "-\\d+\\.\\d*|-\\d+\\.\\d+E\\d+|-Infinity";//負浮點數
    public static final String INSTANCE_FUNCTION_NAME      = "_inst_";
    public static final String DESTRUCTOR_FUNCTION_NAME    = "_dest_";
    public static final String VALUE_NAME                  = "[A-Za-z\\u4e00-\\u9fa5_$][A-Za-z\\u4e00-\\u9fa50-9_$]*";//匹配中文:[\u4e00-\u9fa5]   匹配日文[\u0800-\u4e00]
    public static final String ASHELL_TYPE                 = String.format("%s|%s|%s|%s|%s|AShell_(%s|%s|%s|%s|%s)@\\d+", NULL,DIGITAL,NAN,TRUE,FALSE,OBJECT_N,CLASS_N,
                                                                                                                                    FUNCTION_N,NATIVE_FUNCTION_N,ARRAY_N);
    public static final String ASHELL_TYPE_EXCLUDE_DIGITAL = String.format("%s|%s|%s|%s|AShell_(%s|%s|%s|%s|%s)@\\d+", NULL,NAN,TRUE,FALSE,OBJECT_N,CLASS_N,
                                                                                                                                    FUNCTION_N,NATIVE_FUNCTION_N,ARRAY_N);
}

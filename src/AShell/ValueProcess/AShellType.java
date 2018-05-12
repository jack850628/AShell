package AShell.ValueProcess;

import static AShell.CommandResolve.StringScan.to_AShell_String;
import AShell.Data_Type_And_Struct.Class_Type;
import AShell.Data_Type_And_Struct.Function;
import AShell.Data_Type_And_Struct.Native_Function;
import AShell.Data_Type_And_Struct.Type_String;
import AShell.Memory_Management.Memory_Management;

//AShell與java溝通用的類型交換類別
public class AShellType {
        String AShell_Value;//AShell中介狀態值
        public static enum java_Type{java_String,java_long,java_double,java_bloolean,java_null}//AShell可轉換的成的java類型
        private static String TypeError(java_Type Type){
            if(Type==java_Type.java_String)
                return "String";
            else if(Type==java_Type.java_long)
                return "long";
            else if(Type==java_Type.java_double)
                return "double";
            else if(Type==java_Type.java_bloolean)
                return "boolean";
            else
                return "null";
        } 
        /**
         * 取得AShell陣列中的其中一個元素
         * @param Array AShell陣列的記憶體位址
         * @param intex 要取得的元素項
         * @return 取得道的元素(打包成AShellType類型)
         * @throws Exception Array參數類型不是AShell陣列的記憶體位址 或 查詢失敗
         */
        public static AShellType get_AShell_Array_Item(String Array,int intex) throws Exception{
            if(!Array.startsWith("AShell_A"))
                throw new Exception("java函數get_AShell_Array_Item只接受AShell Array記憶體類型值");
            return new AShellType().setAShell_Value(Memory_Management.get_Array_Item(Memory_Management.move(Array, intex)).toString());
        }
        /**
         * 取得AShell陣列長度
         * @param Array AShell陣列的記憶體位址
         * @return AShell陣列長度
         * @throws Exception Array參數類型不是AShell陣列的記憶體位址 或 查詢失敗
         */
        public static int get_AShell_Array_Size(String Array) throws Exception{
            if(!Array.startsWith("AShell_A"))
                throw new Exception("java函數get_AShell_Array_Size只接受AShell Array記憶體類型值");
            return Memory_Management.get_Array_Sise(Array);
        }
        public java_Type Type;//表示這個值可轉換的java值
        public boolean is_AShell_Memory_Type=false;
        private Object AShell_Memory_Type;
        public AShellType(){//AShell to java或java to AShell都可以使用的通用建構式，用在當AShell建立Native函數參數 或 java to AShell值為null時
            AShell_Value=Type_String.NULL;
        }
        //-----------------------建構式(java to AShell用)-----------------------------
        public AShellType(String Str) throws Exception{//將java字串轉換成AShell字串，但是要放入AShell記憶體類型或boolean或null時用這個建構式，那麼也會被轉換成AShell字串，所以要放入AShell記憶體類型或AShell的boolean或AShell的null時請用put_AShell_Memory_Type或put_AShell_Boolean_Type或put_AShell_Null_Type或put_AShell_Type_Auto函數
            this.AShell_Value = to_AShell_String(Str != null ? Str : "").toString();
            this.Type=java_Type.java_String;
        }
        public AShellType(char value){
            switch (value) {
                case '\"':
                    this.AShell_Value="\"\\\"\"";
                    break;
                case '\\':
                    this.AShell_Value="\"\\\\\"";
                    break;
                case '\n':
                    this.AShell_Value="\"\\n\"";
                    break;
                case '\t':
                    this.AShell_Value="\"\\t\"";
                    break;
                case '\b':
                    this.AShell_Value="\"\\b\"";
                    break;
                case '\r':
                    this.AShell_Value="\"\\r\"";
                    break;
                case '\f':
                    this.AShell_Value="\"\\f\"";
                    break;
                default:
                    this.AShell_Value="\""+value+"\"";
                    break;
            }
            this.Type=java_Type.java_String;
        }
        public AShellType(boolean value){
            this.AShell_Value=String.valueOf(value);
            this.Type=java_Type.java_bloolean;
        }
        public AShellType(short value){
            this.AShell_Value=String.valueOf(value);
            this.Type=java_Type.java_long;
        }
        public AShellType(int value){
            this.AShell_Value=String.valueOf(value);
            this.Type=java_Type.java_long;
        }
        public AShellType(long value){
            this.AShell_Value=String.valueOf(value);
            this.Type=java_Type.java_long;
        }
        public AShellType(float value){
            this.AShell_Value=String.valueOf(value);
            this.Type=java_Type.java_double;
        }
        public AShellType(double value){
            this.AShell_Value=String.valueOf(value);
            this.Type=java_Type.java_double;
        }
        //--------------------------------------------------------------------------------------
        /**
         * '放入AShell記憶體類型
         * @param AShell_Memory AShell記憶體位址
         * @param AllowNull 寬鬆檢查(允許AShell_Memory值AShell的Null類型)
         * @return AShellType
         * @throws Exception 放入值錯誤
         */
        public AShellType put_AShell_Memory_Type(String AShell_Memory,boolean AllowNull) throws Exception{
            if(AShell_Memory.matches(Type_String.OBJECT_M)){
                AShell_Memory_Type=Memory_Management.get_Object(AShell_Memory);
                this.is_AShell_Memory_Type=true;
            }else if(AShell_Memory.matches(Type_String.CLASS_M)){
                AShell_Memory_Type=Memory_Management.get_Class(AShell_Memory);
                this.is_AShell_Memory_Type=true;
            }else if(AShell_Memory.matches(Type_String.FUNCTION_M)){
                AShell_Memory_Type=Memory_Management.get_Function(AShell_Memory);
                this.is_AShell_Memory_Type=true;
            }else if(AShell_Memory.matches(Type_String.NATIVE_FUNCTION_M)){
                AShell_Memory_Type=Memory_Management.get_Native_Function(AShell_Memory);
                this.is_AShell_Memory_Type=true;
            }else if(AShell_Memory.matches(Type_String.ARRAY_M))
                this.is_AShell_Memory_Type=true;
            else if(AllowNull&&AShell_Memory.equals(Type_String.NULL))
                this.Type=java_Type.java_null;
            else
            //if(!AShell_Memory.matches(Type_String.MEMORY_TYPE+((AllowNull)?"|"+Type_String.NULL:"")))
                throw new Exception("java函數put_AShell_Memory_Type放入的不是AShell的Memory"+((AllowNull)?"或null":"")+"類型");
            this.AShell_Value=AShell_Memory;
            this.Type=java_Type.java_String;
            return this;
        }
        public AShellType put_AShell_Boolean_Type(String AShell_Boolean) throws Exception{
            if(!AShell_Boolean.matches(Type_String.BOOLEAN_TYPE))
                throw new Exception("java函數put_AShell_Memory_Type放入的不是AShell的Boolean類型");
            this.AShell_Value=AShell_Boolean;
            this.Type=java_Type.java_bloolean;
            return this;
        }
        public AShellType put_AShell_Null_Type(String AShell_Null) throws Exception{
            if(!AShell_Null.matches(Type_String.NULL))
                throw new Exception("java函數put_AShell_Memory_Type放入的不是AShell的Null類型");
            this.AShell_Value=AShell_Null;
            this.Type=java_Type.java_null;
            return this;
        }
        public AShellType put_AShell_Value_Auto(String value) throws Exception{//自動類型判斷放入(將字串自動轉換成AShell各種類型)
            if(value.equals(Type_String.NULL)){
                this.AShell_Value=value;
                this.Type=java_Type.java_null;
            }else if(value.matches(Type_String.BOOLEAN_TYPE)){
                this.AShell_Value=value;
                this.Type=java_Type.java_bloolean;
            }else if(value.matches(Type_String.FLOAT)){
                this.AShell_Value=value;
                this.Type=java_Type.java_double;
            }else if(value.matches(Type_String.INTEGER)){
                this.AShell_Value=value;
                this.Type=java_Type.java_long;
            }else if(value.matches("(AShell_)("+Type_String.OBJECT_N+"|"+Type_String.CLASS_N+"|"+Type_String.FUNCTION_N+
                    "|"+Type_String.NATIVE_FUNCTION_N+"|"+Type_String.ARRAY_N+")(@)(\\d+)")){
                if(value.matches(Type_String.OBJECT_M)){
                    AShell_Memory_Type=Memory_Management.get_Object(value);
                    this.is_AShell_Memory_Type=true;
                }else if(value.matches(Type_String.CLASS_M)){
                    AShell_Memory_Type=Memory_Management.get_Class(value);
                    this.is_AShell_Memory_Type=true;
                }else if(value.matches(Type_String.FUNCTION_M)){
                    AShell_Memory_Type=Memory_Management.get_Function(value);
                    this.is_AShell_Memory_Type=true;
                }else if(value.matches(Type_String.NATIVE_FUNCTION_M)){
                    AShell_Memory_Type=Memory_Management.get_Native_Function(value);
                    this.is_AShell_Memory_Type=true;
                }else if(value.matches(Type_String.ARRAY_M))
                    this.is_AShell_Memory_Type=true;
                this.AShell_Value=value;
                this.Type=java_Type.java_String;
            }else{
                this.AShell_Value = to_AShell_String(value).toString();
                this.Type=java_Type.java_String;
            }
            return this;
        }
        AShellType setAShell_Value(String value) throws Exception{//AShell to java，Native函數參數用，並不提供java to AShell用
            if(value.matches(Type_String.FLOAT))
                this.Type=java_Type.java_double;
            else if(value.matches(Type_String.INTEGER))
                this.Type=java_Type.java_long;
            else if(value.matches(Type_String.BOOLEAN_TYPE))
                this.Type=java_Type.java_bloolean;
            else if(value.matches(Type_String.NULL))
                this.Type=java_Type.java_null;
            else{//如果是字串或AShell記憶體類型
                if(value.matches(Type_String.OBJECT_M)){
                    AShell_Memory_Type=Memory_Management.get_Object(value);
                    this.is_AShell_Memory_Type=true;
                }else if(value.matches(Type_String.CLASS_M)){
                    AShell_Memory_Type=Memory_Management.get_Class(value);
                    this.is_AShell_Memory_Type=true;
                }else if(value.matches(Type_String.FUNCTION_M)){
                    AShell_Memory_Type=Memory_Management.get_Function(value);
                    this.is_AShell_Memory_Type=true;
                }else if(value.matches(Type_String.NATIVE_FUNCTION_M)){
                    AShell_Memory_Type=Memory_Management.get_Native_Function(value);
                    this.is_AShell_Memory_Type=true;
                }else if(value.matches(Type_String.ARRAY_M))
                    this.is_AShell_Memory_Type=true;
                this.Type=java_Type.java_String;
            }
            this.AShell_Value=value;
            return this;
        }
        public String to_java_String() throws Exception{
            if(Type!=java_Type.java_String)
                throw new Exception("AShell轉換java型態錯誤，應該要是'"+TypeError(java_Type.java_String)+"'但卻是'"+TypeError(this.Type)+"'");
            if(AShell_Value.matches("(AShell_)("+Type_String.OBJECT_N+"|"+Type_String.CLASS_N+"|"+Type_String.FUNCTION_N+
                    "|"+Type_String.NATIVE_FUNCTION_N+"|"+Type_String.ARRAY_N+")(@)(\\d+)"))//因為已經經過類型過濾，所以只需要判斷是不是AShell記憶體類型
                return AShell_Value;
            StringBuilder SB=new StringBuilder();
            for(int i=1;i<AShell_Value.length()-1;i++){
                if(AShell_Value.charAt(i)!='\\'){//如果取到的字串不是跳脫字元
                        SB.append(AShell_Value.charAt(i));
                }else{
                    char c=AShell_Value.charAt(++i);
                    switch(c){
                        case 'n':
                            SB.append('\n');
                            break;
                        case 'r':
                            SB.append('\r');
                            break;
                        case 't':
                            SB.append('\t');
                            break;
                        case 'b':
                            SB.append('\b');
                            break;
                        case 'f':
                            SB.append('\f');
                            break;
                        case '\\':
                            SB.append('\\');
                            break;
                        default:
                            SB.append(c);
                            break;
                    }
                }
            }
            return SB.toString();
	}
        public String enforce_to_java_String() throws Exception{//不管內部是什麼類型都強制轉換成java String
            if(Type==java_Type.java_String)
                return to_java_String();
            return AShell_Value;
	}
        public long to_java_long() throws Exception{
            if(Type!=java_Type.java_long)
                throw new Exception("AShell轉換java型態錯誤，應該要是'"+TypeError(java_Type.java_long)+"'但卻是'"+TypeError(this.Type)+"'");
            return Long.parseLong(AShell_Value);
        }
        public double to_java_double() throws Exception{
            if(Type!=java_Type.java_double)
                throw new Exception("AShell轉換java型態錯誤，應該要是'"+TypeError(java_Type.java_double)+"'但卻是'"+TypeError(this.Type)+"'");
            return Double.parseDouble(AShell_Value);
        }
        public boolean to_java_boolean() throws Exception{
            if(Type!=java_Type.java_bloolean&&Type!=java_Type.java_long)
                throw new Exception("AShell轉換java型態錯誤，應該要是'"+TypeError(java_Type.java_bloolean)+"'但卻是'"+TypeError(this.Type)+"'");
            return AShell_Value.matches(Type_String.TRUE+"|1");
        }
        public Class_Type to_AShell_Class() throws Exception{
            if(is_AShell_Memory_Type&&!AShell_Value.matches(Type_String.OBJECT_M))
                throw new Exception("轉換AShell型態錯誤，不是AShell class類型");
            return (Class_Type)AShell_Memory_Type;
        }
        public Class_Type to_AShell_Static_Class() throws Exception{
            if(is_AShell_Memory_Type&&!AShell_Value.matches(Type_String.CLASS_M))
                throw new Exception("轉換AShell型態錯誤，不是AShell static class類型");
            return (Class_Type)AShell_Memory_Type;
        }
        public Function to_AShell_Functuon() throws Exception{
            if(is_AShell_Memory_Type&&!AShell_Value.matches(Type_String.FUNCTION_M))
                throw new Exception("轉換AShell型態錯誤，不是AShell function類型");
            return (Function)AShell_Memory_Type;
        }
        public Native_Function to_AShell_Native_Functuon() throws Exception{
            if(is_AShell_Memory_Type&&!AShell_Value.matches(Type_String.NATIVE_FUNCTION_M))
                throw new Exception("轉換AShell型態錯誤，不是AShell native function類型");
            return (Native_Function)AShell_Memory_Type;
        }
}

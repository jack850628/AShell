using String
using Type

class Stdio
	static
		var JAVA_CLASS="AShell.Native_Class.AShell_Stdio"
		#基本輸出函數
		native print(JAVA_CLASS)
		#基本輸出函數(換行)
		function println(str="") = print(str.."\n")
		#格式化輸出
		function printf(form,arr) = print(String.format(form,arr))
		#基本錯誤函數
		native error(JAVA_CLASS)
		#基本輸入函數
		native scan(JAVA_CLASS)
		
		function inputNumber() = Type.toDigital(scan())
		#基本取單一字元函數
		native getch(JAVA_CLASS)
		#清除畫面
		native clear(JAVA_CLASS)
	endst
endcl
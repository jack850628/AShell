class Type
	static
		var JAVA_CLASS="AShell.Native_Class.AShell_Type"
		var INTEGER   = 0
		var FLOAT     = 1
		var BOOLEAN   = 2
		var STRING    = 3
		var CLASS     = 4
		var OBJECT    = 5
		var FUNCTION  = 6
		var NFUNCTION = 7
		var ARRAY     = 8
		var NULL      = 9
		#取得AShell資料類型
		native getType(JAVA_CLASS)
		#判斷參數是否為整數
		function isInteger(digital)
			if !isDigital(digital)
				return false
			endif
			return digital%1==0
		endfu
		#判斷參數是否為浮點數
		function isFloat(digital)
			if !isDigital(digital)
				return false
			endif
			return digital%1!=0
		endfu
		#判斷參數是否為數字
		function isDigital(digital)
			if digital==0
				return true
			endif
			return digital==toDigital(digital)
		endfu
		#判斷參數是否為字串
		function isString(digital)
			return !isDigital(digital)
		endfu
		#將浮點數轉換成整數
		function toInteger(float)
			return toDigital(float)//1
		endfu
		#將參數轉換成字串
		function toString(arg)
			return arg..""
		endfu
		#將參數轉換成數字
		native toDigital(JAVA_CLASS)
	endst
endcl
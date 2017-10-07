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
		#眔AShell戈摸
		native getType(JAVA_CLASS)
		#耞把计琌俱计
		function isInteger(digital)
			if !isDigital(digital)
				return false
			endif
			return digital%1==0
		endfu
		#耞把计琌疊翴计
		function isFloat(digital)
			if !isDigital(digital)
				return false
			endif
			return digital%1!=0
		endfu
		#耞把计琌计
		function isDigital(digital)
			if digital==0
				return true
			endif
			return digital==toDigital(digital)
		endfu
		#耞把计琌﹃
		function isString(digital)
			return !isDigital(digital)
		endfu
		#盢疊翴计锣传Θ俱计
		function toInteger(float)
			return toDigital(float)//1
		endfu
		#盢把计锣传Θ﹃
		function toString(arg)
			return arg..""
		endfu
		#盢把计锣传Θ计
		native toDigital(JAVA_CLASS)
	endst
endcl
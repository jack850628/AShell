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
		#���oAShell�������
		native getType(JAVA_CLASS)
		#�P�_�ѼƬO�_�����
		function isInteger(digital)
			if !isDigital(digital)
				return false
			endif
			return digital%1==0
		endfu
		#�P�_�ѼƬO�_���B�I��
		function isFloat(digital)
			if !isDigital(digital)
				return false
			endif
			return digital%1!=0
		endfu
		#�P�_�ѼƬO�_���Ʀr
		function isDigital(digital)
			if digital==0
				return true
			endif
			return digital==toDigital(digital)
		endfu
		#�P�_�ѼƬO�_���r��
		function isString(digital)
			return !isDigital(digital)
		endfu
		#�N�B�I���ഫ�����
		function toInteger(float)
			return toDigital(float)//1
		endfu
		#�N�Ѽ��ഫ���r��
		function toString(arg)
			return arg..""
		endfu
		#�N�Ѽ��ഫ���Ʀr
		native toDigital(JAVA_CLASS)
	endst
endcl
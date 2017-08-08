using String
using Type

class Stdio
	static
		var JAVA_CLASS="AShell.Native_Class.AShell_Stdio"
		#�򥻿�X���
		native print(JAVA_CLASS)
		#�򥻿�X���(����)
		function println(str="")
			return print(str.."\n")
		endfu
		#�榡�ƿ�X
		function printf(form,arr)
			return print(String.format(form,arr))
		endfu
		#�򥻿��~���
		native error(JAVA_CLASS)
		#�򥻿�J���
		native scan(JAVA_CLASS)
		
		function inputNumber()
			return Type.toDigital(scan())
		endfu
		#�򥻨���@�r�����
		native getch(JAVA_CLASS)
		#�M���e��
		native clear(JAVA_CLASS)
	endst
endcl
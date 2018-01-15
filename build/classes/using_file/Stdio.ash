using String
using Type

class Stdio
	static
		var JAVA_CLASS="AShell.Native_Class.AShell_Stdio"
		#�򥻿�X���
		native print(JAVA_CLASS)
		#�򥻿�X���(����)
		function println(str="") = print(str.."\n")
		#�榡�ƿ�X
		function printf(form,arr) = print(String.format(form,arr))
		#�򥻿��~���
		native error(JAVA_CLASS)
		#�򥻿�J���
		native scan(JAVA_CLASS)
		
		function inputNumber() = Type.toDigital(scan())
		#�򥻨���@�r�����
		native getch(JAVA_CLASS)
		#�M���e��
		native clear(JAVA_CLASS)
	endst
endcl
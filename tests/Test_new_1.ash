using Stdio.ash
using System.ash

function a()
	Stdio.println("a")
endfu
class b
	function _inst_()
		Stdio.println("ib")
	endfu
endcl
a()
b()
if true
	function a()
		Stdio.println("b")
	endfu
	class b
		function _inst_()
			Stdio.println("ibb")
		endfu
	endcl
	a()
	b()
endif
a()
b()
function poi()
	function poi2()
		function poi3()
			return "poi"
		endfu
		return poi3
	endfu
	return poi2
endfu
Stdio.println(poi()()())
function ppooii(i)
	if i>0
		Stdio.println(i)
		ppooii(i-1)
	endif
endfu
ppooii(5)
class asd
	var k="yee"
	static
		function ins()
			return asd()
		endfu
	endst
endcl
Stdio.println(asd.ins().k)
System.system("pause")
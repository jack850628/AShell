using Stdio.ash
using Thread.ash

class a 
	static
		var s="qqq"
	endst
	function aa()
		Stdio.println("aa")
	endfu
endcl
function aa()
	Stdio.println("qwe")
endfu
class b:a
	static 
		function x
			Stdio.println(s)
		endfu
		x()
	endst
	function showt_this
		Stdio.println(this)
	endfu
	showt_this()
	class p
	endcl
endcl
b.x()
b().aa()
b().p.this
Thread.pause()
using Stdio.ash
using Thread.ash

class a 
	function aa()
		Stdio.println("aa")
	endfu
	function g()
		aa()
	endfu
endcl
class b:a
	function _inst_()
		super.aa=this.aa
	endfu
	function aa()
		Stdio.println("bb")
	endfu
endcl
b().g()
Thread.pause()
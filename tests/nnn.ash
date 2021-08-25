#using Stdio.ash
using Thread.ash

var v=9
class r
	static
		var q="poi"
	endst
	function ff()
		return this
	endfu
endcl
class a:r
	
		var v=8
		function f(a=v,l)
			Stdio.println("f"..a..l)
			return ""
		endfu
		function rr()
			return f
		endfu
		function f2()
			Stdio.println(super.q)
		endfu
	
endcl
class c:a
	var v=87
		Stdio.println(v..q)
	
endcl
Stdio.println(v)
Stdio.println(c.q..c().rr()())
a().f2()
var h=a()
Stdio.println(h.this.." "+h.super.this)
var hh=9
try
	qwe
catch var hh
	hh=1
endtr
Stdio.println(hh)
var temp
if true
	var hh=87
	class t
	endcl
	temp=t
endif
Stdio.println(temp.hh)
class a1
	static
		var v1="v1"
		class b
			
		endcl
	endst
endcl
class b1:a1.b
	function f()
		Stdio.println(a1.v1)
	endfu
endcl
b1().f()
class s
endcl
var sr=87
Stdio.println(s.sr)
Thread.pause()
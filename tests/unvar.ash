using Stdio.ash
using System.ash

var a=9,b=8
unvar base.a,base.b
try
	Stdio.println(b)
catch var e
	Stdio.println(e)
endtr
class ca
endcl
function ca.fun()
	Stdio.println("cafun")
endfu
ca.fun()
class ca2
endcl
class ca2.cla
	function _inst_()
		Stdio.println("ca2clainst")
	endfu
endcl
ca2.cla()
System.system("pause")
using Stdio.ash
using System.ash

class yo
	static
		var f="t1"
	endst
	static
		var f2="t2"
		class u
		endcl
	endst
	var j=9
	class jk
		function p()
			Stdio.println(j)
		endfu
	endcl
endcl
var poi=yo()
poi.j="poi"
var hhh=poi.jk()
hhh.p()
Stdio.println(poi.j)
try
	Stdio.println(yo.j)
catch e
	Stdio.println(e)
endtr
Stdio.println(yo.f)
Stdio.println(yo.f2)
Stdio.println(yo.u.." "..poi.u)
Stdio.println(poi.jk.." "..yo().jk)
System.system("pause")
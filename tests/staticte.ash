using Stdio.ash
using System.ash

class a
	static
		class aa
			static
				var v="aa"
			endst
		endcl
	endst
endcl
class b:a.aa
endcl
Stdio.println(b.v)
b.v="bb"
Stdio.println(b().v)
System.system("pause")
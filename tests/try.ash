using Stdio.ash
using System.ash

function t
	class p
		static
			var s="message"
		endst
	endcl
	throw p
endfu
try
	t()
catch e
	Stdio.println(e.s)
	System.system("pause")
endtr
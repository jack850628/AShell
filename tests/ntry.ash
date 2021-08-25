using Stdio.ash
using Thread.ash

var e="poi"
try 
	f
catch var e
	Stdio.println(e)
endtr
Stdio.println(e)

try 
	g
catch e
	Stdio.println(e)
endtr
Stdio.println(e)
Thread.pause()
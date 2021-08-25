using Stdio.ash
using Thread.ash

function COMBI(
	r
	,
	n
	) 
	var p=1
    for (var i = 1; i <= n; i = i + 1)
        p = p * (r - i + 1) / i
	endfo
    return p
endfu
Stdio.println(
	COMBI
		(
		/*9
		,*/
		36
		,
		9
		)
		)
function a(a,k,j)
	return "a"
endfu
Stdio.println(a(/**/1,2))
Thread.pause()
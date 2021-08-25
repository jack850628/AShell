using Stdio.ash
using System.ash

class kp
	var l[10]
	function _inst_()
		/*var l[10]
		this.l=l*/
		l[5]=10
	endfu
endcl
function m(l,kk,i)
	#var i={{1,2},{3,4},{5,6}}
	Stdio.println(i[1][0][1])
	var j[2][3]
	var k={1,2,3,4,5,6}
	for (var i=0,v=0;i<2;i=i+1)
		for (var x=0;x<3;x=x+1,v=v+1)
			j[i][x]=k[v]
		endfo
	endfo
	for (var i=0,v=0;i<2;i=i+1)
		for (var x=0;x<3;x=x+1,v=v+1)
			Stdio.println(j[i][x])
		endfo
	endfo
	var a[3]
	a[1]={7,8,9}
	Stdio.println("a[1][1]:"..a[1][1])
	var as=kp()
	Stdio.println(as.l[5])
	as.l[1]="poi"
	Stdio.println(as.l[1])
	var aas=kp()
	Stdio.println(aas.l[1])
	var add=as+aas
	Stdio.println(add)
	System.system("pause")
endfu
m(,,{{{1,11},{2,22}},{{3,33},{4,44}},{{5,55},{6,66}}})
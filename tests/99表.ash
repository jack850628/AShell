using Thread
/*var i=1,j=1
function jp
	if i!=9
		return
	endif
	jj=j
	j=j+1
	return jj//j
endfu
for (;j<10;i=i==9?jp()+1:i+1)
	Stdio.printf("{0}*{1}={2:2}{3}",{i,j,i*j,i==9?"\n":"\t"})
endfo*/
var i=1,j=1
function jp
	if i==9
		var jj=j
		j=j+1
		return jj//j+1
	else
		return i+1
	endif
endfu
for (;j<10;i=jp())
	Stdio.printf("{0}x{1}={2:2}{3}",{i,j,i*j,i==9?"\n":"\t"})
endfo
Thread.pause()
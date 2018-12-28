using Thread;

var a[4][3][2];
var l=1;
for (var i=0;i<a.size();i=i+1)
	for (var j=0;j<a[i].size();j=j+1)
		for (var k=0;k<a[i][j].size();k=k+1)
			a[i][j][k]=l;
			l=l+1;
		endfo
	endfo
endfo
for (var i=0;i<a.size();i=i+1)
	for (var j=0;j<a[i].size();j=j+1)
		for (var k=0;k<a[i][j].size();k=k+1)
			Stdio.println(a[i][j][k]);
		endfo
	endfo
endfo
Thread.pause();
using foreach
using Thread

function leftRotation(arr,to)
	for (var r=0;r<to;r=r+1)
		var temp=arr[0]
		for (var i=1;i<arr.size();i=i+1)
			arr[i-1]=arr[i]
		endfo
		arr[arr.size()-1]=temp
	endfo
endfu

function p(a)
	Stdio.print(a..",")
endfu
var arr={1,2,3,4,5}
foreach(arr,p)
Stdio.println()
leftRotation(arr,3)
foreach(arr,p)
Stdio.println()
Thread.pause()

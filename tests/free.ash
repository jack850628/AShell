using Stdio.ash
using AShlib.ash
using System.ash

try
	class a
		static
			var i="poi"
		endst
	endcl
	AShlib.free(a)
	Stdio.println(a.i)
catch var e
	Stdio.println(e)
endtr
try
	var arr={{1,4,7},{{8,6,4,5},8},3},arr2=arr[1]
	AShlib.free(arr)
	Stdio.println(arr2[0])
	Stdio.println(arr[0])
catch var e
	Stdio.println(e)
endtr
try
	var arr={1,{{8,6,4,5},8},3},arr2=arr[1],arr3=arr2[0]
	AShlib.free(arr)
	Stdio.println(arr2[1])
	Stdio.println(arr[0])
	Stdio.println(arr3[1])
catch var e
	Stdio.println(e)
endtr
System.system("pause")
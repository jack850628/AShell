using foreach
using Thread
/**
�A���s�}�C���e���U�N�����k��
*/
function subArray(array,from,to)#���o�l�}�C
	if ![0<=from<array.size()]||![0<to<=array.size()]
		throw "�W�X�}�C�d��"
	endif
	var subArr[to-from]
	for (var i=from,j=0;i<to;i=i+1,j=j+1)
		subArr[j]=array[i]
	endfo
	return subArr
endfu
	
function moveRight(arr,to)
	var subarr1=subArray(arr,0,arr.size()-to),subarr2=subArray(arr,arr.size()-to,arr.size())
	var i=0
	for (j=0;i<subarr2.size();i=i+1,j=j+1)
		arr[i]=subarr2[j]
	endfo
	for (j=0;i<arr.size();i=i+1,j=j+1)
		arr[i]=subarr1[j]
	endfo
endfu
function p(a)
	Stdio.print(a..",")
endfu

var arr={1,2,3,4,5,6,7,8},LeftTo=6
Stdio.println("�V�k��"..LeftTo.."��")
foreach(arr,p)
moveRight(arr,LeftTo)
Stdio.println()
foreach(arr,p)
Stdio.println()
Thread.pause()
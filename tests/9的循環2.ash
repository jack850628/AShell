/**
*1~30位排成一圈，每隔9位就剃除，剃除掉的位置由下一位補上，要剃除掉15位
**/
using Thread
using LinkList

var a = LinkList()
for (var i = 1; i <= 30;i=i+1)
	a.add(i)
endfo

var i = 8, c = 0, l#因為陣列從0開始，8就代表第9位

while c != 15
	l = a.size()#記下當下陣列的長度
	var offset = 0#紀錄已經從陣列中去除掉幾個
	do
		Stdio.println(a.get(i - offset))
		a.remove(i - offset)
		offset = offset + 1
		c = c + 1
		i = i + 9
	dwhile (i - offset) < a.size()
	i = i % l#找出下一個第九位在循環後的位置
endwh
	
Thread.pause()

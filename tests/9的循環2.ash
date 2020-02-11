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
	Stdio.println(a.get(i))
	a.remove(i)
	i = (i + 8) % a.size()#+8也就是-1+9
	c = c + 1
endwh
	
Thread.pause()

/**
*1~30位排成一圈，每隔9位就剃除，剃除掉的位置由下一位補上，要剃除掉15位
**/
using Thread
using LinkList

a = LinkList()
for (var i = 1; i <= 30;i=i+1)
	a.add(i)
endfo

b = LinkList()#紀錄要被剃除的index

function f()#實施剃除
	var i = 0
	while b.size() != 0
		a.remove(b.get(0) - i)
		b.remove(0)
		i = i+1
	endwh
endfu

for (var i = 8, j = 0;j != 15; j = j+1)
	Stdio.println(a.get(i))
	b.add(i)
	if (i + 9) > a.size()
		i = (i + 9) % a.size()
		f()
	else
		i = i + 9
	endif
endfo
	
Thread.pause()

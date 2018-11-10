using Stdio.ash
using Thread.ash
using Threading.ash

var t=Threading()
var k=Threading()
function a()
	#call ioo
	t.wait()
	for (var i=0;i<50;i=i+1)
		Stdio.println("a:"..i)
		Thread.sleep(100)
	endfo
	k.notify()
endfu
t.start(a)
#t.wait()
for (var i=0;i<20;i=i+1)
	Stdio.println("main:"..i)
	Thread.sleep(100)
endfo
function poi
	t.notify()
	k.wait()
	Stdio.println("end")
	Thread.pause()
endfu
k.start(poi)
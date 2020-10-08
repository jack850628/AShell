/*
* 一瓶酒精1元，用完後兩個空瓶可跟店家換一瓶酒精，問：你有20元，最多可以買到幾瓶酒精？
*/
using Thread

var a = 20, b = 0, c = 0
while a != 0
	b = b + a
	a = a + c
	c = a %% 2
	a = a // 2
endwh
Stdio.println(b + "瓶")
Thread.pause()
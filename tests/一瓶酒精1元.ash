/*
* �@�~�s��1���A�Χ����ӪŲ~�i�򩱮a���@�~�s��A�ݡG�A��20���A�̦h�i�H�R��X�~�s��H
*/
using Thread

var a = 20, b = 0, c = 0
while a != 0
	b = b + a
	a = a + c
	c = a %% 2
	a = a // 2
endwh
Stdio.println(b + "�~")
Thread.pause()
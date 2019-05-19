#用於AShell內建Arral的foreach
function foreach(array,func)
	for (var i=0;i<array.size();i=i+1)
		func(array[i])
	endfo
endfu
#用於class建立的List的foreach，需要實作size()與get()才能使用
function foreach_c(array,func)
	for (var i=0;i<array.size();i=i+1)
		func(array.get(i))
	endfo
endfu
using System;
using Thread;
using Threading;

function f
    for (var i=0;i<1000;i=i+1)
	    Stdio.printf("i={0}\n",{i});
	endfo
endfu
Threading().start(f);
Thread.sleep(1000);
Stdio.println("finish");
System.finish();
Stdio.println("yee");
Thread.pause()
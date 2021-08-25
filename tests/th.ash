using Stdio.ash
using System.ash
using Threading.ash

class th:Threading
	function start()
		super.start(this.run)
		#npcall poi
	endfu
	function run()
		for (var i=0;i<10;i=i+1)
			Stdio.println(i)
		endfo
		System.system("pause")
	endfu
endcl
th().start()
for (var i=0;i<10;i=i+1)
			Stdio.println(i.."?")
endfo
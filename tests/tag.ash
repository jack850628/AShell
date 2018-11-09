using Stdio.ash
using Thread.ash

function main()
	var i=0
	:tag
	Stdio.println(i)
	i=i+1
	if i<5
		goto tag
	endif
	function mmain()
		var i=0
		:tag
		Stdio.println(i)
		i=i+1
		if i<5
			goto tag
		endif
		function mmmain()
			var i=0
			:tag
			Stdio.println(i)
			i=i+1
			if i<5
				goto tag
			endif
		endfu
		mmmain()
	endfu
	mmain()
	Thread.pause()
endfu
main()
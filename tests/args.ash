using Stdio.ash
using System.ash

function main(args)
	if args!=null
		for (var i=0;i<args.size();i=i+1)
			Stdio.println(args[i])
		endfo
	endif
	System.system("pause")
endfu
main(System.getArgs())
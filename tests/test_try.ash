using Stdio.ash
using System.ash


function main()
	try
		for (var i=0;i<2;i=i+1)
			try
				if true
					class E
						function _inst_(name)
							this.name=name
						endfu
						var name
					endcl
					throw E("test")
				endif
				Stdio.println("jojo")
			catch err
				Stdio.println(err.name)
			finally
				throw "wwww"
				Stdio.println("yee")
			endtr
		endfo
	catch e
		Stdio.println(e)
	finally
		Stdio.println("Á`¬O°õ¦æ")
	endtr
	#throw ~E("YEE")
	System.system("pause")
endfu
main()
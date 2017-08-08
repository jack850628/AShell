class Math
	static
		var JAVA_CLASS="AShell.Native_Class.AShell_Math"
		#轉整數
		function toint(arg)
			arg=(arg//1)
			return arg
		endfu
		#四捨五入
		function round(arg)
			arg=(arg+0.5)
			return base.toint(arg)
		endfu
		#取絕對值
		function abs(arg)
			if arg<0
				return (arg*-1)
			else
				return arg
			endif
		endfu
		#取亂數
		native rand(JAVA_CLASS)
		#取最大值
		function max(ar1,ar2)
			if ar1>ar2
				return ar1
			elseif ar1<ar2
				return ar2
			else
				return ar1
			endif
		endfu
		#取最小值
		function min(ar1,ar2)
			if ar1<ar2
				return ar1
			elseif ar1>ar2
				return ar2
			else
				return ar1
			endif
		endfu
		#取費氏係數
		function fid(n,curr=0,next=1)
			if n==0
				return curr
			else
				return fid((n-1),next,(curr+next))
			endif
		endfu
		#取次方
		function pow(ar1,ar2)
			if ar2<0
				return "error!"
			elseif ar2==0
				return 1
			elseif ar2==1
				return ar1
			else
				return (ar1*pow(ar1,(ar2-1)))
			endif
		endfu
		function sqrt(a,b)
			return a**(1/b)
		endfu
		#取圓周率
		function getPI()
			return 3.14
		endfu
		/*#階乘
		function factorial(n)
			if n==0
				return 1
			endif
			if n<0
				return n*factorial((n*-1)-1)
			else
				return n*factorial(n-1)
			endif
		endfu*/
	endst
endcl
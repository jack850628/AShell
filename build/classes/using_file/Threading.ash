class Threading
	static
		var JAVA_CLASS="AShell.Native_Class.AShell_Threading"
		native notifyall(JAVA_CLASS)
	endst
	function _inst_(run)
		if run!=null
			this.run=run
		endif
	endfu
	native _start(JAVA_CLASS)
	native _wait(JAVA_CLASS)
	native notify(JAVA_CLASS)
	function wait(t)
		_wait(Type.toDigital(t))
	endfu
	/*
	#因為複寫功能並不完善所以先註解掉
	function start()
		_start()
	endfu
	function start_for_argrun(run)
		if run!=null
			this.run=run
		endif
		_start()
	endfu
	function run()
	endfu*/
	function start(run)
		if run!=null
			this.run=run
		endif
		_start()
	endfu
	var run
endcl 
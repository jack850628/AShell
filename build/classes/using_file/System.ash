class System
	static
		var JAVA_CLASS="AShell.Native_Class.AShell_System"
		#結束批次執行
		native finish(JAVA_CLASS)
		#呼叫終端機
		native system(JAVA_CLASS)
		#切換目錄
		native setPath(JAVA_CLASS)
		#取得目錄
		native getPath(JAVA_CLASS)
		#取得腳本檔案所在目錄
		native getScriptFilePath(JAVA_CLASS)
		#取得啟動時的參數
		native getArgs(JAVA_CLASS)
	endst
endcl
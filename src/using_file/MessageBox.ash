class MessageBox
	static
		var java_class="AShell.Native_Class.AShell_MessageBox"
		/*訊息方塊
		showMessageBox(內容)
		showMessageBox(標題,內容,提示圖示樣式)
		showMessageBox(標題,內容,提示圖示樣式,自訂圖示(需要絕對路徑))
		*/
		native showMessageBox(java_class)
		/*確認方塊
		showConfirmBox(標題,內容,按鈕樣式,提示圖示樣式)
		showConfirmBox(標題,內容,按鈕樣式,提示圖示樣式,自訂圖示(需要絕對路徑))
		*/
		native showConfirmBox(java_class)
		/*輸入方塊
		showInputBox(內容)
		showInputBox(標題,內容,提示圖示樣式)
		showInputBox(標題,內容,提示圖示樣式,自訂圖示(需要絕對路徑),{下拉式選單內容(陣列)},選單預設選項)
		*/
		native showInputBox(java_class)
		/*選擇方塊
		showOptionBox(標題,內容,按鈕樣式,提示圖示樣式,自訂圖示(需要絕對路徑),{按鈕文字(陣列，一個元素代表一個按鈕)},按鈕預設選項)
		*/
		native showOptionBox(java_class)
	endst
	static#提示圖示樣式
		var ERROR_MESSAGE=0
		var INFORMATION_MESSAGE=1
		var WARNING_MESSAGE=2
		var QUESTION_MESSAGE=3
		var PLAIN_MESSAGE=-1#無圖示樣式
	endst
	static#按鈕樣式
		var DEFAULT_OPTION=-1
		var YES_NO_OPTION=0
		var YES_NO_CANCEL_OPTION=1
		var OK_CANCEL_OPTION=2
	endst
endcl
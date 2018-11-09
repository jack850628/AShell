using MessageBox
using Threading

function a()
	MessageBox.showMessageBox(MessageBox.showOptionBox(1234,5678,MessageBox.DEFAULT_OPTION,MessageBox.WARNING_MESSAGE,/*"D:\\Users\\·z­×\\Desktop\\Âø¹Ï\\529503_660564057299896_863782177_n.jpg"*/,{null.."","true",false.."",Stdio.."",{}.."",Stdio.print,Stdio(),"87",(0/0).."",(9/0).."",-(9/0)..""},true))
endfu
for var i=0;i<20;i=i+1
	Threading().start(a)
	Threading().start(a)
	Threading().start(a)
	Threading().start(a)
endfo
MessageBox.showMessageBox(MessageBox.showInputBox(1234,5678,MessageBox.WARNING_MESSAGE,/*"D:\\Users\\·z­×\\Desktop\\Âø¹Ï\\529503_660564057299896_863782177_n.jpg"*/,{null.."","true",false.."",Stdio.."",{}.."",Stdio.print,Stdio(),"87",(0/0).."",(9/0).."",-(9/0)..""},true))
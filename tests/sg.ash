using Stdio
using Thread
var _Beep
try
	using Beep
	_Beep = Beep
catch var e
endtr
var Beep = _Beep
unvar _Beep

var a=8
var b=22
var c=4
var g1=(b//2)
var z="#"
var x="W",x2="A"
var fi="!"
var ps=" "
var gn[a][b]
var i=0,j=0,a1=g1,a2=(g1-1),a3=(g1+1),ed2=0,ed=0,gk=((b*c)-(c*2))
var p
var f

function fire(d)
	var pf=0
	if d==(a-3)
		f=a1
	endif
	if d==-1
		pf=1
	elseif gn[d][f]==z
		ed=(ed+1),pf=1
	endif
	if d!=-1
		if pf!=1
			gn[d][f]=fi
		else
			gn[d][f]=ps
		endif
	endif
	Stdio.clear()
	updata()
	if d!=-1
		gn[d][f]=ps
	endif
	if pf==0
		fire((d-1))
	endif
	if ed==gk&&ed2==0
		ed2=1
		end()
	endif
endfu
function rl()
			gn[(a-2)][a1]=ps
			if p==","
				a1=(a1-1),a2=(a2-1)
				gn[(a-2)][a1]=x2
				gn[(a-1)][a3]=ps
				a3=(a3-1)
				gn[(a-1)][a2]=x
			else
				a1=(a1+1),a3=(a3+1)
				gn[(a-2)][a1]=x2
				gn[(a-1)][a2]=ps
				a2=(a2+1)
				gn[(a-1)][a3]=x
			endif
			Stdio.clear()
			updata()
endfu
function updata()
	var i=0,j
	while i<a
		j=0
		while j<b
			Stdio.print(gn[i][j])
			j=(j+1)
		endwh
		Stdio.print("\n")
		i=(i+1)
	endwh
	Stdio.print("按 <:左移 >:右移 Z:射擊 X:重新遊戲 C:結束遊戲")
endfu
function end()
	Stdio.clear()
	var i=0,j
	var te=(a//2-1)
	while i<a
		j=0
		if i==te
			var te2=(b//2)
			while j<b
				if j==(te2-2)
					Stdio.print("遊")
				elseif j==(te2-1)
					Stdio.print("戲")
				elseif j==te2
					Stdio.print("結")
				elseif j==(te2+1)
					Stdio.print("束")
				else
					Stdio.print( gn[i][j])
				endif
				j=(j+1)
			endwh
			if Beep != null
				Beep.beep(800,300)
			endif
		else
			while j<b
				Stdio.print(gn[i][j])
				j=(j+1)
			endwh
		endif
		Stdio.print( "\n")
		i=(i+1)
	endwh
	Stdio.print("按 X:重新遊戲 C:結束遊戲\a")
endfu
function start()
	Stdio.print("請稍後...\n")
	var i=0,j
	while i<a
		j=0
		while j<b
			if j==0||j==(b-1)
				gn[i][j]=ps
			elseif i<c
				gn[i][j]=z
			elseif i==(a-2)&&j==g1
				gn[i][j]=x2
			elseif i==(a-1)&&j>=(g1-1)&&j<=(g1+1)
				gn[i][j]=x
			else
				gn[i][j]=ps
			endif
			j=(j+1)
		endwh
		i=(i+1)
	endwh
	Stdio.clear()
	updata()
endfu

Stdio.clear()
start()
while 1==1
	p=Stdio.getch()
	if ed2==0&&p==","&&a1>1||ed2==0&&p=="."&&a1<(b-2)
		rl()
	endif
	if p=="z"&&ed2==0
		fire((a-3))
	endif
	if p=="x"
		a1=g1
		a2=(g1-1)
		a3=(g1+1)
		ed=0
		ed2=0
		Stdio.clear()
		if Beep != null
			Beep.beep(800,300)
		endif
		start()
	endif
	if p=="c"
		break
	endif
endwh
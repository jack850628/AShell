 using Type.ash

/*字串處理函數宣告*/
class String
	static
		var JAVA_CLASS="AShell.Native_Class.AShell_String"
		#取得字串長度
		native strlen(JAVA_CLASS)
		#取得字串中的一個字元
		native charAt(JAVA_CLASS)
		#將字串以某個字元做分割
		function split(str1,str2)
			if str2==""||str2==null
				return split_is_enpty(str1)
			else
				return split_no_enpty(str1,str2)
			endif
		endfu
		#當str2不是""或null時的分割方式
		function split_no_enpty(str1,str2)
			var str1_len=strlen(str1),index=1
			for var i=0;i<str1_len;i=i+1#取得需要產生的陣列長度
				if str1[i]==str2
					index=index+1
				endif
			endfo
			var arr[index],buffer
			arr[0]=""
			index=0
			for var i=0;i<str1_len;i=i+1
				buffer=str1[i]
				if buffer==str2
					index=index+1
					arr[index]=""
				else
					arr[index]=arr[index]..buffer
				endif
			endfo
			return arr
		endfu#當str2是""或null時的分割方式
		function split_is_enpty(str1)
			var str1_len=strlen(str1)
			var arr[str1_len]
			for var i=0;i<str1_len;i=i+1
				arr[i]=str1[i]
			endfo
			return arr
		endfu
		#判斷字串是否以某個字串為開始
		function startsWith(str1,str2)
			var str1_len=strlen(str1),str2_len=strlen(str2)
			if str1_len<str2_len
				return false
			endif
			for var i=0;i<str2_len;i=i+1
				if str1[i]!=str2[i]
					return false
				endif
			endfo
			return true
		endfu
		#判斷字串是否以某個字串為結束
		function endsWith(str1,str2)
			var str1_len=strlen(str1),str2_len=strlen(str2)
			if str1_len<str2_len
				return false
			endif
			for var i=0,j=str1_len-str2_len;i<str2_len;i=i+1,j=j+1
				if str1[j]!=str2[i]
					return false
				endif
			endfo
			return true
		endfu
		#格式化字串，使用{}語法，{0}代表第0個參數，{5:0}代表第0個參數至少5格並靠右，{-5:0}代表第0個參數至少5格並靠左
		function format(form,arr)
			var buffer=""
			var temp
			var length=strlen(form)
			for var i=0;i<length;i=i+1
				temp=form[i]
				if temp=="{"
					if form[i+1]=="{"
						i=i+1
						buffer=buffer.."{"
						continue
					endif
					var index="",Str_Len=""
					while true
						i=i+1
						temp=form[i]
						if temp=="}"
							break
						elseif temp==":"
							Str_Len=index
							index=""
						else
							index=index+temp
						endif
					endwh
					index=Type.toDigital(index)
					Str_Len=Type.toDigital(Str_Len)
					if index>arr.size()||index<0
						throw "超出引數範圍"
					endif
					buffer=buffer..Align(arr[index],Str_Len)
				elseif temp=="}"
					if form[i+1]=="}"
						i=i+1
						buffer=buffer.."}"
					else
						throw "大括號不對稱"
					endif
				else
					buffer=buffer..temp
				endif
			endfo
			return buffer
		endfu
		#字串對齊
		function Align(Str,Len)
			var Str_Len=String.strlen(Type.toString(Str))
			var Front=Len>=0#是否為前面空白
			if !Front
				Len=-Len
			endif
			if Str_Len>=Len
				return Str
			elseif Front
				return (" "*(Len-Str_Len))..Str
			else
				return Str..(" "*(Len-Str_Len))
			endif
		endfu
		#KMP字串搜尋法
		function KmpSearch(s,p)
			var i=j=0
			var slen=strlen(s)
			var plen=strlen(p)
			var next[plen]
			GetNext(p,next)
			while i<slen&&j<plen
				if j==-1
					j=j+1
					i=i+1
				elseif s[i]==p[j]
					j=j+1
					i=i+1
				else
					j=next[j]
				endif
			endwh
			if j==plen
				return i-j
			else
				return -1
			endif
		endfu
		#取得失配表
		/*以p=abcdabd為例
		一開始k=-1 j=0
		k=-1所以k++ j++然後因為p[j]!=p[k] => p[1]!=p[0](b!=a)所以next[j]=k => next[1]=0
		接下來k=0 j=1因為p[j]!=p[k](b!=a)所以進else k=next[k] => k=next[0] => k=-1
		然後 k==-1所以k++ j++ 然後因為p[j]!=p[k] => p[2]!=p[0](c!=a)所以next[j]=k => next[2]=0
		然後 k=0 j=2 因為p[j]!=p[k](c!=a)所以進else k=next[k] => k=next[0] => k=-1
		然後 k==-1所以k++ j++ 然後因為p[j]!=p[k] => p[3]!=p[0](d!=a)所以next[j]=k => next[3]=0
		然後 k=0 j=3 因為p[j]!=p[k](d!=a)所以進else k=next[k] => k=next[0] => k=-1
		然後 k==-1所以k++ j++ 然後因為p[j]==p[k] => p[4]==p[0](a==a)所以next[j]=next[k] => next[4]=next[0] => next[4]=-1
		然後 k=0 j=4 因為p[j]==p[k](a==a)所以 k++ j++ 然後因為p[j]==p[k] => p[5]==p[1](b==b)所以next[j]=next[k] => next[5]=next[1] => next[5]=0
		然後 k=1 j=5 因為p[j]==p[k](b==b)所以 k++ j++ 然後因為p[j]!=p[k] => p[6]!=p[2](c!=d)所以next[j]=k => next[6]=2
		然後 k=2 j=6 但是因為while j<plen-1 => while 6<7-1 所以跳出
		則next為{-1,0,0,0,-1,0,2}
		*/
		function GetNext(p,next)
			var plen=String.strlen(p)
			next[0]=-1
			var k=-1
			var j=0
			while j<plen-1
				if k==-1
					k=k+1
					j=j+1
					if p[j]!=p[k]
						next[j]=k
					else
						#因為不能出現p[j] = p[ next[j ]]，所以當出現時需要繼續遞歸，k = next[k] = next[next[k]] 
						next[j]=next[k]
					endif
				elseif p[j]==p[k]
					k=k+1
					j=j+1
					if p[j]!=p[k]
						next[j]=k
					else
						#因為不能出現p[j] = p[ next[j ]]，所以當出現時需要繼續遞歸，k = next[k] = next[next[k]] 
						next[j]=next[k]
					endif
				else
					k=next[k]
				endif
			endwh
		endfu
	endst
endcl
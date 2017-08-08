 using Type.ash

/*�r��B�z��ƫŧi*/
class String
	static
		var JAVA_CLASS="AShell.Native_Class.AShell_String"
		#���o�r�����
		native strlen(JAVA_CLASS)
		#���o�r�ꤤ���@�Ӧr��
		native charAt(JAVA_CLASS)
		#�N�r��H�Y�Ӧr��������
		function split(str1,str2)
			if str2==""||str2==null
				return split_is_enpty(str1)
			else
				return split_no_enpty(str1,str2)
			endif
		endfu
		#��str2���O""��null�ɪ����Τ覡
		function split_no_enpty(str1,str2)
			var str1_len=strlen(str1),index=1
			for var i=0;i<str1_len;i=i+1#���o�ݭn���ͪ��}�C����
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
		endfu#��str2�O""��null�ɪ����Τ覡
		function split_is_enpty(str1)
			var str1_len=strlen(str1)
			var arr[str1_len]
			for var i=0;i<str1_len;i=i+1
				arr[i]=str1[i]
			endfo
			return arr
		endfu
		#�P�_�r��O�_�H�Y�Ӧr�ꬰ�}�l
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
		#�P�_�r��O�_�H�Y�Ӧr�ꬰ����
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
		#�榡�Ʀr��A�ϥ�{}�y�k�A{0}�N���0�ӰѼơA{5:0}�N���0�ӰѼƦܤ�5��þa�k�A{-5:0}�N���0�ӰѼƦܤ�5��þa��
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
						throw "�W�X�޼ƽd��"
					endif
					buffer=buffer..Align(arr[index],Str_Len)
				elseif temp=="}"
					if form[i+1]=="}"
						i=i+1
						buffer=buffer.."}"
					else
						throw "�j�A�������"
					endif
				else
					buffer=buffer..temp
				endif
			endfo
			return buffer
		endfu
		#�r����
		function Align(Str,Len)
			var Str_Len=String.strlen(Type.toString(Str))
			var Front=Len>=0#�O�_���e���ť�
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
		#KMP�r��j�M�k
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
		#���o���t��
		/*�Hp=abcdabd����
		�@�}�lk=-1 j=0
		k=-1�ҥHk++ j++�M��]��p[j]!=p[k] => p[1]!=p[0](b!=a)�ҥHnext[j]=k => next[1]=0
		���U��k=0 j=1�]��p[j]!=p[k](b!=a)�ҥH�ielse k=next[k] => k=next[0] => k=-1
		�M�� k==-1�ҥHk++ j++ �M��]��p[j]!=p[k] => p[2]!=p[0](c!=a)�ҥHnext[j]=k => next[2]=0
		�M�� k=0 j=2 �]��p[j]!=p[k](c!=a)�ҥH�ielse k=next[k] => k=next[0] => k=-1
		�M�� k==-1�ҥHk++ j++ �M��]��p[j]!=p[k] => p[3]!=p[0](d!=a)�ҥHnext[j]=k => next[3]=0
		�M�� k=0 j=3 �]��p[j]!=p[k](d!=a)�ҥH�ielse k=next[k] => k=next[0] => k=-1
		�M�� k==-1�ҥHk++ j++ �M��]��p[j]==p[k] => p[4]==p[0](a==a)�ҥHnext[j]=next[k] => next[4]=next[0] => next[4]=-1
		�M�� k=0 j=4 �]��p[j]==p[k](a==a)�ҥH k++ j++ �M��]��p[j]==p[k] => p[5]==p[1](b==b)�ҥHnext[j]=next[k] => next[5]=next[1] => next[5]=0
		�M�� k=1 j=5 �]��p[j]==p[k](b==b)�ҥH k++ j++ �M��]��p[j]!=p[k] => p[6]!=p[2](c!=d)�ҥHnext[j]=k => next[6]=2
		�M�� k=2 j=6 ���O�]��while j<plen-1 => while 6<7-1 �ҥH���X
		�hnext��{-1,0,0,0,-1,0,2}
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
						#�]������X�{p[j] = p[ next[j ]]�A�ҥH��X�{�ɻݭn�~���k�Ak = next[k] = next[next[k]] 
						next[j]=next[k]
					endif
				elseif p[j]==p[k]
					k=k+1
					j=j+1
					if p[j]!=p[k]
						next[j]=k
					else
						#�]������X�{p[j] = p[ next[j ]]�A�ҥH��X�{�ɻݭn�~���k�Ak = next[k] = next[next[k]] 
						next[j]=next[k]
					endif
				else
					k=next[k]
				endif
			endwh
		endfu
	endst
endcl
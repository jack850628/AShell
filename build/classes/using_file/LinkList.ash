#連結清單
using AShlib.ash

class LinkList
	var list
	class Obj
		var obj
		var next
		function _inst_(obj)
			this.obj=obj
		endfu
		function size(size)
			if next==null
				return size
			else
				return next.size(size+1)
			endif
		endfu
		function remove(index,obj)
			if index==0
				obj.next=this.next
				AShlib.free(this)
			else
				next.remove(index-1,this)
			endif
		endfu
	endcl
	function _inst_(obj)
		if obj!=null
			list=Obj(obj)
		endif
	endfu
	
	function add(obj)
		if list==null
			list=Obj(obj)
		elseif list.next==null
			list.next=Obj(obj)
		else
			_add(obj,list.next)
		endif
	endfu
	function _add(obj,next)
		if next.next==null
			next.next=Obj(obj)
		else
			_add(obj,next.next)
		endif
	endfu
	
	function get(index)
		if index==0
			return list.obj
		else
			return _get(index-1,list.next)
		endif
	endfu
	function _get(index,next)
		if index==0
			return next.obj
		else
			return _get(index-1,next.next)
		endif
	endfu
	
	function set(index,obj)
		if index==0
			list.obj=obj
		else
			_set(index-1,obj,list.next)
		endif
	endfu
	function _set(index,obj,next)
		if index==0
			next.obj=obj
		else
			_set(index-1,obj,next.next)
		endif
	endfu
	
	function size()
		if list==null
			return 0
		else
			return list.size(1)
		endif
	endfu
	function remove(index)
		if index==0
			list=list.next
		else
			list.next.remove(index-1,list)
		endif
	endfu
	function clear()
		var self=list,temp
		list=null
		while self.next!=null
			temp=self
			self=self.next
			AShlib.free(temp)
		endwh
	endfu
endcl 
using System

#萬年曆 AShell語言版
function main()
    var year, month, first_space, feb_days 
 
    Stdio.print("請輸入西元") 
    year=Stdio.inputNumber()
 
    Stdio.print("請輸入月份") 
    month=Stdio.inputNumber()
 
 
    if month<=12&& month>=1
		first_space=1
        for (var q=0;q<(year-1900);q=q+1)
        
            if q%%4==0&& q%%100!=0|| q%%400==100
            
                 first_space=(first_space+366)%%7 
            else
            
                first_space=(first_space+365)%%7 
            endif
        endfo
 
        #計算2月天數
        if year%%4==0&& year%%100!=0|| year%%400==0
        
            feb_days =29 
        else
        
            feb_days =28 
        endif
 
 
 
        var space={0,first_space,0,0,0,0,0,0,0,0,0,0,0} 
        var days={0,31,feb_days,31,30,31,30,31,31,30,31,30,31} 
 
 
		var i
        for (i=1;i<month;i=i+1)
        
            space[i+1]=(space[i]+days[i])%%7 
        endfo
 
 
 
        Stdio.printf("\t\t{0}年{1}月\n",{year,month}) 
        Stdio.print("\t日  一  二  三  四  五  六 \n\t") 
        for (var m=1;m<=space[i];m=m+1)
            Stdio.print("    ") 
        endfo
 
        for (var n=1;n<=days[i];n=n+1)
        
            Stdio.printf("{2:0}  ",{n}) 
 
            if n%%7==(7-space[i])%%7
            
                Stdio.print("\n\t") 
            endif
        endfo
 
        Stdio.println() 
     endif
     System.system("pause") 
endfu
main()
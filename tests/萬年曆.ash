using System

#�U�~�� AShell�y����
function main()
    var year, month, first_space, feb_days 
 
    Stdio.print("�п�J�褸") 
    year=Stdio.inputNumber()
 
    Stdio.print("�п�J���") 
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
 
        #�p��2��Ѽ�
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
 
 
 
        Stdio.printf("\t\t{0}�~{1}��\n",{year,month}) 
        Stdio.print("\t��  �@  �G  �T  �|  ��  �� \n\t") 
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
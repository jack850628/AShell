using Stdio.ash
using System.ash

var a={{1,{2,3},{{4,5},{6,7.."8¤À"}},87},8,9}
Stdio.println("a[0][1][0]="..a[0][1][0])
Stdio.println("a[0][2][1][1]="..a[0][2][1][1])
Stdio.println("a[0][2][0][0]="..a[0][2][0][0])
Stdio.println("a[1]="..a[1])
Stdio.println("a[0][1][0]="..a[0][1][0])
Stdio.println("a.size()="..a.size())
Stdio.println("a[0].size()="..a[0].size())
Stdio.println("a[0][2].size()="..a[0][2].size())
Stdio.println({,{}}[1][0])
System.system("pause")
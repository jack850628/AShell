#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include <string.h>
//#include<iostream>
//using namespace std;
int main(int argc, char *argv[]){
	char com[_MAX_PATH],Current_Path[_MAX_PATH]/*,temp[100]*/;
	//system("cd ");
	/*char *path=(char*)malloc(sizeof(char*)*sizeof(argv[0]));
	
	int i,k=0,t=0;
	for(i=0;argv[0][i]!='\0';i++,k++){
		if(argv[0][i]!='\\'){
			temp[k]=argv[0][i]; 
		}else{
			for(k=0;t<i;t++,k++){
				path[t]=temp[k];
				//printf("%c",path[t]);
			}
			path[t]='\\';
			//printf("%c",path[t]);
			t++;
			k=-1;
		}
	}
	path[t]='\0';*/
	if(argc==1){//如果沒有參數 
		char path[_MAX_PATH];
		getcwd(path,_MAX_PATH);//取得現在執行之路徑 
		sprintf(com,"java call_AShell false \"%s\"",path);
	}else if(argv[1][0]=='.'){//如果呼叫執行檔時有加參數 且 參數開頭是. 
		char new_path[_MAX_PATH];
		getcwd(Current_Path,_MAX_PATH);//取得現在執行之路徑 
		sprintf(new_path,"%s%s",Current_Path,argv[1]+1);//將現在執行之路徑與除了.以後的參數相連，放置新路徑變數 
		//printf("%s",com);
		sprintf(com,"java call_AShell true \"%s\"",new_path);
	}else if(strlen(argv[1])<2||argv[1][1]!=':'){//非腳本檔的絕對路徑，如果呼叫執行檔時有加參數 且 參數開頭第二個字元不是:，這是因為路徑參數一定要絕對路徑且Windows絕對路徑開頭第二個字元一定是:，如果是在Linux底下時只需要判斷開頭字元是不是/ 
		char new_path[_MAX_PATH];
		getcwd(Current_Path,_MAX_PATH);//取得現在執行之路徑 
		sprintf(new_path,"%s\\%s",Current_Path,argv[1]);//將現在執行之路徑與參數相連，放置新路徑變數 
		//printf("%s",com);
		sprintf(com,"java call_AShell true \"%s\"",new_path);
	}else{//參數是腳本檔的絕對路徑
		sprintf(com,"java call_AShell true \"%s\"",argv[1]);
	}
	if(argc>2){//將AShell腳本參數都加上引號，因為參數進到用C語言寫的啟動器會被去掉，但是接下來要用System函數以命令方式啟動用java寫的AShell所以如果沒加回引號像是原本參數"poi yee"就會變成poi yee一個參數變兩個參數 
		int index=0;
		while(1){//取得字串最末端的結束字元位置 
			if(com[index]=='\0'){
				//printf("LOG index=%d\n",index);
				break;
			}
			index++;
		}
		int i;
		//printf("LOG argc=%d\n",argc);
		for(i=2;i<argc;i++){
			com[index++]=' ';//如果是第一次做，那麼就具有把原本的字串結束字元以空格取代掉的功能 
			com[index++]='\"';
			int j;
			for(j=0;1;j++){
				if(argv[i][j]=='\0')
					break;
				com[index++]=argv[i][j];
			}
			//printf("LOG argv[%d]=%s\n",i,argv[i]);
			com[index++]='\"';
		}
		com[index]='\0';
		//printf("\n\"%s\"\n",com);
	} 
	//printf("\n%s\n",argv[0]);
	//printf("\n%s\n",path);
	chdir("C:\\AShell");//切換路徑至 C:\AShell
	//chdir(path);
	//system("cd");
	//printf("Log:%s\n",com);
	system(com);
	//system("pause");
	//free(path);
	return 0;
}

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
	if(argc==1){//�p�G�S���Ѽ� 
		char path[_MAX_PATH];
		getcwd(path,_MAX_PATH);//���o�{�b���椧���| 
		sprintf(com,"java call_AShell false \"%s\"",path);
	}else if(argv[1][0]=='.'){//�p�G�I�s�����ɮɦ��[�Ѽ� �B �Ѽƶ}�Y�O. 
		char new_path[_MAX_PATH];
		getcwd(Current_Path,_MAX_PATH);//���o�{�b���椧���| 
		sprintf(new_path,"%s%s",Current_Path,argv[1]+1);//�N�{�b���椧���|�P���F.�H�᪺�ѼƬ۳s�A��m�s���|�ܼ� 
		//printf("%s",com);
		sprintf(com,"java call_AShell true \"%s\"",new_path);
	}else if(strlen(argv[1])<2||argv[1][1]!=':'){//�D�}���ɪ�������|�A�p�G�I�s�����ɮɦ��[�Ѽ� �B �Ѽƶ}�Y�ĤG�Ӧr�����O:�A�o�O�]�����|�ѼƤ@�w�n������|�BWindows������|�}�Y�ĤG�Ӧr���@�w�O:�A�p�G�O�bLinux���U�ɥu�ݭn�P�_�}�Y�r���O���O/ 
		char new_path[_MAX_PATH];
		getcwd(Current_Path,_MAX_PATH);//���o�{�b���椧���| 
		sprintf(new_path,"%s\\%s",Current_Path,argv[1]);//�N�{�b���椧���|�P�ѼƬ۳s�A��m�s���|�ܼ� 
		//printf("%s",com);
		sprintf(com,"java call_AShell true \"%s\"",new_path);
	}else{//�ѼƬO�}���ɪ�������|
		sprintf(com,"java call_AShell true \"%s\"",argv[1]);
	}
	if(argc>2){//�NAShell�}���ѼƳ��[�W�޸��A�]���Ѽƶi���C�y���g���Ұʾ��|�Q�h���A���O���U�ӭn��System��ƥH�R�O�覡�Ұʥ�java�g��AShell�ҥH�p�G�S�[�^�޸����O�쥻�Ѽ�"poi yee"�N�|�ܦ�poi yee�@�ӰѼ��ܨ�ӰѼ� 
		int index=0;
		while(1){//���o�r��̥��ݪ������r����m 
			if(com[index]=='\0'){
				//printf("LOG index=%d\n",index);
				break;
			}
			index++;
		}
		int i;
		//printf("LOG argc=%d\n",argc);
		for(i=2;i<argc;i++){
			com[index++]=' ';//�p�G�O�Ĥ@�����A����N�㦳��쥻���r�굲���r���H�Ů���N�����\�� 
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
	chdir("C:\\AShell");//�������|�� C:\AShell
	//chdir(path);
	//system("cd");
	//printf("Log:%s\n",com);
	system(com);
	//system("pause");
	//free(path);
	return 0;
}

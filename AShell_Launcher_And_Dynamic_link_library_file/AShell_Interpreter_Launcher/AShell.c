#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>
#include<assert.h>

#include "parg.h" //https://github.com/jibsen/parg

char* strrep(const char *s1, const char *s2, const char *s3)
{
    if (!s1 || !s2 || !s3)
        return 0;
    size_t s1_len = strlen(s1);
    if (!s1_len)
        return (char *)s1;
    size_t s2_len = strlen(s2);
    if (!s2_len)
        return (char *)s1;

    /*
     * Two-pass approach: figure out how much space to allocate for
     * the new string, pre-allocate it, then perform replacement(s).
     */

    size_t count = 0;
    const char *p = s1;
    assert(s2_len); /* otherwise, strstr(s1,s2) will return s1. */
    do {
        p = strstr(p, s2);
        if (p) {
            p += s2_len;
            ++count;
        }
    } while (p);

    if (!count)
        return (char *)s1;

    /*
     * The following size arithmetic is extremely cautious, to guard
     * against size_t overflows.
     */
    assert(s1_len >= count * s2_len);
    assert(count);
    size_t s1_without_s2_len = s1_len - count * s2_len;
    size_t s3_len = strlen(s3);
    size_t newstr_len = s1_without_s2_len + count * s3_len;
    if (s3_len &&
        ((newstr_len <= s1_without_s2_len) || (newstr_len + 1 == 0)))
        /* Overflow. */
        return 0;
    
    char *newstr = (char *)malloc(newstr_len + 1); /* w/ terminator */
    if (!newstr)
        /* ENOMEM, but no good way to signal it. */
        return 0;
    
    char *dst = newstr;
    const char *start_substr = s1;
    size_t i;
    for (i = 0; i != count; ++i) {
        const char *end_substr = strstr(start_substr, s2);
        assert(end_substr);
        size_t substr_len = end_substr - start_substr;
        memcpy(dst, start_substr, substr_len);
        dst += substr_len;
        memcpy(dst, s3, s3_len);
        dst += s3_len;
        start_substr = end_substr + s2_len;
    }

    /* copy remainder of s1, including trailing '\0' */
    size_t remains = s1_len - (start_substr - s1) + 1;
    assert(dst + remains == newstr + newstr_len + 1);
    memcpy(dst, start_substr, remains);
    assert(strlen(newstr) == newstr_len);
    return newstr;
}

void runForScriptFile(char* command, char* path){
	char currentPath[_MAX_PATH];
	
	if(path[0]=='.'){//�p�G�I�s�����ɮɦ��[�Ѽ� �B �Ѽƶ}�Y�O. 
		char new_path[_MAX_PATH];
		getcwd(currentPath,_MAX_PATH);//���o�{�b���椧���| 
		sprintf(new_path,"%s%s",currentPath,path+1);//�N�{�b���椧���|�P���F.�H�᪺�ѼƬ۳s�A��m�s���|�ܼ� 
		//printf("%s",com);
		sprintf(command,"java call_AShell -f \"%s\"",new_path);
	}else if(strlen(path)<2||path[1]!=':'){//�D�}���ɪ�������|�A�p�G�I�s�����ɮɦ��[�Ѽ� �B �Ѽƶ}�Y�ĤG�Ӧr�����O:�A�o�O�]�����|�ѼƤ@�w�n������|�BWindows������|�}�Y�ĤG�Ӧr���@�w�O:�A�p�G�O�bLinux���U�ɥu�ݭn�P�_�}�Y�r���O���O/ 
		char new_path[_MAX_PATH];
		getcwd(currentPath,_MAX_PATH);//���o�{�b���椧���| 
		sprintf(new_path,"%s\\%s",currentPath,path);//�N�{�b���椧���|�P�ѼƬ۳s�A��m�s���|�ܼ� 
		//printf("%s",com);
		sprintf(command,"java call_AShell -f \"%s\"",new_path);
	}else{//�ѼƬO�}���ɪ�������|
		sprintf(command,"java call_AShell -f \"%s\"",path);
	}
}

int main(int argc, char *argv[]){
	const char* OPTSTRAIG = "ivf:e:";
	char com[_MAX_PATH]/*,temp[100]*/;
	
	struct parg_state ps;
	int c;

	parg_init(&ps);

	if(argc==1){//�p�G�S���Ѽ� 
		char path[_MAX_PATH];
		getcwd(path,_MAX_PATH);//���o�{�b���椧���| 
		sprintf(com,"java call_AShell -i \"%s\"",path);
	}else{
		int optend = parg_reorder(argc, argv, OPTSTRAIG, NULL);//�p�G�֤F�o��h�S���ﶵ���Ѽ�(nonoptions)�N�|�X�{�bcase 1: 
		while ((c = parg_getopt(&ps, optend, argv, OPTSTRAIG)) != -1) {
			switch (c) {
			case 'i':{
				char path[_MAX_PATH];
				getcwd(path,_MAX_PATH);//���o�{�b���椧���| 
				sprintf(com,"java call_AShell -i \"%s\"",path);
				break;
			}
			case 'f':{
				runForScriptFile(com,ps.optarg);
				break;
			}
			case 'e':
				sprintf(com,"java call_AShell -e \"%s\"",strrep(ps.optarg,"\"","\\\""));
				break;
			case 'v':
				sprintf(com,"java call_AShell -v");
				break;
			/*case '?':
				return 1;
				break;*/
			default:
				printf("AShell [-e cmd | -f file | -v | -i | file ] [arg ...]\n" \
				"-i             : �Ұʥ椬���R�O����\n" \ 
				"-e com         : �ѩR�O�CŪ���{���X\n" \
				"-f file | file : �q�ɮ�Ū���{���X\n" \ 
				"-v             : ������T\n");
				return 1;
				break;
			}
		}
		if(ps.optind == 1 && argc > 1){//ps.optind == 1�N�� �S�����󦳿ﶵ���Ѽ�,argc > 1�N���@�ӥH�W���S���ﶵ���Ѽ�(nonoptions) 
			runForScriptFile(com,argv[ps.optind]);
			ps.optind++;
		}
		if(ps.optind != argc){
			int index=0;
			while(1){//���o�r��̥��ݪ������r����m 
				if(com[index]=='\0'){
					//printf("LOG index=%d\n",index);
					break;
				}
				index++;
			}
			for (c = ps.optind; c < argc; ++c) {//�NAShell�}���ѼƳ��[�W�޸��A�]���Ѽƶi���C�y���g���Ұʾ��|�Q�h���A���O���U�ӭn��System��ƥH�R�O�覡�Ұʥ�java�g��AShell�ҥH�p�G�S�[�^�޸����O�쥻�Ѽ�"poi yee"�N�|�ܦ�poi 
				com[index++]=' ';//�p�G�O�Ĥ@�����A����N�㦳��쥻���r�굲���r���H�Ů���N�����\�� 
				com[index++]='\"';
				int j;
				for(j=0;1;j++){
					if(argv[c][j]=='\0')
						break;
					com[index++]=argv[c][j];
				}
				com[index++]='\"';
			}
			com[index]='\0';
		}
	}
	chdir("C:\\AShell");//�������|�� C:\AShell
	system(com);
	return 0;
}

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
	
	if(path[0]=='.'){//如果呼叫執行檔時有加參數 且 參數開頭是. 
		char new_path[_MAX_PATH];
		getcwd(currentPath,_MAX_PATH);//取得現在執行之路徑 
		sprintf(new_path,"%s%s",currentPath,path+1);//將現在執行之路徑與除了.以後的參數相連，放置新路徑變數 
		//printf("%s",com);
		sprintf(command,"java call_AShell -f \"%s\"",new_path);
	}else if(strlen(path)<2||path[1]!=':'){//非腳本檔的絕對路徑，如果呼叫執行檔時有加參數 且 參數開頭第二個字元不是:，這是因為路徑參數一定要絕對路徑且Windows絕對路徑開頭第二個字元一定是:，如果是在Linux底下時只需要判斷開頭字元是不是/ 
		char new_path[_MAX_PATH];
		getcwd(currentPath,_MAX_PATH);//取得現在執行之路徑 
		sprintf(new_path,"%s\\%s",currentPath,path);//將現在執行之路徑與參數相連，放置新路徑變數 
		//printf("%s",com);
		sprintf(command,"java call_AShell -f \"%s\"",new_path);
	}else{//參數是腳本檔的絕對路徑
		sprintf(command,"java call_AShell -f \"%s\"",path);
	}
}

int main(int argc, char *argv[]){
	const char* OPTSTRAIG = "ivf:e:";
	char com[_MAX_PATH]/*,temp[100]*/;
	
	struct parg_state ps;
	int c;

	parg_init(&ps);

	if(argc==1){//如果沒有參數 
		char path[_MAX_PATH];
		getcwd(path,_MAX_PATH);//取得現在執行之路徑 
		sprintf(com,"java call_AShell -i \"%s\"",path);
	}else{
		int optend = parg_reorder(argc, argv, OPTSTRAIG, NULL);//如果少了這行則沒有選項的參數(nonoptions)就會出現在case 1: 
		while ((c = parg_getopt(&ps, optend, argv, OPTSTRAIG)) != -1) {
			switch (c) {
			case 'i':{
				char path[_MAX_PATH];
				getcwd(path,_MAX_PATH);//取得現在執行之路徑 
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
				"-i             : 啟動交互式命令介面\n" \ 
				"-e com         : 由命令列讀取程式碼\n" \
				"-f file | file : 從檔案讀取程式碼\n" \ 
				"-v             : 版本資訊\n");
				return 1;
				break;
			}
		}
		if(ps.optind == 1 && argc > 1){//ps.optind == 1代表 沒有任何有選項的參數,argc > 1代表有一個以上的沒有選項的參數(nonoptions) 
			runForScriptFile(com,argv[ps.optind]);
			ps.optind++;
		}
		if(ps.optind != argc){
			int index=0;
			while(1){//取得字串最末端的結束字元位置 
				if(com[index]=='\0'){
					//printf("LOG index=%d\n",index);
					break;
				}
				index++;
			}
			for (c = ps.optind; c < argc; ++c) {//將AShell腳本參數都加上引號，因為參數進到用C語言寫的啟動器會被去掉，但是接下來要用System函數以命令方式啟動用java寫的AShell所以如果沒加回引號像是原本參數"poi yee"就會變成poi 
				com[index++]=' ';//如果是第一次做，那麼就具有把原本的字串結束字元以空格取代掉的功能 
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
	chdir("C:\\AShell");//切換路徑至 C:\AShell
	system(com);
	return 0;
}

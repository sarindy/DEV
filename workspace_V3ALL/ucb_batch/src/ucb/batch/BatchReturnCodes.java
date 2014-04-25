package ucb.batch;

public interface BatchReturnCodes {
    
    public static final int RC_0000 = 0;  //Job successfully.
    
    //RC1 第一類RC：一律通知AP.    
    public static final int RC1_08    =  8;     //ERR.
    public static final int RC1_1033  =  1033;  //記憶體ERR.                                    
    public static final int RC1_12    =  12;    //ERR 例//刪除MDSJR LOCK.                       
    public static final int RC1_16    =  16;    //MBR找無 OR SORT SPACE滿.                                                   
    public static final int RC1_1666  =  1666;  //處理部份有誤.                                 
    public static final int RC1_1777  =  1777;  //DEAD LOCK.                                    
    public static final int RC1_1878  =  1878;  //FTP CMD有誤.                                  
    public static final int RC1_888   =  888;   //輸入檔有誤.                                   
    public static final int RC1_1888  =  1888;  //輸入檔有誤.                                   
    public static final int RC1_1954  =  1954;  //400抓檔問題.                                  
    public static final int RC1_1997  =  1997;  //低於預期值.                                   
    public static final int RC1_1998  =  1998;  //高於預期值.                                   
    public static final int RC1_999   =  999;   //處理有誤.                                     
    public static final int RC1_1999  =  1999;  //處理有誤.
    public static final int RC1_2888  =  2888;  //輸入檔日期有誤.                               
    public static final int RC1_2977  =  2977;  //R6檔案權限ERR.                                
    public static final int RC1_3862  =  3862;  //FireWall問題.                                 
    public static final int RC1_4036  =  4036;  //PGM PCB ERROR.                                
    public static final int RC1_4038  =  4038;  //DS找無或有誤.                                 
    public static final int RC1_4039  =  4039;  //LOAD MODULE 找無.                             
    public static final int RC1_4094  =  4094;  //ERR.    
    public static final int RC1_474   =  474;   //PGM IDX ERR.            
    public static final int RC1_476   =  476;   //PGM PCB ERR.            
    public static final int RC1_777   =  777;   //DDEAD LOCK；OP稍後RERUN.
                                                                                              
    //RC2  第二類RC：免通知AP.
    public static final int RC2_02    =   2;    //部份有誤；免處理.                             
    public static final int RC2_03    =   3;    //免處理，免通知.                               
    public static final int RC2_04    =   4;    //免處理，免通知.    
    public static final int RC2_333   =   333;  //預期中錯誤；強迫結束.                         
    public static final int RC2_555   =   555;  //JOB今日不執行；強迫結束.                      
    public static final int RC2_666   =   666;  //處理有誤；強迫結束該整包AD.                    
                                                                                              
    //RC3  第三類RC：參考說明 .                                                               
    public static final int RC3_457   =   457;  //PSB撞到；OP稍後RERUN.                                                  
    public static final int RC3_1808  =   1808; //FTP有問題.                                     
    public static final int RC3_166   =   166;  //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_2624  =   2624; //FTP有問題；OP稍後RERUN。                       
    public static final int RC3_2850  =   2850; //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_1328  =   1328; //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_1807  =   1807; //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_2674  =   2674; //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_2845  =   2845; //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_2874  =   2874; //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_2875  =   2875; //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_2876  =   2876; //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_2974  =   2974; //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_2028  =   2028; //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_2424  =   2424; //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_2574  =   2574; //FTP有問題，請依個案說明辦理.                   
    public static final int RC3_3712  =   3712; //FTP有問題(DSN正被他人使用中)，請依個案說明辦理.
}


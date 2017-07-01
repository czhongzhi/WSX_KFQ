@REM #########################################################   
@REM  Name: 递归复制文件，请把此文件放在你希望执行的那个目录  

@REM  Desciption:   

@REM  Author: amosryan  

@REM  Date: 2015-06-08  

@REM  Version: 1.0  

@REM  Copyright: Jimmy.  

@REM #########################################################  

  

@echo off  

setlocal enabledelayedexpansion  

  

@REM 设置你想制制的文件:源文件、目标文件  

set WHAT_SRC_FILENAME=Root_OK 
set WHAT_SHOULD_BE_COPYED=Root  

  

for /r . %%a in (!WHAT_SHOULD_BE_COPYED!) do (  

  if exist %%a (  

  echo "复制"%%a   

  copy /y .\%WHAT_SRC_FILENAME%  "%%a"  

 )  

)  

  
pause

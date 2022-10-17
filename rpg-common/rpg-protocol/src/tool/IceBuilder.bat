@echo off
set work_path=../main/slice

cd %work_path%
for /R %%s in (.,*) do (
echo %%s
slice2java %%s --output-dir ../java
)

pause
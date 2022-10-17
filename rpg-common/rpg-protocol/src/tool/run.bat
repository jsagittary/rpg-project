@echo off

set ProjectPath=D:\rpg\rpg-project\rpg-common\rpg-protocol
set ProtoPath=%ProjectPath%/src/main/protocol
set ProjectType=maven
set CreateJava=true
set CreateCPP=false
set CreateCS=false
set CreateLua=false

java -jar protocol.jar %ProjectPath% %ProtoPath% %ProjectType% %CreateJava% %CreateCPP% %CreateCS% %CreateLua%
echo. & pause
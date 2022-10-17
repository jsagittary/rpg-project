@echo off

set ProjectPath=D:\svn_181\servers\rpg-servers\rpg-protocol
set ProtoPath=%ProjectPath%/src/main/cache
set ProjectType=maven

java -jar gamecache.jar %ProjectPath% %ProtoPath% %ProjectType%
echo. & pause
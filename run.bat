@echo off
title Edgeville
java -server -Xmx3024m -cp bin;lib/* edgeville.Server
pause
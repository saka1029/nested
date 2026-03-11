#!/bin/sh
DoExitAsm ()
{ echo "An error occurred while assembling $1"; exit 1; }
DoExitLink ()
{ echo "An error occurred while linking $1"; exit 1; }
echo Linking src/main/pascal/variable
OFS=$IFS
IFS="
"
/usr/bin/ld.bfd -b elf64-x86-64 -m elf_x86_64     -s  -L. -o src/main/pascal/variable -T link8715.res -e _start
if [ $? != 0 ]; then DoExitLink src/main/pascal/variable; fi
IFS=$OFS

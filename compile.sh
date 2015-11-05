#############################################################################
# File: compile.sh
# Author: Ashish Tibrewal
# Date: 12.10.2015
# Description: Script to compile all the source files and run the simulator
#############################################################################

#!/bin/bash

echo "Current working directory:"
pwd
echo "Moving to source directory"
cd src/
echo "Compiling source files"
javac -d ../bin/ *.java
echo "Moving to class/bin directory"
cd ../bin/
echo "Starting JVM to run the program (CPU simulator)"
java ProcessorSimulator input.txt 

#############################################################################
# File: compile.sh
# Author: Ashish Tibrewal
# Date: 12.10.2015
# Description: Script to compile all the source files and run the simulator
#############################################################################

#!/bin/bash

clear
echo "Current working directory:"
pwd
echo "Moving to source directory."
cd src/
echo "Compiling source files."
javac -Xlint:unchecked -d ../bin/ *.java   # Could use javac -d ../bin/ ProcessorSimulator.java (Since this is the main entry point to the simulator) 
if [ $? -eq 0 ]           # Check if the previous command retured with exit status 0. If true, only then run the simulator.
then
  echo "Compilation successful."
  echo "Moving to class/bin directory."
  cd ../bin/
  echo "Starting JVM to run the program (CPU simulator)."
  java ProcessorSimulator input.txt 
else
  echo "Compilation failed."
fi

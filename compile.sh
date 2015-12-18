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
# javac -d bin -sourcepath src src/ProcessorSimulator.javac       # If using this, then this needs to be run from parent directory (i.e. directory in which this script is located)
if [ $? -eq 0 ]           # Check if the previous command retured with exit status 0. If true, only then run the simulator.
then
  echo "Compilation successful."
  echo "Moving to class/bin directory."
  cd ../bin/
  echo "Starting JVM to run the program (CPU simulator)."
  java ProcessorSimulator input.txt
  # java -cp bin ProcessorSimulator input.txt       # If using this, then this needs to be run from parent directory (i.e. directory in which this script is located)
else
  echo "Compilation failed."
fi

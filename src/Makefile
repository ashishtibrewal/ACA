#
# Makefile
# Author: Ashish Tibrewal
# Date: 23.10.15
# Details: Makefile to built the CPU Simulator (i.e. Processor Simulator)
#

# Java compiler macro/variable
JC = javac

# Compiler flag macro/variable
JFLAGS = -g

# Default Java runtime - JVM (Java Virtual Machine)
JVM = java

# Source directory
SOURCEDIR = src

# Build directory
BUILDDIR = bin

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

SOURCES = \
	ProcessorSimulator.java \
	Instruction.java \
	Memory.java \
	ISA.java \
	Register.java \
	MemIndexOutOfBoundsException.java \
	RegisterAccessException.java

MAIN = ProcessorSimulator

classes: $(SOURCES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)

clean:
	$(RM) *.class
#@$(RM) *.class



# Old version of the Makefiles
# # Java source files for the CPU Simulator (i.e. Processor Simulator)
# SOURCES = Instruction.java \
# 					Memory.java \
# 					ISA.java \
# 					ProcessorSimulator.java \
# 					Register.java \
# 					MemIndexOutOfBoundsException.java \
# 					RegisterAccessException.java

# # Java class files for the CPU Simulator (i.e. Processor Simulator)
# CLASSES = $(SOURCES:.java=.class)

# # Default target
# TARGET = CPUSimulator

# $(CPUSimulator): $(SOURCES)
# 				$(JC) $(JFLAGS) @^

# TARGET: classes

# classes: $(CLASSES:.java=.class)

# .PHONY: clean

# clean:
# 		@rm -f *.class
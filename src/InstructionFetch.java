/**
 * @file InstructionFetch.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details This file contains the InstructionFetch class that handles the first stage of the pipeline. It is used to read instructions
 * from the main memory which is then passed to the next pipeline stage (i.e. InstructionDecode) for further processing.
 */

// Import packages
import java.io.*;
import java.util.*;
import java.lang.*;

/**
 * This class implements the Instruction Fetch unit of the processor.
 */
public class InstructionFetch implements IPipelineStage
{
  // Class/Instance fields
  private Register cpuRegisters;
  private Memory cpuMemory;
  private int currentPC;
  private int currentInstruction;

  public InstructionFetch(Register cpuRegisters, Memory cpuMemory)
  {
      this.cpuRegisters = cpuRegisters;
      this.cpuMemory = cpuMemory;
  }

  public void step()
  {
    currentPC = cpuRegisters.readPC();                        // Read value of the PC register
    currentInstruction = cpuMemory.readValue(currentPC);      // Read value from main memory at the location specified by the PC register.
    cpuRegisters.writeIR(currentInstruction);                 // Write value to the instruction register (IR)
    cpuRegisters.incrementPC();
  }
}

/**
 * @file InstructionDecode.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details This file contains the InstructionDecode class that handles the second stage of the pipeline. It is used to decode instructions
 * that have been fetched from the main memory by the InstructionFetch class.
 */

// Import packages
import java.io.*;
import java.util.*;
import java.lang.*;

/**
 * This class implements the Instruction Decode unit of the processor.
 */
public class InstructionDecode implements IPipelineStage
{
  // Class/Instance fields
  private Register cpuRegisters;
  private Memory cpuMemory;
  private int currentPC;
  private int currentInstruction;

  public InstructionDecode(Register cpuRegisters, Memory cpuMemory)
  {
      this.cpuRegisters = cpuRegisters;
      this.cpuMemory = cpuMemory;
  }

  public void step()
  {
    currentInstruction = cpuRegisters.readIR();       // Read the value currently stored in the instruction register (IR)
    cpuRegisters.updatePC(false);
  }
}
/**
 * @file InstructionDecodeStage.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details This file contains the InstructionDecodeStage class that handles the second stage of the pipeline. It is used to decode instructions
 * that have been fetched from the main memory by the InstructionFetch class.
 */

// Import packages
// Custom packages
import pipeline.*;

// Pre-defined Java packages
import java.util.*;
import java.lang.*;

/**
 * This class implements the Instruction Decode unit of the processor.
 */
public class InstructionDecodeStage implements IStage
{
  // Class/Instance fields
  // private Register cpuRegisters;
  // private Memory cpuMemory;
  private int currentPC;
  private int currentInstruction;
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */

  // public InstructionDecode(Register cpuRegisters, Memory cpuMemory)
  // {
  //     this.cpuRegisters = cpuRegisters;
  //     this.cpuMemory = cpuMemory;
  // }

  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext)context;              // Explicitly cast context to ProcessorPipelineContext type
    currentInstruction = pContext.cpuRegisters.readIR();       // Read the value currently stored in the instruction register (IR)
    pContext.cpuRegisters.updatePC(false);                     // Update primary PC register with the incremented shadow PC register value.
  }
}
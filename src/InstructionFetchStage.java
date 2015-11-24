/**
 * @file InstructionFetchStage.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details This file contains the InstructionFetchStage class that handles the first stage of the pipeline. It is used to read instructions
 * from the main memory which is then passed to the next pipeline stage (i.e. InstructionDecode) for further processing.
 */

// Import packages
// Custom packages
import pipeline.*;

// Pre-defined Java packages
import java.util.*;
import java.lang.*;

/**
 * This class implements the Instruction Fetch (IF) stage of the processor.
 */
public class InstructionFetchStage implements IStage
{
  // Class/Instance fields
  // private Register cpuRegisters;
  // private Memory cpuMemory;
  private int currentPC;
  private int currentInstruction;
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */

  // public InstructionFetch(Register cpuRegisters, Memory cpuMemory)
  // {
  //     this.cpuRegisters = cpuRegisters;
  //     this.cpuMemory = cpuMemory;
  // }

  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;                     // Explicitly cast context to ProcessorPipelineContext type
    currentPC = pContext.cpuRegisters.readPC();                        // Read value of the PC register
    currentInstruction = pContext.cpuMemory.readValue(currentPC);      // Read value from main memory at the location specified by the PC register
    pContext.cpuRegisters.writeIR(currentInstruction);                 // Write value to the instruction register (IR)
    pContext.cpuRegisters.incrementPC();                               // Increment value stored in the (temporary) PC register
  }
}

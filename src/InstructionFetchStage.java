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
public class InstructionFetchStage implements IProcessorPipelineStage
{
  // Class/Instance fields
  // private Register cpuRegisters;
  // private Memory cpuMemory;
  private int currentPC;
  private int currentInstruction;
  private Register cpuRegisters;                 /** Reference to architectural registers */
  private Memory cpuMemory;                      /** Reference to main memory */
  private ProcessorPipelineContext pContext;     /** Reference to the processor pipeline context */

  // public InstructionFetch(Register cpuRegisters, Memory cpuMemory)
  // {
  //     this.cpuRegisters = cpuRegisters;
  //     this.cpuMemory = cpuMemory;
  // }

  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;                          // Explicitly cast context to ProcessorPipelineContext type
    cpuRegisters = pContext.getCpuRegisters();                              // Obtain and store the reference to the primary cpu registers object from the pipeline context (Doing this to avoid having to type it over and over again)
    cpuMemory = pContext.getCpuMemory();                                    // Obtain and store the reference to the primary cpu memory object from the pipeline context (Doing this to avoid having to type it over and over again)
    currentPC = cpuRegisters.readPC();                                      // Read value of the PC register
    currentInstruction = cpuMemory.readValue(currentPC);                    // Read value from main memory at the location specified by the PC register
    cpuRegisters.writeIR(currentInstruction);                               // Write value to the instruction register (IR)
    cpuRegisters.incrementPC();                                             // Increment value stored in the (temporary/shadow) PC register. Actual value is set in the instruction execute or memory access stage.
  }

  // TODO need to fill function contents accordingly
  public void flush(IPipelineContext context)
  {

  }
}

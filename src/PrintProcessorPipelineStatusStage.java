/**
 * @file PrintProcessorPipelineStatusStage.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details This file contains the PrintProcessorPipelineStatusStage class that handles the debug/utility(fifth) stage of the pipeline. It is used to display
 * the state of the processor pipeline at the end of each cycle. This stage can be helpful during the debugging process.
 */

// Import packages
// Custom packages
import pipeline.*;

// Pre-defined Java packages
import java.util.*;
import java.lang.*;

/**
 * This class implements the Instruction Execute (IE) stage of the processor.
 */

public class PrintProcessorPipelineStatusStage implements IStage 
{
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */
  //private static int tempCounter = 0;

  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    // TODO Add stage functionality here to print the current state of the pipeline
    //tempCounter++;
    //System.out.println("....... " + tempCounter + " ..... " +pContext.cpuRegisters.readPC());
    System.out.println("Current state of the process pipeline is shown below:");
    System.out.print("List of istructions : "); 
    for (Instruction instruction:pContext.getInstructionQueue())
    {
      System.out.print(instruction.getOpCode() + ", ");
    }
    System.out.println();
  }
}
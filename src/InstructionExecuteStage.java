/**
 * @file InstructionExecuteStage.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details This file contains the InstructionExecuteStage class that handles the fourth stage of the pipeline. It is used to execute instructions
 * that have been fetched, decoded from the main memory and issued by the Instruction Issue stage. This stage is supposed to contain multiple execution
 * untis (EUs) such as multiple ALUs, Load/store units, Branch units, Vector units, floating-point units, etc.
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
public class InstructionExecuteStage implements IStage
{
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */

    public void execute(IPipelineContext context)
    {
      pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
      // TODO Add stage functionality here
    }
}
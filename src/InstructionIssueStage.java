/**
 * @file InstructionIssueStage.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details This file contains the InstructionIssueStage class that handles the third stage of the pipeline. It is used to store/queue instructions
 * that have been fetched and decoded from the main memory. This stage queues the instructions until a Execution Unit (EU) in the Instruction Execution 
 * stage becomes free.
 * This stage is supposed to contain the following features:
 * 1. Dependency checking
 * 2. Register renaming
 * 3. Instruction issue to the Execution stage
 */

// Import packages
// Custom packages
import pipeline.*;

// Pre-defined Java packages
import java.util.*;
import java.lang.*;

/**
 * This class implements the Instruction Issue (II) stage of the processor.
 */
public class InstructionIssueStage implements IStage
{
  private ProcessorPipelineContext pContext;        /** Reference to the processor pipeline context */
  private ArrayList<Instruction> instructionList;   /** Reference to the processor instruction list */
  private Queue<Instruction> instructionQueue;      /** Reference to the processor's primary instruction queue */

  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    // TODO Add stage functionality here
    instructionList = new ArrayList<Instruction>();
    instructionList.add(pContext.currentInstruction);
  }

}
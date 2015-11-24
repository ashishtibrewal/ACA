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
 * 3. Reservation station (To hold instructions that can be executed in out of order fashion)
 * 4. Instruction issue to the Execution stage
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
  private Instruction currentInstruction;
  private ArrayList<Instruction> instructionList;   /** Reference to the processor instruction list */
  private Queue<Instruction> instructionQueue;   /** Reference to the current instruction queue */

  public InstructionIssueStage()
  {
    instructionList = new ArrayList<Instruction>();   // Instantiate the instruction list object
    //instructionQueue = new ArrayBlockingQueue<Instruction>(GlobalConstants.INSTRUCTION_QUEUE_CAPACITY);   // Note this requires the java.util.concurrent.* package to be imported
    instructionQueue = new LinkedList<Instruction>();
  }

  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;              // Explicitly cast context to ProcessorPipelineContext type
    // TODO Add stage functionality here
    currentInstruction = pContext.getCurrentInstruction();      // Obtain the current instruction from the pipeline context
    instructionList.add(currentInstruction);                    // Add the current instruction to the list

    // TODO need to obtain source register values for all the instructions and update the instructions dependency flag depending on the dependency checking algorithm
    // Instrucions can remain in this stage for multiple clock cycles (Mainly due to dependencies)
    // TODO Do all the work and (flow) checking on the instruction list and only add the instructions that need to be executed in a specific order to the instruction queue
    // TODO obtain the correct source register value and set it in the instruction object before adding it to the queue. Would need to check for dependencies.
    
    if (currentInstruction.getDependencyFlag() == false)    // Only add the instruction to the instruction queue (and remove from the instruction list) if it doesn't have any dependencies (or if all it's dependencies have finished executing)
    {
      switch(currentInstruction.getInstructionType())
      {
        // RRR type
        case "RRR":
          currentInstruction.setSourceReg1Val(pContext.getCpuRegisters().readGP(currentInstruction.getSourceReg1Loc()));      // Read value for source register 1
          currentInstruction.setSourceReg2Val(pContext.getCpuRegisters().readGP(currentInstruction.getSourceReg2Loc()));      // Read value for source register 2
          break;

        // RRI type
        case "RRI":
          currentInstruction.setSourceReg1Val(pContext.getCpuRegisters().readGP(currentInstruction.getSourceReg1Loc()));      // Read value for source register 1
          break;

        // RR type
        case "RR":
          currentInstruction.setSourceReg1Val(pContext.getCpuRegisters().readGP(currentInstruction.getSourceReg1Loc()));      // Read value for source register 1
          break;

        // RI type
        case "RI":
          // Don't need to read from any source registers
          break;

        // I type
        case "I":
          // Don't need to read from any source registers
          break;

        // Shoudn't get here since it's an invalid instruction type
        default:
          System.out.println("Invalid Instruction Type! Instruction couldn't be issued!");
          break;
      }
      instructionQueue.add(instructionList.get(GlobalConstants.INSTRUCTION_LIST_START_INDEX));    // Add the top instruction to the queue by obtaining it from the instruction list
      instructionList.remove(GlobalConstants.INSTRUCTION_LIST_START_INDEX);                       // Remove the top instruction from the list after it has been added to the queue
    }
  }

  // TODO need to fill function contents accordingly
  public void flush()
  {

  }

  /**
   * Method to obtain (a reference) to the instruction queue object
   * @return Reference to the instruction queue object
   */
  public Queue<Instruction> getInstructionQueue()
  {
    return instructionQueue;
  }
}
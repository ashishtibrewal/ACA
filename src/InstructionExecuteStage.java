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
  private IExecutionUnit ALU;      /** Reference to the ALU */
  private IExecutionUnit LSU;      /** Reference to the LSU */
  private IExecutionUnit BU;       /** Reference to the BU */
  private Instruction currentInstruction;     /** Reference to the current instruction */   // TODO Should actually be a list of instructions when going superscalar
  private ExecutionUnit requiredExecutionUnit;

  public InstructionExecuteStage()
  {
    ALU = new Alu();    // Instantiate the ALU object
    LSU = new Lsu();    // Instantiate the LSU object
    BU = new Bu();      // Instantiate the BU object
  }

  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    // TODO Add stage functionality here
    // TODO Read instructions from the instruction queue and feed in to the correct execution unit
    //currentInstruction = ((LinkedList<Instruction>)pContext.getInstructionQueue()).getFirst();
    currentInstruction = ((LinkedList<Instruction>)pContext.getInstructionQueue()).remove();      //TODO Extract the first instruction from the instruction queue, remove it from the queue and shift all contents forward
    // TODO NOTE NEED TO CHECK IF THE INSTRUCTION IS A BRANCH INSTRUCTION, IF YES, ONLY RUN THE BU UNIT, IF NOT YOU CAN RUN BOTH THE ALU AND LSU UNITS. NOTE THAT ALL THE THREE UNITS CAN'T BE RUNNING TOGETHER IN THE SAME CYCLE.
    requiredExecutionUnit = currentInstruction.getExecutionUnit();
    
    switch(requiredExecutionUnit)
    {
      // ALU instruction
      case ALU:
        ALU.execute(currentInstruction, context);       // Execute ALU
        break;
      
      // LSU instruction
      case LSU:
        LSU.execute(currentInstruction, context);       // Execute LSU
        break;

      // BU instruction
      case BU:
        BU.execute(currentInstruction, context);        // Execute BU
        break;

      // Default case. This condition should never be reached
      default:
        System.out.println("Fatal error! Unknown/Invalid execution unit (EU) parameter!");
        break;
    }

    // The line below needs to be removed and put in the execute or the memory access stage
    pContext.getCpuRegisters().updatePC(false);                     // Update primary PC register with the incremented shadow PC register value.
  }

  // TODO need to fill function contents accordingly
  public void flush()
  {

  }
}
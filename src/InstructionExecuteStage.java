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
public class InstructionExecuteStage implements IProcessorPipelineStage
{
  private IExecutionUnit ALU;      /** Reference to the ALU */
  private IExecutionUnit LSU;      /** Reference to the LSU */
  private IExecutionUnit BU;       /** Reference to the BU */
  private Instruction instruction;     /** Reference to the current instruction */   // TODO Should actually be a list of instructions when going superscalar
  private ExecutionUnit requiredExecutionUnit;      
  private Register cpuRegisters;                    /** Reference to architectural registers */
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */

  public InstructionExecuteStage()
  {
    ALU = new Alu();    // Instantiate the ALU object
    LSU = new Lsu();    // Instantiate the LSU object
    BU = new Bu();      // Instantiate the BU object
  }

  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    cpuRegisters = pContext.getCpuRegisters();                 // Obtain and store the reference to the primary cpu registers object from the pipeline context (Doing this to avoid having to type it over and over again)
    instruction = pContext.getCurrentInstructionFromInstructionQueue();   // Extract the current instruction from the instruction queue
    // TODO NOTE NEED TO CHECK IF THE INSTRUCTION IS A BRANCH INSTRUCTION, IF YES, ONLY RUN THE BU UNIT, IF NOT YOU CAN RUN BOTH THE ALU AND LSU UNITS. NOTE THAT ALL THE THREE UNITS CAN'T BE RUNNING TOGETHER IN THE SAME CLOCK CYCLE.
    requiredExecutionUnit = instruction.getExecutionUnit();
    switch (requiredExecutionUnit)     // Execute the requried functional/execution unit depending on the type of instruction.
    {
      // ALU instruction
      case ALU:
        ALU.execute(instruction, context);       // Execute ALU
        break;
      
      // LSU instruction
      case LSU:
        LSU.execute(instruction, context);       // Execute LSU
        break;

      // BU instruction
      case BU:
        BU.execute(instruction, context);        // Execute BU
        break;

      // Default case. This condition should never be reached
      default:
        System.err.println("Fatal error! Unknown/Invalid execution unit (EU) parameter!");
        break;
    }

    // THIS IS WHERE THE PC IS UPDATED
    cpuRegisters.updatePC(pContext.getBranchTaken());   // Update the primary/actual PC register with the correct/required value based on whether a branch was taken or not
    pContext.setBranchTakenOld(false);                  // Function to store the current value of the branchTaken variable in the pipeline context. THIS IS ONLY USEFUL FOR DEBUGGING/PRINTING PURPOSES.
    pContext.setBranchTaken(false);                     // Revert the branch taken variable back to false

    cpuRegisters.incrementInstructionCounter();         // Increment the instruction counter register (variable)
    if (instruction.getOpCode() == Isa.NOP)
    {
      cpuRegisters.incrementInstructionCounterNOP();    // Increment the NOP instruction counter register (variable)
    }
  }

  // TODO need to fill function contents accordingly
  public void flush(IPipelineContext context)
  {
   instruction = new Instruction(GlobalConstants.DEFAULT_INSTRUCTION_TYPE,
                                 GlobalConstants.DEFAULT_INSTRUCTION_MNEMONIC,
                                 ExecutionUnit.ALU,
                                 GlobalConstants.DEFAULT_INSTRUCTION_OPCODE, 
                                 GlobalConstants.DEFAULT_MEM_FETCH_LOC,
                                 GlobalConstants.DEFAULT_INSTRUCTION,
                                 Isa.InstructionType.RRR.NUMBER_OF_CYCLES,
                                 GlobalConstants.DEFAULT_BRANCH_PREDICTION,
                                 Isa.DEFAULT_REG_VALUE,
                                 Isa.DEFAULT_REG_VALUE,
                                 Isa.DEFAULT_REG_VALUE,
                                 Isa.DEFAULT_IMM_VALUE);
   requiredExecutionUnit = GlobalConstants.DEFAULT_EXECUTION_UNIT;
  }

  /**
   * Method to obtain the current instruction to be executed by the IE stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current instruction to be executed
   */
  public Instruction getCurrentInstruction()
  {
    return instruction;
  }
}
/**
 * @file InstructionIssueStage.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details This file contains the InstructionIssueStage class that handles the third stage of the pipeline. It is used to store/queue instructions
 * that have been fetched and decoded from the main memory. This stage queues the instructions until a Execution Unit (EU) in the Instruction Execution 
 * stage becomes free. Note that instructions can remain in this stage for multiple clock cycles, mainly due to its dependencies.
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
public class InstructionIssueStage implements IProcessorPipelineStage
{
  private Instruction[] instruction;
  private ArrayList<Instruction> instructionList;   /** Reference to the processor instruction list */
  private Register cpuRegisters;                    /** Reference to architectural registers */
  private ProcessorPipelineContext pContext;        /** Reference to the processor pipeline context */

  public InstructionIssueStage()
  {
    instructionList = new ArrayList<Instruction>();   // Instantiate the instruction list object
  }

  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;        // Explicitly cast context to ProcessorPipelineContext type
    cpuRegisters = pContext.getCpuRegisters();            // Obtain and store the reference to the primary cpu registers object from the pipeline context (Doing this to avoid having to type it over and over again)
    instruction = pContext.getCurrentInstruction();       // Obtain the current instruction from the pipeline context
    for (int i = 0; i < GlobalConstants.BUS_WIDTH; i++)
    {
      instructionList.add(instruction[i]);                     // Add the current instruction to the list
    }
    // TODO need to obtain source register values for all the instructions and update the instructions dependency flag depending on the dependency checking algorithm
    // Instrucions can remain in this stage for multiple clock cycles (Mainly due to dependencies)
    // TODO Do all the work and (flow) checking on the instruction list and only add the instructions that need to be executed in a specific order to the instruction queue
    // TODO Obtain the correct/updated source register values and set it in the instruction object before adding it to the queue. Would need to check for dependencies. NOTE THIS IS NOT DONE IN THE DECODE STAGE AND NEEDS TO BE DONE HERE !!!
    int i = 0;
    if (instruction[i].getDependencyFlag() == false)    // Only add the instruction to the instruction queue (and remove from the instruction list) if it doesn't have any dependencies (or if all it's dependencies have finished executing)
    {
      switch (instruction[i].getInstructionType())
      {
        // RRR type
        case "RRR":
          instruction[i].setSourceReg1Val(cpuRegisters.readGP(instruction[i].getSourceReg1Loc()));      // Read value for source register 1
          instruction[i].setSourceReg2Val(cpuRegisters.readGP(instruction[i].getSourceReg2Loc()));      // Read value for source register 2
          break;

        // RRI type
        case "RRI":
          if (instruction[i].getOpCode() == Isa.BEQ || instruction[i].getOpCode() == Isa.BNE || instruction[i].getOpCode() == Isa.BLT || instruction[i].getOpCode() == Isa.BGT)
          {
            instruction[i].setSourceReg1Val(cpuRegisters.readGP(instruction[i].getSourceReg1Loc()));      // Read value for source register 1
            instruction[i].setSourceReg2Val(cpuRegisters.readGP(instruction[i].getSourceReg2Loc()));      // Read value for source register 2
          }
          else
          {
            instruction[i].setSourceReg1Val(cpuRegisters.readGP(instruction[i].getSourceReg1Loc()));      // Read value for source register 1
          }
          break;

        // RR type
        case "RR":
          instruction[i].setSourceReg1Val(cpuRegisters.readGP(instruction[i].getSourceReg1Loc()));      // Read value for source register 1
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
          System.err.println("Invalid Instruction Type! Instruction couldn't be issued!");
          break;
      }
      pContext.setNextInstructionToInstructionQueue(instructionList.get(GlobalConstants.INSTRUCTION_LIST_START_INDEX)); // Add the top instruction to the queue by obtaining it from the instruction list
      instructionList.remove(GlobalConstants.INSTRUCTION_LIST_START_INDEX);                                             // Remove the top instruction from the list after it has been added to the executable instruction queue
      // instructionList.remove(GlobalConstants.INSTRUCTION_LIST_START_INDEX + 1);                                             // Remove the top instruction from the list after it has been added to the executable instruction queue
      // instructionList.remove(GlobalConstants.INSTRUCTION_LIST_START_INDEX + 2);                                             // Remove the top instruction from the list after it has been added to the executable instruction queue
      // instructionList.remove(GlobalConstants.INSTRUCTION_LIST_START_INDEX + 3);                                             // Remove the top instruction from the list after it has been added to the executable instruction queue
    }
  }

  // TODO need to fill function contents accordingly
  public void flush(IPipelineContext context)
  {
    for (int i = 0; i < GlobalConstants.BUS_WIDTH; i++)
    {
      instruction[i] = new Instruction(GlobalConstants.DEFAULT_INSTRUCTION_TYPE,
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
    }
   instructionList.clear();     // Clear the instruction (array) list
  }

  /**
   * Method to obtain the current instruction to be issued by the II stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current instruction to be issued
   */
  public Instruction[] getCurrentInstruction()
  {
    return instruction;
  }
}
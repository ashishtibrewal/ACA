/**
 * @file InstructionWritebackStage.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details This file contains the InstructionWritebackStage class that handles the fifth stage of the pipeline. It is used to writeback register values for instructions
 * that have been executed by the Instruction Execute stage.
 */

// Import packages
// Custom packages
import pipeline.*;

// Pre-defined Java packages
import java.util.*;
import java.lang.*;

/**
 * This class implements the Instruction Writeback (WB) stage of the processor.
 */
public class InstructionWritebackStage implements IProcessorPipelineStage
{
  Instruction instruction;

  public InstructionWritebackStage()
  {
    this.instruction = new Instruction(GlobalConstants.DEFAULT_INSTRUCTION_TYPE,
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
                                       Isa.DEFAULT_IMM_VALUE);                        // Instantiate new instruction object to avoid getting a null pointer exception in the IE stage when performing result passing. Only useful in the first cycle of the simulation
  }

  public void execute(IPipelineContext context)
  {
    ProcessorPipelineContext pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    Register cpuRegisters = pContext.getCpuRegisters();                                 // Obtain and store the reference to the primary cpu registers object from the pipeline context (Doing this to avoid having to type it over and over again)
    instruction = pContext.getCurrentInstructionWriteBack();
    // TODO Insert check to only writeback those instructions that are valid for a writeback operation
    if (instruction.getExecutionUnit() == ExecutionUnit.ALU || instruction.getExecutionUnit() == ExecutionUnit.LSU)       // The writeback stage is only valid for ALU and LSU instructions in this design (BU updates required registers in the IE stage)
    {
      if (instruction.getOpCode() != Isa.NOP)       // Prevent any writes to registers for the NOP instruction
      {
        cpuRegisters.writeGP(instruction.getDestinationRegLoc(), instruction.getWritebackVal());
      }
    }
    // THIS IS WHERE THE PC IS UPDATED
    cpuRegisters.updatePC(pContext.getBranchTaken());   // Update the primary/actual PC register with the correct/required value based on whether a branch was taken or not
    pContext.setBranchTakenOld(false);                  // Function to store the current value of the branchTaken variable in the pipeline context. THIS IS ONLY USEFUL FOR DEBUGGING/PRINTING PURPOSES.
    pContext.setBranchTaken(false);                     // Revert the branch taken variable back to false
  }

  // This flush method should do nothing (be empty) since this stage is ahead of the instruction execute stage in the pipeline. Only stages before/behind the instruction execute stage need to be flushed.
  public void flush(IPipelineContext context)
  {

  }

  /**
   * Method to obtain the current instruction to be issued by the II stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current instruction to be written back
   */
  public Instruction getCurrentInstruction()
  {
    return instruction;
  }
}
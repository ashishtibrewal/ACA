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
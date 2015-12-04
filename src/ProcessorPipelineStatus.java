/**
 * @file ProcessorPipelineStatus.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details This file contains the ProcessorPipelineStatus class. It is used to display the state of the processor pipeline 
 * at the end of each cycle. This stage can be helpful during the debugging process.
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

public class ProcessorPipelineStatus
{
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */
  private InstructionFetchStage instructionFetchStage;          /** Reference to the Instruction Fetch Stage of the pipeline. Useful if something is required from this stage by other stages. */
  private InstructionDecodeStage instructionDecodeStage;        /** Reference to the Instruction Decode Stage of the pipeline. Useful if something is required from this stage by other stages. */
  private InstructionIssueStage instructionIssueStage;          /** Reference to the Instruction Issue Stage of the pipeline. Useful if something is required from this stage by other stages. */
  private InstructionExecuteStage instructionExecuteStage;      /** Reference to the Instruction Execute Stage of the pipeline. Useful if something is required from this stage by other stages. */
  private Register cpuRegisters;                                /** Reference to the primary architectural cpu registers object */
  //private Memory cpuMemory;                                     /** Reference to the main memory */

  public void print(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    instructionFetchStage = (InstructionFetchStage) pContext.getIF_Stage();       // Explicitly cast to InstructionFetchStage type of be able to call its methods
    instructionDecodeStage = (InstructionDecodeStage) pContext.getID_Stage();     // Explicitly cast to InstructionDecodeStage type of be able to call its methods
    instructionIssueStage = (InstructionIssueStage) pContext.getII_Stage();       // Explicitly cast to InstructionIssueStage type of be able to call its methods
    instructionExecuteStage = (InstructionExecuteStage) pContext.getIE_Stage();   // Explicitly cast to InstructionExecuteStage type of be able to call its methods
    cpuRegisters = pContext.getCpuRegisters();
    //cpuMemory = pContext.getCpuMemory();
    System.out.println("Clock Cycle: " + (cpuRegisters.readClockCounter() - 1));  // Subtracting 1 because the clock counter value has already been incremented in this cycle before its being printed here
    System.out.println(">>> Current state of the process pipeline (At the end of the current cycle) <<<");
    System.out.println("+-+-------------------------------+-+---------------------------------+-+--------------------------------+-+-----------------------------+-+");
    System.out.println("|+|           IF Stage            |+|           ID Stage              |+|            II Stage            |+|          IE Stage           |+|");
    System.out.println("|+|--------------------------------------------------------------------------------------------------------------------------------------|+|");
    System.out.format ("|+| PC: %04d                      |+| PC: N/A                         |+| PC: N/A                        |+| PC: %04d (Branch: %5s)    |+|%n", instructionFetchStage.getCurrentPC(), cpuRegisters.readPC(), pContext.getBranchTakenOld());
    System.out.format ("|+| Instruction: 0x%08x       |+| Instruction: 0x%08x         |+| Instruction (Head): 0x%08x |+| Instruction: 0x%08x     |+|%n", instructionFetchStage.getCurrentInstructionRead(), instructionDecodeStage.getCurrentInstruction(), instructionIssueStage.getCurrentInstruction().getInstructionVal(), instructionExecuteStage.getCurrentInstruction().getInstructionVal());
    System.out.format ("|+| Branch Instruction: %5s     |+| Mnemonic: %4s (%3s)            |+| Mnemonic  : %4s (%3s)         |+| Mnemonic: %4s (%3s)        |+|%n", instructionFetchStage.getBranchInstruction(), instructionDecodeStage.getCurrentInstructionMnemonic(), instructionDecodeStage.getCurrentInstructionType(), instructionIssueStage.getCurrentInstruction().getInstructionMnemonic(), instructionIssueStage.getCurrentInstruction().getInstructionType(), instructionExecuteStage.getCurrentInstruction().getInstructionMnemonic(), instructionExecuteStage.getCurrentInstruction().getInstructionType());
    System.out.format ("|+| Branch predictor  : %5s     |+| SR1: R%02d                        |+| SR1 (Val) : R%02d (%08d)     |+| Execution Unit: %3s         |+|%n", instructionFetchStage.getBranchPredictorResult(), instructionDecodeStage.getCurrentSourceReg1(), instructionIssueStage.getCurrentInstruction().getSourceReg1Loc(), instructionIssueStage.getCurrentInstruction().getSourceReg1Val(), instructionExecuteStage.getCurrentInstruction().getExecutionUnit());
    System.out.format ("|+| PC/Branch target  : %04d      |+| SR2: R%02d                        |+| SR2 (Val) : R%02d (%08d)     |+|                             |+|%n", cpuRegisters.readPC(), instructionDecodeStage.getCurrentSourceReg2(), instructionIssueStage.getCurrentInstruction().getSourceReg2Loc(), instructionIssueStage.getCurrentInstruction().getSourceReg2Val());
    System.out.format ("|+|                               |+| DR : R%02d                        |+| DR        : R%02d                |+|                             |+|%n", instructionDecodeStage.getCurrentDestinationReg(), instructionIssueStage.getCurrentInstruction().getDestinationRegLoc());
    System.out.format ("|+|                               |+| Imm: %08d                   |+| Imm:      : %08d           |+|                             |+|%n", instructionDecodeStage.getCurrentSignedImmediate(), instructionIssueStage.getCurrentInstruction().getSignedImmediateVal());
    System.out.format ("|+|                               |+|                                 |+| Dependency: %5s              |+|                             |+|%n", instructionIssueStage.getCurrentInstruction().getDependencyFlag());
    System.out.println("+-+-------------------------------+-+---------------------------------+-+--------------------------------+-+-----------------------------+-+");

    /*
    System.out.print("List of istructions (In reverse order when looking at the pipeline) : "); 
    for (Instruction instruction:pContext.getInstructionQueue())
    {
      System.out.print(instruction.getOpCode() + ", ");
    }
    */
    System.out.println();

    pContext.setBranchTakenOld(true);       // Set the old branch taken boolean value to its default value, i.e. set it to false
  }
}
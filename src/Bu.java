import pipeline.*;
/**
 * Class that implements the Branch Unit (BU) of the processor
 */
public class Bu implements IExecutionUnit
{
  // TODO Add all required functionality for the BU
  private int opCode;
  private int sourceReg1Val;
  private int sourceReg2Val;
  private int signedImmediateVal;
  private int calculationResult;
  private Register cpuRegisters;
  private Memory cpuMemory;
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */
  public void execute(Instruction instruction, IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    cpuRegisters = pContext.getCpuRegisters();                 // Obtain and store the reference to the primary cpu registers object from the pipeline context (Doing this to avoid having to type it over and over again)
    cpuMemory = pContext.getCpuMemory();                       // Obtain and store the reference to the primary cpu memory object from the pipeline context (Doing this to avoid having to type it over and over again)
    opCode = instruction.getOpCode();
    // SR1 result passing
    if (instruction.getSourceReg1Val() != cpuRegisters.readGP(instruction.getSourceReg1Loc()))                              // Check if the current value held in the register is the same as that evaluated by the II unit
    {
      sourceReg1Val = cpuRegisters.readGP(instruction.getSourceReg1Loc());
      instruction.setSourceReg1Val(sourceReg1Val);
    }
    else
    {
      sourceReg1Val = instruction.getSourceReg1Val();
    }
    if (instruction.getSourceReg1Loc() == pContext.getCurrentInstructionWriteBack().getDestinationRegLoc())       // Check if the WB stage is going to write to a register that needs to be read by the ALU in the IE stage
    {
      sourceReg1Val = pContext.getCurrentInstructionWriteBack().getWritebackVal();
      instruction.setSourceReg1Val(sourceReg1Val);
    }
    else
    {
      sourceReg1Val = instruction.getSourceReg1Val();
    }
    // SR2 result passing
    if (instruction.getSourceReg2Val() != cpuRegisters.readGP(instruction.getSourceReg2Loc()))                              // Check if the current value held in the register is the same as that evaluated by the II unit
    {
      sourceReg2Val = cpuRegisters.readGP(instruction.getSourceReg2Loc());
      instruction.setSourceReg2Val(sourceReg2Val);
    }
    else
    {
      sourceReg2Val = instruction.getSourceReg2Val();
    }
    if (instruction.getSourceReg2Loc() == pContext.getCurrentInstructionWriteBack().getDestinationRegLoc())                 // Check if the WB stage is going to write to a register that needs to be read by the ALU in the IE stage
    {
      sourceReg2Val = pContext.getCurrentInstructionWriteBack().getWritebackVal();
      instruction.setSourceReg2Val(sourceReg2Val);
    }
    else
    {
      sourceReg2Val = instruction.getSourceReg2Val();
    }
    switch (opCode)
    {
      // Unconditional branches
      // BU Ix --- Unconditional branch --- Used in loops (when branching back to the start of the loop - do while, while and for loops)
      case Isa.BU:
        signedImmediateVal = instruction.getSignedImmediateVal();
        calculationResult = instruction.getMemoryFetchLocation() + signedImmediateVal;     // Add instruction relative PC with the signed immediate
        //calculationResult = cpuRegisters.readPC() + signedImmediateVal;                           // Add PC with the signed immediate. Similar to the above line but this uses the PC value stored in the register object
        if (!instruction.getBranchPredictionResult())     // If this branch was predicted to be not taken, only then execute this
        {
          pContext.setBranchTaken(!instruction.getBranchPredictionResult());
          pContext.setBranchTarget(calculationResult);
          pContext.setCorrectBranchPrediction(instruction.getBranchPredictionResult());     // Assert the BP global variable in the pipeline context for the simulator to check if the pipeline needs to be flushed
          cpuRegisters.incrementBranchPredictionsIncorrect();               // Increment incorrect branch prediction counter
        }
        else              // else do nothing, since the branch predictor predicted correct
        {
          cpuRegisters.incrementBranchPredictionsCorrect();               // Increment correct branch prediction counter
        }
        break;

      // BL Ix --- Unconditional branch with link --- Used in function calls (i.e. jumping to different labels in assembly code)
      case Isa.BL:
        signedImmediateVal = instruction.getSignedImmediateVal();
        calculationResult = instruction.getMemoryFetchLocation() + signedImmediateVal;     // Add instruction relative PC with the signed immediate, i.e. using the PC value (memory location from where the instruction was fetched) stored in the instruction object
        //calculationResult = cpuRegisters.readPC() + signedImmediateVal;                           // Add PC with the signed immediate. Similar to the above line but this uses the PC value stored in the register object
        if (!instruction.getBranchPredictionResult())     // If this branch was predicted to be not taken, only then execute this
        {
          //cpuRegisters.writeLR(cpuRegisters.readPC() + 1);                                          // Write the return address to the link regiter (Note LR = PC + 1) using the PC register value contained in the cpuRegisters object
          cpuRegisters.writeLR(instruction.getMemoryFetchLocation() + 1);                    // Write the return address to the link regiter (Note LR = PC + 1) using the memory fetch location value stored in the current instruction object
          // Push register contents to the stack. Stack pointer incremented implicitly for simplicity.
          for (int regNumber = 0; regNumber < GlobalConstants.BL_ITEMS_TO_PUSH; regNumber++)
          {
            if (regNumber < GlobalConstants.TOTAL_GP_REGISTERS)    // Push values stored in the GP registers on to the stack (Starting from R0 ... to R15). Note this causes the value of R0 to be at the bottom of the stack and the value of R15 to be at the top of the stack
            {
              cpuMemory.stackPush(cpuRegisters.readGP(regNumber));        
            }
            else              // Push value stored in the link register on to the stack
            {
              cpuMemory.stackPush(cpuRegisters.readLR());
            }
          }
          pContext.setBranchTaken(!instruction.getBranchPredictionResult());                // Assert that a branch needs to be taken
          pContext.setBranchTarget(calculationResult);                        // Set the branch target (i.e. PC = PC + Ix)
          pContext.setCorrectBranchPrediction(instruction.getBranchPredictionResult());     // Assert the BP global variable in the pipeline context for the simulator to check if the pipeline needs to be flushed
          cpuRegisters.incrementBranchPredictionsIncorrect();               // Increment incorrect branch prediction counter
        }
        else              // else do nothing, since the branch predictor predicted correct
        {
          cpuRegisters.incrementBranchPredictionsCorrect();               // Increment correct branch prediction counter
        }
        break;

      // RET --- Return from a function call - Used to return to the caller function
      case Isa.RET:
        if (!instruction.getBranchPredictionResult())     // If this branch was predicted to be not taken, only then execute this
        {
          // Pop register contents off the stack. Stack pointer decremented implicitly for simplicity
          for (int regNumber = (GlobalConstants.RET_ITEMS_TO_POP - 1); regNumber <= 0 ; regNumber--)
          {
            if (regNumber == GlobalConstants.TOTAL_GP_REGISTERS)     // Pop the value of the LR register off the stack
            {
              calculationResult = cpuMemory.stackPop(); 
            }
            else          // Pop the values of all GP registers off the stack (Starting from R15 ... to R0). This is because the value of R15 is stored on top of the stack and the value of RO is stored at the bottom of the stack.
            {
              cpuRegisters.writeGP(regNumber, cpuMemory.stackPop()); 
            }
          }
          pContext.setBranchTaken(!instruction.getBranchPredictionResult());                // Assert that a branch needs to be taken
          pContext.setBranchTarget(calculationResult);    // Set the branch target (i.e. PC = LR)
          pContext.setCorrectBranchPrediction(instruction.getBranchPredictionResult());     // Assert the BP global variable in the pipeline context for the simulator to check if the pipeline needs to be flushed
          cpuRegisters.incrementBranchPredictionsIncorrect();               // Increment incorrect branch prediction counter
        }
        else              // else do nothing, since the branch predictor predicted correct
        {
          cpuRegisters.incrementBranchPredictionsCorrect();               // Increment correct branch prediction counter
        }
        break;

      // Conditional branches
      // BEQ sr1, sr2, Ix --- Branch if two registers are equal
      case Isa.BEQ:
        signedImmediateVal = instruction.getSignedImmediateVal();
        calculationResult = instruction.getMemoryFetchLocation() + signedImmediateVal;     // Add instruction relative PC with the signed immediate, i.e. using the PC value (memory location from where the instruction was fetched) stored in the instruction object
        if (!instruction.getBranchPredictionResult())         // If this branch was predicted to be not taken
        {
          if (sourceReg1Val == sourceReg2Val)
          {
            pContext.setBranchTaken(!instruction.getBranchPredictionResult());    // Assert that a branch needs to be taken
            pContext.setBranchTarget(calculationResult);                          // Set the branch target (i.e. PC = PC + Ix)
            pContext.setCorrectBranchPrediction(instruction.getBranchPredictionResult());     // Assert the BP global variable in the pipeline context for the simulator to check if the pipeline needs to be flushed
            cpuRegisters.incrementBranchPredictionsIncorrect();               // Increment incorrect branch prediction counter
          }
          else              // else do nothing, since the branch predictor predicted correct
          {
            cpuRegisters.incrementBranchPredictionsCorrect();               // Increment correct branch prediction counter
          }
        }
        else        // If this branch was predicted taken
        {
          if (!(sourceReg1Val == sourceReg2Val))
          {
            pContext.setBranchTaken(instruction.getBranchPredictionResult());    // Assert that a branch needs to be taken
            pContext.setBranchTarget(instruction.getMemoryFetchLocation() + 1);   // Set the branch target (i.e. PC = Fetch location + 1)
            pContext.setCorrectBranchPrediction(!(instruction.getBranchPredictionResult()));     // Assert the BP global variable in the pipeline context for the simulator to check if the pipeline needs to be flushed
            cpuRegisters.incrementBranchPredictionsIncorrect();               // Increment incorrect branch prediction counter
          }
          else              // else do nothing, since the branch predictor predicted correct
          {
            cpuRegisters.incrementBranchPredictionsCorrect();               // Increment correct branch prediction counter
          }
        }
        break;

      // BEQ sr1, sr2, Ix --- Branch if two registers are not equal
      case Isa.BNE:
        signedImmediateVal = instruction.getSignedImmediateVal();
        calculationResult = instruction.getMemoryFetchLocation() + signedImmediateVal;     // Add instruction relative PC with the signed immediate 
        if (!instruction.getBranchPredictionResult())         // If this branch was predicted to be not taken
        {
          if (sourceReg1Val != sourceReg2Val)
          {
            pContext.setBranchTaken(!instruction.getBranchPredictionResult());    // Assert that a branch needs to be taken
            pContext.setBranchTarget(calculationResult);                          // Set the branch target (i.e. PC = PC + Ix)
            pContext.setCorrectBranchPrediction(instruction.getBranchPredictionResult());     // Assert the BP global variable in the pipeline context for the simulator to check if the pipeline needs to be flushed
            cpuRegisters.incrementBranchPredictionsIncorrect();               // Increment incorrect branch prediction counter
          }
          else              // else do nothing, since the branch predictor predicted correct
          {
            cpuRegisters.incrementBranchPredictionsCorrect();               // Increment correct branch prediction counter
          }
        }
        else        // If this branch was predicted taken
        {
          if (!(sourceReg1Val != sourceReg2Val))
          {
            pContext.setBranchTaken(instruction.getBranchPredictionResult());    // Assert that a branch needs to be taken
            pContext.setBranchTarget(instruction.getMemoryFetchLocation() + 1);   // Set the branch target (i.e. PC = Fetch location + 1)
            pContext.setCorrectBranchPrediction(!(instruction.getBranchPredictionResult()));     // Assert the BP global variable in the pipeline context for the simulator to check if the pipeline needs to be flushed
            cpuRegisters.incrementBranchPredictionsIncorrect();               // Increment incorrect branch prediction counter
          }
          else              // else do nothing, since the branch predictor predicted correct
          {
            cpuRegisters.incrementBranchPredictionsCorrect();               // Increment correct branch prediction counter
          }
        }
        break;

      // BEQ sr1, sr2, Ix --- Branch if sr1 < sr2
      case Isa.BLT:
        signedImmediateVal = instruction.getSignedImmediateVal();
        calculationResult = instruction.getMemoryFetchLocation() + signedImmediateVal;     // Add instruction relative PC with the signed immediate 
        if (!instruction.getBranchPredictionResult())         // If this branch was predicted to be not taken
        {
          if (sourceReg1Val < sourceReg2Val)
          {
            pContext.setBranchTaken(!instruction.getBranchPredictionResult());    // Assert that a branch needs to be taken
            pContext.setBranchTarget(calculationResult);                          // Set the branch target (i.e. PC = PC + Ix)
            pContext.setCorrectBranchPrediction(instruction.getBranchPredictionResult());     // Assert the BP global variable in the pipeline context for the simulator to check if the pipeline needs to be flushed
            cpuRegisters.incrementBranchPredictionsIncorrect();               // Increment incorrect branch prediction counter
          }
          else              // else do nothing, since the branch predictor predicted correct
          {
            cpuRegisters.incrementBranchPredictionsCorrect();               // Increment correct branch prediction counter
          }
        }
        else        // If this branch was predicted taken
        {
          if (!(sourceReg1Val < sourceReg2Val))
          {
            pContext.setBranchTaken(instruction.getBranchPredictionResult());    // Assert that a branch needs to be taken
            pContext.setBranchTarget(instruction.getMemoryFetchLocation() + 1);   // Set the branch target (i.e. PC = Fetch location + 1)
            pContext.setCorrectBranchPrediction(!(instruction.getBranchPredictionResult()));     // Assert the BP global variable in the pipeline context for the simulator to check if the pipeline needs to be flushed
            cpuRegisters.incrementBranchPredictionsIncorrect();               // Increment incorrect branch prediction counter
          }
          else              // else do nothing, since the branch predictor predicted correct
          {
            cpuRegisters.incrementBranchPredictionsCorrect();               // Increment correct branch prediction counter
          }
        }
        break;

      // BEQ sr1, sr2, Ix --- Branch if sr1 > sr2
      case Isa.BGT:
        signedImmediateVal = instruction.getSignedImmediateVal();
        calculationResult = instruction.getMemoryFetchLocation() + signedImmediateVal;     // Add instruction relative PC with the signed immediate 
        if (!instruction.getBranchPredictionResult())         // If this branch was predicted to be not taken
        {
          if (sourceReg1Val > sourceReg2Val)
          {
            pContext.setBranchTaken(!instruction.getBranchPredictionResult());    // Assert that a branch needs to be taken
            pContext.setBranchTarget(calculationResult);                          // Set the branch target (i.e. PC = PC + Ix)
            pContext.setCorrectBranchPrediction(instruction.getBranchPredictionResult());     // Assert the BP global variable in the pipeline context for the simulator to check if the pipeline needs to be flushed
            cpuRegisters.incrementBranchPredictionsIncorrect();               // Increment incorrect branch prediction counter
          }
          else              // else do nothing, since the branch predictor predicted correct
          {
            cpuRegisters.incrementBranchPredictionsCorrect();               // Increment correct branch prediction counter
          }
        }
        else        // If this branch was predicted taken
        {
          if (!(sourceReg1Val > sourceReg2Val))
          {
            pContext.setBranchTaken(instruction.getBranchPredictionResult());    // Assert that a branch needs to be taken
            pContext.setBranchTarget(instruction.getMemoryFetchLocation() + 1);   // Set the branch target (i.e. PC = Fetch location + 1)
            pContext.setCorrectBranchPrediction(!(instruction.getBranchPredictionResult()));     // Assert the BP global variable in the pipeline context for the simulator to check if the pipeline needs to be flushed
            cpuRegisters.incrementBranchPredictionsIncorrect();               // Increment incorrect branch prediction counter
          }
          else              // else do nothing, since the branch predictor predicted correct
          {
            cpuRegisters.incrementBranchPredictionsCorrect();               // Increment correct branch prediction counter
          }
        }
        break;
    }
    cpuRegisters.incrementInstructionBranchCounter();         // Increment the branch instruction counter
  }
}
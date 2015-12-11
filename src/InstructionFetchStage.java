/**
 * @file InstructionFetchStage.java
 * @author Ashish Tibrewal
 * @date 02.11.2015
 * @details This file contains the InstructionFetchStage class that handles the first stage of the pipeline. It is used to read instructions
 * from the main memory which is then passed to the next pipeline stage (i.e. InstructionDecode) for further processing.
 */

// Import packages
// Custom packages
import pipeline.*;

// Pre-defined Java packages
import java.util.*;
import java.lang.*;

/**
 * This class implements the Instruction Fetch (IF) stage of the processor.
 */
public class InstructionFetchStage implements IProcessorPipelineStage
{
  // Class/Instance fields
  // private Register cpuRegisters;
  // private Memory cpuMemory;
  private int programCounter;
  private int instruction;
  private boolean branchInstruction;             /** Variable stating whether the fetched instruction is a branch instruction */
  private boolean branchPredictorResult;         /** Branch predictor's result */
  private Register cpuRegisters;                 /** Reference to architectural registers */
  private Memory cpuMemory;                      /** Reference to main memory */
  private ProcessorPipelineContext pContext;     /** Reference to the processor pipeline context */

  // public InstructionFetch(Register cpuRegisters, Memory cpuMemory)
  // {
  //     this.cpuRegisters = cpuRegisters;
  //     this.cpuMemory = cpuMemory;
  // }


  // TODO Do some predecode to see if the fetched instruction is a branch instructions. Include branch predictor to predict if a branch is taken or not. The current desing assumes that a branch is always not taken (i.e. this is a static branch prediction scheme)
  public void execute(IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;                          // Explicitly cast context to ProcessorPipelineContext type
    cpuRegisters = pContext.getCpuRegisters();                              // Obtain and store the reference to the primary cpu registers object from the pipeline context (Doing this to avoid having to type it over and over again)
    cpuMemory = pContext.getCpuMemory();                                    // Obtain and store the reference to the primary cpu memory object from the pipeline context (Doing this to avoid having to type it over and over again)
    programCounter = cpuRegisters.readPC();                                 // Read value of the PC register
    instruction = cpuMemory.readValue(programCounter);                      // Read value from main memory at the location specified by the PC register
    //this.preDecode();                                                       // Pre-decode the instruction
    //pContext.setNextIR(instruction);                                        // Write value to the (next/output) instruction register (IR)
    pContext.setNextIR(this.preDecode());                                   // Write value to the (next/output) instruction register (IR)
    //cpuRegisters.writeIR(instruction);                                    // Write value to the instruction register (IR)
    pContext.setNextMemoryFetchLoc(programCounter);                         // Store the next value of the PC (i.e. the location in memory from where the instruction was fetched) to be used by the ID stage
    cpuRegisters.incrementPC();                                             // Increment value stored in the (temporary/shadow) PC register. Actual value is set in the instruction execute or memory access stage.
  }

  // TODO need to fill function contents accordingly - This would need to be execute when the branch prediction was incorrect
  public void flush(IPipelineContext context)
  {
    // Effectively set all the class fields to it's default values
    programCounter = GlobalConstants.DEFAULT_PC;
    instruction = GlobalConstants.DEFAULT_INSTRUCTION;
    branchInstruction = false;      // The default assumption is that the fetched instruction is not a branch instruction
    branchPredictorResult = GlobalConstants.DEFAULT_BRANCH_PREDICTION;
  }

  /**
   * Method to pre-decode the current instruction. Only useful when (pre) decoding branch instructions and when using the branch predictor
   * @return Next value to be stored in the instruction register (IR)
   */
  private int preDecode()
  {
    BranchPredictor branchPredictor;
    Instruction instruction = new Instruction(GlobalConstants.DEFAULT_INSTRUCTION_TYPE,
                                              GlobalConstants.DEFAULT_INSTRUCTION_MNEMONIC,
                                              GlobalConstants.DEFAULT_EXECUTION_UNIT,
                                              GlobalConstants.DEFAULT_INSTRUCTION_OPCODE, 
                                              GlobalConstants.DEFAULT_MEM_FETCH_LOC,
                                              GlobalConstants.DEFAULT_INSTRUCTION,
                                              Isa.InstructionType.RRR.NUMBER_OF_CYCLES,
                                              GlobalConstants.DEFAULT_BRANCH_PREDICTION,
                                              Isa.DEFAULT_REG_VALUE,
                                              Isa.DEFAULT_REG_VALUE,
                                              Isa.DEFAULT_REG_VALUE,
                                              Isa.DEFAULT_IMM_VALUE);                   // Need to insert a default value or else the compiler complains 
    String instructionMnemonic = GlobalConstants.DEFAULT_INSTRUCTION_MNEMONIC;          // Need to insert a default value or else the compiler complains 
    String instructionType = GlobalConstants.DEFAULT_INSTRUCTION_TYPE;                  // Need to insert a default value or else the compiler complains
    ExecutionUnit executionUnit = GlobalConstants.DEFAULT_EXECUTION_UNIT;               // Need to insert a default value or else the compiler complains
    int sourceReg1;    
    int sourceReg2;    
    int destinationReg;
    int signedImmediate;
    int signedImmediateVal;
    int calculationResult = 0;                      // Need to insert a default value or else the compiler complains
    String instructionBinary = Utility.convertToBin(this.instruction, 0);   // Not using the Integer.toBinaryString() method because it truncates leading binary zero characters.
    int opCode = (this.instruction >> (Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH)) & (int)(Math.pow(2, Isa.OPCODE_LENGTH) - 1);     // Extract instruction OpCode (Logical AND with 31 since its 11111 in binary and opcode length is )
    if(opCode > (Isa.ISA_TOTAL_INSTRUCTIONS - 1))     // Check if the opCode is valid (i.e. check if it's a valid instruction)
    {
      throw new IllegalInstructionException("Illegal instruction (Instruction with OpCode \"" + Utility.convertToBin(opCode, 0).substring((Isa.INSTRUCTION_LENGTH - Isa.OPCODE_LENGTH), Isa.INSTRUCTION_LENGTH) + "\" is not specified in the ISA)."); 
    }

    switch (opCode)      // Only need to predecode branch instructions and feed it to the branch prediction (BP) unit
    {
      // Unconditional branches
      // BU Ix
      case Isa.BU:
        instructionMnemonic = "BU";
        instructionType = "I";
        executionUnit = ExecutionUnit.BU;
        branchInstruction = true;
        break;

      // BL Ix
      case Isa.BL:
        instructionMnemonic = "BL";
        instructionType = "I";
        executionUnit = ExecutionUnit.BU;
        branchInstruction = true;
        break;

      // RET
      case Isa.RET:
        instructionMnemonic = "RET";
        instructionType = "RRR";
        executionUnit = ExecutionUnit.BU;
        branchInstruction = true;
        break;

      // Conditional branches
      // BEQ sr1, sr2, Ix
      case Isa.BEQ:
        instructionMnemonic = "BEQ";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.BU;
        branchInstruction = true;
        break;

      // BNE sr1, sr2, Ix
      case Isa.BNE:
        instructionMnemonic = "BNE";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.BU;
        branchInstruction = true;
        break;

      // BLT sr1, sr2, Ix
      case Isa.BLT:
        instructionMnemonic = "BLT";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.BU;
        branchInstruction = true;
        break;

      // BGT sr1, sr2, Ix
      case Isa.BGT:
        instructionMnemonic = "BGT";
        instructionType = "RRI";
        executionUnit = ExecutionUnit.BU;
        branchInstruction = true;
        break;

      // Non-branch instruction
      default:
        branchInstruction = false;        // The instruction fetched is not a branch instruction
        return this.instruction;          // Return back to the main execute function of the IF stage since the instruction fetched is a non-branch instruction
    }

    if (branchInstruction == true)        // Only execute this section if the fetched instruction is a branch instruction. Don't really need this check since the function returns to the caller before it can get here if it's not a branch instruction
    {
      switch (instructionType)            // Branch instructions can either be of RRR or RRI or I type only. It cannot be of RR or RI type. Note all source register location "fetches" are irrelevant since they are not used here - Only being done to be able to create an instruction object.
      {
        // RRR type
        case "RRR":
        sourceReg1 = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RRR.S1_START, Isa.InstructionType.RRR.S1_END), false);
        sourceReg2 = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RRR.S2_START, Isa.InstructionType.RRR.S2_END), false);
        destinationReg = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RRR.D_START, Isa.InstructionType.RRR.D_END), false);
        signedImmediate = Isa.DEFAULT_IMM_VALUE;
        instruction = new Instruction(instructionType,
                                      instructionMnemonic,
                                      executionUnit,
                                      opCode, 
                                      programCounter,
                                      this.instruction,
                                      Isa.InstructionType.RRR.NUMBER_OF_CYCLES,
                                      GlobalConstants.DEFAULT_BRANCH_PREDICTION,
                                      sourceReg1,
                                      sourceReg2,
                                      destinationReg,
                                      signedImmediate);
        break;
      
        // RRI type
        case "RRI":
          sourceReg1 = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RRI.S1_START, Isa.InstructionType.RRI.S1_END), false);
          sourceReg2 = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RRI.D_START, Isa.InstructionType.RRI.D_END), false);
          destinationReg =  Isa.DEFAULT_REG_VALUE;
          signedImmediate = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.RRI.IMM_START, Isa.InstructionType.RRI.IMM_END), true);
          instruction = new Instruction(instructionType,
                                        instructionMnemonic,
                                        executionUnit,
                                        opCode, 
                                        programCounter,
                                        this.instruction,
                                        Isa.InstructionType.RRI.NUMBER_OF_CYCLES,
                                        GlobalConstants.DEFAULT_BRANCH_PREDICTION,
                                        sourceReg1,
                                        sourceReg2,
                                        destinationReg,
                                        signedImmediate);
          break;

        // I type
        case "I":
          sourceReg1 = Isa.DEFAULT_REG_VALUE;
          sourceReg2 = Isa.DEFAULT_REG_VALUE;
          destinationReg = Isa.DEFAULT_REG_VALUE;
          signedImmediate = Utility.convertToInt(instructionBinary.substring(Isa.InstructionType.I.IMM_START, Isa.InstructionType.I.IMM_END), true);
          instruction = new Instruction(instructionType,
                                        instructionMnemonic,
                                        executionUnit,
                                        opCode, 
                                        programCounter,
                                        this.instruction,
                                        Isa.InstructionType.I.NUMBER_OF_CYCLES,
                                        GlobalConstants.DEFAULT_BRANCH_PREDICTION,
                                        signedImmediate);
          break;

        // Shoudn't get here since it's an invalid instruction type
        default:
          throw new IllegalInstructionException("Invalid instruction type for the fetched branch instruction! Instruction information couldn't be extracted! Check the executable/binary files for possible issues!");
      }
      branchPredictor = pContext.getBranchPredictor();                              // Obtain a reference to the processor's branch prediction unit
      branchPredictorResult = branchPredictor.predict(instruction);                 // Call the predict method in the processor's branch predictor unit
      pContext.setNextInstructionBranchPredictionResult(branchPredictorResult);  
      if (branchPredictorResult == true)      // If branch predictor predicts true for the fetched instruction - Update temporary PC to branch target in the IF stage itself, otherwise carry on with normal execution
      {
        switch (instruction.getOpCode())
        {
          // Unconditional branches
          // BU Ix --- Unconditional branch --- Used in loops (when branching back to the start of the loop - do while, while and for loops)
          case Isa.BU:
            signedImmediateVal = instruction.getSignedImmediateVal();
            calculationResult = instruction.getMemoryFetchLocation() + signedImmediateVal;     // Add instruction relative PC with the signed immediate
            //calculationResult = cpuRegisters.readPC() + signedImmediateVal;                           // Add PC with the signed immediate. Similar to the above line but this uses the PC value stored in the register object
            pContext.setBranchTaken(branchPredictorResult);
            pContext.setBranchTarget(calculationResult);
            break;

          // BL Ix --- Unconditional branch with link --- Used in function calls (i.e. jumping to different labels in assembly code)
          case Isa.BL:
            signedImmediateVal = instruction.getSignedImmediateVal();
            calculationResult = instruction.getMemoryFetchLocation() + signedImmediateVal;     // Add instruction relative PC with the signed immediate, i.e. using the PC value (memory location from where the instruction was fetched) stored in the instruction object
            //calculationResult = cpuRegisters.readPC() + signedImmediateVal;                           // Add PC with the signed immediate. Similar to the above line but this uses the PC value stored in the register object
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
            pContext.setBranchTaken(branchPredictorResult); // Assert that a branch needs to be taken
            pContext.setBranchTarget(calculationResult);    // Set the branch target (i.e. PC = PC + Ix)
            break;

          // RET --- Return from a function call - Used to return to the caller function
          case Isa.RET:
            // Pop register contents off the stack. Stack pointer decremented implicitly for simplicity
            for (int regNumber = (GlobalConstants.RET_ITEMS_TO_POP - 1); regNumber >= 0 ; regNumber--)
            {
              if (regNumber == GlobalConstants.TOTAL_GP_REGISTERS)     // Pop the value of the LR register off the stack
              {
                calculationResult = cpuMemory.stackPop();
              }
              else          // Pop the values of all GP registers off the stack (Starting from R15 ... to R0). This is because the value of R15 is stored on top of the stack and the value of RO is stored at the bottom of the stack.
              {
                if (regNumber != 0)     // Check to make sure that a value is not written to R0 since R0 is write protected
                {
                  cpuRegisters.writeGP(regNumber, cpuMemory.stackPop()); 
                }
                else
                {
                  cpuMemory.stackPop();         // Pop the value for R0 off the stack but don't write it back to R0
                }
              }
            }
            pContext.setBranchTaken(branchPredictorResult); // Assert that a branch needs to be taken
            pContext.setBranchTarget(calculationResult);    // Set the branch target (i.e. PC = LR)
            break;

          // Conditional branches
          // BEQ sr1, sr2, Ix --- Branch if two registers are equal
          case Isa.BEQ:
            signedImmediateVal = instruction.getSignedImmediateVal();
            calculationResult = instruction.getMemoryFetchLocation() + signedImmediateVal;     // Add instruction relative PC with the signed immediate, i.e. using the PC value (memory location from where the instruction was fetched) stored in the instruction object
            pContext.setBranchTaken(branchPredictorResult);
            pContext.setBranchTarget(calculationResult);    // Set the branch target (i.e. PC = PC + Ix)
            break;

          // BEQ sr1, sr2, Ix --- Branch if two registers are not equal
          case Isa.BNE:
            signedImmediateVal = instruction.getSignedImmediateVal();
            calculationResult = instruction.getMemoryFetchLocation() + signedImmediateVal;     // Add instruction relative PC with the signed immediate 
            pContext.setBranchTaken(branchPredictorResult);
            pContext.setBranchTarget(calculationResult);    // Set the branch target (i.e. PC = PC + Ix)
            break;

          // BEQ sr1, sr2, Ix --- Branch if sr1 < sr2
          case Isa.BLT:
            signedImmediateVal = instruction.getSignedImmediateVal();
            calculationResult = instruction.getMemoryFetchLocation() + signedImmediateVal;     // Add instruction relative PC with the signed immediate 
            pContext.setBranchTaken(branchPredictorResult);
            pContext.setBranchTarget(calculationResult);    // Set the branch target (i.e. PC = PC + Ix)
            break;

          // BEQ sr1, sr2, Ix --- Branhc if sr1 > sr2
          case Isa.BGT:
            signedImmediateVal = instruction.getSignedImmediateVal();
            calculationResult = instruction.getMemoryFetchLocation() + signedImmediateVal;     // Add instruction relative PC with the signed immediate 
            pContext.setBranchTaken(branchPredictorResult);
            pContext.setBranchTarget(calculationResult);    // Set the branch target (i.e. PC = PC + Ix)
            break;

          // Shouldn't get here since all different types of branches have been covered
          default:
            throw new IllegalInstructionException("Invalid pre-decoded branch instruction! Check the executable/binary files for possible issues!");
        } 
      }
      // If branch predictor predicts false for the fetched instruction - DO NOTHING. THIS IS ONLY USEFUL FOR CONDITIONAL INSTRUCTIONS AND IS TAKEN CARE OF IN THE INSTRUCTION EXECUTE STAGE SINCE A UNCONDITIONAL BRANCH IS ALWAYS PREDICTED TO BE TAKEN.
    }

    /*if (instruction.getOpCode() == Isa.BU || instruction.getOpCode() == Isa.BL || instruction.getOpCode() == Isa.RET)     // Unconditional branch instructions
    {
      // TODO return the original instruction and check if the branch prediction was correct in the execute stage. Cannot convert this to a NOP since a branch is still effectively a valid instruction
      return GlobalConstants.DEFAULT_INSTRUCTION;    // Set the next IR value to a NOP instruction since the unconditional branch has been taken care of here
    }
    else                      // All other instructions (Including conditional branch instructions)
    {
      return this.instruction;    // Set the next IR value to the fetched instruction
    }*/
    return this.instruction;
  }

  /**
   * Method to obtain the value of the PC that has been read by the IF stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current PC value that has been read by the IF stage
   */
  public int getCurrentPC()
  {
    return programCounter;
  }

  /**
   * Method to obtain if the latest fetched instruction is a branch instruction
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Boolean variable stating if the latest fetched instruction is a branch instruction
   */
  public boolean getBranchInstruction()
  {
    return branchInstruction;
  }

  /**
   * Method to obtain the branch predictor's result
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Branch predictor's result
   */
  public boolean getBranchPredictorResult()
  {
    return branchPredictorResult;
  }

  /**
   * Method to reset the value of the branchPredictorResult field back to its default value
   */
  public void resetBranchPredictorResult()
  {
    branchPredictorResult = GlobalConstants.DEFAULT_BRANCH_PREDICTION;
  }

  /**
   * Method to obtain the value of the instruction that has been read (from main memory) by the IF stage in the current (running) cycle.
   * USED ONLY FOR PRINTING AND DEBUGGING.
   * @return Current instruction value that has been read by the IF stage
   */
  public int getCurrentInstructionRead()
  {
    return instruction;
  }
}

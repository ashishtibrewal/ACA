import pipeline.*;
/**
 * Class that implements the Arithmetic Logic Unit (ALU) of the processor
 */
public class Alu implements IExecutionUnit
{
  private int opCode;
  private int sourceReg1Val;
  private int sourceReg2Val;
  private int signedImmediateVal;
  private int calculationResult;
  private Register cpuRegisters;
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */

  public void execute(Instruction instruction, IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    cpuRegisters = pContext.getCpuRegisters();                 // Obtain and store the reference to the primary cpu registers object from the pipeline context (Doing this to avoid having to type it over and over again)
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
    if (instruction.getSourceReg2Loc() == pContext.getCurrentInstructionWriteBack().getDestinationRegLoc())       // Check if the WB stage is going to write to a register that needs to be read by the ALU in the IE stage
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
      // NOP
      case Isa.NOP:
        calculationResult = sourceReg1Val + sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;
      
      // ADD dr, sr1, sr2
      case Isa.ADD:
        calculationResult = sourceReg1Val + sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // SUB dr, sr1, sr2
      case Isa.SUB:
        calculationResult = sourceReg1Val - sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // MULT dr, sr1, sr2
      case Isa.MULT:
        calculationResult = sourceReg1Val * sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // DIV dr, sr1, sr2
      case Isa.DIV:
        calculationResult = sourceReg1Val / sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        
        break;

      // ADDI dr, sr1, Ix
      case Isa.ADDI:
        signedImmediateVal = instruction.getSignedImmediateVal();
        calculationResult = sourceReg1Val + signedImmediateVal;
        instruction.setWritebackVal(calculationResult);
        break;

      // SUBI dr, sr1, Ix
      case Isa.SUBI:
        signedImmediateVal = instruction.getSignedImmediateVal();
        calculationResult = sourceReg1Val - signedImmediateVal;
        instruction.setWritebackVal(calculationResult);
        
        break;

      // AND dr, sr1, sr2
      case Isa.AND:
        calculationResult = sourceReg1Val & sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // OR dr, sr1, sr2 
      case Isa.OR:
        calculationResult = sourceReg1Val | sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // XOR dr, sr1, sr2
      case Isa.XOR:
        calculationResult = sourceReg1Val ^ sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // NOT dr, sr1
      case Isa.NOT:
        calculationResult = ~ sourceReg1Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // SLL dr, sr1, Ix
      case Isa.SLL:
        signedImmediateVal = instruction.getSignedImmediateVal();
        calculationResult = sourceReg1Val << signedImmediateVal;
        instruction.setWritebackVal(calculationResult);
        break;

      // SLR dr, sr1, Ix
      case Isa.SLR:
        signedImmediateVal = instruction.getSignedImmediateVal();
        calculationResult = sourceReg1Val >>> signedImmediateVal;
        instruction.setWritebackVal(calculationResult);
        break;

      // SLLV dr, s1, sr2
      case Isa.SLLV:
        calculationResult = sourceReg1Val << sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // SRA dr, sr1, Ix
      case Isa.SRA:
        signedImmediateVal = instruction.getSignedImmediateVal();
        calculationResult = sourceReg1Val >> signedImmediateVal;
        instruction.setWritebackVal(calculationResult);
        break;

      // Default case. This condition should never be reached
      default:
        System.out.println("Fatal error! Incorrect execution unit (EU) used for the instruction!");
        break;
    }
  }
}
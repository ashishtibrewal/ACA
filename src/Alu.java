import pipeline.*;
/**
 * Class that implements the Arithmetic Logic Unit (ALU) of the processor
 */
public class Alu implements IExecutionUnit
{
  private int opCode;
  private int sourceReg1Val;
  private int sourceReg2Val;
  private int destinationRegLoc;
  private int signedImmediateVal;
  private int calculationResult;
  private Register cpuRegisters;
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */

  public void execute(Instruction instruction, IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    cpuRegisters = pContext.getCpuRegisters();                 // Obtain and store the reference to the primary cpu registers object from the pipeline context (Doing this to avoid having to type it over and over again)
    opCode = instruction.getOpCode();
    switch (opCode)
    {
      // NOP
      case Isa.NOP:
        sourceReg1Val = instruction.getSourceReg1Val();
        sourceReg2Val = instruction.getSourceReg2Val();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val + sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;
      
      // ADD dr, sr1, sr2
      case Isa.ADD:
        sourceReg1Val = instruction.getSourceReg1Val();
        sourceReg2Val = instruction.getSourceReg2Val();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val + sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // SUB dr, sr1, sr2
      case Isa.SUB:
        sourceReg1Val = instruction.getSourceReg1Val();
        sourceReg2Val = instruction.getSourceReg2Val();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val - sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // MULT dr, sr1, sr2
      case Isa.MULT:
        sourceReg1Val = instruction.getSourceReg1Val();
        sourceReg2Val = instruction.getSourceReg2Val();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val * sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // DIV dr, sr1, sr2
      case Isa.DIV:
        sourceReg1Val = instruction.getSourceReg1Val();
        sourceReg2Val = instruction.getSourceReg2Val();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val / sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        
        break;

      // ADDI dr, sr1, Ix
      case Isa.ADDI:
        sourceReg1Val = instruction.getSourceReg1Val();
        signedImmediateVal = instruction.getSignedImmediateVal();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val + signedImmediateVal;
        instruction.setWritebackVal(calculationResult);
        break;

      // SUBI dr, sr1, Ix
      case Isa.SUBI:
        sourceReg1Val = instruction.getSourceReg1Val();
        signedImmediateVal = instruction.getSignedImmediateVal();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val - signedImmediateVal;
        instruction.setWritebackVal(calculationResult);
        
        break;

      // AND dr, sr1, sr2
      case Isa.AND:
        sourceReg1Val = instruction.getSourceReg1Val();
        sourceReg2Val = instruction.getSourceReg2Val();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val & sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // OR dr, sr1, sr2 
      case Isa.OR:
        sourceReg1Val = instruction.getSourceReg1Val();
        sourceReg2Val = instruction.getSourceReg2Val();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val | sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // XOR dr, sr1, sr2
      case Isa.XOR:
        sourceReg1Val = instruction.getSourceReg1Val();
        sourceReg2Val = instruction.getSourceReg2Val();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val ^ sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // NOT dr, sr1
      case Isa.NOT:
        sourceReg1Val = instruction.getSourceReg1Val();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = ~ sourceReg1Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // SLL dr, sr1, Ix
      case Isa.SLL:
        sourceReg1Val = instruction.getSourceReg1Val();
        signedImmediateVal = instruction.getSignedImmediateVal();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val << signedImmediateVal;
        instruction.setWritebackVal(calculationResult);
        break;

      // SLR dr, sr1, Ix
      case Isa.SLR:
        sourceReg1Val = instruction.getSourceReg1Val();
        signedImmediateVal = instruction.getSignedImmediateVal();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val >>> signedImmediateVal;
        instruction.setWritebackVal(calculationResult);
        break;

      // SLLV dr, s1, sr2
      case Isa.SLLV:
        sourceReg1Val = instruction.getSourceReg1Val();
        sourceReg2Val = instruction.getSourceReg2Val();
        destinationRegLoc = instruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val << sourceReg2Val;
        instruction.setWritebackVal(calculationResult);
        break;

      // SRA dr, sr1, Ix
      case Isa.SRA:
        sourceReg1Val = instruction.getSourceReg1Val();
        signedImmediateVal = instruction.getSignedImmediateVal();
        destinationRegLoc = instruction.getDestinationRegLoc();
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
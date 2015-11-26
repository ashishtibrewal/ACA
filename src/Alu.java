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

  public void execute(Instruction currentInstruction, IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    cpuRegisters = pContext.getCpuRegisters();                 // Obtain and store the reference to the primary cpu registers object from the pipeline context (Doing this to avoid having to type it over and over again)
    opCode = currentInstruction.getOpCode();
    switch (opCode)
    {
      // NOP
      case Isa.NOP:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val + sourceReg2Val;
        //cpuRegisters.writeGP(destinationRegLoc, calculationResult);    // Prevent any writes to registers for the NOP operation
        break;
      
      // ADD dr, sr1, sr2
      case Isa.ADD:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val + sourceReg2Val;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        break;

      // SUB dr, sr1, sr2
      case Isa.SUB:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val - sourceReg2Val;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        break;

      // MULT dr, sr1, sr2
      case Isa.MULT:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val * sourceReg2Val;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        break;

      // DIV dr, sr1, sr2
      case Isa.DIV:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val / sourceReg2Val;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        
        break;

      // ADDI dr, sr1, Ix
      case Isa.ADDI:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        signedImmediateVal = currentInstruction.getSignedImmediateVal();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val + signedImmediateVal;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        break;

      // SUBI dr, sr1, Ix
      case Isa.SUBI:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        signedImmediateVal = currentInstruction.getSignedImmediateVal();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val - signedImmediateVal;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        
        break;

      // AND dr, sr1, sr2
      case Isa.AND:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val & sourceReg2Val;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        break;

      // OR dr, sr1, sr2 
      case Isa.OR:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val | sourceReg2Val;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        break;

      // XOR dr, sr1, sr2
      case Isa.XOR:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val ^ sourceReg2Val;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        break;

      // NOT dr, sr1
      case Isa.NOT:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = ~ sourceReg1Val;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        break;

      // SLL dr, sr1, Ix
      case Isa.SLL:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        signedImmediateVal = currentInstruction.getSignedImmediateVal();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val << signedImmediateVal;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        break;

      // SLR dr, sr1, Ix
      case Isa.SLR:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        signedImmediateVal = currentInstruction.getSignedImmediateVal();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val >>> signedImmediateVal;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        break;

      // SLLV dr, s1, sr2
      case Isa.SLLV:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val << sourceReg2Val;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        break;

      // SRA dr, sr1, Ix
      case Isa.SRA:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        signedImmediateVal = currentInstruction.getSignedImmediateVal();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationResult = sourceReg1Val >> signedImmediateVal;
        cpuRegisters.writeGP(destinationRegLoc, calculationResult);
        break;

      // Default case. This condition should never be reached
      default:
        System.out.println("Fatal error! Incorrect execution unit (EU) used for the instruction!");
        break;
    }
  }
}
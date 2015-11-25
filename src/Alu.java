import pipeline.*;
/**
 * Class that implements the Arithmetic Logic Unit (ALU) of the processor
 */
public class Alu implements IExecutionUnit
{
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */
  // TODO Add all required functionality for the ALU
  private int opCode;
  private int sourceReg1Val;
  private int sourceReg2Val;
  private int destinationRegLoc;
  private int signedImmediateVal;
  private int calculationValue;
  public void execute(Instruction currentInstruction, IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    opCode = currentInstruction.getOpCode();
    switch (opCode)
    {
      // NOP
      case Isa.NOP:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val + sourceReg2Val;
        //pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);    // Prevent any writes to registers for the NOP operation
        break;
      
      // ADD dr, sr1, sr2
      case Isa.ADD:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val + sourceReg2Val;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        break;

      // SUB dr, sr1, sr2
      case Isa.SUB:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val - sourceReg2Val;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        break;

      // MULT dr, sr1, sr2
      case Isa.MULT:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val * sourceReg2Val;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        break;

      // DIV dr, sr1, sr2
      case Isa.DIV:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val / sourceReg2Val;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        
        break;

      // ADDI dr, sr1, Ix
      case Isa.ADDI:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        signedImmediateVal = Utility.convertToInt(Utility.signExtend(Integer.toBinaryString(currentInstruction.getSignedImmediateVal())),false);   // TODO check if sign extension of the immediate value works correctly (or else it would fail with negative numbers)
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val + signedImmediateVal;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        break;

      // SUBI dr, sr1, Ix
      case Isa.SUBI:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        signedImmediateVal = Utility.convertToInt(Utility.signExtend(Integer.toBinaryString(currentInstruction.getSignedImmediateVal())),false);   // TODO check if sign extension of the immediate value works correctly (or else it would fail with negative numbers)
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val - signedImmediateVal;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        
        break;

      // AND dr, sr1, sr2
      case Isa.AND:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val & sourceReg2Val;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        break;

      // OR dr, sr1, sr2 
      case Isa.OR:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val | sourceReg2Val;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        break;

      // XOR dr, sr1, sr2
      case Isa.XOR:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val ^ sourceReg2Val;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        break;

      // NOT dr, sr1
      case Isa.NOT:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = ~ sourceReg1Val;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        break;

      // SLL dr, sr1, Ix
      case Isa.SLL:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        signedImmediateVal = Utility.convertToInt(Utility.signExtend(Integer.toBinaryString(currentInstruction.getSignedImmediateVal())),false);   // TODO check if sign extension of the immediate value works correctly (or else it would fail with negative numbers)
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val << signedImmediateVal;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        break;

      // SLR dr, sr1, Ix
      case Isa.SLR:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        signedImmediateVal = Utility.convertToInt(Utility.signExtend(Integer.toBinaryString(currentInstruction.getSignedImmediateVal())),false);   // TODO check if sign extension of the immediate value works correctly (or else it would fail with negative numbers)
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val >>> signedImmediateVal;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        break;

      // SLLV dr, s1, sr2
      case Isa.SLLV:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        sourceReg2Val = currentInstruction.getSourceReg2Val();
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val << sourceReg2Val;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        break;

      // SRA dr, sr1, Ix
      case Isa.SRA:
        sourceReg1Val = currentInstruction.getSourceReg1Val();
        signedImmediateVal = Utility.convertToInt(Utility.signExtend(Integer.toBinaryString(currentInstruction.getSignedImmediateVal())),false);   // TODO check if sign extension of the immediate value works correctly (or else it would fail with negative numbers)
        destinationRegLoc = currentInstruction.getDestinationRegLoc();
        calculationValue = sourceReg1Val >> signedImmediateVal;
        pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
        break;

      // Default case. This condition should never be reached
      default:
        System.out.println("Fatal error! Incorrect execution unit (EU) used for the instruction!");
        break;
    }
  }
}
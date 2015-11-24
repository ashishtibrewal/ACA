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
  private int signedImmediate;
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
        //pContext.getCpuRegisters().writeGP(destinationRegLoc, calculationValue);
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
        
        break;

      // DIV dr, sr1, sr2
      case Isa.DIV:
        
        break;

      // ADDI dr, sr1, Ix
      case Isa.ADDI:
        
        break;

      // SUBI dr, sr1, Ix
      case Isa.SUBI:
        
        break;

      // AND dr, sr1, sr2
      case Isa.AND:
        
        break;

      // OR dr, sr1, sr2 
      case Isa.OR:
        
        break;

      // XOR dr, sr1, sr2
      case Isa.XOR:
        
        break;

      // NOT dr, sr1, sr2
      case Isa.NOT:
        
        break;

      // SLL dr, sr1, Ix
      case Isa.SLL:
        
        break;

      // SLR dr, sr1, Ix
      case Isa.SLR:
        
        break;

      // SLLV dr, s1, sr2
      case Isa.SLLV:
        
        break;

      // SRA dr, sr1, Ix
      case Isa.SRA:
        
        break;

      // Default case. This condition should never be reached
      default:
        System.out.println("Fatal error! Incorrect execution unit (EU) used for the instruction!");
        break;
    }
  }
}
import pipeline.*;
/**
 * Class that implements the Load-Store Unit (LSU) of the processor
 */
public class Lsu implements IExecutionUnit
{
  // The LSU is the only execution unit that has access to the main memory. Would need an adder (maybe a simple alu) inside the LSU to calculate memory address (including offsets)
  private int opCode;
  private int sourceReg1Val;
  private int destinationRegLoc;
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
    switch (opCode)
    {
      // LW dr, sr1, Ix --- dr = mem[sr1 + Ix]    (Load word from memory, i.e. read from main memory)
      case Isa.LW:
      //signedImmediateVal = Utility.convertToInt(Utility.signExtend(Integer.toBinaryString(instruction.getSignedImmediateVal())), false);
      signedImmediateVal = instruction.getSignedImmediateVal();
      calculationResult = sourceReg1Val + signedImmediateVal;       // Evaluate the memory address
      cpuRegisters.writeMAR(calculationResult);       // Write the calculated memory address to the memory address register (MAR). Not very useful in the current design but can come in really handy when extending the pipeline to 5 stages, one that includes a memory access stage.
      cpuRegisters.writeMDR(cpuMemory.readValue(cpuRegisters.readMAR()));             // Read the value from main memory and write it into the memory data register (MDR) using the memory address register (MAR)
      //cpuRegisters.writeMDR(cpuMemory.readValue(calculationResult));                              // Read the value from main memory and write it into the memroy data register (MDR) directly using the calculated value
      destinationRegLoc = instruction.getDestinationRegLoc();                                // Obtain the location of the destination register from the current instruction object
      //cpuRegisters.writeGP(destinationRegLoc, cpuRegisters.readMDR());                              // Write the value to the required GP register
      //instruction.setWritebackVal(cpuRegisters.readMDR());
      instruction.setWritebackVal(cpuMemory.readValue(calculationResult));
      break;

      // SW sr1, dr, Ix --- mem[dr + Ix] = sr1    (Store word to memory, i.e. write to main memory)
      case Isa.SW:
      //signedImmediateVal = Utility.convertToInt(Utility.signExtend(Integer.toBinaryString(instruction.getSignedImmediateVal())), false);
      signedImmediateVal = instruction.getSignedImmediateVal();
      destinationRegLoc = instruction.getDestinationRegLoc();                                // Obtain the location of the destination register from the current instruction object
      calculationResult = cpuRegisters.readGP(destinationRegLoc) + signedImmediateVal;    // Evaluate the memory address      // TODO Check if the destinationRegVal needs to be added to the instruction object. If yes, does it need to be updated in the instruction issue stage ?
      cpuRegisters.writeMAR(calculationResult);       // Write the calculated memory address to the memory address register (MAR). Not very useful in the current design but can come in really handy when extending the pipeline to 5 stages, one that includes a memory access stage.
      cpuRegisters.writeMDR(sourceReg1Val);           // Write the required source register value to the memory data register (MDR)
      //cpuMemory.writeValue(cpuRegisters.readMAR(), sourceReg1Val);    // Write the required value to memory directly using the source register value 
      cpuMemory.writeValue(cpuRegisters.readMAR(), cpuRegisters.readMDR());    // Write the required value to memory using the value stored in the memory data register (MDR)
      break;

      // MOVI dr, Ix
      case Isa.MOVI:
      //signedImmediateVal = Utility.convertToInt(Utility.signExtend(Integer.toBinaryString(instruction.getSignedImmediateVal())), false);
      signedImmediateVal = instruction.getSignedImmediateVal();  
      destinationRegLoc = instruction.getDestinationRegLoc();                                // Obtain the location of the destination register from the current instruction object
      instruction.setWritebackVal(signedImmediateVal);
      break;

      // MOVR dr, sr1
      case Isa.MOVR:
      destinationRegLoc = instruction.getDestinationRegLoc();                                // Obtain the location of the destination register from the current instruction object
      instruction.setWritebackVal(sourceReg1Val);
      break;

      default:
      System.out.println("Fatal error! Incorrect execution unit (EU) used for the instruction!");
      break;
    }
  }
}
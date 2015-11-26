import pipeline.*;
/**
 * Class that implements the Load-Store Unit (LSU) of the processor
 */
public class Lsu implements IExecutionUnit
{
  // TODO Add all required functionality for the LSU
  // TODO The LSU is the only execution unit that has access to the main memory. Would need an adder (maybe a simple alu) inside the LSU to calculate memory address (including offsets)
  private int opCode;
  private int sourceReg1Val;
  private int destinationRegLoc;
  private int signedImmediateVal;
  private int calculationResult;
  private Register cpuRegisters;
  private Memory cpuMemory;
  private ProcessorPipelineContext pContext;    /** Reference to the processor pipeline context */

  public void execute(Instruction currentInstruction, IPipelineContext context)
  {
    pContext = (ProcessorPipelineContext) context;             // Explicitly cast context to ProcessorPipelineContext type
    cpuRegisters = pContext.getCpuRegisters();                 // Obtain and store the reference to the primary cpu registers object from the pipeline context (Doing this to avoid having to type it over and over again)
    cpuMemory = pContext.getCpuMemory();                       // Obtain and store the reference to the primary cpu memory object from the pipeline context (Doing this to avoid having to type it over and over again)
    opCode = currentInstruction.getOpCode();
    switch (opCode)
    {
      // LW dr, sr1, Ix --- dr = mem[sr1 + Ix]    (Load word from memory, i.e. read from main memory)
      case Isa.LW:
      sourceReg1Val = currentInstruction.getSourceReg1Val();
      signedImmediateVal = Utility.convertToInt(Utility.signExtend(Integer.toBinaryString(currentInstruction.getSignedImmediateVal())), false);
      calculationResult = sourceReg1Val + signedImmediateVal;       // Evaluate the memory address
      cpuRegisters.writeMAR(calculationResult);       // Write the calculated memory address to the memory address register (MAR). Not very useful in the current design but can come in really handy when extending the pipeline to 5 stages, one that includes a memory access stage.
      cpuRegisters.writeMDR(cpuMemory.readValue(cpuRegisters.readMAR()));             // Read the value from main memory and write it into the memory data register (MDR) using the memory address register (MAR)
      //cpuRegisters.writeMDR(cpuMemory.readValue(calculationResult));                              // Read the value from main memory and write it into the memroy data register (MDR) directly using the calculated value
      destinationRegLoc = currentInstruction.getDestinationRegLoc();                                // Obtain the location of the destination register from the current instruction object
      cpuRegisters.writeGP(destinationRegLoc, cpuRegisters.readMDR());                              // Write the value to the required GP register
      break;

      // SW sr1, dr, Ix --- mem[dr + Ix] = sr1    (Store word to memory, i.e. write to main memory)
      case Isa.SW:
      sourceReg1Val = currentInstruction.getSourceReg1Val();
      signedImmediateVal = Utility.convertToInt(Utility.signExtend(Integer.toBinaryString(currentInstruction.getSignedImmediateVal())), false);
      destinationRegLoc = currentInstruction.getDestinationRegLoc();                                // Obtain the location of the destination register from the current instruction object
      calculationResult = cpuRegisters.readGP(destinationRegLoc) + signedImmediateVal;    // Evaluate the memory address      // TODO Check if the destinationRegVal needs to be added to the instruction object. If yes, does it need to be updated in the instruction issue stage ?
      cpuRegisters.writeMAR(calculationResult);       // Write the calculated memory address to the memory address register (MAR). Not very useful in the current design but can come in really handy when extending the pipeline to 5 stages, one that includes a memory access stage.
      cpuRegisters.writeMDR(sourceReg1Val);           // Write the required source register value to the memory data register (MDR)
      //cpuMemory.writeValue(cpuRegisters.readMAR(), sourceReg1Val);    // Write the required value to memory directly using the source register value 
      cpuMemory.writeValue(cpuRegisters.readMAR(), cpuRegisters.readMDR());    // Write the required value to memory using the value stored in the memory data register (MDR)
      break;

      // MOVI dr, Ix
      case Isa.MOVI:
      signedImmediateVal = Utility.convertToInt(Utility.signExtend(Integer.toBinaryString(currentInstruction.getSignedImmediateVal())), false);
      /*
      System.out.println("1. " + Integer.toBinaryString(currentInstruction.getSignedImmediateVal()));
      System.out.println("2. " + Utility.signExtend(Integer.toBinaryString(currentInstruction.getSignedImmediateVal())));
      System.out.println("3. " + signedImmediateVal);
      */
      destinationRegLoc = currentInstruction.getDestinationRegLoc();                                // Obtain the location of the destination register from the current instruction object
      cpuRegisters.writeGP(destinationRegLoc, signedImmediateVal);                                  // Write the sign extended immediate to the destination register
      break;

      // MOVR dr, sr1
      case Isa.MOVR:
      sourceReg1Val = currentInstruction.getSourceReg1Val();
      destinationRegLoc = currentInstruction.getDestinationRegLoc();                                // Obtain the location of the destination register from the current instruction object
      cpuRegisters.writeGP(destinationRegLoc, sourceReg1Val);                                       // Write the current value stored in source register 1 to the destination register
      break;

      default:
      System.out.println("Fatal error! Incorrect execution unit (EU) used for the instruction!");
      break;
    }
  }
}
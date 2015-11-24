/**
 * @file Instruction.java
 * @author Ashish Tibrewal
 * @date 17.10.2015
 * @details This file contains the declaration/definition for the Instruction class. The Instruction class encapsulates an instruction 
 * specified in the ISA.
 */

/**
 * Instruction class encapsulating all parameters contained within an instruction.
 */
public class Instruction
{
  private int memoryFetchLocation;
  private String instructionType;
  private int numberOfCycles;
  private int opCode;
  private int sourceReg1Val;
  private int sourceReg2Val;
  private int destinationRegLoc;
  private int signedImmediateVal;

  // RRR and RRI type instructions
  public Instruction(String instructionType, int opCode, int memoryFetchLocation, int numberOfCycles, int sourceReg1Val, int sourceReg2Val, int destinationRegLoc, int signedImmediateVal)
  {
    if (instructionType == "RRR")     // RRR type
    {
      this.opCode = opCode;
      this.instructionType = instructionType;
      this.memoryFetchLocation = memoryFetchLocation;
      this.numberOfCycles = numberOfCycles;
      this.sourceReg1Val = sourceReg1Val;
      this.sourceReg2Val = sourceReg2Val;
      this.destinationRegLoc = destinationRegLoc;
      this.signedImmediateVal = 0;      // Overwrite immediate value since this instruction type doesn't use it
    }
    else      // RRI type
    {
      this.opCode = opCode;
      this.instructionType = instructionType;
      this.memoryFetchLocation = memoryFetchLocation;
      this.numberOfCycles = numberOfCycles;
      this.sourceReg1Val = sourceReg1Val;
      this.sourceReg2Val = 0;           // Overwrite source register 2 value since this instruction type doesn't use it
      this.destinationRegLoc = destinationRegLoc;
      this.signedImmediateVal = signedImmediateVal;
    }
  }

  // RR and RI type instruction
  public Instruction(String instructionType, int opCode, int memoryFetchLocation, int numberOfCycles, int sourceReg1Val, int destinationRegLoc, int signedImmediateVal)
  {
    if (instructionType == "RR")      // RR type
    {
      this.opCode = opCode;
      this.instructionType = instructionType;
      this.memoryFetchLocation = memoryFetchLocation;
      this.numberOfCycles = numberOfCycles;
      this.sourceReg1Val = sourceReg1Val;
      this.destinationRegLoc = destinationRegLoc;
      this.signedImmediateVal = 0;    // Overwrite source register 1 value since this instruction type doesn't use it
    }
    else      // RI type
    {
      this.opCode = opCode;
      this.instructionType = instructionType;
      this.memoryFetchLocation = memoryFetchLocation;
      this.numberOfCycles = numberOfCycles;
      this.sourceReg1Val = 0;       // Overwrite source register 1 value since this instruction type doesn't use it
      this.destinationRegLoc = destinationRegLoc;
      this.signedImmediateVal = signedImmediateVal;
    }
  }

  // I type instruction
  public Instruction(String instructionType, int opCode, int memoryFetchLocation, int numberOfCycles, int destinationRegLoc, int signedImmediateVal)
  {
    this.opCode = opCode;
    this.instructionType = instructionType;
    this.memoryFetchLocation = memoryFetchLocation;
    this.numberOfCycles = numberOfCycles;    
    this.destinationRegLoc = destinationRegLoc;
    this.signedImmediateVal = signedImmediateVal;
  }
}
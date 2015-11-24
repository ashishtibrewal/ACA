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
  private String instructionName;
  private int numberOfCycles;
  private int opCode;
  private int sourceReg1Loc;
  private int sourceReg2Loc;
  private int destinationRegLoc;
  private int sourceReg1Val;
  private int sourceReg2Val;
  private int signedImmediateVal;
  private ExecutionUnit executionUnit;

  // TODO add flag(s) required for dependency checking

  // RRR and RRI type instructions
  public Instruction(String instructionType, String instructionName, ExecutionUnit executionUnit, int opCode, int memoryFetchLocation, int numberOfCycles, int sourceReg1Loc, int sourceReg2Loc, int destinationRegLoc, int signedImmediateVal)
  {
    if (instructionType == "RRR")     // RRR type
    {
      this.opCode = opCode;
      this.instructionType = instructionType;
      this.instructionName = instructionName;
      this.executionUnit = executionUnit;
      this.memoryFetchLocation = memoryFetchLocation;
      this.numberOfCycles = numberOfCycles;
      this.sourceReg1Loc = sourceReg1Loc;
      this.sourceReg2Loc = sourceReg2Loc;
      this.destinationRegLoc = destinationRegLoc;
      this.signedImmediateVal = 0;      // Overwrite immediate value since this instruction type doesn't use it
    }
    else      // RRI type
    {
      this.opCode = opCode;
      this.instructionType = instructionType;
      this.instructionName = instructionName;
      this.executionUnit = executionUnit;
      this.memoryFetchLocation = memoryFetchLocation;
      this.numberOfCycles = numberOfCycles;
      this.sourceReg1Loc = sourceReg1Loc;
      this.sourceReg2Loc = 0;           // Overwrite source register 2 value since this instruction type doesn't use it
      this.destinationRegLoc = destinationRegLoc;
      this.signedImmediateVal = signedImmediateVal;
    }
  }

  // RR and RI type instruction
  public Instruction(String instructionType, String instructionName, ExecutionUnit executionUnit, int opCode, int memoryFetchLocation, int numberOfCycles, int sourceReg1Loc, int destinationRegLoc, int signedImmediateVal)
  {
    if (instructionType == "RR")      // RR type
    {
      this.opCode = opCode;
      this.instructionType = instructionType;
      this.instructionName = instructionName;
      this.executionUnit = executionUnit;
      this.memoryFetchLocation = memoryFetchLocation;
      this.numberOfCycles = numberOfCycles;
      this.sourceReg1Loc = sourceReg1Loc;
      this.destinationRegLoc = destinationRegLoc;
      this.signedImmediateVal = 0;    // Overwrite source register 1 value since this instruction type doesn't use it
    }
    else      // RI type
    {
      this.opCode = opCode;
      this.instructionType = instructionType;
      this.instructionName = instructionName;
      this.executionUnit = executionUnit;
      this.memoryFetchLocation = memoryFetchLocation;
      this.numberOfCycles = numberOfCycles;
      this.sourceReg1Loc = 0;       // Overwrite source register 1 value since this instruction type doesn't use it
      this.destinationRegLoc = destinationRegLoc;
      this.signedImmediateVal = signedImmediateVal;
    }
  }

  // I type instruction
  public Instruction(String instructionType, String instructionName, ExecutionUnit executionUnit, int opCode, int memoryFetchLocation, int numberOfCycles, int destinationRegLoc, int signedImmediateVal)
  {
    this.opCode = opCode;
    this.instructionType = instructionType;
    this.instructionName = instructionName;
    this.executionUnit = executionUnit;
    this.memoryFetchLocation = memoryFetchLocation;
    this.numberOfCycles = numberOfCycles;    
    this.destinationRegLoc = destinationRegLoc;
    this.signedImmediateVal = signedImmediateVal;
  }

  /**
   * Method to return the OpCode for an instruction
   * @return Instruction's OpCode
   */
  public int getOpCode()
  {
    return opCode;
  }

  public void setSourceReg1Val(int _sourceReg1Val)
  {
    sourceReg1Val = _sourceReg1Val;
  }

  public void setSourceReg2Val(int _sourceReg2Val)
  {
    sourceReg2Val = _sourceReg2Val;
  }
}
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
  private boolean dependencyFlag;         // Set to false by default. TODO This needs to be updated by the Instruction Issue stage after having checked for dependencies
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
      this.dependencyFlag = false;      // By default we set this to false and assume that an instruction doesn't have any dependencies
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
      this.dependencyFlag = false;      // By default we set this to false and assume that an instruction doesn't have any dependencies
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
      this.dependencyFlag = false;      // By default we set this to false and assume that an instruction doesn't have any dependencies
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
      this.dependencyFlag = false;      // By default we set this to false and assume that an instruction doesn't have any dependencies
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
    this.dependencyFlag = false;      // By default we set this to false and assume that an instruction doesn't have any dependencies
  }

  /**
   * Method to obtain the OpCode for an instruction
   * @return Instruction's OpCode
   */
  public int getOpCode()
  {
    return opCode;
  }

  /**
   * Method to set the value for source register 1
   * @param _sourceReg1Val Source register 1 value 
   */
  public void setSourceReg1Val(int _sourceReg1Val)
  {
    sourceReg1Val = _sourceReg1Val;
  }

  /**
   * Method to set the value for source register 2
   * @param _sourceReg1Val Source register 2 value 
   */
  public void setSourceReg2Val(int _sourceReg2Val)
  {
    sourceReg2Val = _sourceReg2Val;
  }

  /**
   * Method to get the value for source register 1
   * @return Source register 1 value 
   */
  public int getSourceReg1Val()
  {
    return sourceReg1Val;
  }

  /**
   * Method to get the value for source register 2
   * @return Source register 2 value 
   */
  public int getSourceReg2Val()
  {
    return sourceReg2Val;
  }

  /**
   * Method to get the value for the signed immediate
   * @return Signed immediate value 
   */
  public int getSignedImmediateVal()
  {
    return signedImmediateVal;
  }

  /**
   * Method to obtain the value for the instruction's dependency flag
   * @return Instruction dependency flag current value
   */
  public boolean getDependencyFlag()
  {
    return dependencyFlag;
  }

  /**
   * Method to set the value for the instruction's dependency flag
   * @param _dependencyFlag Instruction dependency flag new value
   */
  public void setDependencyFlag(boolean _dependencyFlag)
  {
    dependencyFlag = _dependencyFlag;
  }

  /**
   * Method to obtain the type of execution unit (EU) required to execute the instruction
   * @return Type of execution unit (EU) required to execute the instruction
   */
  public ExecutionUnit getExecutionUnit()
  {
    return executionUnit;
  }

  /**
   * Method to obtain a value for the type of instruction
   * @return Type of instruction
   */
  public String getInstructionType()
  {
    return instructionType;
  }

  /**
   * Method to obtain the first source register's location from which a value needs to be read by the issue stage to issue the instruction to the execution stage
   * @return Location of source register 1
   */
  public int getSourceReg1Loc()
  {
    return sourceReg1Loc;
  }

  /**
   * Method to obtain the second source register's location from which a value needs to be read by the issue stage to issue the instruction to the execution stage
   * @return Location of source register 2
   */
  public int getSourceReg2Loc()
  {
    return sourceReg2Loc;
  }

  /**
   * Method to obtain the destination register's location to which a value needs to be written to by the execute/writeback stage
   * @return Location of destination register
   */
  public int getDestinationRegLoc()
  {
    return destinationRegLoc;
  }
}
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
  private int instruction;              /** Instruction value. Useful for debugging/printing purposes */
  private String instructionType;
  private String instructionMnemonic;
  private int numberOfCycles;
  private int opCode;
  private int sourceReg1Loc;
  private int sourceReg2Loc;
  private int destinationRegLoc;
  private int sourceReg1Val;
  private int sourceReg2Val;
  private int signedImmediateVal;
  private int writebackVal;           /** Result value that is used by the writeback stage to update the registers */
  private ExecutionUnit executionUnit;
  private boolean dependencyFlag;         // Set to false by default. TODO This needs to be updated by the Instruction Issue stage after having checked for dependencies
  private boolean branchPredictionResult; // Branch prediction result. Set by the processor's branch prediction unit. Only useful for branch instructions.
  // TODO add flag(s) required for dependency checking

  // RRR and RRI type instructions
  public Instruction(String instructionType, String instructionMnemonic, ExecutionUnit executionUnit, int opCode, int memoryFetchLocation, int instruction, int numberOfCycles, boolean branchPredictionResult, int sourceReg1Loc, int sourceReg2Loc, int destinationRegLoc, int signedImmediateVal)
  {
    if (instructionType == "RRR")     // RRR type
    {
      this.opCode = opCode;
      this.instructionType = instructionType;
      this.instructionMnemonic = instructionMnemonic;
      this.executionUnit = executionUnit;
      this.memoryFetchLocation = memoryFetchLocation;
      this.instruction = instruction;
      this.numberOfCycles = numberOfCycles;
      this.branchPredictionResult = branchPredictionResult;
      this.sourceReg1Loc = sourceReg1Loc;
      this.sourceReg2Loc = sourceReg2Loc;
      this.destinationRegLoc = destinationRegLoc;
      this.signedImmediateVal = Isa.DEFAULT_IMM_VALUE;      // Overwrite immediate value since this instruction type doesn't use it
      this.dependencyFlag = GlobalConstants.DEFAULT_DEPENDENCY_FLAG;            // By default we set this to false and assume that an instruction doesn't have any dependencies
      this.writebackVal = Isa.DEFAULT_REG_VALUE;
    }
    else      // RRI type
    {
      if (opCode == Isa.BEQ || opCode == Isa.BNE || opCode == Isa.BLT || opCode == Isa.BGT)
      {
        this.opCode = opCode;
        this.instructionType = instructionType;
        this.instructionMnemonic = instructionMnemonic;
        this.executionUnit = executionUnit;
        this.memoryFetchLocation = memoryFetchLocation;
        this.instruction = instruction;
        this.numberOfCycles = numberOfCycles;
        this.branchPredictionResult = branchPredictionResult;
        this.sourceReg1Loc = sourceReg1Loc;
        this.sourceReg2Loc = sourceReg2Loc;           // Overwrite source register 2 value since this instruction type doesn't use it
        this.destinationRegLoc = Isa.DEFAULT_REG_VALUE;
        this.signedImmediateVal = signedImmediateVal;
        this.dependencyFlag = GlobalConstants.DEFAULT_DEPENDENCY_FLAG;            // By default we set this to false and assume that an instruction doesn't have any dependencies
        this.writebackVal = Isa.DEFAULT_REG_VALUE;
      }
      else
      {
        this.opCode = opCode;
        this.instructionType = instructionType;
        this.instructionMnemonic = instructionMnemonic;
        this.executionUnit = executionUnit;
        this.memoryFetchLocation = memoryFetchLocation;
        this.instruction = instruction;
        this.numberOfCycles = numberOfCycles;
        this.branchPredictionResult = branchPredictionResult;
        this.sourceReg1Loc = sourceReg1Loc;
        this.sourceReg2Loc = Isa.DEFAULT_REG_VALUE;           // Overwrite source register 2 value since this instruction type doesn't use it
        this.destinationRegLoc = destinationRegLoc;
        this.signedImmediateVal = signedImmediateVal;
        this.dependencyFlag = GlobalConstants.DEFAULT_DEPENDENCY_FLAG;            // By default we set this to false and assume that an instruction doesn't have any dependencies
        this.writebackVal = Isa.DEFAULT_REG_VALUE;
      }
    }
  }

  // RR and RI type instruction
  public Instruction(String instructionType, String instructionMnemonic, ExecutionUnit executionUnit, int opCode, int memoryFetchLocation, int instruction, int numberOfCycles, boolean branchPredictionResult, int sourceReg1Loc, int destinationRegLoc, int signedImmediateVal)
  {
    if (instructionType == "RR")      // RR type
    {
      this.opCode = opCode;
      this.instructionType = instructionType;
      this.instructionMnemonic = instructionMnemonic;
      this.executionUnit = executionUnit;
      this.memoryFetchLocation = memoryFetchLocation;
      this.instruction = instruction;
      this.numberOfCycles = numberOfCycles;
      this.branchPredictionResult = branchPredictionResult;
      this.sourceReg1Loc = sourceReg1Loc;
      this.destinationRegLoc = destinationRegLoc;
      this.signedImmediateVal = Isa.DEFAULT_IMM_VALUE;    // Overwrite source register 1 value since this instruction type doesn't use it
      this.dependencyFlag = GlobalConstants.DEFAULT_DEPENDENCY_FLAG;            // By default we set this to false and assume that an instruction doesn't have any dependencies
      this.writebackVal = Isa.DEFAULT_REG_VALUE;
    }
    else      // RI type
    {
      this.opCode = opCode;
      this.instructionType = instructionType;
      this.instructionMnemonic = instructionMnemonic;
      this.executionUnit = executionUnit;
      this.memoryFetchLocation = memoryFetchLocation;
      this.instruction = instruction;
      this.numberOfCycles = numberOfCycles;
      this.branchPredictionResult = branchPredictionResult;
      this.sourceReg1Loc = 0;       // Overwrite source register 1 value since this instruction type doesn't use it
      this.destinationRegLoc = destinationRegLoc;
      this.signedImmediateVal = signedImmediateVal;
      this.dependencyFlag = GlobalConstants.DEFAULT_DEPENDENCY_FLAG;            // By default we set this to false and assume that an instruction doesn't have any dependencies
      this.writebackVal = Isa.DEFAULT_REG_VALUE;
    }
  }

  // I type instruction
  public Instruction(String instructionType, String instructionMnemonic, ExecutionUnit executionUnit, int opCode, int memoryFetchLocation, int instruction, int numberOfCycles, boolean branchPredictionResult, int signedImmediateVal)
  {
    this.opCode = opCode;
    this.instructionType = instructionType;
    this.instructionMnemonic = instructionMnemonic;
    this.executionUnit = executionUnit;
    this.memoryFetchLocation = memoryFetchLocation;
    this.instruction = instruction;
    this.numberOfCycles = numberOfCycles;
    this.branchPredictionResult = branchPredictionResult;
    this.signedImmediateVal = signedImmediateVal;
    this.dependencyFlag = GlobalConstants.DEFAULT_DEPENDENCY_FLAG;            // By default we set this to false and assume that an instruction doesn't have any dependencies
    this.writebackVal = Isa.DEFAULT_REG_VALUE;
  }

  // Copy constructor. Could have used Java Object class' default clone() method but that but default only does a shallow clone and would reuire quite a bit of modification to do a deep clone. Hence, for simplicity this is being used.
  public Instruction(Instruction _instruction)
  {
    // Copy/clone all the field values stored in instruction to the new Instruction object being created. Note that sourceReg1Val and sourceReg2Val have been avoided since it's not updated at this stage (i.e. not when the object is being created). These fields will contain defualt values of its respective (declared) type.
    this.opCode = _instruction.opCode;
    this.instructionType = _instruction.instructionType;
    this.instructionMnemonic = _instruction.instructionMnemonic;
    this.executionUnit = _instruction.executionUnit;
    this.memoryFetchLocation = _instruction.memoryFetchLocation;
    this.instruction = _instruction.instruction;
    this.numberOfCycles = _instruction.numberOfCycles;
    this.sourceReg1Loc = _instruction.sourceReg1Loc;
    this.sourceReg2Loc = _instruction.sourceReg2Loc;
    this.destinationRegLoc = _instruction.destinationRegLoc;
    this.signedImmediateVal = _instruction.signedImmediateVal;
    this.dependencyFlag = _instruction.dependencyFlag;
    this.branchPredictionResult = _instruction.branchPredictionResult;
    this.writebackVal = Isa.DEFAULT_REG_VALUE;
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

  /**
   * Method to obtain the location in memory from which the instruction was fetched
   * @return Location in memory from which the instruction was fetched
   */
  public int getMemoryFetchLocation()
  {
    return memoryFetchLocation;
  }

  /**
   * Method to obtain the instruction value
   * @return Instruction value
   */
  public int getInstructionVal()
  {
    return instruction;
  }

  /**
   * Method to obtain the instruction mnemonic
   * @return Instruction mnemonic
   */
  public String getInstructionMnemonic()
  {
    return instructionMnemonic;
  }

  /**
   * Method to obtain the branch predictor's result for this instruction. Only useful for branch instructions.
   * @return Branch predictor's result for this instruction
   */
  public boolean getBranchPredictionResult()
  {
    return branchPredictionResult;
  }

  /**
   * Method to set the writeback value (i.e. instruction result). Used by the instruction execute (IE) stage.
   * @param _writebackVal Writeback value (i.e. instruction result)
   */
  public void setWritebackVal(int _writebackVal)
  {
    writebackVal = _writebackVal;
  }

  /**
   * Method to obtain the writeback value (i.e. instruction result). Used by the instruction writeback (WB) stage.
   * @return Writeback value (i.e. instruction result)
   */
  public int getWritebackVal()
  {
    return writebackVal;
  }
}
/**
 * Class that stores intermediate stage values that is used to communicate between different stages of the pipeline.
 */
import pipeline.*;

import java.util.*;

/**
 * This class is used to store the shared state of the pipeline. This class is accessed by all the pipeline stages to read and update values when required.
 */
public class ProcessorPipelineContext implements IPipelineContext
{
  private Register cpuRegisters;                 /** Reference to architectural registers */
  private Memory cpuMemory;                      /** Reference to main memory */
  private IStage instructionFetchStage;          /** Reference to the Instruction Fetch Stage of the pipeline. Useful if something is required from this stage by other stages. */
  private IStage instructionDecodeStage;         /** Reference to the Instruction Decode Stage of the pipeline. Useful if something is required from this stage by other stages. */
  private IStage instructionIssueStage;          /** Reference to the Instruction Issue Stage of the pipeline. Useful if something is required from this stage by other stages. */
  private IStage instructionExecuteStage;        /** Reference to the Instruction Execute Stage of the pipeline. Useful if something is required from this stage by other stages. */
  private Instruction currentInstruction;        /** Reference to the current instruction */   // TODO this should actually be a list of instructions when going superscalar becase in one cycle the instruction fetch unit would fetch multiple executions 

  public ProcessorPipelineContext(Register cpuRegisters, Memory cpuMemory, IStage instructionFetchStage, IStage instructionDecodeStage, IStage instructionIssueStage, IStage instructionExecuteStage)
  {
    this.cpuRegisters = cpuRegisters;
    this.cpuMemory = cpuMemory;
    this.instructionFetchStage = instructionFetchStage;
    this.instructionDecodeStage = instructionDecodeStage;
    this.instructionIssueStage = instructionIssueStage;
    this.instructionExecuteStage = instructionExecuteStage;
  }
  // TODO Add functions and fields to handle outputs from other stages of the pipeline. (I.e. instruction fetch output, instruction decode output etc)
  // TODO WOULD NEED TO HAVE TWO COPIES OF CERTAIN FIELDS, ONE TO STORE THE VALUES FOR THE CURRENT CYCLE AND THE NEXT TO STORE VALUES FOR THE NEXT CYCLE
  
  /**
   * Method to obtain (a reference) to the cpu registers object
   * @return Reference to the cpu registers object
   */
  public Register getCpuRegisters()
  {
    return cpuRegisters;
  }

  /**
   * Method to obtain (a reference) to the cpu memory object
   * @return Reference to the cpu memory object
   */
  public Memory getCpuMemory()
  {
    return cpuMemory;
  }

  /**
   * Method to obtain (a reference) to the current instruction object
   * @return Reference to the current instruction object
   */
  public Instruction getCurrentInstruction()
  {
    return currentInstruction;
  }

  /**
   * * Method to set the current instruction
   * @param _currentInstruction Latest decoded instruction
   */
  public void setCurrentInstruction(Instruction _currentInstruction)
  {
    currentInstruction = _currentInstruction;
  }

  /**
   * Method to obtain (a reference) to the instruction queue object (Note that the instruction queue object is contained in the Instruction Issue Stage object)
   * @return Reference to the instruction queue object
   */
  public Queue<Instruction> getInstructionQueue()
  {
    return ((InstructionIssueStage) instructionIssueStage).getInstructionQueue();
  }
}
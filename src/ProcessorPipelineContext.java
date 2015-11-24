/**
 * Class that stores intermediate stage values that is used to communicate between different stages of the pipeline.
 */
import pipeline.*;

import java.util.*;
//import java.util.concurrent.*;

public class ProcessorPipelineContext implements IPipelineContext
{
  // All classs fields/members should be declared public since this makes it easier to access them from outside this class. Note that this class is accessed 
  // by all pipeline stages to read and update values. Having made it public also removes the hassle of creating a method for each functionality the objects
  // declared offers.
  private Register cpuRegisters;                 /** Reference to architectural registers */
  private Memory cpuMemory;                      /** Reference to main memory */
  private Instruction currentInstruction;        /** Reference to the current instruction */   // TODO this should actually be a list of instructions when going superscalar becase in one cycle the instruction fetch unit would fetch multiple executions 
  private Queue<Instruction> instructionQueue;   /** Reference to the current instruction queue */

  public ProcessorPipelineContext(Register cpuRegisters, Memory cpuMemory)
  {
    this.cpuRegisters = cpuRegisters;
    this.cpuMemory = cpuMemory;
    //instructionQueue = new ArrayBlockingQueue<Instruction>(GlobalConstants.INSTRUCTION_QUEUE_CAPACITY);
    instructionQueue = new LinkedList<Instruction>();
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
   * Method to obtain (a reference) to the instruction queue object
   * @return Reference to the instruction queue object
   */
  public Queue<Instruction> getInstructionQueue()
  {
    return instructionQueue;
  }
}
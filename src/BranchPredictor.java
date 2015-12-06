/**
 * Branch prediction (BP) unit. Class that handles branch predictions for the processor.
 * Currently a static branch predictor.
 * Predicts always taken for unconditional branch instructions
 * Predicts always not taken for conditional branch instructions
 */

public class BranchPredictor
{
  /**
   * Method to predict whether a branch is taken or not.
   * Taken = true
   * Not taken = false
   * @param  instruction Branch instruction on which a prediction must be made
   * @param  context     Processor pipeline context
   * @return Branch prediction result (True = predict taken, false = predict not taken)
   */
  public boolean predict(Instruction instruction)
  { 
    int opCode = instruction.getOpCode();
    boolean predictionResult;       // Result of the branch predictor
    if (opCode == Isa.BU || opCode == Isa.BL || opCode == Isa.RET)     // If the instruction is an unconditional branch
    {
      predictionResult = true;    // Predict that unconditional branches are always taken
    }
    else    // If the instruction is a conditional branch
    {
      //TODO check immediate value for conditional branch instructions. If negative, predict true since it's probably a loop else predict false. Look for other factors that can improve performance. Uncomment the following section
      if (instruction.getSignedImmediateVal() < 0)      // Check if it is a backward branch (i.e. if the immediate is a negative value) - Most likely to be a loop, hence predict taken
      {
        predictionResult = true;
      }
      else                                              // Forward branch, hence, predict always not taken
      {
        predictionResult = false;
      }
      //predictionResult = false;   // Predict that conditional branches are always not taken
    }
    return predictionResult;
  }
}
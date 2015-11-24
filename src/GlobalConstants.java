/**
 * @file GlobalConstants.java
 * @author Ashish Tibrewal
 * @date 12.10.2015
 * @details This file contains the GlobalConstants class that stores all the constant values used by the simulator
 */

public final class GlobalConstants
{

  // Register contants
  public static final int TOTAL_ARCHITECTURAL_REGISTERS = 16;     // Total number of physical architectural registers available in the microarchitecure. Note that this number only refers to the General Purpose registers.
  public static final int STATUS_REGISTER_LENGTH = 4;
  public static final int REGISTER_INITIALIZATION_VALUE = 0;

  // Memory constants
  // //public static final int MEMORY_SIZE = 1024;
  public static final int MEMORY_SIZE = 32;
  public static final int MEMORY_INITIALIZATION_VALUE = 0;


  // Other processor constants
  public static final int PIPELINE_LENGTH = 5;
  // TODO Insert ALL constants to this class.

}
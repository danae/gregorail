package dev.danae.gregorail.commands;


public class CommandUsageException extends Exception
{
  // Constructor
  public CommandUsageException(Throwable cause)
  {
    super(cause);
  }
  public CommandUsageException()
  {
    super();
  }
}

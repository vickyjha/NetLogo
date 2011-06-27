package org.nlogo.api

/**
 * Partial implementation of Command provides common implentations of some methods.  Implements
 * every method except <code>Command.perform(...)</code>.
 */
abstract class DefaultCommand extends Command {

  /**
   * Indicates that this primitive can be used by any agent.
   *
   * @return <code>"OTPL"</code>
   */
  override def getAgentClassString = "OTPL"

  /**
   * Indicates that this command takes no arguments.
   *
   * @return <code>Syntax.commandSyntax()</code>
   */
  override def getSyntax = Syntax.commandSyntax()

  /**
   * Indicates that NetLogo does not need to switch agents after executing this command.
   */
  override def getSwitchesBoolean = false

  /**
   * Returns a new instance of this class, created by invoking
   * the empty constructor.
   *
   * @return <code>(Command) this.getClass().newInstance()</code>
   */
  override def newInstance(name: String) =
    getClass.newInstance.asInstanceOf[Command]

}
package slack

/**
 * Created by robertk on 11/09/15.
 */
object AngryUtils {
  val words = """(\b+)""".r

  def extractAngryWords(text: String): Seq[String] = {
    text.split(""" """) //should split on whitspace regex TODO
  }
}

package slack

import akka.actor._
import slack.rtm.SlackRtmClient

object Main extends App {
  val token = sys.env("SLACK_ANGRY_TOKEN")
  implicit val system = ActorSystem("slack")
  implicit val ec = system.dispatcher

  val client = SlackRtmClient(token)
  val selfId = client.state.self.id

  client.onEvent { event =>

    import slack.models._
    system.log.info("Received new event: {}", event)

    //TODO pass this off to another actor to process
    event match {
      case message: Message => {

        val mentionedIds = SlackUtil.extractMentionedIds(message.text)

        if (mentionedIds.contains(selfId)) {
          val retort = doRetort(message.text)
          client.sendMessage(message.channel, s"<@${message.user}>: ${retort}!")
        }
      }

      case _ => {}
    }
  }


  val retorts = List(
    "Aaaaaargh!",
    "WHY!!!!!!!!!!!!",
    "THat vloody does it!!!!!!",
    "Feck Off!!!",
    "Get Lost!",
    "No!!!!"
  )

  def doRetort(message: String) = {
    val ws = AngryUtils.extractAngryWords(message.toLowerCase)
    val wSet = ws.toSet
    if(wSet.contains("matter")) "You're the f*%##$&g matter!" else {
      if(wSet.contains("problem")) "You're the f*%##$&g problem!" else {
        val rand = scala.util.Random.nextInt(retorts.size)
        retorts(rand)
      }
    }
  }
}
package twilio.flutter.twilio_conversations.listeners

import com.twilio.conversations.Conversation
import com.twilio.conversations.ConversationListener
import com.twilio.conversations.Message
import com.twilio.conversations.Participant
import twilio.flutter.twilio_conversations.Api
import twilio.flutter.twilio_conversations.Mapper
import twilio.flutter.twilio_conversations.TwilioConversationsPlugin

class ConversationListener(private val conversationSid: String) : ConversationListener {
    private val TAG = "ConversationListener"

    private val resultListener: Api.Result<Void>
        get() = object : Api.Result<Void> {
            override fun success(result: Void?) {}

            override fun error(error: Throwable?) {}
        }

    override fun onMessageAdded(message: Message) {
        debug("onMessageAdded => messageSid = ${message.sid}")
        TwilioConversationsPlugin.flutterClientApi.messageAdded(
            conversationSid,
            Mapper.messageToPigeon(message), resultListener)
    }

    override fun onMessageUpdated(message: Message, reason: Message.UpdateReason) {
        debug("onMessageUpdated => messageSid = ${message.sid}, reason = $reason")
        TwilioConversationsPlugin.flutterClientApi.messageUpdated(
            conversationSid,
            Mapper.messageToPigeon(message),
            reason.toString(), resultListener)
    }

    override fun onMessageDeleted(message: Message) {
        debug("onMessageDeleted => messageSid = ${message.sid}")
        TwilioConversationsPlugin.flutterClientApi.messageDeleted(
            conversationSid,
            Mapper.messageToPigeon(message), resultListener)
    }

    override fun onParticipantAdded(participant: Participant) {
        debug("onParticipantAdded => participantSid = ${participant.sid}")
        TwilioConversationsPlugin.flutterClientApi.participantAdded(
            conversationSid,
            Mapper.participantToPigeon(participant), resultListener)
    }

    override fun onParticipantUpdated(participant: Participant, reason: Participant.UpdateReason) {
        debug("onParticipantUpdated => participantSid = ${participant.sid}, reason = $reason")
        TwilioConversationsPlugin.flutterClientApi.participantUpdated(
            conversationSid,
            Mapper.participantToPigeon(participant),
            reason.toString(), resultListener)
    }

    override fun onParticipantDeleted(participant: Participant) {
        debug(".onParticipantDeleted => participantSid = ${participant.sid}")
        TwilioConversationsPlugin.flutterClientApi.participantDeleted(
            conversationSid,
            Mapper.participantToPigeon(participant), resultListener)
    }

    override fun onTypingStarted(conversation: Conversation, participant: Participant) {
        debug("onTypingStarted => conversationSid = ${conversation.sid}, participantSid = ${conversation.sid}")
        TwilioConversationsPlugin.flutterClientApi.typingStarted(
            conversationSid,
            Mapper.conversationToPigeon(conversation),
            Mapper.participantToPigeon(participant), resultListener)
    }

    override fun onTypingEnded(conversation: Conversation, participant: Participant) {
        debug("onTypingEnded => conversationSid = ${conversation.sid}, participantSid = ${participant.sid}")
        TwilioConversationsPlugin.flutterClientApi.typingEnded(
            conversationSid,
            Mapper.conversationToPigeon(conversation),
            Mapper.participantToPigeon(participant), resultListener)
    }

    override fun onSynchronizationChanged(conversation: Conversation) {
        debug("onSynchronizationChanged => sid: ${conversation.sid}, status: ${conversation.synchronizationStatus}")
        TwilioConversationsPlugin.flutterClientApi.synchronizationChanged(
            conversationSid,
            Mapper.conversationToPigeon(conversation), resultListener)
    }

    fun debug(message: String) {
        TwilioConversationsPlugin.debug("$TAG::$message")
    }
}

package twilio.flutter.twilio_conversations.listeners

import com.twilio.conversations.Conversation
import com.twilio.conversations.ConversationsClient
import com.twilio.conversations.ConversationsClientListener
import com.twilio.util.ErrorInfo
import com.twilio.conversations.User
import twilio.flutter.twilio_conversations.Api
import twilio.flutter.twilio_conversations.Mapper
import twilio.flutter.twilio_conversations.TwilioConversationsPlugin

class ClientListener : ConversationsClientListener {
    private val TAG = "ClientListener"

    private val resultListener: Api.Result<Void>
        get() = object :Api.Result<Void> {
            override fun success(result: Void?) {}

            override fun error(error: Throwable?) {}
        }

    override fun onClientSynchronization(status: ConversationsClient.SynchronizationStatus) {
        debug("onClientSynchronization => status = $status")
        TwilioConversationsPlugin.flutterClientApi.clientSynchronization(status.toString(), resultListener)
    }

    override fun onConversationSynchronizationChange(conversation: Conversation) {
        debug("onConversationSynchronizationChange => sid = ${conversation.sid}")
        TwilioConversationsPlugin.flutterClientApi.conversationSynchronizationChange(Mapper.conversationToPigeon(conversation), resultListener)
    }

    override fun onNotificationSubscribed() {
        debug("onNotificationSubscribed")
        TwilioConversationsPlugin.flutterClientApi.notificationSubscribed(resultListener)
    }

    override fun onUserSubscribed(user: User?) {
        user ?: return
        debug("onUserSubscribed => user '${user?.identity}'")
        TwilioConversationsPlugin.flutterClientApi.userSubscribed(Mapper.userToPigeon(user), resultListener)
    }

    override fun onUserUnsubscribed(user: User?) {
        user ?: return
        debug("onUserUnsubscribed => user '${user?.identity}'")
        TwilioConversationsPlugin.flutterClientApi.userUnsubscribed(Mapper.userToPigeon(user), resultListener)
    }

    override fun onUserUpdated(user: User?, reason: User.UpdateReason?) {
        user ?: return
        reason ?: return
        debug("onUserUpdated => user '${user?.identity}' updated, $reason")
        TwilioConversationsPlugin.flutterClientApi.userUpdated(Mapper.userToPigeon(user), reason.toString(), resultListener)
    }

    override fun onNotificationFailed(errorInfo: ErrorInfo?) {
        errorInfo ?: return
        TwilioConversationsPlugin.flutterClientApi.notificationFailed(Mapper.errorInfoToPigeon(errorInfo), resultListener)
    }

    override fun onTokenExpired() {
        debug("onTokenExpired")
        TwilioConversationsPlugin.flutterClientApi.tokenExpired(resultListener)
    }

    override fun onConversationUpdated(conversation: Conversation?, reason: Conversation.UpdateReason?) {
        debug("onConversationUpdated => conversation '${conversation?.sid}' updated, $reason")
        val event = Api.ConversationUpdatedData()
        event.conversation = Mapper.conversationToPigeon(conversation)
        event.reason = reason?.toString()
        TwilioConversationsPlugin.flutterClientApi.conversationUpdated(event, resultListener)
    }

    override fun onConversationAdded(conversation: Conversation) {
        debug("onConversationAdded => sid = ${conversation.sid}")
        TwilioConversationsPlugin.flutterClientApi.conversationAdded(Mapper.conversationToPigeon(conversation), resultListener)
    }

    override fun onNewMessageNotification(conversationSid: String?, messageSid: String?, messageIndex: Long) {
        conversationSid ?: return
        debug("onNewMessageNotification => conversationSid = $conversationSid, messageSid = $messageSid, messageIndex = $messageIndex")
        TwilioConversationsPlugin.flutterClientApi.newMessageNotification(conversationSid, messageIndex, resultListener)
    }

    override fun onAddedToConversationNotification(conversationSid: String?) {
        conversationSid ?: return
        debug("onAddedToConversationNotification => conversationSid = $conversationSid")
        TwilioConversationsPlugin.flutterClientApi.addedToConversationNotification(conversationSid, resultListener)
    }

    override fun onConnectionStateChange(state: ConversationsClient.ConnectionState) {
        debug("onConnectionStateChange => state = $state")
        TwilioConversationsPlugin.flutterClientApi.connectionStateChange(state.toString(), resultListener)
    }

    override fun onError(errorInfo: ErrorInfo?) {
        if (errorInfo == null) {
            return
        }
        TwilioConversationsPlugin.flutterClientApi.error(Mapper.errorInfoToPigeon(errorInfo), resultListener)
    }

    override fun onConversationDeleted(conversation: Conversation) {
        debug("onConversationDeleted => sid = ${conversation.sid}")
        TwilioConversationsPlugin.flutterClientApi.conversationDeleted(Mapper.conversationToPigeon(conversation), resultListener)
    }

    override fun onRemovedFromConversationNotification(conversationSid: String?) {
        debug("onRemovedFromConversationNotification => conversationSid = $conversationSid")
        conversationSid ?: return
        TwilioConversationsPlugin.flutterClientApi.removedFromConversationNotification(conversationSid, resultListener)
    }

    override fun onTokenAboutToExpire() {
        debug("onTokenAboutToExpire")
        TwilioConversationsPlugin.flutterClientApi.tokenAboutToExpire(resultListener)
    }

    fun debug(message: String) {
        TwilioConversationsPlugin.debug("$TAG::$message")
    }
}

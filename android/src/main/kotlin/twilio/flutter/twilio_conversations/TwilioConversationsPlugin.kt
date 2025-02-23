package twilio.flutter.twilio_conversations

import ConversationMethods
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.NonNull
import com.twilio.conversations.ConversationListener
import com.twilio.conversations.ConversationsClient
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import twilio.flutter.twilio_conversations.listeners.ClientListener
import twilio.flutter.twilio_conversations.methods.ConversationClientMethods
import twilio.flutter.twilio_conversations.methods.MessageMethods
import twilio.flutter.twilio_conversations.methods.ParticipantMethods
import twilio.flutter.twilio_conversations.methods.PluginMethods
import twilio.flutter.twilio_conversations.methods.UserMethods

/** TwilioConversationsPlugin */
class TwilioConversationsPlugin : FlutterPlugin {
    companion object {
        @Suppress("unused")
        @JvmStatic
        lateinit var instance: TwilioConversationsPlugin
        private var initialized = false

        // Flutter > Host APIs
        @JvmStatic
        val pluginApi: Api.PluginApi = PluginMethods()

        @JvmStatic
        val conversationClientApi: Api.ConversationClientApi = ConversationClientMethods()

        @JvmStatic
        val conversationApi: Api.ConversationApi = ConversationMethods()

        @JvmStatic
        val participantApi: Api.ParticipantApi = ParticipantMethods()

        @JvmStatic
        val messageApi: Api.MessageApi = MessageMethods()

        @JvmStatic
        val userApi: Api.UserApi = UserMethods()

        // Host > Flutter APIs
        @JvmStatic
        lateinit var flutterClientApi: Api.FlutterConversationClientApi

        @JvmStatic
        lateinit var flutterLoggingApi: Api.FlutterLoggingApi

        @JvmStatic
        var client: ConversationsClient? = null

        lateinit var messenger: BinaryMessenger

        lateinit var applicationContext: Context

        var clientListener: ClientListener? = null

        var conversationListeners: HashMap<String, ConversationListener> = hashMapOf()

        var handler = Handler(Looper.getMainLooper())
        var nativeDebug: Boolean = false
        val LOG_TAG = "Twilio_Conversations"

        @JvmStatic
        fun debug(msg: String) {
            if (nativeDebug) {
                Log.d(LOG_TAG, msg)
                handler.post {
                    flutterLoggingApi.logFromHost(msg, object :Api.Result<Void> {
                        override fun success(result: Void?) {}

                        override fun error(error: Throwable?) {}
                    })
                }
            }
        }
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
//        if (initialized) {
//            Log.d(LOG_TAG, "TwilioConversationsPlugin.onAttachedToEngine: already initialized")
//            return
//        } else {
//            Log.d(LOG_TAG, "TwilioConversationsPlugin.onAttachedToEngine")
//        }
        initialized = true
        instance = this
        messenger = flutterPluginBinding.binaryMessenger
        applicationContext = flutterPluginBinding.applicationContext

        Api.PluginApi.setUp(flutterPluginBinding.binaryMessenger, pluginApi)
        Api.ConversationClientApi.setUp(flutterPluginBinding.binaryMessenger, conversationClientApi)
        Api.ConversationApi.setUp(flutterPluginBinding.binaryMessenger, conversationApi)
        Api.ParticipantApi.setUp(flutterPluginBinding.binaryMessenger, participantApi)
        Api.MessageApi.setUp(flutterPluginBinding.binaryMessenger, messageApi)
        Api.UserApi.setUp(flutterPluginBinding.binaryMessenger, userApi)

        flutterClientApi = Api.FlutterConversationClientApi(flutterPluginBinding.binaryMessenger)
        flutterLoggingApi = Api.FlutterLoggingApi(flutterPluginBinding.binaryMessenger)
    }

    override fun onDetachedFromEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        Log.d(LOG_TAG, "TwilioConversationsPlugin.onDetachedFromEngine")

        initialized = false
        instance = this
        messenger = flutterPluginBinding.binaryMessenger
        applicationContext = flutterPluginBinding.applicationContext

        Api.PluginApi.setUp(flutterPluginBinding.binaryMessenger, null)
        Api.ConversationClientApi.setUp(flutterPluginBinding.binaryMessenger, null)
        Api.ConversationApi.setUp(flutterPluginBinding.binaryMessenger, null)
        Api.ParticipantApi.setUp(flutterPluginBinding.binaryMessenger, null)
        Api.MessageApi.setUp(flutterPluginBinding.binaryMessenger, null)
        Api.UserApi.setUp(flutterPluginBinding.binaryMessenger, null)

        flutterClientApi = Api.FlutterConversationClientApi(flutterPluginBinding.binaryMessenger)
        flutterLoggingApi = Api.FlutterLoggingApi(flutterPluginBinding.binaryMessenger)

    }
}

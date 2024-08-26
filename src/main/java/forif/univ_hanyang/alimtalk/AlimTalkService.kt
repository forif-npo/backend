package forif.univ_hanyang.alimtalk

import forif.univ_hanyang.user.entity.User
import forif.univ_hanyang.user.repository.StudyUserRepository
import forif.univ_hanyang.user.repository.UserRepository
import net.nurigo.sdk.NurigoApp
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException
import net.nurigo.sdk.message.model.KakaoOption
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.service.DefaultMessageService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ssm.SsmClient
import software.amazon.awssdk.services.ssm.model.GetParameterRequest
import java.util.concurrent.CompletableFuture

@Service
class AlimTalkService(
    private val userRepository: UserRepository,
) {
    private val logger = LoggerFactory.getLogger(AlimTalkService::class.java)
    private val ssmClient: SsmClient = SsmClient.builder()
        .region(Region.AP_NORTHEAST_2)  // 서울 지역
        .build()

    private val apiKey: String by lazy { getSSMParameter("/config/forif/SMS_API_KEY") }
    private val apiSecret: String by lazy { getSSMParameter("/config/forif/SMS_API_SECRET") }
    private val pfId: String by lazy { getSSMParameter("/config/forif/SMS_PF_ID") }
    private val senderNumber: String by lazy { getSSMParameter("/config/forif/SMS_SENDER_NUMBER") }

    private val messageService: DefaultMessageService by lazy {
        NurigoApp.initialize(apiKey, apiSecret, "https://api.solapi.com")
    }

    private fun getSSMParameter(parameterName: String): String {
        val request = GetParameterRequest.builder()
            .name(parameterName)
            .withDecryption(true)
            .build()

        return try {
            val result = ssmClient.getParameter(request)
            result.parameter().value()
        } catch (e: Exception) {
            logger.error("Failed to get parameter $parameterName", e)
            throw RuntimeException("Failed to get SSM parameter", e)
        }
    }

    fun sendAlimTalk(user: User, request: AlimTalkRequest): CompletableFuture<List<String>> {
        if (user.authLv < 3)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "알림 톡을 보낼 권한이 없습니다.")
        return CompletableFuture.supplyAsync {
            request.receivers.map { receiver ->
                sendMessageToReceiver(receiver, request)
            }
        }
    }

    private fun sendMessageToReceiver(receiver: String, request: AlimTalkRequest): String {
        return userRepository.findByPhoneNumber(receiver)
            .map { user ->
                val variables = createVariables(user, request)
                sendMessage(receiver, request.templateCode, variables)
            }
            .orElse(null)
    }

    private fun sendMessage(receiver: String, templateCode: String, variables: HashMap<String, String>): String {
        val message = Message(
            from = senderNumber,
            to = receiver,
            kakaoOptions = KakaoOption(
                pfId = pfId,
                templateId = templateCode,
                disableSms = true,
                variables = variables
            )
        )

        return try {
            val response = messageService.send(message)
            logger.info("Message sent successfully: $response")
            "Success"
        } catch (e: NurigoMessageNotReceivedException) {
            logger.error("Failed to send message", e)
            "Failed message list: ${e.failedMessageList}"
            "Failed message: ${e.message}"
        } catch (e: Exception) {
            logger.error("Unexpected error while sending message", e)
            "Failed: ${e.message}"
        }
    }

    private fun createVariables(user: User, request: AlimTalkRequest): HashMap<String, String> {
        val variables = HashMap<String, String>()

        variables["#{이름}"] = user.name

        request.studyName?.let { variables["#{스터디명}"] = it }
        request.responseSchedule?.let { variables["#{응답일정}"] = it }
        request.dateTime?.let { variables["#{일시}"] = it }
        request.location?.let { variables["#{장소}"] = it }
        request.url?.let { variables["#{url}"] = it }

        return variables
    }
}
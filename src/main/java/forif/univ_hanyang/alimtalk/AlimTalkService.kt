package forif.univ_hanyang.alimtalk

import forif.univ_hanyang.user.entity.User
import forif.univ_hanyang.user.repository.StudyUserRepository
import forif.univ_hanyang.user.repository.UserRepository
import net.nurigo.sdk.NurigoApp
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException
import net.nurigo.sdk.message.model.KakaoOption
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.service.DefaultMessageService
import org.apache.commons.codec.binary.Hex
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ssm.SsmClient
import software.amazon.awssdk.services.ssm.model.GetParameterRequest
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import org.springframework.http.*
import org.springframework.web.client.RestTemplate


@Service
class AlimTalkService(
    private val userRepository: UserRepository,
    private val studyUserRepository: StudyUserRepository
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

    fun sendAlimTalk(user: User, request: AlimTalkRequest): CompletableFuture<List<String>> {
        if (user.authLv < 3)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "알림 톡을 보낼 권한이 없습니다.")
        return CompletableFuture.supplyAsync {
            request.receivers.map { receiver ->
                sendMessageToReceiver(receiver, request)
            }
        }
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


    private fun sendMessageToReceiver(receiver: String, request: AlimTalkRequest): String {
        return userRepository.findByPhoneNumber(receiver)
            .map { user ->
                val variables = createVariables(user, request)
                sendMessage(receiver, request.templateCode, variables)
            }
            .orElseThrow {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 정보를 찾을 수 없습니다: $receiver")
            }
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

        val studyName = studyUserRepository.findByUser(user)
            .maxByOrNull { it.study.id ?: -1}
            ?.study?.name
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 유저는 스터디에 가입되어 있지 않습니다. 학번: ${user.id}")

        variables["#{이름}"] = user.name
        variables["#{스터디명}"] = studyName
        request.responseSchedule?.let { variables["#{응답일정}"] = it }
        request.dateTime?.let { variables["#{일시}"] = it }
        request.location?.let { variables["#{장소}"] = it }
        request.url?.let { variables["#{url}"] = it }

        return variables
    }


    fun getKakaoTemplates(user: User): ResponseEntity<String> {
        if(user.authLv < 3)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "알림 톡 템플릿을 조회할 권한이 없습니다.")

        val url = "https://api.solapi.com/kakao/v2/templates/"

        val authHeader = getHeaders()

        // HTTP 헤더 설정
        val headers = HttpHeaders().apply {
            set("Authorization", authHeader)
            contentType = MediaType.APPLICATION_JSON
        }

        // HTTP 엔티티 생성
        val entity = HttpEntity<String>(headers)

        // GET 요청 보내기
        return RestTemplate().exchange(
            url,
            HttpMethod.GET,
            entity,
            String::class.java
        )
    }

    fun getAlimTalkLogs(user: User): ResponseEntity<String> {
        if(user.authLv < 3)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "알림 톡 로그를 조회할 권한이 없습니다.")

        val url = "https://api.solapi.com/messages/v4/list"

        val authHeader = getHeaders()

        // HTTP 헤더 설정
        val headers = HttpHeaders().apply {
            set("Authorization", authHeader)
            contentType = MediaType.APPLICATION_JSON
        }

        // HTTP 엔티티 생성
        val entity = HttpEntity<String>(headers)

        // GET 요청 보내기
        return RestTemplate().exchange(
            url,
            HttpMethod.GET,
            entity,
            String::class.java
        )
    }

    private fun getHeaders(): String? {
        return try {
            val salt = UUID.randomUUID().toString().replace("-", "")
            val date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString().split("[")[0]

            val sha256HMAC = Mac.getInstance("HmacSHA256")
            val secretKey = SecretKeySpec(apiSecret.toByteArray(StandardCharsets.UTF_8), "HmacSHA256")
            sha256HMAC.init(secretKey)
            val signature = String(Hex.encodeHex(sha256HMAC.doFinal((date + salt).toByteArray(StandardCharsets.UTF_8))))

            "HMAC-SHA256 ApiKey=$apiKey, Date=$date, salt=$salt, signature=$signature"
        } catch (e: InvalidKeyException) {
            logger.error("Error occurred while getting headers(Invalid Key): ${e.message}")
            null
        } catch (e: NoSuchAlgorithmException) {
            logger.error("Error occurred while getting headers(No Such Algorithm): ${e.message}")
            null
        }
    }

}
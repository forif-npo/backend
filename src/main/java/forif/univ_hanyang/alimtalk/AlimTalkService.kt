//package forif.univ_hanyang.alimtalk
//
//import forif.univ_hanyang.user.entity.User
//import forif.univ_hanyang.user.repository.StudyUserRepository
//import forif.univ_hanyang.user.repository.UserRepository
//import net.nurigo.sdk.NurigoApp
//import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException
//import net.nurigo.sdk.message.model.KakaoOption
//import net.nurigo.sdk.message.model.Message
//import net.nurigo.sdk.message.service.DefaultMessageService
//import org.slf4j.LoggerFactory
//import org.springframework.stereotype.Service
//import software.amazon.awssdk.regions.Region
//import software.amazon.awssdk.services.ssm.SsmClient
//import software.amazon.awssdk.services.ssm.model.GetParameterRequest
//import java.util.concurrent.CompletableFuture
//
//@Service
//class AlimTalkService(
//    private val userRepository: UserRepository,
//    private val studyUserRepository: StudyUserRepository
//) {
//    private val logger = LoggerFactory.getLogger(AlimTalkService::class.java)
//    private val ssmClient: SsmClient = SsmClient.builder()
//        .region(Region.AP_NORTHEAST_2)  // 서울 리전, 필요에 따라 변경
//        .build()
//
//    private val apiKey: String by lazy { getSSMParameter("/config/forif/SMS_API_KEY") }
//    private val apiSecret: String by lazy { getSSMParameter("/config/forif/SMS_API_SECRET") }
//    private val pfId: String by lazy { getSSMParameter("/config/forif/SMS_PF_ID") }
//    private val senderNumber: String by lazy { getSSMParameter("/config/forif/SMS_SENDER_NUMBER") }
//
//    private val messageService: DefaultMessageService by lazy {
//        NurigoApp.initialize(apiKey, apiSecret, "https://api.solapi.com")
//    }
//
//    private fun getSSMParameter(parameterName: String): String {
//        val request = GetParameterRequest.builder()
//            .name(parameterName)
//            .withDecryption(true)
//            .build()
//
//        return try {
//            val result = ssmClient.getParameter(request)
//            result.parameter().value()
//        } catch (e: Exception) {
//            logger.error("Failed to get parameter $parameterName", e)
//            throw RuntimeException("Failed to get SSM parameter", e)
//        }
//    }
//
//    fun sendAlimTalk(request: AlimTalkRequest): CompletableFuture<List<String>> {
//        return CompletableFuture.supplyAsync {
//            request.receivers.map { receiver ->
//                userRepository.findByPhoneNumber(receiver)
//                    .map { sendMessage(receiver, request.templateCode, createVariables(receiver)) }
//                    .orElse("User not found")
//                sendMessage(receiver, request.templateCode, createVariables(receiver))
//            }
//        }
//    }
//
//    private fun sendMessage(receiver: String, templateCode: String, variables: HashMap<String, String>): String {
//        val message = Message(
//            from = senderNumber,
//            to = receiver,
//            kakaoOptions = KakaoOption(
//                pfId = pfId,
//                templateId = templateCode,
//                disableSms = true,
//                variables = variables
//            )
//        )
//
//        return try {
//            val response = messageService.send(message)
//            logger.info("Message sent successfully: $response")
//            "Success"
//        } catch (e: NurigoMessageNotReceivedException) {
//            logger.error("Failed to send message", e)
//            "Failed message list: ${e.failedMessageList}"
//            "Failed message: ${e.message}"
//        }
//        catch (e: Exception) {
//            logger.error("Unexpected error while sending message", e)
//            "Failed: ${e.message}"
//        }
//    }
//
//    private fun createVariables(receiver: String): HashMap<String, String> = hashMapOf(
//        "#{이름}" to userRepository.findByPhoneNumber(receiver).get().name,
//        "#{스터디명}" to studyUserRepository.findTopById_UserIdOrderByStudyIdDesc(userRepository.findByPhoneNumber(receiver).get().id).get().study.name,
//        "#{일시}" to "치환문구 값 입력",
//        "#{장소}" to "치환문구 값 입력2"
//    )
//}
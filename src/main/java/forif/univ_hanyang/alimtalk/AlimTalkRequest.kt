package forif.univ_hanyang.alimtalk


data class AlimTalkRequest (
    val receivers: List<String>,
    val templateCode: String,
    val studyName: String?,
    val responseSchedule: String?,
    val dateTime: String?,
    val location: String?,
    val url: String?,
)

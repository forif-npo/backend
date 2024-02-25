package fororo.univ_hanyang.cert.dto;

import lombok.Data;

@Data
public class GetCertInfosRequest {
    private String api_key;
    private Integer activityYear;
    private Integer activitySemester;
}

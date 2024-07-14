package forif.univ_hanyang.cert.controller;

import forif.univ_hanyang.cert.dto.GetCertInfosResponse;
import forif.univ_hanyang.cert.dto.GetPostCertRequest;
import forif.univ_hanyang.cert.dto.GetPostCertResponse;
import forif.univ_hanyang.cert.dto.GetCertJsonResponse;
import forif.univ_hanyang.cert.service.CertService;
import forif.univ_hanyang.jwt.RequireJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cert")
public class CertController {
    private final CertService certService;

    @RequireJWT
    @GetMapping("/info")
    public List<GetCertInfosResponse> getCertInfos(
            @RequestParam(value="year", required = true) Integer activityYear,
            @RequestParam(value="semester", required = true) Integer activitySemester
    ) {
        return certService.getCertInfos(activityYear, activitySemester);
    }


    //cert-jsons
    @RequireJWT
    @ResponseBody
    @PostMapping
    public GetPostCertResponse postCertService(@RequestBody GetPostCertRequest getPostCertRequest) {
        GetPostCertResponse getPostCertResponse = new GetPostCertResponse();
        String id = certService.postCertService(getPostCertRequest);
        getPostCertResponse.setUri(id);
        return getPostCertResponse;
    }

    @RequireJWT
    @GetMapping
    public GetCertJsonResponse getCertJson(
            @RequestParam(value="certJsonId",required = true) String certJsonId
    ){
        return certService.getCertJson(certJsonId);
    }
}

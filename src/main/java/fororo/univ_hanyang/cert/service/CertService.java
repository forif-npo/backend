package fororo.univ_hanyang.cert.service;

import fororo.univ_hanyang.cert.dto.GetPostCertRequest;
import fororo.univ_hanyang.cert.dto.GetCertJsonResponse;
import fororo.univ_hanyang.cert.entity.CertJson;
import fororo.univ_hanyang.cert.repository.CertJsonRepository;
import fororo.univ_hanyang.clubInfo.entity.ClubInfo;
import fororo.univ_hanyang.clubInfo.repository.ClubInfoRepository;
import fororo.univ_hanyang.user.entity.User;
import fororo.univ_hanyang.user.repository.UserRepository;
import fororo.univ_hanyang.cert.dto.GetCertInfosResponse;
import fororo.univ_hanyang.cert.entity.IssueInfo;
import fororo.univ_hanyang.user.entity.UserStudy;
import fororo.univ_hanyang.study.entity.Study;
import fororo.univ_hanyang.cert.repository.IssueInfoRepository;
import fororo.univ_hanyang.user.repository.UserStudyRepository;
import fororo.univ_hanyang.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CertService {
    private final UserRepository userRepository;
    private final ClubInfoRepository clubInfoRepository;
    private final IssueInfoRepository issueInfoRepository;
    private final UserStudyRepository UserStudyRepository;
    private final StudyRepository studyRepository;
    private final CertJsonRepository certJsonRepository;

    public List<GetCertInfosResponse> getCertInfos(Integer activityYear, Integer activitySemester) {

        // clubInfo에서 activityYear, activitySemester에 해당하는 값과 사람이 있는지 확인.
        // 해당 값이 없으면 에러.
        Optional<ClubInfo> optionalClubInfo = clubInfoRepository.findByActivityYearAndActivitySemester(activityYear, activitySemester);
        if (optionalClubInfo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "학년 또는 학기가 잘못되었습니다.");
        }

        // 위 성공후 clubPresident의 Name에 해당하는 부분을 미리 저장함
        ClubInfo clubInfo = optionalClubInfo.get();

        // clubId를 사용하여 해당 학기의 스터디의 목록 뽑기
        List<Study> studies = studyRepository.findAllByClubId((clubInfo.getClubId()));

        // 반환할 객체의 리스트를 미리 선언 및 정의
        LinkedList<GetCertInfosResponse> ret = new LinkedList<>();

        // 스터디마다 반복
        for (Study study : studies) {
            // 해당 스터디를 수강한 학생의 목록 뽑기
            List<UserStudy> studentsStudies = UserStudyRepository.findAllById_StudyId(study.getStudyId());
            // 각 학생의 목록 별로 순회
            for (UserStudy oneUserStudy : studentsStudies) {
                User user = userRepository.findById(oneUserStudy.getId().getUserId()).get();
                Optional<IssueInfo> issueInfo1 = issueInfoRepository.findById_StudyId(oneUserStudy.getId().getStudyId());
                // 조건 : 출석 패스 & 해커톤 패스
                IssueInfo issueInfo = issueInfo1.get();
                boolean condition = issueInfo.getAttendancePass() && issueInfo.getHackathonPass();
                if (!condition) {
                    continue;
                }
                GetCertInfosResponse getCertInfosResponse = new GetCertInfosResponse();
                getCertInfosResponse.setUserId(user.getId());
                getCertInfosResponse.setUserName(user.getUserName());
                getCertInfosResponse.setActivityYear(activityYear);
                getCertInfosResponse.setActivitySemester(activitySemester);
                getCertInfosResponse.setStudyName(study.getStudyName());
                ret.add(getCertInfosResponse);
            }
        }
        return ret;
    }

    //cert-jsons
    public String postCertService(GetPostCertRequest getPostCertRequest) {
        String name = getPostCertRequest.getName();
        String study = getPostCertRequest.getStudy();
        String image = getPostCertRequest.getImage();
        CertJson certJson = new CertJson();
        certJson.setName(name);
        certJson.setStudy(study);
        certJson.setImage(image);
        String id = String.valueOf(certJson.getCertJsonId());
        certJsonRepository.save(certJson);
        return id;
    }

    public GetCertJsonResponse getCertJson(String certJsonId) {
        //certJsonId로 검색
        Optional<CertJson> optionalCertJson = certJsonRepository.findByCertJsonId(certJsonId);
        if (optionalCertJson.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "인증서 아이디가 잘못되었습니다.");
        }

        //response DTO에 담아서 반환
        CertJson certJson = optionalCertJson.get();
        GetCertJsonResponse getCertJsonResponse = new GetCertJsonResponse();
        getCertJsonResponse.setName(certJson.getName());
        getCertJsonResponse.setStudy(certJson.getStudy());
        getCertJsonResponse.setImage(certJson.getImage());
        return getCertJsonResponse;
    }
}

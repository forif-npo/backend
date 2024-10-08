package forif.univ_hanyang.hackathon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HackathonService {
    private final HackathonRepository hackathonRepository;

    public List<HackathonResponse> getHackathons() {
        return HackathonResponse.from(hackathonRepository.findAll());
    }
}

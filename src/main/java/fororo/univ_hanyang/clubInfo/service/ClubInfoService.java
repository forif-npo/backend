package fororo.univ_hanyang.clubInfo.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;

@Service
@Getter
@RequiredArgsConstructor
public class ClubInfoService {

    public Integer makeClubID(){
        int year =  LocalDate.now().getYear();
        int semester = 1;
        if(LocalDate.now().getMonthValue() > 6)
            semester = 2;

        return year * 100000 + semester * 1000 + 214;
    }
}

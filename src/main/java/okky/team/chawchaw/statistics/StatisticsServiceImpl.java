package okky.team.chawchaw.statistics;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.statistics.dto.HopeLanguageUsersDto;
import okky.team.chawchaw.statistics.dto.LanguageUsersDto;
import okky.team.chawchaw.statistics.dto.SchoolUsersDto;
import okky.team.chawchaw.user.UserRepository;
import okky.team.chawchaw.user.language.UserHopeLanguageRepository;
import okky.team.chawchaw.user.language.UserLanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{

    private final UserRepository userRepository;
    private final UserLanguageRepository userLanguageRepository;
    private final UserHopeLanguageRepository userHopeLanguageRepository;

    @Override
    public List<SchoolUsersDto> findSchoolRanks() {
        return userRepository.findSchoolRanks().stream().limit(10).collect(Collectors.toList());
    }

    @Override
    public List<LanguageUsersDto> findLanguageRanks() {
        return userLanguageRepository.findLanguageRanks().stream().limit(10).collect(Collectors.toList());
    }

    @Override
    public List<HopeLanguageUsersDto> findHopeLanguageRanks() {
        return userHopeLanguageRepository.findHopeLanguageRanks().stream().limit(10).collect(Collectors.toList());
    }
}

package okky.team.chawchaw.statistics.log;

import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.statistics.log.dto.CreateSearchLogDto;
import okky.team.chawchaw.statistics.log.dto.SearchLanguageUsersDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchLogServiceImpl implements SearchLogService {

    private final SearchLogRepository searchLogRepository;

    @Override
    @Transactional(readOnly = false)
    public void createSearchLog(CreateSearchLogDto createSearchLogDto) {
        searchLogRepository.save(new SearchLogEntity(createSearchLogDto.getUserId(), createSearchLogDto.getLanguageAbbr()));
    }

    @Override
    public List<SearchLanguageUsersDto> findLanguageRanks() {
        return searchLogRepository.findLanguageRanks().stream().limit(10).collect(Collectors.toList());
    }
}

package okky.team.chawchaw.admin;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.admin.dto.FindUserDto;
import okky.team.chawchaw.admin.dto.UserCardDto;
import okky.team.chawchaw.user.QUserEntity;
import okky.team.chawchaw.user.Role;
import okky.team.chawchaw.user.language.QLanguageEntity;
import okky.team.chawchaw.user.language.QUserHopeLanguageEntity;
import okky.team.chawchaw.user.language.QUserLanguageEntity;
import okky.team.chawchaw.utils.dto.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class AdminRepositorySupportImpl implements AdminRepositorySupport{

    private final JPAQueryFactory jpaQueryFactory;

    QUserEntity user = QUserEntity.userEntity;
    QUserLanguageEntity userLanguage = QUserLanguageEntity.userLanguageEntity;
    QUserHopeLanguageEntity userHopeLanguage = QUserHopeLanguageEntity.userHopeLanguageEntity;
    QLanguageEntity language = QLanguageEntity.languageEntity;


    @Override
    public Page<UserCardDto> findAllByElement(FindUserDto findUserDto) {

        Pageable pageable = new PageDto(findUserDto.getPageNo()).getPageable();

        QueryResults<UserCardDto> result = jpaQueryFactory
                .select(Projections.constructor(
                        UserCardDto.class,
                        user.id,
                        user.name,
                        user.school,
                        user.email,
                        user.repCountry,
                        user.repLanguage,
                        user.repHopeLanguage,
                        user.likeTo.size(),
                        user.views,
                        user.regDate
                ))
                .from(user)
                .where(
                        /* 구사할 수 있는 언어 */
                        StringUtils.hasText(findUserDto.getLanguage()) ?
                                user.in(
                                        JPAExpressions
                                                .select(userLanguage.user)
                                                .from(userLanguage)
                                                .join(userLanguage.user, user)
                                                .join(userLanguage.language, language)
                                                .where(userLanguage.language.abbr.eq(findUserDto.getLanguage()))
                                ) : null,
                        /* 배우길 희망하는 언어 */
                        StringUtils.hasText(findUserDto.getHopeLanguage()) ?
                                user.in(
                                        JPAExpressions
                                                .select(userHopeLanguage.user)
                                                .from(userHopeLanguage)
                                                .join(userHopeLanguage.user, user)
                                                .join(userHopeLanguage.hopeLanguage, language)
                                                .where(userHopeLanguage.hopeLanguage.abbr.eq(findUserDto.getHopeLanguage()))
                                ) : null,
                        /* 학교 */
                        StringUtils.hasText(findUserDto.getSchool()) ? user.school.eq(findUserDto.getSchool()) : null,
                        /* 이름 */
                        StringUtils.hasText(findUserDto.getName()) ? user.name.contains(findUserDto.getName()) : null,
                        /* 권한 */
                        user.role.ne(Role.ADMIN)
                )
                .orderBy(getSortedColumn(findUserDto.getOrder(), findUserDto.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private OrderSpecifier<?> getSortedColumn(String order, String sort) {
        if (StringUtils.hasText(order)) {
            if (sort.equals("desc")) {
                if (order.equals("like")) { return user.likeTo.size().desc(); }
                else if (order.equals("view")) { return user.views.desc(); }
                else if (order.equals("date")) { return user.regDate.desc(); }
                else if (order.equals("name")) { return user.name.desc(); }
            }
            else if (sort.equals("asc")) {
                if (order.equals("like")) { return user.likeTo.size().asc(); }
                else if (order.equals("view")) { return user.views.asc(); }
                else if (order.equals("date")) { return user.regDate.asc(); }
                else if (order.equals("name")) { return user.name.asc(); }
            }
        }
        return user.id.desc();
    }

}

package okky.team.chawchaw.admin;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.admin.dto.FindUserDto;
import okky.team.chawchaw.admin.dto.UserCardDto;
import okky.team.chawchaw.user.QUserEntity;
import okky.team.chawchaw.user.Role;
import okky.team.chawchaw.user.country.QCountryEntity;
import okky.team.chawchaw.user.country.QUserCountryEntity;
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
public class AdminRepositorySupportImpl implements AdminRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    private final QUserEntity user = QUserEntity.userEntity;
    private final QUserLanguageEntity userLanguage = QUserLanguageEntity.userLanguageEntity;
    private final QUserHopeLanguageEntity userHopeLanguage = QUserHopeLanguageEntity.userHopeLanguageEntity;
    private final QLanguageEntity language = QLanguageEntity.languageEntity;
    private final QUserCountryEntity userCountry = QUserCountryEntity.userCountryEntity;
    private final QCountryEntity country = QCountryEntity.countryEntity;

    @Override
    public Page<UserCardDto> findAllByFindUserDto(final FindUserDto findUserDto) {

        final Pageable pageable = new PageDto(findUserDto.getPageNo()).getPageable();

        final QueryResults<UserCardDto> result = jpaQueryFactory
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
                        containsCountry(findUserDto.getCountry()),
                        containsLanguage(findUserDto.getLanguage()),
                        containsHopeLanguage(findUserDto.getHopeLanguage()),
                        eqSchool(findUserDto.getSchool()),
                        containsName(findUserDto.getName()),
                        user.role.ne(Role.ADMIN)
                )
                .orderBy(getSortedColumn(findUserDto.getSort(), findUserDto.getOrder()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression containsCountry(final String countryName) {
        return StringUtils.hasText(countryName) ?
                user.in(
                        JPAExpressions
                                .select(userCountry.user)
                                .from(userCountry)
                                .join(userCountry.user, user)
                                .join(userCountry.country, country)
                                .where(userCountry.country.name.eq(countryName))
                ) : null;
    }

    private BooleanExpression containsLanguage(final String languageName) {
        return StringUtils.hasText(languageName) ?
                user.in(
                        JPAExpressions
                                .select(userLanguage.user)
                                .from(userLanguage)
                                .join(userLanguage.user, user)
                                .join(userLanguage.language, language)
                                .where(userLanguage.language.abbr.eq(languageName))
                ) : null;
    }

    private BooleanExpression containsHopeLanguage(final String hopeLanguageName) {
        return StringUtils.hasText(hopeLanguageName) ?
                user.in(
                        JPAExpressions
                                .select(userHopeLanguage.user)
                                .from(userHopeLanguage)
                                .join(userHopeLanguage.user, user)
                                .join(userHopeLanguage.hopeLanguage, language)
                                .where(userHopeLanguage.hopeLanguage.abbr.eq(hopeLanguageName))
                ) : null;
    }

    private BooleanExpression eqSchool(final String schoolName) {
        return StringUtils.hasText(schoolName) ? user.school.eq(schoolName) : null;
    }

    private BooleanExpression containsName(final String name) {
        return StringUtils.hasText(name) ? user.name.contains(name) : null;
    }

    private OrderSpecifier<?> getSortedColumn(final String sort, final String order) {
        final ComparableExpressionBase<?> sortedColumn;
        if (!StringUtils.hasText(sort) || !StringUtils.hasText(order))
            return user.id.desc();

        switch (sort) {
            case "like":
                sortedColumn = user.likeTo.size();
                break;
            case "view":
                sortedColumn = user.views;
                break;
            case "date":
                sortedColumn = user.regDate;
                break;
            case "name":
                sortedColumn = user.name;
                break;
            default:
                sortedColumn = user.id;
        }

        if (order.equals("asc"))
            return sortedColumn.asc();
        else
            return sortedColumn.desc();
    }
}

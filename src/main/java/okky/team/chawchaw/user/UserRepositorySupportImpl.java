package okky.team.chawchaw.user;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.user.dto.FindUserDto;
import okky.team.chawchaw.user.dto.UserCardDto;
import okky.team.chawchaw.user.language.QLanguageEntity;
import okky.team.chawchaw.user.language.QUserHopeLanguageEntity;
import okky.team.chawchaw.user.language.QUserLanguageEntity;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositorySupportImpl implements UserRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    private final QUserEntity user = QUserEntity.userEntity;
    private final QUserLanguageEntity userLanguage = QUserLanguageEntity.userLanguageEntity;
    private final QUserHopeLanguageEntity userHopeLanguage = QUserHopeLanguageEntity.userHopeLanguageEntity;
    private final QLanguageEntity language = QLanguageEntity.languageEntity;

    @Override
    public List<UserCardDto> findAllByFindUserDto(final FindUserDto findUserDto) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        UserCardDto.class,
                        user.id,
                        user.name,
                        user.imageUrl,
                        user.content,
                        user.repCountry,
                        user.repLanguage,
                        user.repHopeLanguage,
                        user.regDate,
                        user.likeTo.size()
                ))
                .from(user)
                .where(
                        containsLanguage(findUserDto.getLanguage()),
                        containsHopeLanguage(findUserDto.getHopeLanguage()),
                        eqSchool(findUserDto.getSchool()),
                        containsName(findUserDto.getName()),
                        notContainsExclude(findUserDto.getExclude()),
                        user.role.eq(Role.USER)
                )
                .orderBy(getSortedColumn(findUserDto.getSort()))
                .limit(getLimit(findUserDto.getIsFirst()))
                .fetch();
    }

    private int getLimit(final boolean isFirst) {
        return isFirst ? 6 : 3;
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

    private BooleanExpression notContainsExclude(final List<Long> excludes) {
        return excludes != null && !excludes.isEmpty() ? user.id.notIn(excludes) : null;
    }

    private OrderSpecifier<?> getSortedColumn(final String sort) {
        if (StringUtils.hasText(sort)) {
            switch (sort) {
                case "like":
                    return user.likeTo.size().desc();
                case "view":
                    return user.views.desc();
                case "date":
                    return user.regDate.desc();
            }
        }
        return Expressions.numberTemplate(Double.class, "function('rand')").asc();
    }
}

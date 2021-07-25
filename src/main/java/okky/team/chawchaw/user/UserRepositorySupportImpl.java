package okky.team.chawchaw.user;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import okky.team.chawchaw.user.dto.FindUserVo;
import okky.team.chawchaw.user.dto.RequestUserVo;
import okky.team.chawchaw.user.language.QLanguageEntity;
import okky.team.chawchaw.user.language.QUserHopeLanguageEntity;
import okky.team.chawchaw.user.language.QUserLanguageEntity;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
public class UserRepositorySupportImpl implements UserRepositorySupport{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UserEntity> findAllByElement(FindUserVo findUserVo) {

        String languageVo = findUserVo.getLanguage();
        String hopeLanguageVo = findUserVo.getHopeLanguage();
        String name = findUserVo.getName();

        if (languageVo == null)
            languageVo = "";
        if (hopeLanguageVo == null)
            hopeLanguageVo = "";
        if (name == null)
            name = "";

        QUserEntity user = QUserEntity.userEntity;
        QUserLanguageEntity userLanguage = QUserLanguageEntity.userLanguageEntity;
        QUserHopeLanguageEntity userHopeLanguage = QUserHopeLanguageEntity.userHopeLanguageEntity;
        QLanguageEntity language = QLanguageEntity.languageEntity;

        System.out.println(findUserVo.toString());

        jpaQueryFactory
                .selectFrom(user)
                .where(
                        user.in(
                                JPAExpressions
                                        .select(userLanguage.user)
                                        .from(userLanguage)
                                        .join(userLanguage.language, language)
                                        .where(userLanguage.language.abbr.eq(languageVo))
                        ),
                        user.in(
                                JPAExpressions
                                        .select(userHopeLanguage.user)
                                        .from(userHopeLanguage)
                                        .join(userHopeLanguage.hopeLanguage, language)
                                        .where(userHopeLanguage.hopeLanguage.abbr.eq(hopeLanguageVo))
                        ),
                        user.name.eq(name)
                ).fetchResults();

        return null;
    }

}

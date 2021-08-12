package com.hansung.vinyl.member.domain;

import com.hansung.vinyl.common.exception.validate.FormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("전화번호 도메인 테스트")
public class PhoneTest {
    @DisplayName("전화번호 생성 예외 - 형식이 틀린 경우")
    @ParameterizedTest
    @ValueSource(strings = {"1234567891", "-01024424476", "", " ", "010-2442-4476", "전화번호", "!@#$qwe123", "01324424476"})
    public void 전화번호_생성_예외(String phone) throws Exception {
        // when
        // then
        assertThatThrownBy(() -> new Phone(phone)).isInstanceOf(FormatException.class);
    }

    @DisplayName("전화번호 생성 확인 - 숫자만, 10자리 혹은 11자리 허용, 앞 3자리는 010,011,016,017,018,019 허용")
    @ParameterizedTest
    @ValueSource(strings = {"01024424476","01124424476","01624424476","01724424476","01824424476","01924424476","0102442447"})
    public void 전화번호_생성_확인(String phoneNumber) throws Exception {
        //when
        Phone phone = new Phone(phoneNumber);

        //then
        assertThat(phone).isEqualTo(new Phone(phoneNumber));
    }
}

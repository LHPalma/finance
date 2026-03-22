package com.falizsh.finance.infrastructure.security.encoder;

import com.falizsh.finance.support.TestSupport;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class Argon2WithPepperEncoderTest extends TestSupport {

    private Argon2WithPepperEncoder encoder;

    private static final String TEST_PEPPER = "test-pepper-2024";
    private static final String RAW_PASSWORD = "senha123";

    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;
    private static final int PARALLELISM = 4;
    private static final int MEMORY = 65536;
    private static final int ITERATIONS = 3;

    @Override
    public void init() {
        this.encoder = new Argon2WithPepperEncoder(
                TEST_PEPPER,
                SALT_LENGTH,
                HASH_LENGTH,
                PARALLELISM,
                MEMORY,
                ITERATIONS
        );
    }

    @Test
    void shouldEncodePasswordWithPepper() {
        String encodedPassword = encoder.encode(RAW_PASSWORD);

        assertThat(encodedPassword)
                .isNotNull()
                .isNotEmpty()
                .startsWith("$argon2id$")
                .doesNotContain(TEST_PEPPER)
                .doesNotContain(RAW_PASSWORD);
    }

    @Test
    void shouldGenerateDifferentHashesForSamePassword() {
        String hash1 = encoder.encode(RAW_PASSWORD);
        String hash2 = encoder.encode(RAW_PASSWORD);

        assertThat(hash1)
                .isNotEqualTo(hash2);
    }

    @Test
    void shouldMatchCorrectPasswordWithPepper() {
        String encodedPassword = encoder.encode(RAW_PASSWORD);

        boolean matches = encoder.matches(RAW_PASSWORD, encodedPassword);

        assertThat(matches).isTrue();
    }

    @Test
    void shouldNotMatchIncorrectPassword() {
        String encodedPassword = encoder.encode(RAW_PASSWORD);
        String wrongPassword = "senhaErrada";

        boolean matches = encoder.matches(wrongPassword, encodedPassword);

        assertThat(matches).isFalse();
    }

    @Test
    void shouldNotMatchIfPepperIsDifferent() {
        Argon2WithPepperEncoder encoderWithPepper1 = new Argon2WithPepperEncoder(
                "pepper1", SALT_LENGTH, HASH_LENGTH, PARALLELISM, MEMORY, ITERATIONS
        );
        Argon2WithPepperEncoder encoderWithPepper2 = new Argon2WithPepperEncoder(
                "pepper2", SALT_LENGTH, HASH_LENGTH, PARALLELISM, MEMORY, ITERATIONS
        );

        String encodedPassword = encoderWithPepper1.encode(RAW_PASSWORD);
        boolean matches = encoderWithPepper2.matches(RAW_PASSWORD, encodedPassword);

        assertThat(matches).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenEncodingNullPassword() {
        assertThatThrownBy(() -> encoder.encode(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldReturnFalseWhenMatchingNullPassword() {
        String encodedPassword = encoder.encode(RAW_PASSWORD);

        boolean matches = encoder.matches(null, encodedPassword);

        assertThat(matches).isFalse();
    }

    @Test
    void shouldReturnFalseWhenMatchingAgainstNullHash() {
        boolean matches = encoder.matches(RAW_PASSWORD, null);

        assertThat(matches).isFalse();
    }

    @Test
    void shouldEncodeEmptyPasswordSuccessfully() {
        String encodedPassword = encoder.encode("");

        assertThat(encodedPassword)
                .isNotNull()
                .isNotEmpty()
                .startsWith("$argon2id$");
    }

    @Test
    void shouldMatchEmptyPasswordCorrectly() {
        String encodedPassword = encoder.encode("");

        boolean matches = encoder.matches("", encodedPassword);

        assertThat(matches).isTrue();
    }

    @Test
    void shouldHandleVeryLongPasswords() {
        String longPassword = "a".repeat(1000);

        String encodedPassword = encoder.encode(longPassword);
        boolean matches = encoder.matches(longPassword, encodedPassword);

        assertThat(encodedPassword).isNotNull();
        assertThat(matches).isTrue();
    }

    @Test
    void shouldHandleSpecialCharactersInPassword() {
        String specialPassword = "P@ssw0rd!#$%^&*(){}[]<>?/";

        String encodedPassword = encoder.encode(specialPassword);
        boolean matches = encoder.matches(specialPassword, encodedPassword);

        assertThat(encodedPassword).isNotNull();
        assertThat(matches).isTrue();
    }

    @Test
    void shouldUseConfiguredArgon2Parameters() {
        String encodedPassword = encoder.encode(RAW_PASSWORD);

        assertThat(encodedPassword)
                .contains("m=" + MEMORY)
                .contains("t=" + ITERATIONS)
                .contains("p=" + PARALLELISM);
    }

    @Test
    void shouldProduceHashesWithDifferentParameterMetadata() {
        Argon2WithPepperEncoder weakEncoder = new Argon2WithPepperEncoder(
                TEST_PEPPER, 16, 32, 1, 16384, 2
        );
        Argon2WithPepperEncoder strongEncoder = new Argon2WithPepperEncoder(
                TEST_PEPPER, 16, 32, 4, 65536, 3
        );

        String weakHash = weakEncoder.encode(RAW_PASSWORD);
        String strongHash = strongEncoder.encode(RAW_PASSWORD);

        assertThat(weakHash)
                .contains("m=16384")
                .contains("t=2")
                .contains("p=1");

        assertThat(strongHash)
                .contains("m=65536")
                .contains("t=3")
                .contains("p=4");

        assertThat(weakEncoder.matches(RAW_PASSWORD, weakHash)).isTrue();
        assertThat(strongEncoder.matches(RAW_PASSWORD, strongHash)).isTrue();
    }
}
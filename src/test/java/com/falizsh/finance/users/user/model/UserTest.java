package com.falizsh.finance.users.user.model;

import com.falizsh.finance.support.TestSupport;
import com.falizsh.finance.users.userEmail.model.UserEmail;
import com.falizsh.finance.users.userEmail.model.UserEmailType;
import com.falizsh.finance.users.userTelephone.model.TelephoneType;
import com.falizsh.finance.users.userTelephone.model.UserTelephone;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the {@link User} aggregate root entity.
 * Focuses on business logic validation for aggregates like Telephones and Emails.
 */
class UserTest extends TestSupport {

    private User user;

    /**
     * Setup method to initialize a clean User instance before each test.
     */
    @Override
    public void init() {
        user = User.builder()
                .telephones(new ArrayList<>())
                .emails(new ArrayList<>())
                .addresses(new ArrayList<>())
                .build();
    }

    /**
     * Ensures that when the first telephone of a specific type is added,
     * it is automatically set as the primary telephone for that type.
     */
    @Test
    void should_add_telephone_automatically_primary_when_first_of_type() {
        user.addTelephone("999990001", "11", TelephoneType.PERSONAL, false);

        assertThat(user.getTelephones()).hasSize(1);

        UserTelephone telephone = user.getTelephones().stream().findFirst().get();
        assertThat(telephone.getIsPrimary()).isTrue();
        assertThat(telephone.getType()).isEqualTo(TelephoneType.PERSONAL);
    }

    /**
     * Verifies that the "isPrimary" flag is mutually exclusive only within the same TelephoneType.
     * Adding a primary COMMERCIAL phone should not unset a primary PERSONAL phone.
     */
    @Test
    void should_manage_primary_status_scoped_by_type() {
        user.addTelephone("999990001", "11", TelephoneType.PERSONAL, true);

        user.addTelephone("33330000", "11", TelephoneType.COMMERCIAL, true);

        assertThat(user.getTelephones())
                .extracting(UserTelephone::getIsPrimary)
                .containsExactly(true, true);

        user.addTelephone("999990002", "11", TelephoneType.PERSONAL, true);

        assertThat(user.getTelephones())
                .filteredOn(t -> t.getTelephone().equals("999990001"))
                .extracting(UserTelephone::getIsPrimary)
                .containsExactly(false);

        assertThat(user.getTelephones())
                .filteredOn(t -> t.getTelephone().equals("999990002"))
                .extracting(UserTelephone::getIsPrimary)
                .containsExactly(true);

        assertThat(user.getTelephones())
                .filteredOn(t -> t.getType() == TelephoneType.COMMERCIAL)
                .extracting(UserTelephone::getIsPrimary)
                .containsExactly(true);
    }

    /**
     * Verifies that the system throws an exception when attempting to add a telephone
     * that already exists for the specific user and type.
     */
    @Test
    void should_throw_exception_when_adding_duplicate_telephone_same_type() {
        user.addTelephone("999990001", "11", TelephoneType.PERSONAL, true);

        assertThatThrownBy(() ->
                user.addTelephone("999990001", "11", TelephoneType.PERSONAL, false)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Telephone.already.exists");
    }

    /**
     * Verifies that the same telephone number can be registered if the TelephoneType is different.
     */
    @Test
    void should_allow_same_number_for_different_types() {
        user.addTelephone("999990001", "11", TelephoneType.PERSONAL, true);

        user.addTelephone("999990001", "11", TelephoneType.PROFESSIONAL, true);

        assertThat(user.getTelephones()).hasSize(2);
    }

    /**
     * Verifies the logic of setting a specific telephone as primary by its ID.
     * Should ensure other telephones of the same type are unmarked as primary.
     */
    @Test
    void should_set_primary_telephone_by_id_and_unset_others_of_same_type() {
        UserTelephone t1 = UserTelephone.builder().id(1L).type(TelephoneType.PERSONAL).user(user).telephone("1").isPrimary(true).build();
        UserTelephone t2 = UserTelephone.builder().id(2L).type(TelephoneType.PERSONAL).user(user).telephone("2").isPrimary(false).build();

        User userWithIds = User.builder()
                .telephones(new ArrayList<>(java.util.Arrays.asList(t1, t2)))
                .build();

        userWithIds.setPrimaryTelephone(2L);

        assertThat(t1.getIsPrimary()).isFalse();
        assertThat(t2.getIsPrimary()).isTrue();
    }

    /**
     * Verifies that adding a new primary email unsets the primary status of any existing emails.
     */
    @Test
    void should_manage_primary_email() {
        user.addEmail("pessoal@teste.com", UserEmailType.PERSONAL, true);
        user.addEmail("trabalho@teste.com", UserEmailType.PROFESSIONAL, true);

        assertThat(user.getEmails())
                .filteredOn(e -> e.getEmail().equals("pessoal@teste.com"))
                .extracting(UserEmail::getIsPrimary)
                .containsExactly(false);

        assertThat(user.getEmails())
                .filteredOn(e -> e.getEmail().equals("trabalho@teste.com"))
                .extracting(UserEmail::getIsPrimary)
                .containsExactly(true);
    }
}
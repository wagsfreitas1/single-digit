package org.wagner.single_digit.domain.entity;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserTest {

    @Test
    void updateFrom_givenUser_shouldUpdate() {
        User user = new User();
        user.setName("name");
        user.setEmail("email");

        User userToChangeFrom = new User();
        userToChangeFrom.setName("newName");
        userToChangeFrom.setEmail("newEmail");

        user.updateFrom(userToChangeFrom);

        assertEquals(userToChangeFrom.getName(), user.getName());
        assertEquals(userToChangeFrom.getEmail(), user.getEmail());
    }

    @Test
    void addSingleDigit_givenSingleDigit_shouldAdd() {
        SingleDigit singleDigit = new SingleDigit();
        User user = new User();

        user.addSingleDigit(singleDigit);

        assertEquals(1, user.getSingleDigits().size());
        assertEquals(singleDigit, user.getSingleDigits().get(0));
        assertEquals(user, user.getSingleDigits().get(0).getUser());
    }
}
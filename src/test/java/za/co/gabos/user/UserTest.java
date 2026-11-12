package za.co.gabos.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    private User user1;

    @BeforeEach
    public void setUp(){
        user1 = new User("testUser1@gmail.com","123Password");
    }

    @Test
    public void userEmailNullTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            new User(null, "passsword123");
        });
    }


}

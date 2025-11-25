package com.hotel.Service;

import com.hotel.Model.Admin;
import com.hotel.Model.Guest;
import com.hotel.Model.User;
import com.hotel.Model.UserRole;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    private UserManager manager;

    @BeforeEach
    void setUp() {
        manager = new UserManager();
    }

    @Test
    void testGenerateUserIdIncrements() {
        String id1 = manager.generateUserId("U");
        String id2 = manager.generateUserId("U");
        assertNotEquals(id1, id2);
        assertTrue(id1.startsWith("U"));
        assertTrue(id2.startsWith("U"));
    }

    @Test
    void testAddAndGetUserByIdAndEmail() {
        Admin a = new Admin("A100", "Admin", "admin@hotel.com", "087", "pass");
        manager.addUser(a);

        User byId = manager.getUser("A100");
        assertEquals(a, byId);

        User byEmail = manager.getUserByEmail("ADMIN@hotel.com"); // case-insensitive
        assertEquals(a, byEmail);

        assertEquals(1, manager.getTotalUserCount());
        assertTrue(manager.emailExists("admin@hotel.com"));
    }

    @Test
    void testAddUserNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> manager.addUser(null));
    }

    @Test
    void testAddDuplicateIdThrows() {
        Admin a1 = new Admin("I1", "One", "one@h.com", "087", "p");
        Admin a2 = new Admin("I1", "Two", "two@h.com", "087", "p");
        manager.addUser(a1);
        assertThrows(IllegalArgumentException.class, () -> manager.addUser(a2));
    }

    @Test
    void testAddDuplicateEmailThrows() {
        Admin a1 = new Admin("I10", "One", "dup@h.com", "087", "p");
        Admin a2 = new Admin("I11", "Two", "DUP@h.com", "087", "p");
        manager.addUser(a1);
        assertThrows(IllegalArgumentException.class, () -> manager.addUser(a2));
    }

    @Test
    void testGetUserInvalidIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> manager.getUser(null));
        assertThrows(IllegalArgumentException.class, () -> manager.getUser("   "));
    }

    @Test
    void testGetUserByEmailInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () -> manager.getUserByEmail(null));
        assertThrows(IllegalArgumentException.class, () -> manager.getUserByEmail("  "));
    }

    @Test
    void testAuthenticateUser() {
        Admin a = new Admin("A200", "Auth", "auth@h.com", "087", "secret");
        manager.addUser(a);

        User ok = manager.authenticateUser("auth@h.com", "secret");
        assertEquals(a, ok);

        User bad = manager.authenticateUser("auth@h.com", "wrong");
        assertNull(bad);

        User nulls = manager.authenticateUser(null, null);
        assertNull(nulls);
    }

    @Test
    void testUpdateUserHappyPathAndEmailChangeConflict() {
        Admin a = new Admin("U1", "Original", "orig@h.com", "087", "pw");
        Guest g = new Guest("G1", "Guest", "guest@h.com", "087", "gpass");
        manager.addUser(a);
        manager.addUser(g);

        // change admin email to a new unused email
        Admin updated = new Admin("U1", "Original", "new@h.com", "087", "pw");
        manager.updateUser(updated);
        assertEquals("new@h.com", manager.getUser("U1").getEmail());

        // attempt to change admin email to one already used by guest
        Admin conflict = new Admin("U1", "Original", "guest@h.com", "087", "pw");
        assertThrows(IllegalArgumentException.class, () -> manager.updateUser(conflict));
    }

    @Test
    void testUpdateUserNullAndNotFound() {
        assertThrows(IllegalArgumentException.class, () -> manager.updateUser(null));

        Admin missing = new Admin("M1", "Missing", "m@h.com", "087", "pw");
        assertThrows(IllegalArgumentException.class, () -> manager.updateUser(missing));
    }

    @Test
    void testDeleteUser() {
        Admin a = new Admin("D1", "Del", "del@h.com", "087", "pw");
        manager.addUser(a);
        assertTrue(manager.deleteUser("D1"));
        assertFalse(manager.emailExists("del@h.com"));
        assertFalse(manager.deleteUser("D1"));
    }

    @Test
    void testGetAllUsersAndByRoleAndSearch() {
        Admin a = new Admin("R1", "Alice Admin", "alice@h.com", "087", "pw");
        Guest g1 = new Guest("R2", "Bob Guest", "bob@h.com", "087", "pw");
        Guest g2 = new Guest("R3", "Bobby Guest", "bobby@h.com", "087", "pw");
        manager.addUser(a);
        manager.addUser(g1);
        manager.addUser(g2);

        List<User> all = manager.getAllUsers();
        assertEquals(3, all.size());

        List<User> admins = manager.getUsersByRole(UserRole.ADMIN);
        assertEquals(1, admins.size());
        assertEquals(a, admins.get(0));

        assertThrows(IllegalArgumentException.class, () -> manager.getUsersByRole(null));

        List<User> searchEmpty = manager.searchUsersByName(null);
        assertTrue(searchEmpty.isEmpty());

        List<User> searchBob = manager.searchUsersByName("bob");
        assertEquals(2, searchBob.size()); // matches Bob and Bobby
    }

    @Test
    void testEmailExistsAndGetTotalCount() {
        assertFalse(manager.emailExists(null));
        Guest g = new Guest("E1", "Eddie", "eddie@h.com", "087", "pw");
        manager.addUser(g);
        assertTrue(manager.emailExists("eddie@h.com"));
        assertEquals(1, manager.getTotalUserCount());
    }
}

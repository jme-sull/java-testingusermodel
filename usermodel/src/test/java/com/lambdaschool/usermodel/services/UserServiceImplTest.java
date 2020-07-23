package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest
{
    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception
    {
        //mocks -> fake data
        //stubs -> fake methods
        MockitoAnnotations.initMocks(this);

        List<User> myList = userService.findAll();

        for (User u : myList)
        {
            System.out.println(u.getUserid() + " " + u.getUsername());
        }

    }


    @After
    public void tearDown() throws Exception
    {
    }


    @Test
    public void a_findUserById()
    {
        assertEquals("test-cinnamon", userService.findUserById(7).getUsername());

    }

    @Test
    public void b_findByNameContaining()
    {
        assertEquals(1, userService.findByNameContaining("kitty").size());
    }

    @Test
    public void c_findAll()
    {
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void d_delete()
    {
        userService.delete(13);
        assertEquals(4, userService.findAll().size());
    }

    @Test
    public void e_findByName()
    {
        User user = userService.findByName("test-barnbarn");
        assertNotNull(user);
        assertEquals("test-barnbarn", user.getUsername());
    }

    @Test
    public void f_save()
    {
        User u1 = new User("test-jamie",
            "password",
            "jamie@lambdaschool.local");
        u1.getUseremails()
            .add(new Useremail(u1,
                "jamie@email.local"));
        u1.getUseremails()
            .add(new Useremail(u1,
                "jamie@mymail.local"));

        User addUser = userService.save(u1);
        assertNotNull(addUser);
        assertEquals("test-jamie", addUser.getUsername());
    }


    @Test
    public void g_update()
    {
        User u2 = new User("test-cinnamon",
            "1234567",
            "jkcrawshaw@gmail.com");
        u2.getUseremails()
            .add(new Useremail(u2,
                "cinnamon@mymail.local"));
        u2.getUseremails()
            .add(new Useremail(u2,
                "hops@mymail.local"));
        u2.getUseremails()
            .add(new Useremail(u2,
                "bunny@email.local"));

        User updateUser = userService.update(u2,7);
        assertNotNull(updateUser);
        assertEquals("jkcrawshaw@gmail.com", updateUser.getPrimaryemail());
    }

    @Test
    public void h_deleteAll()
    {
        userService.deleteAll();
        assertEquals(0, userService.findAll().size());
    }
}
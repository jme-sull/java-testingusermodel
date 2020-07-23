package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    private List<User> userList;



    @Before
    public void setUp() throws Exception
    {
        userList = new ArrayList<>();

        User u1 = new User("test-admin",
            "password",
            "admin@lambdaschool.local");
        u1.setUserid(1);
        userList.add(u1);

        User u2 = new User("test-cinnamon",
            "1234567",
            "cinnamon@lambdaschool.local");
        u2.setUserid(2);
        userList.add(u2);

        User u3 = new User("test-barnbarn",
            "ILuvM4th!",
            "barnbarn@lambdaschool.local");
        u3.setUserid(3);
        userList.add(u3);

        User u4 = new User("test-puttat",
            "password",
            "puttat@school.lambda");
        u4.setUserid(4);
        userList.add(u4);

        User u5 = new User("test-misskitty",
            "password",
            "misskitty@school.lambda");
        u5.setUserid(5);
        userList.add(u5);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void a_listAllUsers() throws Exception
    {
        String apiURL =  "/users/users";
        Mockito.when(userService.findAll()).thenReturn(userList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiURL).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList);

        assertEquals(er, tr);

    }

    @Test
    public void b_getUserById() throws Exception
    {
        String apiURL =  "/users/user/2";
        Mockito.when(userService.findUserById(2)).thenReturn(userList.get(1));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiURL).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(1));

        assertEquals(er, tr);
    }

    @Test
    public void c_getUserByName() throws Exception
    {
        String apiURL =  "/users/user/name/barnbarn";
        Mockito.when(userService.findByName("barnbarn")).thenReturn(userList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiURL).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(0));

        assertEquals(er, tr);
    }

    @Test
    public void d_getUserLikeName() throws Exception
    {
        String apiURL =  "/users/user/name/like/barn";
        Mockito.when(userService.findByNameContaining("barn")).thenReturn(userList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiURL).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList);

        assertEquals(er, tr);
    }


    @Test
    public void e_addNewUser() throws Exception
    {
        String apiURL =  "/users/user";
        User u1 = new User("test-jamie",
            "password",
            "jamie@lambdaschool.local");
        u1.getUseremails()
            .add(new Useremail(u1,
                "jamie@email.local"));
        u1.getUseremails()
            .add(new Useremail(u1,
                "jamie@mymail.local"));
        u1.setUserid(100);
        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u1);


        Mockito.when(userService.save(any(User.class))).thenReturn(u1);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiURL)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(userString);
        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void f_updateFullUser() throws Exception
    {
        String apiURL = "/users/user/100";
        User u1 = new User("test-jamie",
            "password",
            "jamie@lambdaschool.local");
        u1.getUseremails()
            .add(new Useremail(u1,
                "jamie@email.local"));
        u1.getUseremails()
            .add(new Useremail(u1,
                "jamie@mymail.local"));
        u1.setUserid(100);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u1);

        Mockito.when(userService.save(u1))
            .thenReturn(u1);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiURL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userString);
        mockMvc.perform(rb)
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }
    

    @Test
    public void h_deleteUserById() throws Exception
    {
        String apiURL = "/users/user/100";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiURL, 100)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }
}
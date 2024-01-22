package com.gps.server.model.data;

import com.gps.server.Register;
import com.gps.shared_resources.Request;
import com.gps.shared_resources.User;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DBHandlerTest {
    DBHandler dbHandler = new DBHandler();

    DBHandlerTest() throws SQLException {
    }

    @Test
    void loginUsernamEPasswordErrada() {
        Request request = new Request();
        request.setId(10);
        Pair<String,String> login = new Pair<>("joao","jose");
        request.setRequest(login);
        File file = new File("t.tmp");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            assertFalse(dbHandler.login(request,oos));
            oos.close();
            fos.close();
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loginPasswordErrada() {
        Request request = new Request();
        request.setId(10);
        Pair<String,String> login = new Pair<>("1","2");
        request.setRequest(login);
        File file = new File("t.tmp");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            assertFalse(dbHandler.login(request,oos));
            oos.close();
            fos.close();
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loginCorreto() {
        Request request = new Request();
        Pair<String,String> login = new Pair<>("1","1");
        request.setRequest(login);
        File file = new File("t.tmp");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            assertTrue(dbHandler.login(request,oos));
            oos.close();
            fos.close();
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void logout() {
        loginCorreto();
        Request request = new Request();
        request.setId(22);
        assertTrue(dbHandler.disconnect(request,null));
    }

    @Test
    void registerInvalidEmail() {
        Request request = new Request();
        Register register = new Register("eduardo","edu@gmail","123",new Date());
        request.setRequest(register);
        File file = new File("t.tmp");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            assertFalse(dbHandler.register(request,oos));
            oos.close();
            fos.close();
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerValidEmailInvalidUserName() {
        Request request = new Request();
        Register register = new Register("edu","edu@gmail.com","123",new Date());
        request.setRequest(register);
        File file = new File("t.tmp");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            assertFalse(dbHandler.register(request,oos));
            oos.close();
            fos.close();
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerInvalidPassword() {
        Request request = new Request();
        Register register = new Register("eduardo bento","edu@gmail.com","123",new Date());
        request.setRequest(register);
        File file = new File("t.tmp");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            assertFalse(dbHandler.register(request,oos));
            oos.close();
            fos.close();
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerInvalidPassword2() {
        Request request = new Request();
        Register register = new Register("eduardo bento","edu@gmail.com","123A",new Date());
        request.setRequest(register);
        File file = new File("t.tmp");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            assertFalse(dbHandler.register(request,oos));
            oos.close();
            fos.close();
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerInvalidPassword3LowerCase() {
        Request request = new Request();
        Register register = new Register("eduardo bento","edu@gmail.com","123sdasdsad",new Date());
        request.setRequest(register);
        File file = new File("t.tmp");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            assertFalse(dbHandler.register(request,oos));
            oos.close();
            fos.close();
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerInvalidPassword3Length() {
        Request request = new Request();
        Register register = new Register("eduardo bento","edu@gmail.com","123Aad",new Date());
        request.setRequest(register);
        File file = new File("t.tmp");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            assertFalse(dbHandler.register(request,oos));
            oos.close();
            fos.close();
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerInvalidDate() {
        Request request = new Request();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        Register register = new Register("eduardo bento","edu@gmail.com","123Aaasdasdd",calendar.getTime());
        request.setRequest(register);
        File file = new File("t.tmp");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            assertFalse(dbHandler.register(request,oos));
            oos.close();
            fos.close();
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerValid() {
        Request request = new Request();
        Register register = new Register("eduardo bento","edu@gmail.com","123Aaasdasdd",new Date());
        request.setRequest(register);
        File file = new File("t.tmp");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            assertTrue(dbHandler.register(request,oos));
            oos.close();
            fos.close();
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeWorker() {
        User user = new User(27,"rui");
        Pair<Boolean,String> pair = dbHandler.removeWorker(user);
        assertTrue(pair.getKey());
    }

    @Test
    void removeWorkerInvalid() {
        User user = new User(38,"1");
        Pair<Boolean,String> pair = dbHandler.removeWorker(user);
        System.out.println(pair.getValue());
        assertFalse(pair.getKey());
    }
}
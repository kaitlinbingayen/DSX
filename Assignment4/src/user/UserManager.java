package user;

import exceptions.DataValidationException;
import exceptions.InvalidValueException;
import tradable.TradableDTO;

import java.util.HashMap;
import java.util.Random;

public final class UserManager {

    private static UserManager instance;
    private HashMap<String, User> users;

    private UserManager() {
        users = new HashMap<>();
    }

    public static UserManager getInstance(){
        if (instance == null){
            instance = new UserManager();
        }
        return instance;
    }
    public void init(String[] usersIn) throws DataValidationException, InvalidValueException {
        if (usersIn == null){
            throw new DataValidationException("User Id is null");
        }
        for (String userId : usersIn){
            User user = new User(userId);
            users.put(userId, user);
        }
    }

    public User getRandomUser(){
        if (users.isEmpty()) {
            return null;
        }
        Object[] userArray = users.values().toArray();
        return (User) userArray[new Random().nextInt(userArray.length)];
    }

    public void addToUser(String userId, TradableDTO o) throws DataValidationException {
        if (userId == null | o == null){
            throw new DataValidationException("User ID and Tradable cannot be null");
        }
        User user = users.get(userId);
        if (user == null) {
            throw new DataValidationException("Invalid user id");
        }
        user.addTradable(o);
    }

    public User getUser(String id){
        if (!users.containsKey(id) || id == null){
            return null;
        }
        return users.get(id);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (User user : users.values()) {
            sb.append(user).append("\n");
        }
        return sb.toString();
    }
}

package com.mytway.database.userLocalizations;


import android.content.Context;

public class UserLocalizationsFacade {

    public int insertUserLocalizationToDb(Context context, UserLocalizationsTable userLocalizationsTable){
        UserLocalizationsRepo userRepo = new UserLocalizationsRepo(context);
        int result = userRepo.insert(userLocalizationsTable);
        return result;
    }

    public int updateUserLocalizationToDb(Context context, UserLocalizationsTable userLocalizationsTable){
        UserLocalizationsRepo userRepo = new UserLocalizationsRepo(context);
        int result = userRepo.update(userLocalizationsTable);
        return result;
    }

    public int updateByUserNameUserLocalizationToDb(Context context, UserLocalizationsTable userLocalizationsTable){
        UserLocalizationsRepo userRepo = new UserLocalizationsRepo(context);
        int result = userRepo.updateByUserName(userLocalizationsTable);
        return result;
    }

    public int deleteUserLocalizationFromDb(Context context, int userId){
        UserLocalizationsRepo userRepo = new UserLocalizationsRepo(context);
        int result = userRepo.delete(userId);
        return result;
    }

    public UserLocalizationsTable selectUserLocalizationByIdFromDb(Context context, int userId){
        UserLocalizationsRepo userRepo = new UserLocalizationsRepo(context);
        UserLocalizationsTable userLocalization = userRepo.getUserLocalizationsById(userId);
        return userLocalization;
    }

}

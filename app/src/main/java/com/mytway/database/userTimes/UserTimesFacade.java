package com.mytway.database.userTimes;

import android.content.Context;

public class UserTimesFacade {

    public int insertUserLocalizationToDb(Context context, UserTimesTable userTimesTable){
        UserTimesRepo userRepo = new UserTimesRepo(context);
        int result = userRepo.insert(userTimesTable);
        return result;
    }

    public int updateUserLocalizationToDb(Context context, UserTimesTable userTimesTable){
        UserTimesRepo userRepo = new UserTimesRepo(context);
        int result = userRepo.update(userTimesTable);
        return result;
    }

    public int updateByUserNameUserLocalizationToDb(Context context, UserTimesTable userTimesTable){
        UserTimesRepo userRepo = new UserTimesRepo(context);
        int result = userRepo.updateByUserName(userTimesTable);
        return result;
    }

    public int deleteUserLocalizationFromDb(Context context, int userId){
        UserTimesRepo userRepo = new UserTimesRepo(context);
        int result = userRepo.delete(userId);
        return result;
    }

    public UserTimesTable selectUserLocalizationByIdFromDb(Context context, int userId){
        UserTimesRepo userRepo = new UserTimesRepo(context);
        UserTimesTable userLocalization = userRepo.getUserById(userId);
        return userLocalization;
    }
    
}

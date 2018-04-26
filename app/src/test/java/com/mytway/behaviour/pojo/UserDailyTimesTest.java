package com.mytway.behaviour.pojo;

import org.junit.Test;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest({Log.class})
public class UserDailyTimesTest {

    @Test
    public void testOfStringToBoolean() {
        boolean wasSavedArriveToHomeTimeBefore = true;
        String booleanToStringAfterSetter = String.valueOf(wasSavedArriveToHomeTimeBefore);

        boolean wasSavedStartWorkTimeBefore;
        String wasSavedStartWorkTimeBeforeString = String.valueOf(wasSavedArriveToHomeTimeBefore);
        if(wasSavedStartWorkTimeBeforeString.equals("")){
            wasSavedStartWorkTimeBefore = false;
        }else{
            wasSavedStartWorkTimeBefore = Boolean.getBoolean(wasSavedStartWorkTimeBeforeString);
        }
    }

    @Test
    public void testofGetBoolean(){
        boolean wasSavedStartWorkTimeBefore = Boolean.valueOf("true");
    }
}




//        extends TestCase {

//    @InjectMocks
//    private UserDailyTimes userDailyTimes;
//
//    @Mock
//    private SharedPreferences sharedPreferences;
//
//    @Mock
//    private Context context;
//
//    @Before
//    public void initMocks() {
//        MockitoAnnotations.initMocks(this);
//    }

//    public void testGetWasSavedLeaveHomeTimeBefore() throws Exception {
//        //given
//        boolean wasSavedArriveToHomeTimeBefore = true;
//        String booleanToStringAfterSetter = String.valueOf(wasSavedArriveToHomeTimeBefore);
//
//        userDailyTimes = new UserDailyTimes(context);
//        userDailyTimes.setSharedPreferences(sharedPreferences);
//        Mockito.when(context.getSharedPreferences(SharedPreferencesNames.USER_SHARED_PREFERENCES, 1)).thenReturn(sharedPreferences);
//        Mockito.when(sharedPreferences.getString(UserDailyTimes.WAS_SAVED_LEAVE_HOME_TIME_BEFORE , "")).thenReturn(booleanToStringAfterSetter);
//        //when
//        boolean result  = userDailyTimes.getWasSavedLeaveHomeTimeBefore();
//
//        //then

//    }
//
//}
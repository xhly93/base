package com.ssj.user.Utils;

/**
 * Created by 王矩龙 on 2018/2/28.
 */

public class Contants {
    public static final String WEIXIN_APP_ID = "wx659f3c384093e279";
    public static final String WEIXIN_APP_SECRET = "308fc96c5a480cc65aebc25daf16c1a5";
    public class NetWorkUrl {
        public static final String URL = "http://180.169.188.66:28062";
        public static final String BASE_URL_PARENT = "https://xt.sharingschool.com/";
        public static final String REGISTER_URL = URL + "/student/user/registerUser/0";
        public static final String REGISTER_VERIFY_CODE_URL = URL + "/student/msg/sendVerifyCode4Register/0?";
        public static final String PASSWORD_VERIFY_CODE_URL = URL + "/student/msg/sendVerifyCode4FindPwd/0?";
        public static final String LOG_OUT_URL = URL + "/student/user/logout/0";
        public static final String LOG_IN_URL = URL + "/student/user/login/0";
        public static final String FORGET_PASSWORD_URL = URL + "/student/user/updateUserPwd/0";
        public static final String UPDATE_USRINFO_URL = URL + "/student/user/updateUserInfo/0";
        public static final String CHECK_PHONE_URL = URL + "/student/user/checkUserIsExistsByPhone/0";
        public static final String CHECK_USERNAME_URL = URL + "/student/user/checkUserIsExistsByUserName/0";
        public static final String HOME_WORK_LIST_URL = URL + "/student/homework/searchList/0";
        public static final String HOME_WORK_DETAIL_URL = URL + "/student/homework/details/0";
        public static final String LOAD_IMAGE_HEAD_URL = "http://ssj-problem-test.oss-cn-shenzhen.aliyuncs.com/";
        public static final String GET_USER_INFO_URL = URL + "/student/user/searchMyUserInfo/0";
        public static final String GET_OSSFS_TOKEN = URL + "/student/user/getOssfsToken/0";
        public static final String GET_CLASSMATES_LIST = URL + "/student/schoolmateCircle/searchList/0";
        public static final String COMMIT_CLASSMATES_COMMENTS = URL + "/student/comment/commentCircle/0";
        public static final String CLASSMATES_COMMENTS_LIST = URL + "/student/comment/searchList/0";
        public static final String CLASSMATES_COMMENTS_DIANZAN = URL + "/student/like/likeCircle/0";
        public static final String SCHOOL_MATE_SHARE_URL = URL + "/student/schoolmateCircle/share/0";
        public static final String SCHOOL_PROBLEM_LIS_URL = URL + "/student/problem/searchList/0";
        public static final String SCHOOL_PROBLEM_DETAIL_URL = URL + "/student/problem/details/0";
        public static final String SCHOOL_SHIGUANG_LIST_URL = URL + "/student/smarttime/searchList/0";
        public static final String SCHOOL_SHIGUANG_DELETE_URL = URL + "/student/smarttime/delete/0";
        public static final String USER_LOGON ="student/user/authlogin/0";
    }

    public class NetWorkKeyContans {
        public static final String IMAGE_KEY = "imageKey";// "string",
        public static final String NICK_NAME = "nickname";// "string",
        public static final String PHONE = "phone";//"string",
        public static final String PW1 = "pwd1";//"string",
        public static final String PW2 = "pwd2";// "string",
        public static final String UASER_NAME = "userName";// "string",
        public static final String USER_TYPE = "userType";//"string",
        public static final String UPLOAD_TYPE = "uploadType";//"string",
        public static final String VERIFY_CODE = "verifyCode";// "string"
        public static final String PASSWORD = "password";  //" "string",
        public static final String DATALIST = "dataList";
        public static final String HOMEWORK_ID = "homeworkId";
        public static final String PARENT_ID = "parentId";
        public static final String FILE_KEY = "fileKey";
        public static final String CREATE_TIME = "createTime";
        public static final String IS_FEED_BACK = "isFeedback";
        public static final String FEED_BACK_NUMBER = "feedbackNum";
        public static final String COMMENT_NUM = "commentNum";
        public static final String SHATE_CONTENT = "shareContent";
        public static final String COMMENT = "comment";
        public static final String LIKE_NUM = "likeNum";
        public static final String IS_SHARE = "isShare";
        public static final String WEEK_DAY = "weekday";
        public static final String USER_ID = "userId";
        public static final String TEACHER_BACK = "teacherFeedback";
        public static final String SCHOOL_PARENT_ID = "ischoolParentId";
        public static final String TOKEN_TYPE = "@type";
        public static final String TOKEN_ACCESS_ID = "accessKeyId";
        public static final String TOKEN_DIR = "dir";
        public static final String TOKEN_SECRET_ID = "secretKeyId";
        public static final String TOKEN_SECRET_ENTITY = "securityToken";
        public static final String NEW_PASSWORD = "newPwd1";
        public static final String NEW_PASSWORD_AGAIN = "newPwd2";
        public static final String OLD_PASSWORD = "oldPwd";
        public static final String USER_IMAGE_KEY = "user_image_key";
        public static final String COMMENT_ID = "commentId";
        public static final String USER_HEAD_IMG = "user_head_img";

    }

    public class CommentContan {
        public static final String COMMENT_ID = "commentId";
        public static final String CREATTIME = "createTime";
        public static final String CONTENT = "content";
        public static final String COMMENT_USER_ID = "commentUserId";
        public static final String COMMENT_NICK_NAME = "commentNickname";
        public static final String COMMENT_IMAGEKEY = "commentImgKey";
    }

    public class ResponseContans {

        public static final String TIME_STAMP = "timestamp";// 1519903253284,
        public static final String STATUS = "status";// 400,
        public static final String ERROR = "error";//"Bad Request",
        public static final String EXCEPTION = "exception";// "org.springframework.http.converter.HttpMessageNotReadableException",
        public static final String MESSAGE = "message";// "Could not read document: Unexpected character ('(' (code 40)): expected a valid value (number, String, array, object, 'true', 'false' or 'null')\n at [Source: java.io.PushbackInputStream@4eac9d7; line: 1, column: 2]; nested exception is com.fasterxml.jackson.core.JsonParseException: Unexpected character ('(' (code 40)): expected a valid value (number, String, array, object, 'true', 'false' or 'null')\n at [Source: java.io.PushbackInputStream@4eac9d7; line: 1, column: 2]",
        public static final String PATH = "path";//"/student/user/registerUser/0"

    }

    public class UserType {
        public static final String STUDENT = "STUDENT";
        public static final String PARENT = "PARENT";
        public static final String TEACHER = "";
        public static final String SCHOOLMASTER = "";
    }

    public class BizType {
        public static final String BIZID = "bizId";
        public static final String BIZTYPE = "bizType";
        public static final String TYPE_HOMEWORK = "HOMEWORK";
        public static final String TYPE_PROBLEM = "PROBLEM";
        public static final String TYPE_CONTENT = "content";
        //public static final String SCHOOLMASTER = "";
    }

    public class PhotoType {
        public static final int LITTLE_TIME = 0;
    }

    public class MainType {
        public static final int HMWORK = 0;
        public static final int DIFFICULTY = 1;
        public static final int FRIENDS = 2;
        public static final int MYSELF = 3;
    }

    public class JsobContants {
        public static final String MESSAGE = "message";//"string",
        public static final String STATUS = "status";//0
        public static final String TOKEN = "token";
        public static final String JSONOBJ = "jsonObj";//{}
        public static final String SUCCESS = "success";//true
        public static final String TIMESTAMP = "timestamp";//1520322686510,
        public static final String ERROR = "error";//Bad Request"
        public static final String EXCEPTION = "exception";//: "org.springframework.http.converter.HttpMessageNotReadableException",
        public static final String PATH = "path";// "/student/homework/delete/0"
    }

    public class EventBusNumber {
        public static final int HOME_WORK_DATA_OK = 10000;
    }

    public class TransmitKey {
        public static final String INTENT_USGER_INFO = "intent_user_info";
        public static final String INTENT_HOME_WOEK_INFO = "intent_home_work_info";
    }

    public class CommenValues {
        public static final int SUCCESS = 0;
        public static final int ERROR = 1;
        public static final int TYPE_PROBLEM_DETAIL = 2;
        public static final int TYPE_HOMEWORK_DETAIL = 3;
        public static final int TYPE_COMMENTS_DETAIL = 4;
    }

    public class ClassMates {
        public static final String CIRCLE_ID = "circleId";
        public static final String CONTENT = "content";
        public static final String PAGE_NO = "pageNo";
        public static final String PAGE_SIZE = "pageSize";
        public static final String CLASSMATE_BEAN = "classmate_bean";
    }
}

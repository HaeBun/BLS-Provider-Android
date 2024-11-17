package com.tetraplace.snedu.sign_in;

public class SignInModel{

    /**
     *  SNS Auth Activity의 결과를 구분하는 SNS TYPE
     */
    public static final int SNS_TYPE_KAKAO = 1000;
    public static final int SNS_TYPE_GOOGLE = 1001;

    /**
     *  SNS Auth 서버로부터 제공받는 정보입니다.
     *  실제 이름 및 UID는 서버에서 조회하여 관리합니다.
     */
    public String snsIDToken;
    public String snsType;

    /**
     *  카카오 Auth 에서 발급받은 키를 입력합니다.
     */
    public static final String APP_KEY_KAKAO = "";

    /**
     *  SNS Auth로부터 발급받은 IDToken을 선언합니다.
     */
    public SignInModel(String snsType, String snsIDToken) {
        this.snsType = snsType;
        this.snsIDToken = snsIDToken;
    }

    public String getSnsIDToken() {
        return snsIDToken;
    }

    public String getSnsType() {
        return snsType;
    }
}
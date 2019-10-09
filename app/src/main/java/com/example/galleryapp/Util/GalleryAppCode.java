package com.example.galleryapp.Util;

import android.os.Environment;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class GalleryAppCode {
    public static final String Position = "Position";
    public static final String GalleryList = "GalleryList";
    public static final String Path =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest/";
    public static final String GoToFilterPath = "GoToFilterPath";
    public static final String GoLeft = "LEFT";
    public static final String GoRight = "RIGHT";
    // Camera 관련 참고 : https://yeolco.tistory.com/45?category=757621
    // UI THREAD 해결 참고 - https://stackoverflow.com/questions/14678593/the-application-may-be-doing-too-much-work-on-its-main-thread
    // ImageView - URI VS Bitmap - https://stackoverflow.com/questions/41430796/difference-between-uri-and-bitmap-image







    //카카오톡 연동 하는 방법
    // 1. KAKAO DEVELoper 등록 - 내 애플리케이션(앱 만들기)
    // 2. 패키지 명 입력, 마켓 URL 작성, 네이티브앱키 복사
    // 3. build.gradle(Project)
    //  subprojects {
    //      repositories {
    //      mavenCentral()
    //      maven { url 'http://devrepo.kakao.com:8088/nexus/content/groups/public/' }
    //      }
    //  }
    // 4. gradle.properties - KAKAO_SDK_VERSION = 1.1.7(사용할 카톡 버젼 명시)
    // 5. build.gradle - dependedcies
    //    dependencies {
    //
    //        compile group: 'com.kakao.sdk', name: 'kakaolink', version: project.KAKAO_SDK_VERSION
    //
    //    }
    // 6. kakao_string.xml만들기 (in values아래)
//    <?xml version="1.0" encoding="utf-8"?>
//<resources>
//    <string name="kakao_app_key">"발급 받은 네이티브앱키</string>
//    <string name="kakao_scheme">kakao발급 받은 네이티브앱키</string>
//    <string name="kakaolink_host">kakaolink</string>
//
//</resources>

    // 7. manifest에 작성
    // <intent-filter>
    //<action android:name ="android.intent.action.VIEW"
    // <category android:nmae ="android.intent.category.DEFAULT">
    //<category android:name="android.intent.category.BROWSABLE>
    //<data android:scheme="@string/kakao_scheme"
    // android:host="@string/kakaolink_host"/>

    //<meta-data
    //  android:nmae="com.kakao.sdk.AppKey"
    // android:value="@string/kakao_app_key"

    //8.activity에 작성
//    KakaoLink kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
//    KakaoTalkLinkMessageBuilder messageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
//messageBuilder.addText("카카오톡으로 공유해요."); -- 메세지 전달
//kakaoLink.sendMessage(messageBuilder,getApplicationContext());



}

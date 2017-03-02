package choongyul.android.com.soundplayer;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import choongyul.android.com.soundplayer.domain.Common;

/**
 * Created by myPC on 2017-02-28.
 */

public class Server {

    List<Observer> observers = new ArrayList<>();
    // 데이터 저장소
    private int position = -1;
    List<?> datas;
    private String typeFlag = "";
//    Uri musicUri;

    // 옵저버 저장소에 옵저버를 저장하는 역할
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // 변경사항이 발생했을 때 옵저버들에게 통지하는 역할
    public void notification() {
        for(Observer observer : observers) {
            // 인터페이스의 함수를 호출하면 그 인터페이스 함수를 구현한 모든 객체에 연락이 가나???????????
            observer.update();
        }
    }

    // student가 사용하는 메시지 전달함수
    public void sendMessage(int position, List<?> datas, String flag) {
        // 데이터 저장소에 데이터를 반영해준다.
        this.position = position;
        this.datas = datas;
        this.typeFlag = flag;
//        this.musicUri = musicUri;

        // 전달해야 하는 메시지가 발생 하면 notification 으로 모든 옵저버에게 알린다
        notification();
    }

    public int getPosition() {
        return position;
    }

    public String getTypeFlag() {
        return typeFlag;
    }
}


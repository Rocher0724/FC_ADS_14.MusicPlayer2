package choongyul.android.com.soundplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import choongyul.android.com.soundplayer.domain.Artist;
import choongyul.android.com.soundplayer.domain.Music;

import java.util.List;

import static choongyul.android.com.soundplayer.App.ARG_LIST_TYPE;

public class ListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    public static final String TYPE_SONG = "SONG";
    public static final String TYPE_ARTIST = "ARTIST";
    public static final String TYPE_ALBUM = "ALBUM";
    public static final String TYPE_GENRE = "GENRE";
    public static final String TYPE_RECENT = "RECENT";
    private int mColumnCount = 1;
    View view;


    private List<?> datas; // 제네릭을 사용해서 어떤 타입이 들어가도 괜찮게 구성하였다.
    public String mListType = "";

    public ListFragment() {
    }

    public static ListFragment newInstance(int columnCount, String flag) {
        Bundle args = new Bundle();
        ListFragment fragment = new ListFragment();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_LIST_TYPE, flag);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mListType = getArguments().getString(ARG_LIST_TYPE);

            // 들어오는 타입에 따른 분기
            if(TYPE_SONG.equals(mListType))
                datas = DataLoader.getMusicDatas(getContext());
            else if(TYPE_ALBUM.equals(mListType))
                datas = DataLoader.getArtistDatas(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if( view != null) {
            return view;
        }

        view = inflater.inflate(R.layout.list_fragment, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new ListAdapter(datas, mListType, context));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

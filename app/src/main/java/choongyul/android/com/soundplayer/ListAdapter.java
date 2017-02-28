package choongyul.android.com.soundplayer;

import android.content.Context;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

import choongyul.android.com.soundplayer.domain.Common;
import choongyul.android.com.soundplayer.domain.Music;
import choongyul.android.com.soundplayer.util.fragment.PagerAdapter;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<?> datas;
    private String flag;
    private int item_layout_id;
    Context context;
    MainActivity maina;
    Server server = new Server();


    public ListAdapter(List<?> datas, String flag, Context context) {
        this.context = context;
        this.datas = datas;
        maina = (MainActivity) context;

        this.flag = flag;
        switch (flag) {
            case ListFragment.TYPE_ALBUM:
                item_layout_id = R.layout.list_fragment_item_album;
                break;
            case ListFragment.TYPE_ARTIST:

            case ListFragment.TYPE_GENRE:

            case ListFragment.TYPE_SONG:
                item_layout_id = R.layout.list_fragment_item;
                break;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(item_layout_id, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Common common = (Common) datas.get(position);
        holder.position = position;
        Glide.with(context)
                .load(common.getImageUri())
                // 이미지가 없을 경우 대체 이미지
                .placeholder(R.mipmap.icon_play96)
                .into(holder.imgView);

        holder.tvThick.setText(common.getThickTV());
        holder.tvThin.setText(common.getThinTV());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        int position;



//        new Student(server, "박유석");

        // 컨스트레인트
        ConstraintLayout box;
        ImageView imgView;
        TextView tvThick;
        TextView tvThin;

        // 플레이어
        ImageView imgAlbum_player;
        TextView tvThick_player;
        TextView tvThin_player;



        public ViewHolder(View view) {
            super(view);

            box = (ConstraintLayout) view.findViewById(R.id.list_item);

            switch (flag) {
                case ListFragment.TYPE_SONG:
                    Log.e("ListAdapter", "viewHolder - song 내부 ");
                    tvThick = (TextView) view.findViewById(R.id.tvThick_list);
                    tvThin = (TextView) view.findViewById(R.id.tvThin_list);
                    imgView = (ImageView) view.findViewById(R.id.imgAlbum_list);
                    break;
                case ListFragment.TYPE_ALBUM:
                    Log.e("ListAdapter", "viewHolder - album 내부 ");

                    tvThick = (TextView) view.findViewById(R.id.tvAlbumId_album);
                    tvThin = (TextView) view.findViewById(R.id.tvNumOfTracks_album);
                    imgView = (ImageView) view.findViewById(R.id.imgAlbum_album);
                    break;
            }

            box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    server.sendMessage(position, datas);
                }
            });
        }



    }
}


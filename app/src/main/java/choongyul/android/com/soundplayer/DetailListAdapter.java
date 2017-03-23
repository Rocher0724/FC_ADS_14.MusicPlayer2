package choongyul.android.com.soundplayer;

import android.content.Context;
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

import choongyul.android.com.soundplayer.domain.Common;

import static choongyul.android.com.soundplayer.R.layout.list_fragment_item;

/**
 * Created by myPC on 2017-03-10.
 */

class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.ViewHolder> {

    private List<?> datas;
    Context context;
    Server server = Server.getInstance();
    String flag = "";

    public DetailListAdapter(List<?> datas, Context context, String flag) {
        this.datas = datas;
        this.context = context;
        this.flag = flag;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(list_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Common common = (Common) datas.get(position);
        holder.position = position;
        Glide.with(context)
                .load(common.getImageUri())
                .placeholder(R.mipmap.icon_play96)
                .into(holder.imgView);

        holder.tvThick.setText(common.getTitle());
        holder.tvThin.setText(common.getArtist());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        int position;

        ConstraintLayout box;
        ImageView imgView;
        TextView tvThick;
        TextView tvThin;

        public ViewHolder(View view) {
            super(view);

            box = (ConstraintLayout) view.findViewById(R.id.list_item);
            imgView = (ImageView) view.findViewById(R.id.imgAlbum_list);
            tvThick = (TextView) view.findViewById(R.id.tvThick_list);
            tvThin = (TextView) view.findViewById(R.id.tvThin_list);

            box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ListAdapter", "box를 클릭했다. ");

                    server.sendMessage(position, datas, flag); // 플래그를 서버에 옮길 필요까지는 없는것 같다.
                }
            });

        }
    }

}

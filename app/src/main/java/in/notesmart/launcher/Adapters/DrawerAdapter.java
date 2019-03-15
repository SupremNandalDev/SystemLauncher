package in.notesmart.launcher.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.notesmart.launcher.Model.AppsData;
import in.notesmart.launcher.R;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.PremViewHolder> {

    Context context;
    List<AppsData> data;
    ItemClickListener mClickListener;
    ItemLongClickListner mLongLinster;

    public DrawerAdapter(Context context, List<AppsData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public PremViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_drawer, viewGroup, false);
        return new PremViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PremViewHolder premViewHolder, int i) {
        premViewHolder.txt.setText(data.get(i).appTitle);
        premViewHolder.img.setImageDrawable(data.get(i).appIcon);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setLongClickListener(ItemLongClickListner listener) {
        this.mLongLinster = listener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface ItemLongClickListner {
        void onItemLongClick(View view, int position);
    }

    class PremViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView txt;
        ImageView img;

        public PremViewHolder(View premView) {
            super(premView);
            img = premView.findViewById(R.id.app_icon_drawer);
            txt = premView.findViewById(R.id.app_title_drawer);
            premView.setOnLongClickListener(this);
            premView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongLinster != null) {
                mLongLinster.onItemLongClick(v, getAdapterPosition());
            }
            return false;
        }
    }

}

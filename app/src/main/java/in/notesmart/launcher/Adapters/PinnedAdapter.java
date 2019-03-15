package in.notesmart.launcher.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import in.notesmart.launcher.Model.AppsDataPinned;
import in.notesmart.launcher.R;

public class PinnedAdapter extends RecyclerView.Adapter<PinnedAdapter.PremViewHolder> {

    Context context;
    List<AppsDataPinned> list;
    ItemClickListener mClickListener;

    public PinnedAdapter(Context context, List<AppsDataPinned> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public PremViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_pinned, viewGroup, false);
        return new PremViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PremViewHolder premViewHolder, int i) {
        String title = list.get(i).getAppTitle().toLowerCase();
        if (title.contains("phone")) {
            premViewHolder.img.setImageResource(R.drawable.ic_phone);
        } else if (title.contains("message") || title.contains("messages") || title.contains("messaging")) {
            premViewHolder.img.setImageResource(R.drawable.ic_messages);
        } else if (title.contains("chrome")) {
            premViewHolder.img.setImageResource(R.drawable.ic_chrome);
        } else if (title.contains("whatsapp")) {
            premViewHolder.img.setImageResource(R.drawable.ic_whatsapp);
        } else if (title.contains("youtube")) {
            premViewHolder.img.setImageResource(R.drawable.ic_youtube);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    class PremViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img;

        public PremViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.app_icon_pinned);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}

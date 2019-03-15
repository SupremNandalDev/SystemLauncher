package `in`.notesmart.launcher.Adapters

import `in`.notesmart.launcher.R
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class PinnedAdapter(internal var context: Context, internal var list: List<AppsDataPinned>) : RecyclerView.Adapter<PinnedAdapter.PremViewHolder>() {
    internal var mClickListener: ItemClickListener? = null

    fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PremViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_pinned, viewGroup, false)
        return PremViewHolder(v)
    }

    override fun onBindViewHolder(premViewHolder: PremViewHolder, i: Int) {
        val title = list[i].appTitle.toLowerCase()
        if (title.contains("phone")) {
            premViewHolder.img.setImageResource(R.drawable.ic_phone)
        } else if (title.contains("message") || title.contains("messages") || title.contains("messaging")) {
            premViewHolder.img.setImageResource(R.drawable.ic_messages)
        } else if (title.contains("chrome")) {
            premViewHolder.img.setImageResource(R.drawable.ic_chrome)
        } else if (title.contains("whatsapp")) {
            premViewHolder.img.setImageResource(R.drawable.ic_whatsapp)
        } else if (title.contains("youtube")) {
            premViewHolder.img.setImageResource(R.drawable.ic_youtube)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    inner class PremViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var img: ImageView = itemView.findViewById(R.id.app_icon_pinned)

        init {
            itemView.setOnClickListener(this)
        }


        override fun onClick(v: View) {
            if (mClickListener != null) {
                mClickListener!!.onItemClick(v, adapterPosition)
            }
        }
    }
}

package `in`.notesmart.launcher.Adapters

import `in`.notesmart.launcher.Model.AppsData
import `in`.notesmart.launcher.R
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class DrawerAdapter(internal var context: Context, internal var data: List<AppsData>) : RecyclerView.Adapter<DrawerAdapter.PremViewHolder>() {
    internal var mClickListener: ItemClickListener? = null
    internal var mLongLinster: ItemLongClickListner? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PremViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_drawer, viewGroup, false)
        return PremViewHolder(v)
    }

    override fun onBindViewHolder(premViewHolder: PremViewHolder, i: Int) {
        premViewHolder.txt.text = data[i].appTitle
        premViewHolder.img.setImageDrawable(data[i].appIcon)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    fun setLongClickListener(listener: ItemLongClickListner) {
        this.mLongLinster = listener
    }

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    interface ItemLongClickListner {
        fun onItemLongClick(view: View, position: Int)
    }

    inner class PremViewHolder(premView: View) : RecyclerView.ViewHolder(premView), View.OnClickListener, View.OnLongClickListener {
        var txt: TextView = premView.findViewById(R.id.app_title_drawer)
        var img: ImageView = premView.findViewById(R.id.app_icon_drawer)

        init {
            premView.setOnLongClickListener(this)
            premView.setOnClickListener(this)
        }


        override fun onClick(v: View) {
            if (mClickListener != null) {
                mClickListener!!.onItemClick(v, adapterPosition)
            }
        }

        override fun onLongClick(v: View): Boolean {
            if (mLongLinster != null) {
                mLongLinster!!.onItemLongClick(v, adapterPosition)
            }
            return false
        }
    }

}

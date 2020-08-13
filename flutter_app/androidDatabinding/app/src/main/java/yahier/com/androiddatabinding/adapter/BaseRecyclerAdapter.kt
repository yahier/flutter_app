package yahier.com.androiddatabinding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class BaseRecyclerAdapter<Bean, Binding : ViewDataBinding>
constructor(
    private val layoutRes: Int,
    private val onCellClick: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract val baseList: MutableList<Bean>

    class BaseSimpleViewHolder<Binding : ViewDataBinding>(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val binding: Binding? by lazy {
            DataBindingUtil.bind<Binding>(itemView)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return BaseSimpleViewHolder<Binding>(
            LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        )
    }

    override fun getItemCount() = baseList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as BaseSimpleViewHolder<Binding>
        holder.binding!!.root.setOnClickListener {
            onCellClick(position)
        }
        bindData(holder.binding!!, position)
    }


    abstract fun bindData(binding: Binding, position: Int)

    fun replaceData(newList: MutableList<Bean>) {
        baseList.run {
            clear()
            addAll(newList)
        }
        notifyDataSetChanged()
    }

    fun addData(data: Bean) {
        baseList.add(data)
        notifyItemInserted(baseList.size - 1)
    }

    fun removeData(position: Int) {
        baseList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, baseList.size)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        //交换位置
        if (toPosition >= 0 && toPosition < baseList.size) {
            Collections.swap(baseList, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
        }
    }

}
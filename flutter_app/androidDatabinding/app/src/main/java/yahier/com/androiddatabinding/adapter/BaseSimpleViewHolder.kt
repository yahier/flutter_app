package yahier.com.androiddatabinding.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseSimpleViewHolder<Binding : ViewDataBinding>(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    val binding: Binding? by lazy {
        DataBindingUtil.bind<Binding>(itemView)
    }

}
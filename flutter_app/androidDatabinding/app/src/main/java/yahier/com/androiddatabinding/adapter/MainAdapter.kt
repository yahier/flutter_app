package yahier.com.androiddatabinding.adapter

import yahier.com.androiddatabinding.R
import yahier.com.androiddatabinding.bean.Name

/**
 * 一个adapter的示例，够简洁吧
 */
class MainAdapter(
    val list: MutableList<Name>,
    onCellClick: (Int) -> Unit
) : BaseRecyclerAdapter<Name, yahier.com.androiddatabinding.databinding.MainCellBinding>(
    R.layout.item_main_list,
    onCellClick
) {
    override val baseList: MutableList<Name> = list
    override fun bindData(binding: yahier.com.androiddatabinding.databinding.MainCellBinding, position: Int) {
        binding.person = list[position]
    }
}
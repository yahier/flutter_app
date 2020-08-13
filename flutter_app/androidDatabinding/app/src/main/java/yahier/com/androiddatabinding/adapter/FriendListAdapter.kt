package yahier.com.androiddatabinding.adapter

import yahier.com.androiddatabinding.R
import yahier.com.androiddatabinding.bean.Person

class FriendListAdapter(
    val list: MutableList<Person>,
    onCellClick: (Int) -> Unit
) : BaseRecyclerAdapter<Person, yahier.com.androiddatabinding.databinding.FriendListCellBinding>(
    R.layout.item_friends_list,
    onCellClick
) {
    override val baseList: MutableList<Person> = list
    override fun bindData(binding: yahier.com.androiddatabinding.databinding.FriendListCellBinding, position: Int) {
        binding.person = list[position]
    }
}
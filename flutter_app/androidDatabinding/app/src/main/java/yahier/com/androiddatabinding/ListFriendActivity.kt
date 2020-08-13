package yahier.com.androiddatabinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import yahier.com.androiddatabinding.adapter.FriendListAdapter
import yahier.com.androiddatabinding.bean.Person

class ListFriendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_list_friend)
        showData()
    }

    fun showData() {
        findViewById<RecyclerView>(R.id.recyclerFriend).run {
            val friendList = mutableListOf(
                Person(
                    "aaaaa",
                    "https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
                    "测试A"
                ),
                Person(
                    "aaaaa",
                    "https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
                    "测试b"
                )
            )
            layoutManager = LinearLayoutManager(context)
            adapter = FriendListAdapter(friendList) { position ->
                //jumpToChat(context, friendList[position].uid)
                //Toast.makeText(ListFriendActivity@ this, "", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

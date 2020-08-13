package yahier.com.androiddatabinding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import yahier.com.androiddatabinding.adapter.MainAdapter
import yahier.com.androiddatabinding.bean.Name

class MainActivity : AppCompatActivity() {
    private val listData = mutableListOf<Name>()
    lateinit var mAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showData()
    }


    private fun showData() {
        listData.add(Name("基础数据绑定"))
        listData.add(Name("列表数据绑定"))

        mAdapter = MainAdapter(listData) { position ->
            when (position) {
                0 -> {
                    startActivity(Intent(MainActivity@ this, BasicDataBindAct::class.java))
                }

                1 -> {
                    startActivity(Intent(MainActivity@ this, ListFriendActivity::class.java))
                }

            }


        }

        mainRecycler.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }


    }
}

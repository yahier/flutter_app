package yahier.com.androiddatabinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

/**
 * 基础数据的绑定
 */
class BasicDataBindAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.act_basic_data_bind)

        val binding = DataBindingUtil.setContentView<yahier.com.androiddatabinding.databinding.ActBasicDataBindBinding>(
            BasicDataBindAct@ this,
            R.layout.act_basic_data_bind
        )


        binding.mainTv2.setOnClickListener {
            binding.content = "绝世好剑 ，在act中赋值"
        }

    }
}
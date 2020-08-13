package yahier.com.androiddatabinding.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class CircleImageView : ImageView {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
    }

    @BindingAdapter("imageUrl")
    fun loadImage(imageView:ImageView, url:String?){
        Glide.with(imageView.context).load(url).into(imageView)
    }
}

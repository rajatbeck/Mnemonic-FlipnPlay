package com.mnemonic.play

import android.content.Context
import android.view.View
import android.os.Build
import android.support.annotation.Nullable
import android.support.annotation.RequiresApi
import android.util.AttributeSet


class BoxView : View {


    constructor(context: Context?) : super(context)


    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs)


    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    


}
package com.example.constraintlayouttest

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val buttonList = mutableListOf<Button>()

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createButtons()

        Handler().postDelayed({
            repeatList()
        }, 2000)

    }

    private fun repeatList() {
        Thread {
            var buttonListIndex = -1
            val list = getAllColors()
            list.forEachIndexed { index: Int, color: Int ->
                Thread.sleep(100)
                if (buttonListIndex == buttonList.size - 1) {
                    buttonListIndex = -1
                }
                buttonListIndex++
                runOnUiThread {
                    val dr = ContextCompat.getDrawable(this, R.drawable.dr_round_bg)
                    dr?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
                    buttonList[buttonListIndex].background = dr
                }

                if (index == list.size - 1) {
                    repeatList()
                }
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun createButtons() {
        val length = 7
        val angle = 360 / (length + 1)
        for (i in 0..length) {
            val button = Button(this)
            button.id = View.generateViewId()
            button.background = ContextCompat.getDrawable(this, R.drawable.dr_round_bg)
            val layout = ConstraintLayout.LayoutParams(
                50.toPx(),
                50.toPx()
            )
            layout.circleRadius = 120.toPx()
            layout.circleConstraint = R.id.main_button
            layout.circleAngle = (i * angle).toFloat()
            layout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            layout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            layout.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            layout.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            button.layoutParams = layout
            buttonList.add(button)
            main_layout.addView(button)
        }
    }

    private fun Int.toPx(): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(), resources.displayMetrics
        ).toInt()

    private fun getAllColors(): List<Int> {
        val field = Class.forName("$packageName.R\$color").declaredFields
        val list = mutableListOf<Int>()
        field.forEach {
            list.add(ContextCompat.getColor(this, it.getInt(null)))
        }
        return list
    }
}
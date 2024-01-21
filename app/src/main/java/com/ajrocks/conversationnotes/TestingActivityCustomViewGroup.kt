package com.ajrocks.conversationnotes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.max


enum class TestingSlots {
    LEFT,
    TITLE,
    RIGHT
}

interface TestingConstituent {
    val slotType: TestingSlots
}

class TestingTextView: AppCompatTextView, TestingConstituent {

    override val slotType: TestingSlots
        get() = TestingSlots.TITLE

    constructor(context: Context): super(context){
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs,
        defStyle) {
    }
}

class TestingArtWork: AppCompatImageView {
    constructor(context: Context): super(context){
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs,
        defStyle) {
    }
}

class TestingLeftBackBtn: AppCompatImageView, TestingConstituent {
    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs,
        defStyle)

    override val slotType: TestingSlots
        get() = TestingSlots.LEFT
}

class TestingMainTitle: AppCompatTextView, TestingConstituent {
    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs,
        defStyle)

    override val slotType: TestingSlots
        get() = TestingSlots.TITLE
}

class TestingAction: AppCompatImageView, TestingConstituent {
    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs,
        defStyle)

    override val slotType: TestingSlots
        get() = TestingSlots.RIGHT
}

class TestingViewViewGroup: ViewGroup {
    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        this.addViewInLayout(
            TestingAction(context).apply {
                                         background = context.getDrawable(R.drawable.baseline_local_airport_24)
            },
            0,
            LayoutParams(
                70,
                70
            )
        )
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle)


    companion object {
        val PADDING_VERTICAL = 16
        val PADDING_HORIZONTAL = 16
        val GAP = 10
    }

    override fun onViewAdded(child: View?) {

        super.onViewAdded(child)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var maxWidth = PADDING_HORIZONTAL
        var maxHeight = PADDING_VERTICAL

        var actionWidth = 0
        var actionHeight = 0
        var totalActions = 0

        var avatarImageHeight = 100
        var avatarImageWidth = 0



        if(childCount > 0){
            for (i in 0 until childCount){
                val child = getChildAt(i)
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
                if(child is TestingLeftBackBtn || child is TestingAction){
                    actionWidth = child.measuredWidth
                    actionHeight = max(child.measuredHeight, actionHeight)
                    totalActions++
                }
                if(child is TestingArtWork){
                    avatarImageHeight = max(child.measuredHeight, avatarImageHeight)
                    avatarImageWidth = max(avatarImageWidth, child.measuredWidth)
                }
               maxWidth += child.measuredWidth
               maxHeight += child.measuredHeight
            }
        }

        maxWidth += PADDING_HORIZONTAL
        maxHeight += PADDING_VERTICAL

        setMeasuredDimension(widthMeasureSpec, actionHeight + GAP + avatarImageHeight + 2 * PADDING_VERTICAL)

        if(childCount > 0) {
            for(i in 0 until childCount){
                val child = getChildAt(i)


                if(child is TestingTextView){
                    child.measure(
                        MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) -
                                2 * PADDING_HORIZONTAL -
                                totalActions * (actionWidth + GAP),
                            MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(actionHeight, MeasureSpec.EXACTLY),
                    )
                }

                if(child is TestingMainTitle){
                    child.measure(
                        MeasureSpec.makeMeasureSpec(
                            MeasureSpec.getSize(widthMeasureSpec) -
                            2 * PADDING_HORIZONTAL -
                            avatarImageWidth,
                            MeasureSpec.EXACTLY
                        ),
                        MeasureSpec.makeMeasureSpec(
                            avatarImageHeight,
                            MeasureSpec.EXACTLY
                        )
                    )
                }

                else {
                    child.measure(
                        MeasureSpec.makeMeasureSpec(child.measuredWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(child.measuredHeight, MeasureSpec.EXACTLY),
                    )
                }

            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {


        var totalForwardTraversedRow1 = PADDING_HORIZONTAL
        var totalForwardTraversedRow2 = PADDING_HORIZONTAL
        var offsetForActions = 0
        var heightOfFirstRow = 0


        if(childCount > 0){
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if(child is TestingAction || child is TestingLeftBackBtn || child is TestingTextView){
                    heightOfFirstRow = child.measuredHeight
                }
                if(child is TestingLeftBackBtn || child is TestingTextView){
                    offsetForActions += child.measuredWidth + GAP
                }
            }
        }

        Log.d("MeasuredHeight", heightOfFirstRow.toString())

        var totalActionsLaidOut = 0


        if(childCount > 0){
            for(i in 0 until childCount) {
                val child = getChildAt(i)
                if(child is TestingArtWork || child is TestingMainTitle){
                    child.layout(
                        totalForwardTraversedRow2, PADDING_VERTICAL + heightOfFirstRow,
                        totalForwardTraversedRow2 + child.measuredWidth,
                        child.measuredHeight + PADDING_VERTICAL + heightOfFirstRow
                    )
                    totalForwardTraversedRow2 += child.measuredWidth + GAP
                }

                else if(child is TestingAction){
                   child.layout(
                       totalActionsLaidOut * child
                           .measuredWidth + GAP + offsetForActions, PADDING_VERTICAL, totalActionsLaidOut * child
                           .measuredWidth + GAP + offsetForActions + child.measuredWidth,
                       child.measuredHeight + PADDING_VERTICAL
                   )
                    totalActionsLaidOut++
                }

                else{
                    child.layout(totalForwardTraversedRow1, PADDING_VERTICAL, totalForwardTraversedRow1 +
                            child.measuredWidth, child.measuredHeight + PADDING_VERTICAL)
                    totalForwardTraversedRow1 += child.measuredWidth + GAP
                }
            }
        }
    }

}


class TestingViewGroupWrapper: FrameLayout {

    private var children: MutableList<View> = mutableListOf()
    private lateinit var innerTestingViewGroup: TestingViewViewGroup

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        inflate(context, R.layout.testing_activity, this)
        innerTestingViewGroup = findViewById(R.id.testing_activity)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs,
        defStyle)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
       super.onLayout(changed, left, top, right, bottom)
    }

    override fun onViewAdded(child: View?) {
        if(child !is TestingViewViewGroup && child is View){
            children.add(child)
        }

        if(child !is TestingViewViewGroup){
            detachViewFromParent(child)
            innerTestingViewGroup.addView(child)
        }
    }


}

class TestingActivityCustomViewGroup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing_custom_view_group)
    }
}
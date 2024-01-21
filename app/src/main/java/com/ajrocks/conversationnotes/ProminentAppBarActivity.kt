package com.ajrocks.conversationnotes

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import io.ktor.websocket.Frame

class ProminentAppBarActivityBehaviour : CoordinatorLayout.Behavior<View> {

    private var dependentHeight: Int = 0
    private lateinit var dependencyView: LinearLayout
    private lateinit var dependencyTextTitle: TextView
    private lateinit var dependencyBackBtn: ImageView
    private lateinit var dependencyTextTitleCompressed: TextView
    private lateinit var prominentAppBarTitleLayout: LinearLayout
    private var isCoupled = true
    private val contractedHeight = 150


    constructor() : super() {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        Log.d("Dependency", "${dependency}, child- ${child}")
        if (dependency is LinearLayout) {
            dependencyView = dependency
            dependencyTextTitle = dependency.findViewById(R.id
                .prominent_app_bar_title)
            dependencyTextTitleCompressed = dependency.findViewById(R.id
                .prominent_app_bar_title_compressed)
            dependencyBackBtn = dependency.findViewById(R.id
                .prominent_app_bar_back_btn)
            prominentAppBarTitleLayout = dependency.findViewById(R.id
                .prominent_app_bar_title_layout)
        }
        return dependency is LinearLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        Log.d("DependentViewChanged", "${dependency.height} $isCoupled")
        if (isCoupled) {
            dependentHeight = dependency.height
            child.top = (dependency.y + dependency.height).toInt()
//            val maxYPosForChild = dependency.height
//            child.y = Math.min(dependency.y.toInt() + dependency.height, maxYPosForChild).toFloat()
        }

        val depRange = arrayOf(0, dependency.height - contractedHeight)
        //                                   h - 2padding - imgHeight
        val yTranslationRange = arrayOf(0,500)
        val titleLayoutTranslationRange = arrayOf(0, depRange[1] - 10)
        val opacityRange = arrayOf(0, 1)
        val translationAmt = -1 * (dependency.y / depRange[1]) * titleLayoutTranslationRange[1]

        val opacityForCompressedTitle = -1 * (dependency.y / depRange[1])

        Log.d("Opacity", "$translationAmt")

        dependencyTextTitle.translationY = -1 * (yTranslationRange[1] / depRange[1]) * dependency.y

        if(translationAmt <= titleLayoutTranslationRange[1]){
            prominentAppBarTitleLayout.translationY = translationAmt
        }
        else{
            prominentAppBarTitleLayout.translationY = depRange[1].toFloat()
        }
        if(opacityForCompressedTitle <= 1){
            dependencyTextTitleCompressed.alpha = opacityForCompressedTitle
            dependencyTextTitle.alpha = 1 - opacityForCompressedTitle
        }

        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return true
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        //output
        consumed: IntArray,
        type: Int
    ) {

        val isScrollingUp = dy >= 0
        val isScrollingDown = dy < 0

        val actionBarY = dependencyView.y
        val actionBarHeight = dependencyView.height
        val childY = child.y
        val childScrollPosition = child.scrollY

//        /** For case when hideOnScroll is disabled ***/
//
//        if(childY <= contractedHeight && isScrollingUp) {
//            isCoupled = false
//        }
//
//        else if (childY > 0 && childScrollPosition == 0) {
//            isCoupled = true
//            consumed[1] = dy
//            val newY = dependencyView.y - dy
//            if (newY > 0) {
//                dependencyView.y = 0f
//            } else if (newY <= contractedHeight - actionBarHeight) {
//                dependencyView.y = (contractedHeight - actionBarHeight).toFloat()
//            } else {
//                dependencyView.y = newY
//            }
//        }

        Log.d("ProminentAppBar", "$actionBarY $childY $childScrollPosition $isScrollingDown $isScrollingUp")

        /*For case when hideOnScroll is enabled*/

        if (childY >= contractedHeight.toFloat() && isScrollingDown && childScrollPosition == 0) {
            isCoupled = true
            consumed[1] = dy
            val newY = dependencyView.y - dy
            if (newY > 0) {
                dependencyView.y = 0f
            } else if (newY < -1 * actionBarHeight) {
                dependencyView.y = -1f * actionBarHeight
            } else {
                dependencyView.y = newY
            }
        } else if (childY == contractedHeight.toFloat() && isScrollingDown) {
            isCoupled = false
        } else if (childY <= contractedHeight && isScrollingDown) {
            isCoupled = true
            consumed[1] = dy
            val newY = dependencyView.y - dy
            if (newY > 0) {
                dependencyView.y = 0f
            } else if (newY >= (contractedHeight - actionBarHeight)) {
                dependencyView.y = (contractedHeight - actionBarHeight).toFloat()
            } else {
                dependencyView.y = newY
            }
        } else if (childY > contractedHeight && isScrollingDown) {
            isCoupled = false
        } else if (childY > 0f) {
            isCoupled = true
            consumed[1] = dy
            val newY = dependencyView.y - dy
            if (newY > 0) {
                dependencyView.y = 0f
            } else if (newY < -1 * actionBarHeight) {
                dependencyView.y = -1f * actionBarHeight
            } else {
                dependencyView.y = newY
            }
        } else if (childY <= 0f) {
            isCoupled = false
            child.y = 0f
        }


        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {


        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
    }
}

class ProminentAppBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.menuInflater
        setContentView(R.layout.activity_prominent_app_bar)
    }
}
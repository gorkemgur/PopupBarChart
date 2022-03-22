/*
 * Copyright 2022 Justin George
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.justin.popupbarchart

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat

data class GraphModel(val splitRect: SplitRect, val graphBarRect: GraphBarRect)

data class SplitRect(val left: Float) {
    var top: Float = 0f
    var right: Float = 0f
    var bottom: Float = 0f
}

data class GraphBarRect(val left: Float) {
    var top: Float = 0f
    var right: Float = 0f
    var bottom: Float = 0f
}

@SuppressLint("WrongConstant")
class CustomBarChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : View(context, attrs) {

    /*Tooltip variables*/
    private val topPoint = 10f
    private val padding = context.dpToPx( 7)
    private val paddingBetweenText = 4f
    private val completedText = "completed"

    private var percentageText = "00%"
    private var tooltipAnchorBottomPoint = 0f
    private val percentageBounds = Rect()
    private var tooltipHeadRectWidth = 0f
    private val pathTooltip2 = Path()

    private var tooltipLeftPoint = 10f
    private var tooltipTopPoint = 10f


    /*Graph Variables*/
    private val barWidth = context.dpToPx( 16)
    private val graphLeftAndRightPadding = context.dpToPx( 20)
    private val graphLeftPadding = context.dpToPx( 25)/2
    private val totalBarCount = 7

    private var split = 0
    private var startingPoint = 0f
    private var barHeight = 0f
    private var graphModelList = arrayListOf<GraphModel>()

    /* other values*/
    private val listGraphValues = arrayListOf<GraphValue>()

    val colors = intArrayOf(
        ContextCompat.getColor(context,
            R.color.bg_bar_graph_green_start),
        ContextCompat.getColor(context,
            R.color.bg_bar_graph_green_end),
    )


    private val mProgressAnimator = ValueAnimator()
    private val mAnimatorListener: Animator.AnimatorListener? = null
    private val mAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener? = null

    // Max and min fraction values
    private val maxFraction = 1.0f
    private val minFraction = 0.0f
    private val animateAllIndex = -2
    private val disableAnimateIndex = -1
    private val mAnimationDuration = 1000

    // Action move variables
    private var mActionMoveModelIndex: Int = disableAnimateIndex
    private var mAnimatedFraction = 0f

    val mProgressPaint: Paint = object : Paint(
        ANTI_ALIAS_FLAG) {
        init {
            isDither = true
            style = Style.STROKE
            strokeWidth = barWidth
            style = Style.STROKE
            strokeCap = Cap.ROUND
            strokeJoin = Join.ROUND
            this.color = ContextCompat.getColor(context, R.color.grey_a7)

        }
    }


    /*
    * Used to Paint Day indications eg: Day 1, Day 2, Day 3
    * */
    private val mDayTextPaint: TextPaint = object : TextPaint(ANTI_ALIAS_FLAG) {
        init {
            this.color = ContextCompat.getColor(context, R.color.grey_b2b2)
            this.textSize = context.spToPx(10)
        }
    }


    /*
     * Used to Paint Today indications eg: Day 1, Day 2, Day 3
     * */
    private val mTodayTextPaint: TextPaint = object : TextPaint(ANTI_ALIAS_FLAG) {
        init {
            this.color = ContextCompat.getColor(context, R.color.black_2121)
            this.textSize = context.spToPx(10)
        }
    }


    private val mProgressBGPaint: Paint = object : Paint(
        ANTI_ALIAS_FLAG) {
        init {
            isDither = true
            style = Style.STROKE
            strokeWidth = context.dpToPx( 16)
            style = Style.STROKE
            strokeCap = Cap.ROUND
            strokeJoin = Join.ROUND
            this.color = ContextCompat.getColor(context, R.color.white_f0)
        }
    }



    private val percentagePaint: TextPaint = object : TextPaint(ANTI_ALIAS_FLAG) {
        init {
            color = ContextCompat.getColor(context, R.color.white)
            this.textSize = context.spToPx(10)
        }
    }

    private val completedPaint: TextPaint = object : TextPaint(ANTI_ALIAS_FLAG) {
        init {
            color = ContextCompat.getColor(context, R.color.white)
            this.textSize = context.spToPx(8)
        }
    }



    private val tooltipBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 1.0f
        val radius = context.dpToPx( 4)
        val corEffect = CornerPathEffect(radius)
        pathEffect = corEffect
    }


    fun setGraphValues(graphValue: List<GraphValue>) {
        listGraphValues.clear()
        listGraphValues.addAll(graphValue)
        //postInvalidate()
    }

    init {
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, null)

        mProgressAnimator.setFloatValues(minFraction,maxFraction)
        mProgressAnimator.addUpdateListener { animation ->
            mAnimatedFraction = animation.animatedValue as Float
            mAnimatorUpdateListener?.onAnimationUpdate(animation)
            postInvalidate()
        }
    }


    fun animateProgress() {
        if (mProgressAnimator.isRunning) {
            if (mAnimatorListener != null) mProgressAnimator.removeListener(mAnimatorListener)
            mProgressAnimator.cancel()
        }

        mActionMoveModelIndex = animateAllIndex
        mProgressAnimator.duration = mAnimationDuration.toLong()
        if (mAnimatorListener != null) {
            mProgressAnimator.removeListener(mAnimatorListener)
            mProgressAnimator.addListener(mAnimatorListener)
        }
        mProgressAnimator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        calculateDefaultTooltipSize(percentageText = percentageText)
        val requiredWidth = width -  graphLeftAndRightPadding
        calculateGraphValues(requiredWidth.toInt(), height)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    for (i in 0 until listGraphValues.size) {
                        val graph = graphModelList[i]
                        val graphRectF = RectF(graph.graphBarRect.left - context.dpToPx( 10),
                            graph.graphBarRect.top - context.dpToPx( 10),
                            graph.graphBarRect.right + context.dpToPx( 10),
                            graph.graphBarRect.bottom + context.dpToPx( 10))
                        if (graphRectF.contains(it.x, it.y)) {
                            for (j in 0 until listGraphValues.size) {
                                val gv = listGraphValues[j]
                                if (j == i) {
                                    gv.showToolTip = true
                                    listGraphValues.removeAt(j)
                                    listGraphValues.add(j, gv)
                                } else {
                                    gv.showToolTip = false
                                    listGraphValues.removeAt(j)
                                    listGraphValues.add(j, gv)
                                }
                            }
                            postInvalidate()
                        }
                    }
                }
                else -> {
                }
            }
        }
        return super.onTouchEvent(event)
    }


    private fun calculateGraphValues(width: Int, height: Int) {
        split = width / totalBarCount
        startingPoint = tooltipAnchorBottomPoint + context.dpToPx( 10)
        barHeight = height.toFloat()/* - startingPoint*/
        val todayText = "TODAY"
        val todayRect = Rect()
        mTodayTextPaint.getTextBounds(todayText, 0, todayText.length, todayRect)
        mProgressPaint.shader = LinearGradient(0f,
            0f,
            0f,
            height.toFloat(),
            colors,
            null,
            Shader.TileMode.MIRROR)
        graphModelList.clear()
        for (i in 0..6) {
            val startX = (split * i + graphLeftPadding)
            val barX = (((startX + split) - startX) - barWidth) / 2
            val tempBarHeight = barHeight - todayRect.height() - context.dpToPx( 30)

            val myDelta = context.dpToPx( 7)
            val splitRect = SplitRect(startX)
            val graphBarRect = GraphBarRect((startX + barX + myDelta))
            graphBarRect.apply {
                top = startingPoint
                right = (startX + barX + myDelta)
                bottom = tempBarHeight
            }
            val graphModel = GraphModel(splitRect, graphBarRect)
            graphModelList.add(graphModel)
        }
    }

    private fun calculateDefaultTooltipSize(percentageText: String) {
        percentagePaint.getTextBounds(percentageText, 0, percentageText.length, percentageBounds)
        val completedBounds = Rect()
        completedPaint.getTextBounds(completedText, 0, completedText.length, completedBounds)
        val textWidth = completedBounds.width()
        val textHeight = completedBounds.height()
        tooltipHeadRectWidth = (padding * 2) + textWidth

        val tooltipHeadRectHeight =
            (textHeight + percentageBounds.height()) + paddingBetweenText + (padding * 2)
        val tooltipHeadRectHeightPoint = tooltipHeadRectHeight + topPoint
        val deltaTooltipHeight = tooltipHeadRectHeight / 3
        tooltipAnchorBottomPoint = tooltipHeadRectHeightPoint + deltaTooltipHeight
    }


    private fun calculateTooltipValuesV2(percentageText: String, xPoint: Float, yPoint: Float) {

        percentagePaint.getTextBounds(percentageText, 0, percentageText.length, percentageBounds)
        val completedBounds = Rect()
        completedPaint.getTextBounds(completedText, 0, completedText.length, completedBounds)

        val textWidth = completedBounds.width()
        val textHeight = completedBounds.height()

        tooltipHeadRectWidth = (padding * 2) + textWidth

        val rightPoint = xPoint - (tooltipHeadRectWidth / 2)
        var topPoint = yPoint - tooltipAnchorBottomPoint

        topPoint -= context.dpToPx( 7)

        tooltipLeftPoint = rightPoint
        tooltipTopPoint = topPoint

        val tooltipHeadRectHeight =
            (textHeight + percentageBounds.height()) + paddingBetweenText + (padding * 2)

        val tooltipHeadRectRightPoint = tooltipHeadRectWidth + rightPoint
        val tooltipHeadRectHeightPoint = tooltipHeadRectHeight + topPoint

        val deltaTooltipAnchorPoint = tooltipHeadRectWidth / 3
        val tooltipAnchorPointTopRight =
            tooltipHeadRectWidth + rightPoint - deltaTooltipAnchorPoint
        val tooltipAnchorPointBottom = tooltipHeadRectWidth / 2 + rightPoint
        val deltaTooltipHeight = tooltipHeadRectHeight / 3

        pathTooltip2.reset()
        pathTooltip2.moveTo(rightPoint, topPoint) //startingPoint
        pathTooltip2.lineTo(tooltipHeadRectRightPoint, topPoint) // right top corner
        pathTooltip2.lineTo(tooltipHeadRectRightPoint,
            tooltipHeadRectHeightPoint) //right Bottom Corner
        pathTooltip2.lineTo(tooltipAnchorPointTopRight,
            tooltipHeadRectHeightPoint) // bottom tooltip right curve
        pathTooltip2.lineTo(tooltipAnchorPointBottom,
            tooltipHeadRectHeightPoint + deltaTooltipHeight) // tooltip anchor tip
        pathTooltip2.lineTo(deltaTooltipAnchorPoint + rightPoint,
            tooltipHeadRectHeightPoint) // bottom tooltip left curve
        pathTooltip2.lineTo(rightPoint, tooltipHeadRectHeightPoint) // bottom left point
        pathTooltip2.close() // enclosing the path.

    }

    private fun drawTooltip(canvas: Canvas) {
        canvas.apply {
            canvas.drawPath(pathTooltip2, tooltipBgPaint)
            val width = ((tooltipHeadRectWidth - (percentageBounds.width())) / 2) + tooltipLeftPoint
            canvas.drawText(percentageText,
                width,
                tooltipTopPoint + padding + context.dpToPx( 8),
                percentagePaint)
            canvas.drawText(completedText,
                tooltipLeftPoint + padding,
                tooltipTopPoint + padding + paddingBetweenText + percentageBounds.height() + context.dpToPx( 8),
                completedPaint)

        }
    }

    private fun drawGraph(canvas: Canvas) {
        canvas.apply {
            for (i in 0 until graphModelList.size) {
                val graphModel = graphModelList[i]
                val graphBarRect = graphModel.graphBarRect
                drawLine(graphBarRect.left,
                    graphBarRect.top,
                    graphBarRect.right,
                    graphBarRect.bottom,
                    mProgressBGPaint)
            }

            for (i in 0 until listGraphValues.size) {
                val graphValue = listGraphValues[i]
                val graphModel = graphModelList[i]
                val graphBarRect = graphModel.graphBarRect
                val splitRect = graphModel.splitRect

                if (graphValue.progress >0 ) {
                    val height = graphBarRect.bottom - graphBarRect.top
                    val tempProgressValue = graphValue.progress*mAnimatedFraction
                    val tempProgress = (((100 - tempProgressValue) / 100) * height)
                    drawLine(graphBarRect.left,
                        graphBarRect.top + tempProgress,
                        graphBarRect.right,
                        graphBarRect.bottom,
                        mProgressPaint)
                }

                if (graphValue.isToday) {
                    val todayText = "TODAY"
                    val todayRect = Rect()
                    mTodayTextPaint.getTextBounds(todayText, 0, todayText.length, todayRect)
                    val textViewWidth = todayRect.width()
                    val deltaA = splitRect.left
                    val deltaB = (splitRect.left + split)
                    val deltaValue = ((deltaB - deltaA) - textViewWidth) / 2
                    drawText(todayText,
                        (deltaA + deltaValue),
                        (graphBarRect.bottom + context.dpToPx( 25)),
                        mTodayTextPaint)
                } else {
                    val dayText = "DAY ${graphValue.day}"
                    val dayRect = Rect()
                    mDayTextPaint.getTextBounds(dayText, 0, dayText.length, dayRect)
                    val textViewWidth = dayRect.width()
                    val deltaA = splitRect.left
                    val deltaB = (splitRect.left + split)
                    val deltaValue = ((deltaB - deltaA) - textViewWidth) / 2
                    drawText(dayText,
                        (deltaA + deltaValue),
                        (graphBarRect.bottom + context.dpToPx( 25)),
                        mDayTextPaint)
                }

                if (graphValue.showToolTip) {
                    percentageText = "${graphValue.progress}%"
                    calculateTooltipValuesV2(percentageText,
                        graphBarRect.left,
                        graphBarRect.top)
                    drawTooltip(this)
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            save()
            drawGraph(this)
            restore()
        }
    }
}


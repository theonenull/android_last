package com.example.easylife.SelfView
//
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.res.TypedArray
//import android.graphics.Rect
//import android.os.Handler
//import android.os.Looper
//import android.text.TextPaint
//import android.text.TextUtils
//import android.util.AttributeSet
//import android.view.GestureDetector
//import android.view.GestureDetector.SimpleOnGestureListener
//import android.view.MotionEvent
//import android.view.animation.LinearInterpolator
//import android.widget.Scroller
//import android.widget.TextView
//import androidx.core.view.GestureDetectorCompat
//import com.example.easylife.R
//import java.lang.StringBuilder
//
//
///**
// * <pre>
// * author : xiaweizi
// * class  : com.xiaweizi.marquee.MarqueeTextView
// * e-mail : 1012126908@qq.com
// * time   : 2017/12/26
// * desc   : 自定义跑马灯
//</pre> *
// */
//@SuppressLint("AppCompatCustomView")
//class MarqueeTextView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyle: Int = 0
//) :
//    TextView(context, attrs, defStyle) {
//    /**
//     * 滚动器
//     */
//    private var mScroller: Scroller? = null
//    /**
//     * 获取滚动一次的时间
//     */
//    /**
//     * 设置滚动一次的时间
//     */
//    /**
//     * 滚动一次的时间
//     */
//    var rndDuration = 0
//    /**
//     * 获取滚动速度
//     * @return
//     */
//    /**
//     * 设置滚动速度
//     * @return
//     */
//    /**
//     * 滚动速度
//     */
//    var rollingSpeed = 0
//
//    /**
//     * 滚动的初始 X 位置
//     */
//    private var mXPaused = 0
//
//    /**
//     * 是否暂停
//     */
//    var isPaused = true
//        private set
//
//    /**
//     * 是否第一次
//     */
//    private var mFirst = true
//    /**
//     * 获取滚动模式
//     */
//    /**
//     * 设置滚动模式
//     */
//    /**
//     * 滚动模式
//     */
//    var scrollMode = 0
//    /**
//     * 获取第一次滚动延迟
//     */
//    /**
//     * 设置第一次滚动延迟
//     */
//    /**
//     * 初次滚动时间间隔
//     */
//    var scrollFirstDelay = 0
//
//    /**
//     * 传入显示的公告列表
//     */
//    private lateinit var listData: ArrayList<String>
//
//    /**
//     * 用来保存公告列表所在的位置 根据内容长度以x轴坐标计算
//     */
//    private lateinit var listDataPos:IntArray
//
//    /**
//     * 手势检测器
//     */
//    private var mGestureDetector: GestureDetectorCompat? = null
//    private var onItemClickListener: OnMarqueeItemClickListener? = null
//    private var showTextData: String? = null
//    private fun initView(context: Context, attrs: AttributeSet?, defStyle: Int) {
//        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeTextView)
//        rndDuration =
//            typedArray.getInt(R.styleable.MarqueeTextView_scroll_interval, ROLLING_INTERVAL_DEFAULT)
//        rollingSpeed =
//            typedArray.getInt(R.styleable.MarqueeTextView_scroll_speed, ROLLING_SPEED_DEFAULT)
//        scrollMode = typedArray.getInt(R.styleable.MarqueeTextView_scroll_mode, SCROLL_FOREVER)
//        scrollFirstDelay = typedArray.getInt(
//            R.styleable.MarqueeTextView_scroll_first_delay,
//            FIRST_SCROLL_DELAY_DEFAULT
//        )
//        typedArray.recycle()
//        setSingleLine()
//        ellipsize = null
//        // 手势监听工具
//        mGestureDetector = GestureDetectorCompat(context, gestureListener)
//    }
//
//    /**
//     * 开始滚动
//     */
//    fun startScroll() {
//        mXPaused = 0
//        isPaused = true
//        mFirst = true
//        if (onItemClickListener != null) {
//            val strings = onItemClickListener!!.initShowTextList()
//            strings?.let { setData(it) }
//        }
//        resumeScroll()
//    }
//
//    /**
//     * 继续滚动
//     */
//    fun resumeScroll() {
//        if (!isPaused) return
//        // 设置水平滚动
//        setHorizontallyScrolling(true)
//
//        // 使用 LinearInterpolator 进行滚动
//        if (mScroller == null) {
//            mScroller = Scroller(this.context, LinearInterpolator())
//            setScroller(mScroller)
//        }
//        val scrollingLen = calculateScrollingLen(showTextData)
//        val distance = scrollingLen - mXPaused
//        // 滚动的时间间隔应该按照内容长短来
//        // final int duration = (Double.valueOf(mRollingInterval * distance * 1.00000 / scrollingLen)).intValue();
//        val duration = java.lang.Double.valueOf(1000 * distance * 1.00000 / rollingSpeed).toInt()
//        if (mFirst) {
//            Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
//                override fun run() {
//                    mScroller!!.startScroll(mXPaused, 0, distance, 0, duration)
//                    invalidate()
//                    isPaused = false
//                }
//            }, scrollFirstDelay.toLong())
//        } else {
//            mScroller!!.startScroll(mXPaused, 0, distance, 0, duration)
//            invalidate()
//            isPaused = false
//        }
//    }
//
//    /**
//     * 暂停滚动
//     */
//    fun pauseScroll() {
//        if (null == mScroller) return
//        if (isPaused) return
//        isPaused = true
//        mXPaused = mScroller!!.currX
//        mScroller!!.abortAnimation()
//    }
//
//    /**
//     * 停止滚动，并回到初始位置
//     */
//    fun stopScroll() {
//        if (null == mScroller) {
//            return
//        }
//        isPaused = true
//        mScroller!!.startScroll(0, 0, 0, 0, 0)
//    }
//
//    /**
//     * 计算滚动的距离
//     *
//     * @return 滚动的距离
//     */
//    private fun calculateScrollingLen(strTxt: String?): Int {
//        if (TextUtils.isEmpty(strTxt)) {
//            return 0
//        }
//        val tp = paint
//        val rect = Rect()
//        tp.getTextBounds(strTxt, 0, strTxt!!.length, rect)
//        return rect.width()
//    }
//
//    /**
//     * 添加显示的公告列表，并
//     *
//     * @param list
//     */
//    fun setData(list: Array<String>?) {
//        listData = list
//        showTextData = getShowTextData()
//        text = showTextData
//    }
//
//    /**
//     * 拼接显示字符串，并计算出每个item所在的位置
//     *
//     * @return
//     */
//    private fun getShowTextData(): String {
//        return if (listData != null && listData!!.size > 0) {
//            listDataPos = IntArray(listData!!.size)
//            val showData = StringBuilder()
//            for (i in listData!!.indices) {
//                showData.append(listData!![i])
//                // 每一条后都添加空格占位符
//                showData.append("\t\t\t\t")
//                listDataPos[i] = calculateScrollingLen(showData.toString())
//            }
//            showData.toString()
//        } else {
//            text.toString()
//        }
//    }
//
//    override fun onScrollChanged(horiz: Int, vert: Int, oldHoriz: Int, oldVert: Int) {
//        super.onScrollChanged(horiz, vert, oldHoriz, oldVert)
//    }
//
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        // 将触摸事件交给手势处理
//        mGestureDetector!!.onTouchEvent(event)
//        return true //继续执行后面的代码
//    }
//
//    override fun computeScroll() {
//        super.computeScroll()
//        if (null == mScroller) return
//        if (mScroller!!.isFinished && !isPaused) {
//            if (scrollMode == SCROLL_ONCE) {
//                stopScroll()
//                return
//            }
//            isPaused = true
//            mXPaused = -1 * width
//            mFirst = false
//            resumeScroll()
//        }
//    }
//
//    fun setOnItemClickListener(onItemClickListener: OnMarqueeItemClickListener?) {
//        this.onItemClickListener = onItemClickListener
//    }
//
//    //GestureDetector.OnDoubleTapListener
//    // 手势监听
//    private val gestureListener: GestureDetector.OnGestureListener =
//        object : SimpleOnGestureListener() {
//            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
//                // 点击事件 获取点击的位置
//                if (onItemClickListener != null) {
//                    val clickX = mScroller!!.currX + e.x
//                    for (i in listDataPos.indices) {
//                        if (clickX >= 0 && clickX <= listDataPos[i]) {
//                            onItemClickListener!!.onClick(i)
//                            break
//                        }
//                    }
//                }
//                return super.onSingleTapConfirmed(e)
//            }
//        }
//
//    /**
//     * 提供一个对外的初始化显示数据集合的方法
//     *
//     * @return 显示点数组
//     */
//    interface OnMarqueeItemClickListener {
//        fun onClick(position: Int)
//        fun initShowTextList(): Array<String>?
//    }
//
//    companion object {
//        /**
//         * 默认滚动时间
//         */
//        private const val ROLLING_INTERVAL_DEFAULT = 10000
//
//        /**
//         * 默认滚动速度:单位（100像素/1000毫秒）
//         */
//        private const val ROLLING_SPEED_DEFAULT = 100
//
//        /**
//         * 第一次滚动默认延迟
//         */
//        private const val FIRST_SCROLL_DELAY_DEFAULT = 1000
//
//        /**
//         * 滚动模式-一直滚动
//         */
//        const val SCROLL_FOREVER = 100
//
//        /**
//         * 滚动模式-只滚动一次
//         */
//        const val SCROLL_ONCE = 101
//    }
//
//    init {
//        initView(context, attrs, defStyle)
//    }
//}
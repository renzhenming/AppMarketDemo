package com.example.appmarket.ui.view;

import com.example.appmarket.R;
import com.example.appmarket.ui.activity.BaseActivity;
import com.example.appmarket.utils.UIUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.*;
import android.widget.ImageButton;
import android.widget.TextView;

public class PagerTab extends ViewGroup {

	private ViewPager mViewPager;
	private PageListener mPageListener = new PageListener();//鐢ㄤ簬娉ㄥ唽缁橵iewPager鐩戝惉鐘舵�鍜屾粴鍔�
	private OnPageChangeListener mDelegatePageListener;//鐢ㄤ簬閫氱煡澶栫晫ViewPager鐨勭姸鎬佸拰婊氬姩
	private BaseActivity mActivity;

	private int mDividerPadding = 12;// 鍒嗗壊绾夸笂涓嬬殑padding
	private int mDividerWidth = 1;// 鍒嗗壊绾跨殑瀹藉害
	private int mDividerColor = 0x1A000000;//鍒嗗壊绾块鑹�
	private Paint mDividerPaint;//鍒嗗壊绾跨殑鐢荤瑪

	private int mIndicatorHeight = 4;//鎸囩ず鍣ㄧ殑楂樺害
	private int mIndicatorWidth;//鎸囩ず鍣ㄧ殑瀹藉害锛屾槸鍔ㄦ�鐨勯殢鐫�ab鐨勫搴﹀彉鍖�
	private int mIndicatorLeft;//鎸囩ず鍣ㄧ殑璺濈宸﹁竟鐨勮窛绂�
	private int mIndicatorColor = 0xFF0084FF;//鎸囩ず鍣ㄩ鑹�
	private Paint mIndicatorPaint; //鎸囩ず鍣ㄧ殑鐢荤瑪

	private int mContentWidth;//璁板綍鑷韩鍐呭鐨勫搴�
	private int mContentHeight;//璁板綍鑷韩鍐呭鐨勯珮搴�

	private int mTabPadding = 24;// tab宸﹀彸鐨勫唴杈硅窛
	private int mTabTextSize = 16; //tab鏂囧瓧澶у皬
	private int mTabBackgroundResId = R.drawable.bg_tab_text;// tab鑳屾櫙璧勬簮
	private int mTabTextColorResId = R.color.tab_text_color; //tab鏂囧瓧棰滆壊
	private int mTabCount;//tab鐨勪釜鏁�

	private int mCurrentPosition = 0;//褰撳墠鍏夋爣鎵�鐨則ab锛岃鍒欐槸浠ュ厜鏍囩殑鏈�乏绔墍鍦ㄧ殑item鐨刾osition
	private float mCurrentOffsetPixels;//鍏夋爣宸﹁竟璺濈褰撳墠鍏夋爣鎵�鐨則ab鐨勫乏杈硅窛绂�
	private int mSelectedPosition = 0; //褰撳墠琚�涓殑tab锛岀敤浜庤褰曟墜鎸囩偣鍑籺ab鐨刾osition

	private boolean mIsBeingDragged = false;//鏄惁澶勪簬鎷栧姩涓�
	private float mLastMotionX;//涓婁竴娆℃墜鎸囪Е鎽哥殑x鍧愭爣
	private VelocityTracker mVelocityTracker;//鐢ㄤ簬璁板綍閫熷害鐨勫府鍔╃被
	private int mMinimumVelocity;//绯荤粺榛樿鐨勬渶灏忔弧瓒砯ling鐨勯�搴�
	private int mMaximumVelocity;//绯荤粺榛樿鏈�ぇ鐨刦ling閫熷害
	private int mTouchSlop;//绯荤粺榛樿婊¤冻婊戝姩鐨勬渶灏忎綅绉�

	private ScrollerCompat mScroller;//澶勭悊婊氬姩鐨勫府鍔╄�
	private int mLastScrollX;//璁板綍涓婁竴娆℃粴鍔ㄧ殑x浣嶇疆锛岃繖鏄敤浜庡鐞唎verScroll锛屽疄闄呬綅缃彲鑳戒細鍙楀埌闄愬埗

	private int mMaxScrollX = 0;// 鎺т欢鏈�ぇ鍙粴鍔ㄧ殑璺濈
	private int mSplitScrollX = 0;// 鏍规嵁item鐨勪釜鏁帮紝璁＄畻鍑烘瘡绉诲姩涓�釜item鎺т欢闇�绉诲姩鐨勮窛绂�

	private EdgeEffectCompat mLeftEdge;//澶勭悊overScroll鐨勫弽棣堟晥鏋�
	private EdgeEffectCompat mRightEdge;

	public PagerTab(Context context) {
		this(context, null);
	}

	public PagerTab(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PagerTab(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (context instanceof BaseActivity) {
			mActivity = (BaseActivity) context;
		}
		init();
		initPaint();
	}

	/** 鍒濆鍖栦竴浜涘父閲�*/
	private void init() {
		//鎶婁竴涓�浠巇ip杞崲鎴恜x
		mIndicatorHeight = UIUtils.dp2px(mIndicatorHeight);
		mDividerPadding = UIUtils.dp2px(mDividerPadding);
		mTabPadding = UIUtils.dp2px(mTabPadding);
		mDividerWidth = UIUtils.dp2px(mDividerWidth);
		mTabTextSize = UIUtils.dp2px(mTabTextSize);
		//鍒涘缓涓�釜scroller
		mScroller = ScrollerCompat.create(mActivity);
		//鑾峰彇涓�釜绯荤粺鍏充簬View鐨勫父閲忛厤缃被
		final ViewConfiguration configuration = ViewConfiguration.get(mActivity);
		//鑾峰彇婊戝姩鐨勬渶灏忚窛绂�
		mTouchSlop = configuration.getScaledTouchSlop();
		//鑾峰彇fling鐨勬渶灏忛�搴�
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		//鑾峰彇fling鐨勬渶澶ч�搴�
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

		mLeftEdge = new EdgeEffectCompat(mActivity);
		mRightEdge = new EdgeEffectCompat(mActivity);
	}

	/** 鍒濆鍖栫瑪 */
	private void initPaint() {
		mIndicatorPaint = new Paint();
		mIndicatorPaint.setAntiAlias(true);
		mIndicatorPaint.setStyle(Paint.Style.FILL);
		mIndicatorPaint.setColor(mIndicatorColor);

		mDividerPaint = new Paint();
		mDividerPaint.setAntiAlias(true);
		mDividerPaint.setStrokeWidth(mDividerWidth);
		mDividerPaint.setColor(mDividerColor);
	}

	/** 璁剧疆ViewPager */
	public void setViewPager(ViewPager viewPager) {
		if (viewPager == null || viewPager.getAdapter() == null) {
			throw new IllegalStateException("ViewPager is null or ViewPager does not have adapter instance.");
		}
		mViewPager = viewPager;
		onViewPagerChanged();
	}

	private void onViewPagerChanged() {
		mViewPager.setOnPageChangeListener(mPageListener);//缁橵iewPager璁剧疆鐩戝惉
		mTabCount = mViewPager.getAdapter().getCount();//鏈夊灏戜釜tab闇�鐪媀iewPager鏈夊灏戜釜椤甸潰
		for (int i = 0; i < mTabCount; i++) {
			if (mViewPager.getAdapter() instanceof IconTabProvider) {//濡傛灉鎯宠浣跨敤icon浣滀负tab锛屽垯闇�adapter瀹炵幇IconTabProvider鎺ュ彛
				addIconTab(i, ((IconTabProvider) mViewPager.getAdapter()).getPageIconResId(i));
			} else {
				addTextTab(i, mViewPager.getAdapter().getPageTitle(i).toString());
			}
		}
		ViewTreeObserver viewTreeObserver = getViewTreeObserver();
		if (viewTreeObserver != null) {//鐩戝惉绗竴涓殑鍏ㄥ眬layout浜嬩欢锛屾潵璁剧疆褰撳墠鐨刴CurrentPosition锛屾樉绀哄搴旂殑tab
			viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					getViewTreeObserver().removeGlobalOnLayoutListener(this);//鍙渶瑕佺洃鍚竴娆★紝涔嬪悗閫氳繃listener鍥炶皟鍗冲彲
					mCurrentPosition = mViewPager.getCurrentItem();
					if (mDelegatePageListener != null) {
						mDelegatePageListener.onPageSelected(mCurrentPosition);
					}
				}
			});
		}
	}

	/** 璁剧疆鐩戝惉锛屽洜涓篢ab浼氱洃鍚琕iewPager鐨勭姸鎬侊紝鎵�互涓嶈缁橵iewPager璁剧疆鐩戝惉浜嗭紝璁剧疆缁橳ab锛岀敱Tab杞彂 */
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mDelegatePageListener = listener;
	}

	/** 娣诲姞鏂囧瓧tab */
	private void addTextTab(final int position, String title) {
		TextView tab = new TextView(mActivity);
		tab.setText(title);
		tab.setGravity(Gravity.CENTER);
		tab.setSingleLine();
		tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
		tab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		tab.setTextColor(UIUtils.getColorStateList(mTabTextColorResId));
		tab.setBackgroundDrawable(UIUtils.getDrawable(mTabBackgroundResId));
		tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		addTab(position, tab);
	}

	/** 娣诲姞鍥剧墖icon */
	private void addIconTab(final int position, int resId) {
		ImageButton tab = new ImageButton(mActivity);
		tab.setImageResource(resId);
		tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addTab(position, tab);
	}

	private void addTab(final int position, View tab) {
		tab.setFocusable(true);
		//璁剧疆tab鐨勭偣鍑讳簨浠讹紝褰搕ab琚偣鍑绘椂鍊欏垏鎹ager鐨勯〉闈�
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(position);
			}
		});
		tab.setPadding(mTabPadding, 0, mTabPadding, 0);
		addView(tab, position);
	}

	/** 娴嬮噺鏃剁殑鍥炶皟 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 鑾峰彇鎺т欢鑷韩鐨勫楂�妯″紡
		int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingBottom();
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int totalWidth = 0;
		int highest = 0;
		int goneChildCount = 0;
		for (int i = 0; i < mTabCount; i++) {
			final View child = getChildAt(i);
			if (child == null || child.getVisibility() == View.GONE) {
				goneChildCount--;
				continue;
			}
			int childWidthMeasureSpec;
			int childHeightMeasureSpec;

			LayoutParams childLayoutParams = child.getLayoutParams();
			if (childLayoutParams == null) {
				childLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			}

			if (childLayoutParams.width == LayoutParams.MATCH_PARENT) {
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
			} else if (childLayoutParams.width == LayoutParams.WRAP_CONTENT) {
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);
			} else {
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.width, MeasureSpec.EXACTLY);
			}

			if (childLayoutParams.height == LayoutParams.MATCH_PARENT) {
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
			} else if (childLayoutParams.height == LayoutParams.WRAP_CONTENT) {
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
			} else {
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.height, MeasureSpec.EXACTLY);
			}

			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			totalWidth += childWidth;
			highest = highest < childHeight ? childHeight : highest;
		}

		if (totalWidth <= widthSize) {//濡傛灉瀛怲ab鐨勬�瀹藉害灏忎簬PagerTab锛屽垯閲囩敤骞冲垎妯″紡
			int splitWidth = (int) (widthSize / (mTabCount - goneChildCount + 0.0f) + 0.5f);
			for (int i = 0; i < mTabCount; i++) {
				final View child = getChildAt(i);
				if (child == null || child.getVisibility() == View.GONE) {
					continue;
				}
				int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(splitWidth, MeasureSpec.EXACTLY);
				int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), MeasureSpec.EXACTLY);
				child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
			}
			mMaxScrollX = 0;
			mSplitScrollX = 0;
		} else {//濡傛灉鎵�湁瀛怴iew澶т簬鎺т欢鐨勫搴�
			mMaxScrollX = totalWidth - widthSize;
			mSplitScrollX = (int) (mMaxScrollX / (mTabCount - goneChildCount - 1.0f) + 0.5f);
		}

		if (widthMode == MeasureSpec.EXACTLY) {
			mContentWidth = widthSize;
		} else {
			mContentWidth = totalWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			mContentHeight = heightSize;
		} else {
			mContentHeight = highest;
		}

		int measureWidth = mContentWidth + getPaddingLeft() + getPaddingRight();
		int measureHeight = mContentHeight + getPaddingTop() + getPaddingBottom();
		setMeasuredDimension(measureWidth, measureHeight);
	}

	/** 甯冨眬鏃剁殑鍥炶皟 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {//杩欓噷绠�寲浜嗭紝娌℃湁鑰冭檻margin鐨勬儏鍐�
		if (changed) {
			int height = b - t;//鎺т欢渚涘瓙View鏄剧ず鐨勯珮搴�
			int left = l;
			for (int i = 0; i < mTabCount; i++) {
				final View child = getChildAt(i);
				if (child == null || child.getVisibility() == View.GONE) {
					continue;
				}
				int top = (int) ((height - child.getMeasuredHeight()) / 2.0f + 0.5f);//濡傛灉鎺т欢姣攖ab瑕侀珮锛屽垯灞呬腑鏄剧ず
				int right = left + child.getMeasuredWidth();
				child.layout(left, top, right, top + child.getMeasuredHeight());//鎽嗘斁tab
				left = right;//鍥犱负鏄按骞虫憜鏀剧殑锛屾墍浠ヤ负涓嬩竴涓噯澶噇eft鍊�
			}
		}
	}

	/** 缁樺埗鏃剁殑鍥炶皟 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		final int height = getHeight();
		//鐢绘寚绀哄櫒
		canvas.drawRect(mIndicatorLeft, height - mIndicatorHeight, mIndicatorLeft + mIndicatorWidth, height, mIndicatorPaint);

		// 鐢诲垎鍓茬嚎
		for (int i = 0; i < mTabCount - 1; i++) {//鍒嗗壊绾跨殑涓暟姣攖ab鐨勪釜鏁板皯涓�釜
			final View child = getChildAt(i);
			if (child == null || child.getVisibility() == View.GONE) {
				continue;
			}
			if (child != null) {
				canvas.drawLine(child.getRight(), mDividerPadding, child.getRight(), mContentHeight - mDividerPadding, mDividerPaint);
			}
		}
		// 鍥犱负overScroll鏁堟灉鏄竴涓寔缁晥鏋滐紝鎵�互闇�鎸佺画鐢�
		boolean needsInvalidate = false;
		if (!mLeftEdge.isFinished()) {//濡傛灉鏁堟灉娌″仠姝�
			final int restoreCount = canvas.save();//鍏堜繚瀛樺綋鍓嶇敾甯�
			final int heightEdge = getHeight() - getPaddingTop() - getPaddingBottom();
			final int widthEdge = getWidth();
			canvas.rotate(270);
			canvas.translate(-heightEdge + getPaddingTop(), 0);
			mLeftEdge.setSize(heightEdge, widthEdge);
			needsInvalidate |= mLeftEdge.draw(canvas);
			canvas.restoreToCount(restoreCount);
		}
		if (!mRightEdge.isFinished()) {
			final int restoreCount = canvas.save();
			final int widthEdge = getWidth();
			final int heightEdge = getHeight() - getPaddingTop() - getPaddingBottom();
			canvas.rotate(90);
			canvas.translate(-getPaddingTop(), -(widthEdge + mMaxScrollX));
			mRightEdge.setSize(heightEdge, widthEdge);
			needsInvalidate |= mRightEdge.draw(canvas);
			canvas.restoreToCount(restoreCount);
		}
		if (needsInvalidate) {
			postInvalidate();
		}
	}

	/** 瑙︽懜浜嬩欢鏄惁鎷︽埅鐨勬柟娉�*/
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		if (mIsBeingDragged && action == MotionEvent.ACTION_MOVE) {//褰撳凡缁忓浜庢嫋鍔紝骞朵笖褰撳墠浜嬩欢鏄疢OVE锛岀洿鎺ユ秷璐规帀
			return true;
		}
		switch (action) {
			case MotionEvent.ACTION_DOWN: {
				final float x = ev.getX();
				mLastMotionX = x; //璁板綍浣忓綋鍓嶇殑x鍧愭爣
				mIsBeingDragged = !mScroller.isFinished();//濡傛灉鎸変笅鐨勬椂鍊欒繕鍦ㄦ粴鍔紝鍒欐妸鐘舵�澶勪簬鎷栧姩鐘舵�
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				final float x = ev.getX();
				final int xDiff = (int) Math.abs(x - mLastMotionX);//璁＄畻涓ゆ鐨勫樊鍊�
				if (xDiff > mTouchSlop) {//濡傛灉澶т簬鏈�皬绉诲姩鐨勮窛绂伙紝鍒欐妸鐘舵�鏀瑰彉涓烘嫋鍔ㄧ姸鎬�
					mIsBeingDragged = true;
					mLastMotionX = x;
					ViewParent parent = getParent();//骞惰姹傜埗View涓嶈鍐嶆嫤鎴嚜宸辫Е鎽镐簨浠讹紝浜ょ粰鑷繁澶勭悊
					if (parent != null) {
						parent.requestDisallowInterceptTouchEvent(true);
					}
				}
				break;
			}
			case MotionEvent.ACTION_CANCEL://褰撴墜鎸囩寮�垨鑰呰Е鎽镐簨浠跺彇娑堢殑鏃跺�锛屾妸鎷栧姩鐘舵�鍙栨秷鎺�
			case MotionEvent.ACTION_UP:
				mIsBeingDragged = false;
				break;
		}
		return mIsBeingDragged;//濡傛灉鏄嫋鍔ㄧ姸鎬侊紝鍒欐嫤鎴簨浠讹紝浜ょ粰鑷繁鐨刼nTouch澶勭悊
	}

	/** 瑙︽懜浜嬩欢鐨勫鐞嗘柟娉�*/
	public boolean onTouchEvent(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		final int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN: {//濡傛灉鏄痙own浜嬩欢锛岃褰曚綇褰撳墠鐨剎鍧愭爣
				final float x = ev.getX();
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
				}
				mLastMotionX = x;
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				final float x = ev.getX();
				final float deltaX = x - mLastMotionX;
				if (!mIsBeingDragged) {//濡傛灉杩樻病鏈夊浜庢嫋鍔紝鍒欏垽鏂袱娆＄殑宸�鏄惁澶т簬鏈�皬鎷栧姩鐨勮窛绂�
					if (Math.abs(deltaX) > mTouchSlop) {
						mIsBeingDragged = true;
					}
				}
				if (mIsBeingDragged) {//濡傛灉澶勪簬鎷栧姩鐘舵�锛岃褰曚綇x鍧愭爣
					mLastMotionX = x;
					onMove(deltaX);
				}
				break;
			}
			case MotionEvent.ACTION_UP: {
				if (mIsBeingDragged) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					//鍏堝閫熷害杩涜涓�釜璋冩暣锛岀涓�釜鍙傛暟鏄椂闂村崟浣嶏紝1000姣锛岀浜屼釜鍙傛暟鏄渶澶ч�搴︺�
					velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
					float velocity = velocityTracker.getXVelocity();//鑾峰彇姘村钩鏂瑰悜涓婄殑閫熷害
					onUp(velocity);
				}
			}
			case MotionEvent.ACTION_CANCEL: {
				mIsBeingDragged = false;
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				break;
			}
		}
		return true;
	}

	private void onMove(float x) {
		if (mMaxScrollX <= 0) {
			if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()) {
				mViewPager.fakeDragBy(x);
			}
		} else {
			int scrollByX = -(int) (x + 0.5);
			if (getScrollX() + scrollByX < 0) {
				scrollByX = 0 - getScrollX();
				mLeftEdge.onPull(Math.abs(x) / getWidth());
			}
			if (getScrollX() + scrollByX > mMaxScrollX) {
				scrollByX = mMaxScrollX - getScrollX();
				mRightEdge.onPull(Math.abs(x) / getWidth());
			}
			scrollBy(scrollByX, 0);
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	private void onUp(float velocity) {
		if (mMaxScrollX <= 0) {
			if (mViewPager.isFakeDragging()) mViewPager.endFakeDrag();
		} else {
			if (Math.abs(velocity) <= mMinimumVelocity) {
				return;
			}
			mScroller.fling(getScrollX(), 0, -(int) (velocity + 0.5), 0, 0, mMaxScrollX, 0, 0, 270, 0);
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int oldX = mLastScrollX;
			mLastScrollX = mScroller.getCurrX();
			if (mLastScrollX < 0 && oldX >= 0) {
				mLeftEdge.onAbsorb((int) mScroller.getCurrVelocity());
			} else if (mLastScrollX > mMaxScrollX && oldX <= mMaxScrollX) {
				mRightEdge.onAbsorb((int) mScroller.getCurrVelocity());
			}
			int x = mLastScrollX;
			if (mLastScrollX < 0) {
				x = 0;
			} else if (mLastScrollX > mMaxScrollX) {
				x = mMaxScrollX;
			}
			scrollTo(x, 0);
		}
		ViewCompat.postInvalidateOnAnimation(this);
	}

	/** 妫�祴mIndicatorOffset鐨勫悎娉曟�锛屽苟璁＄畻鍑哄叾浠栨湁鍏硉ab鐨勫睘鎬у� */
	private void checkAndcalculate() {
		// 濡傛灉鎸囩ず鍣ㄨ捣濮嬩綅缃瘮绗竴涓猼ab鐨勮捣濮嬩綅缃繕瑕佸皬锛岀籂姝ｄ负绗竴涓猼ab鐨勮捣濮嬩綅缃紝鎸囩ず鍣ㄥ搴﹀氨鏄涓�釜tab鐨勫搴�
		final View firstTab = getChildAt(0);
		if (mIndicatorLeft < firstTab.getLeft()) {
			mIndicatorLeft = firstTab.getLeft();
			mIndicatorWidth = firstTab.getWidth();
		}
		// 濡傛灉鎸囩ず鍣ㄨ捣濮嬩綅缃瘮鏈�悗涓�釜tab鐨勮捣濮嬩綅缃繕瑕佸ぇ锛岀籂姝ｄ负鏈�悗涓�釜tab鐨勮捣濮嬩綅缃紝鎸囩ず鍣ㄥ搴﹀氨鏄渶鍚庝竴涓猼ab鐨勫搴�
		View lastTab = getChildAt(mTabCount - 1);
		if (mIndicatorLeft > lastTab.getLeft()) {
			mIndicatorLeft = lastTab.getLeft();
			mIndicatorWidth = lastTab.getWidth();
		}
		// 閫氳繃鎸囩ず鍣ㄧ殑璧峰浣嶇疆璁＄畻鍑哄綋鍓嶅浜庣鍑犱釜position锛屽苟涓旇绠楀嚭宸茬粡鍋忕Щ浜嗗灏戯紝鍋忕Щ閲忔槸浠ュ綋鍓嶆墍澶勭殑tab鐨勫搴︾殑鐧惧垎姣�
		for (int i = 0; i < mTabCount; i++) {
			View tab = getChildAt(i);
			if (mIndicatorLeft < tab.getLeft()) {
				mCurrentPosition = i - 1;
				View currentTab = getChildAt(mCurrentPosition);
				mCurrentOffsetPixels = (mIndicatorLeft - currentTab.getLeft()) / (currentTab.getWidth() + 0.0f);
				break;
			}
		}
	}

	/** 婊氬姩鍒版寚瀹氱殑child */
	public void scrollSelf(int position, float offset) {
		if (position >= mTabCount) {
			return;
		}
		final View tab = getChildAt(position);
		mIndicatorLeft = (int) (tab.getLeft() + tab.getWidth() * offset + 0.5);
		int rightPosition = position + 1;
		if (offset > 0 && rightPosition < mTabCount) {
			View rightTab = getChildAt(rightPosition);
			mIndicatorWidth = (int) (tab.getWidth() * (1 - offset) + rightTab.getWidth() * offset + 0.5);
		} else {
			mIndicatorWidth = tab.getWidth();
		}
		checkAndcalculate();

		int newScrollX = position * mSplitScrollX + (int) (offset * mSplitScrollX + 0.5);
		if (newScrollX < 0) {
			newScrollX = 0;
		}
		if (newScrollX > mMaxScrollX) {
			newScrollX = mMaxScrollX;
		}
		//scrollTo(newScrollX, 0);//婊戝姩
		int duration = 100;
		if (mSelectedPosition != -1) {
			duration = (Math.abs(mSelectedPosition - position)) * 100;
		}
		mScroller.startScroll(getScrollX(), 0, (newScrollX - getScrollX()), 0, duration);
		ViewCompat.postInvalidateOnAnimation(this);
	}

	/** 閫変腑鎸囧畾浣嶇疆鐨凾ab */
	private void selectTab(int position) {
		for (int i = 0; i < mTabCount; i++) {
			View tab = getChildAt(i);
			if (tab != null) {
				tab.setSelected(position == i);
			}
		}
	}

	/** ViewPager鐨凮nPageChangeListener瀹炵幇绫伙紝鍥犱负鎴戜滑闇�鍦≒agerTab涓幏鍙朠agerView鐨勭洃鍚紝浠ヤ究鍙互璋冩暣tab */
	private class PageListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int position, float positionOffset, final int positionOffsetPixels) {
			//鏍规嵁VierPager鐨勫亸绉诲�鏉ユ粴鍔╰ab
			scrollSelf(position, positionOffset);
			if (mDelegatePageListener != null) {//杩欎釜鏄彁渚涚粰澶栭儴鐨�
				mDelegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				mSelectedPosition = -1;
			}
			if (mDelegatePageListener != null) {
				mDelegatePageListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position) {
			System.out.println("onPageSelected:" + position);
			mSelectedPosition = position;
			selectTab(position);
			if (mDelegatePageListener != null) {
				mDelegatePageListener.onPageSelected(position);
			}
		}
	}

	/** 濡傛灉鎸囩ず鍣ㄥ笇鏈涙槸鍥剧墖锛屽垯缁ф壙璇ユ帴鍙�*/
	public interface IconTabProvider {
		public int getPageIconResId(int position);
		public int getPageSelectedIconResId();
	}
}
